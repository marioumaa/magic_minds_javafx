package Service;

import Entity.Command;
import Utili.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class CommandCRUD implements PService<Command> {
    private Connection connection;
    public CommandCRUD(){
        connection=MyDB.getInstance().getConnection();
    }
    //INSERT INTO `commande`(`id`, `iduser_id`, `totalprice`) VALUES ('[value-1]','[value-2]','[value-3]')
    //INSERT INTO `commande_produit`(`commande_id`, `produit_id`) VALUES ('[value-1]','[value-2]')
    @Override
    public void ajouter(Command command) throws SQLDataException {
        String commandeQuery = "INSERT INTO commande (iduser_id, totalprice) VALUES (?, ?)";

        try {
            PreparedStatement commandePs = connection.prepareStatement(commandeQuery, Statement.RETURN_GENERATED_KEYS);
            commandePs.setInt(1, command.getId_user());
            commandePs.setDouble(2, command.getTotal());
            commandePs.executeUpdate();

            // Retrieve the generated id from the inserted row
            ResultSet generatedKeys = commandePs.getGeneratedKeys();
            if (generatedKeys.next()) {
                int commandeId = generatedKeys.getInt(1); // Get the auto-generated id

                // Now insert into the commande_produit table for each product ID
                String commandeProduitQuery = "INSERT INTO commande_produit (commande_id, produit_id) VALUES (?, ?)";
                try (PreparedStatement commandeProduitPs = connection.prepareStatement(commandeProduitQuery)) {
                    for (int produitId : command.getId_produit()) {
                        commandeProduitPs.setInt(1, commandeId);
                        commandeProduitPs.setInt(2, produitId);
                        commandeProduitPs.executeUpdate();
                    }
                }
            } else {
                throw new SQLException("Failed to retrieve auto-generated key for commande.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modifier(Command command) throws SQLDataException {

    }

    @Override
    public void supprimer(Command command) throws SQLDataException {
        String req="DELETE FROM commande WHERE id=?";
        try {
            PreparedStatement ps=connection.prepareStatement(req);
            ps.setInt(1,command.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afficher(Command command) throws SQLDataException {

    }

    @Override
    public ArrayList<Command> afficherAll() throws SQLException {
        ArrayList<Command> commands = new ArrayList<>();
        String query = "SELECT c.id, c.iduser_id, c.totalprice, cp.produit_id " +
                "FROM commande c " +
                "LEFT JOIN commande_produit cp ON c.id = cp.commande_id";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            HashMap<Integer, Command> commandMap = new HashMap<>();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                if (!commandMap.containsKey(id)) {
                    int idUser = resultSet.getInt("iduser_id");
                    double totalPrice = resultSet.getDouble("totalprice");
                    ArrayList<Integer> produitIds = new ArrayList<>();
                    commandMap.put(id, new Command(id, idUser, produitIds, totalPrice));
                }
                Command command = commandMap.get(id);
                int produitId = resultSet.getInt("produit_id");
                if (!resultSet.wasNull()) {
                    command.getId_produit().add(produitId);
                }
            }

            commands.addAll(commandMap.values());
        } catch (SQLException e) {
            throw new RuntimeException("Error when fetching commands from database", e);
        }

        return commands;
    }

    public ArrayList<Command> afficherAll(int userId) throws SQLException {
        ArrayList<Command> commands = new ArrayList<>();
        String query = "SELECT c.id, c.iduser_id, c.totalprice, cp.produit_id " +
                "FROM commande c " +
                "LEFT JOIN commande_produit cp ON c.id = cp.commande_id " +
                "WHERE c.iduser_id = ?"; // Filter by user ID

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                HashMap<Integer, Command> commandMap = new HashMap<>();

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    if (!commandMap.containsKey(id)) {
                        double totalPrice = resultSet.getDouble("totalprice");
                        ArrayList<Integer> produitIds = new ArrayList<>();
                        commandMap.put(id, new Command(id, userId, produitIds, totalPrice));
                    }
                    Command command = commandMap.get(id);
                    int produitId = resultSet.getInt("produit_id");
                    if (!resultSet.wasNull()) {
                        command.getId_produit().add(produitId);
                    }
                }

                commands.addAll(commandMap.values());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error when fetching commands from database", e);
        }

        return commands;
    }
    public  String getUsername(int userId) throws SQLException {
        String username = null;
        String query = "SELECT first_name, last_name FROM user WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    username = firstName + " " + lastName;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error when fetching username from database", e);
        }
        return username;
    }

}
