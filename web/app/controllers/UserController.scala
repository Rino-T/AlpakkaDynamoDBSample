package controllers

import controllers.viewmodels.{AddUserRequest, UpdateUserRequest}
import models.{Age, FullName, User, UserId, UserRepository}
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

  def add: Action[AddUserRequest] = Action(AddUserRequest.validateJson(cc.parsers)) { implicit request =>
    val name = FullName(request.body.name)
    val age  = Age(request.body.age)
    val user = User(UserId.generate, name, age)
    userRepository.add(user)
    Ok
  }

  def update(id: String): Action[UpdateUserRequest] = Action.async(UpdateUserRequest.validateJson(cc.parsers)) {
    implicit request =>
      val userId = UserId.from(id)
      userRepository.find(userId).map {
        case None => NotFound
        case Some(user) =>
          val name        = request.body.name.map(FullName.apply)
          val age         = request.body.age.map(Age.apply)
          val updatedUser = user.update(name, age)
          userRepository.update(updatedUser)
          Accepted
      }
  }

  def delete(id: String): Action[AnyContent] = Action { implicit request =>
    val userId = UserId.from(id)
    userRepository.delete(userId)
    NoContent
  }
}
