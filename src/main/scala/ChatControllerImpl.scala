import User.{PrivateMessage, PublicMessage}
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.cluster.Cluster
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.{Publish, Subscribe}
import javafx.application.Platform
import javafx.event.ActionEvent

import java.net.URL
import java.util.ResourceBundle

class ChatControllerImpl extends ChatController{

  var login: String = _ // var, потому что сюда передается login из окна входа
  var system: ActorSystem = _
  var mediator: ActorRef = _


  override def onSendMessageButton(event: ActionEvent): Unit = {
    val nickname = login // создаю val на основе var login, чтобы не отправлять var
    val message = messageInput.getText.trim
    if (message != "") {
    message match {
    //приватное сообщение /private;получатель; текст
      case input: String if message.contains("/private") =>
        val command = input.split(";").toVector
        val to = command(1)
        val text = command(2)
        this.mediator ! Publish("chat", PrivateMessage(nickname, to, text))

      case _ => this.mediator ! Publish("chat", PublicMessage(nickname, message))
      }
    messageInput.clear()
    }
  }

  override def initialize(url: URL, resourceBundle: ResourceBundle): Unit = {
    this.system = ActorSystem("ClusterSystem")
    val actor = this.system.actorOf(Props(classOf[User], this))
    val cluster = Cluster(system)
    cluster.registerOnMemberUp {
      this.mediator = DistributedPubSub(system).mediator
      this.mediator ! Subscribe("chat", actor)
      messagesField.appendText(s"Вы онлайн как: $login\nЧтобы отправить приватное сообщение используйте команду /private;NicknameПолучателя;Текст сообщения\n\n")
    }

  }



  override def onExitButton(event: ActionEvent): Unit = {
    system.terminate()
    Platform.exit()
    System.exit(0)
  }
}
