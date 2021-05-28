package util

import akka.http.scaladsl.server.Route

class Paths(apiPaths: Seq[ApiPath]) extends ApiPath {

  override def routes: Route = concat(apiPaths.map(_.routes): _*)
}