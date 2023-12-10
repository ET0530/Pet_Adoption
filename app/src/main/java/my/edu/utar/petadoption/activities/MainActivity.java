package my.edu.utar.petadoption.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import my.edu.utar.petadoption.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity implements PostAdapter.OnPostClickListener {

    private static final int USER_REQUEST_CODE = 50;
    private List<Post> posts = new ArrayList<>();
    private PostAdapter postAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ImageButton postButton = findViewById(R.id.imageButton2);

        postButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, WriteNewPost.class);
            startActivityForResult(intent, USER_REQUEST_CODE);
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerViewPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        postAdapter = new PostAdapter(posts, this);
        recyclerView.setAdapter(postAdapter);

        retrieveAndDisplayPosts();
    }
    private void retrieveAndDisplayPosts() {

        List<Post> localPosts = getLocalPosts();
        posts.addAll(localPosts);

        // retrieve posts from Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                posts.clear();

                posts.addAll(localPosts);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if (post != null) {
                        posts.add(post);
                    }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to retrieve posts: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<Post> getLocalPosts() {
        List<Post> samplePosts = new ArrayList<>();

        samplePosts.add(new Post(
                "Seeking for adopters",
                "Found this cat near the back of my farm.",
                Uri.parse("android.resource://my.edu.utar.petadoption/drawable/sample_pic1"),
                "1 years old",
                "Male",
                "012-3456789"
        ));

        samplePosts.add(new Post(
                "Any adopt volunteers?",
                "Saw this poor fella lying at this walkway.",
                Uri.parse("android.resource://my.edu.utar.petadoption/drawable/sample_pic2"),
                "2 years old",
                "Female",
                "011-2345678"
        ));

        samplePosts.add(new Post(
                "Helping this kitten to find a home",
                "Encountered this cute kitten while jungle tracking!",
                Uri.parse("android.resource://my.edu.utar.petadoption/drawable/sample_pic3"),
                "2 weeks old",
                "Male",
                "010-1234567"
        ));

        samplePosts.add(new Post(
                "Puppy lover don't miss out!",
                "This puppy was so excited to be adopt, feel free to contact me.",
                Uri.parse("android.resource://my.edu.utar.petadoption/drawable/sample_pic4"),
                "2 weeks old",
                "Male",
                "010-1234567"
        ));

        return samplePosts;

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == USER_REQUEST_CODE) {

            String title = data.getStringExtra("title");
            String description = data.getStringExtra("content");
            String imageUriString = data.getStringExtra("imageUri");
            String birth = data.getStringExtra("birth");
            String gender = data.getStringExtra("gender");
            String contact = data.getStringExtra("contact");

            Uri imageUri = Uri.parse(imageUriString);

            // Upload image to Firebase Storage
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("post_images");
            final StorageReference imageFilePath = storageReference.child(imageUri.getLastPathSegment());

            imageFilePath.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {

                imageFilePath.getDownloadUrl().addOnSuccessListener(uri -> {

                    Post newPost = new Post(title, description, imageUri, birth, gender, contact);

                    // Add post to the Realtime Database
                    addPost(newPost);

                    // Add new post to the local list for display
                    posts.add(0, newPost);
                    postAdapter.notifyDataSetChanged();

                    Toast.makeText(MainActivity.this, "Post added successfully", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this, "Couldn't get image download URL", Toast.LENGTH_SHORT).show();
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(MainActivity.this, "Couldn't upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void addPost(Post newPost) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Posts").push();

        // get post unique ID and update post key
        String key = myRef.getKey();
        newPost.setPostKey(key);

        Log.d("FirebaseWrite", "Writing post to database: " + newPost.toString());

        // add post data to firebase database
        myRef.setValue(newPost).addOnSuccessListener(unused -> {
            Toast.makeText(MainActivity.this, "Post added successfully", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(MainActivity.this, "Failed to add post to database: " + e.getMessage(), Toast.LENGTH_SHORT).show();

            Log.e("FirebaseWrite", "Failed to write post to database: " + e.getMessage());
        });
    }
    @Override
    public void onPostClick(int position) {

        Post selectedPost = posts.get(position);

        Intent intent = new Intent(this, PostDetailActivity.class);

        intent.putExtra("title", selectedPost.getTitle());
        intent.putExtra("description", selectedPost.getContent());

        if (selectedPost.getImageUri() != null) {
            intent.putExtra("imageUri", selectedPost.getImageUri().toString());
        }

        intent.putExtra("birth", selectedPost.getBirth());
        intent.putExtra("gender", selectedPost.getGender());
        intent.putExtra("contact", selectedPost.getContact());

        startActivity(intent);
    }
}