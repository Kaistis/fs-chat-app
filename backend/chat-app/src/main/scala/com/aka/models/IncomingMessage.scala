package com.aka.models

import play.api.libs.json.{Json, OFormat}

case class IncomingMessage(username: String, message: String)

object IncomingMessage {
  implicit val format: OFormat[IncomingMessage] = Json.format[IncomingMessage]
}