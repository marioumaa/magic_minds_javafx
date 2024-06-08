package Controller.Admin;

import Entity.Participation;
import Entity.User;
import Service.ServiceEvenement;
import Service.ServiceParticipation;
import Service.UserService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class AfficherParticipation {
    @FXML
    private PieChart participationPieChart;
    @FXML
    private TableView<Participation> participationTable;

    @FXML
    private TableColumn<Participation, String> dateColumn;
    @FXML
    private TableColumn<Participation, String> NomuserColumn;
    @FXML
    private TableColumn<Participation, String> NomeventColumn;

    @FXML
    private TableColumn<Participation, String> heureColumn;

    private final ServiceParticipation serviceParticipation = new ServiceParticipation();
    private final ServiceEvenement serviceEvenement = new ServiceEvenement();

    private final UserService serviceUser = new UserService();
    @FXML
    void initialize() {

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        heureColumn.setCellValueFactory(new PropertyValueFactory<>("heure"));
        NomuserColumn.setCellValueFactory(cellData -> {
            String username = null;
            User user ;
            try {
                user = serviceUser.getUserById(cellData.getValue().getId_user_id());
                username = user.getFirstName() + " "+user.getLastName();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return new SimpleStringProperty(username);
        });

        // Cell factory pour le nom de l'événement
        NomeventColumn.setCellValueFactory(cellData -> {
            String eventName = serviceEvenement.getEventNameById(cellData.getValue().getEvenementId());
            return new SimpleStringProperty(eventName);
        });


        List<Participation> participations = serviceParticipation.getAll();


        participationTable.getItems().addAll(participations);

        showParticipationStatistics(participations);
        addButtonToTable();
    }
    private void showParticipationStatistics(List<Participation> participations) {
        Map<String, Integer> eventCounts = new HashMap<>();

        // Count the number of participations for each event
        for (Participation participation : participations) {
            String eventName = serviceEvenement.getEventNameById(participation.getEvenementId());
            eventCounts.put(eventName, eventCounts.getOrDefault(eventName, 0) + 1);
        }

        // Create a list of segments for the PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        int totalParticipations = participations.size();
        for (Map.Entry<String, Integer> entry : eventCounts.entrySet()) {
            String eventName = entry.getKey();
            int count = entry.getValue();
            double percentage = ((double) count / totalParticipations) * 100;
            pieChartData.add(new PieChart.Data(eventName + " (" + String.format("%.2f", percentage) + "%)", count));
        }

        // Display the PieChart
        participationPieChart.setData(pieChartData);

        // Set the English title
        participationPieChart.setTitle("Participation Statistics");
    }

    @FXML
    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addButtonToTable() {
        TableColumn<Participation, Void> deleteColumn = new TableColumn<>("Delete" );
        deleteColumn.setMinWidth(80);

        deleteColumn.setCellFactory(param -> new ButtonCell());

        deleteColumn.setOnEditCommit(event -> {
            Participation participation = event.getRowValue();
            deleteParticipation(participation);
        });

        participationTable.getColumns().add(deleteColumn);
    }

    @FXML
    private void deleteParticipation(Participation participation) {
        if (participation != null) {
            serviceParticipation.delete(participation);
            participationTable.getItems().remove(participation);
        } else {
            // Afficher un message d'erreur si aucune participation n'est sélectionnée
            showAlert("Veuillez sélectionner une participation à supprimer.", Alert.AlertType.ERROR);
        }
    }


    private class ButtonCell extends TableCell<Participation, Void> {
        private final Button deleteButton = new Button("Delete");

        ButtonCell() {
            deleteButton.setOnAction(event -> {

                Participation participation = getTableView().getItems().get(getIndex());
                deleteParticipation(participation);
            });
            deleteButton.setStyle("-fx-background-color: #f3f35b; -fx-font-weight: bold; ");
        }


        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(deleteButton);
            }
        }
    }
    @FXML
    void goBackaffichage(ActionEvent event) {
        redirectToEventDisplay(event);
    }

    private void redirectToEventDisplay(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenementAdmin.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current window
            ((Stage) participationTable.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
