package controllers.viewmodels

import play.api.libs.json.{Json, OFormat}
import play.api.mvc.Results.BadRequest
import play.api.mvc.{BodyParser, PlayBodyParsers}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

case class UpdateUserRequest(
    name: Option[String],
    age: Option[Int]
)

object UpdateUserRequest {
  implicit val jsonFormat: OFormat[UpdateUserRequest] = Json.format[UpdateUserRequest]

  def validateJson(parsers: PlayBodyParsers)(implicit ec: ExecutionContext): BodyParser[UpdateUserRequest] = {
    parsers.json.validate { jsValue =>
      Try(jsValue.validate[UpdateUserRequest]) match {
        case Failure(e) => Left(BadRequest(e.getMessage))
        case Success(request) =>
          request.asEither match {
            case Left(e)    => Left(BadRequest("json format error"))
            case Right(req) => Right(req)
          }
      }
    }
  }
}
