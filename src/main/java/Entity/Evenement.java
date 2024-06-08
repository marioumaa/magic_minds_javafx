package Entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Evenement {

        private int id ,nb_participant ;
        private String nom ,description,localisation,categorie ;
       private Date date_debut, date_fin;


    public Evenement(int id, int nb_participant, String nom, String description, String localisation, String categorie, Date date_debut, Date date_fin) {
        this.id = id;
        this.nb_participant = nb_participant;
        this.nom = nom;
        this.description = description;
        this.localisation = localisation;
        this.categorie = categorie;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
    }

    public Evenement() {
    }

    public int getId() {
        return id;
    }

    public int getNb_participant() {
        return nb_participant;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public String getLocalisation() {
        return localisation;
    }

    public String getCategorie() {
        return categorie;
    }

    public Date getDate_debut() {
        return date_debut;
    }

    public Date getDate_fin() {
        return date_fin;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNb_participant(int nb_participant) {
        this.nb_participant = nb_participant;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public void setDate_debut(Date date_debut) {
        this.date_debut = date_debut;
    }

    public void setDate_fin(Date date_fin) {
        this.date_fin = date_fin;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "id=" + id +
                ", nb_participant=" + nb_participant +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", localisation='" + localisation + '\'' +
                ", categorie='" + categorie + '\'' +
                ", date_debut=" + date_debut +
                ", date_fin=" + date_fin +
                '}';
    }
    public static List<Evenement> rechercherParFiltrage(List<Evenement> evenements, String localisation, String categorie) {
        List<Evenement> resultats = new ArrayList<>();
        for (Evenement evenement : evenements) {
            if (evenement.getLocalisation().equalsIgnoreCase(localisation) && evenement.getCategorie().equalsIgnoreCase(categorie)) {
                resultats.add(evenement);
            }
        }
        return resultats;
    }
}
