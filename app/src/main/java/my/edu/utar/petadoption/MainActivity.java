package my.edu.utar.petadoption;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

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

        retrieveAndDisplayLocalPosts();
    }
    private void retrieveAndDisplayLocalPosts() {

        List<Post> localPosts = getLocalPosts();
        posts.addAll(localPosts);
        postAdapter.notifyDataSetChanged();
    }
    private List<Post> getLocalPosts() {
        return new ArrayList<>();
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
            String characteristics = data.getStringExtra("characteristics");

            Post newPost = new Post(title, description, Uri.parse(imageUriString), birth, gender, characteristics);

            posts.add(0, newPost);
            postAdapter.notifyDataSetChanged();
        }
    }
    // Implementation of the OnPostClickListener interface method
    @Override
    public void onPostClick(int position) {
        // Handle the click event for the selected post
        Post selectedPost = posts.get(position);

        // Create an Intent to navigate to the PostDetailActivity
        Intent intent = new Intent(this, PostDetailActivity.class);

        // Pass the selected post details to the PostDetailActivity
        intent.putExtra("title", selectedPost.getTitle());
        intent.putExtra("description", selectedPost.getContent());
        intent.putExtra("imageUri", selectedPost.getImageUri().toString());
        intent.putExtra("birth", selectedPost.getBirth());
        intent.putExtra("gender", selectedPost.getGender());
        intent.putExtra("characteristics", selectedPost.getCharacteristics());

        // Start the PostDetailActivity
        startActivity(intent);
    }
}

