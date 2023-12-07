package my.edu.utar.petadoption.activities;

import android.Manifest;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import my.edu.utar.petadoption.R;


public class WriteNewPost extends AppCompatActivity {

    // permissions constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;

    // image pick constants
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;

    ActivityResultLauncher<Intent> activityLauncher;
    private List<Post> posts = new ArrayList<>();
    private PostAdapter postAdapter;

    // permissions array
    String[] cameraPermissions;
    String[] storagePermissions;

    //views
    EditText titleEt, descriptionEt, birthEt, genderEt, characteristicsEt;
    ImageView imageIv;
    Button uploadBtn;
    Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_new_post);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add New Post");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // initialize permissions arrays
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // initialize views
        titleEt = findViewById(R.id.pTitleEt);
        descriptionEt = findViewById(R.id.pDescriptionEt);
        imageIv = findViewById(R.id.pImageIv);
        uploadBtn = findViewById(R.id.pUploadBtn);
        birthEt = findViewById(R.id.pBirthEt);
        genderEt = findViewById(R.id.pGenderEt);
        characteristicsEt = findViewById(R.id.pCharacteristicsEt);

        posts = new ArrayList<>();
        postAdapter = new PostAdapter(posts, null);

        activityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            // Handle the result data here
                            String title = data.getStringExtra("title");
                            String description = data.getStringExtra("content");
                            imageUri = Uri.parse(data.getStringExtra("imageUri"));
                            String birth = data.getStringExtra("birth");
                            String gender = data.getStringExtra("gender");
                            String characteristics = data.getStringExtra("characteristics");

                            // Create a new Post object with the retrieved data
                            Post newPost = new Post(title, description, imageUri, birth, gender, characteristics);

                            // Add the new post to the posts list
                            posts.add(0, newPost); // Add at the beginning of the list to show the newest post first

                            // Notify the adapter that the data has changed
                            postAdapter.notifyDataSetChanged();
                        }
                    }
                });

        // get image from camera/gallery on click
        imageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show image pick dialog
                showImagePickDialog();
            }
        });

        // post button
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get data (title, description) from EditTexts
                String title = titleEt.getText().toString().trim();
                String description = descriptionEt.getText().toString().trim();
                String birth = birthEt.getText().toString().trim();
                String gender = genderEt.getText().toString().trim();
                String characteristics = characteristicsEt.getText().toString().trim();

                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(WriteNewPost.this, "Enter title...", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(description)) {
                    Toast.makeText(WriteNewPost.this, "Enter description...", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if an image is selected
                if (imageUri == null) {
                    Toast.makeText(WriteNewPost.this, "Select an image...", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Save the image locally
                saveImageLocally(title, description);

                // Notify the user that the post is saved locally
                Toast.makeText(WriteNewPost.this, "Post saved", Toast.LENGTH_SHORT).show();

                // upload user input to the new post
                Intent resultIntent = new Intent();
                resultIntent.putExtra("title", title);
                resultIntent.putExtra("content", description);
                resultIntent.putExtra("imageUri", imageUri.toString());
                resultIntent.putExtra("birth", birth);
                resultIntent.putExtra("gender", gender);
                resultIntent.putExtra("characteristics", characteristics);

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    private void saveImageLocally(String title, String description) {
        try {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, title);
            values.put(MediaStore.Images.Media.DESCRIPTION, description);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

            // Insert image to the gallery
            imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            // Open an OutputStream to the image
            OutputStream outputStream = getContentResolver().openOutputStream(imageUri);

            // Get the drawable from the ImageView
            Drawable drawable = imageIv.getDrawable();

            // Convert the drawable to a Bitmap
            Bitmap yourBitmap = ((BitmapDrawable) drawable).getBitmap();

            // Compress and save the image
            if (yourBitmap != null) {
                yourBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                // Close the OutputStream
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
        // options (camera, gallery) to show in dialog
        String[] options = {"Camera", "Gallery"};

        // dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose image from");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // item click handle
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
        // create and show dialog
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
}