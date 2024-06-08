package Controller;

import Entity.Cours;
import Entity.Evenement;
import Entity.Produit;
import Entity.User;
import Service.CoursService;
import Service.ProduitCRUD;
import Service.ServiceEvenement;
import Service.UserService;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;


public class DOutil {
    CoursService coursService=new CoursService();
    UserService userService= new UserService();
    ProduitCRUD produitService= new ProduitCRUD();
    ServiceEvenement eventService= new ServiceEvenement();

    public int totalCourses(){
        ObservableList<Cours> CL= coursService.getAll();
        int N= CL.size();
        return N ;
    }
    public int totalUsers()  {
        List<User> UL= null;
        try {
            UL = userService.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int N= UL.size();
        return N ;
    }
    public int totalProduct(){
        List<Produit>  PL= null;
        try {
            PL = produitService.afficherAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int N= PL.size();
        return N ;
    }
    public int totalEvent(){
        List<Evenement> EL= eventService.getAll();
        int N= EL.size();
        return N ;
    }


}

