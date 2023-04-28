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
import android.widget.DatePicker;
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

public class insertDataEksplanActivity extends AppCompatActivity {
    private EditText namaEksplanTxt, jenisTanamanTxt, tempatPenyimpananTxt, namaPelangganTxt, mediaPenyimpananText,
            tanggalTerimaTxt, statusTxt,keteranganTxt, tanggalTanamTxt;
    private EditText noTelpPelangganTxt;
    private Button datePickerBtn, submitBtn, insertGambarBtn, datePickerTanamBtn;
    private ImageView imageGambar;
    public Spinner statusSpinner;
    DatePickerDialog datePickerDialog;
    DatePickerDialog datePickerTanamDialog;
    int year, month, dayOfMonth;
    Calendar calendar;
    DatabaseReference databaseReference, databaseReferenceAdmin;
    public StorageReference mStorageRef;
    private static final int SELECT_PICTURE = 100;
    private FirebaseAuth mAuth;
    dataEksplan dataEksplan = new dataEksplan();
    public String namaAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data_eksplan);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        insertGambarBtn = findViewById(R.id.btnInsertGambarEksplan);
        imageGambar = findViewById(R.id.imageViewInsertEksplan);
        namaEksplanTxt = findViewById(R.id.namaEksplanText);
        jenisTanamanTxt = findViewById(R.id.jenisTanamanInsertText);
        tempatPenyimpananTxt = findViewById(R.id.tempatPenyimpananInsertText);
        namaPelangganTxt = findViewById(R.id.namaPelangganInsertText);
        noTelpPelangganTxt = findViewById(R.id.noTelpPelangganInsertText);
        mediaPenyimpananText = findViewById(R.id.mediaTanamInsertText);
        tanggalTerimaTxt = findViewById(R.id.tanggalTerimaInsertText);
        tanggalTanamTxt = findViewById(R.id.tanggalTanamInsertText);
        statusSpinner = findViewById(R.id.statusSpinner);
        final String[] daftarStatus = {"Isolasi","Sterilisasi","Inisiasi","Inkubasi","Multiplikasi","Aklimatisasi", "Kontaminasi"};
        statusTxt = findViewById(R.id.statusInsertText);
        datePickerBtn = findViewById(R.id.datePickerBtn);
        datePickerTanamBtn = findViewById(R.id.datePickerTanamBtn);
        submitBtn = findViewById(R.id.insertBtn);
        keteranganTxt = findViewById(R.id.keteranganInsertText);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser theUser = mAuth.getCurrentUser();
        final String _UID = theUser.getUid().toString();


        mStorageRef = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReferenceAdmin = FirebaseDatabase.getInstance().getReference();

        insertGambarBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
        });

        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(insertDataEksplanActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                tanggalTerimaTxt.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        datePickerTanamBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(insertDataEksplanActivity.this,
                        (datePicker, year, month, day) ->
                                tanggalTanamTxt.setText(day + "/" + (month + 1) + "/" + year), year, month, dayOfMonth);
                //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                finish();
            }
        });
        final ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(insertDataEksplanActivity.this, android.R.layout.simple_spinner_dropdown_item, daftarStatus);
        areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(areasAdapter);
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    imageGambar.setImageURI(selectedImageUri);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void saveData(){
        final FirebaseUser theUser = mAuth.getCurrentUser();
        final String _UID = theUser.getUid().toString();
        final String namaEksplan = namaEksplanTxt.getText().toString().trim();
        final String jenisTanaman = jenisTanamanTxt.getText().toString().trim();
        final String tempatPenyimpanan = tempatPenyimpananTxt.getText().toString().trim();
        final String namaPelanggan = namaPelangganTxt.getText().toString().trim();
        final String noTelpPelanggan = noTelpPelangganTxt.getText().toString().trim();
        final String tanggalTerima = tanggalTerimaTxt.getText().toString().trim();
        final String mediaPenyimpanan = mediaPenyimpananText.getText().toString().trim();
        final String status = statusTxt.getText().toString().trim();
        final String keterangan = keteranganTxt.getText().toString().trim();
        final String tanggalTanam;
        if(status.equals("Isolasi")||status.equals("Sterilisasi")||status.equals("Inisiasi"))
        {
            tanggalTanam = "";
        }
        else {
            tanggalTanam = tanggalTanamTxt.getText().toString().trim();
        }
        final StorageReference imageRef = mStorageRef.child("EksplanImages");

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
        if (TextUtils.isEmpty(namaPelanggan)) {
            isEmptyFields = true;
            namaPelangganTxt.setError("Field ini tidak boleh kosong");
        }

        if (TextUtils.isEmpty(tanggalTerima)) {
            isEmptyFields = true;
            tanggalTerimaTxt.setError("Field ini tidak boleh kosong");
        }
        if (TextUtils.isEmpty(tanggalTerima)) {
            isEmptyFields = true;
            tanggalTanamTxt.setError("Field ini tidak boleh kosong");
        }
        if (TextUtils.isEmpty(status)) {
            isEmptyFields = true;
            statusTxt.setError("Field ini tidak boleh kosong");
        }

        if (TextUtils.isEmpty(keterangan)) {
            isEmptyFields = true;
            keteranganTxt.setError("Field ini tidak boleh kosong");
        }
        if (! isEmptyFields) {
            final DatabaseReference dbEksplan = databaseReference.child("List Eksplan");
            final DatabaseReference dbAdmin = databaseReferenceAdmin.child("Admin");
            imageGambar.setDrawingCacheEnabled(true);
            imageGambar.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imageGambar.getDrawable()).getBitmap();
            final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,120,120, false);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] dataImg = baos.toByteArray();
            final UploadTask uploadTask = imageRef.child(namaEksplan).child(namaEksplan+".jpg").putBytes(dataImg);

            dbAdmin.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        namaAdmin = dataSnapshot.child(_UID).child("namaAdmin").getValue().toString();
                        String id = dbEksplan.push().getKey();

                        dataEksplan.setNamaAdmin(namaAdmin);
                        dataEksplan.setNamaEksplan(namaEksplan);
                        dataEksplan.setJenisTanaman(jenisTanaman);
                        dataEksplan.setNamaPelanggan(namaPelanggan);
                        dataEksplan.setNoTelpPelanggan(noTelpPelanggan);
                        dataEksplan.setTanggalTerima(tanggalTerima);
                        dataEksplan.setTanggalTanam(tanggalTanam);
                        dataEksplan.setMediaPenyimpanan(mediaPenyimpanan);
                        dataEksplan.setStatus(status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase());
                        dataEksplan.setKeterangan(keterangan);
                        dataEksplan.setTempatPenyimpanan(tempatPenyimpanan);
                        dbEksplan.child(id).setValue(dataEksplan);
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
                                        dbEksplan.child(id).child("linkImages").setValue(imageQRRef);
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

            Toast.makeText(insertDataEksplanActivity.this,"Data Berhasil Dimasukkan",Toast.LENGTH_LONG).show();
        }

    }
}
