package my.edu.utar.petadoption.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
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
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");

        String imageUriString = intent.getStringExtra("imageUri");
        Uri imageUri = null;
        if (imageUriString != null && !imageUriString.isEmpty()) {
            imageUri = Uri.parse(imageUriString);
        }

        String birth = intent.getStringExtra("birth");
        String gender = intent.getStringExtra("gender");
        String contact = intent.getStringExtra("contact");

        TextView titleTextView = findViewById(R.id.pTitleEt);
        TextView descriptionTextView = findViewById(R.id.pDescriptionEt);
        ImageView imageView = findViewById(R.id.pImageIv);
        TextView birthTextView = findViewById(R.id.pBirthEt);
        TextView genderTextView = findViewById(R.id.pGenderEt);
        TextView contactTextView = findViewById(R.id.pContactEt);

        titleTextView.setText(title);
        descriptionTextView.setText(description);
        birthTextView.setText(birth);
        genderTextView.setText(gender);
        contactTextView.setText(contact);

        Glide.with(this)
                .load(imageUri)
                .into(imageView);
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