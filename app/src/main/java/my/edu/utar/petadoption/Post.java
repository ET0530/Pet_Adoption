package my.edu.utar.petadoption;

import android.net.Uri;

public class Post {
    private String title;
    private String content;
    private Uri imageUri;
    private String birth;
    private String gender;
    private String characteristics;

    public Post(String title, String content, Uri imageUri, String birth, String gender, String characteristics) {
        this.title = title;
        this.content = content;
        this.imageUri = imageUri;
        this.birth = birth;
        this.gender = gender;
        this.characteristics = characteristics;
    }

    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public Uri getImageUri() {
        return imageUri;
    }
    public String getBirth() {
        return birth;
    }

    public String getGender() {
        return gender;
    }

    public String getCharacteristics() {
        return characteristics;
    }
}