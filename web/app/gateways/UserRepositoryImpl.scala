package gateways

import akka.actor.ActorSystem
import akka.stream.alpakka.dynamodb.scaladsl.DynamoDb
import com.github.matsluni.akkahttpspi.AkkaHttpClient
import models._
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model._

import java.net.URI
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import scala.jdk.CollectionConverters.{MapHasAsJava, MapHasAsScala}

class UserRepositoryImpl @Inject() (implicit ec: ExecutionContext, system: ActorSystem) extends UserRepository {

  implicit private val client: DynamoDbAsyncClient = DynamoDbAsyncClient
    .builder()
    .endpointOverride(URI.create("http://localhost:8000"))
    .httpClient(AkkaHttpClient.builder().withActorSystem(system).build())
    .build()

  system.registerOnTermination(client.close())

  override def find(id: UserId): Future[Option[User]] = {

    val keyToGet = Map("user_id" -> AttributeValue.builder().s(id.value.toString).build())

    val request = GetItemRequest
      .builder()
      .tableName("users")
      .key(keyToGet.asJava)
      .build()

    DynamoDb.single(request).map { response =>
      val resMap = response.item().asScala.toMap
      if (response.hasItem) {
        val userId = resMap("user_id").s()
        val name   = resMap("user_name").s()
        val age    = resMap("age").n().toInt

        val user = User(
          id = UserId.from(userId),
          name = FullName(name),
          age = Age(age)
        )
        Some(user)
      } else {
        None
      }
    }
  }

  override def add(user: User): Unit = {
    val itemValues = Map(
      "user_id"   -> AttributeValue.builder().s(user.id.value.toString).build(),
      "user_name" -> AttributeValue.builder().s(user.name.value).build(),
      "age"       -> AttributeValue.builder().n(user.age.value.toString).build()
    )

    val request = PutItemRequest
      .builder()
      .tableName("users")
      .item(itemValues.asJava)
      .build()

    DynamoDb.single(request).foreach(println)
  }

  override def update(user: User): Unit = {
    val itemKey = Map("user_id" -> AttributeValue.builder().s(user.id.value.toString).build())

    val updatedValues = Map(
      "user_name" -> AttributeValueUpdate
        .builder()
        .value(AttributeValue.builder().s(user.name.value).build())
        .action(AttributeAction.PUT)
        .build(),
      "age" -> AttributeValueUpdate
        .builder()
        .value(AttributeValue.builder().n(user.age.value.toString).build())
        .action(AttributeAction.PUT)
        .build()
    )

    val request = UpdateItemRequest
      .builder()
      .tableName("users")
      .key(itemKey.asJava)
      .attributeUpdates(updatedValues.asJava)
      .build()

    DynamoDb.single(request).foreach(println)
  }

  override def delete(userId: UserId): Unit = {
    val keyToGet = Map("user_id" -> AttributeValue.builder().s(userId.value.toString).build())

    val request = DeleteItemRequest
      .builder()
      .tableName("users")
      .key(keyToGet.asJava)
      .build()

    DynamoDb.single(request).foreach(println)
  }
}
