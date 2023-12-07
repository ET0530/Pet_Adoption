package my.edu.utar.petadoption.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import my.edu.utar.petadoption.R;

public class PostDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // Get data from the intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        String imageUriString = intent.getStringExtra("imageUri");
        Uri imageUri = Uri.parse(imageUriString);
        String birth = intent.getStringExtra("birth");
        String gender = intent.getStringExtra("gender");
        String characteristics = intent.getStringExtra("characteristics");

        // Set up views
        TextView titleTextView = findViewById(R.id.pTitleEt);
        TextView descriptionTextView = findViewById(R.id.pDescriptionEt);
        ImageView imageView = findViewById(R.id.pImageIv);
        TextView birthTextView = findViewById(R.id.pBirthEt);
        TextView genderTextView = findViewById(R.id.pGenderEt);
        TextView characteristicsTextView = findViewById(R.id.pCharacteristicsEt);

        // Set data to views
        titleTextView.setText("Name: " + title);
        descriptionTextView.setText(description);
        birthTextView.setText("Birth: " + birth);
        genderTextView.setText("Gender: " + gender);
        characteristicsTextView.setText("Characteristics: " + characteristics);

        // Load image using Glide
        Glide.with(this)
                .load(imageUri)
                .into(imageView);
    }
}