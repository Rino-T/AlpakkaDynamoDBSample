package di

import com.google.inject.AbstractModule
import gateways.UserRepositoryImpl
import models.UserRepository
import net.codingwell.scalaguice.ScalaModule

class Module extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[UserRepository].to[UserRepositoryImpl]
  }
}
