package com.example.kuljarhanip.ui.home;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kuljarhanip.R;
import com.example.kuljarhanip.RecyclerAdapter;
import com.example.kuljarhanip.dataEksplan;
import com.example.kuljarhanip.mainMenuActivity;
import com.example.kuljarhanip.readDataEksplan;
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

public class HomeFragment extends Fragment {

    private View listDataEksplanView;
    private RecyclerView listDataEksplan;
    public DatabaseReference dataEksplanRef, eksplanRef;
    public FirebaseAuth mAuth;
    public String currentUserID;
    public boolean searchFound = false;
    public String searchF;
    //Bundle bundle = new Bundle();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        //getActivity().setTitle("List Eksplan");
        listDataEksplanView  = inflater.inflate(R.layout.fragment_home, container, false);
        listDataEksplan = listDataEksplanView.findViewById(R.id.listDataEksplanView);
        listDataEksplan.setLayoutManager(new LinearLayoutManager(getContext()));
        FloatingActionButton fab = getActivity().findViewById(R.id.fab2);
        FloatingActionButton fab2 = getActivity().findViewById(R.id.fab);
        fab2.show();
        fab.hide();
        dataEksplanRef = FirebaseDatabase.getInstance().getReference().child("List Eksplan");
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        return listDataEksplanView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query que = dataEksplanRef.orderByChild("namaEksplan");
        init(que);
    }
    public void init(Query q)
    {
        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<dataEksplan>()
                        .setQuery(q, dataEksplan.class).build();
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
                                    searchFound = true;
                                    if(searchFound)
                                        searchF = "Found";
                                    if (dataSnapshot.hasChild("status" ))
                                    {
                                        final String jenisTanaman = dataSnapshot.child("jenisTanaman").getValue().toString();
                                        final String namaEksplan = dataSnapshot.child("namaEksplan").getValue().toString();
                                        final String namaAdmin = dataSnapshot.child("namaAdmin").getValue().toString();
                                        final String namaPelanggan = dataSnapshot.child("namaPelanggan").getValue().toString();
                                        final String noTelpPelanggan = dataSnapshot.child("noTelpPelanggan").getValue().toString();
                                        final String tanggalTerima = dataSnapshot.child("tanggalTerima").getValue().toString();
                                        final String tanggalTanam = dataSnapshot.child("tanggalTanam").getValue().toString();
                                        final String mediaTanam = dataSnapshot.child("mediaPenyimpanan").getValue().toString();
                                        final String ketEksplan = dataSnapshot.child("keterangan").getValue().toString();
                                        final String Status = dataSnapshot.child("status").getValue().toString();
                                        final String tempatPenyimpanan = dataSnapshot.child("tempatPenyimpanan").getValue().toString();
                                        //final String anakan = dataSnapshot.child("anakan").getValue().toString();

                                        dataEksplanViewHolder.jenisTanaman.setText(namaEksplan);
                                        dataEksplanViewHolder.ketEksplan.setText("Nama Pelanggan: " + namaPelanggan);
                                        dataEksplanViewHolder.statusEksplan.setText(Status);

                                        dataEksplanViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent i = new Intent(getContext(), readDataEksplan.class);
                                                i.putExtra("idEksplan",idEksplan);
                                                i.putExtra("namaEksplan",namaEksplan);
                                                i.putExtra("namaAdmin",namaAdmin);
                                                i.putExtra("namaPelanggan",namaPelanggan);
                                                i.putExtra("noTelpPelanggan",noTelpPelanggan);
                                                i.putExtra("jenisTanaman",jenisTanaman);
                                                i.putExtra("tanggalTerima",tanggalTerima);
                                                i.putExtra("tanggalTanam",tanggalTanam);
                                                i.putExtra("mediaTanam",mediaTanam);
                                                i.putExtra("keterangan",ketEksplan);
                                                i.putExtra("status",Status);
                                                i.putExtra("tempatPenyimpanan",tempatPenyimpanan);
                                                //i.putExtra("anakan",anakan);

                                                startActivity(i);
                                            }
                                        });

                                    }
                                    else {
                                        final String jenisTanaman = dataSnapshot.child("jenisTanaman").getValue().toString();
                                        final String namaEksplan = dataSnapshot.child("namaEksplan").getValue().toString();
                                        final String namaAdmin = dataSnapshot.child("namaAdmin").getValue().toString();
                                        final String namaPelanggan = dataSnapshot.child("namaPelanggan").getValue().toString();
                                        final String noTelpPelanggan = dataSnapshot.child("noTelpPelanggan").getValue().toString();
                                        final String tanggalTerima = dataSnapshot.child("tanggalTerima").getValue().toString();
                                        final String tanggalTanam = dataSnapshot.child("tanggalTanam").getValue().toString();
                                        final String mediaTanam = dataSnapshot.child("mediaPenyimpanan").getValue().toString();
                                        final String ketEksplan = dataSnapshot.child("keterangan").getValue().toString();
                                        final String Status = dataSnapshot.child("status").getValue().toString();
                                        final String tempatPenyimpanan = dataSnapshot.child("tempatPenyimpanan").getValue().toString();
                                        //final String anakan = dataSnapshot.child("Anakan").getValue().toString();

                                        dataEksplanViewHolder.jenisTanaman.setText(namaEksplan);
                                        dataEksplanViewHolder.ketEksplan.setText("Nama Pelanggan: " + namaPelanggan);

                                        dataEksplanViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent i = new Intent(getContext(), readDataEksplan.class);
                                                i.putExtra("idEksplan",idEksplan);
                                                i.putExtra("namaEksplan",namaEksplan);
                                                i.putExtra("namaPelanggan",namaPelanggan);
                                                i.putExtra("namaAdmin",namaAdmin);
                                                i.putExtra("noTelpPelanggan",noTelpPelanggan);
                                                i.putExtra("jenisTanaman",jenisTanaman);
                                                i.putExtra("tanggalTanam",tanggalTanam);
                                                i.putExtra("mediaTanam",mediaTanam);
                                                i.putExtra("tanggalTerima",tanggalTerima);
                                                i.putExtra("keterangan",ketEksplan);
                                                i.putExtra("status",Status);
                                                i.putExtra("tempatPenyimpanan",tempatPenyimpanan);
                                                // i.putExtra("anakan",anakan);

                                                startActivity(i);
                                            }
                                        });
                                    }
                                }
                                 else  {
                                     searchFound = false;
                                     if(!searchFound)
                                         searchF = "not found";
                                }
                                 System.out.println(searchF);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public dataEksplanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data_eksplan_layout, parent,false);
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("List Eksplan");
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem eksplanItem = menu.findItem(R.id.action_search);
        MenuItem eksplanItem2 = menu.findItem(R.id.action_searchbyNama);
        MenuItem eksplanItem3 = menu.findItem(R.id.action_searchStatus);
        SearchView searchView = (SearchView) eksplanItem.getActionView();
        SearchView searchView2 = (SearchView) eksplanItem2.getActionView();
        SearchView searchView3 = (SearchView) eksplanItem3.getActionView();
        searchView.setQueryHint("Cari Nama Eksplan");
        searchView2.setQueryHint("Cari Nama Pelanggan");
        searchView3.setQueryHint("Cari Status");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length()>0) {
                    //query = query.substring(0, 1).toUpperCase();
                    searchProcessnamaEksplan(query.substring(0,1).toUpperCase()+ query.substring(1).toLowerCase());
                    //searchProcessnamaPelanggan(query);
                    //searchProcessStatus(query);
                }
                else
                {
                    //levelSearch = 0;
                    Query que = dataEksplanRef.orderByChild("namaEksplan");
                    init(que);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length()>0) {
                    //newText = newText.substring(0,1).toUpperCase();
                    searchProcessnamaEksplan(newText.substring(0,1).toUpperCase() + newText.substring(1).toLowerCase());
                    //searchProcessnamaPelanggan(newText);
                    //searchProcessStatus(newText);
                }
                else
                {
                    //levelSearch = 0;
                    Query que = dataEksplanRef.orderByChild("namaEksplan");
                    init(que);
                }
                return false;
            }
        });

        searchView2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length()>0) {
                    //query = query.substring(0, 1).toUpperCase();
                    //searchProcessnamaEksplan(query);
                    searchProcessnamaPelanggan(query.substring(0,1).toUpperCase()+ query.substring(1).toLowerCase());
                    //searchProcessStatus(query);
                }
                else
                {
                    //levelSearch = 0;
                    Query que = dataEksplanRef.orderByChild("namaPelanggan");
                    init(que);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length()>0) {
                    //newText = newText.substring(0,1).toUpperCase();
                    //searchProcessnamaEksplan(newText);
                    searchProcessnamaPelanggan(newText.substring(0,1).toUpperCase() + newText.substring(1).toLowerCase());
                    //searchProcessStatus(newText);
                }
                else
                {
                    //levelSearch = 0;
                    Query que = dataEksplanRef.orderByChild("namaPelanggan");
                    init(que);
                }
                return false;
            }
        });
        searchView3.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String Fquery = query.substring(0, 1).toUpperCase();
                if (query.length()>1) {
                    //query = query.substring(0, 1).toUpperCase() + query;

                    //searchProcessStatus(query);
                    //searchProcessnamaPelanggan(query);
                    searchProcessStatus(query.substring(0,1).toUpperCase()+ query.substring(1).toLowerCase());
                }
                else
                {
                    //levelSearch = 0;
                    Query que = dataEksplanRef.orderByChild("status");
                    init(que);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length()>0) {
                    //newText = newText.substring(0,1).toUpperCase() + newText;
                    //searchProcessStatus(newText);
                    //searchProcessnamaPelanggan(newText);
                    searchProcessStatus(newText.substring(0,1).toUpperCase() + newText.substring(1).toLowerCase());
                }
                else
                {
                    //levelSearch = 0;
                    Query que = dataEksplanRef.orderByChild("status");
                    init(que);
                }
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search)
        {
            return true;
        }
        else if (id == R.id.action_searchbyNama)
        {
            return true;
        }
        else if (id == R.id.action_searchStatus)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void searchProcessnamaEksplan(String s)
    {
        Query que = dataEksplanRef.orderByChild("namaPelanggan").startAt(s).endAt(s+"\uf8ff");
        Query que2 = dataEksplanRef.orderByChild("namaEksplan").startAt(s).endAt(s+"\uf8ff");
       // Query que3 = dataEksplanRef.orderByChild("status").startAt(s).endAt(s+"\uf8ff");
        init(que2);
    }
    public void searchProcessnamaPelanggan(String s)
    {
        Query que = dataEksplanRef.orderByChild("namaPelanggan").startAt(s).endAt(s+"\uf8ff");

        init(que);
    }
    public void searchProcessStatus(String s)
    {
        Query que = dataEksplanRef.orderByChild("status").startAt(s).endAt(s+"\uf8ff");

        init(que);
    }


}