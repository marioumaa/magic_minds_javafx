package Service;

import Entity.Evaluation;
import Utili.MyDB;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EvaluationCrud{

    static Connection connection;

    public EvaluationCrud() {
        connection = MyDB.getInstance().getConnection();
    }


    public void ajouter(Evaluation evaluation) throws SQLException {
        try {
            String req = "INSERT INTO `evaluation` (`id_user_id`,`id_quiz_id`,`resultat`,`date`) VALUES (?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, evaluation.getId_user_id());
            ps.setInt(2, evaluation.getId_quiz_id());
            ps.setInt(3, evaluation.getResultat());
            ps.setString(4, evaluation.getDate().toString());

            ps.executeUpdate();
            System.out.println("Evaluation added successfully");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }



    public void modifier(Evaluation evaluation) throws SQLException {

    }


    public void supprimer(int id) throws SQLException {

    }


    public List<Evaluation> recuperer() throws SQLException {
        List<Evaluation> q = new ArrayList<>();
        try {
            String req = "SELECT * FROM `evaluation`";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(req);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int idUser = resultSet.getInt("id_user_id");
                int idQuiz = resultSet.getInt("id_quiz_id");
                int resultat = resultSet.getInt("resultat");
               LocalDate date= (resultSet.getDate("date").toLocalDate());
                Evaluation evaluation=new Evaluation(idQuiz,resultat,idUser,date);

                q.add(evaluation);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return q;
    }
}
