package com.example.kuljarhanip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class dataEksplanAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<dataEksplan> dataEksplanList = new ArrayList<>();


    public dataEksplanAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return dataEksplanList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataEksplanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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

    public ArrayList<dataEksplan> getDataEksplanList() {
        return dataEksplanList;
    }

    public void setDataEksplanList(ArrayList<dataEksplan> dataEksplanList) {
        this.dataEksplanList = dataEksplanList;
    }
}
