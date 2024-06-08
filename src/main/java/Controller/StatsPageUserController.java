package Controller;

import Service.UserService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;

public class StatsPageUserController implements Initializable {

    @FXML
    private BarChart<String, Number> barchart;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Genre");
        yAxis.setLabel("Nombre");


        barchart.setTitle("Statistiques des utilisateurs");
        barchart.setLegendVisible(false);
        barchart.getXAxis().setTickLabelFont(Font.font(12));


        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Nombre d'utilisateurs");

        try {
            UserService userService = new UserService();
            Map<String, Integer> genderStats = userService.getGenderStats();


            for (Map.Entry<String, Integer> entry : genderStats.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            barchart.getData().add(series);


            for (XYChart.Data<String, Number> data : series.getData()) {
                Tooltip.install(data.getNode(), new Tooltip(data.getYValue().toString()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void affichage() throws IOException{
        Stage stage = new Stage();
        Parent parent = FXMLLoader.load(getClass().getResource("/UserManagementController.fxml"));


        Scene scene = new Scene(parent);

        stage.setTitle("Dashboard");

        stage.initStyle(StageStyle.UTILITY);
        stage.setScene(scene);

        stage.show();
    }
    @FXML
    void close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        try {
            affichage();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
