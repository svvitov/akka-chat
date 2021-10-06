import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatController{

    @FXML
    protected TextArea messagesField;

    @FXML
    protected TextField messageInput;

    @FXML
    protected Button sendMessageButton;

    @FXML
    protected Button exitButton;

    @FXML
    void onExitButton(ActionEvent event) {

    }

    @FXML
    void onSendMessageButton(ActionEvent event) {

    }
}