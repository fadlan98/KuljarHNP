package com.example.kuljarhanip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class insertTanamanActivity extends AppCompatActivity {
    private EditText namaTanaman, namaLatin, deksripsi, sebaran, habitat, sumber;
    public ImageView imgTanaman;
    private Button insertTanamanBtn, chooseFileBtn;
    DatabaseReference databaseReference;
    dataTanaman dataTanaman = new dataTanaman();

    private static final int SELECT_PICTURE = 100;
    StorageReference storageReference, imageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_tanaman);
        namaTanaman = findViewById(R.id.namaTanamanInsertText);
        namaLatin = findViewById(R.id.namaLatinInsertText);
        deksripsi = findViewById(R.id.deskripsiInsertText);
        sebaran = findViewById(R.id.sebaranInsertText);
        habitat = findViewById(R.id.habitatInsertText);
        sumber = findViewById(R.id.sumberInsertText);
        chooseFileBtn = findViewById(R.id.btnInsertImageTanaman);
        insertTanamanBtn = findViewById(R.id.insertTanamanBtn);
        imgTanaman = findViewById(R.id.imageViewTanamanInsert);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        chooseFileBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
        });
        insertTanamanBtn.setOnClickListener(v -> {
           insertData();
            //deleteData();
            startActivity(new Intent(insertTanamanActivity.this,mainMenuActivity.class));
            finish();
        });


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    String path = selectedImageUri.getPath();
                    //Log.e("image path", path + "");
                    imgTanaman.setImageURI(selectedImageUri);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void deleteData(){
        DatabaseReference dbNode = FirebaseDatabase.getInstance().getReference().getRoot().child("Daftar Tanaman");
        dbNode.setValue(null);
       // dbwew.
       // String id = dbTanaman.push().getKey();

    }

    public void insertData()
    {
        final String namaTanamann = namaTanaman.getText().toString().trim();
        final String namaLatinn = namaLatin.getText().toString().trim();
        final String deksripsii = deksripsi.getText().toString().trim();
        final String habitatt = habitat.getText().toString().trim();
        final String sumberr = sumber.getText().toString().trim();
        final String sebarann = sebaran.getText().toString().trim();
        final DatabaseReference dbTanaman = databaseReference.child("Daftar Tanaman");
        imageRef = storageReference.child("TanamanImages");

        imgTanaman.setDrawingCacheEnabled(true);
        imgTanaman.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imgTanaman.getDrawable()).getBitmap();
        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 120, 120, false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] dataImg = baos.toByteArray();
        final UploadTask uploadTask = imageRef.child(namaTanamann).child(namaTanamann + ".jpg").putBytes(dataImg);
        String id = dbTanaman.push().getKey();
        dataTanaman.setNamaTanaman(namaTanamann);
        dataTanaman.setNamaLatin(namaLatinn);
        dataTanaman.setDeskripsi(deksripsii);
        dataTanaman.setSebaran(sebarann);
        dataTanaman.setHabitat(habitatt);
        dataTanaman.setSumber(sumberr);
        dbTanaman.child(id).setValue(dataTanaman);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageMetadata snapshotMetadata = taskSnapshot.getMetadata();
                Task<Uri> downloadUrl = imageRef.child(namaTanamann).child(namaTanamann + ".jpg").getDownloadUrl();
                downloadUrl.addOnSuccessListener((new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageQRRef = uri.toString();
//                      dataEksplan.setLinkQR(imageQRRef);
                        //Toast.makeText(insertDataEksplanActivity.this,"QR Berhasil Dimasukkan",Toast.LENGTH_LONG).show();
                        dbTanaman.child(id).child("linkImages").setValue(imageQRRef);
                        //finish();
                    }
                }));
            }
        });
        Toast.makeText(insertTanamanActivity.this, "Data Berhasil Dimasukkan", Toast.LENGTH_LONG).show();
    }

}
