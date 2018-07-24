package gembalabaik2018.application.com.newsgembalabaik.ModelFirebase;

public class NewsFirebase {
    public String bodyNews;
    public String urlImage;
    public String typeNews;
    public String createdAt;
    public String createdBy;
    public Integer idx;
    public String classId;

    public NewsFirebase() {

    }

    public NewsFirebase(String classId, String bodyNews, String typeNews, String urlImage, String createdAt, String createdBy, Integer idx) {
        this.urlImage = urlImage;
        this.bodyNews = bodyNews;
        this.typeNews = typeNews;
        this.classId = classId;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.idx = idx;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getBodyNews() {
        return bodyNews;
    }

    public void setBodyNews(String bodyNews) {
        this.bodyNews = bodyNews;
    }

    public String getTypeNews() {
        return typeNews;
    }

    public void setTypeNews(String typeNews) {
        this.typeNews = typeNews;
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
