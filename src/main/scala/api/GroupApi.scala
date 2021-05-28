package api

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import model.Group
import service.GroupService
import util.ApiPath

import javax.ws.rs._
import javax.ws.rs.core.MediaType
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Path("/group")
class GroupApi(groupService: GroupService)(implicit val ec: ExecutionContext) extends ApiPath {

  val routes: Route = getAll ~ getById ~ create ~ update ~ deleteGroup

  @GET
  @Produces(Array(MediaType.APPLICATION_JSON))
  def getAll: Route =
    path("group") {
      get {
        pathEnd {
          val groupsFuture: Future[List[Group]] = groupService.getAll.map(_.toList)
          onComplete(groupsFuture) {
            case Success(groups) => complete(groups)
            case Failure(exception) => complete(StatusCodes.InternalServerError, exception.getMessage)
          }
        }
      }
    }

  @GET
  @Path("/{id}")
  @Produces(Array(MediaType.APPLICATION_JSON))
  def getById: Route =
    pathPrefix("group" / LongNumber) { groupId =>
      get {
        pathEnd {
          val groupsFuture: Future[Option[Group]] = groupService.getById(groupId)
          onComplete(groupsFuture) {
            case Success(maybeGroup) => maybeGroup match {
              case Some(group) => complete(group)
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
    path("group") {
      post {
        pathEnd {
          entity(as[Group]) { group =>
            val groupsFuture: Future[Group] = groupService.insert(group)
              .map(i => group.copy(id = i))
            onComplete(groupsFuture) {
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
    path("group") {
      put {
        pathEnd {
          entity(as[Group]) { group =>
            val groupsFuture: Future[Int] = groupService.update(group)
            onComplete(groupsFuture) {
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
  def deleteGroup: Route =
    pathPrefix("group" / LongNumber) { groupId =>
      delete {
        pathEnd {
          val future = groupService.delete(groupId)
          onComplete(future) {
            case Success(_) => complete(StatusCodes.OK)
            case Failure(exception) => complete(StatusCodes.InternalServerError, exception.getMessage)
          }
        }
      }
    }
}