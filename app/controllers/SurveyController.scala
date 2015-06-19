package controllers

import models.Survey
import play.api.mvc._
import play.api.libs.json.Json
import controllers.responses._
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.Future

/**
 * Created by: MAXIRU 
 * Date: 6/17/15
 * Time: 1:38 PM
 */
class SurveyController extends Controller {

  def list = Action.async { request =>
    val eventFuture: Future[Seq[Survey]] = Survey.list

    val response = eventFuture.map { events =>
      Ok(Json.toJson(SuccessResponse(events)))
    }

    response
  }

  def getByID(surveyId: Long) = Action.async { request =>
    val surveyFuture: Future[Option[Survey]] = Survey.getById(surveyId)
    surveyFuture.map { survey =>
      survey.fold {
        NotFound(Json.toJson(ErrorResponse(NOT_FOUND, "No survey found")))
      } { e =>
        Ok(Json.toJson(SuccessResponse(e)))
      }
    }
  }

  def create = Action.async(parse.json) { request =>
    // parse from json post body

    val incomingBody = request.body.validate[Survey]

    incomingBody.fold(
      error => {
        val errorMessage = s"Invalid JSON: $error"
        val response = ErrorResponse(ErrorResponse.INVALID_JSON, errorMessage)
        Future.successful(BadRequest(Json.toJson(response)))
      }, survey => {
        // save survey and get a copy back
        val createdSurveyFuture: Future[Survey] = Survey.create(survey)
        createdSurveyFuture.map { createdSurvey =>
          Created(Json.toJson(SuccessResponse(createdSurvey)))
        }
      }
    )

  }
}
