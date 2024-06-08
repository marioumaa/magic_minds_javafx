package Controller.Admin;

import Controller.*;
import Entity.*;
import Service.*;
import Controller.Cours.UpdateCours;
import Controller.Ressource.UpdateChapter;
import Controller.Categorie.UpdateCategory;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GestionCours implements Initializable {
    //------------------------All -------------------------------------------------------------------
    @FXML
    private HBox afficherCategrie;
    @FXML
    private TabPane tabpane;
    @FXML
    private Pane dashboard;

    @FXML
    private Button btn_corses;
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
    private TextField txt_search;
    Outil outil= new Outil();
    String tabName="Categories";

    String userDir = System.getProperty("user.dir").replace("\\", "/");
    String trimmedPath = userDir.substring(0, userDir.lastIndexOf("/")).replace("\\", "/");

    public void searchCat(String Svalue){
            ObservableList<Categorie> categories =new CategorieService().getAll();
            if (Svalue.isEmpty()) {
                showCat.setItems(FXCollections.observableArrayList(categories));
            } else {
                ObservableList<Categorie> matchingCat = FXCollections.observableArrayList();
                for (Categorie category : categories) {
                    if (category.getTitre().toLowerCase().contains(Svalue)) {
                        matchingCat.add(category) ;
                    } else if (String.valueOf(category.getNbr_cours()).toLowerCase().contains(Svalue)) {
                        matchingCat.add(category);
                    }
                }
                showCat.setItems(matchingCat);
            }
    }
    public void searchC(String Svalue){
        ObservableList<Cours> courses =new CoursService().getAll();
        if (Svalue.isEmpty()) {
            showCours.setItems(FXCollections.observableArrayList(courses));
        } else {
            ObservableList<Cours> matchingCat = FXCollections.observableArrayList();
            for (Cours cours: courses) {
                Categorie cat =categorieService.getbyId(cours.getCategorie_id());
                String catName = cat.getTitre();
                if (cours.getTitre().toLowerCase().contains(Svalue)) {
                    matchingCat.add(cours);
                } else if (cours.getStatus().toLowerCase().equals(Svalue)) {
                    matchingCat.add(cours);
                } else if (String.valueOf(cours.getNb_chapitre()).toLowerCase().contains(Svalue)) {
                    matchingCat.add(cours);
                } else if (String.valueOf(cours.getDuree()).toLowerCase().contains(Svalue)) {
                    matchingCat.add(cours);
                }else if (catName.toLowerCase().contains(Svalue)){
                    matchingCat.add(cours);
                }
            }
            showCours.setItems(matchingCat);
            }
    }
    public void searchChap(String Svalue){
        ObservableList<Ressource> ressources =new RessourceService().getAll();
        if (Svalue.isEmpty()) {
            showChap.setItems(ressources);
        } else {
            ObservableList<Ressource> matchingChap = FXCollections.observableArrayList();
            for (Ressource r: ressources) {
                Cours c =coursService.getbyId(r.getId_cours_id());
                String cName = c.getTitre();
                if (r.getTitre().toLowerCase().contains(Svalue)) {
                    matchingChap.add(r);
                } else if (r.getType().toLowerCase().contains(Svalue)) {
                    matchingChap.add(r);

                }else if (cName.toLowerCase().contains(Svalue)){
                    matchingChap.add(r);
                }
            }
            showChap.setItems(matchingChap);
        }
    }
    @FXML
    void search(MouseEvent event) {
        txt_search.setStyle("-fx-text-fill: white;");
        String Svalue=txt_search.getText().toLowerCase();
        if (tabName.equals("Categories")){
            searchCat(Svalue);
        }else if (tabName.equals("Courses")){
            searchC(Svalue);
        } else if (tabName.equals("Chapters")) {
            searchChap(Svalue);
        }

    }
    @FXML
    void txtsearch(ActionEvent event) {
        txt_search.setStyle("-fx-text-fill: white;");
        String Svalue=txt_search.getText().toLowerCase();
        if (tabName.equals("Categories")){
            searchCat(Svalue);
        }else if (tabName.equals("Courses")){
            searchC(Svalue);
        } else if (tabName.equals("Chapters")) {
            searchChap(Svalue);
        }
    }
    //---------------------------
//***********************************************Categorie*********************************************
    @FXML
    private TableColumn<Categorie,String> CatAction;
    @FXML
    private ImageView DImg;
    @FXML
    private Label DNbCours;
    @FXML
    private Label DNbchap;
    @FXML
    private Label Ddescrip;
    @FXML
    private Label Dtitle;

    @FXML
    private TableColumn<Categorie, String> catDescCol;
    @FXML
    private TableColumn<Categorie, String> catTitleCol;
    @FXML
    private Button refresh_btn;
    @FXML
    private Button downPDF;
    @FXML
    private TableView<Categorie> showCat;
    CategorieService categorieService =new CategorieService();
    GeneratePDF generatePDF=new GeneratePDF();
    @FXML
    void DownloadPDF(MouseEvent event) {
        DirectoryChooser directoryChooser=new DirectoryChooser();
        directoryChooser.setTitle("Choose Directory");
        File selectedDirectory = directoryChooser.showDialog(downPDF.getScene().getWindow());
        if (selectedDirectory != null) {
            generatePDF.pdf(selectedDirectory.getAbsolutePath());
            System.out.println(selectedDirectory.getAbsolutePath());
            outil.showSAlert("Download Complete", "Your file has been downloaded in the following directory\n "+ selectedDirectory.getAbsolutePath());
        }

    }

    @FXML
    void refreshTab(ActionEvent event) {
        updateCatTable();
    }
    @FXML
    void OpenAdd(MouseEvent event) {
        try {
            Parent addPageRoot = FXMLLoader.load(getClass().getResource("/AddCategory.fxml"));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);
            Scene newPageScene = new Scene(addPageRoot,650,500);
            stage.setScene(newPageScene);
            stage.setTitle("New Category");
            stage.show();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    public void updateCatTable(){
        showCat.setFixedCellSize(40);
        Categorie defaultItem = new Categorie();
        catTitleCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        catDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        ObservableList<Categorie> categories =new CategorieService().getAll();
        showCat.setItems(categories);
        defaultItem = categories.get(0) ;
        CatAction.setCellValueFactory(new PropertyValueFactory<>(""));
        CatAction.setCellFactory(param -> new TableCell() {
            FontAwesomeIconView deleteButton = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
            FontAwesomeIconView updateButton = new FontAwesomeIconView(FontAwesomeIcon.PENCIL);
            {
                deleteButton.setOnMouseClicked(event -> {
                    Categorie category = (Categorie) getTableRow().getItem();
                    deleteCategory(category);
                });
                updateButton.setOnMouseClicked(event -> {
                    Categorie category = (Categorie) getTableRow().getItem();
                    updateCategory(category);
                });
            }
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttonsContainer = new HBox(15);
                    buttonsContainer.setPadding(new Insets(5, 8, 3, 8));
                    buttonsContainer.getChildren().addAll(deleteButton, updateButton);
                    deleteButton.setSize("25px");
                    updateButton.setSize("25px");
                    setGraphic(buttonsContainer);
                }
            }
        });
        if (defaultItem != null) {
            Dtitle.setText(defaultItem.getTitre());
            Ddescrip.setText(defaultItem.getDescription());
            String cati="file:/"+trimmedPath+"/MagicMinds/public/uploads/images/"+defaultItem.getImage();
            cati.replace("\\", "/");
            System.out.println(cati);
            Image CatImg = new Image( cati);
            DImg.setImage(CatImg);
            DNbchap.setText(String.valueOf(defaultItem.getNbr_chapitre()));
            DNbCours.setText(String.valueOf(defaultItem.getNbr_cours()));
        }
    }
    private void updateCategory(Categorie category) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateCategory.fxml"));
            Parent updatePageRoot = loader.load();
            UpdateCategory updateCategory= loader.getController();
            updateCategory.fetchData(category);
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
    private void deleteCategory(Categorie category) {
        try {
            int categoryId = category.getId();
            CategorieService categorieService = new CategorieService();
            boolean confirm =outil.DeleteAlert("Delete","Delete category "+category.getTitre(),true);
            if (confirm){
                categorieService.delete(categoryId);
                showCat.getItems().remove(category);
                String cati="file:/"+trimmedPath+"/MagicMinds/public/uploads/images/"+category.getImage();
                cati.replace("\\", "/");
                Path ImgToDelete = Paths.get(cati);
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




    //*****************************************************Cours**************************************************

    @FXML
    private Button refreshC_btn;
    @FXML
    private TableView<Cours> showCours;
    @FXML
    private TableColumn<Cours, String> coursTitleCol;
    @FXML
    private TableColumn<Cours, String> coursDescCol;
    @FXML
    private TableColumn<Cours, String> coursStatusCol;
    @FXML
    private TableColumn<Cours,String> CoursAction;
    @FXML
    private Label DCtitle;
    @FXML
    private Label Dperiode;
    @FXML
    private Label DCdescrip;
    @FXML
    private Label DCNbchap;
    @FXML
    private Label Dcat;
    CoursService coursService=new CoursService();

    @FXML
    void refreshCTab(ActionEvent event){refreshCoursTab();}


    @FXML
    void OpenAddCours(MouseEvent event) {
        try {
            Parent addCPageRoot = FXMLLoader.load(getClass().getResource("/AddCours.fxml"));
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);
            Scene newPageScene = new Scene(addCPageRoot,650,500);
            stage.setScene(newPageScene);
            stage.setTitle("New Course");
            stage.show();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    public void refreshCoursTab(){
        showCours.setFixedCellSize(40);
        Cours defaultCours = new Cours();
        coursTitleCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        coursDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        coursStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        ObservableList<Cours> cours =new CoursService().getAll();
        showCours.setItems(cours);
        defaultCours = cours.get(0) ;
        CoursAction.setCellValueFactory(new PropertyValueFactory<>(""));
        CoursAction.setCellFactory(param -> new TableCell() {
            FontAwesomeIconView Cdel_btn = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
            FontAwesomeIconView Cup_btn = new FontAwesomeIconView(FontAwesomeIcon.PENCIL);
            FontAwesomeIconView CInValid_btn = new FontAwesomeIconView(FontAwesomeIcon.MINUS_CIRCLE);
            FontAwesomeIconView CValid_btn = new FontAwesomeIconView(FontAwesomeIcon.CHECK_CIRCLE);
            {
                Cdel_btn.setOnMouseClicked(event -> {
                    Cours cours1 = (Cours) getTableRow().getItem();
                    deleteCours(cours1);
                });
                Cup_btn.setOnMouseClicked(event -> {
                    Cours cours1 = (Cours) getTableRow().getItem();
                    updateCours(cours1);
                });
                CInValid_btn.setOnMouseClicked(event -> {
                    Cours cours1 = (Cours) getTableRow().getItem();
                    InValidCours(cours1);
                    refreshCoursTab();
                });
                CValid_btn.setOnMouseClicked(event -> {
                    Cours cours1 = (Cours) getTableRow().getItem();
                    ValidCours(cours1);
                    refreshCoursTab();
                });

            }
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttonsContainer = new HBox(10);
                    buttonsContainer.setPadding(new Insets(5, 8, 3, 8));
                    buttonsContainer.getChildren().addAll(Cdel_btn, Cup_btn,CInValid_btn,CValid_btn);
                    Cdel_btn.setSize("22px");
                    Cup_btn.setSize("22px");
                    CInValid_btn.setSize("22px");
                    CValid_btn.setSize("22px");
                    setGraphic(buttonsContainer);
                }
            }
        });
        if (defaultCours != null) {
            DCtitle.setText(defaultCours.getTitre());
            DCdescrip.setText(defaultCours.getDescription());
            DCNbchap.setText(String.valueOf(defaultCours.getNb_chapitre()));
            Dperiode.setText(String.valueOf(defaultCours.getDuree()));
            Categorie cat=categorieService.getbyId(defaultCours.getCategorie_id());
            Dcat.setText(cat.getTitre());
        }
    }
    private void updateCours(Cours cours) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateCours.fxml"));
            Parent updatePageRoot = loader.load();
            UpdateCours updateCours= loader.getController();
            updateCours.fetchCData(cours);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);
            Scene newPageScene = new Scene(updatePageRoot,650,500);
            stage.setScene(newPageScene);
            stage.setTitle("Update Course");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void deleteCours(Cours cours) {
        try {
            boolean confirm =outil.DeleteAlert("Delete","Delete course "+cours.getTitre(),true);
            if (confirm){
                Categorie cat=categorieService.getbyId((cours.getCategorie_id()));
                categorieService.updateCourse(cat.getId(),outil.NbCourse(cat.getId()));
                coursService.delete(cours.getId());
                showCours.getItems().remove(cours);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private  void ValidCours(Cours cours){
        cours.setStatus("Valid");
        coursService.update(cours.getId(),cours);
    }
    private void InValidCours(Cours cours){
        cours.setStatus("Invalid");
        coursService.update(cours.getId(),cours);
    }
    //*************************************************CHAPTER********************************************************
    @FXML
    private Button refreshChaop_btn;
    @FXML
    private TableColumn<Ressource,String> chapFile;
    @FXML
    private TableColumn<Ressource, String> chapTitleCol;

    @FXML
    private TableColumn<Ressource, String> chapType;
    @FXML
    private TableColumn<Ressource, String> ChapAction;
    @FXML
    private TableView<Ressource> showChap;
    @FXML
    void OpenAddChap(MouseEvent event) {
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
    @FXML
    void refreshChapTab(ActionEvent event) {updateChapTab();}
    public void updateChapTab(){
        showChap.setFixedCellSize(40);
        chapTitleCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        chapFile.setCellValueFactory(new PropertyValueFactory<>("url"));
        chapType.setCellValueFactory(new PropertyValueFactory<>("type"));
        ObservableList<Ressource> chapters =new RessourceService().getAll();
        showChap.setItems(chapters);
        ChapAction.setCellValueFactory(new PropertyValueFactory<>(""));
        ChapAction.setCellFactory(param -> new TableCell() {
            FontAwesomeIconView Chapdel_btn = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
            FontAwesomeIconView Chapup_btn = new FontAwesomeIconView(FontAwesomeIcon.PENCIL);

            {
                Chapdel_btn.setOnMouseClicked(event -> {
                    Ressource ressource = (Ressource) getTableRow().getItem();
                    deleteChap(ressource);
                });
                Chapup_btn.setOnMouseClicked(event -> {
                    Ressource ressource = (Ressource) getTableRow().getItem();
                    updateChap(ressource);
                });
            }
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttonsContainer = new HBox(15);
                    buttonsContainer.setPadding(new Insets(5, 8, 3, 8));
                    buttonsContainer.getChildren().addAll(Chapdel_btn, Chapup_btn);
                    Chapdel_btn.setSize("25px");
                    Chapup_btn.setSize("25px");
                    setGraphic(buttonsContainer);
                }
            }
        });

    }
    private void updateChap(Ressource ressource) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateChapter.fxml"));
            Parent updatePageRoot = loader.load();
            UpdateChapter updateChapter= loader.getController();
            updateChapter.fetchChapData(ressource);
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
    private void deleteChap(Ressource ressource) {
        try {
            int chapId = ressource.getId();
            RessourceService ressourceService = new RessourceService();
            boolean confirm =outil.DeleteAlert("Delete","Delete chapter "+ressource.getTitre(),true);
            if (confirm){
                ressourceService.delete(chapId);
                showChap.getItems().remove(ressource);
                String mediaFile=trimmedPath+"/MagicMinds/public/uploads/ressources/"+ressource.getUrl();
                mediaFile.replace("\\", "/");
                Path FileToDelete = Paths.get(mediaFile);
                try {
                    Files.delete(FileToDelete);
                }catch (IOException e){
                    e.printStackTrace();
                }
                Cours cours=coursService.getbyId(ressource.getId_cours_id());
                int id =cours.getCategorie_id();
                categorieService.updateChap(id,outil.NbChap(id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void showDetails(){
        showChap.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Ressource selectedRessource = showChap.getSelectionModel().getSelectedItem();
                if (selectedRessource != null) {
                    if (selectedRessource.getType().equals("PDF")) {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PreviewPDF.fxml"));
                            Parent updatePageRoot = loader.load();
                            PreviewPDF previewPDF = loader.getController();
                            System.out.println(selectedRessource.getUrl());
                            previewPDF.fetchPDF(selectedRessource.getUrl());
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
                    else if (selectedRessource.getType().equals("Video") || selectedRessource.getType().equals("Image")) {
                        System.out.println(selectedRessource.getType() + "  here type");
                        System.out.println(selectedRessource.getUrl() + "  here url");
                        try {
                            FXMLLoader loaderF = new FXMLLoader(getClass().getResource("/PreviewFile.fxml"));
                            Parent updatePageRoot = loaderF.load();
                            PreviewFile previewFile =loaderF.getController();
                            previewFile.fetchUrl(selectedRessource.getUrl(),selectedRessource.getType());
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
        });
    }


//________________________________________________________________________________________________________________________
    public void initialize (URL url, ResourceBundle resourceBundle) {
        FontAwesomeIconView refreshBtn = new FontAwesomeIconView(FontAwesomeIcon.REFRESH,"20");
        tabpane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            tabName=newTab.getText();
        });

        //-----------------------------------CATEGORY-----------------------------------------------------------
        refresh_btn.setGraphic(refreshBtn);
        updateCatTable();
        if (txt_search.getText()==null||txt_search.getText().isEmpty()){
            updateCatTable();
        }
        showCat.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Dtitle.setText(newSelection.getTitre());
                Ddescrip.setText(newSelection.getDescription());
                String cati="file:/"+trimmedPath+"/MagicMinds/public/uploads/images/"+newSelection.getImage();
                cati.replace("\\", "/");
                Image DCatImg = new Image(cati);
                DImg.setImage(DCatImg);
                DNbchap.setText(String.valueOf(newSelection.getNbr_chapitre()));
                DNbCours.setText(String.valueOf(newSelection.getNbr_cours()));
            } else {
                Dtitle.setText(" ");
                Ddescrip.setText(" ");
                DNbchap.setText(" ");
                DNbCours.setText(" ");
            }
        });
        //---------------------------------------COURS----------------------------------------------------------
        FontAwesomeIconView refreshBtnvours = new FontAwesomeIconView(FontAwesomeIcon.REFRESH,"20");
        refreshC_btn.setGraphic(refreshBtnvours);
        refreshCoursTab();
        showCours.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                DCtitle.setText(newSelection.getTitre());
                DCdescrip.setText(newSelection.getDescription());
                DCNbchap.setText(String.valueOf(newSelection.getNb_chapitre()));
                Dperiode.setText(String.valueOf(newSelection.getDuree()));
                Categorie cat=categorieService.getbyId(newSelection.getCategorie_id());
                Dcat.setText(cat.getTitre());
            } else {
                DCtitle.setText(" ");
                DCdescrip.setText(" ");
                DCNbchap.setText(" ");
                Dperiode.setText(" ");
                Dcat.setText(" ");
            }
        });
        //-------------------------------------------CHAPTERS------------------------------------------------------
        FontAwesomeIconView refreshBtnchap = new FontAwesomeIconView(FontAwesomeIcon.REFRESH,"20");
        refreshChaop_btn.setGraphic(refreshBtnchap);
        updateChapTab();
        showDetails();
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

   

