package Service;

import Entity.Cours;
import Entity.Ressource;
import Utili.MyDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RessourceService  {
    private Connection connection ;
    public RessourceService(){
        connection = MyDB.getInstance().getConnection();
    }

    public void insert(Ressource ressource) throws SQLException {
        PreparedStatement ste = null;
        String sql = "INSERT INTO ressource"
                + "(titre,type,url,id_cours_id) "
                + "VALUES(?,?,?,?)";
        ste = connection.prepareStatement(sql);
        ste.setString(1,ressource.getTitre());
        ste.setString(2,ressource.getType());
        ste.setString(3,ressource.getUrl());
        ste.setInt(4,ressource.getId_cours_id());
        ste.executeUpdate();
    }


    public void update(int id, Ressource ressource)  {
        String sql = "UPDATE ressource SET titre= ?, type = ?,url = ?,id_cours_id = ? WHERE id = ?";
        try {
            PreparedStatement ste = connection.prepareStatement(sql);
            ste.setString(1,ressource.getTitre());
            ste.setString(2,ressource.getType());
            ste.setString(3,ressource.getUrl());
            ste.setInt(4,ressource.getId_cours_id());
            ste.setInt(5,id);
            ste.executeUpdate();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void delete(int id) throws SQLException {
        String sql ="DELETE FROM ressource WHERE id = ?";
        PreparedStatement ste = connection.prepareStatement(sql);
        ste.setInt(1,id);
        ste.executeUpdate();

    }


    public ObservableList<Ressource> getAll() {
        ObservableList<Ressource> chapters = FXCollections.observableArrayList();
        String sql = "SELECT * FROM ressource";
        try {
            Statement ste = connection.createStatement();
            ResultSet resultSet = ste.executeQuery(sql);
            while (resultSet.next()) {
                Ressource r = new Ressource();
                r.setId(resultSet.getInt("id"));
                r.setTitre(resultSet.getString("titre"));
                r.setType(resultSet.getString("type"));
                r.setUrl(resultSet.getString("url"));
                r.setId_cours_id(resultSet.getInt("id_cours_id"));
                chapters.add(r);
            }
        }catch (SQLException e) {
                System.out.println(e.getMessage());}
        return chapters;
    }
    public List<Ressource> getChaptersByCat(int coursId) {
        String sql = "SELECT * FROM ressource WHERE id_cours_id = ?";
        List<Ressource> chaptersByCat = new ArrayList<>();
        try {
            PreparedStatement ste = connection.prepareStatement(sql);
            ste.setInt(1, coursId);
            ResultSet resultSet = ste.executeQuery();
            while (resultSet.next()) {
                Ressource r = new Ressource();
                r.setId(resultSet.getInt("id"));
                r.setTitre(resultSet.getString("titre"));
                r.setType(resultSet.getString("type"));
                r.setUrl(resultSet.getString("url"));
                r.setId_cours_id(resultSet.getInt("id_cours_id"));
                chaptersByCat.add(r);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return chaptersByCat;
    }
}
