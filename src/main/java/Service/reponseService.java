/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temquestione file, choose Tools | Temquestiones
 * and open the temquestione in the editor.
 */
package Service;

import Entity.User;
import Entity.question;
import Entity.reponse;
import Utili.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//**************//


/**
 * @author asus
 */
public class reponseService {

    Connection cnx;
    public Statement ste;
    public PreparedStatement pst;

    public reponseService() {

        cnx = MyDB.getInstance().getConnection();
    }

    public List<reponse> getReponsesByQuestionId(int questionId) throws SQLException {
        List<reponse> responses = new ArrayList<>();

        // Your SQL query
        String query = "SELECT * FROM reponsefiras WHERE id = ?";

        // Using try-with-resources to ensure that resources are closed
        try (PreparedStatement preparedStatement = cnx.prepareStatement(query)) {
            preparedStatement.setInt(1, questionId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Assuming you have a constructor in your 'reponse' class that
                    // takes the ResultSet and extracts the properties for the 'reponse' object
                    reponse res = new reponse(resultSet);
                    responses.add(res);
                }
            }
        }
        return responses;
    }

    public void ajouterreponse(reponse p) {
        User U = new User();
        questionService es = new questionService();
        String requete = "INSERT INTO `reponsefiras` (`id` ,`id_user`,`description`,`fullname`) VALUES(?,?,?,?) ;";

        try {
            question tempev = es.FetchOneev(p.getId());
            System.out.println("before" + tempev);

            es.modifierquestion(tempev);
            int new_id = tempev.getId();
            p.setQuestion(tempev);
            System.out.println("after" + tempev);

            pst = (PreparedStatement) cnx.prepareStatement(requete);

            pst.setInt(1, p.getId());
            pst.setInt(2, p.getId_user());
            pst.setString(3, p.getDescription());
            pst.setString(4, p.getFullname());


            pst.executeUpdate();


            System.out.println("reponse with id ev = " + p.getId() + " is added successfully");

        } catch (SQLException ex) {
            System.out.println("error in adding reponse");
            Logger.getLogger(reponseService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<reponse> recupererReponse() throws SQLException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temquestiones.
        reponse dernierDescription = null;

        List<reponse> particip = new ArrayList<>();
        String s = "SELECT * FROM reponsefiras WHERE id_reponse = (SELECT MAX(id_reponse) FROM reponse)";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(s);
        while (rs.next()) {
            reponse pa = new reponse();
            pa.setId_reponse(rs.getInt("id_reponse"));
            pa.setId_user(rs.getInt("id_user"));
            pa.setId(rs.getInt("id"));

            pa.setDescription(rs.getString("description"));
            pa.setFullname(rs.getString("fullname"));

            particip.add(pa);

        }
        return particip;
    }

    public List<reponse> recupererComment() throws SQLException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temquestiones.
        reponse dernierDescription = null;
        List<reponse> particip = new ArrayList<>();
        String s = "select * from reponsefiras";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(s);
        while (rs.next()) {
            reponse pa = new reponse();
            pa.setId_reponse(rs.getInt("id_reponse"));
            pa.setId_user(rs.getInt("id_user"));
            pa.setId(rs.getInt("id"));

            pa.setDescription(rs.getString("description"));
            pa.setFullname(rs.getString("fullname"));
            particip.add(pa);

        }
        return particip;
    }

    public void supprimerreponse(reponse p) throws SQLException {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temquestiones.
        String req = "delete from reponsefiras where id_reponse  = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, p.getId_reponse());
        ps.executeUpdate();
        System.out.println("reponse with id= " + p.getId_reponse() + "  is deleted successfully");
    }

    public reponse FetchOneRes(int id) throws SQLException {
        reponse r = new reponse();
        String requete = "SELECT * FROM `reponsefiras` where id_reponse=" + id;

        try {
            ste = (Statement) cnx.createStatement();
            ResultSet rs = ste.executeQuery(requete);

            while (rs.next()) {

                r = new reponse(rs.getInt("id_reponse"), rs.getInt("id_user"), rs.getInt("id"), rs.getString("description"), rs.getString("fullname"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(questionService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return r;
    }

    public void Deletereponse(reponse p) throws SQLException {
        questionService es = new questionService();
        reponseService rs = new reponseService();

        reponse r = rs.FetchOneRes(p.getId_reponse());

        String requete = "delete from reponsefiras where id_reponse=" + p.getId_reponse();
        try {
            question tempev = es.FetchOneev(r.getId());
            System.out.println("before" + tempev);

            es.modifierquestion(tempev);
            System.out.println("after" + tempev);
            pst = (PreparedStatement) cnx.prepareStatement(requete);
            //pst.setInt(1, id);

            pst.executeUpdate();
            System.out.println("reponse with id=" + p.getId_reponse() + " is deleted successfully");
        } catch (SQLException ex) {
            System.out.println("error in delete reponse " + ex.getMessage());
        }
    }

    public void modifierreponse(reponse p) throws SQLException {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temquestiones.
        try {
            String req = "UPDATE reponsefiras SET id_user = ?,id = ?,description = ?,fullname = ? where id_reponse = ?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, p.getId_user());
            ps.setInt(2, p.getId());
            ps.setString(3, p.getDescription());
            ps.setString(4, p.getFullname());
            ps.setInt(5, p.getId_reponse());

            ps.executeUpdate();
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
    }

    public void ajoutercomment(reponse p) throws SQLException {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Temquestiones.

        String req = "INSERT INTO `reponsefiras` (`id_user`,`id`,`description`,`fullname`) "
                + "VALUES (?,?,?,?,?);";
        try {
            pst = (PreparedStatement) cnx.prepareStatement(req);
            pst.setInt(1, p.getId_user());
            pst.setInt(2, p.getId());

            pst.setString(3, p.getDescription());
            pst.setString(4, p.getFullname());
            pst.setInt(5, p.getId_reponse());


            pst.executeUpdate();
            System.out.println("ev " + p.getFullname() + " added successfully");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
