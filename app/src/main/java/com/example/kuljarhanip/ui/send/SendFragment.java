package com.example.kuljarhanip.ui.send;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.kuljarhanip.R;
import com.example.kuljarhanip.loginActivity;
import com.example.kuljarhanip.ui.home.HomeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class SendFragment extends Fragment {
    public SendFragment(){

    }

    private SendViewModel sendViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sendViewModel =
                ViewModelProviders.of(this).get(SendViewModel.class);
        View root = inflater.inflate(R.layout.fragment_send, container, false);
        FloatingActionButton fab = getActivity().findViewById(R.id.fab2);
        fab.hide();
        AlertDialog.Builder alertDialog2 = new
                AlertDialog.Builder(
                getActivity());

        // Setting Dialog Title
        alertDialog2.setTitle("Confirm LogOut");

        // Setting Dialog Message
        alertDialog2.setMessage("Apakah Anda Yakin Untuk LogOut?");

        // Setting Positive "Yes" Btn
        alertDialog2.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(getActivity(),
                                loginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                });

        // Setting Negative "NO" Btn
        alertDialog2.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        //Toast.makeText(getApplicationContext(),
                               // "You clicked on NO", Toast.LENGTH_SHORT)
                               // .show();
                        HomeFragment homeFragment = new HomeFragment();
                        getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, homeFragment, homeFragment.getTag()).commit();
                        dialog.cancel();
                    }
                });

        // Showing Alert Dialog
        alertDialog2.show();
        return root;
    }

}