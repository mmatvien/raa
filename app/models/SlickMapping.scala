package models

/**
 * Created by: MAXIRU 
 * Date: 6/17/15
 * Time: 2:38 PM
 */

import org.joda.time.DateTime
import java.sql.Timestamp
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import play.api.Play.current

object SlickMapping {
  protected val dbConfig = DatabaseConfigProvider.get[JdbcProfile](current)
  import dbConfig._
  import dbConfig.driver.api._

  implicit val jodaDateTimeMapping = {
    MappedColumnType.base[DateTime, Timestamp](
      dt => new Timestamp(dt.getMillis),
      ts => new DateTime(ts))
  }
}
