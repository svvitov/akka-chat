import akka.actor.{ActorSystem, Props}
import javafx.event.ActionEvent
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

import java.net.URL
import java.util.ResourceBundle

class LoginControllerImpl extends LoginController {

  override def onLoginButton(event: ActionEvent): Unit = {
    if(nicknameTextField.getText.trim != "") {
      loginButton.getScene.getWindow.hide()
      val controller = getController[ChatControllerImpl]("views/ChatWindow.fxml")
      val stage = new Stage()
      controller._2.login = nicknameTextField.getText.trim
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
