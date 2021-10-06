import User.{Bye, IAm, PrivateMessage, PublicMessage, Registration, WhoAreYou}
import akka.actor.{Actor, RootActorPath}
import akka.cluster.ClusterEvent.{InitialStateAsEvents, MemberEvent, MemberUp, UnreachableMember}
import akka.cluster.{Cluster, Member}

object User {

  trait MySerializable

  final case class PublicMessage(from: String, text: String) extends MySerializable

  final case class PrivateMessage(from: String, to: String, text: String) extends MySerializable

  final case class IAm(nickname: String) extends MySerializable

  final case class Bye(nickname: String) extends MySerializable

  final case object Registration extends MySerializable

  final case object WhoAreYou extends MySerializable

}

class User(chatControllerImpl: ChatControllerImpl) extends Actor {

  override def receive = {

    case PublicMessage(from, text) => chatControllerImpl.messagesField.appendText(s"[$from]: $text\n")

    case PrivateMessage(from, to, text) => if (from.equals(chatControllerImpl.login) | to.equals(chatControllerImpl.login)) {
      chatControllerImpl.messagesField.appendText(s"[PRIVATE] [$from]: $text\n")
    }
    case MemberUp(member) => register(member)

    case Registration if !chatControllerImpl.usersOnlineRefs.contains(sender()) =>
      context.watch(sender())
      chatControllerImpl.usersOnlineRefs = chatControllerImpl.usersOnlineRefs :+ sender()

    case WhoAreYou => sender() ! IAm(chatControllerImpl.login)

    case IAm(nickname) => if(!chatControllerImpl.login.equals(nickname)) {
      chatControllerImpl.messagesField.appendText(s"[$nickname online]\n")
    }

    case Bye(nickname) => chatControllerImpl.messagesField.appendText(s"[$nickname offline]\n")

  }


  def register(member: Member) = {
    context.actorSelection(RootActorPath(member.address) / "user" / "*") ! Registration
    context.actorSelection(RootActorPath(member.address) / "user" / "*") ! WhoAreYou
  }

}