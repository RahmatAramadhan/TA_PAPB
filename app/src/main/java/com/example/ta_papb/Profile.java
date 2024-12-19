package com.example.ta_papb;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {

    private ImageView profileImage;
    private TextView profileName;
    private Button changePhotoButton;
    private Button btnLogout;
    private Button btnForum;

    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private FirebaseAuth auth;

    private String userId;
    private Uri selectedImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.profile);

        firestore = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        userId = auth.getCurrentUser().getUid();

        btnLogout = findViewById(R.id.logoutButton);
        btnForum = findViewById(R.id.forumButton);
        profileImage = findViewById(R.id.profileImage);
        profileName = findViewById(R.id.name);
        changePhotoButton = findViewById(R.id.gantiFoto);

        btnLogout.setOnClickListener( v -> logoutUser());
        loadProfileData();
        btnForum.setOnClickListener(v -> {
                Intent intent = new Intent(Profile.this, Forum.class);
                startActivity(intent);
        });

        changePhotoButton.setOnClickListener(v -> openImagePicker());

    }

    private void loadProfileData(){
        firestore.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()){
                String name = documentSnapshot.getString("name");
                String photoPath = documentSnapshot.getString("photoPath");

                profileName.setText(name);

                StorageReference photoRef = storage.getReference(photoPath);
                photoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Picasso.get().load(uri).into(profileImage);
                }).addOnFailureListener( e -> {
                    Toast.makeText(this, "Gagal memuat foto Profil", Toast.LENGTH_SHORT).show();
                });
            } else {
                Toast.makeText(this, "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Gagal mengambil data pengguna: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void openImagePicker(){
        imagePickerLauncher.launch("image/*");
    }

    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null){
                    selectedImageUri = uri;

                    profileImage.setImageURI(selectedImageUri);

                    uploadPhotoToFirebase();
                }
            });

    private void uploadPhotoToFirebase(){
        if(selectedImageUri == null) return;

        String photoPath = "profile_pictures/" + userId + ".jpg";
        StorageReference photoRef = storage.getReference(photoPath);

        photoRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    firestore.collection("users").document(userId)
                            .update("photoPath", photoPath)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Foto profil berhasil diperbarui", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Gagal Memperbarui data foto", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gagal mengunggah foto", Toast.LENGTH_SHORT).show();
                });
    }

    private void logoutUser() {
        auth.signOut();

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }

}