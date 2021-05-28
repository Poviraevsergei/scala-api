package db.schema

import db.Db
import model.User
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

import scala.concurrent.Future

trait UserTable {
  this: Db =>

  class Users(tag: Tag) extends Table[User](tag, "users") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name", O.Length(250))

    def * = (id, name) <> (User.tupled, User.unapply)
  }

  val users = TableQuery[Users]

  def createIfNotExist = db.run(users.schema.createIfNotExists)

  def insert(user: User): Future[Int] = db.run(users += user)

  def getAll: Future[Seq[User]] = db.run[Seq[User]](users.result)

  def getById(id: Long): Future[Option[User]] = db.run(users.filter(s => s.id === id).result.headOption)

  def update(user: User): Future[Int] = db.run(users.filter(s => s.id === user.id).update(user))

  def delete(id: Long): Future[Int] = db.run(users.filter(s => s.id === id).delete)
}