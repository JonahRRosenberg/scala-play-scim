package controllers

import models._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def addScimVersion(obj: JsValue): JsObject = {
    return Json.obj("schemas" -> Json.arr(JsString("urn:scim:schemas:core:1.0"))).
      as[JsObject].deepMerge(obj.as[JsObject])
  }

  def getUser(id: Long) = Action {
    User.get(id) match {
      case user: User => Ok(addScimVersion(Json.toJson(user)))
      case _ => NotFound("Resource " + id + " not found.")
    }
  }

  def deleteUser(id: Long) = Action {
    if (User.delete(id))
      Ok("")
    else
      NotFound("Resource " + id + " not found.")
  }

  def newUser = Action(parse.json) { request =>
    request.body.validate[(String, String)].map{
      case (userName, displayName) => {
        User.create(userName, displayName) match {
          case Some(id) =>
            User.get(id) match {
              case user: User => Created(addScimVersion(Json.toJson(user)))
              case _ => NotFound("Error: Unable to get created resource")
            }
          case None =>
            Conflict("Duplicate username")
        }
      }
    }.recoverTotal {
      e => BadRequest("Detected error: " + JsError.toFlatJson(e))
    }
  }

  def updateUser(id: Long) = Action(parse.json) { request =>
    request.body.validate[(String, String)].map{
      case (userName, displayName) => {
        if (User.update(id, userName, displayName)) {
          User.get(id) match {
            case user: User => Ok(addScimVersion(Json.toJson(user)))
            case _ => NotFound("Error: Unable to get updated resource")
          }
        }
        else {
          Conflict("Unable to update resource with id: " + id)
        }
      }
    }.recoverTotal {
      e => BadRequest("Detected error: " + JsError.toFlatJson(e))
    }
  }

  implicit val rds = (
    (__ \ 'userName).read[String] and
      (__ \ 'displayName).read[String]
    ) tupled

  implicit val metaReads = Json.reads[Meta]
  implicit val metaWrites = Json.writes[Meta]
  implicit val userReads = Json.reads[User]
  implicit val userWrites = Json.writes[User]
}