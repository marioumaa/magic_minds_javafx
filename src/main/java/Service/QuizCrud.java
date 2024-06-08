package Service;

import Entity.Questions;
import Entity.Quiz;

import Utili.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class QuizCrud  {
    static Connection connection;

    public QuizCrud() {
        connection = MyDB.getInstance().getConnection();
    }
    public Quiz getByTitre(String email) {
        try {
            String query = "SELECT * FROM quiz WHERE titre like ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, "%"+email+"%");

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Quiz resultquiz = new Quiz();
                        resultquiz.setId(resultSet.getInt("id"));
                        resultquiz.setTitre(resultSet.getString("titre"));


                        resultquiz.setNb_question(resultSet.getInt("nbquestion"));
                        resultquiz.setTemp(resultSet.getInt("temp"));


                        // Ajoutez d'autres attributs si nécessaire
                        return resultquiz;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public Quiz getByQuizId(int quizId) {
        try {
            String query = "SELECT * FROM quiz WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, quizId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        Quiz resultquiz = new Quiz();
                        resultquiz.setId(resultSet.getInt("id"));
                        resultquiz.setTitre(resultSet.getString("titre"));
                        resultquiz.setNb_question(resultSet.getInt("nbquestion"));
                        resultquiz.setTemp(resultSet.getInt("temp"));

                        // Ajoutez d'autres attributs si nécessaire
                        return resultquiz;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public void ajouter(Quiz quiz) throws SQLException {
        try {
            String req = "INSERT INTO `quiz` (`titre`,`temp`,`nbquestion`) VALUES (?,?,?)";
            PreparedStatement ps = connection.prepareStatement(req);

            ps.setString(1, quiz.getTitre());

            ps.setInt(2, quiz.getTemp());
            ps.setInt(3, quiz.getNb_question());


            ps.executeUpdate();
            System.out.println("Quiz added successfully");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


    public void modifier(Quiz quiz) throws SQLException {
        try {
            String req = "UPDATE `quiz` SET `titre`=?, `temp`=?, `nbquestion`=? WHERE `id`=?";
            PreparedStatement ps = connection.prepareStatement(req);

            ps.setString(1, quiz.getTitre());

            ps.setInt(2, quiz.getTemp());
            ps.setInt(3, quiz.getNb_question());
            ps.setInt(4, quiz.getId());


            ps.executeUpdate();
            System.out.println("Quiz updated successfully");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }





   /* @Override
    public void supprimer(int id) throws SQLException {
        try {
            String req = "DELETE FROM `quiz` WHERE `id`=?";
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Quiz with id " + id + " deleted successfully");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }*/
   public void supprimer(int id) throws SQLException {
       QuestionsCrud qc=new QuestionsCrud();
       try {
           // Supprimer d'abord toutes les questions associées à ce quiz
           List<Questions> questions = qc.recupererParQuizId(id);
           for (Questions question : questions) {
               qc.supprimer(question.getId());
           }

           // Ensuite, supprimer le quiz lui-même
           String req = "DELETE FROM `quiz` WHERE `id`=?";
           PreparedStatement ps = connection.prepareStatement(req);
           ps.setInt(1, id);
           ps.executeUpdate();

           System.out.println("Quiz with id " + id + " deleted successfully");
       } catch (SQLException ex) {
           System.out.println(ex.getMessage());
       }
   }



    public List<Quiz> recuperer() throws SQLException {
        List<Quiz> q = new ArrayList<>();
        try {
            String req = "SELECT * FROM `quiz`";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(req);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String titre = resultSet.getString("titre");
                int temp = resultSet.getInt("temp");
                int nb_question = resultSet.getInt("nbquestion");



                Quiz quiz = new Quiz(id,titre,temp,nb_question);
                q.add(quiz);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return q;
    }


    public boolean titreExisteDeja(String titre) {
        try {
            String query = "SELECT COUNT(*) FROM quiz WHERE titre = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, titre);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // Si count est supérieur à 0, cela signifie que le titre existe déjà
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }




}
