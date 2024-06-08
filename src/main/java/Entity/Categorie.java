package Entity;



import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Categorie {
    private int id , nbr_chapitre ,nbr_cours;
    private String titre ,description,image;
    private List<Cours> coursList = new ArrayList<>();
    public Categorie(){

    }
    public Categorie(int id, String titre, String description, String image) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.image = image;
    }

    public Categorie(String titre, String description, String image) {
        this.titre = titre;
        this.description = description;
        this.image = image;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getNbr_chapitre() {
        return nbr_chapitre;
    }

    public void setNbr_chapitre(int nbr_chapitre) {
        this.nbr_chapitre = nbr_chapitre;
    }

    public int getNbr_cours() {
        return nbr_cours;
    }

    public void setNbr_cours(int nbr_cours) {
        this.nbr_cours = nbr_cours;
    }

    public List<Cours> getCoursList() {
        return coursList;
    }

    public void setCoursList(List<Cours> coursList) {
        this.coursList = coursList;
    }

    @Override
    public String toString() {
        return "Categorie{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categorie categorie = (Categorie) o;
        return id == categorie.id && Objects.equals(titre, categorie.titre);
    }

}

