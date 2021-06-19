import akka.actor.ActorSystem
import akka.stream.alpakka.dynamodb.scaladsl.DynamoDb
import akka.util.Timeout
import com.github.matsluni.akkahttpspi.AkkaHttpClient
import com.typesafe.config.{Config, ConfigFactory}
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.ListTablesRequest

import java.net.URI
import scala.concurrent.ExecutionContextExecutor

object Main {

  def main(args: Array[String]): Unit = {

    val config = ConfigFactory.load()
    val host   = config.getString("http.host")
    val port   = config.getInt("http.port")

    implicit val system: ActorSystem          = ActorSystem()
    implicit val ec: ExecutionContextExecutor = system.dispatcher

    implicit val client: DynamoDbAsyncClient = DynamoDbAsyncClient
      .builder()
      .endpointOverride(URI.create("http://localhost:8000"))
      .httpClient(AkkaHttpClient.builder().withActorSystem(system).build())
      .build()

    system.registerOnTermination(client.close())

    val listTablesResult = DynamoDb.single(ListTablesRequest.builder().build())
    listTablesResult.foreach(println)
  }

}
