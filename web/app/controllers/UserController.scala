package controllers

import models.{UserId, UserRepository}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import javax.inject.Inject
import scala.concurrent.ExecutionContext

class UserController @Inject() (cc: ControllerComponents, userRepository: UserRepository)(implicit ec: ExecutionContext)
    extends AbstractController(cc) {

  def find(userId: String): Action[AnyContent] = Action.async { implicit request =>
    val id = UserId.from(userId)
    userRepository.find(id).map {
      case Some(user) => Ok(user.toString)
      case None       => NotFound
    }
  }
}
