package Service;

import java.util.ArrayList;

public interface IService<T> {

        void add (T t );
        ArrayList<T> getAll();

        void update( int id,T t );
        boolean delete (T t);
//findby..

    //getby ...

}
