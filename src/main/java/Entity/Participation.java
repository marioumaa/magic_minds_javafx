package Entity;

import java.sql.Time;
import java.util.Date;
public class Participation {
    private Date date;
    private Time heure;
    private int id ;
    private int evenementId;
    private int id_user_id;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getHeure() {
        return heure;
    }

    public void setHeure(Time heure) {
        this.heure = heure;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEvenementId() {
        return evenementId;
    }
    public void setEvenementId(int evenementId) {
        this.evenementId = evenementId;
    }
    public int getId_user_id() {
        return id_user_id;
    }

    public void setId_user_id(int id_user_id) {
        this.id_user_id = id_user_id;
    }

    @Override
    public String toString() {
        return "Participation{" +
                "date=" + date +
                ", heure=" + heure +
                ", id=" + id +
                ", evenementId=" + evenementId +
                ", id_user_id=" + id_user_id +
                '}';
    }
}
