package Service;

import Entity.Categorie;
import Entity.Cours;
import Utili.MyDB;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.sql.*;
import java.sql.SQLException;
import java.util.List;


public class CategorieService {
    private Connection connection ;

    public CategorieService(){
        connection = MyDB.getInstance().getConnection();
    }


    public void insert(Categorie categorie)  {
        String sql = "INSERT INTO categorie "
                + "(titre,description,image) "
                + "VALUES(?,?,?)";
        try {
            PreparedStatement ste =connection.prepareStatement(sql);
            ste.setString(1,categorie.getTitre());
            ste.setString(2,categorie.getDescription());
            ste.setString(3,categorie.getImage());
            ste.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void update(int id, Categorie categorie) {

        String sql = "UPDATE categorie SET titre= ?, description = ?,image = ? WHERE id = ?";
        try {
            PreparedStatement ste =connection.prepareStatement(sql);
            ste.setString(1,categorie.getTitre());
            ste.setString(2,categorie.getDescription());
            ste.setString(3,categorie.getImage());
            ste.setInt(4,id);
            ste.executeUpdate();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void updateCourse(int id, int nb) {

        String sql = "UPDATE categorie SET nbr_cours= ? WHERE id = ?";
        try {
            PreparedStatement ste =connection.prepareStatement(sql);
            ste.setInt(1,nb);
            ste.setInt(2,id);
            ste.executeUpdate();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void updateChap(int id, int nb) {

        String sql = "UPDATE categorie SET nbr_chapitre= ? WHERE id = ?";
        try {
            PreparedStatement ste =connection.prepareStatement(sql);
            ste.setInt(1,nb);
            ste.setInt(2,id);
            ste.executeUpdate();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void delete(int id) throws SQLException {
        CoursService coursService=new CoursService();
        try {
            List<Cours> CL=coursService.getCoursesByCat(id);
            for (Cours c : CL){
                coursService.delete(c.getId());
            }
            String sql ="DELETE FROM categorie WHERE id = ?";
            PreparedStatement ste =connection.prepareStatement(sql);
            ste.setInt(1,id);
            ste.executeUpdate();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public ObservableList<Categorie> getAll()  {
        ObservableList<Categorie> categories = FXCollections.observableArrayList();
        String sql = "SELECT * FROM categorie";
        try {
            Statement ste = connection.createStatement();
            ResultSet resultSet = ste.executeQuery(sql);
            while (resultSet.next()){
                Categorie c = new Categorie();
                c.setId(resultSet.getInt("id"));
                c.setTitre(resultSet.getString("titre"));
                c.setDescription(resultSet.getString("description"));
                c.setImage(resultSet.getString("image"));
                c.setNbr_cours(resultSet.getInt("nbr_cours"));
                c.setNbr_chapitre(resultSet.getInt("nbr_chapitre"));
                categories.add(c);
            } } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return categories;
    }
    public  ObservableList<String> getCatNames() {
        ObservableList<String> catNames = FXCollections.observableArrayList();
        String sql = "SELECT titre FROM categorie";
        try {
            Statement ste = connection.createStatement();
            ResultSet resultSet = ste.executeQuery(sql);
            while (resultSet.next()){
                catNames.add(resultSet.getString("titre"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return catNames;
    }
    public int getCatId(String catName) {
        String sql = "SELECT id FROM categorie WHERE titre = ?";
        int catId = 0;
        try {
            PreparedStatement ste = connection.prepareStatement(sql);
            ste.setString(1, catName);
            ResultSet resultSet = ste.executeQuery();
            if (resultSet.next()) {
                catId = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return catId;
    }

    public Categorie getbyId(int id) {
        String sql = "SELECT * FROM categorie WHERE id = ?";
        Categorie c = new Categorie();
        try {
            PreparedStatement ste = connection.prepareStatement(sql);
            ste.setInt(1, id);
            ResultSet resultSet = ste.executeQuery();
            if (resultSet.next()) {
                c.setId(resultSet.getInt("id"));
                c.setTitre(resultSet.getString("titre"));
                c.setDescription(resultSet.getString("description"));
                c.setImage(resultSet.getString("image"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return c;
    }


}
