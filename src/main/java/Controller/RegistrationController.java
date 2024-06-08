package Controller;

import Entity.User;
import Service.MailjetService;
import Service.UserService;
import com.mailjet.client.errors.MailjetException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrationController implements Initializable {

    @FXML
    private Text ageControl;

    @FXML
    private TextField ageId;

    @FXML
    private Text agreeControl;

    @FXML
    private CheckBox agreeId;

    @FXML
    private Text emailControl;

    @FXML
    private TextField emailId;

    @FXML
    private Text firstnameControl;

    @FXML
    private TextField firstnameId;

    @FXML
    private Text genderControl;

    @FXML
    private ComboBox<String> genderId;

    @FXML
    private Text lastnameControl;

    @FXML
    private TextField lastnameId;

    @FXML
    private Text passwordControl;

    @FXML
    private TextField passwordId;

    @FXML
    private Text roleControl;

    @FXML
    private ComboBox<String> roleId;
    private String emailRegex = "^([a-zA-Z0-9._%+-]+)@([a-zA-Z0-9.-]+)\\.([a-zA-Z]{2,})$";
    private String passwordRegex ="^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$";

    private static String code;
    private static int userid;

    public static String getCode() {
        return code;
    }

    public static void setCode(String code) {
        RegistrationController.code = code;
    }
    public static int getId() {
        return userid;
    }

    public static void setId(int id) {
        RegistrationController.userid = id;
    }
    void deleteError() {
        firstnameControl.setText(null);
        firstnameControl.setStyle("-fx-fill: red;");
        firstnameId.setStyle("-fx-border-color: #fe5d37 ;");

        lastnameControl.setText(null);
        lastnameControl.setStyle("-fx-fill: red;");
        lastnameId.setStyle("-fx-border-color: #fe5d37 ;");

        passwordControl.setText(null);
        passwordControl.setStyle("-fx-fill: red;");
        passwordId.setStyle("-fx-border-color: #fe5d37;");

        emailControl.setText(null);
        emailControl.setStyle("-fx-fill: red;");
        emailId.setStyle("-fx-border-color: #fe5d37 ;");



        ageControl.setText(null);
        ageControl.setStyle("-fx-fill: red;");
        ageId.setStyle("-fx-border-color:#fe5d37 ;");



        genderControl.setText(null);
        genderControl.setStyle("-fx-fill: red;");
        genderId.setStyle("-fx-border-color: #fe5d37 ;");

        roleControl.setText(null);
        roleControl.setStyle("-fx-fill: red;");
        roleId.setStyle("-fx-border-color: #fe5d37 ;");
    }
    void emtyError() {
        firstnameControl.setText("Please fill in this field.");
        firstnameControl.setStyle("-fx-fill: red;");
        firstnameId.setStyle("-fx-border-color: red ;");

        lastnameControl.setText("Please fill in this field.");
        lastnameControl.setStyle("-fx-fill: red;");
        lastnameId.setStyle("-fx-border-color: red ;");

        passwordControl.setText("Please fill in this field.");
        passwordControl.setStyle("-fx-fill: red;");
        passwordId.setStyle("-fx-border-color: red;");

        emailControl.setText("Please fill in this field.");
        emailControl.setStyle("-fx-fill: red;");
        emailId.setStyle("-fx-border-color: red ;");



        ageControl.setText("Please fill in this field.");
        ageControl.setStyle("-fx-fill: red;");
        ageId.setStyle("-fx-border-color:red;");



        genderControl.setText("Please fill in this field.");
        genderControl.setStyle("-fx-fill: red;");
        genderId.setStyle("-fx-border-color: red ;");

        roleControl.setText("Please fill in this field.");
        roleControl.setStyle("-fx-fill: red;");
        roleId.setStyle("-fx-border-color: red ;");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        genderId.setItems(FXCollections.observableArrayList("male", "female"));
        roleId.setItems(FXCollections.observableArrayList("admin", "child", "professor", "parent"));
        System.out.println(agreeId);
    }
    public void resetPassword1() throws IOException {

            Parent parent = FXMLLoader.load(getClass().getResource("/resetPasswordController.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();


    }
  public void Login1() throws IOException {

          Parent parent = FXMLLoader.load(getClass().getResource("/loginController.fxml"));
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
           resetPassword1();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void loginPage(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        try {
           Login1();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    @FXML
    void close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    void clear(MouseEvent event) {
        ageId.setText(null);
        firstnameId.setText(null);
        lastnameId.setText(null);
        passwordId.setText(null);
        emailId.setText(null);

        genderId.setValue(null);
      roleId.setValue(null);

        deleteError();
    }
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private String genererMotDePasseAleatoire(int longueur) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(longueur);
        for (int i = 0; i < longueur; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }
    @FXML
    void signUpBtn(MouseEvent event) {
        UserService service = new UserService();
        String FIRSTNAME = firstnameId.getText();
        String lastNAME = lastnameId.getText();
        String PASSWORD = passwordId.getText();
        String EMAIL = emailId.getText();

        String GENDER = genderId.getValue();
        String age = ageId.getText();
        String ROLE = roleId.getValue();
        if ((FIRSTNAME == null) || (lastNAME == null) || (PASSWORD == null)
                || (EMAIL == null) ||  (age == null)) {
            emtyError();

        } else if ((FIRSTNAME.isBlank()) || (lastNAME.isBlank()) || (PASSWORD.isEmpty())
                || (EMAIL.isEmpty()) || (age.isEmpty())) {
//          Alert alertType = new Alert(Alert.AlertType.ERROR);
//          alertType.setTitle("Error");
//          alertType.setHeaderText("Enter a valid content !");
//          alertType.show();
            emtyError();

        }else if (FIRSTNAME.toString().matches("[0-9]+")) {
            firstnameControl.setText(" first name must be string not number !");
            firstnameControl.setStyle("-fx-fill: red;");
            firstnameId.setStyle("-fx-border-color: red ;");

        } else if (lastNAME.toString().matches("[0-9]+")) {
            lastnameControl.setText(" last name must be string not number !");
            lastnameControl.setStyle("-fx-fill: red;");
            lastnameId.setStyle("-fx-border-color: red ;");
        } else if (PASSWORD.toString().length() < 8) {
            passwordControl.setText(" Your password must be at least 8 characters long!");
            passwordControl.setStyle("-fx-fill: red;");
            passwordId.setStyle("-fx-border-color: red ;");
        } else if (!EMAIL.toString().matches(emailRegex)) {
            emailControl.setText("The email address is invalid. Please enter a valid email address (e.g., username@example.com)!");
            emailControl.setStyle("-fx-fill: red;");
            emailId.setStyle("-fx-border-color: red ;");

        } else if ((age.matches("[A-Z]+")) || (age.matches("[a-z]+"))) {
            ageControl.setText("The telephone  msut be a number!");
            ageControl.setStyle("-fx-fill: red;");
            ageId.setStyle("-fx-border-color: red ;");

        }  else if (GENDER.isEmpty()) {
            genderControl.setText("The gender is emty !");
            genderControl.setStyle("-fx-fill: red;");
            genderId.setStyle("-fx-border-color: red ;");
        } else if (ROLE.isEmpty()) {
            roleControl.setText("The role is emty !");
            roleControl.setStyle("-fx-fill: red;");
            roleId.setStyle("-fx-border-color: red ;");

        } else {
            String newhashedPassword = BCrypt.hashpw(PASSWORD, BCrypt.gensalt());
            User user = new User( Integer.parseInt(age), FIRSTNAME, lastNAME, EMAIL, "", GENDER, newhashedPassword, "", ROLE);
            try {
                service.registerUser(user);
                String nouveauMotDePasse = genererMotDePasseAleatoire(8);
                setCode(nouveauMotDePasse);
                MailjetService mailjetService = new MailjetService();
                String mail = " <h1> Bonjour "+ user.getFirstName()+" "+user.getLastName() +" voici votre code </h1>   <hr>"+nouveauMotDePasse ;
                mailjetService.sendMailjet(user.getEmail(),"Registration magic minds mail",mail);
                FXMLLoader loader = new FXMLLoader ();
                RegistrationVerifCode registrationVerifCode = new RegistrationVerifCode();
                try {
                    User user1= service.getUserByEmail(EMAIL);
                    System.out.println(user1);
                   // registrationVerifCode.setUserId(user1.getId());
                    setId(user1.getId());
                }catch (SQLException ex ){
                    Alert alertType = new Alert(Alert.AlertType.ERROR);
                    alertType.setTitle("Error");
                    alertType.setHeaderText("echec");
                    alertType.show();
                }


//                Alert alertType = new Alert(Alert.AlertType.INFORMATION);
//                alertType.setTitle("Sucess");
//                alertType.setHeaderText("registration success ");
//                alertType.show();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();

                try {
                    vericationPage();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } catch (SQLException ex) {
                Alert alertType = new Alert(Alert.AlertType.ERROR);
                alertType.setTitle("Error");
                alertType.setHeaderText("echec");
                alertType.show();
                System.out.println(ex.getMessage());
            } catch (MailjetException e) {
                throw new RuntimeException(e);
            }
        }
        }
        void vericationPage()throws IOException{
            Parent parent = FXMLLoader.load(getClass().getResource("/registrationVerifCode.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        }

}
