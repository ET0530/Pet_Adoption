package my.edu.utar.petadoption.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import my.edu.utar.petadoption.R;
import my.edu.utar.petadoption.models.Post;
import my.edu.utar.petadoption.utilities.Constants;

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
        String postId = intent.getStringExtra("postId");

        if (postId != null) {

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
        }
    }

    private void updateUI(Post post) {
        TextView titleTextView = findViewById(R.id.pTitleEt);
        TextView descriptionTextView = findViewById(R.id.pDescriptionEt);
        ImageView imageView = findViewById(R.id.pImageIv);
        TextView birthTextView = findViewById(R.id.pBirthEt);
        TextView genderTextView = findViewById(R.id.pGenderEt);
        TextView contactTextView = findViewById(R.id.pContactEt);

        titleTextView.setText(post.getTitle());
        descriptionTextView.setText(post.getContent());
        birthTextView.setText(post.getPosterEmail());
        genderTextView.setText(post.getGender());
        contactTextView.setText(post.getContact());

        if (post.getImageUri() != null) {
            Glide.with(this)
                    .load(post.getImageUri())
                    .into(imageView);
        }
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