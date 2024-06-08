package Service;

import Entity.Categorie;
import Entity.Cours;
import Entity.Ressource;
import Utili.MyDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoursService  {
    private Connection connection ;
    public CoursService(){
        connection = MyDB.getInstance().getConnection();
    }

    public void insert(Cours cours) throws SQLException {
        PreparedStatement ste = null;
        String sql = "INSERT INTO cours (duree,nb_chapitre,titre,description,status,categorie_id) VALUES(?,?,?,?,?,?)";
        ste = connection.prepareStatement(sql);
        ste.setInt(1,cours.getDuree());
        ste.setInt(2,cours.getNb_chapitre());
        ste.setString(3,cours.getTitre());
        ste.setString(4,cours.getDescription());
        ste.setString(5,cours.getStatus());
        ste.setInt(6,cours.getCategorie_id());
        ste.executeUpdate();
    }


    public void update(int id, Cours cours) {
        String sql = "UPDATE cours SET duree = ?, nb_chapitre = ?, titre = ?, description = ?, status = ?, categorie_id = ? WHERE id = ?";
        try {
            PreparedStatement ste = connection.prepareStatement(sql);
            ste.setInt(1, cours.getDuree());
            ste.setInt(2, cours.getNb_chapitre());
            ste.setString(3, cours.getTitre());
            ste.setString(4, cours.getDescription());
            ste.setString(5, cours.getStatus());
            ste.setInt(6, cours.getCategorie_id());
            ste.setInt(7, id);
            ste.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void delete(int id) throws SQLException {
        RessourceService ressourceService=new RessourceService();
        try {
            List<Ressource> RL = ressourceService.getChaptersByCat(id);
            for (Ressource r : RL) {
                ressourceService.delete(r.getId());
            }
            String sql ="DELETE FROM cours WHERE id = ?";
            PreparedStatement ste =connection.prepareStatement(sql);
            ste.setInt(1,id);
            ste.executeUpdate();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public ObservableList<Cours> getAll()  {
        ObservableList<Cours> cours = FXCollections.observableArrayList();
        String sql = "SELECT * FROM cours";
        try {
            Statement ste = connection.createStatement();
            ResultSet resultSet = ste.executeQuery(sql);
            while (resultSet.next()){
                Cours c = new Cours();
                c.setId(resultSet.getInt("id"));
                c.setDuree(resultSet.getInt("duree"));
                c.setNb_chapitre(resultSet.getInt("nb_chapitre"));
                c.setTitre(resultSet.getString("titre"));
                c.setDescription(resultSet.getString("Description"));
                c.setStatus(resultSet.getString("Status"));
                c.setCategorie_id(resultSet.getInt("categorie_id"));
                cours.add(c);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return cours;
    }
    public List<Cours> getCoursesByCat(int catId) {
        String sql = "SELECT * FROM cours WHERE categorie_id = ?";
        List<Cours> coursesByCat = new ArrayList<>();
        try {
            PreparedStatement ste = connection.prepareStatement(sql);
            ste.setInt(1, catId);
            ResultSet resultSet = ste.executeQuery();
            while (resultSet.next()) {
                Cours c = new Cours();
                c.setId(resultSet.getInt("id"));
                c.setDuree(resultSet.getInt("duree"));
                c.setNb_chapitre(resultSet.getInt("nb_chapitre"));
                c.setTitre(resultSet.getString("titre"));
                c.setDescription(resultSet.getString("Description"));
                c.setStatus(resultSet.getString("Status"));
                c.setCategorie_id(resultSet.getInt("categorie_id"));
                coursesByCat.add(c);
            }
            System.out.println(coursesByCat);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return coursesByCat;
    }
    public  ObservableList<String> getCoursNames() {
        ObservableList<String> coursNames = FXCollections.observableArrayList();
        String sql = "SELECT titre FROM cours";
        try {
            Statement ste = connection.createStatement();
            ResultSet resultSet = ste.executeQuery(sql);
            while (resultSet.next()){
                coursNames.add(resultSet.getString("titre"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return coursNames;
    }
    public int getCourstId(String coursName) {
        String sql = "SELECT id FROM cours WHERE titre = ?";
        int coursId = 0;
        try {
            PreparedStatement ste = connection.prepareStatement(sql);
            ste.setString(1, coursName);
            ResultSet resultSet = ste.executeQuery();
            if (resultSet.next()) {
                coursId = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return coursId;
    }

    public Cours getbyId(int id) {
        String sql = "SELECT * FROM cours WHERE id = ?";
        Cours c = new Cours();
        try {
            PreparedStatement ste = connection.prepareStatement(sql);
            ste.setInt(1, id);
            ResultSet resultSet = ste.executeQuery();
            if (resultSet.next()) {
                c.setId(resultSet.getInt("id"));
                c.setDuree(resultSet.getInt("duree"));
                c.setNb_chapitre(resultSet.getInt("nb_chapitre"));
                c.setTitre(resultSet.getString("titre"));
                c.setDescription(resultSet.getString("description"));
                c.setStatus(resultSet.getString("Status"));
                c.setCategorie_id(resultSet.getInt("categorie_id"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return c;
    }

}
