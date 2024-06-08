package Entity;
import java.time.LocalDate;

public class Comment {
    private int id,id_prod,id_user;
    private String description;
    private LocalDate date;

    public Comment(int id, int id_prod, int id_user, String description, LocalDate date) {
        this.id = id;
        this.id_prod = id_prod;
        this.id_user = id_user;
        this.description = description;
        this.date = date;
    }
    public Comment(int id){
        this.id=id;
    }
    public Comment(int id,int id_prod,int id_user,String description){
        this.id=id;
        this.id_prod=id_prod;
        this.id_user=id_user;
        this.description=description;
        this.date=LocalDate.now();
    }
   public Comment(){}
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_prod() {
        return id_prod;
    }

    public void setId_prod(int id_prod) {
        this.id_prod = id_prod;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", id_prod=" + id_prod +
                ", id_user=" + id_user +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }
}
