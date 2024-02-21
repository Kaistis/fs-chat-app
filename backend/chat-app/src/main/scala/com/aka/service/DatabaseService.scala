package com.aka.service

import com.aka.model.{ChatMessage, ChatMessages}
import com.aka.model.IncomingMessage
import play.api.libs.json.{JsValue, Json}

import slick.jdbc.PostgresProfile.api._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import com.typesafe.config.ConfigFactory
import slick.jdbc.JdbcBackend.Database

object DatabaseService {
  val db = Database.forConfig("db")
  val chatMessages = TableQuery[ChatMessages]

  def init(): Future[Unit] = db.run(chatMessages.schema.createIfNotExists)

  def insertMessage(chatMessage: ChatMessage): Future[ChatMessage] = {
    val now = System.currentTimeMillis()
    // Create the insert query
    val insertQuery = chatMessages returning chatMessages.map(_.id) into ((_, id) => chatMessage.copy(id = id, timestamp = now))

    // Insert the new message into the database
    db.run(insertQuery += chatMessage)
  }

  def fetchMessages(limit: Int = 50, offset: Int = 0): Future[Seq[JsValue]] = {
  val query = chatMessages
    .sortBy(_.timestamp.asc)
    .drop(offset)
    .take(limit)
    .result
    .map(_.map { msg =>
      Json.obj(
        "id" -> msg.id,
        "username" -> msg.username,
        "message" -> msg.message,
        "timestamp" -> msg.timestamp
      )
    })

  db.run(query).recover {
    case ex: Exception => Seq(Json.obj("error" -> ex.getMessage))
  }
}
}
