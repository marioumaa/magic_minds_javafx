package Controller;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class test implements Initializable {

    @FXML
    private ComboBox<String> combo;

    @FXML
    private Text label;

    @FXML
    void select(InputMethodEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        combo.setItems(FXCollections.observableArrayList("admin","user"));
    }
    @FXML
    void select2(ActionEvent event) {
        System.out.println(combo.getValue());
        label.setText(combo.getValue());
    }
}
