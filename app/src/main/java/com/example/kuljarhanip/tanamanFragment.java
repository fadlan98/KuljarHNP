package com.example.kuljarhanip;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.kuljarhanip.ui.home.dataEksplanViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


/**
 * A simple {@link Fragment} subclass.
 */
public class tanamanFragment extends Fragment {
    private View listDataTanamanView;
    private RecyclerView listDataTanaman;
    public DatabaseReference dataTanamanRef;
    public StorageReference storageReference, imgRef;


    public tanamanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        listDataTanamanView  = inflater.inflate(R.layout.fragment_tanaman, container, false);
        listDataTanaman = listDataTanamanView.findViewById(R.id.listDataTanamanView);
        listDataTanaman.setLayoutManager(new LinearLayoutManager(getContext()));
        dataTanamanRef = FirebaseDatabase.getInstance().getReference().child("Daftar Tanaman");
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        FloatingActionButton fab2 = getActivity().findViewById(R.id.fab2);
        fab2.show();
        fab.hide();
        storageReference = FirebaseStorage.getInstance().getReference();
        // Inflate the layout for this fragment
        return listDataTanamanView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Query que = dataTanamanRef.orderByChild("namaEksplan");
        FirebaseRecyclerOptions options =
                new FirebaseRecyclerOptions.Builder<dataTanaman>()
                        .setQuery(que, dataTanaman.class).build();
        final FirebaseRecyclerAdapter<dataTanaman, dataTanamanViewHolder> adapter =
                new FirebaseRecyclerAdapter<dataTanaman, dataTanamanViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final dataTanamanViewHolder dataTanamanViewHolder, final int i, @NonNull dataTanaman dataTanaman) {
                        final String idTanaman = getRef(i).getKey();
                        dataTanamanRef.child(idTanaman).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists())
                                {
                                    if (dataSnapshot.hasChild("namaLatin" ))
                                    {

                                        final String namaTanaman = dataSnapshot.child("namaTanaman").getValue().toString();
                                        final String namaLatin = dataSnapshot.child("namaLatin").getValue().toString();
                                        final String habitat = dataSnapshot.child("habitat").getValue().toString();
                                        final String sebaran = dataSnapshot.child("sebaran").getValue().toString();
                                        final String deskripsi = dataSnapshot.child("deskripsi").getValue().toString();
                                        final String sumber = dataSnapshot.child("sumber").getValue().toString();
                                        imgRef = storageReference.child("TanamanImages").child(namaTanaman).child(namaTanaman+".jpg");
                                        imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                dataTanamanViewHolder.avi.setVisibility(View.VISIBLE);
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        dataTanamanViewHolder.avi.setVisibility(View.INVISIBLE);
                                                        Glide.with(getContext()).load(uri).into(dataTanamanViewHolder.imgTanaman);
                                                    }
                                                },2000);

                                            }
                                        });

                                        dataTanamanViewHolder.namaTanaman.setText(namaTanaman);
                                        dataTanamanViewHolder.namaLatin.setText(namaLatin);
                                        final String[] deskripsiSplit = deskripsi.split(" ");
                                        dataTanamanViewHolder.deskripsiTxt.setText(deskripsiSplit[0] +" " + deskripsiSplit[1]+" "
                                                + deskripsiSplit[2]+ " "+ deskripsiSplit[3]+ " " + deskripsiSplit[4]+ " " +
                                                deskripsiSplit[5]+ " "+ deskripsiSplit[6]+ " "+ deskripsiSplit[7]+ " " +"...");

                                        dataTanamanViewHolder.itemView.setOnClickListener(v -> {
                                            Intent i1 = new Intent(getContext(), readDataTanamanActivity.class);
                                            i1.putExtra("namaTanaman",namaTanaman);
                                            i1.putExtra("idTanaman",idTanaman);
                                            i1.putExtra("namaLatin",namaLatin);
                                            i1.putExtra("habitat",habitat);
                                            i1.putExtra("sebaran",sebaran);
                                            i1.putExtra("deskripsi",deskripsi);
                                            i1.putExtra("sumber",sumber);
                                            //i.putExtra("anakan",anakan);

                                            startActivity(i1);
                                        });

                                    }
                                }
                                else  {
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public dataTanamanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data_tanaman_layout, parent,false);
                        dataTanamanViewHolder viewHolder = new dataTanamanViewHolder(view);
                        return viewHolder;
                    }
                };
        listDataTanaman.setAdapter(adapter);
        adapter.startListening();
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL);
        listDataTanaman.addItemDecoration(itemDecoration);

    }
}
