package com.example.kuljarhanip.ui.home;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kuljarhanip.R;

public class dataEksplanViewHolder extends RecyclerView.ViewHolder {
    public TextView jenisTanaman,ketEksplan,statusEksplan;
    public TextView jenisTanamanJual,ketEksplanJual,statusEksplanJual;

    public dataEksplanViewHolder(@NonNull View itemView) {
        super(itemView);
        jenisTanaman = itemView.findViewById(R.id.namaEksplanText);
        ketEksplan = itemView.findViewById(R.id.keteranganEksplanText);
        statusEksplan = itemView.findViewById(R.id.statusEksplanText);
        jenisTanamanJual = itemView.findViewById(R.id.namaEksplanJualText);
        //ketEksplanJual = itemView.findViewById(R.id.keteranganEksplanText);
        statusEksplanJual = itemView.findViewById(R.id.statusEksplanJualText);
    }
}
