package Controller;

import Entity.User;
import Service.MailjetService;
import Service.UserService;
import com.mailjet.client.errors.MailjetException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Optional;

public class ReinitialisationPasswordController {

    @FXML
    private Text ageControl;

    @FXML
    private Text emailControl;

    @FXML
    private TextField emailId;

    private String emailRegex = "^([a-zA-Z0-9._%+-]+)@([a-zA-Z0-9.-]+)\\.([a-zA-Z]{2,})$";


    private static String code;
    private static String userEmail;

    public static String getCode() {
        return code;
    }

    public static void setCode(String code) {
        ReinitialisationPasswordController.code = code;
    }
    public static String getUserEmail() {
        return userEmail;
    }

    public static void setUserEmail(String userEmail) {
        ReinitialisationPasswordController.userEmail = userEmail;
    }

    void deleteError() {
        emailControl.setText(null);
        emailControl.setStyle("-fx-fill: red;");
        emailId.setStyle("-fx-border-color: #fe5d37 ;");


    }
    void emtyError() {
        emailControl.setText("Please fill in this field.");
        emailControl.setStyle("-fx-fill: red;");
        emailId.setStyle("-fx-border-color: red ;");


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
    void registration1() throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("/registrationController.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
    void verificationCodePage() throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("/resetPasswordVerifCode.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
    @FXML
    void loginPage(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        try {
            login1();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void registrationPage(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        try {
            registration1();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
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
    void send(MouseEvent event) {
        UserService service = new UserService();
        String EMAIL = emailId.getText();
        if (EMAIL == null) {
            emtyError();

        } else if (EMAIL.isEmpty() || EMAIL.isBlank()) {
//          Alert alertType = new Alert(Alert.AlertType.ERROR);
//          alertType.setTitle("Error");
//          alertType.setHeaderText("Enter a valid content !");
//          alertType.show();
            emtyError();

        }else if (!EMAIL.toString().matches(emailRegex)) {
            emailControl.setText("The email address is invalid. Please enter a valid email address (e.g., username@example.com)!");
            emailControl.setStyle("-fx-fill: red;");
            emailId.setStyle("-fx-border-color: red ;");

        }else {
            try {
                User user = service.getUserByEmail(EMAIL);
                String nouveauMotDePasse = genererMotDePasseAleatoire(8);
                setCode(nouveauMotDePasse);
                setUserEmail(EMAIL);
                MailjetService mailjetService = new MailjetService();
                String mail = " <h1> Bonjour "+ user.getFirstName()+" "+user.getLastName() +" voici votre code </h1>   <hr>"+nouveauMotDePasse ;
                mailjetService.sendMailjet(user.getEmail(),"Reset Password magic minds mail",mail);
                Alert alertType1 = new Alert(Alert.AlertType.INFORMATION);
                alertType1.setTitle("information !");
                alertType1.setHeaderText("email send with success ");
                Optional<ButtonType> result = alertType1.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.close();

                    try {
                        verificationCodePage();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }catch (SQLException | MailjetException e){
                System.out.println(e.getMessage());
            }


        }
    }

}

