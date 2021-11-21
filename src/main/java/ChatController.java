import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements Initializable {

    private Network network;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField messageField;

    @FXML
    private TextArea onlineUsers;

    @FXML
    private Button sendMessage;

    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        network = new Network((args) -> {
            chatArea.appendText((String) args[0]);
        });
    }

    public void sendMessageAction(ActionEvent actionEvent) {
        network.sendMessage(messageField.getText());
        messageField.clear();
        messageField.requestFocus();
    }

}

