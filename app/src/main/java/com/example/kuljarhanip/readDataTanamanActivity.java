package com.example.kuljarhanip;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class readDataTanamanActivity extends AppCompatActivity {
    public EditText namaTanamanTxt, namaLatinTxt, deskripsiTxt, sebaranTxt, habitatTxt, sumberTxt;
    public ImageView imgTanaman;
    public Switch aSwitch;
    public Button updateBtn, changeGambarBtn;
    public String idTanaman, namaTanaman, namaLatin, deskripsi, sebaran, habitat, sumber;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    private static final int SELECT_PICTURE = 100;
    String deskripsiFinal ="";
    dataTanaman dataTanaman = new dataTanaman();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_data_tanaman);
        namaTanamanTxt = findViewById(R.id.namaTanamanReadText);
        namaLatinTxt = findViewById(R.id.namaLatinReadText);
        deskripsiTxt = findViewById(R.id.deskripsiReadText);
        sebaranTxt = findViewById(R.id.sebaranReadText);
        habitatTxt = findViewById(R.id.habitatReadText);
        sumberTxt = findViewById(R.id.sumberReadText);
        imgTanaman = findViewById(R.id.imageViewReadTanaman);
        aSwitch = findViewById(R.id.switchTanaman);
        updateBtn = findViewById(R.id.updateTanamanBtn);
        changeGambarBtn = findViewById(R.id.btnChangeGambarTanaman);

        idTanaman = getIntent().getExtras().get("idTanaman").toString();
        namaTanaman = getIntent().getExtras().get("namaTanaman").toString();
        namaLatin = getIntent().getExtras().get("namaLatin").toString();
        deskripsi = getIntent().getExtras().get("deskripsi").toString();
        sebaran = getIntent().getExtras().get("sebaran").toString();
        habitat = getIntent().getExtras().get("habitat").toString();
        sumber = getIntent().getExtras().get("sumber").toString();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Data Tanaman");
        storageReference = FirebaseStorage.getInstance().getReference().child("TanamanImages")
                .child(namaTanaman).child(namaTanaman+".jpg");

        namaTanamanTxt.setText(namaTanaman);
        namaLatinTxt.setText(namaLatin);

        String[] deskripsiSplit = deskripsi.split(" ");

        for(int i=0 ;i<deskripsiSplit.length;i++)
        {
            deskripsiFinal += deskripsiSplit[i]+" ";
            if (i%5==0 && i!=0)
            {
                deskripsiFinal += "\n";
            }

        }
        System.out.println(deskripsiFinal);
        //deskripsiTxt.setSingleLine(false);
        //deskripsiTxt.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        deskripsiTxt.setText(deskripsi);
        sebaranTxt.setText(sebaran);
        habitatTxt.setText(habitat);
        sumberTxt.setText(sumber);

        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(readDataTanamanActivity.this).load(uri).into(imgTanaman);
            }
        });

        namaTanamanTxt.setEnabled(false);
        namaTanamanTxt.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        namaTanamanTxt.setFocusable(false);

        namaLatinTxt.setEnabled(false);
        namaLatinTxt.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        namaLatinTxt.setFocusable(false);

        deskripsiTxt.setEnabled(false);
        deskripsiTxt.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        deskripsiTxt.setFocusable(true);
        deskripsiTxt.setFocusableInTouchMode(true);

        sebaranTxt.setEnabled(false);
        sebaranTxt.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        sebaranTxt.setFocusable(true);
        sebaranTxt.setFocusableInTouchMode(true);

        habitatTxt.setEnabled(false);
        habitatTxt.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        habitatTxt.setFocusable(true);
        habitatTxt.setFocusableInTouchMode(true);

        sumberTxt.setEnabled(false);
        sumberTxt.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        sumberTxt.setFocusable(true);
        sumberTxt.setFocusableInTouchMode(true);

        changeGambarBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
        });

        imgTanaman.setOnClickListener(v -> {
            Intent i = new Intent(readDataTanamanActivity.this,fullScreenImageTanamanActivity.class);
            i.putExtra("namaTanaman",namaTanaman);
            startActivity(i);
        });
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    namaTanamanTxt.setEnabled(true);
                    namaTanamanTxt.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    namaTanamanTxt.setFocusable(true);
                    namaTanamanTxt.setFocusableInTouchMode(true);

                    namaLatinTxt.setEnabled(true);
                    namaLatinTxt.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    namaLatinTxt.setFocusable(true);
                    namaLatinTxt.setFocusableInTouchMode(true);

                    deskripsiTxt.setEnabled(true);
                    deskripsiTxt.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    deskripsiTxt.setFocusable(true);
                    deskripsiTxt.setFocusableInTouchMode(true);

                    sebaranTxt.setEnabled(true);
                    sebaranTxt.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    sebaranTxt.setFocusable(true);
                    sebaranTxt.setFocusableInTouchMode(true);

                    habitatTxt.setEnabled(true);
                    habitatTxt.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    habitatTxt.setFocusable(true);
                    habitatTxt.setFocusableInTouchMode(true);

                    sumberTxt.setEnabled(true);
                    sumberTxt.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    sumberTxt.setFocusable(true);
                    sumberTxt.setFocusableInTouchMode(true);

                    changeGambarBtn.setVisibility(View.VISIBLE);
                    updateBtn.setVisibility(View.VISIBLE);
                }
                if(!isChecked) {
                    namaTanamanTxt.setEnabled(false);
                    namaTanamanTxt.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    namaTanamanTxt.setFocusable(false);

                    namaLatinTxt.setEnabled(false);
                    namaLatinTxt.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    namaLatinTxt.setFocusable(false);

                    deskripsiTxt.setEnabled(false);
                    deskripsiTxt.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    deskripsiTxt.setFocusable(true);
                    deskripsiTxt.setFocusableInTouchMode(true);

                    sebaranTxt.setEnabled(false);
                    sebaranTxt.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    sebaranTxt.setFocusable(true);
                    sebaranTxt.setFocusableInTouchMode(true);

                    habitatTxt.setEnabled(false);
                    habitatTxt.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    habitatTxt.setFocusable(true);
                    habitatTxt.setFocusableInTouchMode(true);

                    sumberTxt.setEnabled(false);
                    sumberTxt.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    sumberTxt.setFocusable(true);
                    sumberTxt.setFocusableInTouchMode(true);

                    changeGambarBtn.setVisibility(View.GONE);
                    updateBtn.setVisibility(View.INVISIBLE);
                }
            }
        });
        updateBtn.setOnClickListener(v -> {
            updateTanaman();
            startActivity(new Intent(readDataTanamanActivity.this,mainMenuActivity.class));
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

    public void updateTanaman()
    {
        final String namaTanaman = namaTanamanTxt.getText().toString();
        final String namaLatin = namaLatinTxt.getText().toString();
        final String deskripsi = namaLatinTxt.getText().toString();
        final String sebaran = sebaranTxt.getText().toString();
        final String habitat = habitatTxt.getText().toString();
        final String sumber = sumberTxt.getText().toString();
        final StorageReference storageReference1 = storageReference;

        imgTanaman.setDrawingCacheEnabled(true);
        imgTanaman.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imgTanaman.getDrawable()).getBitmap();
        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,120,120, false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] dataImg = baos.toByteArray();
        final UploadTask uploadTask = storageReference1.putBytes(dataImg);
        Toast.makeText(readDataTanamanActivity.this,"Data Tanaman"+namaTanaman+" Berhasi Diupdate",Toast.LENGTH_LONG).show();
        databaseReference.child(idTanaman).child("namaTanaman").setValue(namaTanaman);
        databaseReference.child(idTanaman).child("namaLatin").setValue(namaLatin);
        databaseReference.child(idTanaman).child("deskripsi").setValue(deskripsi);
        databaseReference.child(idTanaman).child("sebaran").setValue(sebaran);
        databaseReference.child(idTanaman).child("habitat").setValue(habitat);
        databaseReference.child(idTanaman).child("sumber").setValue(sumber);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageMetadata snapshotMetadata = taskSnapshot.getMetadata();
                Task<Uri> downloadUrl = storageReference1.child(namaTanaman).child(namaTanaman+".jpg").getDownloadUrl();
                downloadUrl.addOnSuccessListener((new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageQRRef = uri.toString();
//                                        dataEksplan.setLinkQR(imageQRRef);
                        //Toast.makeText(insertDataEksplanActivity.this,"QR Berhasil Dimasukkan",Toast.LENGTH_LONG).show();
                        databaseReference.child(idTanaman).child("linkImages").setValue(imageQRRef);
                        //finish();
                    }
                }));
            }
        });



    }
}
