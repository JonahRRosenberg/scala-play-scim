package models

import anorm._
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat

case class Meta(created: String, lastModified: String) {
}

// Need different identifier name due to outstanding bug in play framework:
// https://github.com/playframework/playframework/issues/2031
object MetaObj {
  def apply(row: Row) = {
    def convertDateTime(row: Row, field: String) = {
      row[DateTime](field).toString(ISODateTimeFormat.dateTime())
    }
    new Meta(convertDateTime(row, "created"), convertDateTime(row, "lastModified"))
  }
}
