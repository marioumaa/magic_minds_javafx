package Entity;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
     private List<Questions> questions = new ArrayList<>();
  private   String titre;
    private  int temp,id;
    private  int nb_question;
    public Quiz(){};

    public Quiz(String titre, int temp, int nb_question) {
        this.titre = titre;
        this.temp = temp;
        this.nb_question = nb_question;
    }
    public Quiz(int id,String titre, int temp, int nb_question) {
        this.id=id;
        this.titre = titre;
        this.temp = temp;
        this.nb_question = nb_question;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNb_question() {
        return nb_question;
    }

    public void setNb_question(int nb_question) {
        this.nb_question = nb_question;
    }

    public List<Questions> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Questions> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "titre='" + titre + '\'' +
                ", temp=" + temp +
                ", id=" + id +
                ", nb_question=" + nb_question +
                '}';
    }




}
