package com.example.jimmy.finall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jimmy on 2016/5/6.
 */
public class addtAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater = null;
    private List<DataHolder> mDataList = new ArrayList<DataHolder>();
    public addtAdapter(Context context, List<DataHolder> datalist) {
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
        return  position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.addtitem, null);
            holder.no1=(TextView)convertView.findViewById(R.id.no1);
            holder.img1=(ImageView)convertView.findViewById(R.id.img1);
            holder.title1=(TextView)convertView.findViewById(R.id.title1);
            holder.cbk=(CheckBox)convertView.findViewById(R.id.checkBox1);
            convertView.setTag(holder);
        }else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }
        int[] arr_pic = {R.drawable.chinese, R.drawable.english, R.drawable.math,  R.drawable.science,R.drawable.socieity,R.drawable.other,R.drawable.all};
        DataHolder item = mDataList.get(position);
        holder.img1.setImageResource(arr_pic[item.sort - 1]);
        holder.title1.setText(item.title1);
        holder.no1.setText(String.valueOf(position + 1));
        holder.cbk.setChecked(item.type);
        return convertView;
    }
    public static class ViewHolder
    {
        TextView title1,no1;
        ImageView img1;
        CheckBox cbk;
    }
    public static class DataHolder
    {
        public String title1,idd;
        public int sort;
        boolean type;
    }
}
