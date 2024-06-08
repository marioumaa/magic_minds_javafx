package test;

import Entity.CurrentUser;
import Entity.User;
import Service.SessionManager;
import Service.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainFX extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Parent parent   = FXMLLoader.load(getClass().getResource("/LoginController.fxml"));
       // AfficherEvenementAdmin
       // statsPageUserController.fxml
       ///UserManagementController.fxml
       // User user = SessionManager.getCurrentUser();
       CurrentUser.user_id= 15;
//        UserService service = new UserService();
//        User user = service.getUserByEmail("benslimenlouay29@gmail.com");
//        SessionManager sessionManager = SessionManager.getInstance();
//        sessionManager.startSession(user);
        Scene scene = new Scene(parent);

        stage.setTitle("Dashboard");

      stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);

        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

}
