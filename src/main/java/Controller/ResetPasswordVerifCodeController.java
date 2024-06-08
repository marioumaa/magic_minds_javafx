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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class ResetPasswordVerifCodeController {

    @FXML
    private Text codeControl;

    @FXML
    private TextField codeId;
    private static int userId;
    String code1=ReinitialisationPasswordController.getCode();
    String email=ReinitialisationPasswordController.getUserEmail();

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        ResetPasswordVerifCodeController.userId = userId;
    }
    void deleteError() {
        codeControl.setText(null);
        codeControl.setStyle("-fx-fill: red;");
        codeId.setStyle("-fx-border-color: #fe5d37 ;");


    }
    void emtyError() {
        codeControl.setText("Please fill in this field.");
        codeControl.setStyle("-fx-fill: red;");
        codeId.setStyle("-fx-border-color: red ;");


    }
    void modifierMotDePassePage() throws IOException{
        Parent parent = FXMLLoader.load(getClass().getResource("/changePasswordController.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
    @FXML
    void activate(MouseEvent event) {
        UserService service = new UserService();

        String CODE = codeId.getText();
        if (CODE == null) {
            emtyError();

        } else if (CODE.isEmpty() || CODE.isBlank()) {
//          Alert alertType = new Alert(Alert.AlertType.ERROR);
//          alertType.setTitle("Error");
//          alertType.setHeaderText("Enter a valid content !");
//          alertType.show();
            emtyError();

        }else if(CODE.length()<8) {
            codeControl.setText(" Your code must be at least 8 characters long!");
            codeControl.setStyle("-fx-fill: red;");
            codeId.setStyle("-fx-border-color: red ;");
        } else if (!code1.equals(CODE)) {
            Alert alertType = new Alert(Alert.AlertType.WARNING);
            alertType.setTitle("Code incorrect");
            alertType.setContentText("Veuillez saisir le code .");
            codeId.clear();
            alertType.show();
        }else{
            try {
                User user = service.getUserByEmail(email);
                setUserId(user.getId());
                Alert alertType1 = new Alert(Alert.AlertType.INFORMATION);
                alertType1.setTitle("information !");
                alertType1.setHeaderText("correct code ");
                Optional<ButtonType> result = alertType1.showAndWait();
                if (result.get() == ButtonType.OK) {
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.close();

                    try {
                        modifierMotDePassePage();
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
