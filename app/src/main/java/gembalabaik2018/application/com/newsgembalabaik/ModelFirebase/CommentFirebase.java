package gembalabaik2018.application.com.newsgembalabaik.ModelFirebase;

public class CommentFirebase {
    public String idNews;
    public String bodyComment;
    public String createdAt;
    public String createdBy;
    public Integer idx;

    public CommentFirebase(){

    }

    public CommentFirebase(String idNews,String bodyComment, String createdAt, String createdBy, Integer idx) {
        this.idNews = idNews;
        this.bodyComment = bodyComment;
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

    public String getIdNews(){
        return idNews;
    }

    public void setIdNews(String idNews){
        this.idNews = idNews;
    }

    public String getBodyComment() {
        return bodyComment;
    }

    public void setBodyComment(String bodyComment) {
        this.bodyComment = bodyComment;
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
