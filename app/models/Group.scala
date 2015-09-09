package models

import java.sql.SQLException

import anorm._
import play.api.db.DB
import play.api.Play.current

case class Group(id: String, meta: Meta, displayName: String, members: List[UserRef])
case class UserRef(value: String, displayName: String)

object Group {
  def getAll: List[Group] = {
    DB.withConnection { implicit c =>
      val rows = SQL("SELECT * FROM groups").apply()

      rows.map(row => {
        val groupId = row[Long]("id")
        val users = SQL(
          """SELECT map.userid, u.displayName
             FROM users_groups_mapping map
             JOIN users u on u.id = map.userid
             WHERE groupid={groupid}""").on(
          'groupid -> groupId
        ).apply().map(row => UserRef(
          row[Long]("userid").toString,
          row[String]("displayName"))).toList
        Group(groupId.toString, MetaObj.apply(row), row[String]("displayName"), users)
      }).toList
    }
  }

  def get(id: Long): Group = {
    DB.withConnection { implicit c =>
      val rows = SQL("SELECT * FROM groups WHERE id={id}").on(
        'id -> id
      ).apply()

      if (rows.length <= 0) {
        return null
      } else {
        val firstRow = rows.head
        return new Group(
          firstRow[Int]("id").toString(),
          MetaObj.apply(firstRow),
          firstRow[String]("displayName"),
          List[UserRef]())
      }
    }
  }

  def create(displayName: String): Option[Long] = {
    DB.withConnection { implicit c =>
      try {
        SQL(
          """INSERT INTO groups (displayName, created, lastModified)
             VALUES ({displayName}, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP())
          """).on(
            'displayName -> displayName
          ).executeInsert().map(id => id)
      } catch {
        // This exception handling would need to be more robust
        case e: SQLException =>
          println("SqlException: " + e.getStackTrace)
          None
      }
    }
  }
}
