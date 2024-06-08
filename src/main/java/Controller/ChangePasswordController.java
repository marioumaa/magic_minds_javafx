package Controller;

import Entity.User;
import Service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class ChangePasswordController {

    @FXML
    private Text passwordControl;

    @FXML
    private PasswordField passwordId;

    @FXML
    private Text secondPassControl;

    @FXML
    private PasswordField secondPassId;
    int userId = ResetPasswordVerifCodeController.getUserId();

    void deleteError() {
        passwordControl.setText(null);
        passwordControl.setStyle("-fx-fill: red;");
        passwordId.setStyle("-fx-border-color: #fe5d37 ;");
        secondPassControl.setText(null);
        secondPassControl.setStyle("-fx-fill: red;");
        secondPassId.setStyle("-fx-border-color: #fe5d37 ;");


    }
    void emtyError() {
        passwordControl.setText("Please fill in this field.");
        passwordControl.setStyle("-fx-fill: red;");
        passwordId.setStyle("-fx-border-color: red ;");
        secondPassControl.setText("Please fill in this field.");
        secondPassControl.setStyle("-fx-fill: red;");
        secondPassId.setStyle("-fx-border-color: red ;");


    }
    @FXML
    void change(MouseEvent event) {
        UserService service = new UserService();

        String pass = passwordId.getText();
        String pass2 = secondPassId.getText();
        if ((pass == null) || (pass2 == null)) {
            emtyError();

        } else if (pass.isEmpty() || pass.isBlank() || pass2.isEmpty() || pass2.isBlank()) {
//          Alert alertType = new Alert(Alert.AlertType.ERROR);
//          alertType.setTitle("Error");
//          alertType.setHeaderText("Enter a valid content !");
//          alertType.show();
            emtyError();

        }else if(pass.length()<8) {
            passwordControl.setText(" Your code must be at least 8 characters long!");
            passwordControl.setStyle("-fx-fill: red;");
            passwordId.setStyle("-fx-border-color: red ;");
        }else if(pass2.length()<8) {
            secondPassControl.setText(" Your code must be at least 8 characters long!");
            secondPassControl.setStyle("-fx-fill: red;");
            secondPassId.setStyle("-fx-border-color: red ;");
        }else if (!pass.equals(pass2)) {
            Alert alertType = new Alert(Alert.AlertType.WARNING);
            alertType.setTitle("Warning");
            alertType.setContentText("your passwords do not match!");
            passwordId.clear();
            secondPassId.clear();
            alertType.show();
        }else{
            UserService userService = new UserService();
            try {
//                User user = userService.getUserById(userId);
                String newhashedPassword = BCrypt.hashpw(pass, BCrypt.gensalt());
                userService.setMDP(userId, newhashedPassword);
                Alert alertType1 = new Alert(Alert.AlertType.INFORMATION);
                alertType1.setTitle("information !");
                alertType1.setHeaderText("mot de passe has modified  ");
                Optional<ButtonType> result = alertType1.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.close();

                    try {
                        login1();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    @FXML
    void close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    void login1() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/loginController.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
    @FXML
    void login(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        try {
            login1();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
