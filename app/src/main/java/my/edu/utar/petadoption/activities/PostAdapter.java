package my.edu.utar.petadoption.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import my.edu.utar.petadoption.R;
import my.edu.utar.petadoption.models.Post;

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

        // Add this log
        Log.d("GlideDebug", "Image URL: " + post.getImageUri());

        holder.titleEt.setText(post.getTitle());
        holder.descriptionEt.setText(post.getContent());

        /*Glide.with(holder.itemView.getContext())
                .load(post.getImageUri())
                .into(holder.imageIv);*/

        if (post.getImageUri() != null) {
            try {
                byte[] bytes = Base64.decode(post.getImageUri(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.imageIv.setImageBitmap(bitmap);
            } catch (Exception e) {
                Log.e("BitmapDecodeError", "Error decoding bitmap: " + e.getMessage());
                e.printStackTrace();
            }
        }

        holder.itemView.setOnClickListener(v -> {
           // to access details of the post
            onPostClickListener.onPostClick(position);
        });
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
            if (onPostClickListener != null) {
                onPostClickListener.onPostClick(getAdapterPosition());
            }
        }
    }

    public interface OnPostClickListener {
        void onPostClick(int position);
    }


}
