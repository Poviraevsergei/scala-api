name := "scala-api"

version := "0.1"

scalaVersion := "2.13.5"

lazy val `newsletter-api` = project
  .in(file("."))
  .configs(IntegrationTest)
  .settings(
    libraryDependencies ++= dependency.deps,
    Defaults.itSettings,
    IntegrationTest / fork := true
  )

lazy val versions = new {
  val Akka = "2.6.8"
  val AkkaHttp = "10.2.3"
  val JavaxWs = "2.0.1"
  val typesafeConfig = "1.4.1"
}

lazy val dependency = new {

  lazy val deps = akka ++ javaxWs ++ slick ++ typesafeConfig ++ dep

  lazy val dep = Seq(
    "com.typesafe.slick" % "slick-hikaricp_2.13" % "3.3.3",
    "org.postgresql" % "postgresql" % "9.4-1206-jdbc42"
  )

  lazy val akka = Seq(
    "com.typesafe.akka" %% "akka-actor-typed" % versions.Akka,
    "com.typesafe.akka" %% "akka-http" % versions.AkkaHttp,
    "com.typesafe.akka" %% "akka-stream" % versions.Akka,
    "com.typesafe.akka" %% "akka-http-spray-json" % versions.AkkaHttp
  )

  lazy val javaxWs = Seq(
    "javax.ws.rs" % "javax.ws.rs-api" % versions.JavaxWs
  )

  lazy val slick = Seq(
    "com.typesafe.slick" %% "slick" % "3.3.3",
    "mysql" % "mysql-connector-java" % "8.0.23"
  )

  lazy val typesafeConfig = Seq(
    "com.typesafe" % "config" % versions.typesafeConfig
  )
}