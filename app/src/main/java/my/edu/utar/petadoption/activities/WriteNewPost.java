package my.edu.utar.petadoption.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.edu.utar.petadoption.R;
import my.edu.utar.petadoption.databinding.ActivityWriteNewPostBinding;
import my.edu.utar.petadoption.models.Post;
import my.edu.utar.petadoption.utilities.Constants;
import my.edu.utar.petadoption.utilities.PreferenceManager;


public class WriteNewPost extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private List<Post> posts = new ArrayList<>();
    private PostAdapter postAdapter;
    ActivityResultLauncher<Intent> activityLauncher;
    String[] cameraPermissions;
    String[] storagePermissions;
    EditText titleEt, descriptionEt, emailEt, genderEt, contactEt;
    ImageView imageIv;
    Button uploadBtn;
    Uri imageUri = null;
    private ActivityWriteNewPostBinding binding;
    private String encodedImage;
    HashMap<String, Object> post = new HashMap<>();
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_new_post);

        binding = ActivityWriteNewPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add New Post");
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        setListeners();

        preferenceManager = new PreferenceManager(getApplicationContext());

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        titleEt = findViewById(R.id.pTitleEt);
        descriptionEt = findViewById(R.id.pDescriptionEt);
        imageIv = findViewById(R.id.pImageIv);
        uploadBtn = findViewById(R.id.pUploadBtn);
        emailEt = findViewById(R.id.pEmailEt);
        genderEt = findViewById(R.id.pGenderEt);
        contactEt = findViewById(R.id.pContactEt);

        posts = new ArrayList<>();
        postAdapter = new PostAdapter(posts, null);

        /*activityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {

                            String title = data.getStringExtra("title");
                            String description = data.getStringExtra("content");
                            String imageUri = data.getStringExtra("imageUri");
                            String birth = data.getStringExtra("birth");
                            String gender = data.getStringExtra("gender");
                            String contact = data.getStringExtra("contact");

                            Post newPost = new Post(title, description, imageUri, birth, gender, contact);

                            posts.add(0, newPost); // add new post on top of previous post

                            postAdapter.notifyDataSetChanged();
                        }
                    }
                });*/
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void setListeners(){
        binding.imageButton3.setOnClickListener(v->{
            Intent intent = new Intent(WriteNewPost.this, MainActivity.class);
            startActivity(intent);
        });
        binding.imageButton4.setOnClickListener(v->{
            Intent intent = new Intent(WriteNewPost.this, UserProfile.class);
            startActivity(intent);
        });

        binding.pImageIv.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });

        binding.pUploadBtn.setOnClickListener(v -> {
            Posts();
        });
    }

    private String encodeImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight,false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result ->{
                if (result.getResultCode() == RESULT_OK){
                    if(result.getData() != null){
                        Uri imageUrl = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUrl);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.pImageIv.setImageBitmap(bitmap);
                            encodedImage = encodeImage(bitmap);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private void Posts(){
        post.put(Constants.KEY_POST_TITLE,binding.pTitleEt.getText().toString());
        post.put(Constants.KEY_POST_DESCRIPTIONS, binding.pDescriptionEt.getText().toString());
        post.put(Constants.KEY_POSTER_EMAIL, binding.pEmailEt.getText().toString());
        post.put(Constants.KEY_POST_GENDER, binding.pGenderEt.getText().toString());
        post.put(Constants.KEY_POSTER_CONTACT, binding.pContactEt.getText().toString());
        post.put(Constants.KEY_POST_IMAGE, encodedImage);
        post.put(Constants.KEY_USER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
        database.collection(Constants.KEY_COLLECTION_POST)
                .add(post)
                .addOnSuccessListener(documentReference -> {
                    showToast("Saved");
                    Intent intent = new Intent(WriteNewPost.this, MainActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(exception -> {
                    showToast(exception.getMessage());
                });

    }



/*
        // get image from camera/gallery on click
        imageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });

        // post button
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = titleEt.getText().toString().trim();
                String description = descriptionEt.getText().toString().trim();
                String birth = birthEt.getText().toString().trim();
                String gender = genderEt.getText().toString().trim();
                String contact = contactEt.getText().toString().trim();

                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(WriteNewPost.this, "Enter title", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(description)) {
                    Toast.makeText(WriteNewPost.this, "Enter description", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(birth)) {
                    Toast.makeText(WriteNewPost.this, "Enter pet age", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(gender)) {
                    Toast.makeText(WriteNewPost.this, "Enter pet gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(contact)) {
                    Toast.makeText(WriteNewPost.this, "Enter contact number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (imageUri == null) {
                    Toast.makeText(WriteNewPost.this, "Select an image", Toast.LENGTH_SHORT).show();
                    return;
                }

                saveImageLocally(title, description);
                Toast.makeText(WriteNewPost.this, "Published", Toast.LENGTH_SHORT).show();

                Intent resultIntent = new Intent();
                resultIntent.putExtra("title", title);
                resultIntent.putExtra("content", description);
                resultIntent.putExtra("imageUri", imageUri.toString());
                resultIntent.putExtra("birth", birth);
                resultIntent.putExtra("gender", gender);
                resultIntent.putExtra("contact", contact);

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }



    /*
    private void saveImageLocally(String title, String description) {
        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, title);
            values.put(MediaStore.Images.Media.DESCRIPTION, description);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            OutputStream outputStream = getContentResolver().openOutputStream(imageUri);

            Drawable drawable = imageIv.getDrawable();

            Bitmap yourBitmap = ((BitmapDrawable) drawable).getBitmap();

            if (yourBitmap != null) {
                yourBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                outputStream.close();
            } else {
                Toast.makeText(WriteNewPost.this, "Error: Bitmap is null", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(WriteNewPost.this, "Error saving image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showImagePickDialog() {

        String[] options = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Choose image from");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {
                    // camera
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                }
                if (which == 1) {
                    // gallery
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pick");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Descr");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                // image is picked from gallery, get uri of image
                imageUri = data.getData();

                // set to imageView
                imageIv.setImageURI(imageUri);
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                // image is captured from camera, set to imageView
                imageIv.setImageURI(imageUri);
            }
        }
    }




     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed(); // back button
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}