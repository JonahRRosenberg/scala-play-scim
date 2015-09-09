package models

import java.sql.SQLException

import anorm._
import play.api.db.DB
import play.api.Play.current

case class User(id: String, meta: Meta, userName: String, displayName: String)

object User {
  def getAll: List[User] = {
    DB.withConnection { implicit c =>
      SQL("SELECT * FROM users").apply().map(
        row => User(
          row[Int]("id").toString(),
          MetaObj.apply(row),
          row[String]("userName"),
          row[String]("displayName"))).toList
    }
  }

  def get(id: Long): User = {
    DB.withConnection { implicit c =>
      val rows = SQL("SELECT * FROM users WHERE id={id}").on(
        'id -> id
      ).apply()

      if (rows.length <= 0) {
        return null
      } else {
        val firstRow = rows.head
        return new User(
          firstRow[Int]("id").toString(),
          MetaObj.apply(firstRow),
          firstRow[String]("userName"),
          firstRow[String]("displayName"))
      }
    }
  }

  def delete(id: Long): Boolean = {
    DB.withConnection { implicit c =>
      SQL("DELETE FROM users WHERE id={id}").on(
        'id -> id
      ).executeUpdate() > 0
    }
  }

  def create(userName: String, displayName: String): Option[Long] = {
    DB.withConnection { implicit c =>
      try {
        SQL(
          """INSERT INTO users (userName, displayName, created, lastModified)
             VALUES ({userName}, {displayName}, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP())
          """).on(
            'userName -> userName,
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

  def update(id: Long, userName: String, displayName: String): Boolean = {
    DB.withConnection { implicit c =>
      SQL(
        """UPDATE users SET
           userName={userName},
           displayName={displayName},
           lastModified=CURRENT_TIMESTAMP
           WHERE id={id}
        """).on(
          'userName -> userName,
          'displayName -> displayName,
          'id -> id
        ).executeUpdate() > 0
    }
  }
}
