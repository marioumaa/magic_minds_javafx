package Service;

import Entity.Questions;
import Utili.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionsCrud {
    static Connection connection;

    public QuestionsCrud() {
        connection = MyDB.getInstance().getConnection();
    }


    public void ajouter(Questions questions) throws SQLException {
        try {
            String req = "INSERT INTO `question` (`quiz_id`,`question`,`choix1`,`choix2`,`choix3`,`reponse`) VALUES (?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, questions.getIdQuiz());
            ps.setString(2, questions.getQuestion());
            ps.setString(3, questions.getChoix1());
            ps.setString(4, questions.getChoix2());
            ps.setString(5, questions.getChoix3());
            ps.setString(6, questions.getReponse());
            ps.executeUpdate();
            System.out.println("Question added successfully");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    public void modifier(Questions questions) throws SQLException {
        try {
            String req = "UPDATE `question` SET `quiz_id`=?, `question`=?, `choix1`=?, `choix2`=?, `choix3`=?, `reponse`=? WHERE `id`=?";
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, questions.getIdQuiz());
            ps.setString(2, questions.getQuestion());
            ps.setString(3, questions.getChoix1());
            ps.setString(4, questions.getChoix2());
            ps.setString(5, questions.getChoix3());
            ps.setString(6, questions.getReponse());
            ps.setInt(7, questions.getId());

            ps.executeUpdate();
            System.out.println("Question updated successfully");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    public void supprimer(int id) throws SQLException {
        try {
            String req = "DELETE FROM `question` WHERE `id`=?";
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Question with id " + id + " deleted successfully");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }


    public List<Questions> recuperer() throws SQLException {
        List<Questions> q = new ArrayList<>();
        try {
            String req = "SELECT * FROM `question`";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(req);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int idQuiz = resultSet.getInt("quiz_id");
                String question = resultSet.getString("question");
                String choix1 = resultSet.getString("choix1");
                String choix2 = resultSet.getString("choix2");
                String choix3 = resultSet.getString("choix3");
                String reponse = resultSet.getString("reponse");
               Questions questions=new Questions(question,choix1,choix2,choix3,reponse,id,idQuiz);
                q.add(questions);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return q;
    }

    public List<Questions> recupererAvecNomQuiz() throws SQLException {
        List<Questions> questions = new ArrayList<>();
        try {
            String req = "SELECT q.*, quiz.titre AS nomQuiz " +
                    "FROM question q " +
                    "INNER JOIN quiz ON q.quiz_id = quiz.id";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(req);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int idQuiz = resultSet.getInt("quiz_id");
                String question = resultSet.getString("question");
                String choix1 = resultSet.getString("choix1");
                String choix2 = resultSet.getString("choix2");
                String choix3 = resultSet.getString("choix3");
                String reponse = resultSet.getString("reponse");
                String nomQuiz = resultSet.getString("nomQuiz");

                Questions questionObj = new Questions(question, choix1, choix2, choix3, reponse, id, idQuiz);
                questionObj.setNomQuiz(nomQuiz); // Setter pour le nom du quiz
                questions.add(questionObj);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return questions;
    }

    public List<Questions> recupererParQuizId(int idQuiz) throws SQLException {
        List<Questions> questions = new ArrayList<>();
        try {
            String req = "SELECT * FROM `question` WHERE `quiz_id`=?";
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, idQuiz);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String question = resultSet.getString("question");
                String choix1 = resultSet.getString("choix1");
                String choix2 = resultSet.getString("choix2");
                String choix3 = resultSet.getString("choix3");
                String reponse = resultSet.getString("reponse");

                Questions q = new Questions(question, choix1, choix2, choix3, reponse, id, idQuiz);
                questions.add(q);
            }
        } catch (SQLException ex) {
            throw new SQLException("Erreur lors de la récupération des questions: " + ex.getMessage());
        }
        return questions;
    }

    public boolean questionExisteDeja(String question) {
        try {
            String query = "SELECT COUNT(*) FROM question WHERE question = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, question);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // Si count est supérieur à 0, cela signifie que la question existe déjà
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
