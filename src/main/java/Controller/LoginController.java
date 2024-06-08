package Controller;

import Service.SessionManager;
import Service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class LoginController {

    @FXML
    private TextField email;

    @FXML
    private Text emailControl;

    @FXML
    private Text generaleErrors;

    @FXML
    private Text passwordControl;

    @FXML
    private PasswordField passwordId;
    private String emailRegex = "^([a-zA-Z0-9._%+-]+)@([a-zA-Z0-9.-]+)\\.([a-zA-Z]{2,})$";
    void deleteError() {
        emailControl.setText(null);
        emailControl.setStyle("-fx-fill: red;");
        email.setStyle("-fx-border-color: #fe5d37 ;");

        passwordControl.setText(null);
        passwordControl.setStyle("-fx-fill: red;");
        passwordId.setStyle("-fx-border-color: #fe5d37 ;");


    }
    void emtyError() {


        passwordControl.setText("Please fill in this field.");
        passwordControl.setStyle("-fx-fill: red;");
        passwordId.setStyle("-fx-border-color: red;");

        emailControl.setText("Please fill in this field.");
        emailControl.setStyle("-fx-fill: red;");
        email.setStyle("-fx-border-color: red ;");








    }
    @FXML
    void close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    void forgetPass() throws IOException {

        Parent parent = FXMLLoader.load(getClass().getResource("/reinitialisationPasswordController.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
    public void registration() throws IOException {

        Parent parent = FXMLLoader.load(getClass().getResource("/registrationController.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();

    }
    @FXML
    void forgetPasswordPage(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        try {
            forgetPass();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void login(MouseEvent event) throws SQLException {
        UserService service = new UserService();
        String page ="";
        String message = "";

        String EMAIL = email.getText();
        String PASS = passwordId.getText();
        if ((EMAIL == null) || (PASS == null)) {
            emtyError();

        } else if ((EMAIL.isBlank()) || (PASS.isBlank()) || (EMAIL.isEmpty())
                || (PASS.isEmpty()) ) {
//          Alert alertType = new Alert(Alert.AlertType.ERROR);
//          alertType.setTitle("Error");
//          alertType.setHeaderText("Enter a valid content !");
//          alertType.show();
            emtyError();

        } else if (!EMAIL.toString().matches(emailRegex)) {
            emailControl.setText("The email address is invalid. Please enter a valid email address (e.g., username@example.com)!");
            emailControl.setStyle("-fx-fill: red;");
            email.setStyle("-fx-border-color: red ;");

        }else if (PASS.toString().length() < 8) {
            passwordControl.setText(" Your password must be at least 8 characters long!");
            passwordControl.setStyle("-fx-fill: red;");
            passwordId.setStyle("-fx-border-color: red ;");
        } else if (service.checkUserUnique(EMAIL)) {
            emailControl.setText("The email not found.!");
            emailControl.setStyle("-fx-fill: red;");
            email.setStyle("-fx-border-color: red ;");
        }else{
            SessionManager sessionManager= SessionManager.getInstance();
            Boolean authentifie = service.authenticateUser(EMAIL,PASS);
            if(authentifie == false){
                generaleErrors.setText(" Wrong Password !");
                generaleErrors.setStyle("-fx-fill: red;");
            } else if (!service.getUserByEmail(EMAIL).isActive()) {
                generaleErrors.setText(" User desactivated !");
                generaleErrors.setStyle("-fx-fill: red;");
            } else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                sessionManager.startSession(service.getUserByEmail(EMAIL));
                String role = service.getUserByEmail(EMAIL).getRoles();
                switch (role) {
                    case "[\"ROLE_ADMIN\"]":
                    message ="admin";

                        page = "/UserManagementController.fxml";
                        break;

                    case "[\"ROLE_CHILD\"]":

                        message ="child";
                        page = "/CoursesEnfant.fxml";
                        break;

                    case "[\"ROLE_PROFESSOR\"]":

                        message ="professor";
                        page = "/CoursesProf.fxml";
                        break;
                    case "[\"ROLE_PARENT\"]":

                        message ="prefessor";
                        page = "/Store.fxml";
                        break;
                }
//                alert.setAlertType(Alert.AlertType.INFORMATION);
//                alert.setContentText("Admin connected");
//                alert.show();
//                Optional<ButtonType> result = alert.showAndWait();
//                if (result.get() == ButtonType.OK) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();

                try {
                    page(page);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
//                }
            }

        }

    }

    @FXML
    void registrationPage(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        try {
            registration();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
      void page(String page)throws IOException{

          Parent parent = FXMLLoader.load(getClass().getResource(page));
          Scene scene = new Scene(parent);
          Stage stage = new Stage();
          stage.setScene(scene);
          stage.initStyle(StageStyle.UTILITY);
          stage.show();
      }
}
