package Service;

import Entity.Evenement;
import Utili.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServiceEvenement implements IService<Evenement> {

    private Connection cnx;

    public ServiceEvenement() {
        cnx = MyDB.getInstance().getConnection();
    }


    @Override
    public void add(Evenement event) {
        if (event == null || event.getDate_debut() == null) {
            System.out.println("Event or its start date is null.");
            return; // or throw an exception if necessary
        }

        // Vérifier que la date de début n'est pas null
        if (event.getDate_debut() == null) {
            System.out.println("Start date of the event is null.");
            return;
        }

        String qry = "INSERT INTO `evenement`(`nb_participant`, `nom`, `description`, `localisation`, `categorie`, `date_debut`, `date_fin`) VALUES (?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);

            pstm.setInt(1, event.getNb_participant());
            pstm.setString(2, event.getNom());
            pstm.setString(3, event.getDescription());
            pstm.setString(4, event.getLocalisation());
            pstm.setString(5, event.getCategorie());
            pstm.setDate(6, new java.sql.Date(event.getDate_debut().getTime()));
            pstm.setDate(7, new java.sql.Date(event.getDate_fin().getTime()));

            pstm.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public ArrayList<Evenement> getAll() {
        ArrayList<Evenement> evenements = new ArrayList<>();
        String qry = "SELECT * FROM `evenement`";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Evenement event = new Evenement();
                event.setId(rs.getInt("id"));
                event.setNb_participant(rs.getInt("nb_participant"));
                event.setNom(rs.getString("nom"));
                event.setDescription(rs.getString("description"));
                event.setLocalisation(rs.getString("localisation"));
                event.setCategorie(rs.getString("categorie"));
                event.setDate_debut(rs.getDate("date_debut"));
                event.setDate_fin(rs.getDate("date_fin"));

                evenements.add(event);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return evenements;
    }


    @Override
    public void update(int id, Evenement event) {
        try {
            String req = "UPDATE `evenement` SET `nb_participant`=?, `nom`=?, `description`=?, `localisation`=?, `categorie`=?, `date_debut`=?, `date_fin`=? WHERE `id`=?";

            PreparedStatement ps = cnx.prepareStatement(req);

            ps.setInt(1, event.getNb_participant());
            ps.setString(2, event.getNom());
            ps.setString(3, event.getDescription());
            ps.setString(4, event.getLocalisation());
            ps.setString(5, event.getCategorie());
            ps.setDate(6, new java.sql.Date(event.getDate_debut().getTime()));
            ps.setDate(7, new java.sql.Date(event.getDate_fin().getTime()));
            ps.setInt(8, id);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Mise à jour réussie pour l'événement avec ID : " + event.getId());
            } else {
                System.out.println("Aucun enregistrement mis à jour pour l'événement avec ID : " + event.getId());
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la mise à jour de l'événement : " + ex.getMessage());
            ex.printStackTrace(); // Affiche la trace de la pile pour obtenir des détails sur l'exception
        }
    }
    @Override
    public boolean delete(Evenement event) {
        String qry = "DELETE FROM `evenement` WHERE `id`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, event.getId());
            int rowsAffected = pstm.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());

            return false;
        }
    }
    // Méthode pour récupérer toutes les catégories disponibles
    public List<String> getAllCategories() {
        Set<String> categoriesSet = new HashSet<>();
        List<Evenement> evenements = getAll();
        for (Evenement evenement : evenements) {
            categoriesSet.add(evenement.getCategorie());
        }
        return new ArrayList<>(categoriesSet);
    }

    // Méthode pour récupérer toutes les localisations disponibles
    public List<String> getAllLocations() {
        Set<String> locationsSet = new HashSet<>();
        List<Evenement> evenements = getAll();
        for (Evenement evenement : evenements) {
            locationsSet.add(evenement.getLocalisation());
        }
        return new ArrayList<>(locationsSet);
    }
    public List<Evenement> filtrerParCategorieEtLocalisation(List<Evenement> evenements, String categorie, String localisation) {
        List<Evenement> evenementsFiltres = new ArrayList<>();
        for (Evenement evenement : evenements) {
            if (evenement.getCategorie().equals(categorie) && evenement.getLocalisation().equals(localisation)) {
                evenementsFiltres.add(evenement);
            }
        }
        return evenementsFiltres;
    }


    public String getEventNameById(int eventId) {
        String eventName = null;
        String query = "SELECT nom FROM evenement WHERE id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, eventId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    eventName = resultSet.getString("nom");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return eventName;
    }
    public List<Evenement> getEventsByCategory(String category) {
        List<Evenement> events = new ArrayList<>();
        String query = "SELECT * FROM evenement WHERE categorie = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, category);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Evenement event = new Evenement();
                    event.setId(resultSet.getInt("id"));
                    event.setNb_participant(resultSet.getInt("nb_participant"));
                    event.setNom(resultSet.getString("nom"));
                    event.setDescription(resultSet.getString("description"));
                    event.setLocalisation(resultSet.getString("localisation"));
                    event.setCategorie(resultSet.getString("categorie"));
                    event.setDate_debut(resultSet.getDate("date_debut"));
                    event.setDate_fin(resultSet.getDate("date_fin"));
                    events.add(event);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return events;
    }

    public Evenement getById(int eventId) {
        Evenement evenement = null;
        String query = "SELECT * FROM evenement WHERE id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, eventId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    evenement = new Evenement();
                    evenement.setId(resultSet.getInt("id"));
                    evenement.setNb_participant(resultSet.getInt("nb_participant"));
                    evenement.setNom(resultSet.getString("nom"));
                    evenement.setDescription(resultSet.getString("description"));
                    evenement.setLocalisation(resultSet.getString("localisation"));
                    evenement.setCategorie(resultSet.getString("categorie"));
                    evenement.setDate_debut(resultSet.getDate("date_debut"));
                    evenement.setDate_fin(resultSet.getDate("date_fin"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return evenement;
    }}

