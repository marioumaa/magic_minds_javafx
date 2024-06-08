/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temquestione file, choose Tools | Temquestiones
 * and open the temquestione in the editor.
 */
package Service;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author asus
 */
public interface IquestionService<T> {
    
       public void ajouterquestion(T t) throws SQLException;
    public void modifierquestion(T t) throws SQLException;
    public void supprimerquestion(T t) throws SQLException;
    public List<T> recupererquestion() throws SQLException;
    
}
