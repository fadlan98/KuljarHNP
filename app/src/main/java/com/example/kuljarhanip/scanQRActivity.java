package com.example.kuljarhanip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

public class scanQRActivity extends AppCompatActivity {
    CodeScanner codeScanner;
    CodeScannerView scannerView;
    //private String idEksplan,namaEksplan, jenisTanaman, tempatPenyimpanan, namaPelanggan, noTelpPelanggan, tanggalTerima, status,keterangan, anakan;
    private TextView resultTxt;
    public DatabaseReference dataEksplanRef, dataEksplanAnakanRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        scannerView = findViewById(R.id.scanner_view);
        //resultTxt = findViewById(R.id.resultText);
        codeScanner = new CodeScanner(scanQRActivity.this, scannerView);
        dataEksplanRef = FirebaseDatabase.getInstance().getReference().child("List Eksplan");
        dataEksplanAnakanRef = FirebaseDatabase.getInstance().getReference().child("List Eksplan");
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //final String idEksplan = result.getText().toString();
                        final String res = result.getText();
                        final String[] codeEksplan = res.split(" ");
                        final String idEksplan,idEksplanAnakan;
                        if(codeEksplan.length > 1){
                            idEksplan = codeEksplan[0];
                            idEksplanAnakan = codeEksplan[1];
                            dataEksplanRef.child(idEksplan).child("Anakan").child(idEksplanAnakan).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        final String jenisTanaman = dataSnapshot.child("jenisTanaman").getValue().toString();
                                        final String namaEksplan = dataSnapshot.child("namaEksplan").getValue().toString();
                                        final String namaAdmin = dataSnapshot.child("namaAdmin").getValue().toString();
                                        final String Status = dataSnapshot.child("status").getValue().toString();
                                        final String tempatPenyimpanan = dataSnapshot.child("tempatPenyimpanan").getValue().toString();

                                        Intent i = new Intent(scanQRActivity.this, readDataAnakan.class);
                                        i.putExtra("idEksplan", idEksplan);
                                        i.putExtra("idEksplanAnakan", idEksplanAnakan);
                                        i.putExtra("namaEksplanAnakan", namaEksplan);
                                        i.putExtra("namaAdminAnakan", namaAdmin);
                                        i.putExtra("jenisTanamanAnakan", jenisTanaman);
                                        i.putExtra("statusAnakan", Status);
                                        i.putExtra("tempatPenyimpananAnakan", tempatPenyimpanan);
                                        startActivity(i);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(scanQRActivity.this,"Data tidak Ditemukan",Toast.LENGTH_SHORT).show();
                                }
                            });
                            //resultTxt.setText(idEksplan + " " + idEksplanAnakan);
                        }
                        else{
                            idEksplan = codeEksplan[0];
                            //resultTxt.setText(idEksplan);
                            dataEksplanRef.child(idEksplan).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        final String jenisTanaman = dataSnapshot.child("jenisTanaman").getValue().toString();
                                        final String namaEksplan = dataSnapshot.child("namaEksplan").getValue().toString();
                                        final String namaPelanggan = dataSnapshot.child("namaPelanggan").getValue().toString();
                                        final String noTelpPelanggan = dataSnapshot.child("noTelpPelanggan").getValue().toString();
                                        final String tanggalTerima = dataSnapshot.child("tanggalTerima").getValue().toString();
                                        final String namaAdmin = dataSnapshot.child("namaAdmin").getValue().toString();
                                        final String ketEksplan = dataSnapshot.child("keterangan").getValue().toString();
                                        final String Status = dataSnapshot.child("status").getValue().toString();
                                        final String tempatPenyimpanan = dataSnapshot.child("tempatPenyimpanan").getValue().toString();

                                        Intent i = new Intent(scanQRActivity.this, readDataEksplan.class);
                                        i.putExtra("idEksplan", idEksplan);
                                        i.putExtra("namaEksplan", namaEksplan);
                                        i.putExtra("namaPelanggan", namaPelanggan);
                                        i.putExtra("noTelpPelanggan", noTelpPelanggan);
                                        i.putExtra("jenisTanaman", jenisTanaman);
                                        i.putExtra("tanggalTerima", tanggalTerima);
                                        i.putExtra("namaAdmin", namaAdmin);
                                        i.putExtra("keterangan", ketEksplan);
                                        i.putExtra("status", Status);
                                        i.putExtra("tempatPenyimpanan", tempatPenyimpanan);
                                        //i.putExtra("anakan",anakan);
                                        startActivity(i);
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(scanQRActivity.this,"Data tidak Ditemukan",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
//                        resultTxt.setText(idEksplan);
                        //if(!idEksplan.equals(dataEksplanRef.child(idEksplan).equalTo(idEksplan).toString()))
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        //codeScanner.startPreview();
        requestForCamera();
        codeScanner.startPreview();
    }

    private void requestForCamera() {
        Dexter.withActivity(scanQRActivity.this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(scanQRActivity.this,"Camera Permission is Required",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }
/*
    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }*/
}
