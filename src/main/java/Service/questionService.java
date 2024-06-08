/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temquestione file, choose Tools | Temquestiones
 * and open the temquestione in the editor.
 */
package Service;

//import com.sun.javafx.iio.ImageStorage.ImageType;

import Entity.question;
import Utili.MyDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author asus
 */
public class questionService implements IquestionService<question> {

    Connection cnx;
    public Statement ste;
    public PreparedStatement pst;

    public questionService() {
        cnx = MyDB.getInstance().getConnection();

    }

    @Override
    public void ajouterquestion(question e) throws SQLException {

        String requete = "INSERT INTO `questionfiras` (`name`,`image`,`commentaire`,`type`,`date`) "
                + "VALUES (?,?,?,?,?);";
        try {
            pst = (PreparedStatement) cnx.prepareStatement(requete);
            pst.setString(1, e.getName());

            pst.setString(2, e.getImage());
            pst.setString(3, e.getCommentaire());
            pst.setString(4, e.getType());
            pst.setDate(5, e.getDate());


            pst.executeUpdate();
            System.out.println("ev " + e.getName() + " added successfully");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
    public Map<String, Integer> countQuestionsByType() throws SQLException {
        Map<String, Integer> counts = new HashMap<>();
        String sql = "SELECT type, COUNT(*) AS count FROM questionfiras GROUP BY type";
        try (PreparedStatement ps = cnx.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                counts.put(rs.getString("type"), rs.getInt("count"));
            }
        }
        return counts;
    }


    @Override
    public void modifierquestion(question e) throws SQLException {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temquestiones.
        String req = "UPDATE questionfiras SET name = ?,image=?,commentaire = ?,type = ? where id = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, e.getName());

        ps.setString(2, e.getImage());
        ps.setString(3, e.getCommentaire());
        ps.setString(4, e.getType());


        ps.setInt(5, e.getId());
        ps.executeUpdate();
    }

    @Override
    public void supprimerquestion(question e) throws SQLException {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temquestiones.
        String req = "delete from questionfiras where id = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, e.getId());
        ps.executeUpdate();
        System.out.println("ev with id= " + e.getId() + "  is deleted successfully");
    }


    public  void updateLikes(int questionId) {
        String req = "UPDATE questionfiras SET `like` = `like` + 1 WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, questionId);
            ps.executeUpdate();
            System.out.println("Likes updated successfully for question ID: " + questionId);
        } catch (SQLException ex) {
            Logger.getLogger(questionService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public  void updateDislikes(int questionId) {
        String req = "UPDATE questionfiras SET `dislike` = `dislike` + 1 WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, questionId);
            ps.executeUpdate();
            System.out.println("Dislikes updated successfully for question ID: " + questionId);
        } catch (SQLException ex) {
            Logger.getLogger(questionService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getLikes(int questionId) {
        String req = "SELECT `like` FROM questionfiras WHERE id = ?";
        int likes = 0;
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, questionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                likes = rs.getInt("like");
            }
        } catch (SQLException ex) {
            Logger.getLogger(questionService.class.getName()).log(Level.SEVERE, "Error fetching likes", ex);
        }
        return likes;
    }
    public int getDislikes(int questionId) {
        String req = "SELECT `dislike` FROM questionfiras WHERE id = ?";
        int dislikes = 0;
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, questionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                dislikes = rs.getInt("dislike");
            }
        } catch (SQLException ex) {
            Logger.getLogger(questionService.class.getName()).log(Level.SEVERE, "Error fetching dislikes", ex);
        }
        return dislikes;
    }


    @Override
    public List<question> recupererquestion() throws SQLException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temquestiones.

        List<question> question = new ArrayList<>();
        String s = "select * from questionfiras";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(s);
        while (rs.next()) {
            question e = new question();
            e.setName(rs.getString("name"));

            e.setImage(rs.getString("Image"));
            e.setCommentaire(rs.getString("commentaire"));
            e.setType(rs.getString("type"));
            e.setDate(rs.getDate("date"));
            e.setLike(rs.getInt("like"));
            e.setDislike(rs.getInt("dislike"));
            e.setId(rs.getInt("id"));

            question.add(e);

        }
        return question;
    }

    public question FetchOneev(int id) {
        question ev = new question();
        String requete = "SELECT * FROM `questionfiras` where id = " + id;

        try {
            ste = (Statement) cnx.createStatement();
            ResultSet rs = ste.executeQuery(requete);

            while (rs.next()) {

                ev = new question(rs.getInt("id"), rs.getString("name"), rs.getString("image"), rs.getString("commentaire"), rs.getString("type"), rs.getDate("date"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(questionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ev;
    }

    public ObservableList<question> Fetchevs() {
        ObservableList<question> evs = FXCollections.observableArrayList();
        String requete = "SELECT * FROM `questionfiras`";
        try {
            ste = (Statement) cnx.createStatement();
            ResultSet rs = ste.executeQuery(requete);

            while (rs.next()) {
                evs.add(new question(rs.getInt("id"), rs.getString("name"), rs.getString("image"), rs.getString("commentaire"), rs.getString("type"), rs.getDate("date")));
            }

        } catch (SQLException ex) {
            Logger.getLogger(questionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return evs;
    }


    public ObservableList<question> chercherev(String chaine) {
        String sql = "SELECT * FROM questionfiras WHERE (name LIKE ? or type_question LIKE ?  ) order by name ";
        //Connection cnx= Maconnexion.getInstance().getCnx();
        String ch = "%" + chaine + "%";
        ObservableList<question> myList = FXCollections.observableArrayList();
        try {

            Statement ste = cnx.createStatement();
            // PreparedStatement pst = myCNX.getCnx().prepareStatement(requete6);
            PreparedStatement stee = cnx.prepareStatement(sql);
            stee.setString(1, ch);
            stee.setString(2, ch);

            ResultSet rs = stee.executeQuery();
            while (rs.next()) {
                question e = new question();

                e.setName(rs.getString("name"));

                e.setImage(rs.getString("Image"));
                e.setCommentaire(rs.getString("commentaire"));
                e.setType(rs.getString("type"));
                e.setDate(rs.getDate("date"));


                e.setId(rs.getInt("id"));

                myList.add(e);
                System.out.println("ev trouvé! ");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return myList;
    }

    public List<question> trierev() throws SQLException {
        List<question> question = new ArrayList<>();
        String s = "select * from questionfiras order by name ";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(s);
        while (rs.next()) {
            question e = new question();
            e.setName(rs.getString("name"));

            e.setImage(rs.getString("Image"));
            e.setCommentaire(rs.getString("commentaire"));
            e.setType(rs.getString("type"));
            e.setDate(rs.getDate("date"));


            e.setId(rs.getInt("id"));
            question.add(e);
        }
        return question;
    }


}
