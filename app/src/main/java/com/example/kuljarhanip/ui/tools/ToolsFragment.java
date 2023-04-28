package com.example.kuljarhanip.ui.tools;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kuljarhanip.R;
import com.example.kuljarhanip.dataEksplan;
import com.example.kuljarhanip.readDataAnakan;
import com.example.kuljarhanip.readDataEksplan;
import com.example.kuljarhanip.ui.home.dataEksplanViewHolder;
import com.example.kuljarhanip.ui.slideshow.SlideshowViewModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ToolsFragment extends Fragment {

    private ToolsViewModel toolsViewModel;
    private SlideshowViewModel slideshowViewModel;
    private View listDataEksplanView;
    private RecyclerView listDataEksplan;
    public DatabaseReference dataEksplanRef, eksplanAnakanRef;
    public FirebaseAuth mAuth;
    public String currentUserID;
   //public String idAnakan;
    public final List<String> idEksplan = new ArrayList<>();
    List<String> idAnakanEksplan = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        listDataEksplanView  = inflater.inflate(R.layout.fragment_tools, container, false);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab2);
        FloatingActionButton fab2 = getActivity().findViewById(R.id.fab);
        fab2.hide();
        fab.hide();
        listDataEksplan = listDataEksplanView.findViewById(R.id.listDataEksplanJualView);
        listDataEksplan.setLayoutManager(new LinearLayoutManager(getContext()));
        dataEksplanRef = FirebaseDatabase.getInstance().getReference().child("List Jual");
        //eksplanAnakanRef = FirebaseDatabase.getInstance().getReference().child("List Eksplan").child("Anakan");

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        return listDataEksplanView;
    }

    @Override
    public void onStart() {
        super.onStart();
        String stats = "Terjual";
        Query que = dataEksplanRef.orderByChild("status").equalTo(stats);
        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<dataEksplan>()
                        .setQuery(que, dataEksplan.class).build();
        final FirebaseRecyclerAdapter<dataEksplan, dataEksplanViewHolder> adapter =
                new FirebaseRecyclerAdapter<dataEksplan, dataEksplanViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final dataEksplanViewHolder dataEksplanViewHolder, final int i, @NonNull dataEksplan dataEksplan) {
                        final String idEksplan = getRef(i).getKey();
                        dataEksplanRef.child(idEksplan).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists())
                                {
                                    if (dataSnapshot.hasChild("status"))
                                    {
                                        final String jenisTanaman = dataSnapshot.child("jenisTanaman").getValue().toString();
                                        final String namaEksplan = dataSnapshot.child("namaEksplan").getValue().toString();
                                        final String emailAdmin = dataSnapshot.child("namaAdmin").getValue().toString();
                                        final String Status = dataSnapshot.child("status").getValue().toString();
                                        final String tempatPenyimpanan = dataSnapshot.child("tempatPenyimpanan").getValue().toString();
                                        final String tanggalTanam = dataSnapshot.child("tanggalTanam").getValue().toString();
                                        final String mediaTanam = dataSnapshot.child("mediaPenyimpanan").getValue().toString();
                                        final String idEksplanUntukAnakan = dataSnapshot.child("idEksplanUntukAnakan").getValue().toString();
                                        //final String anakan = dataSnapshot.child("anakan").getValue().toString();

                                        dataEksplanViewHolder.jenisTanamanJual.setText(namaEksplan);
                                        //dataEksplanViewHolder.jenisTanaman.setPadding(0,20,0,0);
                                       // dataEksplanViewHolder.ketEksplan.setVisibility(View.GONE);
                                        dataEksplanViewHolder.statusEksplanJual.setTextSize(30.0f);
                                        dataEksplanViewHolder.statusEksplanJual.setText(Status);
                                        dataEksplanViewHolder.statusEksplanJual.setTextColor(Color.rgb(0,200,0));
                                        dataEksplanViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent i = new Intent(getContext(), readDataAnakan.class);
                                                i.putExtra("idEksplanAnakan",idEksplan);
                                                i.putExtra("namaEksplanAnakan",namaEksplan);
                                                i.putExtra("namaAdminAnakan",emailAdmin);
                                                //i.putExtra("namaPelanggan",namaPelanggan);
                                               // i.putExtra("noTelpPelanggan",noTelpPelanggan);
                                                i.putExtra("jenisTanamanAnakan",jenisTanaman);
                                              //  i.putExtra("tanggalTerima",tanggalTerima);
                                               // i.putExtra("keterangan",ketEksplan);
                                                i.putExtra("statusAnakan",Status);
                                                i.putExtra("tempatPenyimpananAnakan",tempatPenyimpanan);
                                                i.putExtra("tanggalTanam",tanggalTanam);
                                                i.putExtra("mediaTanam",mediaTanam);
                                                i.putExtra("idEksplan",idEksplanUntukAnakan);
                                                //i.putExtra("anakan",anakan);

                                                startActivity(i);
                                            }
                                        });

                                    }
                                    /*else {
                                        final String jenisTanaman = dataSnapshot.child("jenisTanaman").getValue().toString();
                                        final String namaEksplan = dataSnapshot.child("namaEksplan").getValue().toString();
                                        final String emailAdmin = dataSnapshot.child("namaAdmin").getValue().toString();
                                        final String Status = dataSnapshot.child("status").getValue().toString();
                                        final String tempatPenyimpanan = dataSnapshot.child("tempatPenyimpanan").getValue().toString();
                                        //final String anakan = dataSnapshot.child("Anakan").getValue().toString();

                                        dataEksplanViewHolder.jenisTanaman.setText(namaEksplan);
                                        dataEksplanViewHolder.ketEksplan.setText("");

                                        dataEksplanViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent i = new Intent(getContext(), readDataEksplan.class);
                                                i.putExtra("idEksplan",idEksplan);
                                                i.putExtra("namaEksplan",namaEksplan);
                                                //i.putExtra("namaPelanggan",namaPelanggan);
                                                i.putExtra("emailAdmin",emailAdmin);
                                               // i.putExtra("noTelpPelanggan",noTelpPelanggan);
                                                i.putExtra("jenisTanaman",jenisTanaman);
                                               // i.putExtra("tanggalTerima",tanggalTerima);
                                              //  i.putExtra("keterangan",ketEksplan);
                                                i.putExtra("status",Status);
                                                i.putExtra("tempatPenyimpanan",tempatPenyimpanan);
                                                // i.putExtra("anakan",anakan);

                                                startActivity(i);
                                            }
                                        });
                                    }*/
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public dataEksplanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data_jual_layout, parent,false);
                        dataEksplanViewHolder viewHolder = new dataEksplanViewHolder(view);
                        return viewHolder;
                    }
                };
        listDataEksplan.setAdapter(adapter);
        adapter.startListening();
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL);
        listDataEksplan.addItemDecoration(itemDecoration);
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.action_search);
        if(item!=null)
            item.setVisible(false);

        MenuItem item1=menu.findItem(R.id.action_searchbyNama);
        if(item!=null)
            item1.setVisible(false);
        MenuItem item2=menu.findItem(R.id.action_searchStatus);
        if(item!=null)
            item2.setVisible(false);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Daftar Jual");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search)
        {
            return true;
        }if (id == R.id.action_searchbyNama)
        {
            return true;
        }if (id == R.id.action_searchStatus)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}