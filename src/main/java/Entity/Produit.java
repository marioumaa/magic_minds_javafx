package Entity;

public class Produit {
private int id,quantity;
private double prix;
private String nom,description,img1,img2,img3,categorie;

    public Produit(int quantity, double prix, String nom, String description, String img1, String img2, String img3, String categrie) {
        this.quantity = quantity;
        this.prix = prix;
        this.nom = nom;
        this.description = description;
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
        this.categorie = categrie;
    }
    public Produit(int id,int quantity, double prix, String nom, String description, String img1, String img2, String img3, String categrie) {
        this.id=id;
        this.quantity = quantity;
        this.prix = prix;
        this.nom = nom;
        this.description = description;
        this.img1 = img1;
        this.img2 = img2;
        this.img3 = img3;
        this.categorie = categrie;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }



    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return "Produit{" +
                "quantity=" + quantity +
                ", prix=" + prix +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", img1='" + img1 + '\'' +
                ", img2='" + img2 + '\'' +
                ", img3='" + img3 + '\'' +
                ", categorie='" + categorie + '\'' +
                '}';
    }
}
