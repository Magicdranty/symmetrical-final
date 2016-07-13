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
 * Created by jimmy on 2016/5/2.
 */
public class addqAdapter  extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater = null;
    private List<DataHolder> mDataList = new ArrayList<DataHolder>();
    private View.OnClickListener mDelClickListener;
    private addqMyLinearLayout.OnScrollListener mScrollListener;
    public addqAdapter(Context context, List<DataHolder> datalist,View.OnClickListener delClickListener, addqMyLinearLayout.OnScrollListener listener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        if (datalist != null && datalist.size() > 0) {
            mDataList.addAll(datalist);
        }
        mDelClickListener = delClickListener;
        mScrollListener = listener;
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
            convertView = inflater.inflate(R.layout.addqitem, null);
            holder.no1=(TextView)convertView.findViewById(R.id.no1);
            holder.del=(TextView)convertView.findViewById(R.id.del);
            holder.fix=(TextView)convertView.findViewById(R.id.fix);
            holder.img1=(ImageView)convertView.findViewById(R.id.img1);
            holder.title1=(TextView)convertView.findViewById(R.id.title1);
            convertView.setTag(holder);
        }else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }
        int[] arr_pic = {R.drawable.chinese, R.drawable.english, R.drawable.math, R.drawable.science,R.drawable.socieity,R.drawable.other,R.drawable.all};
    // arr_pic[6]是 all 只有測驗的sort有"不分類" 題目沒有不分類 題目的sort只有1~6
        DataHolder item = mDataList.get(position);
        holder.img1.setImageResource(arr_pic[item.sort-1]);
        holder.title1.setText(item.title1);
        holder.no1.setText(String.valueOf(position + 1));
        item.rootView = (addqMyLinearLayout)convertView.findViewById(R.id.lin_root);
        item.rootView.scrollTo(0, 0);
        item.rootView.setOnScrollListener(mScrollListener);
        holder.del.setOnClickListener(mDelClickListener);
        holder.fix.setOnClickListener(mDelClickListener);
        return convertView;
    }
    public static class ViewHolder
    {
        TextView title1,no1,del,fix;
        ImageView img1;
    }
    public static class DataHolder
    {
        public String title1,idd,editor;
        public int sort;
        public addqMyLinearLayout rootView;
    }
    public void removeItem(int position){
        mDataList.remove(position);
        notifyDataSetChanged();
    }
}
