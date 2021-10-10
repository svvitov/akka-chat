import javafx.event.ActionEvent
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

import java.net.NetworkInterface
import scala.collection.convert.ImplicitConversions.`enumeration AsScalaIterator`

class LoginControllerImpl extends LoginController {

  override def onLoginButton(event: ActionEvent): Unit = {
    if(nicknameTextField.getText.trim != "" && port.getText.trim != "") {
      loginButton.getScene.getWindow.hide()
      val controller = getRootController[ChatControllerImpl]("views/ChatWindow.fxml")
      val stage = new Stage()
      controller._2.login = nicknameTextField.getText.trim
      controller._2.start(port.getText)
      stage.setScene(new Scene(controller._1))
      stage.setTitle("Chat")
      stage.setResizable(false)
      stage.showAndWait()
    }
  }

  def getRootController[T](path: String): (Parent, T) = {
    val url = getClass.getClassLoader.getResource(path)
    val loader = new FXMLLoader(url)
    val root = loader.load[Parent]()
    val controller = loader.getController[T]
    (root, controller)
  }
}
