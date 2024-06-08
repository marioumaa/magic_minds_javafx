package Service;

import Entity.User;
import Utili.MyDB;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService implements ServiceUserInterface<User> {
    Connection connection;

    public UserService() {
        connection = MyDB.getInstance().getConnection();
    }


    @Override
    public void insert(User user) throws SQLException {
        PreparedStatement ste = null;
        String sql = "INSERT INTO user "
                + "( `first_name`, `last_name`, `age`, `gender`, `password`, `tel`, `email`, `picture`,`roles`, `is_verified`, `active`) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        ste = connection.prepareStatement(sql);
        ste.setString(1,user.getFirstName());
        ste.setString(2,user.getLastName());
        ste.setInt(3,user.getAge());
        ste.setString(4,user.getGender());
        ste.setString(5,user.getPassword());
        ste.setString(6,user.getTel());
        ste.setString(7,user.getEmail());
        ste.setString(8,user.getPicture());

        ste.setString(9,user.getRoles());
        ste.setBoolean(10,user.isVerified());
        ste.setBoolean(11,user.isActive());

        ste.executeUpdate();
    }

    @Override
    public void update(User user,int id) throws SQLException {
        PreparedStatement ste = null;

        String sql = "UPDATE `user` SET first_name = ?, last_name = ?,age = ?,gender = ?,password= ?,tel= ?,email= ?,picture= ?,roles= ?,is_verified= ?,active= ? WHERE id = ?";
        ste = connection.prepareStatement(sql);
        ste.setString(1,user.getFirstName());
        ste.setString(2,user.getLastName());
        ste.setInt(3,user.getAge());
        ste.setString(4,user.getGender());
        ste.setString(5,user.getPassword());
        ste.setString(6,user.getTel());
        ste.setString(7,user.getEmail());
        ste.setString(8,user.getPicture());

        ste.setString(9,user.getRoles());
        ste.setBoolean(10,user.isVerified());
        ste.setBoolean(11,user.isActive());
        ste.setInt(12,id);
        ste.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        PreparedStatement ste = null ;
        String sql ="DELETE FROM `user` WHERE id = ?";
        ste= connection.prepareStatement(sql);
        ste.setInt(1,id);
        ste.executeUpdate();
    }

    @Override
    public List<User> getAll() throws SQLException {
        Statement ste = null;
        ResultSet resultSet = null;
        List<User>users = new ArrayList<>();
        String sql = "SELECT * FROM `user`";
        ste = connection.createStatement();
        resultSet = ste.executeQuery(sql);
        while(resultSet.next()){
            User p = new User();
            p.setId(resultSet.getInt("id"));
            p.setFirstName(resultSet.getString("first_name"));
            p.setLastName(resultSet.getString("last_name"));
            p.setAge(resultSet.getInt("age"));
            p.setGender(resultSet.getString("gender"));
            p.setPassword(resultSet.getString("password"));
            p.setTel(resultSet.getString("tel"));
            p.setEmail(resultSet.getString("email"));
            p.setPicture(resultSet.getString("picture"));
            p.setRoles(resultSet.getString("roles"));
            p.setVerified(resultSet.getBoolean("is_verified"));
            p.setActive(resultSet.getBoolean("active"));

            users.add(p);
        }
        return users;
    }
    public boolean checkUserUnique(String email) throws SQLException{
        PreparedStatement stmt = null;
        ResultSet resultSet = null;


            String sql = "SELECT COUNT(*) AS count FROM `user` WHERE `email` = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, email);
            resultSet = stmt.executeQuery();


            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count == 0;
            }



        return false;
    }
    public void registerUser(User user) throws SQLException {
        PreparedStatement ste = null;
        String sql = "INSERT INTO user "
                + "( `first_name`, `last_name`, `age`, `gender`, `password`, `tel`, `email`, `picture`,`roles`, `is_verified`, `active`) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        ste = connection.prepareStatement(sql);
        ste.setString(1,user.getFirstName());
        ste.setString(2,user.getLastName());
        ste.setInt(3,user.getAge());
        ste.setString(4,user.getGender());
        ste.setString(5,user.getPassword());
        ste.setString(6,"");
        ste.setString(7,user.getEmail());
        ste.setString(8,"");

        ste.setString(9,user.getRoles());
        ste.setBoolean(10,false);
        ste.setBoolean(11,true);

        ste.executeUpdate();
    }
    public  User getUserById(int id) throws SQLException {
        Statement ste = null;
        ResultSet resultSet = null;
        User p = new User();

        String sql = "SELECT * FROM `user` WHERE `id` = "+id;
        ste = connection.createStatement();
        resultSet = ste.executeQuery(sql);
        while(resultSet.next()){

            p.setId(resultSet.getInt("id"));
            p.setFirstName(resultSet.getString("first_name"));
            p.setLastName(resultSet.getString("last_name"));
            p.setAge(resultSet.getInt("age"));
            p.setGender(resultSet.getString("gender"));
            p.setPassword(resultSet.getString("password"));
            p.setTel(resultSet.getString("tel"));
            p.setEmail(resultSet.getString("email"));
            p.setPicture(resultSet.getString("picture"));
            p.setRoles(resultSet.getString("roles"));
            p.setVerified(resultSet.getBoolean("is_verified"));
            p.setActive(resultSet.getBoolean("active"));


        }
        return p;
    }
    public void setVerification(int id, boolean verif) throws SQLException {

        PreparedStatement stmt = null;

            String sql = "UPDATE `user` SET is_verified = ? WHERE id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setBoolean(1, verif);
            stmt.setInt(2, id);
            stmt.executeUpdate();

        }
    public  User getUserByEmail(String email) throws SQLException {
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        User p = new User();

        String sql = "SELECT * FROM `user` WHERE `email` = ?";
        stmt = connection.prepareStatement(sql);
        stmt.setString(1, email);

        resultSet = stmt.executeQuery();
        while(resultSet.next()){

            p.setId(resultSet.getInt("id"));
            p.setFirstName(resultSet.getString("first_name"));
            p.setLastName(resultSet.getString("last_name"));
            p.setAge(resultSet.getInt("age"));
            p.setGender(resultSet.getString("gender"));
            p.setPassword(resultSet.getString("password"));
            p.setTel(resultSet.getString("tel"));
            p.setEmail(resultSet.getString("email"));
            p.setPicture(resultSet.getString("picture"));
            p.setRoles(resultSet.getString("roles"));
            p.setVerified(resultSet.getBoolean("is_verified"));
            p.setActive(resultSet.getBoolean("active"));


        }
        return p;
    }
    public void setMDP(int id, String mdp) throws SQLException {

        PreparedStatement stmt = null;

        String sql = "UPDATE `user` SET password = ? WHERE id = ?";
        stmt = connection.prepareStatement(sql);
        stmt.setString(1, mdp);
        stmt.setInt(2, id);
        stmt.executeUpdate();

    }
    public void setActive(int id, boolean active) throws SQLException {

        PreparedStatement stmt = null;

        String sql = "UPDATE `user` SET active = ? WHERE id = ?";
        stmt = connection.prepareStatement(sql);
        stmt.setBoolean(1, active);
        stmt.setInt(2, id);
        stmt.executeUpdate();

    }

    public List<User> search(String searchKey) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        List<User> users = new ArrayList<>();



            String sql = "SELECT * FROM `user` WHERE first_name LIKE ? OR last_name LIKE ? OR email LIKE ?";
            pstmt = connection.prepareStatement(sql);


            pstmt.setString(1, "%" + searchKey + "%");
            pstmt.setString(2, "%" + searchKey + "%");
            pstmt.setString(3, "%" + searchKey + "%");

            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                User p = new User();
                p.setId(resultSet.getInt("id"));
                p.setFirstName(resultSet.getString("first_name"));
                p.setLastName(resultSet.getString("last_name"));
                p.setAge(resultSet.getInt("age"));
                p.setGender(resultSet.getString("gender"));
                p.setPassword(resultSet.getString("password"));
                p.setTel(resultSet.getString("tel"));
                p.setEmail(resultSet.getString("email"));
                p.setPicture(resultSet.getString("picture"));
                p.setRoles(resultSet.getString("roles"));
                p.setVerified(resultSet.getBoolean("is_verified"));
                p.setActive(resultSet.getBoolean("active"));

                users.add(p);
            }
        return users;
    }
    public List<User> getUsersInRange(int startIndex, int itemsPerPage) throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user LIMIT ?, ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, startIndex);
            stmt.setInt(2, itemsPerPage);
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setAge(resultSet.getInt("age"));
                user.setGender(resultSet.getString("gender"));
                user.setPassword(resultSet.getString("password"));
                user.setTel(resultSet.getString("tel"));
                user.setEmail(resultSet.getString("email"));
                user.setPicture(resultSet.getString("picture"));
                user.setRoles(resultSet.getString("roles"));
                user.setVerified(resultSet.getBoolean("is_verified"));
                user.setActive(resultSet.getBoolean("active"));
                users.add(user);
            }
        }
        return users;
    }
    public Map<String, Integer> getGenderStats() throws SQLException {
        Map<String, Integer> genderStats = new HashMap<>();
        String sql = "SELECT gender, COUNT(*) AS count FROM user GROUP BY gender";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                int count = resultSet.getInt("count");
                genderStats.put(gender, count);
            }
        }
        return genderStats;
    }


public boolean authenticateUser(String email, String password) throws SQLException {
    PreparedStatement stmt = null;
    ResultSet resultSet = null;



        String sql = "SELECT `password` FROM `user` WHERE `email` = ?";
        stmt = connection.prepareStatement(sql);
        stmt.setString(1, email);
        resultSet = stmt.executeQuery();


        if (resultSet.next()) {

            String storedPassword = resultSet.getString("password");


            return BCrypt.checkpw(password, storedPassword);
        } else {

            return false;
        }

}
}