package com.example.kuljarhanip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
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
import java.util.Calendar;

public class readDataAnakan extends AppCompatActivity {
    private EditText namaEksplanTxt, jenisTanamanTxt, tempatPenyimpananTxt, tanggalTanamTxt, statusTxt,
            mediaPenyimpananText, namaAdminTxt;
    private Button  updateBtn, deleteBtn, insertQRAnakanBtn, changeGambarBtn, datePickerBtn;
    private ImageView imgGambarAnakan;
    private Spinner statusSpinner;
    private Switch switchanakan;
    public DatabaseReference databaseReference, eksplanAnakanRef, databaseReferenceAdmin;
    private String idEksplan, idEksplanAnakan,namaEksplan, jenisTanaman,
            mediaTanam , tempatPenyimpanan, tanggalTanam, status, namaAdminAnakan;
    dataEksplan dataEksplan = new dataEksplan();
    DatePickerDialog datePickerDialog;
    public FirebaseAuth mAuth;
    public StorageReference mStorageRef, imgRef;
    int year, month, dayOfMonth;
    Calendar calendar;
    private static final int SELECT_PICTURE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_data_anakan);
        changeGambarBtn = findViewById(R.id.btnInsertChangeGambarEksplanAnakan);
        imgGambarAnakan = findViewById(R.id.imageViewReadEksplanAnakan);

        namaEksplanTxt = findViewById(R.id.namaEksplanAnakanReadText);
        jenisTanamanTxt = findViewById(R.id.jenisTanamanAnakanReadText);
        tempatPenyimpananTxt = findViewById(R.id.tempatPenyimpananAnakanReadText);
        tanggalTanamTxt = findViewById(R.id.tanggalTanamReadAnakanText);
        datePickerBtn = findViewById(R.id.datePickerTanamReadAnakanBtn);
        mediaPenyimpananText = findViewById(R.id.mediaTanamAnakanReadText);
        namaAdminTxt = findViewById(R.id.namaAdminReadAnakanText);
        statusTxt = findViewById(R.id.statusAnakanReadText);
        mAuth = FirebaseAuth.getInstance();
        switchanakan = findViewById(R.id.switch1Anakan);
        deleteBtn = findViewById(R.id.buttonDeleteAnakan);
        insertQRAnakanBtn = findViewById(R.id.buttonTambahQRAnakan);
        statusSpinner = findViewById(R.id.statusAnakanReadSpinner);
        final String[] daftarStatus = {"Isolasi","Sterilisasi","Inisiasi","Inkubasi","Multiplikasi","Aklimatisasi", "Kontaminasi", "Terjual"};

        final FirebaseUser theUser = mAuth.getCurrentUser();
        final String _UID = theUser.getUid().toString();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReferenceAdmin = FirebaseDatabase.getInstance().getReference().child("Admin").child(_UID);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        namaEksplanTxt.setEnabled(false);
        namaEksplanTxt.setInputType(InputType.TYPE_NULL);
        namaEksplanTxt.setFocusable(false);

        jenisTanamanTxt.setEnabled(false);
        jenisTanamanTxt.setInputType(InputType.TYPE_NULL);
        jenisTanamanTxt.setFocusable(false);

        tempatPenyimpananTxt.setEnabled(false);
        tempatPenyimpananTxt.setInputType(InputType.TYPE_NULL);
        tempatPenyimpananTxt.setFocusable(false);

        tanggalTanamTxt.setEnabled(false);
        tanggalTanamTxt.setInputType(InputType.TYPE_NULL);
        tanggalTanamTxt.setFocusable(false);

        namaAdminTxt.setEnabled(false);
        namaAdminTxt.setInputType(InputType.TYPE_NULL);
        namaAdminTxt.setFocusable(false);

        statusTxt.setEnabled(false);
        statusTxt.setInputType(InputType.TYPE_NULL);
        statusTxt.setFocusable(false);

        mediaPenyimpananText.setEnabled(false);
        mediaPenyimpananText.setInputType(InputType.TYPE_NULL);
        mediaPenyimpananText.setFocusable(false);

        idEksplan = getIntent().getExtras().get("idEksplan").toString();
        idEksplanAnakan = getIntent().getExtras().get("idEksplanAnakan").toString();
        namaEksplan = getIntent().getExtras().get("namaEksplanAnakan").toString();
        jenisTanaman = getIntent().getExtras().get("jenisTanamanAnakan").toString();
        tempatPenyimpanan = getIntent().getExtras().get("tempatPenyimpananAnakan").toString();
        mediaTanam = getIntent().getExtras().get("mediaTanam").toString();
        status = getIntent().getExtras().get("statusAnakan").toString();
        tanggalTanam = getIntent().getExtras().get("tanggalTanam").toString();
        namaAdminAnakan = getIntent().getExtras().get("namaAdminAnakan").toString();

        namaEksplanTxt.setText(namaEksplan);
        jenisTanamanTxt.setText(jenisTanaman);
        tempatPenyimpananTxt.setText(tempatPenyimpanan);
        statusTxt.setText(status);
        namaAdminTxt.setText(namaAdminAnakan);
        tanggalTanamTxt.setText(tanggalTanam);
        mediaPenyimpananText.setText(mediaTanam);
        updateBtn = findViewById(R.id.updateAnakanBtn);
        updateBtn.setVisibility(View.INVISIBLE);
        imgRef = FirebaseStorage.getInstance().getReference().child("EksplanImages").child(namaEksplan).child(namaEksplan+".jpg");
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(readDataAnakan.this).load(uri).into(imgGambarAnakan);
            }
        });

        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(readDataAnakan.this,
                        (datePicker, year, month, day) ->
                                tanggalTanamTxt.setText(day + "/" + (month + 1) + "/" + year), year, month, dayOfMonth);
                //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        final ArrayAdapter<String> areasAdapter2 = new ArrayAdapter<String>(readDataAnakan.this, android.R.layout.simple_spinner_dropdown_item, daftarStatus);
        areasAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(areasAdapter2);
        for(int i= 0; i < statusSpinner.getAdapter().getCount(); i++)
        {
            if(statusSpinner.getAdapter().getItem(i).toString().contains(status))
            {
                statusSpinner.setSelection(i);
            }
        }
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusTxt.setText(daftarStatus[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        changeGambarBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDataEksplanAnakan();
                Intent i = new Intent(readDataAnakan.this,mainMenuActivity.class);
                startActivity(i);
            }
        });
        if(statusTxt.getText().toString().equals("Kontaminasi"))
        {
            //deleteBtn.show();
            deleteBtn.setVisibility(View.VISIBLE);

            //switch1.setVisibility(View.INVISIBLE);
        }
        else
        {
            //deleteBtn.hide();
            deleteBtn.setVisibility(View.INVISIBLE);

        }
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(readDataAnakan.this);
                alertDialogBuilder.setTitle("Hapus Data Eksplan");
                alertDialogBuilder.setMessage("Apakah anda yakin untuk menghapus data ini?").setCancelable(false).
                        setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference dbEksplan = databaseReference.child("List Eksplan").child(idEksplan).child("Anakan").child(idEksplanAnakan);
                                dbEksplan.removeValue();
                                Toast.makeText(readDataAnakan.this,"Data "+namaEksplan+" Berhasi Dihapus",Toast.LENGTH_LONG).show();
                                Intent i = new Intent(readDataAnakan.this,mainMenuActivity.class);
                                startActivity(i);
                            }
                        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
       insertQRAnakanBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(readDataAnakan.this,insertQREksplanActivity.class);
               i.putExtra("idEksplan", idEksplan + " " + idEksplanAnakan);
               i.putExtra("namaEksplan", namaEksplan);
               i.putExtra("jenisTanaman", jenisTanaman);
               startActivity(i);
           }
       });

        switchanakan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    databaseReferenceAdmin.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String _UIDnama = dataSnapshot.child("namaAdmin").getValue().toString();
                            if(!namaAdminAnakan.equals(_UIDnama))
                            {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(readDataAnakan.this);
                                alertDialogBuilder.setTitle("Warning!");
                                alertDialogBuilder.setMessage("Maaf Anda Tidak Dapat Mengupdate Data Ini. \n Silahkan Hubungi Admin yang Bersangkutan")
                                        .setCancelable(false).setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                                switchanakan.setChecked(false);
                            }
                            else{
                                namaEksplanTxt.setEnabled(true);
                                namaEksplanTxt.setInputType(InputType.TYPE_CLASS_TEXT);
                                namaEksplanTxt.setFocusable(true);
                                namaEksplanTxt.setFocusableInTouchMode(true);

                                jenisTanamanTxt.setEnabled(true);
                                jenisTanamanTxt.setInputType(InputType.TYPE_CLASS_TEXT);
                                jenisTanamanTxt.setFocusable(true);
                                jenisTanamanTxt.setFocusableInTouchMode(true);

                                tempatPenyimpananTxt.setEnabled(true);
                                tempatPenyimpananTxt.setInputType(InputType.TYPE_CLASS_TEXT);
                                tempatPenyimpananTxt.setFocusable(true);
                                tempatPenyimpananTxt.setFocusableInTouchMode(true);

                                tanggalTanamTxt.setEnabled(true);
                                tanggalTanamTxt.setInputType(InputType.TYPE_CLASS_TEXT);
                                tanggalTanamTxt.setFocusable(true);
                                tanggalTanamTxt.setFocusableInTouchMode(true);

                                statusTxt.setEnabled(true);
                                statusTxt.setInputType(InputType.TYPE_CLASS_TEXT);
                                statusTxt.setFocusable(true);
                                statusTxt.setFocusableInTouchMode(true);

                                mediaPenyimpananText.setEnabled(true);
                                mediaPenyimpananText.setInputType(InputType.TYPE_CLASS_TEXT);
                                mediaPenyimpananText.setFocusable(true);
                                mediaPenyimpananText.setFocusableInTouchMode(true);

                                updateBtn.setVisibility(View.VISIBLE);
                                datePickerBtn.setVisibility(View.VISIBLE);
                                changeGambarBtn.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                if(!isChecked){
                    namaEksplanTxt.setEnabled(false);
                    namaEksplanTxt.setInputType(InputType.TYPE_NULL);
                    namaEksplanTxt.setFocusable(false);

                    jenisTanamanTxt.setEnabled(false);
                    jenisTanamanTxt.setInputType(InputType.TYPE_NULL);
                    jenisTanamanTxt.setFocusable(false);

                    tempatPenyimpananTxt.setEnabled(false);
                    tempatPenyimpananTxt.setInputType(InputType.TYPE_NULL);
                    tempatPenyimpananTxt.setFocusable(false);

                    tanggalTanamTxt.setEnabled(false);
                    tanggalTanamTxt.setInputType(InputType.TYPE_NULL);
                    tanggalTanamTxt.setFocusable(false);

                    statusTxt.setEnabled(false);
                    statusTxt.setInputType(InputType.TYPE_NULL);
                    statusTxt.setFocusable(false);

                    mediaPenyimpananText.setEnabled(false);
                    mediaPenyimpananText.setInputType(InputType.TYPE_NULL);
                    mediaPenyimpananText.setFocusable(false);

                    updateBtn.setVisibility(View.INVISIBLE);
                    datePickerBtn.setVisibility(View.INVISIBLE);
                    changeGambarBtn.setVisibility(View.INVISIBLE);
                }
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
    public void updateDataEksplanAnakan() {
        String namaEksplan = namaEksplanTxt.getText().toString().trim();
        String jenisTanaman = jenisTanamanTxt.getText().toString().trim();
        String tempatPenyimpanan = tempatPenyimpananTxt.getText().toString().trim();
        String mediaTanam = mediaPenyimpananText.getText().toString().trim();
        final String tanggalTanam;
        String namaAdmin = namaAdminTxt.getText().toString().trim();
        String status = statusTxt.getText().toString().trim();
        final StorageReference imageRef = mStorageRef.child("EksplanImages");
        imgGambarAnakan.setDrawingCacheEnabled(true);
        imgGambarAnakan.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imgGambarAnakan.getDrawable()).getBitmap();
        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,120,120, false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] dataImg = baos.toByteArray();
        final UploadTask uploadTask = imageRef.child(namaEksplan).child(namaEksplan+".jpg").putBytes(dataImg);

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

        if (!isEmptyFields) {
        DatabaseReference dbEksplan = databaseReference.child("List Eksplan");
        DatabaseReference dbJual = databaseReference.child("List Jual");
        Toast.makeText(readDataAnakan.this,"Data Anakan "+namaEksplan+" Berhasil Diupdate",Toast.LENGTH_LONG).show();
        String id = dbEksplan.push().getKey();
        //dataEksplan.setNamaEksplan(namaEksplan);
        //dataEksplan.setJenisTanaman(jenisTanaman);
       // dataEksplan.setStatus(status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase());
        //dataEksplan.setTempatPenyimpanan(tempatPenyimpanan);
        dbEksplan.child(idEksplan).child("Anakan").child(idEksplanAnakan).child("namaEksplan").setValue(namaEksplan);
        dbEksplan.child(idEksplan).child("Anakan").child(idEksplanAnakan).child("jenisTanaman").setValue(jenisTanaman);
        dbEksplan.child(idEksplan).child("Anakan").child(idEksplanAnakan).child("status").setValue(status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase());
        dbEksplan.child(idEksplan).child("Anakan").child(idEksplanAnakan).child("tempatPenyimpanan").setValue(tempatPenyimpanan);
        dbEksplan.child(idEksplan).child("Anakan").child(idEksplanAnakan).child("tanggalTanam").setValue(tanggalTanam);
        dbEksplan.child(idEksplan).child("Anakan").child(idEksplanAnakan).child("mediaPenyimpanan").setValue(mediaTanam);
        dbEksplan.child(idEksplan).child("Anakan").child(idEksplanAnakan).child("namaAdmin").setValue(namaAdmin);
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
                            dbEksplan.child(idEksplan).child("Anakan").child(idEksplanAnakan).child("linkImages").setValue(imageQRRef);
                            //finish();
                        }
                    }));
                }
            });
        if (status.equals("Terjual")) {
            dbJual.child(idEksplanAnakan).child("namaEksplan").setValue(namaEksplan);
            dbJual.child(idEksplanAnakan).child("jenisTanaman").setValue(jenisTanaman);
            dbJual.child(idEksplanAnakan).child("status").setValue(status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase());
            dbJual.child(idEksplanAnakan).child("tempatPenyimpanan").setValue(tempatPenyimpanan);
            dbJual.child(idEksplanAnakan).child("namaAdmin").setValue(namaAdmin);
            dbJual.child(idEksplanAnakan).child("tanggalTanam").setValue(namaAdmin);
            dbJual.child(idEksplanAnakan).child("mediaPenyimpanan").setValue(mediaTanam);
            dbJual.child(idEksplanAnakan).child("idEksplanUntukAnakan").setValue(idEksplan);
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
                            dbJual.child(idEksplanAnakan).child("linkImages").setValue(imageQRRef);
                            //finish();
                        }
                    }));
                }
            });
        }
    }
}

}
