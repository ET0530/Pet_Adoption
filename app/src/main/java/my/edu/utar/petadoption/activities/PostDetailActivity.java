package my.edu.utar.petadoption.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import my.edu.utar.petadoption.R;

public class PostDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Post details");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bitmap bitmap=null;
        String postTitle = intent.getStringExtra("postTitle");
        String descriptions = intent.getStringExtra("descriptions");
        String postGender = intent.getStringExtra("postGender");
        String postImage = intent.getStringExtra("postImage");
        if (postImage != null) {
            try {
                byte[] bytes = Base64.decode(postImage, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            } catch (Exception e) {
                Log.e("BitmapDecodeError", "Error decoding bitmap: " + e.getMessage());
                e.printStackTrace();
            }
        }
        String posterContact = intent.getStringExtra("posterContact");
        String posterEmail = intent.getStringExtra("posterEmail");
        String userId = intent.getStringExtra("userId");
        updateUI(postTitle,descriptions,postGender,bitmap,posterContact,posterEmail,userId);


        /*if (postId != null) {

            DocumentReference postRef = FirebaseFirestore.getInstance().collection(Constants.KEY_COLLECTION_POST).document(postId);

            postRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Convert the document snapshot to a Post object
                        Post post = document.toObject(Post.class);

                        if (post != null) {
                            updateUI(post);
                        }
                    }
                } else {
                    // Handle errors
                }
            });
        }*/
    }

    private void updateUI(String postTitle,String descriptions,String postGender,Bitmap bitmap,String posterContact,String posterEmail, String userId) {
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
        imageView.setImageBitmap(bitmap);
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