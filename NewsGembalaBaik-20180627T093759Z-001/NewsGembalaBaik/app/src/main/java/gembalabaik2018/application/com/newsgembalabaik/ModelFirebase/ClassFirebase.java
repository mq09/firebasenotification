package gembalabaik2018.application.com.newsgembalabaik.ModelFirebase;

public class ClassFirebase {
    public String classId;
    public String className;
    public String totalChild;
    public String teacher;
    public String mentor1;
    public String mentor2;
    public String createdAt;
    public String createdBy;

    public ClassFirebase(){

    }

    public ClassFirebase( String classId, String className, String totalChild, String teacher, String mentor1, String mentor2, String createdAt, String createdBy) {

        this.classId = classId;
        this.className = className;
        this.totalChild = totalChild;
        this.teacher = teacher;
        this.mentor1 = mentor1;
        this.mentor2 = mentor2;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTotalChild() {
        return totalChild;
    }

    public void setTotalChild(String totalChild) {
        this.totalChild = totalChild;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getMentor1() {
        return mentor1;
    }

    public void setMentor1(String mentor1) {
        this.mentor1 = mentor1;
    }

    public String getMentor2() {
        return mentor2;
    }

    public void setMentor2(String mentor2) {
        this.mentor2 = mentor2;
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
