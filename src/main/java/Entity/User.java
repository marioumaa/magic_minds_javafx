package Entity;

import java.util.Date;

public class User {
    private int id ,age;
    private String firstName,lastName,email,tel,gender,password,picture,roles;
    private Date createdAt,updateAt ;
    private boolean isVerified,active;

    public User(int age, String firstName, String lastName, String email, String tel, String gender, String password, String picture,String roles) {
        this.age = age;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.tel = tel;
        this.gender = gender;
        this.password = password;
        this.picture = picture;
        this.active= true;
        this.isVerified = false;
        this.createdAt = new Date();

        this.roles = "[\"ROLE_"+roles.toUpperCase()+"\"]";
    }



    public User(int id, int age, String firstName, String lastName, String email, String tel, String gender, String password, String picture,String roles) {
        this.id = id;
        this.age = age;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.tel = tel;
        this.gender = gender;
        this.password = password;
        this.picture = picture;
        this.active= true;
        this.isVerified = false;
        this.roles = "[\"ROLE_"+roles.toUpperCase()+"\"]";
        this.createdAt = new Date();
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", age=" + age +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", tel='" + tel + '\'' +
                ", gender='" + gender + '\'' +
                ", password='" + password + '\'' +
                ", picture='" + picture + '\'' +
                ", createdAt=" + createdAt +
                ", updateAt=" + updateAt +
                ", isVerified=" + isVerified +
                ", active=" + active +
                '}';
    }
}
