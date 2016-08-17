package com.example.jimmy.finall;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.util.LruCache;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ininranking extends AppCompatActivity {
    int max = 0;
    int now = 0;
    String pfa;
    //List<adapterforrank.DataHolder> items = new ArrayList<>();
    HandlerThread mHandlerThread;
    Handler mHandler;
    MyAdapter mAdapter;
    ListView lv;
    List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
    private LruCache<String, Bitmap> mLruCache;
    //private adapterforrank adt;
    Socket soc;
    connectuse con;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ininranking);
        settings = getSharedPreferences("teacheruse_pref", 0);
        Bundle bd = getIntent().getExtras();
        con = (connectuse) ininranking.this.getApplication();
        max = bd.getInt("max");
        now = bd.getInt("now");
        pfa = bd.getString("pfa");
        TextView maxtext = (TextView) findViewById(R.id.max);
        maxtext.setText(String.valueOf(max));
        TextView numtext = (TextView) findViewById(R.id.num);
        numtext.setText(String.valueOf(now));
///
        mHandlerThread = new HandlerThread("LRU Cache Handler");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 2;

        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
        getdata();
        lv = (ListView) findViewById(R.id.listView);
        mAdapter = new MyAdapter();
        lv.setAdapter(mAdapter);
        /////////
        soc = con.getSocket();
    }

    public void getdata() {
        String result = DBConnector.executeQuery("select  saccount,grade  from buffer where testpfa='" + pfa + "' order by grade desc");
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("score", jsonData.getInt("grade"));
                map.put("studentid", jsonData.getString("saccount"));
                map.put("url", "http://" + settings.getString("net", "XXX") + "/uploads/s" + jsonData.getString("saccount") + ".png");
                lists.add(map);
            }
        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }
    }

    public void next(View v) {
        try {
            connectuse con = (connectuse) ininranking.this.getApplication();
            BufferedWriter bwt = con.getwrite();
            BufferedReader brt = con.getread();
            if (max > now) {
                bwt.write("continue\n");
                bwt.flush();
                finish();
            } else {
                Toast.makeText(ininranking.this, "TEST OVER", Toast.LENGTH_SHORT).show();
                bwt.write("end\n");
                bwt.flush();
                DBConnector.executeQuery("insert into grade(grade,saccount,testpfa) SELECT grade,saccount,testpfa from buffer where testpfa='" + pfa + "'");
                DBConnector.executeQuery("delete from buffer where testpfa='" + pfa + "'");
                soc.close();
                Log.e("SOC", "CLOSE");
                Intent it = new Intent(ininranking.this, ininreal.class);
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(it);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {   //確定按下退出鍵and防止重複按下退出鍵
            dia();
        }
        return false;
    }

    public void dia() {
        new AlertDialog.Builder(ininranking.this)
                .setTitle("警告")
                .setMessage("是否要離開測驗")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBConnector.executeQuery("delete from buffer where testpfa='" + pfa + "'");
                        DBConnector.executeQuery("delete from record where  pinforaccess='" + pfa + "'");
                        try {
                            soc.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Intent it = new Intent(ininranking.this, MainActivity.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(it);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();

    }

    //////////////////
    private class MyAdapter extends BaseAdapter {
        private Map<String, String> mLoadingMap;

        public MyAdapter() {
            mLoadingMap = new HashMap<String, String>();
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = convertView;
            final Holder holder;
            if (null == v) {
                v = LayoutInflater.from(ininranking.this).inflate(R.layout.itemforrank, null);
                holder = new Holder();
                holder.no = (TextView) v.findViewById(R.id.ininno);
                holder.img = (ImageView) v.findViewById(R.id.ininimg);
                holder.title = (TextView) v.findViewById(R.id.inintitle);
                holder.score = (TextView) v.findViewById(R.id.score);
                v.setTag(holder);
            } else {
                holder = (Holder) v.getTag();
            }
            holder.img.setImageResource(R.drawable.zzz);
            holder.title.setText(lists.get(position).get("studentid").toString());
            holder.no.setText(String.valueOf(position + 1));
            holder.score.setText(lists.get(position).get("score").toString());
            final String key = position + "_cache";
            Bitmap b = mLruCache.get(key);
            if (b == null && !mLoadingMap.containsKey(key)) {
                mLoadingMap.put(key, lists.get(position).get("url").toString());
                Log.e("lru", "load pic" + position);
                mHandler.post(new Runnable() {
                    Bitmap bmp;

                    @Override
                    public void run() {
                        bmp = decodeBitmap(lists.get(position).get("url").toString(), 200);
                        if (bmp != null) {
                            mLruCache.put(key, bmp);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    notifyDataSetChanged();
                                    mLoadingMap.remove(key);
                                }
                            });
                        }

                    }
                });
            } else {
                Log.e("lru", "cache");
                holder.img.setImageBitmap(b);
            }
            return v;
        }

        class Holder {
            TextView no, title, score;
            ImageView img;
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeBitmap(String url, int maxWidth) {
        Bitmap bitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxWidth);
            InputStream is = (InputStream) new URL(url).getContent();
            bitmap = BitmapFactory.decodeStream(is, null, options);
        } catch (MalformedInputException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}


