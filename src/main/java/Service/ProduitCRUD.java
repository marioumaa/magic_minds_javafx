package Service;

import Entity.Produit;
import Utili.MyDB;

import java.sql.*;
import java.util.ArrayList;
public class ProduitCRUD implements PService<Produit> {
    private Connection connection;
    public ProduitCRUD(){
      connection=MyDB.getInstance().getConnection();
    }
    @Override
    public void ajouter(Produit produit) throws SQLDataException {
        String req = "INSERT INTO produit (nom, prix, description, img1, img2, img3, categorie, quantite) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, produit.getNom());
            ps.setDouble(2, produit.getPrix());
            ps.setString(3, produit.getDescription());
            ps.setString(4, produit.getImg1());
            ps.setString(5, produit.getImg2());
            ps.setString(6, produit.getImg3());
            ps.setString(7, produit.getCategorie());
            ps.setInt(8, produit.getQuantity());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modifier(Produit produit) throws SQLDataException {
     String req="UPDATE produit SET nom=?,prix=?,description=?,img1=?,img2=?,img3=?,categorie=?,quantite=? WHERE id=?";
        try {
            PreparedStatement ps=connection.prepareStatement(req);
            ps.setString(1, produit.getNom());
            ps.setDouble(2, produit.getPrix());
            ps.setString(3, produit.getDescription());
            ps.setString(4, produit.getImg1());
            ps.setString(5, produit.getImg2());
            ps.setString(6, produit.getImg3());
            ps.setString(7, produit.getCategorie());
            ps.setInt(8, produit.getQuantity());
            ps.setInt(9, produit.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void supprimer(Produit produit) throws SQLDataException {
    String req="DELETE FROM produit WHERE id=?";
        try {
            PreparedStatement ps=connection.prepareStatement(req);
            ps.setInt(1,produit.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afficher(Produit produit) throws SQLDataException {

    }

    @Override
    public ArrayList<Produit> afficherAll() throws SQLException {
        ArrayList<Produit>produits=new ArrayList<>();
        String req="SELECT * FROM produit";
        PreparedStatement ps=connection.prepareStatement(req);
        ResultSet rs=ps.executeQuery();
        while (rs.next()){
            Produit pr=new Produit(
                    rs.getInt("id"),
                    rs.getInt("quantite"),
                    rs.getDouble("prix"),
                    rs.getString("nom"),
                    rs.getString("description"),
                    rs.getString("img1"),
                    rs.getString("img2"),
                    rs.getString("img3"),
                    rs.getString("categorie")

            );
            produits.add(pr);
        }
        return produits;
    }
    public Produit getById(int id) throws SQLException {
        String query = "SELECT * FROM produit WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Produit(
                        resultSet.getInt("id"),
                        resultSet.getInt("quantite"),
                        resultSet.getDouble("prix"),
                        resultSet.getString("nom"),
                        resultSet.getString("description"),
                        resultSet.getString("img1"),
                        resultSet.getString("img2"),
                        resultSet.getString("img3"),
                        resultSet.getString("categorie")
                );
            }
        }
        return null; // Return null if product with given ID is not found
    }

}
