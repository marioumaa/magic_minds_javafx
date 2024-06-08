package Controller.Parent;

import Entity.Participation;
import Entity.User;
import Service.ServiceEvenement;
import Service.ServiceParticipation;
import Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherParticipation {

    @FXML
    private VBox participationContainer;

    private final ServiceParticipation serviceParticipation = new ServiceParticipation();
    private final ServiceEvenement serviceEvenement = new ServiceEvenement();

    private final UserService serviceUser = new UserService();
    @FXML
    void initialize() throws SQLException {
        loadParticipations();
    }

    public void loadParticipations() throws SQLException {
        List<Participation> participations = serviceParticipation.getAll();
        participationContainer.getChildren().clear();

        for (Participation participation : participations) {

            VBox card = createParticipationCard(participation);
            participationContainer.getChildren().add(card);
        }
    }

    private VBox createParticipationCard(Participation participation) throws SQLException {
        VBox card = new VBox(10);
        card.getStyleClass().add("participation-card");

        String eventName = serviceEvenement.getEventNameById(participation.getEvenementId());
        User user =serviceUser.getUserById(participation.getId_user_id());
        String username = user.getFirstName()+ " "+ user.getLastName();

        Label dateLabel = new Label("Date: " + participation.getDate());
        Label timeLabel = new Label("Time: " + participation.getHeure());
        Label eventNameLabel = new Label("Event Name: " + eventName);
        Label usernameLabel = new Label("User Name: " + username);
        Button deleteButton = new Button("Delete");

        deleteButton.setStyle("-fx-background-color: #fe5d37; -fx-font-weight: bold; -fx-text-fill: white;");



        deleteButton.setOnAction(event -> {
            try {
                deleteParticipation(participation);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(deleteButton);

        card.getChildren().addAll(dateLabel, timeLabel,eventNameLabel,usernameLabel,deleteButton);

        return card;
    }

    private void deleteParticipation(Participation participation) throws SQLException {
        serviceParticipation.delete(participation);
        loadParticipations(); // Rafraîchir la liste des participations après suppression
    }
    @FXML
    void goBackEP(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenementParent.fxml"));
            Parent root = loader.load();
            participationContainer.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
