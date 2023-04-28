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
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class readDataEksplan extends AppCompatActivity {

    private EditText namaEksplanTxt, jenisTanamanTxt, namaAdminTxt, tempatPenyimpananTxt, namaPelangganTxt,
            tanggalTerimaTxt, tanggalTanamTxt, statusTxt,keteranganTxt, mediaPenyimpananTxt;
    private EditText noTelpPelangganTxt;
    public Button datePickerBtn, updateBtn, deleteBtn, insertQRBtn, changeGambarBtn, datePickerTanamBtn;
    DatePickerDialog datePickerDialog;
    int year, month, dayOfMonth;
    Calendar calendar;
    public FloatingActionButton fab ;
    private Spinner spinner1, statusSpinner;
    private Switch switch1;
    public DatabaseReference databaseReference, databaseReferenceAdmin, eksplanAnakanRef;
    dataEksplan dataEksplan = new dataEksplan();
    final List<String> listAnakan = new ArrayList<>();
    List<String> listIDAnakan = new ArrayList<>();
    List<String> listjenisTanamanAnakan = new ArrayList<>();
    List<String> listnamaAdminAnakan = new ArrayList<>();
    List<String> liststatusAnakan = new ArrayList<>();
    List<String> listtempatPenyimpananAnakan = new ArrayList<>();
    List<String> listTanggalTanam = new ArrayList<>();
    List<String> listMediaTanam = new ArrayList<>();
    public String idEksplanAnakan;
    public FirebaseAuth mAuth;
    public ImageView imgGambar;
    public StorageReference mStorageRef, imgRef;
    private static final int SELECT_PICTURE = 100;


    private String idEksplan,namaEksplan, jenisTanaman, namaAdmin,  tempatPenyimpanan, namaPelanggan,
            noTelpPelanggan, tanggalTerima, tanggalTanam, status,keterangan, mediaPenyimpanan;
    //public String updateStatus;
    public String namaEksplanAnakan,jenisTanamanAnakan,statusAnakan,tempatPenyimpananAnakan, namaAdminAnakan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_data_eksplan);
        mAuth = FirebaseAuth.getInstance();
        //updateStatus = "Update Success";
        imgGambar = findViewById(R.id.imageViewReadEksplan);
        changeGambarBtn = findViewById(R.id.btnChangeGambarEksplan);
        namaEksplanTxt = findViewById(R.id.namaEksplanReadText);
        jenisTanamanTxt = findViewById(R.id.jenisTanamanReadText);
        namaAdminTxt = findViewById(R.id.emailAdminTxt);
        tempatPenyimpananTxt = findViewById(R.id.tempatPenyimpananReadText);
        namaPelangganTxt = findViewById(R.id.namaPelangganReadText);
        mediaPenyimpananTxt = findViewById(R.id.mediaTanamReadText);
        noTelpPelangganTxt = findViewById(R.id.noTelpPelangganReadText);
        tanggalTerimaTxt = findViewById(R.id.tanggalTerimaReadText);
        tanggalTanamTxt = findViewById(R.id.tanggalTanamReadText);
        statusSpinner = findViewById(R.id.statusReadSpinner);
        statusTxt = findViewById(R.id.statusReadText);
        datePickerBtn = findViewById(R.id.datePickerBtn);
        datePickerTanamBtn = findViewById(R.id.datePickerTanamReadBtn);
        updateBtn = findViewById(R.id.updateBtn);
        keteranganTxt = findViewById(R.id.keteranganReadText);
        fab = findViewById(R.id.tambahAnakanBtn);
        spinner1 = findViewById(R.id.spinner);
        switch1 = findViewById(R.id.switch1);
        insertQRBtn = findViewById(R.id.buttonTambahQR);
        deleteBtn = findViewById(R.id.buttonDelete);
        final String[] daftarStatus = {"Isolasi","Sterilisasi","Inisiasi","Inkubasi","Multiplikasi","Aklimatisasi", "Kontaminasi"};

        final FirebaseUser theUser = mAuth.getCurrentUser();
        final String _UID = theUser.getUid().toString();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        databaseReferenceAdmin = FirebaseDatabase.getInstance().getReference().child("Admin").child(_UID);


        idEksplan = getIntent().getExtras().get("idEksplan").toString();
        namaEksplan = getIntent().getExtras().get("namaEksplan").toString();
        jenisTanaman = getIntent().getExtras().get("jenisTanaman").toString();
        namaAdmin = getIntent().getExtras().get("namaAdmin").toString();
        tempatPenyimpanan = getIntent().getExtras().get("tempatPenyimpanan").toString();
        namaPelanggan = getIntent().getExtras().get("namaPelanggan").toString();
        noTelpPelanggan = getIntent().getExtras().get("noTelpPelanggan").toString();
        tanggalTerima = getIntent().getExtras().get("tanggalTerima").toString();
        tanggalTanam = getIntent().getExtras().get("tanggalTanam").toString();
        status = getIntent().getExtras().get("status").toString();
        keterangan = getIntent().getExtras().get("keterangan").toString();
        mediaPenyimpanan = getIntent().getExtras().get("mediaTanam").toString();

        namaEksplanTxt.setText(namaEksplan);
        jenisTanamanTxt.setText(jenisTanaman);
        tempatPenyimpananTxt.setText(tempatPenyimpanan);
        namaAdminTxt.setText(namaAdmin);
        namaPelangganTxt.setText(namaPelanggan);
        noTelpPelangganTxt.setText(noTelpPelanggan);
        tanggalTerimaTxt.setText(tanggalTerima);
        tanggalTanamTxt.setText(tanggalTanam);
        mediaPenyimpananTxt.setText(mediaPenyimpanan);
        final ArrayAdapter<String> areasAdapter2 = new ArrayAdapter<String>(readDataEksplan.this, android.R.layout.simple_spinner_dropdown_item, daftarStatus);
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
        statusTxt.setText(status);
        keteranganTxt.setText(keterangan);
        //deleteBtn.hide();
        deleteBtn.setVisibility(View.INVISIBLE);
        datePickerTanamBtn.setVisibility(View.INVISIBLE);

        namaEksplanTxt.setEnabled(false);
        namaEksplanTxt.setInputType(InputType.TYPE_NULL);
        namaEksplanTxt.setFocusable(false);

        jenisTanamanTxt.setEnabled(false);
        jenisTanamanTxt.setInputType(InputType.TYPE_NULL);
        jenisTanamanTxt.setFocusable(false);

        namaAdminTxt.setEnabled(false);
        namaAdminTxt.setInputType(InputType.TYPE_NULL);
        namaAdminTxt.setFocusable(false);

        tempatPenyimpananTxt.setEnabled(false);
        tempatPenyimpananTxt.setInputType(InputType.TYPE_NULL);
        tempatPenyimpananTxt.setFocusable(false);

        namaPelangganTxt.setEnabled(false);
        namaPelangganTxt.setInputType(InputType.TYPE_NULL);
        namaPelangganTxt.setFocusable(false);

        noTelpPelangganTxt.setEnabled(false);
        noTelpPelangganTxt.setInputType(InputType.TYPE_NULL);
        noTelpPelangganTxt.setFocusable(false);

        tanggalTerimaTxt.setEnabled(false);
        tanggalTerimaTxt.setInputType(InputType.TYPE_NULL);
        tanggalTerimaTxt.setFocusable(false);

        tanggalTanamTxt.setEnabled(false);
        tanggalTanamTxt.setInputType(InputType.TYPE_NULL);
        tanggalTanamTxt.setFocusable(false);

        mediaPenyimpananTxt.setEnabled(false);
        mediaPenyimpananTxt.setInputType(InputType.TYPE_NULL);
        mediaPenyimpananTxt.setFocusable(false);

        statusTxt.setEnabled(false);
        statusTxt.setInputType(InputType.TYPE_NULL);
        statusTxt.setFocusable(false);

        keteranganTxt.setEnabled(false);
        keteranganTxt.setInputType(InputType.TYPE_NULL);
        keteranganTxt.setFocusable(false);


        databaseReference = FirebaseDatabase.getInstance().getReference();
        eksplanAnakanRef = FirebaseDatabase.getInstance().getReference().child("List Eksplan").child(idEksplan).child("Anakan");
        //idEksplanAnakan = eksplanAnakanRef.push().getKey();

        //String wew = eksplanAnakanRef.child("List Eksplan").child(idEksplan).getKey();
        //.makeText(readDataEksplan.this,wew,Toast.LENGTH_SHORT);

        imgRef = FirebaseStorage.getInstance().getReference().child("EksplanImages").child(namaEksplan).child(namaEksplan+".jpg");
        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(readDataEksplan.this).load(uri).into(imgGambar);
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDataEksplan();
                Intent i = new Intent(readDataEksplan.this,mainMenuActivity.class);
                startActivity(i);
            }
        });

        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(readDataEksplan.this,
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
                datePickerDialog = new DatePickerDialog(readDataEksplan.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                tanggalTanamTxt.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(readDataEksplan.this,insertAnakanActivity.class);
                i.putExtra("idEksplan", idEksplan);
                startActivity(i);
            }
        });

        insertQRBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(readDataEksplan.this,insertQREksplanActivity.class);
                i.putExtra("idEksplan", idEksplan);
                i.putExtra("namaEksplan", namaEksplan);
                i.putExtra("jenisTanaman", jenisTanaman);
                startActivity(i);
            }
        });

        databaseReferenceAdmin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String _UIDnama = dataSnapshot.child("namaAdmin").getValue().toString();
                if(!namaAdmin.equals(_UIDnama))
                {
                    fab.hide();
                    deleteBtn.setVisibility(View.INVISIBLE);
                    insertQRBtn.setVisibility(View.INVISIBLE);
                    //changeGambarBtn.setVisibility(View.INVISIBLE);
                }
                else {
                    fab.show();
                    insertQRBtn.setVisibility(View.VISIBLE);
                    //changeGambarBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                //Toast.makeText(readDataEksplan.this,"Data "+namaEksplan+" Berhasi Diupdate",Toast.LENGTH_LONG).show();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(readDataEksplan.this);
                alertDialogBuilder.setTitle("Hapus Data Eksplan");
                alertDialogBuilder.setMessage("Apakah anda yakin untuk menghapus data ini?").setCancelable(false).
                        setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference dbEksplan = databaseReference.child("List Eksplan").child(idEksplan);
                                dbEksplan.removeValue();
                                Toast.makeText(readDataEksplan.this,"Data "+namaEksplan+" Berhasi Dihapus",Toast.LENGTH_LONG).show();
                                Intent i = new Intent(readDataEksplan.this,mainMenuActivity.class);
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

        eksplanAnakanRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String defaultAnakan = "Anakan:";
                String defaultIDAnakan = "";
                listAnakan.add(defaultAnakan);
                listIDAnakan.add(defaultIDAnakan);
                listjenisTanamanAnakan.add(defaultIDAnakan);
                listnamaAdminAnakan.add(defaultIDAnakan);
                listTanggalTanam.add(defaultIDAnakan);
                liststatusAnakan.add(defaultIDAnakan);
                listtempatPenyimpananAnakan.add(defaultIDAnakan);
                listMediaTanam.add(defaultAnakan);
                //final String wew = dataSnapshot.getKey();
                for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                    idEksplanAnakan = areaSnapshot.getKey();
                    namaEksplanAnakan = areaSnapshot.child("namaEksplan").getValue(String.class);
                    jenisTanamanAnakan = areaSnapshot.child("jenisTanaman").getValue(String.class);
                    statusAnakan = areaSnapshot.child("status").getValue(String.class);
                    String tanggalTanamAnakan = areaSnapshot.child("tanggalTanam").getValue(String.class);
                    String mediaTanam = areaSnapshot.child("mediaPenyimpanan").getValue(String.class);
                    tempatPenyimpananAnakan = areaSnapshot.child("tempatPenyimpanan").getValue(String.class);
                    namaAdminAnakan = areaSnapshot.child("namaAdmin").getValue(String.class);
                    listIDAnakan.add(idEksplanAnakan);
                    listAnakan.add(namaEksplanAnakan);
                    listTanggalTanam.add(tanggalTanamAnakan);
                    listjenisTanamanAnakan.add(jenisTanamanAnakan);
                    listMediaTanam.add(mediaTanam);
                    liststatusAnakan.add(statusAnakan);
                    listtempatPenyimpananAnakan.add(tempatPenyimpananAnakan);
                    listnamaAdminAnakan.add(namaAdminAnakan);

                    if(listAnakan.size()==1)
                    {
                        listAnakan.get(1).replace("Anakan:","Tidak ada Anakan") ;
                    }
                }
                final ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(readDataEksplan.this, android.R.layout.simple_spinner_dropdown_item, listAnakan);
                //final ArrayAdapter<String> areasAdapter2 = new ArrayAdapter<String>(readDataEksplan.this, android.R.layout.simple_spinner_dropdown_item, listAnakan);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner1.setAdapter(areasAdapter);
                spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        // Display the selected item into the TextView
                        //keteranganTxt.setText(listIDAnakan.get(position));
                        if(position==0)
                        {

                        }
                        else if (position>0) {
                            Intent i = new Intent(getApplicationContext(), readDataAnakan.class);
                            i.putExtra("idEksplanAnakan", listIDAnakan.get(position));
                            i.putExtra("namaEksplanAnakan", listAnakan.get(position));
                            i.putExtra("jenisTanamanAnakan", listjenisTanamanAnakan.get(position));
                            i.putExtra("statusAnakan", liststatusAnakan.get(position));
                            i.putExtra("tempatPenyimpananAnakan", listtempatPenyimpananAnakan.get(position));
                            i.putExtra("mediaTanam", listMediaTanam.get(position));
                            i.putExtra("tanggalTanam", listTanggalTanam.get(position));
                            i.putExtra("namaAdminAnakan", listnamaAdminAnakan.get(position));
                            i.putExtra("idEksplan",idEksplan);
                            //i.putExtra("anakan",anakan);
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    final String defaultAnakan = "Tidak ada Anakan";
                    listAnakan.add(defaultAnakan);
                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(readDataEksplan.this, android.R.layout.simple_spinner_dropdown_item, listAnakan);
                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner1.setAdapter(areasAdapter);
                }
            });
        if(listAnakan.size()==1)
        {
            listAnakan.get(0).replace("Anakan:","Tidak ada Anakan") ;
        }
        changeGambarBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
        });
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    databaseReferenceAdmin.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String _UIDnama = dataSnapshot.child("namaAdmin").getValue().toString();
                            if(!namaAdmin.equals(_UIDnama))
                            {
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(readDataEksplan.this);
                                alertDialogBuilder.setTitle("Warning!");
                                alertDialogBuilder.setMessage("Maaf Anda Tidak Dapat Mengupdate Data Ini. \nSilahkan Hubungi Admin yang Bersangkutan")
                                        .setCancelable(true).setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                                switch1.setChecked(false);
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

                                namaPelangganTxt.setEnabled(true);
                                namaPelangganTxt.setInputType(InputType.TYPE_CLASS_TEXT);
                                namaPelangganTxt.setFocusable(true);
                                namaPelangganTxt.setFocusableInTouchMode(true);

                                noTelpPelangganTxt.setEnabled(true);
                                noTelpPelangganTxt.setInputType(InputType.TYPE_CLASS_TEXT);
                                noTelpPelangganTxt.setFocusable(true);
                                noTelpPelangganTxt.setFocusableInTouchMode(true);

                                tanggalTerimaTxt.setEnabled(true);
                                tanggalTerimaTxt.setInputType(InputType.TYPE_CLASS_TEXT);
                                tanggalTerimaTxt.setFocusable(true);
                                tanggalTerimaTxt.setFocusableInTouchMode(true);

                                tanggalTanamTxt.setEnabled(true);
                                tanggalTanamTxt.setInputType(InputType.TYPE_CLASS_TEXT);
                                tanggalTanamTxt.setFocusable(true);
                                tanggalTanamTxt.setFocusableInTouchMode(true);

                                mediaPenyimpananTxt.setEnabled(true);
                                mediaPenyimpananTxt.setInputType(InputType.TYPE_CLASS_TEXT);
                                mediaPenyimpananTxt.setFocusable(true);
                                mediaPenyimpananTxt.setFocusableInTouchMode(true);

                                statusTxt.setEnabled(true);
                                statusTxt.setInputType(InputType.TYPE_CLASS_TEXT);
                                statusTxt.setFocusable(true);
                                statusTxt.setFocusableInTouchMode(true);

                                keteranganTxt.setEnabled(true);
                                keteranganTxt.setInputType(InputType.TYPE_CLASS_TEXT);
                                keteranganTxt.setFocusable(true);
                                keteranganTxt.setFocusableInTouchMode(true);

                                datePickerBtn.setVisibility(View.VISIBLE);
                                datePickerTanamBtn.setVisibility(View.VISIBLE);
                                updateBtn.setVisibility(View.VISIBLE);
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

                    namaPelangganTxt.setEnabled(false);
                    namaPelangganTxt.setInputType(InputType.TYPE_NULL);
                    namaPelangganTxt.setFocusable(false);

                    noTelpPelangganTxt.setEnabled(false);
                    noTelpPelangganTxt.setInputType(InputType.TYPE_NULL);
                    noTelpPelangganTxt.setFocusable(false);

                    tanggalTerimaTxt.setEnabled(false);
                    tanggalTerimaTxt.setInputType(InputType.TYPE_NULL);
                    tanggalTerimaTxt.setFocusable(false);

                    mediaPenyimpananTxt.setEnabled(false);
                    mediaPenyimpananTxt.setInputType(InputType.TYPE_NULL);
                    mediaPenyimpananTxt.setFocusable(false);

                    tanggalTanamTxt.setEnabled(false);
                    tanggalTanamTxt.setInputType(InputType.TYPE_NULL);
                    tanggalTanamTxt.setFocusable(false);

                    statusTxt.setEnabled(false);
                    statusTxt.setInputType(InputType.TYPE_NULL);
                    statusTxt.setFocusable(false);

                    keteranganTxt.setEnabled(false);
                    keteranganTxt.setInputType(InputType.TYPE_NULL);
                    keteranganTxt.setFocusable(false);
                    datePickerBtn.setVisibility(View.INVISIBLE);
                    datePickerTanamBtn.setVisibility(View.INVISIBLE);
                    updateBtn.setVisibility(View.INVISIBLE);
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
                    imgGambar.setImageURI(selectedImageUri);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void updateDataEksplan() {
        String namaEksplan = namaEksplanTxt.getText().toString().trim();
        String jenisTanaman = jenisTanamanTxt.getText().toString().trim();
        String tempatPenyimpanan = tempatPenyimpananTxt.getText().toString().trim();
        String namaAdmin = namaAdminTxt.getText().toString().trim();
        String noTelpPelanggan = noTelpPelangganTxt.getText().toString().trim();
        String namaPelanggan = namaPelangganTxt.getText().toString().trim();
        String tanggalTerima = tanggalTerimaTxt.getText().toString().trim();
        String mediaTanam = mediaPenyimpananTxt.getText().toString().trim();
        String tanggalTanam;
        String status = statusTxt.getText().toString().trim();
        String keterangan = keteranganTxt.getText().toString().trim();
        if(status.equals("Isolasi")||status.equals("Sterilisasi")||status.equals("Inisiasi"))
        {
            tanggalTanam = "";
        }
        else {
            tanggalTanam = tanggalTanamTxt.getText().toString().trim();
        }
        final StorageReference imageRef = mStorageRef.child("EksplanImages");

        imgGambar.setDrawingCacheEnabled(true);
        imgGambar.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imgGambar.getDrawable()).getBitmap();
        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,120,120, false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] dataImg = baos.toByteArray();
        final UploadTask uploadTask = imageRef.child(namaEksplan).child(namaEksplan+".jpg").putBytes(dataImg);

        boolean isEmptyFields = false;

        if (TextUtils.isEmpty(namaEksplan)) {
            isEmptyFields = true;
            namaEksplanTxt.setError("Field ini tidak boleh kosong");
        }
        if (TextUtils.isEmpty(jenisTanaman)) {
            isEmptyFields = true;
            jenisTanamanTxt.setError("Field ini tidak boleh kosong");
        }
        if (TextUtils.isEmpty(jenisTanaman)) {
            isEmptyFields = true;
            namaAdminTxt.setError("Field ini tidak boleh kosong");
        }
        if (TextUtils.isEmpty(tempatPenyimpanan)) {
            isEmptyFields = true;
            tempatPenyimpananTxt.setError("Field ini tidak boleh kosong");
        }
        if (TextUtils.isEmpty(namaPelanggan)) {
            isEmptyFields = true;
            namaPelangganTxt.setError("Field ini tidak boleh kosong");
        }
        if (TextUtils.isEmpty(namaPelanggan)) {
            isEmptyFields = true;
            noTelpPelangganTxt.setError("Field ini tidak boleh kosong");
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
        if (!isEmptyFields) {
            DatabaseReference dbEksplan = databaseReference.child("List Eksplan");
            Toast.makeText(readDataEksplan.this,"Data "+namaEksplan+" Berhasi Diupdate",Toast.LENGTH_LONG).show();
            String id = dbEksplan.push().getKey();
            dataEksplan.setNamaEksplan(namaEksplan);
            dataEksplan.setJenisTanaman(jenisTanaman);
            dataEksplan.setNamaPelanggan(namaPelanggan);
            dataEksplan.setNoTelpPelanggan(noTelpPelanggan);
            dataEksplan.setTanggalTerima(tanggalTerima);
            dataEksplan.setTanggalTanam(tanggalTanam);
            dataEksplan.setStatus(status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase());
            dataEksplan.setKeterangan(keterangan);
            dataEksplan.setMediaPenyimpanan(mediaTanam);
            dataEksplan.setTempatPenyimpanan(tempatPenyimpanan);
            dbEksplan.child(idEksplan).child("namaEksplan").setValue(namaEksplan);
            dbEksplan.child(idEksplan).child("namaAdmin").setValue(namaAdmin);
            dbEksplan.child(idEksplan).child("jenisTanaman").setValue(jenisTanaman);
            dbEksplan.child(idEksplan).child("namaPelanggan").setValue(namaPelanggan);
            dbEksplan.child(idEksplan).child("noTelpPelanggan").setValue(noTelpPelanggan);
            dbEksplan.child(idEksplan).child("tanggalTerima").setValue(tanggalTerima);
            dbEksplan.child(idEksplan).child("tanggalTanam").setValue(tanggalTanam);
            dbEksplan.child(idEksplan).child("mediaPenyimpanan").setValue(mediaTanam);
            dbEksplan.child(idEksplan).child("status").setValue(status.substring(0, 1).toUpperCase() + status.substring(1).toLowerCase());
            dbEksplan.child(idEksplan).child("keterangan").setValue(keterangan);
            dbEksplan.child(idEksplan).child("tempatPenyimpanan").setValue(tempatPenyimpanan);
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
                            dbEksplan.child(idEksplan).child("linkImages").setValue(imageQRRef);
                            //finish();
                        }
                    }));
                }
            });
        }

    }

}
