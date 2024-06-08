/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temquestione file, choose Tools | Temquestiones
 * and open the temquestione in the editor.
 */
package Controller;

import Entity.User;
import Entity.question;
import Entity.reponse;
import Service.SessionManager;
import Service.questionService;
import Service.reponseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


/**
 * FXML Controller class
 *
 * @author asus
 */
public class AfficherreponseController implements Initializable {

    @FXML
    private TableView<reponse> tablereponse;
    @FXML
    private TableColumn<reponse, Integer> iduserTv;
    @FXML
    private TableColumn<reponse, Integer> idevTv;
    @FXML
    private TableColumn<reponse, Date> datePartTv;
    @FXML
    private TableColumn<reponse, String> descriptionevTv;
    @FXML
    private TableColumn<reponse, String> fullnameevTv;
    @FXML
    private Button modifierPartBtn;
    @FXML
    private Button supprimerPartBtn;
    @FXML
    private TextField descriptionevField;
    @FXML
    private TextField fullnameevField;
    @FXML
    private Button ajouter;
    @FXML
    private TextField idread;
    @FXML
    private TextField iduserField;
    @FXML
    private TextField idevField;
    @FXML
    private DatePicker datepartField;
    @FXML
    private TextField chercherevField;
    @FXML
    private Button repondreevButton;
    @FXML
    private TextField datepartField1;

    private questionService questionService;
    private reponseService reponseService;
    private ObservableList<reponse> responseList;
    private question currentQuestion;
    private static final List<String> BAD_WORDS = Arrays.asList("badword1", "badword2", "badword3"); // Add more words as needed

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        questionService = new questionService();
        reponseService = new reponseService();
        responseList = FXCollections.observableArrayList();
        setupTableView();
    }

    private void setupTableView() {
        iduserTv.setCellValueFactory(new PropertyValueFactory<>("id_user"));
        idevTv.setCellValueFactory(new PropertyValueFactory<>("id"));
        descriptionevTv.setCellValueFactory(new PropertyValueFactory<>("description"));
        fullnameevTv.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        tablereponse.setItems(responseList);
    }
    private String cleanUpBadWords(String input) {
        String[] words = input.split("\\s+");
        StringBuilder cleanMessage = new StringBuilder();
        boolean foundBadWord = false;

        for (String word : words) {
            if (BAD_WORDS.contains(word.toLowerCase())) {
                String stars = "*".repeat(word.length());
                cleanMessage.append(stars).append(" ");
                foundBadWord = true;
            } else {
                cleanMessage.append(word).append(" ");
            }
        }

        if (foundBadWord) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Inappropriate Language Detected");
            alert.setHeaderText(null);
            alert.setContentText("Please avoid using inappropriate language.");
            alert.showAndWait();
        }

        return cleanMessage.toString().trim();
    }

    public void setQuestion(question q) {
        currentQuestion = q;
        idread.setText(Integer.valueOf(currentQuestion.getId()).toString());
        getReponsesForQuestion(currentQuestion.getId());
    }

    private void getReponsesForQuestion(int questionId) {
        try {
            List<reponse> responses = reponseService.getReponsesByQuestionId(questionId);
            responseList.setAll(responses);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void ajouterReponse(ActionEvent event) {
        try {
            //int userId = iduserField.getText().isEmpty() ? 1 : Integer.parseInt(iduserField.getText());
            User user = SessionManager.getCurrentUser();
            int userId = user.getId();
            int questionId = currentQuestion.getId();
            String description = descriptionevField.getText();
            description = cleanUpBadWords(description); // Clean up bad words
            String fullname = fullnameevField.getText();

            reponse newResponse = new reponse(userId, questionId, description, fullname);
            reponseService.ajouterreponse(newResponse);
            getReponsesForQuestion(currentQuestion.getId());
        } catch (NumberFormatException e) {
            System.err.println("Input string is not a valid integer: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText("Invalid User ID");
            alert.setContentText("Please enter a valid User ID.");
            alert.showAndWait();
        }
    }



    @FXML
    private void modifierreponse(ActionEvent event) {
        try {
            reponse responseToUpdate = tablereponse.getSelectionModel().getSelectedItem();
            System.out.println("1"+responseToUpdate);
            String description = descriptionevField.getText();
            description = cleanUpBadWords(description);
            responseToUpdate.setDescription(description);
            responseToUpdate.setFullname(fullnameevField.getText());
            System.out.println("2"+responseToUpdate);
            reponseService.modifierreponse(responseToUpdate);
            getReponsesForQuestion(currentQuestion.getId());
        } catch (SQLException ex) {
            System.out.println("Error updating response: " + ex.getMessage());
        }
    }


    @FXML
    private void supprimerreponse(ActionEvent event) {
        reponse responseToDelete = tablereponse.getSelectionModel().getSelectedItem();
        try {
            reponseService.supprimerreponse(responseToDelete);
            getReponsesForQuestion(currentQuestion.getId());
        } catch (SQLException ex) {
            System.out.println("Error deleting response: " + ex.getMessage());
        }
    }

    @FXML
    private void rechercherReponse(KeyEvent event) {
        // Implement the search functionality to filter the responses
        // This is a placeholder for your actual search logic
        String searchText = chercherevField.getText();
        List<reponse> filteredResponses = responseList.stream()
                .filter(r -> r.getDescription().contains(searchText))
                .collect(Collectors.toList());
        tablereponse.setItems(FXCollections.observableArrayList(filteredResponses));
    }

    // This method is called when a row in the table is clicked
    @FXML
    private void choisirreponse(MouseEvent event) {
        reponse selectedResponse = tablereponse.getSelectionModel().getSelectedItem();
        if (selectedResponse != null) {
            idread.setText(String.valueOf(selectedResponse.getId_reponse()));
            idevField.setText(String.valueOf(selectedResponse.getId()));
            iduserField.setText(String.valueOf(selectedResponse.getId_user()));
            descriptionevField.setText(selectedResponse.getDescription());
            fullnameevField.setText(selectedResponse.getFullname());
            // Set the date picker or other fields if necessary
            // datepartField.setValue(LocalDate.parse(selectedResponse.getCreated().toString()));
        }
    }

    private void resetPart() {
        idread.setText("");
        idevField.setText("");
        iduserField.setText("");
        descriptionevField.setText("");
        fullnameevField.setText("");
        // Reset other fields if necessary
        // datepartField.setValue(null);
    }

    // Other methods...
}


 