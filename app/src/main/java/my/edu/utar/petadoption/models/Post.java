package my.edu.utar.petadoption.models;

import android.net.Uri;

import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Post implements Serializable {

    private String postKey;
    private String title;
    private String content;
    private Uri imageUri;
    private String birth;
    private String gender;
    private String contact;
    private transient Object timeStamp;

    public Post() {
    }
    public Post(String title, String content, Uri imageUri, String birth, String gender, String contact) {
        this.title = title;
        this.content = content;
        this.imageUri = imageUri;
        this.birth = birth;
        this.gender = gender;
        this.contact = contact;
        this.timeStamp = ServerValue.TIMESTAMP;
    }

    // logcat verification purpose
    public String toString() {
        return "Post{" +
                "postKey='" + postKey + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", imageUri=" + imageUri +
                ", birth='" + birth + '\'' +
                ", gender='" + gender + '\'' +
                ", contact='" + contact + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
    public String getPostKey() {
        return postKey;
    }
    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }
    public String getTitle() {

        return title;
    }
    public String getContent() {

        return content;
    }

    @Exclude
    public Uri getImageUri() {

        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
    public String getBirth() {

        return birth;
    }
    public String getGender() {

        return gender;
    }
    public String getContact() {

        return contact;
    }
    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent (String content) {
        this.content = content;
    }
}