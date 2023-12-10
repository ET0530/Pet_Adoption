package my.edu.utar.petadoption.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import my.edu.utar.petadoption.databinding.ActivityUserProfileBinding;
import my.edu.utar.petadoption.utilities.Constants;
import my.edu.utar.petadoption.utilities.PreferenceManager;

public class UserProfile extends BaseActivity {

    private ActivityUserProfileBinding binding;
    private PreferenceManager preferenceManager;
    private String encodedImage;
    HashMap<String, Object> user = new HashMap<>();
    FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        loadUserDetails();
        setListeners();
    }

    private void setListeners(){
        binding.imageSignOut.setOnClickListener(v -> signOut());
        binding.btnSave.setOnClickListener(v -> save());
        binding.imageProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });

        binding.imageButton.setOnClickListener(v->{
            Intent intent = new Intent(UserProfile.this, MainActivity.class);
            startActivity(intent);
        });
        binding.imageButton2.setOnClickListener(v->{
            Intent intent = new Intent(UserProfile.this, WriteNewPost.class);
            startActivity(intent);
        });
        binding.imageButton3.setOnClickListener(v->{
            Intent intent = new Intent(UserProfile.this, ChatSpace.class);
            startActivity(intent);
        });
    }

    private void signOut(){
        showToast("Signing out...");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String ,Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> showToast("Unable to sign out"));
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void loadUserDetails(){
        database.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID))
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            binding.gender.setText(document.getString(Constants.KEY_GENDER));
                            binding.personalisedMessages.setText(document.getString(Constants.KEY_PMESSAGES));
                            binding.contactNumber.setText(document.getString(Constants.KEY_CONTACT));
                            binding.username.setText(document.getString(Constants.KEY_NAME));
                            binding.email.setText(" "+document.getString(Constants.KEY_EMAIL));
                            String imageString = document.getString(Constants.KEY_IMAGE);
                            if (imageString != null) {
                                byte[] bytes = Base64.decode(imageString, Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                binding.imageProfile.setImageBitmap(bitmap);
                            }
                        }
                    }
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
                            binding.imageProfile.setImageBitmap(bitmap);
                            encodedImage = encodeImage(bitmap);
                            preferenceManager.putString(Constants.KEY_IMAGE, encodedImage);
                            user.put(Constants.KEY_IMAGE, encodedImage);
                            database.collection(Constants.KEY_COLLECTION_USERS)
                                    .document(preferenceManager.getString(Constants.KEY_USER_ID))
                                    .update(user);
                        }catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private void save(){
        user.put(Constants.KEY_CONTACT,binding.contactNumber.getText().toString());
        user.put(Constants.KEY_GENDER, binding.gender.getText().toString());
        user.put(Constants.KEY_NAME, binding.username.getText().toString());
        user.put(Constants.KEY_PMESSAGES, binding.personalisedMessages.getText().toString());
        database.collection(Constants.KEY_COLLECTION_USERS)
                .document(preferenceManager.getString(Constants.KEY_USER_ID))
                .update(user);
        showToast("Saved");
    }


}