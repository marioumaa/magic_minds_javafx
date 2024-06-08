package Controller;

import Service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RegistrationVerifCode implements Initializable {

    @FXML
    private Text codeControl;

    @FXML
    private TextField codeId;
    private int userId;
    String code1=RegistrationController.getCode();
    int id = RegistrationController.getId();
  public void setUserId(int userId) {
      this.userId = userId;
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
    void login1() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/loginController.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }
    @FXML
    void activate(MouseEvent event) {
        String CODE = codeId.getText();
        if (CODE == null) {
            emtyError();

        } else if (CODE.isEmpty()) {
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
        }else {
            UserService service = new UserService();
            try {
                service.setVerification(id, true);

            }catch (Exception e) {
                System.out.println(e.getMessage());
            }  try {
                service.setVerification(id, true);

            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();

            try {
                login1();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @FXML
    void close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void login(MouseEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
