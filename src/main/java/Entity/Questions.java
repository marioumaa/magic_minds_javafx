package Entity;

import java.util.Objects;

public class Questions {
    private String question;
    private String choix1;
    private String choix2;
    private String choix3;
    private String reponse;
    private int id;
    private int idQuiz;
    private String nomQuiz; // Nouvelle propriété pour stocker le nom du quiz

    // Getters et setters pour la nouvelle propriété

    public Questions(){};

    public Questions(String question, String choix1, String choix2, String choix3, String reponse, int id, int idQuiz) {
        this.question = question;
        this.choix1 = choix1;
        this.choix2 = choix2;
        this.choix3 = choix3;
        this.reponse = reponse;
        this.id = id;
        this.idQuiz = idQuiz;
    }
    public Questions(String question, String choix1, String choix2, String choix3, String reponse, int idQuiz) {
        this.question = question;
        this.choix1 = choix1;
        this.choix2 = choix2;
        this.choix3 = choix3;
        this.reponse = reponse;
        this.idQuiz = idQuiz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Questions questions)) return false;
        return id == questions.id && idQuiz == questions.idQuiz && Objects.equals(question, questions.question) && Objects.equals(choix1, questions.choix1) && Objects.equals(choix2, questions.choix2) && Objects.equals(choix3, questions.choix3) && Objects.equals(reponse, questions.reponse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question, choix1, choix2, choix3, reponse, id, idQuiz);
    }
    public String getNomQuiz() {
        return nomQuiz;
    }

    public void setNomQuiz(String nomQuiz) {
        this.nomQuiz = nomQuiz;
    }
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getChoix1() {
        return choix1;
    }

    public void setChoix1(String choix1) {
        this.choix1 = choix1;
    }

    public String getChoix2() {
        return choix2;
    }

    public void setChoix2(String choix2) {
        this.choix2 = choix2;
    }

    public String getChoix3() {
        return choix3;
    }

    public void setChoix3(String choix3) {
        this.choix3 = choix3;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdQuiz() {
        return idQuiz;
    }

    public void setIdQuiz(int idQuiz) {
        this.idQuiz = idQuiz;
    }

    @Override
    public String toString() {
        return "Questions{" +
                "question='" + question + '\'' +
                ", choix1='" + choix1 + '\'' +
                ", choix2='" + choix2 + '\'' +
                ", choix3='" + choix3 + '\'' +
                ", reponse='" + reponse + '\'' +
                ", id=" + id +
                ", idQuiz=" + idQuiz +
                '}';
    }
}
