package com.example.jimmy.finall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ininreal extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    final List<ininadapter.DataHolder> items = new ArrayList<>();
    DrawerLayout drawerLayout;
    ListView lv;
    String account, email;
    private ininadapter inadt;
    Spinner sp, sp1;//sp for user sp1 for sort
    int pos, posu;
    NavigationView view;
    ImageView img;
    /////////////////////////////
    String pinforaccess;
    Socket soc;
    connectuse connect;
    ////////////////////////////
    SharedPreferences settings;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ininreal);
        settings = getSharedPreferences("teacheruse_pref", 0);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sp = (Spinner) findViewById(R.id.inspinner);
        sp1 = (Spinner) findViewById(R.id.inspinner1);
        sp.setOnItemSelectedListener(this);
        sp1.setOnItemSelectedListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        view = (NavigationView) findViewById(R.id.navigation_view);
        view.getMenu().findItem(R.id.navigation_item_2).setChecked(true);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1:
                        Intent it = new Intent(ininreal.this, inrealtime.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(it);
                        break;
                    case R.id.navigation_item_2:
                        Toast.makeText(ininreal.this, "已經在及時測驗內", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_item_3:
                        Intent it3 = new Intent(ininreal.this, forgrade.class);
                        it3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(it3);
                        break;
                    case R.id.navigation_item_4:
                        Intent it4 = new Intent(ininreal.this, ballot.class);
                        it4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(it4);
                        break;
                    case R.id.navigation_item_5:
                        break;
                }
                return true;
            }
        });
        ////以下是更新帳戶名
        if (view.getHeaderCount() > 0) {
            View header = view.getHeaderView(0);
            TextView tv = (TextView) header.findViewById(R.id.textView2);
            TextView tv2 = (TextView) header.findViewById(R.id.name);
            connectuse x = (connectuse) ininreal.this.getApplication();
            tv2.setText(settings.getString("account", "XXX"));
            tv.setText(settings.getString("email", "XXX"));
            img = (ImageView) header.findViewById(R.id.profile_image);
            img.setImageBitmap(x.b);
        }
///
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        ///
        //buff();
        lv = (ListView) findViewById(R.id.inlistView);
        inadt = new ininadapter(ininreal.this, items);
        lv.setAdapter(inadt);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!items.get(position).count.equals("0")) {
                    readytoin(items.get(position).pft);
                } else {
                    Toast.makeText(ininreal.this, "請選擇有內容的考卷", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ////
    }

    private void getdata() {
        String result;
        if (pos != 0 && posu != 0) {
            result = DBConnector.executeQuery("select numb,title,editor,sort,KEYIN from testlist where sort='" + pos + "' and editor='" + account + "' order by sort");
        } else if (pos != 0 && posu == 0) {
            result = DBConnector.executeQuery("select num,title,editor,sort,KEYIN from testlist where sort='" + pos + "'");
        } else if (pos == 0 && posu != 0) {
            result = DBConnector.executeQuery("select  num,title,editor,sort,KEYIN from testlist where editor='" + account + "' order by sort");
        } else {
            result = DBConnector.executeQuery("select  num,title,editor,sort,KEYIN from testlist order by sort");
        }
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                ininadapter.DataHolder item = new ininadapter.DataHolder();
                item.intitle = jsonData.getString("title");
                item.insort = jsonData.getInt("sort");//只會有1~6
                item.idd = jsonData.getString("num");
                item.editor = jsonData.getString("editor");
                item.pft = jsonData.getString("KEYIN");
                String temp = DBConnector.executeQuery("SELECT COUNT(*) from testinside where testinside.testtitleid='" + jsonData.getString("num") + "'");
                JSONArray jsonArray1 = new JSONArray(temp);
                JSONObject jsonData1 = jsonArray1.getJSONObject(0);
                item.count = jsonData1.getString("COUNT(*)");
                items.add(item);
            }
        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    connectuse x = (connectuse) ininreal.this.getApplication();
                    img.setImageBitmap(x.b);
                }
            });
        }
    }

    public void headclick(View v) {
        drawerLayout.closeDrawers();
        Intent it = new Intent(ininreal.this, fixhead.class);
        startActivityForResult(it, 0);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.inspinner:
                posu = position;
                break;
            case R.id.inspinner1:
                pos = position;
                break;
        }
        Log.e("@@@@!", String.valueOf(position));
        renew();
    }
    public void renew() {//刷新ADAPTER
        Log.e("RENNNEWWW", "!!!!!!!!!");
        items.clear();
        getdata();
        inadt = new ininadapter(this, items);
        lv.setAdapter(inadt);
        inadt.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void readytoin(final String p) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String buff;
                while (true) {
                    buff = forrand();
                    String ss = DBConnector.executeQuery("select testnum from record where pinforaccess='" + buff + "'");
                    if (ss.contains("null")) {
                        pinforaccess = buff;
                        Log.e("!!!!!", "create unique pinforaccess");
                        break;
                    }
                }
                connect = (connectuse) ininreal.this.getApplication();
                connect.init();//
                soc = connect.getSocket();
                if (soc.isConnected()) {
                    Log.e("CANINTIN", "OK");
                    handler.obtainMessage(1, p).sendToTarget();
                } else {
                    Log.e("CANNOTIN", "NOOK");
                    handler.obtainMessage(2).sendToTarget();
                }
            }
        }).start();
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Intent it = new Intent(ininreal.this, ininwait.class);
                    it.putExtra("pfa", pinforaccess);
                    it.putExtra("pft", msg.obj.toString());
                    startActivity(it);
                    break;
                case 2:
                    Toast.makeText(ininreal.this, "server no response", Toast.LENGTH_SHORT).show();
                    break;
                case 3:

                    break;
            }
        }
    };

    public String forrand() {
        StringBuffer xs = new StringBuffer();
        int x = (int) ((Math.random() * 7) % 4);//0~3 數字個數
        for (int i = 0; i < 4; i++) {
            int s = (int) ((Math.random() * 10) % 2);//0 代表抓數字
            if (s == 0 && x != 0) {
                xs.append((char) ((int) ((Math.random() * 11) % 10) + 48));
                x--;
            } else if (s == 1) {
                xs.append((char) ((int) (((Math.random() * 26) + 65))));
            } else {
                i--;
            }
        }
        return xs.toString();

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {   //確定按下退出鍵and防止重複按下退出鍵
            Intent it = new Intent(ininreal.this, inrealtime.class);
            it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(it);
        }
        return false;
    }
}
