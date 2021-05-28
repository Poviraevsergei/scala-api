package db.schema

import db.Db
import model.Group
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag

import scala.concurrent.Future

trait GroupTable {
  this: Db =>

  class Groups(tag: Tag) extends Table[Group](tag, "groups") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("group_name", O.Length(250), O.Unique)

    def * = (id, name) <> (Group.tupled, Group.unapply)
  }

  val groups = TableQuery[Groups]

  def createIfNotExist = db.run(groups.schema.createIfNotExists)

  def insert(group: Group): Future[Int] = db.run(groups += group)

  def getAll: Future[Seq[Group]] = db.run[Seq[Group]](groups.result)

  def getById(id: Long): Future[Option[Group]] = db.run(groups.filter(s => s.id === id).result.headOption)

  def update(group: Group): Future[Int] = db.run(groups.filter(s => s.id === group.id).update(group))

  def delete(id: Long): Future[Int] = db.run(groups.filter(s => s.id === id).delete)
}