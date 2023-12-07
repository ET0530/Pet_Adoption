package my.edu.utar.petadoption;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> posts;
    private OnPostClickListener onPostClickListener;

    public PostAdapter(List<Post> posts, OnPostClickListener onPostClickListener) {
        this.posts = posts;
        this.onPostClickListener = onPostClickListener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view, onPostClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);

        holder.titleEt.setText(post.getTitle());
        holder.descriptionEt.setText(post.getContent());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event to open PostDetailActivity
                onPostClickListener.onPostClick(position);
            }
        });

        Glide.with(holder.itemView.getContext())
                .load(post.getImageUri())
                .into(holder.imageIv);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        EditText titleEt;
        EditText descriptionEt;
        ImageView imageIv;
        OnPostClickListener onPostClickListener;

        public PostViewHolder(@NonNull View itemView, OnPostClickListener onPostClickListener) {
            super(itemView);
            titleEt = itemView.findViewById(R.id.pTitleEt);
            descriptionEt = itemView.findViewById(R.id.pDescriptionEt);
            imageIv = itemView.findViewById(R.id.pImageIv);
            this.onPostClickListener = onPostClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Handle the click event and pass the clicked position to the interface method
            if (onPostClickListener != null) {
                onPostClickListener.onPostClick(getAdapterPosition());
            }
        }
    }

    // Interface to handle click events
    public interface OnPostClickListener {
        void onPostClick(int position);
    }
}
