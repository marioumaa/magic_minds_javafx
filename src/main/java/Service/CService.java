package Service;

import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface CService<T> {
    public void ajouter(T t)throws SQLDataException;
    public void modifier(T t)throws SQLDataException;
    public void supprimer(T t)throws SQLDataException;
    public void afficher(T t)throws SQLDataException;
    ArrayList<T> afficherAll() throws SQLException;
}
