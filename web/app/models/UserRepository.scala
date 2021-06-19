package models

import scala.concurrent.Future

trait UserRepository {
  def find(id: UserId): Future[Option[User]]
}
