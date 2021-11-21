import animations.Shake;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button signUpButton;

    @FXML
    private Button logInButton;

    @FXML
    private TextField login_field;

    @FXML
    private PasswordField password_field;

    @FXML
    private Text wrongLoginOrPassword;

    @FXML
    private Text emptyLoginOrPassword;


    @FXML
    void initialize() {

        logInButton.setOnAction(actionEvent -> {
            String loginText = login_field.getText().trim();
            String loginPassword = password_field.getText().trim();

            if (!loginText.equals("") && !loginPassword.equals("")) {
                loginUser(loginText, loginPassword);
            } else {
                if (!wrongLoginOrPassword.isVisible() || wrongLoginOrPassword.isVisible()) {
                    wrongLoginOrPassword.setVisible(false);
                    emptyLoginOrPassword.setVisible(true);
                }
            }
        });

        signUpButton.setOnAction(actionEvent -> {
            openNewScene("signUpController.fxml", signUpButton);
        });

    }

    private void loginUser(String loginText, String loginPassword) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        User user = new User();
        user.setLogin(loginText);
        user.setPassword(loginPassword);
        ResultSet result = dbHandler.getUser(user);

        int counter = 0;

        try {
            while (result.next()) {
                counter++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (counter >= 1) {
            openNewScene("chatController.fxml", logInButton);
        } else {
            Shake userLoginAnimation = new Shake(login_field);
            Shake userPasswordAnimation = new Shake(password_field);
            userLoginAnimation.playAnimation();
            userPasswordAnimation.playAnimation();
            if (!emptyLoginOrPassword.isVisible() || emptyLoginOrPassword.isVisible()) {
                emptyLoginOrPassword.setVisible(false);
                wrongLoginOrPassword.setVisible(true);
            }

        }
    }

    public void openNewScene(String window, Button button) {
        button.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(window));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}




