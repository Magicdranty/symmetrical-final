package com.example.jimmy.finall;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class forgradeadapter1_1 extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater = null;
    private List<DataHolder> mDataList = new ArrayList<DataHolder>();
    public  forgradeadapter1_1(Context context,List<forgradeadapter1_1.DataHolder> datalist)
    {
        this.context = context;
        inflater = LayoutInflater.from(context);
        if (datalist != null && datalist.size() > 0) {
            mDataList.addAll(datalist);
        }
    }
    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.forgradeitem1_1, null);
            holder.acc = (TextView) convertView.findViewById(R.id.acc);
            holder.sccore =(TextView) convertView.findViewById(R.id.score);
            holder.img=(ImageView)convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }
        DataHolder item = mDataList.get(position);
        holder.acc.setText(item.saccount);
        holder.sccore.setText(item.score);
        holder.img.setImageBitmap(item.head);



        return convertView;
    }
    public static class ViewHolder {
        TextView acc, sccore;ImageView img;
    }
    public static  class DataHolder {
        String saccount,score;Bitmap head;
    }
}
