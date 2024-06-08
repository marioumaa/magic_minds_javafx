package Controller.Admin;

import Controller.DOutil;
import Entity.Categorie;
import Entity.User;
import Service.CategorieService;
import Service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {

    @FXML
    private ImageView CStatImg3;

    @FXML
    private Label NumC;

    @FXML
    private Label NumE;

    @FXML
    private Label NumP;

    @FXML
    private Label NumU;
    @FXML
    private Pane barPane;

    @FXML
    private Button Courses;

    @FXML
    private Button btn_events;

    @FXML
    private Button btn_forum;

    @FXML
    private Button btn_quizzs;

    @FXML
    private Button btn_store;

    @FXML
    private Button btn_user;

    @FXML
    private Pane piePane;

    @FXML
    private Pane inner_pane;

    @FXML
    private HBox root;

    @FXML
    private AnchorPane side_bar;
    DOutil outil = new DOutil();
    CategorieService categorieService=new CategorieService();
    UserService userService=new UserService();
    @FXML
    void goToCourses(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GestionCours.fxml"));
            Parent cartRoot = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
    void GoToQuiz(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Afficher.fxml"));
            Parent cartRoot = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
    void goToEvent(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenementAdmin.fxml"));
            Parent cartRoot = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
    void goToForum(MouseEvent event) {
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
    void goToStore(MouseEvent event) {
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
    void goToUser(MouseEvent event) {
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


    public  void genderUser(){
        try {
            PieChart pieChart = new PieChart();
            List<User> UL=userService.getAll();
            int f=0;
            int m=0;
            for (User u : UL){
                if (u.getGender().equals("female")){
                    f++;
                }
                else {
                    m++;
                }
            }
            PieChart.Data female = new PieChart.Data("female", f);
            PieChart.Data male = new PieChart.Data("male", m);
            pieChart.getData().addAll(female, male);
            pieChart.setPrefSize(336,370);
            pieChart.setTitle("Gender Distribution");
            piePane.getChildren().add(pieChart);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }
    public void bestCat() {

        CategoryAxis cat = new CategoryAxis();
        NumberAxis nombre = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(cat, nombre);

        barChart.setTitle("Courses number by category");

        List<Categorie> list = FXCollections.observableList(categorieService.getAll());
        list.sort(Comparator.comparing(Categorie::getNbr_cours).reversed());
        List<Categorie> topFiveCategories=new ArrayList<>();
        for (int i = 0; i < list.size() ; i++) {
            Categorie c = list.get(i);
            topFiveCategories.add(c);
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Courses");
        Number number ;
        for (Categorie c : topFiveCategories){
            number=(Number) c.getNbr_cours();
            series.getData().add(new XYChart.Data<>(c.getTitre(),number));
        }
        barChart.getData().add(series);
        barChart.setPrefSize(336,370);
        barPane.getChildren().add(barChart);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NumC.setText(String.valueOf(outil.totalCourses()));
        NumE.setText(String.valueOf(outil.totalEvent()));
        NumP.setText(String.valueOf(outil.totalProduct()));
        NumU.setText(String.valueOf(outil.totalUsers()));
        bestCat();
        genderUser();

    }
}
