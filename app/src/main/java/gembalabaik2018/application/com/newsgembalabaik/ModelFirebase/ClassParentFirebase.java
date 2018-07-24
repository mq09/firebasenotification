package gembalabaik2018.application.com.newsgembalabaik.ModelFirebase;

public class ClassParentFirebase {
    public String classId;
    public String createdAt;
    public String createdBy;
    public ClassParentFirebase(){

    }

    public ClassParentFirebase(String classId, String createdAt, String createdBy) {

        this.classId = classId;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
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
