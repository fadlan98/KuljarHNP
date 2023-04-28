package com.example.kuljarhanip;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wang.avi.AVLoadingIndicatorView;

public class dataTanamanViewHolder extends RecyclerView.ViewHolder {
    public TextView namaTanaman,deskripsiTxt,namaLatin;
    public ImageView imgTanaman;
    public AVLoadingIndicatorView avi;
    public LinearLayout layout;

    public dataTanamanViewHolder(@NonNull View itemView) {
        super(itemView);
        namaTanaman = itemView.findViewById(R.id.namaTanamanText);
        deskripsiTxt = itemView.findViewById(R.id.deskripsiText);
        namaLatin = itemView.findViewById(R.id.namaLatinText);
        imgTanaman = itemView.findViewById(R.id.imageViewTanaman);
        avi = itemView.findViewById(R.id.avi2);
        avi.setIndicator("PacmanIndicator");
        layout = itemView.findViewById(R.id.layoutTanaman);
        layout.setClipToOutline(true);
        imgTanaman.setClipToOutline(true);
    }
}
