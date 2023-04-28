package com.example.kuljarhanip;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

public class mainMenuActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public String updateStatus, namaAdmin, idAdmin;
    public TextView textViewMainMenuTxt, textViewHeaderTxt;
    private ImageView imgView;
    private FirebaseAuth mAuth;
    public DatabaseReference databaseReference;
    public StorageReference storageReference;
    public SearchView searchView;
    private RecyclerView listDataEksplan;
    public DatabaseReference dataEksplanRef;
    private View listDataEksplanView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser theUser = mAuth.getCurrentUser();
        final String _UID = theUser.getUid().toString();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        FloatingActionButton fab,fab2;

        DatabaseReference dbAdmin = databaseReference.child("Admin");
        dataEksplanRef = FirebaseDatabase.getInstance().getReference().child("List Eksplan");
        dbAdmin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                namaAdmin = dataSnapshot.child(_UID).child("namaAdmin").getValue().toString();
                idAdmin = dataSnapshot.child(_UID).child("idAdmin").getValue().toString();
                textViewMainMenuTxt.setText(idAdmin);
                textViewHeaderTxt.setText(namaAdmin);
                storageReference = FirebaseStorage.getInstance().getReference().child("Admin").child(_UID).child(namaAdmin+".jpg");
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(mainMenuActivity.this).load(uri).into(imgView);
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        fab = findViewById(R.id.fab);
        fab2 = findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mainMenuActivity.this, insertDataEksplanActivity.class);
                startActivity(i);
            }
        });
        fab2.setOnClickListener(v -> {
                Intent i = new Intent(mainMenuActivity.this, insertTanamanActivity.class);
                startActivity(i);
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        imgView = headerView.findViewById(R.id.imageViewAdmin);
        imgView.setClipToOutline(true);
        textViewMainMenuTxt = headerView.findViewById(R.id.textViewMainMenu);
        textViewHeaderTxt = headerView.findViewById(R.id.textViewHeader);

        /**/
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.berandaFragment, R.id.nav_listEksplan, R.id.nav_scanQRCode, R.id.nav_daftarKontam,
                R.id.nav_daftarRak, R.id.nav_about, R.id.nav_logOut, R.id.blankFragment, R.id.insertDataEksplanActivity, R.id.tanamanFragment)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }



}
