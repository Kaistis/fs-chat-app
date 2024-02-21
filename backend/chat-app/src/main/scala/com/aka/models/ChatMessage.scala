package com.aka.models

import slick.jdbc.PostgresProfile.api._

case class ChatMessage(id: Long = 0L, username: String, message: String, timestamp: Long)

class ChatMessages(tag: Tag) extends Table[ChatMessage](tag, "chat_messages") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def username = column[String]("username")
  def message = column[String]("message")
  def timestamp = column[Long]("timestamp")

  def * = (id, username, message, timestamp) <> (ChatMessage.tupled, ChatMessage.unapply)
}
