package com.aka.streams

import com.aka.actors.ChatRoom
import com.aka.actors.ChatRoom.{ClientSentMessage, NewClient}
import akka.NotUsed
import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.OverflowStrategy
import akka.stream.scaladsl.{Flow, Sink, Source}
import scala.concurrent.Future
import akka.stream.Materializer
import scala.concurrent.ExecutionContext.Implicits.global

object WebSocketFlow {
  def create(chatRoom: ActorRef)(implicit actorSystem: ActorSystem): Flow[Message,Message,NotUsed] = {
    Flow[Message].mapAsync(4) {
      // Properly handle TextMessage to extract the text
      case tm: TextMessage => tm.textStream.runFold("")(_ + _)
      case _ => Future.successful("Unsupported message type")
    }.map { msg =>
      // Send the extracted message to the chat room for broadcasting
      chatRoom ! ChatRoom.ClientSentMessage(msg)
      TextMessage(msg) // Echo the message back to the sender (for now)
    }.via(Flow.fromSinkAndSourceCoupled(
      Sink.ignore, // Ignore messages for now
      Source.actorRef[Message](bufferSize = 10, OverflowStrategy.dropHead)
        .mapMaterializedValue { outActor =>
          chatRoom ! ChatRoom.NewClient(outActor)
          NotUsed
        }
    ))
  }
}
