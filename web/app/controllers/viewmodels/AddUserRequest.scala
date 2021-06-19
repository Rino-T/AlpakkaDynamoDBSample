package controllers.viewmodels

import play.api.libs.json.{JsResult, Json, OFormat}
import play.api.mvc.{BodyParser, PlayBodyParsers}
import play.api.mvc.Results.BadRequest

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

case class AddUserRequest(
    name: String,
    age: Int
)

object AddUserRequest {
  implicit val jsonFormat: OFormat[AddUserRequest] = Json.format[AddUserRequest]

  def validateJson(parsers: PlayBodyParsers)(implicit ec: ExecutionContext): BodyParser[AddUserRequest] = {
    parsers.json.validate { jsValue =>
      Try(jsValue.validate[AddUserRequest]) match {
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
