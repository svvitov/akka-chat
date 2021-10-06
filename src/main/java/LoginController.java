import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    protected TextField nicknameTextField;

    @FXML
    protected TextField host;

    @FXML
    protected TextField port;

    @FXML
    protected Button loginButton;

    @FXML
    void onLoginButton(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}