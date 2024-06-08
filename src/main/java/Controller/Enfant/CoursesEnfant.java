package Controller.Enfant;

import Entity.Categorie;
import Entity.Cours;
import Entity.Ressource;
import Service.CategorieService;
import Service.CoursService;
import Service.RessourceService;
import Controller.*;
import Controller.Cours.UpdateCours;
import Service.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CoursesEnfant implements Initializable {

    @FXML
    private VBox Containe;

    @FXML
    private Pane Content;

    @FXML
    private HBox bar;

    @FXML
    private VBox editable;

    @FXML
    private Button showProgress;



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
    List<Categorie> Catego = new ArrayList<>();
    List<Cours> COPL=new ArrayList<>();
    @FXML
    private Button progress;

    @FXML
    void seeProgress(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Progress.fxml"));
            Parent updatePageRoot = loader.load();
            Progress progress=loader.getController();
            progress.fetchPData(Catego,COPL);
            progress.initialize(null,null);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);
            Scene newPageScene = new Scene(updatePageRoot,650,500);
            stage.setScene(newPageScene);
            stage.setTitle("Progress");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML

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
        Catego.add(catDetail);

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
            cardC.setUserData(c);
            cardC.setOnMouseClicked(this::cardChaplicked);
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
        Button progressBtn=card.add("See progress");
        bar1.getChildren().add(progressBtn);
        bar=bar1;
        Content.getChildren().add(bar);
        progressBtn.setOnMouseClicked(this::seeProgress);
        pagination();
    }
    private void goBack(MouseEvent mouseEvent) {
        backf();
    }
    private void cardChaplicked(MouseEvent mouseEvent) {
        Pane cardC = (Pane) mouseEvent.getSource();
        coursDetail = (Cours) cardC.getUserData();
        pageContainer.getChildren().clear();
        bar.getChildren().clear();
        //creating the bar
        Button back =card.back_btn();
        Label headchap=card.heading("Chapters List");
        bar.getChildren().addAll(back,headchap);
        back.setOnMouseClicked(this::goBackToCours);
        VBox DandChap=this.showChapter(coursDetail);
        pageContainer.getChildren().add(DandChap);
        COPL.add(coursDetail);

    }

    private VBox showChapter(Cours coursDetail) {
        VBox DandChap=new VBox();

        //cours  details
        Pane CatD=card.CatDP();
        HBox det=card.coursD(coursDetail);
        CatD.getChildren().add(det);


        //affichage des chapitres:
        List<Ressource> ChapList=ressourceService.getChaptersByCat(coursDetail.getId());
        Pane chapterList= new Pane();
        VBox Chaptable=card.table(340);
        HBox Chapline=card.line(120,0);
        int i=0;
        for (Ressource r :ChapList){
            Pane cardChap=card.Chapcarte();
            HBox place=card.Chapbox(r);
            cardChap.getChildren().add(place);
            cardChap.setUserData(r);
            cardChap.setOnMouseClicked(this::previewChap);

            Chapline.getChildren().add(cardChap);
            i++;
            if (i % 3==0){
                Chaptable.getChildren().add(Chapline);
                Chapline=new HBox();
                Chapline=card.line(120,0);
            }
        }
        if (!Chapline.getChildren().isEmpty()){
            Chaptable.getChildren().add(Chapline);
        }
        chapterList.getChildren().add(Chaptable);
        DandChap.getChildren().addAll(CatD,chapterList);
        return DandChap ;
    }
    public void goBackToCoursf(){
        pageContainer.getChildren().clear();
        bar.getChildren().clear();
        HBox bar1=new HBox();
        Button back =card.back_btn();
        Label head=card.heading("Courses List");
        bar1.getChildren().addAll(back,head);
        bar=bar1;
        Content.getChildren().add(bar);
        getCourses();
    }
    private void goBackToCours(MouseEvent mouseEvent) {
        goBackToCoursf();
    }
    private void previewChap(MouseEvent mouseEvent) {
        Pane cardChap = (Pane) mouseEvent.getSource();
        ressourceDetail = (Ressource) cardChap.getUserData();
        if (mouseEvent.getClickCount() == 2) {
            if (ressourceDetail != null) {
                if (ressourceDetail.getType().equals("PDF")) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/PreviewPDF.fxml"));
                        Parent updatePageRoot = loader.load();
                        PreviewPDF previewPDF = loader.getController();
                        previewPDF.fetchPDF(ressourceDetail.getUrl());
                        Stage stage = new Stage();
                        stage.initStyle(StageStyle.UTILITY);
                        Scene newPageScene = new Scene(updatePageRoot, 956, 612);
                        stage.setScene(newPageScene);
                        stage.setTitle("Preview Chapter");
                        stage.show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                else if (ressourceDetail.getType().equals("Video") || ressourceDetail.getType().equals("Image")) {
                    try {
                        FXMLLoader loaderF = new FXMLLoader(getClass().getResource("/PreviewFile.fxml"));
                        Parent updatePageRoot = loaderF.load();
                        PreviewFile previewFile =loaderF.getController();
                        previewFile.fetchUrl(ressourceDetail.getUrl(),ressourceDetail.getType());
                        previewFile.initialize(null, null);
                        Stage stage = new Stage();
                        stage.initStyle(StageStyle.UTILITY);
                        Scene newPageScene = new Scene(updatePageRoot, 850, 612);
                        stage.setScene(newPageScene);
                        stage.setTitle("Preview Chapter");
                        stage.show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pagination();
    }
    @FXML
    void goCourse(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CoursesEnfant.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenementEtudiant.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/demo4/Home_front.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherAllQuizzesEnfant.fxml"));
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
