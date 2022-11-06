package PojoClass;

public class _01_GoRestUserCreatinguser {

    // used access modifier and encapsulation logics.

    /** we created this class to build a framework.
    1- we used private keyword because none should be able to see and,or replace the variables without permission.
    2- by using getter and setter methods we are able to print and initialize variable's value
     */

    private String id;
    private String name;
    private String email;
    private String gender;
    private String status;

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

}
