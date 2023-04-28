package com.example.kuljarhanip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfDocument;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class insertQREksplanActivity extends AppCompatActivity {

    private EditText namaEksplanTxt, jenisTanamanTxt;
    private Button generateQRBtn, saveQRBtn;
    public ImageView qrImg;
    public String namaEksplan, jenisTanaman, idEksplan, idEksplanAnakan;
    private StorageReference mStorageRef, imgRef;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    dataEksplan dataEksplan = new dataEksplan();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_qreksplan);
        namaEksplanTxt = findViewById(R.id.namaEksplanTextQR);
        jenisTanamanTxt = findViewById(R.id.jenisTanamanTextQR);
        generateQRBtn = findViewById(R.id.generateQRButton);
        saveQRBtn = findViewById(R.id.saveQRButton);
        qrImg = findViewById(R.id.imgQR);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser theUser = mAuth.getCurrentUser();
        final String _UID = theUser.getUid().toString();

        idEksplan = getIntent().getExtras().get("idEksplan").toString();
        namaEksplan = getIntent().getExtras().get("namaEksplan").toString();
        jenisTanaman = getIntent().getExtras().get("jenisTanaman").toString();

        namaEksplanTxt.setText(namaEksplan);
        jenisTanamanTxt.setText(jenisTanaman);
        namaEksplanTxt.setEnabled(false);
        jenisTanamanTxt.setEnabled(false);
        final StorageReference QRRef = mStorageRef.child("QRImages");

        ActivityCompat.requestPermissions(insertQREksplanActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        imgRef = FirebaseStorage.getInstance().getReference().child("QRImages").child(namaEksplan).child(namaEksplan+".jpg");
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Toast.makeText(insertQREksplanActivity.this,"QR Sudah Pernah Dibuat", Toast.LENGTH_SHORT).show();
                Glide.with(insertQREksplanActivity.this).load(uri).into(qrImg);
            }
        });
        generateQRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String dataIdEksplan = databaseReference.child("List Eksplan").child(idEksplan).toString();
                String dataIdEksplan = idEksplan;
                QRGEncoder qrgEncoder = new QRGEncoder(dataIdEksplan,null, QRGContents.Type.TEXT,500);
                Bitmap qrBits = qrgEncoder.getBitmap();
                qrImg.setImageBitmap(qrBits);
            }
        });
        saveQRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save the imageview to storage and integrate it with the database
                final DatabaseReference dbEksplan = databaseReference.child("List Eksplan");
                final DatabaseReference dbAdmin = databaseReference.child("Admin");
                final String[] codeEksplan = idEksplan.split(" ");

                final String id = dbEksplan.push().getKey();
                qrImg.setDrawingCacheEnabled(true);
                qrImg.buildDrawingCache();
                Bitmap bitmap = ((BitmapDrawable) qrImg.getDrawable()).getBitmap();
                final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,120,120, false);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
                byte[] dataImg = baos.toByteArray();

                dbAdmin.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String _UIDnama = dataSnapshot.child(_UID).child("namaAdmin").getValue().toString();
                        CreatePDF(scaledBitmap,jenisTanaman, namaEksplan ,namaEksplan, _UIDnama);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                final UploadTask uploadTask = QRRef.child(namaEksplan).child(namaEksplan+".jpg").putBytes(dataImg);

                if(codeEksplan.length > 1){
                    idEksplan = codeEksplan[0];
                    idEksplanAnakan = codeEksplan[1];
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            StorageMetadata snapshotMetadata = taskSnapshot.getMetadata();
                            Task<Uri> downloadUrl = QRRef.child(namaEksplan).child(namaEksplan+".jpg").getDownloadUrl();
                            downloadUrl.addOnSuccessListener((new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageQRRef = uri.toString();
//                                        dataEksplan.setLinkQR(imageQRRef);
                                    Toast.makeText(insertQREksplanActivity.this,"QR Berhasil Dimasukkan",Toast.LENGTH_LONG).show();
                                    dbEksplan.child(idEksplan).child("Anakan").child(idEksplanAnakan).child("linkQR").setValue(imageQRRef);
                                    finish();
                                }
                            }));
                        }
                    });
                }
                else{
                    idEksplan = codeEksplan[0];
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            StorageMetadata snapshotMetadata = taskSnapshot.getMetadata();
                            Task<Uri> downloadUrl = QRRef.child(namaEksplan).child(namaEksplan+".jpg").getDownloadUrl();
                            downloadUrl.addOnSuccessListener((new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageQRRef = uri.toString();
//                                        dataEksplan.setLinkQR(imageQRRef);
                                    Toast.makeText(insertQREksplanActivity.this,"QR Berhasil Dimasukkan",Toast.LENGTH_LONG).show();
                                    dbEksplan.child(idEksplan).child("linkQR").setValue(imageQRRef);
                                    finish();
                                }
                            }));
                        }
                    });
                }

            }
        });
    }
    private void CreatePDF(Bitmap bmp,String jenisTanaman, String namaEksplan, String docName, String theUser){
        PdfDocument pdfDocument = new PdfDocument();
        Paint myPaint = new Paint();

        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(500,150,1).create();
        PdfDocument.Page myPage =  pdfDocument.startPage(myPageInfo);
        Canvas canvas = myPage.getCanvas();
        canvas.drawBitmap(bmp,100,20, myPaint);
        String myString = "Nama Eksplan: " + namaEksplan + "\n\nJenis Tanaman: " + jenisTanaman+ "\n\nNama Admin: " + theUser;
        int x = 230, y = 60;
        myPaint.setTextSize(10);
        //canvas.drawText(myString, x, y, myPaint);
        for (String line:myString.split("\n")){
            myPage.getCanvas().drawText(line, x, y, myPaint);
           y+=myPaint.descent()-myPaint.ascent();
        }
        pdfDocument.finishPage(myPage);
        String myFilePath = Environment.getExternalStorageDirectory().getPath() + "/"+docName+".pdf";
        File myFile = new File(myFilePath);
        try {
            pdfDocument.writeTo(new FileOutputStream(myFile));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Uri imageUri = FileProvider.getUriForFile(
                insertQREksplanActivity.this,
                BuildConfig.APPLICATION_ID +".provider", //(use your app signature + ".provider" )
                myFile);
        //Uri uri = Uri.fromFile(myFile);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("application/pdf");
        shareIntent.putExtra(Intent.EXTRA_EMAIL, theUser);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "QR Code " +    namaEksplan);
        //shareIntent.putExtra(Intent.EXTRA_TEXT, "test");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        //shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(shareIntent);

        pdfDocument.close();

    }
    public static int getApproxXToCenterText(String text, int fontSize) {
        Paint p = new Paint();
        p.setTextSize(fontSize);
        float textWidth = p.measureText(text);
        int xOffset = (int)(fontSize/2f);
        return xOffset;
    }
}
