package Service;

import java.sql.SQLException;
import java.util.List;

public interface ServiceUserInterface<T>{
    public void insert(T t)throws SQLException;
    public void update(T t,int id)throws SQLException;
    public void delete(int id)throws SQLException;
    public List<T> getAll()throws SQLException;
}
