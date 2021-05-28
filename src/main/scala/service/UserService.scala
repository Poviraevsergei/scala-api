package service

import db.schema.UserTable
import model.User

import scala.concurrent.Future

class UserService(userTable: UserTable) {

  def getAll: Future[Seq[User]] = {
    userTable.getAll
  }

  def insert(user: User): Future[Int] = userTable.insert(user)

  def getById(id: Long): Future[Option[User]] = userTable.getById(id)

  def update(user: User): Future[Int] = userTable.update(user)

  def delete(id: Long): Future[Int] = userTable.delete(id)
}