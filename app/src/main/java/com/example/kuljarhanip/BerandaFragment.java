package com.example.kuljarhanip;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kuljarhanip.ui.gallery.GalleryFragment;
import com.example.kuljarhanip.ui.home.HomeFragment;
import com.example.kuljarhanip.ui.slideshow.SlideshowFragment;
import com.example.kuljarhanip.ui.tools.ToolsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class BerandaFragment extends Fragment {

    public View beranda;
    public View listEkpsplanLayout;
    public View scanQRLayout;
    public View daftarKontamLayout;
    public View daftarJualLayout;
    public Button listEkpsplanBtn, scanQRBtn, daftarKontamBtn,daftarJualBtn;

    public BerandaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_beranda, container, false);
        listEkpsplanLayout = root.findViewById(R.id.layoutLE);
        scanQRLayout = root.findViewById(R.id.layoutSCQR);
        daftarKontamLayout = root.findViewById(R.id.layoutdaftarKontam);
        daftarJualLayout = root.findViewById(R.id.layoutdaftarJual);
        listEkpsplanBtn = root.findViewById(R.id.btnListEksplan);
        scanQRBtn = root.findViewById(R.id.btnSCQREksplan);
        daftarKontamBtn = root.findViewById(R.id.btndaftarKontam);
        daftarJualBtn = root.findViewById(R.id.btndaftarJual);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab2);
        fab.hide();
        FloatingActionButton fab2 = getActivity().findViewById(R.id.fab);
        fab2.hide();


        listEkpsplanLayout.setOnClickListener(v -> {
            HomeFragment homeFragment = new HomeFragment();
            getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, homeFragment, homeFragment.getTag()).addToBackStack(null).commit();

        });
        scanQRLayout.setOnClickListener(v -> {
            GalleryFragment galleryFragment = new GalleryFragment();
            getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, galleryFragment, galleryFragment.getTag()).addToBackStack(null).commit();

        });
        daftarKontamLayout.setOnClickListener(v -> {
            SlideshowFragment slideshowFragment = new SlideshowFragment();
            getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, slideshowFragment, slideshowFragment.getTag()).addToBackStack(null).commit();
        });
        daftarJualLayout.setOnClickListener(v -> {
            ToolsFragment toolsFragment = new ToolsFragment();
            getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, toolsFragment, toolsFragment.getTag()).addToBackStack(null).commit();
        });

        listEkpsplanBtn.setOnClickListener(v -> {
            HomeFragment homeFragment = new HomeFragment();
            getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, homeFragment, homeFragment.getTag()).addToBackStack(null).commit();

        });
        scanQRBtn.setOnClickListener(v -> {
            GalleryFragment galleryFragment = new GalleryFragment();
            getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, galleryFragment, galleryFragment.getTag()).addToBackStack(null).commit();

        });
        daftarKontamBtn.setOnClickListener(v -> {
            SlideshowFragment slideshowFragment = new SlideshowFragment();
            getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, slideshowFragment, slideshowFragment.getTag()).addToBackStack(null).commit();
        });
        daftarJualBtn.setOnClickListener(v -> {
            ToolsFragment toolsFragment = new ToolsFragment();
            getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, toolsFragment, toolsFragment.getTag()).addToBackStack(null).commit();
        });
        // Inflate the layout for this fragment
        return root;
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.action_search);
        if(item!=null)
            item.setVisible(true);
        MenuItem item1=menu.findItem(R.id.action_searchbyNama);
        if(item!=null)
            item1.setVisible(false);
        MenuItem item2=menu.findItem(R.id.action_searchStatus);
        if(item!=null)
            item2.setVisible(false);
    }
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Home");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search)
        {
            HomeFragment homeFragment = new HomeFragment();
            getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, homeFragment, homeFragment.getTag()).addToBackStack(null).commit();
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
