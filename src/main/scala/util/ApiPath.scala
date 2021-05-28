package util

import akka.http.scaladsl.server.{Directives, Route}
import akka.util.Timeout
import model.ModelJsonSupport

import scala.concurrent.duration.DurationInt

trait ApiPath extends Directives with ModelJsonSupport {

  implicit val timeout: Timeout = Timeout(2.seconds)

  def routes: Route
}