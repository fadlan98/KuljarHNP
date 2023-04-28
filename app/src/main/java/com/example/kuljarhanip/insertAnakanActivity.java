package com.example.kuljarhanip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.util.Calendar;

public class insertAnakanActivity extends AppCompatActivity {

    public String idEksplan;
    private EditText namaEksplanTxt, jenisTanamanTxt, tempatPenyimpananTxt, tanggalTanamTxt, statusTxt, mediaTanamText;
    public Spinner statusAnakanSpinner;
    private Button submitBtn, insertGambarAnakanBtn, datePickerTanamBtn;
    DatePickerDialog datePickerDialog;

    public ImageView imgGambarAnakan;
    DatabaseReference databaseReference;
    dataEksplan dataEksplan = new dataEksplan();
    private FirebaseAuth mAuth;
    public StorageReference mStorageRef;
    private static final int SELECT_PICTURE = 100;
    int year, month, dayOfMonth;
    Calendar calendar;
    public String namaAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_anakan);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        idEksplan = getIntent().getExtras().get("idEksplan").toString();
        final String[] daftarStatus = {"Isolasi","Sterilisasi","Inisiasi","Inkubasi","Multiplikasi","Aklimatisasi", "Kontaminasi", "Terjual"};
        namaEksplanTxt = findViewById(R.id.namaEksplanAnakanText);
        jenisTanamanTxt = findViewById(R.id.jenisTanamanAnakanInsertText);
        tempatPenyimpananTxt = findViewById(R.id.tempatPenyimpananAnakanInsertText);
        tanggalTanamTxt = findViewById(R.id.tanggalTanamInsertAnakanText);
        statusTxt = findViewById(R.id.statusAnakanInsertText);
        statusAnakanSpinner = findViewById(R.id.statusAnakanSpinner);
        submitBtn = findViewById(R.id.insertAnakanBtn);
        insertGambarAnakanBtn = findViewById(R.id.btnInsertGambarEksplanAnakan);
        imgGambarAnakan = findViewById(R.id.imageViewInsertEksplanAnakan);
        mediaTanamText = findViewById(R.id.mediaTanamAnakanInsertText);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //final FirebaseUser theUser = mAuth.getCurrentUser();
        //final String _UID = theUser.getUid().toString();
        datePickerTanamBtn = findViewById(R.id.datePickerTanamAnakanBtn);
        mAuth = FirebaseAuth.getInstance();

        mStorageRef = FirebaseStorage.getInstance().getReference();
        insertGambarAnakanBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataAnakan(idEksplan);
                Intent i = new Intent(insertAnakanActivity.this,mainMenuActivity.class);
                startActivity(i);

            }
        });

        datePickerTanamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(insertAnakanActivity.this,
                        (datePicker, year, month, day) ->
                                tanggalTanamTxt.setText(day + "/" + (month + 1) + "/" + year), year, month, dayOfMonth);
                //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        final ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(insertAnakanActivity.this, android.R.layout.simple_spinner_dropdown_item, daftarStatus);
        areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusAnakanSpinner.setAdapter(areasAdapter);
        statusAnakanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusTxt.setText(daftarStatus[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
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
                    imgGambarAnakan.setImageURI(selectedImageUri);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void saveDataAnakan(String idEksplan) {
        final String namaEksplan = namaEksplanTxt.getText().toString().trim();
        final String jenisTanaman = jenisTanamanTxt.getText().toString().trim();
        final String tempatPenyimpanan = tempatPenyimpananTxt.getText().toString().trim();
        final String tanggalTanam;
        final String status = statusTxt.getText().toString().trim();
        final String mediaPenyimpanan = mediaTanamText.getText().toString().trim();
        final String idEksplanUntukAnakan = idEksplan;
        final FirebaseUser theUser = mAuth.getCurrentUser();
        final String _UID = theUser.getUid().toString();
        final StorageReference imageRef = mStorageRef.child("EksplanImages");
        if(status.equals("Isolasi")||status.equals("Sterilisasi")||status.equals("Inisiasi"))
        {
            tanggalTanam = "";
        }
        else {
            tanggalTanam = tanggalTanamTxt.getText().toString().trim();
        }


        boolean isEmptyFields = false;

        if (TextUtils.isEmpty(namaEksplan)) {
            isEmptyFields = true;
            namaEksplanTxt.setError("Field ini tidak boleh kosong");
        }
        if (TextUtils.isEmpty(jenisTanaman)) {
            isEmptyFields = true;
            jenisTanamanTxt.setError("Field ini tidak boleh kosong");
        }
        if (TextUtils.isEmpty(tempatPenyimpanan)) {
            isEmptyFields = true;
            tempatPenyimpananTxt.setError("Field ini tidak boleh kosong");
        }
        if (TextUtils.isEmpty(tempatPenyimpanan)) {
            isEmptyFields = true;
            tanggalTanamTxt.setError("Field ini tidak boleh kosong");
        }

        if (TextUtils.isEmpty(status)) {
            isEmptyFields = true;
            statusTxt.setError("Field ini tidak boleh kosong");
        }
        if (TextUtils.isEmpty(status)) {
            isEmptyFields = true;
            mediaTanamText.setError("Field ini tidak boleh kosong");
        }

        if (!isEmptyFields) {
            final DatabaseReference dbEksplan = databaseReference.child("List Eksplan");
            final DatabaseReference dbJual = databaseReference.child("List Jual");
            final DatabaseReference dbAdmin = databaseReference.child("Admin");
            imgGambarAnakan.setDrawingCacheEnabled(true);
            imgGambarAnakan.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imgGambarAnakan.getDrawable()).getBitmap();
            final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,120,120, false);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] dataImg = baos.toByteArray();
            final UploadTask uploadTask = imageRef.child(namaEksplan).child(namaEksplan+".jpg").putBytes(dataImg);
            dbAdmin.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    namaAdmin = dataSnapshot.child(_UID).child("namaAdmin").getValue().toString();
                    dataEksplan.setNamaAdmin(namaAdmin);
                    String id = dbEksplan.push().getKey();
                    dataEksplan.setNamaEksplan(namaEksplan);
                    dataEksplan.setJenisTanaman(jenisTanaman);
                    dataEksplan.setTanggalTanam(tanggalTanam);
                    dataEksplan.setStatus(status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase());
                    dataEksplan.setTempatPenyimpanan(tempatPenyimpanan);
                    dataEksplan.setIdEksplanUntukAnakan(idEksplanUntukAnakan);
                    dataEksplan.setMediaPenyimpanan(mediaPenyimpanan);
                    dbEksplan.child(idEksplan).child("Anakan").child(id).setValue(dataEksplan);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            StorageMetadata snapshotMetadata = taskSnapshot.getMetadata();
                            Task<Uri> downloadUrl = imageRef.child(namaEksplan).child(namaEksplan+".jpg").getDownloadUrl();
                            downloadUrl.addOnSuccessListener((new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageQRRef = uri.toString();
//                                        dataEksplan.setLinkQR(imageQRRef);
                                    //Toast.makeText(insertDataEksplanActivity.this,"QR Berhasil Dimasukkan",Toast.LENGTH_LONG).show();
                                    dbEksplan.child(idEksplan).child("Anakan").child(id).child("linkImages").setValue(imageQRRef);
                                    //finish();
                                }
                            }));
                        }
                    });
                    if(status.equals("Terjual"))
                    {
                        String idJual = dbJual.push().getKey();
                        dbJual.child(idJual).setValue(dataEksplan);
                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                StorageMetadata snapshotMetadata = taskSnapshot.getMetadata();
                                Task<Uri> downloadUrl = imageRef.child(namaEksplan).child(namaEksplan+".jpg").getDownloadUrl();
                                downloadUrl.addOnSuccessListener((new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageQRRef = uri.toString();
//                                        dataEksplan.setLinkQR(imageQRRef);
                                        //Toast.makeText(insertDataEksplanActivity.this,"QR Berhasil Dimasukkan",Toast.LENGTH_LONG).show();
                                        dbJual.push().child("linkImages").setValue(imageQRRef);
                                        //finish();
                                    }
                                }));
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            Toast.makeText(insertAnakanActivity.this, "Data Anakan Berhasil Dimasukkan", Toast.LENGTH_LONG).show();

        }
    }
}
