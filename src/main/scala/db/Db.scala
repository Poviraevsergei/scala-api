package db

import slick.jdbc.PostgresProfile.api._

trait Db {
  val db = Database.forConfig("database")
}