package my.edu.utar.petadoption.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import my.edu.utar.petadoption.R;

public class MainActivity extends AppCompatActivity {

    private static final int USER_REQUEST_CODE = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton btn_chat = findViewById(R.id.imageButton3);
        ImageButton user_profile = findViewById(R.id.imageButton4);

        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChatSpace.class);
                startActivity(intent);
            }
        });

        user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserProfile.class);
                startActivity(intent);
            }
        });


    }


}