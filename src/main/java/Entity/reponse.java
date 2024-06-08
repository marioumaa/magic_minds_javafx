/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temquestione file, choose Tools | Temquestiones
 * and open the temquestione in the editor.
 */
package Entity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author asus
 */
public class reponse extends question {
    private int id_reponse;        

private int id_user;
private String description,fullname;
public int id; 
public question question;

    public reponse() {
    }

    public reponse(int id_reponse, int id_user, int id, String description, String fullname) {
        this.id_reponse = id_reponse;

        this.id_user = id_user;
        this.id = id;
        this.description = description;
        this.fullname = fullname;
    }
    public reponse(int id_user, int id) {

        this.id_user = id_user;
        this.id = id;


    }

    public reponse(int id_reponse, int id_user, int id, question question, String description, String fullname) {
        this.id_reponse = id_reponse;

        this.id_user = id_user;
        this.id = id;
        this.question = question;
        this.description = description;
        this.fullname = fullname;
    }
    public reponse( int id_user, int id, String description, String fullname) {


        this.id_user = id_user;
        this.id = id;

        this.description = description;
        this.fullname = fullname;
    }

    public reponse(ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            this.id_reponse = resultSet.getInt("id_reponse");
            this.id_user = resultSet.getInt("id_user");
            this.id = resultSet.getInt("id"); // Assuming 'id_question' is the correct column name
            this.description = resultSet.getString("description");
            this.fullname = resultSet.getString("fullname");
        }
    }

    public int getId_reponse() {
        return id_reponse;
    }



    public int getId_user() {
        return id_user;
    }

    public int getId() {
        return id;
    }

    public question getquestion() {
        return question;
    }

    public void setId_reponse(int id_reponse) {
        this.id_reponse = id_reponse;
    }



    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getFullname() {
        return fullname;
    }
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    public String getDescription() {
        return description;
    }
    public void setQuestion(question question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "reponse{" + "id_reponse=" + id_reponse +  ", id_user=" + id_user + ", id=" + id +  ", description=" + description +  ", fullname=" + fullname + '}';
    }
    
    
    




}
