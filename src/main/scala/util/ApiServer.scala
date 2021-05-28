package util

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import api.{GroupApi, UserApi}
import db.Db
import db.schema.{GroupTable, UserTable}
import service.{GroupService, UserService}

import scala.concurrent.ExecutionContext
import scala.io.StdIn

object ApiServer {
  implicit val actorSystem: ActorSystem[Any] = ActorSystem(Behaviors.empty, "scala-api")

  implicit val ec: ExecutionContext = actorSystem.executionContext

  val userTable = new UserTable with Db
  userTable.createIfNotExist
  val userService = new UserService(userTable)
  val userApi = new UserApi(userService)


  val groupTable = new GroupTable with Db
  groupTable.createIfNotExist
  val groupService = new GroupService(groupTable)
  val groupApi = new GroupApi(groupService)
  val paths = new Paths(Seq(groupApi, userApi))

  def run = {
    val bindFuture = Http().newServerAt("localhost", 8080)
      .bind(paths.routes)
    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")

    StdIn.readLine()

    bindFuture
      .flatMap(_.unbind())
      .onComplete(_ => actorSystem.terminate())
  }
}