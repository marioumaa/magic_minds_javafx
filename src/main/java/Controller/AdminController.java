/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temquestione file, choose Tools | Temquestiones
 * and open the temquestione in the editor.
 */
package Controller;

import Entity.question;
import Entity.reponse;
import Service.questionService;
import Service.reponseService;
import javafx.scene.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * FXML Controller class
 *
 * @author asus
 */
public class AdminController implements Initializable {


    @FXML
    private TableView<reponse> tablereponse;
    questionService ab = new questionService();
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
    private TableView<question> questionTv;
    @FXML
    private TableColumn<question, String> nomevTv;

    @FXML
    private TableColumn<question, String> imageevTv;
    @FXML
    private TableColumn<question, String> dateevTv;

    @FXML
    private TableColumn<question, String> descriptionevTvv;
    @FXML
    private TableColumn<question, String> typeTv;

    reponseService Ps = new reponseService();


    ObservableList<question> evs;
    questionService Ev = new questionService();
    reponseService Pservice = new reponseService();


    @FXML
    private ImageView imageview;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {


        //idLabel.setText("");
        getevs();
        getReponse();
    }


    public void getevs() {
        try {
            // TODO
            List<question> question = Ev.recupererquestion();
            ObservableList<question> olp = FXCollections.observableArrayList(question);
            questionTv.setItems(olp);
            nomevTv.setCellValueFactory(new PropertyValueFactory<>("name"));

            imageevTv.setCellValueFactory(new PropertyValueFactory<>("image"));
            dateevTv.setCellValueFactory(new PropertyValueFactory<>("date"));

            descriptionevTvv.setCellValueFactory(new PropertyValueFactory<>("commentaire"));
            typeTv.setCellValueFactory(new PropertyValueFactory<>("type"));


            // this.delete();
        } catch (SQLException ex) {
            System.out.println("error" + ex.getMessage());
        }
    }//get evs


    @FXML
    private void supprimerquestion(ActionEvent ev) {
        question e = questionTv.getItems().get(questionTv.getSelectionModel().getSelectedIndex());
        try {
            Ev.supprimerquestion(e);
        } catch (SQLException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information ");
        alert.setHeaderText("question delete");
        alert.setContentText("question deleted successfully!");
        alert.showAndWait();
        getevs();
    }


    @FXML
    private void supprimerreponse(ActionEvent ev) {
        reponse p = tablereponse.getItems().get(tablereponse.getSelectionModel().getSelectedIndex());

        try {
            Ps.Deletereponse(p);
        } catch (SQLException ex) {
            Logger.getLogger(AjouterquestionController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information ");
        alert.setHeaderText("reponse delete");
        alert.setContentText("reponse deleted successfully!");
        alert.showAndWait();
        getReponse();

    }

    public void getReponse() {
        try {


            // TODO
            List<reponse> part = Ps.recupererComment();
            ObservableList<reponse> olp = FXCollections.observableArrayList(part);
            tablereponse.setItems(olp);
            iduserTv.setCellValueFactory(new PropertyValueFactory<>("id_user"));
            idevTv.setCellValueFactory(new PropertyValueFactory<>("id"));

            descriptionevTv.setCellValueFactory(new PropertyValueFactory<>("description"));
            fullnameevTv.setCellValueFactory(new PropertyValueFactory<>("fullname"));
            // this.delete();
        } catch (SQLException ex) {
            System.out.println("error" + ex.getMessage());
        }
    }


    @FXML
    public void showStat(ActionEvent actionEvent) {
        try {
            Map<String, Integer> data = ab.countQuestionsByType();
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

            data.forEach((type, count) -> pieChartData.add(new PieChart.Data(type, count)));

            PieChart pieChart = new PieChart(pieChartData);
            pieChart.setTitle("Questions per Type");

            Scene scene = new Scene(pieChart, 800, 600);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Statistics");
            stage.show();
        } catch (SQLException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, "Failed to fetch statistics", ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Error fetching data");
            alert.setContentText("Unable to fetch statistics from the database.");
            alert.showAndWait();
        }
    }

    @FXML
    void goCourses(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionCours.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenementAdmin.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/demo4/Admin.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Afficher.fxml"));
            Parent cartRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the scene of the current stage to the cart view
            Scene scene = new Scene(cartRoot,1050,700);
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
    void goStore(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterProduit.fxml"));
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
    void goUser(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserManagementController.fxml"));
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
    void GoToDashboard(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home_back.fxml"));
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


    





    

