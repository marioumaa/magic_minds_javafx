/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temquestione file, choose Tools | Temquestiones
 * and open the temquestione in the editor.
 */
package Controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


/**
 * FXML Controller class
 *
 * @author asus
 */
public class RunController implements Initializable {

    @FXML
    private Label idLabel;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {


        //idLabel.setText("");

    }


    @FXML
    private void Eleve(ActionEvent ev) {
        try {
            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/demo4/Home_front.fxml")));
            idLabel.getScene().setRoot(loader);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @FXML
    private void Prof(ActionEvent ev) {
        try {
            //navigation
            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/demo4/Home_front_Prof_eleve.fxml")));
            idLabel.getScene().setRoot(loader);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void Admin(ActionEvent ev) {
        try {
            //navigation
            Parent loader = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/demo4/Admin.fxml")));
            idLabel.getScene().setRoot(loader);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


}


    





    

