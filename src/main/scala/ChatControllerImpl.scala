import User.{Bye, PrivateMessage, PublicMessage}
import akka.actor.{ActorRef, ActorSystem, Address, AddressFromURIString, Props}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.{InitialStateAsEvents, MemberEvent, UnreachableMember}
import com.typesafe.config.ConfigFactory
import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.Parent
import javafx.stage.Stage

import java.net.URL
import java.util.ResourceBundle

class ChatControllerImpl extends ChatController {

  var login: String = _
  var system: ActorSystem = _


  var usersOnlineRefs: IndexedSeq[ActorRef] = IndexedSeq.empty[ActorRef]

  @FXML
  override  def onSendMessageButton(event: ActionEvent): Unit = {
    val nickname = login //чтобы не отправлять var

    if (!(messageInput.getText == "")) {
      val yourMessage = messageInput.getText.trim
      messageInput.clear()

      yourMessage match {
        // /private; nickname ; text
        case string: String if yourMessage.contains("/private") =>
          val s2 = string.split(";").toVector
          val destination = s2(1)
          val text = s2(2).mkString
          usersOnlineRefs.foreach(_ ! PrivateMessage(nickname, destination, text))

        case _ => usersOnlineRefs.foreach(_ ! PublicMessage(nickname, yourMessage))

      }
    }
  }

  @FXML
  override  def onExitButton(event: ActionEvent): Unit = {
    val nickname = login //чтобы не отправлять var
    usersOnlineRefs.foreach(_ ! Bye(nickname))
    system.terminate()
    Platform.exit()
    System.exit(0)
  }

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    startCluster()
  }

  def startCluster(): Unit = {
    val conf = ConfigFactory.load("application.conf")
    this.system = ActorSystem("ClusterSystem", conf)
    val actor = system.actorOf(Props(classOf[User], this))
    val cluster = Cluster(this.system)
    cluster.registerOnMemberUp {
      cluster.subscribe(actor, initialStateMode = InitialStateAsEvents, classOf[MemberEvent], classOf[UnreachableMember])
      messagesField.appendText(s"Вы онлайн как: $login\n")
      messagesField.appendText("Чтобы отправить приватное сообщение используйте команду: /private;nickname;text\n\n")
    }
  }


}