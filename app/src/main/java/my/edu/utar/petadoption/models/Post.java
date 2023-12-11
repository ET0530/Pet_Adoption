package my.edu.utar.petadoption.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

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
    private Bitmap bitmap;


    public Post(String postTitle, String descriptions, String postImage, String posterEmail, String postGender, String posterContact,String userID) {

        this.postTitle = postTitle;
        this.descriptions = descriptions;
        this.postImage = postImage;
        this.posterEmail = posterEmail;
        this.postGender = postGender;
        this.posterContact = posterContact;
        this.timeStamp = ServerValue.TIMESTAMP;
        this.userID=userID;
        if (postImage != null) {
            try {
                byte[] bytes = Base64.decode(postImage, Base64.DEFAULT);
                this.bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            } catch (Exception e) {
                Log.e("BitmapDecodeError", "Error decoding bitmap: " + e.getMessage());
                e.printStackTrace();
            }
        }
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
    public Bitmap getBitmap() {
        return bitmap;
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