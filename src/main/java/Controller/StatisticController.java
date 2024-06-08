package Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.Pane;
public class StatisticController {
    @FXML
    private Pane chartPane;
    @FXML
    private PieChart pieChart;

    public void initialize() {
        // Initialise les données du pie chart
        setData(0, 0); // Remplacez les valeurs 0 par les valeurs réelles
    }

    public void setData(int quizzesPassed, int remainingQuizzes) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Quizzes Passed", quizzesPassed),
                new PieChart.Data("Remaining Quizzes", remainingQuizzes)
        );

        pieChart.setData(pieChartData);

    }
}