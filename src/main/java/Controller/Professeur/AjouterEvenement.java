package Controller.Professeur;

import Entity.Evenement;
import Service.ServiceEvenement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AjouterEvenement {
    private final ServiceEvenement serviceEvenement = new ServiceEvenement();

    @FXML
    private TextField tfNom;

    @FXML
    private TextField tfDescription;

    @FXML
    private TextField tfLocalisation;

    @FXML
    private TextField tfCategorie;

    @FXML
    private TextField tfNbParticipant;

    @FXML
    private TextField tfDateDebut;

    @FXML
    private TextField tfDateFin;
    private boolean isValidString(String input) {
        return input.matches("[a-zA-Z\\s]+");
    }
    @FXML
    void ajouterEvenement(ActionEvent event) {

        if (!isValidString(tfNom.getText()) || !isValidString(tfDescription.getText()) || !isValidString(tfLocalisation.getText()) || !isValidString(tfCategorie.getText())) {
            showAlert("Name, description, location, and category fields must contain only alphabetical characters and spaces.", Alert.AlertType.ERROR);
            return;
        }

        LocalDate dateDebut;
        try {
            dateDebut = parseDate(tfDateDebut.getText());
            if (!dateDebut.isAfter(LocalDate.now())) {
                showAlert("Start date must be after the current date.", Alert.AlertType.ERROR);
                return;
            }
        } catch (ParseException e) {
            showAlert("Invalid start date format. Use the format YYYY-MM-DD.", Alert.AlertType.ERROR);
            return;
        }

        LocalDate dateFin;
        try {
            dateFin = parseDate(tfDateFin.getText());
            if (!dateFin.isAfter(dateDebut)) {
                showAlert("End date must be after the start date.", Alert.AlertType.ERROR);
                return;
            }
        } catch (ParseException e) {
            showAlert("Invalid end date format. Use the format YYYY-MM-DD.", Alert.AlertType.ERROR);
            return;
        }

        try {
            int nbParticipants = Integer.parseInt(tfNbParticipant.getText());
            if (nbParticipants <= 20) {
                showAlert("Number of participants must be greater than 20.", Alert.AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid number of participants", Alert.AlertType.ERROR);
            return;
        }


        Evenement evenement = new Evenement();
        evenement.setNom(tfNom.getText());
        evenement.setDescription(tfDescription.getText());
        evenement.setLocalisation(tfLocalisation.getText());
        evenement.setCategorie(tfCategorie.getText());
        evenement.setNb_participant(Integer.parseInt(tfNbParticipant.getText()));
        evenement.setDate_debut(Date.valueOf(dateDebut));
        evenement.setDate_fin(Date.valueOf(dateFin));
        serviceEvenement.add(evenement);
        System.out.println("Événement ajouté avec succès !");


        redirectToEventDisplay(event);
        generateNotification(evenement);
    }
    private void generateNotification(Evenement evenement) {
        Notifications.create()
                .title("Event Added")
                .text("Event Name: " + evenement.getNom() + "\n" +
                        "Description: " + evenement.getDescription() + "\n" +
                        "Location: " + evenement.getLocalisation() + "\n" +
                        "Category: " + evenement.getCategorie() + "\n" +
                        "Start Date: " + evenement.getDate_debut() + "\n" +
                        "End Date: " + evenement.getDate_fin() + "\n" +
                        "Number of Participants: " + evenement.getNb_participant())
                .showInformation();
    }
    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private LocalDate parseDate(String dateString) throws ParseException {
        // Modèle de format flexible
        String[] patterns = {"yyyy-MM-dd", "yyyy-M-dd", "yyyy-MM-d", "yyyy-M-d"};


        for (String pattern : patterns) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                return LocalDate.parse(dateString, formatter);
            } catch (DateTimeParseException e) {

            }
        }

        throw new ParseException("Format de date invalide", 0);
    }

    private void redirectToEventDisplay(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenement.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();


            ((Stage) tfNom.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void goBackPROFE(ActionEvent event) {
        redirectToEventDisplay(event);
    }

}
