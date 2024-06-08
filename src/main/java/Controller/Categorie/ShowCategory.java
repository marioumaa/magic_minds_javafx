package Controller.Categorie;

import Entity.Categorie;
import Service.CategorieService;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;



public class ShowCategory  implements Initializable {
    @FXML
    private Button UpdateCatBtn;

    @FXML
    private Button deleteCatBtn;

    @FXML
    private TableView<Categorie> showCat;
    @FXML
    private TableColumn<Categorie, String> catDescCol;


    @FXML
    private TableColumn<Categorie, String> catTitleCol;



    @FXML
    void DeleteCat(MouseEvent event) {

    }

    @FXML
    void updateCat(MouseEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        catTitleCol.setCellValueFactory(new PropertyValueFactory<>("titre"));
        catDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        ObservableList<Categorie> categories =new CategorieService().getAll();
        showCat.setItems(categories);

    }
}

