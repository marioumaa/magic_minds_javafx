package Service;

import Entity.Participation;
import Utili.MyDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServiceParticipation implements IService<Participation> {

    private final Connection cnx;

    public ServiceParticipation() {
        cnx = MyDB.getInstance().getConnection();
    }


    @Override
    public void add(Participation participation) {
        // Implémentez la logique d'ajout ici
        String query = "INSERT INTO participation (date, heure, id_user_id, id_evenement_id) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = cnx.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setDate(1, new java.sql.Date(participation.getDate().getTime()));
            preparedStatement.setTime(2, participation.getHeure());
            preparedStatement.setInt(3, participation.getId_user_id());
            preparedStatement.setInt(4, participation.getEvenementId()); // Set the evenementId field
            preparedStatement.executeUpdate();

            // Retrieve the generated keys to obtain the ID of the newly inserted record
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                participation.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public ArrayList<Participation> getAll() {
        // Implémentez la logique de récupération de toutes les participations ici
        ArrayList<Participation> participations = new ArrayList<>();
        String query = "SELECT * FROM participation";
        try {
            PreparedStatement preparedStatement = cnx.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Participation participation = new Participation();
                participation.setId(resultSet.getInt("id"));
                participation.setDate(resultSet.getDate("date"));
                participation.setHeure(resultSet.getTime("heure"));
                participation.setEvenementId(resultSet.getInt("id_evenement_id"));
                participation.setId_user_id(resultSet.getInt("id_user_id"));

                participations.add(participation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participations;
    }

    @Override
    public boolean delete(Participation participation) {
        // Implémentez la logique de suppression ici
        String query = "DELETE FROM participation WHERE id = ?";
        try {
            PreparedStatement preparedStatement = cnx.prepareStatement(query);
            preparedStatement.setInt(1, participation.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public void update(int id ,Participation participation) {
        // Laissez le corps de cette méthode vide ou lancez une UnsupportedOperationException
        // si vous ne souhaitez pas permettre l'update
    }

}
