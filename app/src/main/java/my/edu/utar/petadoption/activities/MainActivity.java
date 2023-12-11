package my.edu.utar.petadoption.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import my.edu.utar.petadoption.R;
import my.edu.utar.petadoption.models.Post;
import my.edu.utar.petadoption.utilities.Constants;

public class MainActivity extends AppCompatActivity {

    private static final int USER_REQUEST_CODE = 50;

    private List<Post> posts = new ArrayList<>();
    private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton postButton = findViewById(R.id.imageButton2);
        ImageButton btn_chat = findViewById(R.id.imageButton3);
        ImageButton user_profile = findViewById(R.id.imageButton4);

        btn_chat.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ChatSpace.class);
            startActivity(intent);
        });

        postButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WriteNewPost.class);
            startActivityForResult(intent, USER_REQUEST_CODE);
        });

        user_profile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UserProfile.class);
            startActivity(intent);
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerViewPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        postAdapter = new PostAdapter(posts, this::onPostClick);
        recyclerView.setAdapter(postAdapter);

        retrieveAndDisplayPosts();
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void retrieveAndDisplayPosts() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        List<Post> localPosts = getLocalPosts();
        posts.addAll(localPosts);

        database.collection(Constants.KEY_COLLECTION_POST)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (value != null) {
                        posts.clear();

                        for (DocumentSnapshot snapshot : value) {
                            String postTitle = (String) snapshot.get("postTitle");
                            String descriptions = (String) snapshot.get("descriptions");
                            String postImage = (String) snapshot.get("postImage");
                            String posterEmail = (String) snapshot.get("posterEmail");
                            String postGender = (String) snapshot.get("postGender");
                            String posterContact = (String) snapshot.get("posterContact");
                            String userId = (String) snapshot.get("userId");
                            Post post = new Post(postTitle, descriptions, postImage,posterEmail, postGender, posterContact, userId);
                            if (post != null) {
                                posts.add(post);
                                Log.d("PostDebug", post.toString());
                            }
                        }

                        postAdapter.notifyDataSetChanged();
                    }
                });
    }

    private List<Post> getLocalPosts() {
        List<Post> samplePosts = new ArrayList<>();

        samplePosts.add(new Post(
                "Seeking for adopters",
                "Found this cat near the back of my farm.",
                "android.resource://my.edu.utar.petadoption/drawable/sample_pic1",
                "1 years old",
                "Male",
                "012-3456789",
                "100"
        ));

        samplePosts.add(new Post(
                "Any adopt volunteers?",
                "Saw this poor fella lying at this walkway.",
                "android.resource://my.edu.utar.petadoption/drawable/sample_pic2",
                "2 years old",
                "Female",
                "011-2345678",
                "101"
        ));

        samplePosts.add(new Post(
                "Helping this kitten to find a home",
                "Encountered this cute kitten while jungle tracking!",
                "android.resource://my.edu.utar.petadoption/drawable/sample_pic3",
                "2 weeks old",
                "Male",
                "010-1234567",
                "102"
        ));

        samplePosts.add(new Post(
                "Puppy lover don't miss out!",
                "This puppy was so excited to be adopt, feel free to contact me.",
                "android.resource://my.edu.utar.petadoption/drawable/sample_pic4",
                "2 weeks old",
                "Male",
                "010-1234567",
                "103"
        ));

        return samplePosts;
    }

    public void onPostClick(int position) {
        Post selectedPost = posts.get(position);

        Intent intent = new Intent(this, PostDetailActivity.class);

        intent.putExtra("postTitle", selectedPost.getTitle());
        intent.putExtra("descriptions", selectedPost.getContent());

        if (selectedPost.getBitmap() != null) {
            intent.putExtra("postImage", selectedPost.getImageUri());
        }
        intent.putExtra("userId", selectedPost.getUserId());
        intent.putExtra("posterEmail", selectedPost.getPosterEmail());
        intent.putExtra("postGender", selectedPost.getGender());
        intent.putExtra("posterContact", selectedPost.getContact());

        startActivity(intent);
    }
}