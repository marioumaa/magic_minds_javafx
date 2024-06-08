package Entity;
import java.time.LocalDate;

public class Evaluation {
    private int id_quiz_id;
    private int resultat;
    private int id_user_id;

private LocalDate date;
public Evaluation(){};
    public Evaluation(int id_quiz_id, int resultat, int id_user_id, LocalDate date) {
        this.id_quiz_id = id_quiz_id;
        this.resultat = resultat;
        this.id_user_id = id_user_id;
        this.date = date;
    }

    public int getId_quiz_id() {
        return id_quiz_id;
    }

    public void setId_quiz_id(int id_quiz_id) {
        this.id_quiz_id = id_quiz_id;
    }

    public int getResultat() {
        return resultat;
    }

    public void setResultat(int resultat) {
        this.resultat = resultat;
    }

    public int getId_user_id() {
        return id_user_id;
    }

    public void setId_user_id(int id_user_id) {
        this.id_user_id = id_user_id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "evaluation{" +
                "id_quiz_id=" + id_quiz_id +
                ", resultat=" + resultat +
                ", id_user_id=" + id_user_id +
                ", date=" + date +
                '}';
    }

}
