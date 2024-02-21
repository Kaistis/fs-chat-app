package com.aka.actors

import akka.actor.{Actor, ActorRef, Props, Terminated}
import akka.http.scaladsl.model.ws.TextMessage
import com.aka.service.DatabaseService
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
import play.api.libs.json.Json
import com.aka.model.ChatMessage
import com.aka.model.IncomingMessage

object ChatRoom {
  def props(): Props = Props(new ChatRoom)

  // Messages that the ChatRoom actor can receive
  case class NewClient(client: ActorRef)
  case class ClientSentMessage(msg: String)
  case class ClientDisconnected(client: ActorRef)
}

class ChatRoom extends Actor {
  import ChatRoom._

  // Assuming you have a JSON format for ChatMessage
  implicit val chatMessageFormat: play.api.libs.json.OFormat[com.aka.model.ChatMessage] = Json.format[ChatMessage]

  private var clients: Set[ActorRef] = Set.empty

  def receive: Receive = {
    case NewClient(client) =>
      clients += client
      context.watch(client) // Watch for termination of this client
      sendHistoryTo(client)

    case ClientSentMessage(jsonMsg) =>
      // Assuming jsonMsg is a JSON string containing the message and possibly other data
      val incomingMessage = Json.parse(jsonMsg).as[IncomingMessage]  // Parse the incoming JSON message
      val chatMessage = ChatMessage(0L, incomingMessage.username, incomingMessage.message, System.currentTimeMillis())
      
      DatabaseService.insertMessage(chatMessage).onComplete {
        case Success(savedMessage) =>
          val messageJson = Json.toJson(savedMessage).toString()  // Convert the saved ChatMessage object to JSON string
          broadcast(messageJson)  // Broadcast the JSON string including the timestamp
        case Failure(e) =>
          println(s"Failed to insert message: $e")
      }

    case Terminated(client) =>
      clients -= client
  }

  private def sendHistoryTo(client: ActorRef): Unit = {
    DatabaseService.fetchMessages().onComplete {
      case Success(messages) =>
        // Convert the Seq[JsValue] to a JSON array and then to a string
        val historyJson = Json.toJson(messages)
        client ! TextMessage.Strict(historyJson.toString())
      case Failure(e) =>
        println(s"Failed to fetch message history: $e")
    }
  }

  private def broadcast(msg: String): Unit = {
    clients.foreach(_ ! TextMessage.Strict(msg))
  }
}
