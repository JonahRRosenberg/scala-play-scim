package models

import anorm._
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import play.api.db.DB
import play.api.Play.current

case class User(id: String, meta: Meta, userName: String, displayName: String)

object User {
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

  def create(userName: String, displayName: String): Option[Long] = {
    DB.withConnection { implicit c =>
      SQL(
        """INSERT INTO users (userName, displayName, created, lastModified)
           VALUES ({userName}, {displayName}, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP())
        """).on(
        'userName -> userName,
        'displayName -> displayName
      ).executeInsert().map(id => id)
    }
  }
}
