package Entity;

import Service.CategorieService;

public class Ressource {
    private int id, id_cours_id ;
    private String titre , type , url ,courseName;
    CategorieService categorieService=new CategorieService();

    public Ressource(){

    }

    public Ressource(int id, int id_cours_id, String titre, String type, String url) {
        this.id = id;
        this.id_cours_id = id_cours_id;
        this.titre = titre;
        this.type = type;
        this.url = url;
    }

    public Ressource(int id_cours_id, String titre, String type, String url) {
        this.id_cours_id = id_cours_id;
        this.titre = titre;
        this.type = type;
        this.url = url;
    }

    public String getCourseName(Ressource r) {
        courseName=categorieService.getbyId(r.getId_cours_id()).getTitre();
        return courseName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId_cours_id() {
        return id_cours_id;
    }

    public void setId_cours_id(int id_cours_id) {
        this.id_cours_id = id_cours_id;
    }

    @Override
    public String toString() {
        return "Ressource{" +
                "id=" + id +
                ", id_cours_id=" + id_cours_id +
                ", titre='" + titre + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
