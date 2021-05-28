package service

import db.schema.GroupTable
import model.Group

import scala.concurrent.Future

class GroupService(groupTable: GroupTable) {
  def getAll: Future[Seq[Group]] = {
    groupTable.getAll
  }

  def insert(group: Group): Future[Int] = groupTable.insert(group)

  def getById(id: Long): Future[Option[Group]] = groupTable.getById(id)

  def update(group: Group): Future[Int] = groupTable.update(group)

  def delete(id: Long): Future[Int] = groupTable.delete(id)
}