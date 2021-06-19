package models

import scala.concurrent.Future

trait UserRepository {

  def find(id: UserId): Future[Option[User]]

  def add(user: User): Unit

  def delete(userId: UserId): Unit
}
