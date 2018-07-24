package gembalabaik2018.application.com.newsgembalabaik.ModelFirebase;

public class NotifFirebase {
    public String idNews;
    public String notifBody;
    public String bodyNews;
    public String urlImage;
    public String createdAt;
    public String createdBy;
    public Integer idx;

    public  NotifFirebase(){

    }

    public NotifFirebase(String idNews, String notifBody, String bodyNews, String urlImage, String createdAt, String createdBy, Integer idx) {
        this.idNews = idNews;
        this.notifBody = notifBody;
        this.bodyNews = bodyNews;
        this.urlImage = urlImage;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.idx = idx;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public String getBodyNews() {
        return bodyNews;
    }

    public void setBodyNews(String bodyNews) {
        this.bodyNews = bodyNews;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getIdNews() {
        return idNews;
    }

    public void setIdNews(String idNews) {
        this.idNews = idNews;
    }

    public String getNotifBody() {
        return notifBody;
    }

    public void setNotifBody(String notifBody) {
        this.notifBody = notifBody;
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
