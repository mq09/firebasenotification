package gembalabaik2018.application.com.newsgembalabaik.ModelFirebase;

public class UserFirebase {
    public String phoneNumber;
    public String userName;
    public String childName;
    public String classId;
    public String createdAt;
    public String createdBy;
    public String typeUser;

    public UserFirebase(){

    }

    public UserFirebase(String classId, String phoneNumber, String userName, String childName, String typeUser, String createdAt, String createdBy) {
        this.classId = classId;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.childName = childName;
        this.typeUser = typeUser;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getTypeUser() {
        return typeUser;
    }

    public void setTypeUser(String typeUser) {
        this.typeUser = typeUser;
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

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
