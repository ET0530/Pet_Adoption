package my.edu.utar.petadoption.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import my.edu.utar.petadoption.R;
import my.edu.utar.petadoption.models.User;
import my.edu.utar.petadoption.utilities.Constants;
import my.edu.utar.petadoption.utilities.PreferenceManager;

public class PostDetailActivity extends AppCompatActivity {

    private ImageView imageProfile;
    TextView nameView;
    private ImageView btnChat;
    private User user;
    String userId;
    String postTitle ;
    String descriptions ;
    String postGender ;
    String postImage ;
    String posterContact ;
    String posterEmail ;
    String userName;
    String userImage;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        preferenceManager = new PreferenceManager(getApplicationContext());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Post details");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        imageProfile = findViewById(R.id.imageProfile);
        btnChat = findViewById(R.id.btn_chat);
        nameView = findViewById(R.id.textName);

        Intent intent = getIntent();

        postTitle = intent.getStringExtra("postTitle");
        descriptions = intent.getStringExtra("descriptions");
        postGender = intent.getStringExtra("postGender");
        postImage = intent.getStringExtra("postImage");

        posterContact = intent.getStringExtra("posterContact");
        posterEmail = intent.getStringExtra("posterEmail");
        userId = intent.getStringExtra("userId");
        if (Objects.equals(userId, preferenceManager.getString(Constants.KEY_USER_ID))){
            btnChat.setVisibility(View.INVISIBLE);
        }
        updateUI();

        btnChat.setOnClickListener(v -> {
            Intent intent1 = new Intent(getApplicationContext(), ChatActivity.class);
            intent1.putExtra(Constants.KEY_USER, user);
            startActivity(intent1);
        });

    }

    private void updateUI() {
        getUserFromFirestore();
        TextView titleTextView = findViewById(R.id.pTitleEt);
        TextView descriptionTextView = findViewById(R.id.pDescriptionEt);
        ImageView imageView = findViewById(R.id.pImageIv);
        TextView emailTextView = findViewById(R.id.pEmailEt);
        TextView genderTextView = findViewById(R.id.pGenderEt);
        TextView contactTextView = findViewById(R.id.pContactEt);


        titleTextView.setText(postTitle);
        descriptionTextView.setText(descriptions);
        emailTextView.setText(posterEmail);
        genderTextView.setText(postGender);
        contactTextView.setText(posterContact);
        imageView.setImageBitmap(getBitmap(postImage));

    }

    private void getUserFromFirestore() {
        FirebaseFirestore.getInstance()
                .collection(Constants.KEY_COLLECTION_USERS)
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Log.d("Ifhave", documentSnapshot.toString());
                    if (documentSnapshot.exists()) {
                        user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            user.setId(userId);
                            Log.d("Firestore", user.image);
                            imageProfile.setImageBitmap(getBitmap(user.getImage()));
                            nameView.setText(user.getName());
                        }
                    } else {

                    }
                })
                .addOnFailureListener(e -> {

                });
    }


    public Bitmap getBitmap(String image) {
        Bitmap bitmap = null;
        if (image != null) {
            try {
                byte[] bytes = Base64.decode(image, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

            } catch (Exception e) {
                Log.e("BitmapDecodeError", "Error decoding bitmap: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}