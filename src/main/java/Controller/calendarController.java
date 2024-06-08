package Controller;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.YearMonth;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author asus
 */
public class calendarController implements Initializable {

    @FXML
    public Pane calendarPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
// Créez une instance de FullCalendarView avec l'année et le mois actuels
        FullCalendarView calendarView = new FullCalendarView(YearMonth.now());

        // Obtenez la vue du calendrier et ajoutez-la à votre Pane
        calendarPane.getChildren().add(calendarView.getView());
    }
    @FXML
    private void afficherEvenements(ActionEvent event) {
        System.out.println("Méthode afficherEvenements appelée !");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenementEtudiant.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Obtenir la référence à la fenêtre principale à partir de l'objet event
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement du fichier FXML : " + e.getMessage());
        }
    }



}
