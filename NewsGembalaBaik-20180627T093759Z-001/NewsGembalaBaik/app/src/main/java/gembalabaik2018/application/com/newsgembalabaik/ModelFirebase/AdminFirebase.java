package gembalabaik2018.application.com.newsgembalabaik.ModelFirebase;

public class AdminFirebase {
    public String phoneNumber;
    public String userName;

    public AdminFirebase(){

    }

    public AdminFirebase(String phoneNumber, String userName) {
        this.phoneNumber = phoneNumber;
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String parentName) {
        this.userName = parentName;
    }

}
