package Controller.Parent;

import Controller.*;
import Entity.Categorie;
import Entity.Cours;
import Entity.Ressource;
import Service.CategorieService;
import Service.CoursService;
import Service.RessourceService;
import Service.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CoursesParent implements Initializable {

    @FXML
    private VBox Containe;

    @FXML
    private Pane Content;

    @FXML
    private HBox bar;

    @FXML
    private VBox editable;

    @FXML
    private Pane pageContainer;
    Cards card=new Cards();
    Outil outil=new Outil();
    CategorieService categorieService=new CategorieService();
    CoursService coursService= new CoursService();
    RessourceService ressourceService= new RessourceService();
    Pagination pagination=new Pagination();
    Categorie catDetail=new Categorie();
    Cours coursDetail=new Cours();
    Ressource ressourceDetail=new Ressource();
    public void pagination(){
        //pagination
        ObservableList<Categorie> CatList= categorieService.getAll();
        int nbr_page=0;
        if (CatList.size()%6==0){
            nbr_page = CatList.size()/6;
        }else {
            nbr_page = (CatList.size()/ 6) + 1;
        }
        pagination.setPrefSize(764,490);
        pagination.setPageCount(nbr_page);
        pagination.setPageFactory(this::getPage);
        pageContainer.getChildren().add(pagination);
    }
    public Pane getPage(int pageIndex){
        ObservableList<ObservableList<Categorie>> allpages=sixBysix();
        ObservableList<Categorie> pages= allpages.get(pageIndex);
        Pane page=card.PageContainer(490);
        VBox table=card.table(490);
        HBox line=card.line(220,41);
        int i=0;
        for (Categorie c : pages){
            Pane carte=card.PCard(c);
            carte.setUserData(c);
            carte.setOnMouseClicked(this::cardClicked);
            line.getChildren().add(carte);
            i++;
            if (i % 3==0){
                table.getChildren().add(line);
                line=new HBox();
                line=card.line(220,41);
            }
        }
        if (!line.getChildren().isEmpty()){
            table.getChildren().add(line);
        }
        page.getChildren().add(table);
        return page ;
    }
    public ObservableList<ObservableList<Categorie>> sixBysix(){
        ObservableList<ObservableList<Categorie>> pages= FXCollections.observableArrayList();
        ObservableList<Categorie> CatList= categorieService.getAll();
        for (int j = 0; j < CatList.size() ; j += 6) {
            int endIndex = Math.min(j + 6, CatList.size());  // Ensure endIndex does not exceed CatList size
            ObservableList<Categorie> trimedList = FXCollections.observableArrayList(
                    CatList.subList(j, endIndex)
            );
            pages.add(trimedList);
        }
        return pages;
    }
    private void cardClicked(MouseEvent mouseEvent) {
        Pane carte = (Pane) mouseEvent.getSource();
        catDetail = (Categorie) carte.getUserData();
        getCourses();

    }
    public void getCourses(){
        pageContainer.getChildren().clear();
        bar.getChildren().clear();
        //creating the bar
        Button back =card.back_btn();
        Label head=card.heading("Courses List");
        bar.getChildren().addAll(back,head);
        back.setOnMouseClicked(this::goBack);
        VBox DandC=this.showCours(catDetail);
        pageContainer.getChildren().add(DandC);
    }
    private VBox showCours(Categorie catDetail){
        VBox DandC=new VBox();

        //categorie details
        Pane CatD=card.CatDP();
        HBox det=card.CatDe(catDetail);
        CatD.getChildren().add(det);


        //affichage des cours:
        List<Cours> CoursList=coursService.getCoursesByCat(catDetail.getId());
        Pane coursesList= new Pane();
        VBox Ctable=card.table(340);
        HBox Cline=card.line(120,0);
        int i=0;
        for (Cours c :CoursList){
            Pane cardC=card.courCard(c);
            Cline.getChildren().add(cardC);
            i++;
            if (i % 3==0){
                Ctable.getChildren().add(Cline);
                Cline=new HBox();
                Cline=card.line(120,0);
            }
        }
        if (!Cline.getChildren().isEmpty()){
            Ctable.getChildren().add(Cline);
        }
        coursesList.getChildren().add(Ctable);
        DandC.getChildren().addAll(CatD,coursesList);
        return DandC ;
    }
    public void backf(){
        pageContainer.getChildren().clear();
        bar.getChildren().clear();
        HBox bar1=card.HEbar();
        bar=bar1;
        Content.getChildren().add(bar);
        pagination();
    }
    private void goBack(MouseEvent mouseEvent) {
        backf();
    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pagination();
    }
    @FXML
    void goToStore(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Store.fxml"));
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
    void goCourse(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CoursesParent.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenementParent.fxml"));
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
