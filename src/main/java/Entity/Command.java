package Entity;

import java.util.ArrayList;

public class Command {
    private int id;
    private int id_user;
    private ArrayList<Integer> id_produit;
    private double total;

    public Command(int id, int id_user, ArrayList<Integer> id_produit, double total) {
        this.id = id;
        this.id_user = id_user;
        this.id_produit = id_produit;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public ArrayList<Integer> getId_produit() {
        return id_produit;
    }

    public void setId_produit(ArrayList<Integer> id_produit) {
        this.id_produit = id_produit;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "Command{" +
                "id=" + id +
                ", id_user=" + id_user +
                ", id_produit=" + id_produit +
                ", total=" + total +
                '}';
    }
}
