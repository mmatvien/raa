package models

/**
 * Created by: MAXIRU 
 * Date: 6/17/15
 * Time: 1:33 PM
 */

import org.joda.time.DateTime
import play.api.libs.json.{Json, Format}
import play.api.db.slick.DatabaseConfigProvider
import play.api.Play.current
import slick.driver.JdbcProfile
import SlickMapping.jodaDateTimeMapping
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._

case class Survey(
                   id: Option[Long],
                   name: String,
                   zip: String,
                   created: DateTime,
                   email: Option[String],
                   q1: String,
                   q2: String,
                   q3: String
                   )

object Survey {
  implicit val format: Format[Survey] = Json.format[Survey]

  protected val dbConfig = DatabaseConfigProvider.get[JdbcProfile](current)
  import dbConfig._
  import dbConfig.driver.api._

  class SurveysTable(tag: Tag) extends Table[Survey](tag, "SURVEYS") {

    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME")
    def zip = column[String]("ZIP")
    def created = column[DateTime]("CREATED")
    def email = column[String]("EMAIL")
    def q1 = column[String]("Q1")
    def q2 = column[String]("Q2")
    def q3 = column[String]("Q3")

    def * = (id.?, name, zip, created, email.?, q1, q2, q3) <> ((Survey.apply _).tupled, Survey.unapply)
  }

  val table = TableQuery[SurveysTable]


  def list: Future[Seq[Survey]] = {
    val surveyList = table.result
    db.run(surveyList)
  }

  def getById(surveyId: Long): Future[Option[Survey]] = {
    val surveyByID = table.filter { f =>
      f.id === surveyId
    }.result.headOption

    db.run(surveyByID)
  }

  def create(survey: Survey): Future[Survey] = {
    val insertion = (table returning table.map(_.id)) += survey

    val insertedIDFuture = db.run(insertion)

    val createdCopy: Future[Survey] = insertedIDFuture.map { resultID =>
      survey.copy(id = Option(resultID))
    }

    createdCopy
  }
}

