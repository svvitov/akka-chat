import User.{PrivateMessage, PublicMessage}
import akka.actor.Actor

object User{

  trait MySerializable
    //сообщения внутри чата
   case class PublicMessage(from: String, text: String) extends MySerializable
   case class PrivateMessage(from: String, to: String, text: String) extends MySerializable
}

class User(chatControllerImpl: ChatControllerImpl) extends Actor{

  override def receive: Receive = {
    case PublicMessage(from, text) => chatControllerImpl.messagesField.appendText(s"[$from]: $text\n")

    case PrivateMessage(from, to, text) => if (from.equals(chatControllerImpl.login) | to.equals(chatControllerImpl.login)) {
        chatControllerImpl.messagesField.appendText(s"[Private] [$from]: $text\n")
      }
  }
}
