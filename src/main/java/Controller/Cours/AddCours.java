package Controller.Cours;

import Entity.Categorie;
import Entity.Cours;
import Controller.Outil;
import Service.CategorieService;
import Service.CoursService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddCours implements Initializable {

    @FXML
    private AnchorPane AddCourScene;
    @FXML
    private Label ECChap;

    @FXML
    private Label ECDesc;

    @FXML
    private Label ECtitre;

    @FXML
    private Button InsertCour_btn;

    @FXML
    private ChoiceBox<String> addCCat;

    @FXML
    private Spinner<Integer> addCChap;

    @FXML
    private TextArea addCDescrip;

    @FXML
    private Spinner<Integer> addCPer;

    @FXML
    private TextField addCTiltle;

    @FXML
    private Button goBack_btn;
    CategorieService categorieService=new CategorieService() ;
    CoursService coursService=new CoursService();
    ObservableList<Cours> courses = FXCollections.observableArrayList();
    Outil outil =new Outil();
    String who="";
    public void fetch(String user){
        who=user ;
        System.out.println(who);
    }

    @FXML
    void addCours(MouseEvent event) throws SQLException {
        if (isValid()==true) {
            Cours c = new Cours();
            c.setTitre(addCTiltle.getText());
            c.setDescription(addCDescrip.getText());
            c.setDuree(addCPer.getValue());
            c.setNb_chapitre(addCChap.getValue());
            String catName=addCCat.getValue();
            Categorie cat=categorieService.getbyId(categorieService.getCatId(catName));
            c.setCategorie_id(cat.getId());
            c.setStatus("Valid");
            coursService.insert(c);
            categorieService.updateCourse(cat.getId(),outil.NbCourse(cat.getId()));
            outil.showSAlert("Addition Success", "The course "+c.getTitre()+" was successfully added");
            ECtitre.setText("");
            ECChap.setText("");
        }

    }
    public boolean isValid(){

            Cours c = new Cours();
            Categorie cat = new Categorie();
            String title = addCTiltle.getText();
            String descrip = addCDescrip.getText();
            int chap=addCChap.getValue();
            int per=addCPer.getValue();
            ECtitre.setText("");
            ECDesc.setText("");
            ECChap.setText("");

            if (title == null || title.isEmpty()) {
                ECtitre.setText("The title can't be empty");
                return false;
            }
            if (title.length() < 3 || title.length() > 20) {
                ECtitre.setText("The title is either too long or too short");
                return false;
            }
            for (Cours cours : courses = coursService.getAll()) {
                if (addCTiltle.getText().equals(cours.getTitre())) {
                    ECtitre.setText("This title already exist");
                    return false;
                }
            }
            if (chap==0 || per==0) {
                ECChap.setText("The number of chapters or periode can't be zero");
                return false;
            }
            if (descrip == null || descrip.isEmpty()) {
                ECDesc.setText("The description can't be empty");
                return false;
            }
            if (descrip.length() < 10 || descrip.length() > 150) {
                ECDesc.setText("The description is either too long or too short");
                return false;
            }
            if (chap > per){
                ECChap.setText(" the number of chapters can't be more then the period");
                return false;
            }
            return true ;

    }
    @FXML
    void goBack(MouseEvent event) {
        Stage closestage = (Stage) goBack_btn.getScene().getWindow();
        closestage.close();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addCCat.setItems(categorieService.getCatNames());
        addCCat.setValue(categorieService.getCatNames().get(0));
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20);
        SpinnerValueFactory<Integer> valueCHAPFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20);
        addCPer.setValueFactory(valueFactory);
        addCChap.setValueFactory(valueCHAPFactory);
    }

}
