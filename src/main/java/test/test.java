package test;

import Entity.User;
import Service.MailjetService;
import Service.UserService;
import Utili.MyDB;
import com.mailjet.client.errors.MailjetException;


import java.sql.SQLException;
import java.util.List;

public class test {
    public static void main(String[] args) throws SQLException, MailjetException {
//        UserService service = new UserService();
//        String role = "[\"ROLE_ADMIN\"]";;
//      //  String role1 = role.replaceAll("[\"","").replaceAll("]","");
//        String newRole = role.replaceAll("\\[\"ROLE_.*?\"\\]", "");
//        String roleName = role.replaceAll("\\[\"ROLE_(.*?)\"\\]", "$1").toLowerCase();
//        System.out.println(newRole);
//        System.out.println(roleName);
//        User user = new User(22,"louay","benslimen","email","88999","male","lijlnjn","jjkhjjhj",role);
//        service.insert(user);

        //System.out.println(service.getAll());
//        MailjetService envoie = new MailjetService();
//        envoie.sendMailjet("benslimenlouay29@gmail.com","verifier "," <h1> Bonjour louay voici votre code </h1>   <hr>  52114 ");
        UserService service = new UserService();

        try {
         Boolean user =   service.authenticateUser("benslimenlouay29@gmail.com", "123Loua");
            System.out.println(user);
        }catch (SQLException e){
            e.printStackTrace();
        }


    }
}
