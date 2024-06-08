package Controller;

import Entity.Command;
import Entity.Produit;
import Service.CommandCRUD;
import Service.ProduitCRUD;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
public class ProductStacController {
    @FXML
    private BarChart<String, Integer> barchart;

    @FXML
    private AnchorPane main_form;
    public void initialize() {
        // Retrieve all commands from the database
        CommandCRUD commandCRUD = new CommandCRUD();
        ArrayList<Command> commands;
        try {
            commands = commandCRUD.afficherAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Calculate product quantities sold
        HashMap<Integer, Integer> productQuantities = new HashMap<>();
        for (Command command : commands) {
            for (int productId : command.getId_produit()) {
                productQuantities.put(productId, productQuantities.getOrDefault(productId, 0) + 1);
            }
        }

        // Create chart data
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        for (int productId : productQuantities.keySet()) {
            Produit produit;
            try {
                produit = new ProduitCRUD().getById(productId);
            } catch (SQLException e) {
                e.printStackTrace();
                continue;
            }
            if (produit != null) {
                series.getData().add(new XYChart.Data<>(produit.getNom(), productQuantities.get(productId)));
            }
        }

        // Set chart data
        barchart.getData().add(series);
    }
}
