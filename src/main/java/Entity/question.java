/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temquestione file, choose Tools | Temquestiones
 * and open the temquestione in the editor.
 */
package Entity;

import java.sql.Date;

/**
 * @author asus
 */
public class question {


    private int id;


    private String name, image, commentaire, type;
    private Date date;
    private int like, dislike;


    public question() {
    }

    public question(int id, String name, String image, String commentaire, String type, Date date) {
        this.id = id;


        this.name = name;

        this.image = image;
        this.commentaire = commentaire;
        this.type = type;
        this.date = date;

    }

    public question(String name, String image, String commentaire, String type, Date date) {


        this.name = name;

        this.image = image;
        this.commentaire = commentaire;
        this.type = type;
        this.date = date;

    }


    public question(int id, String name, String image, String commentaire, String type) {
        this.id = id;


        this.name = name;

        this.image = image;
        this.commentaire = commentaire;
        this.type = type;

    }


    //****************** getters ****************
    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getDislike() {
        return dislike;
    }

    public void setDislike(int dislike) {
        this.dislike = dislike;
    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public String getImage() {
        return image;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }


    //****************** setters ****************

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setImage(String image) {
        this.image = image;
    }


    public void setDate(Date date) {
        this.date = date;
    }


    @Override
    public String toString() {
        return "question{" + "id=" + id + ", name=" + name + ", image=" + image + ", commentaire=" + commentaire + ", type=" + type + ", date=" + date + '}';
    }


}
