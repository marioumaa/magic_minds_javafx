package Controller.Professeur;

import Controller.UserManagementController;
import Entity.Evenement;
import Service.ServiceEvenement;
import Service.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AfficherEvenementController {

    @FXML
    private VBox eventContainer;

    private final ServiceEvenement serviceEvenement = new ServiceEvenement();

    @FXML
    void initialize() {
        loadEvents();
    }

    public void loadEvents() {
        List<Evenement> evenements = serviceEvenement.getAll();
        eventContainer.getChildren().clear();

        for (Evenement evenement : evenements) {

            VBox card = createEventCard(evenement);
            eventContainer.getChildren().add(card);
        }
    }

    private VBox createEventCard(Evenement evenement) {
        VBox card = new VBox(20);
        card.getStyleClass().add("event-card");

        Label nomLabel = new Label("Event Title : " + evenement.getNom());
        nomLabel.setStyle("-fx-font-size: 18pt; -fx-font-weight: bold; -fx-text-fill: #FE5D37;");
        Label descriptionLabel = new Label("Event Description : " + evenement.getDescription());
        Label localisationLabel = new Label("Event Location : " + evenement.getLocalisation());
        Label categorieLabel = new Label("Event Category : " + evenement.getCategorie());
        Label dateDebutLabel = new Label("Start Date : " + evenement.getDate_debut());
        Label dateFinLabel = new Label("End Date : " + evenement.getDate_fin());
        Label nbParticipantsLabel = new Label("Number of Participants : " + evenement.getNb_participant());


        Button deleteButton = new Button("Delete");
        Button editButton = new Button("Update");
        deleteButton.setStyle("-fx-background-color: #fe5d37; -fx-font-weight: bold; -fx-text-fill: white;");
        editButton.setStyle("-fx-background-color: #fe5d37; -fx-font-weight: bold; -fx-text-fill: white;");

        deleteButton.setOnAction(event -> deleteEvent(evenement));
        editButton.setOnAction(event -> openEditForm(evenement));

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(deleteButton, editButton);

        card.getChildren().addAll(
                nomLabel,
                descriptionLabel,
                localisationLabel,
                categorieLabel,
                dateDebutLabel,
                dateFinLabel,
                nbParticipantsLabel,
                buttonBox
        );


        card.setAlignment(Pos.CENTER);

        return card;
    }

    private void openEditForm(Evenement evenement) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditEvenement.fxml"));
            Parent root = loader.load();

            EditEvenementController editController = loader.getController();
            editController.setEvenementToEdit(evenement);


            editController.setAfficherEvenementController(this);


            eventContainer.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteEvent(Evenement evenement) {
        serviceEvenement.delete(evenement);
        loadEvents();
    }
    @FXML
    void ADDPROF(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterEvenement.fxml"));
            Parent root = loader.load();

            // Vous pouvez ajouter d'autres configurations si nécessaire

            // Changer la scène pour afficher la vue AjouterEvenementAdmin
            eventContainer.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void goBackPPR(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherParticipationProf.fxml"));
            Parent root = loader.load();

            // Vous pouvez ajouter d'autres configurations si nécessaire

            // Changer la scène pour afficher la vue AjouterEvenementAdmin
            eventContainer.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void goCourse(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CoursesProf.fxml"));
            Parent cartRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene of the current stage to the cart view
            Scene scene = new Scene(cartRoot);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to load the cart view.");
            alert.showAndWait();
        }
    }

    @FXML
    void goEvent(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenement.fxml"));
            Parent cartRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene of the current stage to the cart view
            Scene scene = new Scene(cartRoot);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to load the cart view.");
            alert.showAndWait();
        }
    }

    @FXML
    void goForum(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/demo4/Home_front_Prof_eleve.fxml"));
            Parent cartRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene of the current stage to the cart view
            Scene scene = new Scene(cartRoot);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to load the cart view.");
            alert.showAndWait();
        }
    }

    @FXML
    void goQuizz(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherQuizProf.fxml"));
            Parent cartRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene of the current stage to the cart view
            Scene scene = new Scene(cartRoot);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to load the cart view.");
            alert.showAndWait();
        }
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
    void logout(MouseEvent event) {
        SessionManager sessionManager= SessionManager.getInstance();
        sessionManager.endSession();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        try {
            Login1();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void updateProfile(MouseEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/showUserProfileController.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(UserManagementController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

