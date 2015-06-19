package controllers.responses

import play.api.libs.json.{Format, Json , JsValue , JsNull , Writes}

/**
 * Created by: MAXIRU 
 * Date: 6/17/15
 * Time: 1:44 PM
 */

case class ErrorResult(status: Int, message: String)
object ErrorResult {
  implicit val format: Format[ErrorResult] = Json.format[ErrorResult]
}

case class EndpointResponse(
                             result: String,
                             response: JsValue,
                             error: Option[ErrorResult]
                             )
object EndpointResponse {
  implicit val format: Format[EndpointResponse] = Json.format[EndpointResponse]
}

object ErrorResponse {
  val INVALID_JSON = 1000
  def apply(status: Int, message: String) = {
    EndpointResponse("ko", JsNull, Option(ErrorResult(status, message)))
  }
}

object SuccessResponse {
  def apply[A](successResponse: A)(implicit w: Writes[A]) = {
    EndpointResponse("ok", Json.toJson(successResponse), None)
  }
}