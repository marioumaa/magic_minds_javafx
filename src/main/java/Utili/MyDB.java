package Utili;
import java.sql.*;
public class MyDB {
    Connection connection;
    String url = "jdbc:mysql://localhost:3306/magicminds1";
    String user ="root";
    String pass="";
    static MyDB Instance ;
    public MyDB(){
        try {
            connection = DriverManager.getConnection(url, user, pass);
            System.out.println("Connection");
        }catch (SQLException e){
            System.out.println("problem");
        }
    }

    public  static MyDB getInstance(){
        if(Instance == null){
            Instance = new MyDB();
        }
        return Instance;
    }
    public Connection getConnection(){
        return connection;
    }
}

