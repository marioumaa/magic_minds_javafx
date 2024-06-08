package Controller.Professeur;

import Controller.*;
import Entity.Categorie;
import Entity.Cours;
import Entity.Ressource;
import Service.CategorieService;
import Service.CoursService;
import Service.RessourceService;
import Controller.Categorie.UpdateCategory;
import Controller.Cours.AddCours;
import Controller.Cours.UpdateCours;
import Controller.Ressource.UpdateChapter;
import Service.SessionManager;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CoursesProf implements Initializable {

    @FXML
    private Pane Content;

    @FXML
    private Label Home;

    @FXML
    private Button PaddCat;

    @FXML
    private Pane pageContainer;
    @FXML
    private HBox bar;

    @FXML
    private Button refreshCat;
    Cards card=new Cards();
    Outil outil=new Outil();
    CategorieService categorieService=new CategorieService();
    CoursService coursService= new CoursService();
    RessourceService ressourceService= new RessourceService();
    Pagination pagination=new Pagination();
    Categorie catDetail=new Categorie();
    Cours coursDetail=new Cours();
    Ressource ressourceDetail=new Ressource();

    String who="Prof";
//----------------------------------------------Category----------------------------------------------------
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
        ObservableList<ObservableList<Categorie>> pages=FXCollections.observableArrayList();
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
    private void updateCat(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateCategory.fxml"));
            Parent updatePageRoot = loader.load();
            UpdateCategory updateCategory=loader.getController();
            updateCategory.fetchData(catDetail);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);
            Scene newPageScene = new Scene(updatePageRoot,650,500);
            stage.setScene(newPageScene);
            stage.setTitle("Update Category");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void addCat(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddCategory.fxml"));
            Parent updatePageRoot = loader.load();
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);
            Scene newPageScene = new Scene(updatePageRoot,650,500);
            stage.setScene(newPageScene);
            stage.setTitle("Add New Category");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    void refresh(MouseEvent event) {
        pagination();
    }
    private void delteCat(MouseEvent mouseEvent) {
        try {
            System.out.println(catDetail);
            int categoryId = catDetail.getId();
            CategorieService categorieService = new CategorieService();
            boolean confirm =outil.DeleteAlert("Delete","Delete category "+catDetail.getTitre(),true);
            if (confirm){
                backf();
                categorieService.delete(categoryId);
                Path ImgToDelete = Paths.get(catDetail.getImage());
                try {
                    Files.delete(ImgToDelete);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//-------------------------------------------------------Course-----------------------------------------
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
        Button refraichire=card.refr();
        Button addCours=card.add("Add New Course");
        Label test=new Label("");
        test.getStylesheets().add("HStyle.css");
        test.getStyleClass().add("test");
        bar.getChildren().addAll(back,head,test,refraichire,addCours);
        back.setOnMouseClicked(this::goBack);
        refraichire.setOnMouseClicked(this::refreshC);
        addCours.setOnMouseClicked(this::addCours);

        VBox DandC=this.showCours(catDetail);
        pageContainer.getChildren().add(DandC);
    }
    private VBox showCours(Categorie catDetail){
        VBox DandC=new VBox();

        //categorie details
        Pane CatD=card.CatDP();
        HBox crt=new HBox();
        HBox det=card.CatDe(catDetail);
        HBox action=new HBox();
        Button del=card.deleteC();
        Button up=card.UpdateC();
        action.getChildren().addAll(del,up);
        action.setAlignment(Pos.CENTER_LEFT);
        crt.getChildren().addAll(det,action);
        CatD.getChildren().add(crt);


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
        del.setOnMouseClicked(this::delteCat);
        up.setOnMouseClicked(this::updateCat);
        return DandC ;
    }

    private void addCours(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddCours.fxml"));
            Parent updatePageRoot = loader.load();
            AddCours addCours=loader.getController();
            addCours.fetch(who);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);
            Scene newPageScene = new Scene(updatePageRoot,650,500);
            stage.setScene(newPageScene);
            stage.setTitle("Add New Course");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void refreshC(MouseEvent mouseEvent) {
        Categorie catedit=categorieService.getbyId(catDetail.getId());
        VBox DandC=this.showCours(catedit);
        pageContainer.getChildren().add(DandC);
    }
    public void backf(){
        pageContainer.getChildren().clear();
        bar.getChildren().clear();
        HBox bar1=card.Hbar();
        Button addCourse=new Button("Add New Category");
        addCourse.getStylesheets().add("HStyle.css");
        addCourse.getStyleClass().add("Add");
        bar1.getChildren().add(3,addCourse);
        bar=bar1;
        Content.getChildren().add(bar);
        pagination();
        addCourse.setOnMouseClicked(this::addCat);
    }
    private void updateCours(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateCours.fxml"));
            Parent updatePageRoot = loader.load();
            UpdateCours updateCours=loader.getController();
            updateCours.fetchCData(coursDetail);
            updateCours.fetwho(who);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);
            Scene newPageScene = new Scene(updatePageRoot,650,500);
            stage.setScene(newPageScene);
            stage.setTitle("Update Cours");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void goBack(MouseEvent mouseEvent) {
        backf();
    }
    private void delteCours(MouseEvent mouseEvent) {
        try {
            boolean confirm =outil.DeleteAlert("Delete","Delete course "+coursDetail.getTitre(),true);
            if (confirm){
                goBackToCoursf();
                Categorie cat=categorieService.getbyId((coursDetail.getCategorie_id()));
                categorieService.updateCourse(cat.getId(),outil.NbCourse(cat.getId()));
                coursService.delete(coursDetail.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
//-------------------------------------------------Chapter------------------------------------------
    private void cardChaplicked(MouseEvent mouseEvent) {
        Pane cardC = (Pane) mouseEvent.getSource();
        coursDetail = (Cours) cardC.getUserData();
        pageContainer.getChildren().clear();
        bar.getChildren().clear();
        //creating the bar
        Button back =card.back_btn();
        Label headchap=card.heading("Chapters List");
        Button refraich=card.refr();
        Button addChap=card.add("Add New Chapters");
        Label test=new Label("");
        test.getStylesheets().add("HStyle.css");
        test.getStyleClass().add("test");
        bar.getChildren().addAll(back,headchap,test,refraich,addChap);
        back.setOnMouseClicked(this::goBackToCours);
        refraich.setOnMouseClicked(this::refreshChap);
        addChap.setOnMouseClicked(this::addChap);

        VBox DandChap=this.showChapter(coursDetail);
        pageContainer.getChildren().add(DandChap);
        
    }

    private VBox showChapter(Cours coursDetail) {
        VBox DandChap=new VBox();

        //cours  details
        Pane CatD=card.CatDP();
        HBox crt=new HBox();
        crt.setSpacing(190);
        HBox det=card.coursD(coursDetail);
        HBox actionChap=new HBox();
        Button del=card.deleteC();
        Button up=card.UpdateC();
        actionChap.getChildren().addAll(del,up);
        actionChap.setAlignment(Pos.CENTER_LEFT);
        crt.getChildren().addAll(det,actionChap);
        CatD.getChildren().add(crt);


        //affichage des chapitres:
        List<Ressource> ChapList=ressourceService.getChaptersByCat(coursDetail.getId());
        Pane chapterList= new Pane();
        VBox Chaptable=card.table(340);
        HBox Chapline=card.line(120,0);
        int i=0;
        for (Ressource r :ChapList){
            Pane cardChap=card.Chapcarte();
            HBox place=card.Chapbox(r);
            Button chapD=card.deleteChap();
            chapD.setUserData(r);
            Button chapUp=card.UpdateChap();
            chapUp.setUserData(r);
            place.getChildren().addAll(chapD,chapUp);
            cardChap.getChildren().add(place);
            cardChap.setUserData(r);
            cardChap.setOnMouseClicked(this::previewChap);
            chapD.setOnMouseClicked(this::deleteChap);
            chapUp.setOnMouseClicked(this::updateChap);
            
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
        del.setOnMouseClicked(this::delteCours);
        up.setOnMouseClicked(this::updateCours);
        return DandChap ;
    }

    private void updateChap(MouseEvent mouseEvent) {
        Button chapUp = (Button) mouseEvent.getSource();
        ressourceDetail = (Ressource) chapUp.getUserData();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateChapter.fxml"));
            Parent updatePageRoot = loader.load();
            UpdateChapter updateChapter= loader.getController();
            updateChapter.fetchChapData(ressourceDetail);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);
            Scene newPageScene = new Scene(updatePageRoot,650,400);
            stage.setScene(newPageScene);
            stage.setTitle("Update Chapter");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteChap(MouseEvent mouseEvent) {
        Button chapD = (Button) mouseEvent.getSource();
        ressourceDetail = (Ressource) chapD.getUserData();
        try {
            int chapId = ressourceDetail.getId();
            RessourceService ressourceService = new RessourceService();
            boolean confirm =outil.DeleteAlert("Delete","Delete chapter "+ressourceDetail.getTitre(),true);
            if (confirm){
                frechChap();
                ressourceService.delete(chapId);
                Path FileToDelete = Paths.get(ressourceDetail.getUrl());
                try {
                    Files.delete(FileToDelete);
                }catch (IOException e){
                    e.printStackTrace();
                }
                Cours cours=coursService.getbyId(ressourceDetail.getId_cours_id());
                int id =cours.getCategorie_id();
                categorieService.updateChap(id,outil.NbChap(id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                        System.out.println(ressourceDetail.getUrl());
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


    private void addChap(MouseEvent mouseEvent) {
        try {
            Parent addChapPageRoot = FXMLLoader.load(getClass().getResource("/AddChapter.fxml"));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);
            Scene newPageScene = new Scene(addChapPageRoot,650,400);
            stage.setScene(newPageScene);
            stage.setTitle("New Chapter");
            stage.show();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    public void frechChap(){
        Cours coured=coursService.getbyId(coursDetail.getId());
        VBox DandChap=this.showChapter(coured);
        pageContainer.getChildren().add(DandChap);
    }
    private void refreshChap(MouseEvent mouseEvent) {
        frechChap();
    }
    public void goBackToCoursf(){
        pageContainer.getChildren().clear();
        bar.getChildren().clear();
        HBox bar1=new HBox();
        Button back =card.back_btn();
        Label head=card.heading("Courses List");
        Button refraichire=card.refr();
        Button addCours=card.add("Add New Course");
        Label test=new Label("");
        test.getStylesheets().add("HStyle.css");
        test.getStyleClass().add("test");
        bar1.getChildren().addAll(back,head,test,refraichire,addCours);
        bar=bar1;
        Content.getChildren().add(bar);
        getCourses();
        addCours.setOnMouseClicked(this::addCat);
    }
    private void goBackToCours(MouseEvent mouseEvent) {
        goBackToCoursf();
    }


    @FXML
    void goToHome(MouseEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FontAwesomeIconView RIcon = new FontAwesomeIconView(FontAwesomeIcon.REFRESH,"20");
        refreshCat.setGraphic(RIcon);
        pagination();

    }
    @FXML
    void goCourse(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CoursesProf.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvenement.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/demo4/Home_front_Prof_eleve.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherQuizProf.fxml"));
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
