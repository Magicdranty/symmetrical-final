package com.example.jimmy.finall;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jimmy on 2016/5/12.
 */
public class ininadapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater = null;
    private List<DataHolder> mDataList = new ArrayList<DataHolder>();

    public ininadapter(Context context, List<DataHolder> datalist) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.forinin, null);
            holder.ininno = (TextView) convertView.findViewById(R.id.ininno);
            holder.ininimg = (ImageView) convertView.findViewById(R.id.ininimg);
            holder.inintitle = (TextView) convertView.findViewById(R.id.inintitle);
            holder.ininimg2 = (ImageView) convertView.findViewById(R.id.ininimg2);
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }

        int[] arr_pic = {R.drawable.chinese, R.drawable.english, R.drawable.math, R.drawable.science, R.drawable.socieity, R.drawable.other, R.drawable.all};
        DataHolder item = mDataList.get(position);
        holder.ininimg.setImageResource(arr_pic[item.insort - 1]);
        holder.inintitle.setText(item.intitle);
        holder.ininno.setText(String.valueOf(position + 1));
        if (item.yesno == true) {
            holder.ininimg2.setImageResource(R.drawable.gogo);
        } else {
            holder.ininimg2.setImageResource(R.drawable.nono);
        }

        return convertView;
    }

    public static class ViewHolder {
        TextView ininno, inintitle;
        ImageView ininimg, ininimg2;
    }

    public static class DataHolder {
        public String intitle, idd;
        public int insort;
        boolean yesno;
    }
}
