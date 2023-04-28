package com.example.kuljarhanip.ui.slideshow;

import android.content.Intent;
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
import com.example.kuljarhanip.readDataEksplan;
import com.example.kuljarhanip.ui.home.dataEksplanViewHolder;
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

import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private View listDataEksplanView;
    private RecyclerView listDataEksplan;
    public DatabaseReference dataEksplanRef, eksplanRef;
    public FirebaseAuth mAuth;
    public String currentUserID;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        listDataEksplanView  = inflater.inflate(R.layout.fragment_home, container, false);
        listDataEksplan = listDataEksplanView.findViewById(R.id.listDataEksplanView);
        listDataEksplan.setLayoutManager(new LinearLayoutManager(getContext()));
        dataEksplanRef = FirebaseDatabase.getInstance().getReference().child("List Eksplan");
        FloatingActionButton fab = getActivity().findViewById(R.id.fab2);
        FloatingActionButton fab2 = getActivity().findViewById(R.id.fab);
        fab2.hide();
        fab.hide();

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        return listDataEksplanView;
    }

    @Override
    public void onStart() {
        super.onStart();
        String kontam = "Kontaminasi";
        Query que = dataEksplanRef.orderByChild("status").equalTo(kontam);
        init(que);
    }
    public void init(Query que)
    {
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
                                    if (dataSnapshot.child("status").getValue().toString().equals("Kontaminasi"))
                                    {
                                        final String jenisTanaman = dataSnapshot.child("jenisTanaman").getValue().toString();
                                        final String namaEksplan = dataSnapshot.child("namaEksplan").getValue().toString();
                                        final String namaAdmin = dataSnapshot.child("namaAdmin").getValue().toString();
                                        final String namaPelanggan = dataSnapshot.child("namaPelanggan").getValue().toString();
                                        final String tanggalTerima = dataSnapshot.child("tanggalTerima").getValue().toString();
                                        final String tanggalTanam = dataSnapshot.child("tanggalTanam").getValue().toString();
                                        final String mediaTanam = dataSnapshot.child("mediaPenyimpanan").getValue().toString();
                                        final String ketEksplan = dataSnapshot.child("keterangan").getValue().toString();
                                        final String Status = dataSnapshot.child("status").getValue().toString();
                                        final String tempatPenyimpanan = dataSnapshot.child("tempatPenyimpanan").getValue().toString();
                                        final String noTelpPelanggan = dataSnapshot.child("noTelpPelanggan").getValue().toString();
                                        //final String anakan = dataSnapshot.child("anakan").getValue().toString();

                                        dataEksplanViewHolder.jenisTanaman.setText(namaEksplan);
                                        dataEksplanViewHolder.ketEksplan.setText(namaPelanggan);
                                        dataEksplanViewHolder.statusEksplan.setText(Status);

                                        dataEksplanViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent i = new Intent(getContext(), readDataEksplan.class);
                                                i.putExtra("idEksplan",idEksplan);
                                                i.putExtra("namaEksplan",namaEksplan);
                                                i.putExtra("namaPelanggan",namaPelanggan);
                                                i.putExtra("namaAdmin",namaAdmin);
                                                i.putExtra("jenisTanaman",jenisTanaman);
                                                i.putExtra("tanggalTerima",tanggalTerima);
                                                i.putExtra("tanggalTanam",tanggalTanam);
                                                i.putExtra("mediaTanam",mediaTanam);
                                                i.putExtra("keterangan",ketEksplan);
                                                i.putExtra("status",Status);
                                                i.putExtra("tempatPenyimpanan",tempatPenyimpanan);
                                                i.putExtra("noTelpPelanggan",noTelpPelanggan);
                                                //i.putExtra("anakan",anakan);

                                                startActivity(i);
                                            }
                                        });
                                    }
                                    else {
                                       // dataEksplanViewHolder.jenisTanaman.setVisibility(View.GONE);
                                        //dataEksplanViewHolder.ketEksplan.setVisibility(View.GONE);
                                       // dataEksplanViewHolder.statusEksplan.setVisibility(View.GONE);
                                        /*final String jenisTanaman = dataSnapshot.child("jenisTanaman").getValue().toString();
                                        final String namaEksplan = dataSnapshot.child("namaEksplan").getValue().toString();
                                        final String namaPelanggan = dataSnapshot.child("namaPelanggan").getValue().toString();
                                        final String tanggalTerima = dataSnapshot.child("tanggalTerima").getValue().toString();
                                        final String ketEksplan = dataSnapshot.child("keterangan").getValue().toString();
                                        final String Status = dataSnapshot.child("status").getValue().toString();
                                        final String tempatPenyimpanan = dataSnapshot.child("tempatPenyimpanan").getValue().toString();
                                        //final String anakan = dataSnapshot.child("Anakan").getValue().toString();

                                        dataEksplanViewHolder.jenisTanaman.setText(jenisTanaman);
                                        dataEksplanViewHolder.ketEksplan.setText(ketEksplan);

                                        dataEksplanViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent i = new Intent(getContext(), readDataEksplan.class);
                                                i.putExtra("idEksplan",idEksplan);
                                                i.putExtra("namaEksplan",namaEksplan);
                                                i.putExtra("namaPelanggan",namaPelanggan);
                                                i.putExtra("jenisTanaman",jenisTanaman);
                                                i.putExtra("tanggalTerima",tanggalTerima);
                                                i.putExtra("keterangan",ketEksplan);
                                                i.putExtra("status",Status);
                                                i.putExtra("tempatPenyimpanan",tempatPenyimpanan);
                                                // i.putExtra("anakan",anakan);

                                                startActivity(i);
                                            }
                                        });*/
                                    }
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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Daftar Kontam");
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem eksplanItem = menu.findItem(R.id.action_search);
        eksplanItem.setVisible(false);
        SearchView searchView = (SearchView) eksplanItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length()>0) {
                    query = query.substring(0, 1).toUpperCase();
                    searchProcessnamaEksplan(query);
                    //searchProcessnamaPelanggan(query);
                    //searchProcessStatus(query);
                }
                else
                {
                    //levelSearch = 0;
                    Query que = dataEksplanRef.orderByChild("status").equalTo("Kontaminasi");
                    init(que);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length()>0) {
                    newText = newText.substring(0,1).toUpperCase();
                    searchProcessnamaEksplan(newText);
                    //searchProcessnamaPelanggan(newText);
                    //searchProcessStatus(newText);
                }
                else
                {
                    //levelSearch = 0;
                    Query que = dataEksplanRef.orderByChild("status").equalTo("Kontaminasi");
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
        if (id == R.id.action_searchbyNama)
        {
            return true;
        }
        if (id == R.id.action_searchStatus)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    public void searchProcessnamaEksplan(String s)
    {
        Query que1 = dataEksplanRef.orderByChild("namaEksplan").startAt(s).endAt(s+"\uf8ff");
        // Query que2 = dataEksplanRef.orderByChild("namaPelanggan").startAt(s).endAt(s+"\uf8ff");
        // Query que3 = dataEksplanRef.orderByChild("status").startAt(s).endAt(s+"\uf8ff");
        init(que1);
    }
}