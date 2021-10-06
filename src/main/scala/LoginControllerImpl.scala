import javafx.event.ActionEvent
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

import java.net.{NetworkInterface, URL}
import java.util.ResourceBundle
import scala.collection.convert.ImplicitConversions.`enumeration AsScalaIterator`

class LoginControllerImpl extends LoginController {

  override def onLoginButton(event: ActionEvent): Unit = {
    if(nicknameTextField.getText.trim != "" && port.getText.trim != "") {
      loginButton.getScene.getWindow.hide()
      val controller = getController[ChatControllerImpl]("views/ChatWindow.fxml")
      val stage = new Stage()

      val interfaces = NetworkInterface.getNetworkInterfaces
      val inetAddresses = interfaces.flatMap(interface => interface.getInetAddresses)
      val ip = inetAddresses.find(_.isSiteLocalAddress).map(_.getHostAddress).get
      controller._2.login = nicknameTextField.getText.trim
      controller._2.init(if (host.getText.isEmpty) ip else host.getText, port.getText)


      stage.setScene(new Scene(controller._1))
      stage.setTitle("Chat")
      stage.setResizable(false)
      stage.showAndWait()
    }
  }

  override def initialize(url: URL, resourceBundle: ResourceBundle): Unit = {

  }

  def getController[T](uri: String): (Parent, T) = {
    val url = getClass.getClassLoader.getResource(uri)
    val loader = new FXMLLoader(url)
    val root = loader.load[Parent]()
    val controller = loader.getController[T]
    (root, controller)
  }
}
