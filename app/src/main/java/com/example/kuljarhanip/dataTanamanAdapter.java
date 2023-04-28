package com.example.kuljarhanip;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class dataTanamanAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<dataEksplan> dataTanamanList = new ArrayList<>();

    public dataTanamanAdapter(Context context){
        this.context = context;
    }


    @Override
    public int getCount() {
        return getDataTanamanList().size();
    }

    @Override
    public Object getItem(int position) {
        return getDataTanamanList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;

        if(itemView == null)
        {
            //itemView = LayoutInflater.from(context).inflate(R.layout.)
        }
        return null;
    }

    public ArrayList<dataEksplan> getDataTanamanList() {
        return dataTanamanList;
    }

    public void setDataTanamanList(ArrayList<dataEksplan> dataTanamanList) {
        this.dataTanamanList = dataTanamanList;
    }
}
