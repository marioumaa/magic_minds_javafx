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

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class EditEvenementController {

    private final ServiceEvenement serviceEvenement = new ServiceEvenement();
    private Evenement evenementToEdit;
    private AfficherEvenementController afficherEvenementController;

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

    public void setEvenementToEdit(Evenement evenement) {
        this.evenementToEdit = evenement;
        populateFields();
    }

    public void setAfficherEvenementController(AfficherEvenementController controller) {
        this.afficherEvenementController = controller;
    }

    private void populateFields() {
        if (evenementToEdit != null) {
            tfNom.setText(evenementToEdit.getNom());
            tfDescription.setText(evenementToEdit.getDescription());
            tfLocalisation.setText(evenementToEdit.getLocalisation());
            tfCategorie.setText(evenementToEdit.getCategorie());
            tfNbParticipant.setText(String.valueOf(evenementToEdit.getNb_participant()));
            tfDateDebut.setText(evenementToEdit.getDate_debut().toString());
            tfDateFin.setText(evenementToEdit.getDate_fin().toString());
        }
    }


    @FXML
    void modifierEvenement(ActionEvent event) {

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
            showAlert("Invalid number of participants.", Alert.AlertType.ERROR);
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
        serviceEvenement.update(evenementToEdit.getId(),evenement);

        System.out.println("Event successfully modified!");


        afficherEvenementController.loadEvents();

            redirectToEventDisplay(event);
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
    void goBackEDITP(ActionEvent event) {
        redirectToEventDisplay(event);
    }
    private boolean isValidString(String input) {
        return input.matches("[a-zA-Z\\s]+");
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
}
