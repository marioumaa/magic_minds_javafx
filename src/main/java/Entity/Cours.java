package Entity;

import java.util.ArrayList;
import java.util.List;

public class Cours {
    private int id , duree , nb_chapitre ;
    private String titre, description , status ;
    private int categorie_id ;
    private List<Ressource> chapterList = new ArrayList<>();

    public Cours(){

    }

    public Cours(int id, int duree, int nb_chapitre, String titre, String description, String status, int categorie_id) {
        this.id = id;
        this.duree = duree;
        this.nb_chapitre = nb_chapitre;
        this.titre = titre;
        this.description = description;
        this.status = status;
        this.categorie_id = categorie_id;
    }

    public Cours(int duree, int nb_chapitre, String titre, String description, String status, int categorie_id) {
        this.duree = duree;
        this.nb_chapitre = nb_chapitre;
        this.titre = titre;
        this.description = description;
        this.status = status;
        this.categorie_id = categorie_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public int getNb_chapitre() {
        return nb_chapitre;
    }

    public void setNb_chapitre(int nb_chapitre) {
        this.nb_chapitre = nb_chapitre;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCategorie_id() {
        return categorie_id;
    }

    public void setCategorie_id(int categorie_id) {
        this.categorie_id = categorie_id;
    }

    public List<Ressource> getChapterList() {
        return chapterList;
    }

    public void setChapterList(List<Ressource> chapterList) {
        this.chapterList = chapterList;
    }

    @Override
    public String toString() {
        return "Cours{" +
                "id=" + id +
                ", duree=" + duree +
                ", nb_chapitre=" + nb_chapitre +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", categorie='" + categorie_id + '\'' +
                '}';
    }
}
