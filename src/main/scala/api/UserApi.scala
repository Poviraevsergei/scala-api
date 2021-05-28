package api

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import model.User
import service.UserService
import util.ApiPath

import javax.ws.rs._
import javax.ws.rs.core.MediaType
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Path("/user")
class UserApi(userService: UserService)(implicit val ec: ExecutionContext) extends ApiPath {

  val routes: Route = getAll ~ getById ~ create ~ update ~ deleteUser

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  def getAll: Route =
    path("user") {
      get {
        pathEnd {
          val usersFuture: Future[List[User]] = userService.getAll.map(_.toList)

          onComplete(usersFuture) {
            case Success(users) => complete(users)
            case Failure(exception) => complete(StatusCodes.InternalServerError, exception.getMessage)
          }
        }
      }
    }

  @GET
  @Path("/{id}")
  @Produces(Array(MediaType.APPLICATION_JSON))
  def getById: Route =
    pathPrefix("user" / LongNumber) { userId =>
      get {
        pathEnd {
          val usersFuture: Future[Option[User]] = userService.getById(userId)
            onComplete(usersFuture) {
              case Success(maybeUser) => maybeUser match {
                case Some(user) => complete(user)
                case None => complete(StatusCodes.NotFound)
              }
              case Failure(exception) => complete(StatusCodes.InternalServerError, exception.getMessage)
            }
        }
      }
    }

  @POST
  @Produces(Array(MediaType.APPLICATION_JSON))
  def create: Route =
    path("user") {
      post {
        pathEnd {
          entity(as[User]) { user =>
            val usersFuture: Future[User] = userService.insert(user)
              .map(i => user.copy(id = i))
            onComplete(usersFuture) {
              case Success(_) => complete(StatusCodes.Created)
              case Failure(exception) => complete(StatusCodes.InternalServerError, exception.getMessage)
            }
          }
        }
      }
    }

  @PUT
  @Produces(Array(MediaType.APPLICATION_JSON))
  def update: Route =
    path("user") {
      put {
        pathEnd {
          entity(as[User]) { user =>
            val usersFuture: Future[Int] = userService.update(user)

            onComplete(usersFuture) {
              case Success(_) => complete(StatusCodes.Accepted)
              case Failure(exception) =>
                exception match {
                  case e: IllegalArgumentException => complete(StatusCodes.BadRequest, e.getMessage)
                  case e: Throwable => complete(StatusCodes.InternalServerError, e.getMessage)
                }
            }
          }
        }
      }
    }

  @DELETE
  @Path("/{id}")
  def deleteUser: Route =
    pathPrefix("user" / LongNumber) { userId =>
      delete {
        pathEnd {
          val future = userService.delete(userId)
          onComplete(future) {
            case Success(_) => complete(StatusCodes.OK)
            case Failure(exception) => complete(StatusCodes.InternalServerError, exception.getMessage)
          }
        }
      }
    }
}