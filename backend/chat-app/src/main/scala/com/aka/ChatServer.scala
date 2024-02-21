import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import scala.io.StdIn
import com.aka.actors.ChatRoom
import com.aka.streams.WebSocketFlow
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings
import com.aka.service.DatabaseService
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}

object ChatServer {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("chat-app-system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val chatRoom = system.actorOf(ChatRoom.props(), "chatRoom")
    val settings = CorsSettings.defaultSettings.withAllowGenericHttpRequests(false)
    val route = cors(settings) { // Wrap your route with the CORS directive
      path("chat") {
        get {
          handleWebSocketMessages(WebSocketFlow.create(chatRoom))
        }
      }
    }
    // Initialize database
    DatabaseService.init().onComplete {
      case Success(_) => println("Database initialized successfully")
      case Failure(exception) => println(s"Database initialization failed: ${exception.getMessage}")
    }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
    println("Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}
