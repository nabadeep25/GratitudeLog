package model;

import com.google.firebase.Timestamp;

public class Gratitudemodel {
    private String title;
    private String body;



    private  String imageUrl;
    private String userId;



    private String useName;
    private Timestamp createdAt;
    public Gratitudemodel() {
    }
    public Gratitudemodel(String title, String body, String imageUrl, String userId, String useName, Timestamp createdAt) {
        this.title = title;
        this.body = body;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.useName = useName;
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUseName() {
        return useName;
    }

    public void setUseName(String useName) {
        this.useName = useName;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
