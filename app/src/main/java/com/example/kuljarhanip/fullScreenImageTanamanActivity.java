package com.example.kuljarhanip;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class fullScreenImageTanamanActivity extends AppCompatActivity {
    public ImageView img;
    StorageReference storageReference;
    String namaTanaman;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image_tanaman);
        namaTanaman = getIntent().getExtras().get("namaTanaman").toString();
        storageReference = FirebaseStorage.getInstance().getReference().child("TanamanImages")
                .child(namaTanaman).child(namaTanaman+".jpg");
        img = findViewById(R.id.imageViewTanamanFS);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(fullScreenImageTanamanActivity.this).load(uri).into(img);
            }
        });

    }
}
