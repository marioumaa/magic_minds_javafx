package Controller;

import Entity.User;
import Service.SessionManager;
import Service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class UpdateUserPasswordController implements Initializable {

    @FXML
    private Text generalErrors;

    @FXML
    private PasswordField passwordId;

    @FXML
    private Text passwordIdControl;

    @FXML
    private PasswordField secondPassId;

    @FXML
    private Text secondPassIdControl;


    void deleteError() {
        passwordIdControl.setText(null);
        passwordIdControl.setStyle("-fx-fill: red;");
        passwordId.setStyle("-fx-border-color: #fe5d37 ;");
        secondPassIdControl.setText(null);
        secondPassIdControl.setStyle("-fx-fill: red;");
        secondPassId.setStyle("-fx-border-color: #fe5d37 ;");


    }
    void emtyError() {
        passwordIdControl.setText("Please fill in this field.");
        passwordIdControl.setStyle("-fx-fill: red;");
        passwordId.setStyle("-fx-border-color: red ;");
        secondPassIdControl.setText("Please fill in this field.");
        secondPassIdControl.setStyle("-fx-fill: red;");
        secondPassId.setStyle("-fx-border-color: red ;");


    }
    @FXML
    void Clear(MouseEvent event) {
        passwordId.setText(null);
        secondPassId.setText(null);


        deleteError();

    }

    @FXML
    void Close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void Save(MouseEvent event) {
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
            passwordIdControl.setText(" Your code must be at least 8 characters long!");
            passwordIdControl.setStyle("-fx-fill: red;");
            passwordId.setStyle("-fx-border-color: red ;");
        }else if(pass2.length()<8) {
            secondPassIdControl.setText(" Your code must be at least 8 characters long!");
            secondPassIdControl.setStyle("-fx-fill: red;");
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
                User user = SessionManager.getCurrentUser();

                String newhashedPassword = BCrypt.hashpw(pass, BCrypt.gensalt());
                userService.setMDP(user.getId(), newhashedPassword);
                Alert alertType1 = new Alert(Alert.AlertType.INFORMATION);
                alertType1.setTitle("information !");
                alertType1.setHeaderText("mot de passe has modified  ");
                alertType1.show();
//                Optional<ButtonType> result = alertType1.showAndWait();
//                if (result.get() == ButtonType.OK) {
//                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//                    stage.close();
//
//                    try {
//                        login1();
//                    } catch (IOException e) {
//                        System.out.println(e.getMessage());
//                    }
//                }
            }catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
