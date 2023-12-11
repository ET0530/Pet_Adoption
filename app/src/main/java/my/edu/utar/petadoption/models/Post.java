package my.edu.utar.petadoption.models;

import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Post implements Serializable {

    private String postKey;
    private String userID;
    private String postTitle;
    private String descriptions;
    private String postImage;
    private String posterEmail;
    private String postGender;
    private String posterContact;
    private Object timeStamp;
    private String userId;
    public Post(String postTitle, String descriptions, String postImage, String posterEmail, String postGender, String posterContact,String userID) {

        this.postTitle = postTitle;
        this.descriptions = descriptions;
        this.postImage = postImage;
        this.posterEmail = posterEmail;
        this.postGender = postGender;
        this.posterContact = posterContact;
        this.timeStamp = ServerValue.TIMESTAMP;
        this.userID=userID;
    }

    public Post(){

    };

    // logcat verification purpose
    public String toString() {
        return "Post{" +
                "postKey='" + postKey + '\'' +
                ", postTitle='" + postTitle + '\'' +
                ", descriptions='" + descriptions + '\'' +
                ", postImage=" + postImage +
                ", posterEmail='" + posterEmail + '\'' +
                ", postGender='" + postGender + '\'' +
                ", posterContact='" + posterContact + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
    public String getUserId() {
        return userID;
    }
    public void setUserId(String userID) {
        this.userID = userID;
    }
    public String getPostKey() {
        return postKey;
    }
    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }
    public String getTitle() {

        return postTitle;
    }
    public String getContent() {

        return descriptions;
    }

    @Exclude
    public String getImageUri() {

        return postImage;
    }

    public void setImageUri(String postImage) {
        this.postImage = postImage;
    }
    public String getPosterEmail() {

        return posterEmail;
    }
    public String getGender() {

        return postGender;
    }
    public String getContact() {

        return posterContact;
    }
    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public void setContent (String descriptions) {
        this.descriptions = descriptions;
    }


}