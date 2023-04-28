package com.example.kuljarhanip;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kuljarhanip.ui.home.RecyclerItemClickListener;
import com.example.kuljarhanip.ui.home.dataEksplanViewHolder;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private Context mcontext;
    private List<dataEksplan> listDataEksplan;
    private OnItemClickListener mlistener;

    public RecyclerAdapter(Context context, List<dataEksplan> dataList){
        mcontext = context;
        listDataEksplan = dataList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.list_data_eksplan_layout, parent,false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        dataEksplan currentDataEksplan = listDataEksplan.get(position);
        holder.jenisTanaman.setText(currentDataEksplan.getJenisTanaman());
        holder.ketEksplan.setText(currentDataEksplan.getKeterangan());
        holder.statusEksplan.setText(currentDataEksplan.getStatus());
    }

    @Override
    public int getItemCount() {
        return listDataEksplan.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView jenisTanaman,ketEksplan,statusEksplan;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            jenisTanaman = itemView.findViewById(R.id.namaEksplanText);
            ketEksplan = itemView.findViewById(R.id.keteranganEksplanText);
            statusEksplan = itemView.findViewById(R.id.statusEksplanText);
        }



        @Override
        public void onClick(View v) {
            if (mlistener != null)
            {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION)
                    mlistener.onItemClick(position);
            }
        }

    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mlistener = listener;
    }
}
