package models

import java.util.UUID

case class User(
    id: UserId,
    name: FullName,
    age: Age
)

case class UserId(value: UUID)
object UserId {
  def from(str: String): UserId = UserId(UUID.fromString(str))
  def generate: UserId          = UserId(UUID.randomUUID())
}
case class FullName(value: String)
case class Age(value: Int)
