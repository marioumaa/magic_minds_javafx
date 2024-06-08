package Service;

import Entity.Comment;
import Utili.MyDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
public class CommentCRUD implements CService<Comment> {
    private Connection connection;
    public CommentCRUD(){
        connection=MyDB.getInstance().getConnection();
    }
    @Override
    public void ajouter(Comment comment) throws SQLDataException {
        String req = "INSERT INTO commentaire(id,idproduit_id,iduser_id,descrip,date) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps=connection.prepareStatement(req);
            ps.setInt(1,comment.getId());
            ps.setInt(2,comment.getId_prod());
            ps.setInt(3,comment.getId_user());
            ps.setString(4,comment.getDescription());
            ps.setDate(5, Date.valueOf(comment.getDate()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modifier(Comment comment) throws SQLDataException {
        String req="UPDATE commentaire SET idproduit_id=?,iduser_id=?,descrip=?,date=? WHERE id=?";
        try {
            PreparedStatement ps=connection.prepareStatement(req);
            ps.setInt(1,comment.getId_prod());
            ps.setInt(2,comment.getId_user());
            ps.setString(3,comment.getDescription());
            ps.setDate(4,Date.valueOf(comment.getDate()));
            ps.setInt(5,comment.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void supprimer(Comment comment) throws SQLDataException {
        String req="DELETE FROM commentaire WHERE id=?";
        try {
            PreparedStatement ps=connection.prepareStatement(req);
            ps.setInt(1,comment.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void supprimerwithproduit(int idpr) throws SQLDataException {
        String req="DELETE FROM commentaire WHERE idproduit_id=?";
        try {
            PreparedStatement ps=connection.prepareStatement(req);
            ps.setInt(1,idpr);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afficher(Comment comment) throws SQLDataException {

    }

    @Override
    public ArrayList<Comment> afficherAll() throws SQLException {
        ArrayList<Comment> comments = new ArrayList<>();
        String req = "SELECT * FROM commentaire";
        PreparedStatement ps = connection.prepareStatement(req);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            // Convert java.sql.Date to java.time.LocalDate
            Date date = rs.getDate("date");
            LocalDate localDate = date.toLocalDate();

            Comment com = new Comment(
                    rs.getInt("id"),
                    rs.getInt("idproduit_id"),
                    rs.getInt("iduser_id"),
                    rs.getString("descrip"),
                    localDate
            );

            comments.add(com);
        }

        return comments;
    }
    public ArrayList<Comment> afficherAllByProductId(int productId) throws SQLException {
        ArrayList<Comment> comments = new ArrayList<>();
        String req = "SELECT * FROM commentaire WHERE idproduit_id = ?";
        PreparedStatement ps = connection.prepareStatement(req);
        ps.setInt(1, productId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            LocalDate localDate = rs.getDate("date").toLocalDate();
            Comment com = new Comment(
                    rs.getInt("id"),
                    rs.getInt("idproduit_id"),
                    rs.getInt("iduser_id"),
                    rs.getString("descrip"),
                    localDate
            );
            comments.add(com);
        }
        return comments;
    }
}
