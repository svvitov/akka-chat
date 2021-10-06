import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

class Launcher extends Application{
  override def start(stage: Stage): Unit = {
    val parent: Parent = FXMLLoader.load(getClass.getResource("/views/LoginWindow.fxml"))
    stage.setScene(new Scene(parent))
    stage.setTitle("Chat")
    stage.setResizable(false)
    stage.show()
  }
}
