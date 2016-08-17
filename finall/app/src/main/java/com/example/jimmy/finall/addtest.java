package com.example.jimmy.finall;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class addtest extends AppCompatActivity implements addqMyLinearLayout.OnClickListener, addqMyLinearLayout.OnScrollListener, AdapterView.OnItemSelectedListener {
    final List<addqAdapter.DataHolder> items = new ArrayList<>();
    DrawerLayout drawerLayout;
    private addqMyListView listView;
    private addqMyLinearLayout mLastScrollView;
    private addqAdapter addqAdapter;
    String account, email;
    Spinner sp, spuser;//sp選擇分類 spuser 選擇只看自己的或是全部人的
    int pos, posu;//pos分類的 posu編輯者
    ImageView img;NavigationView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtest);
        ///////spinner
        sp = (Spinner) findViewById(R.id.spinner3);
        spuser = (Spinner) findViewById(R.id.spinneru);
        sp.setOnItemSelectedListener(this);
        spuser.setOnItemSelectedListener(this);
        /////
        listView = (addqMyListView) findViewById(R.id.listview);
        getdata();
        addqAdapter = new addqAdapter(this, items, this, this);
        listView.setAdapter(addqAdapter);
        ////
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ////
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
       view = (NavigationView) findViewById(R.id.navigation_view);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Toast.makeText(addtest.this, menuItem.getTitle() + " pressed", Toast.LENGTH_LONG).show();
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1:
                        Intent it = new Intent(addtest.this,inrealtime.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(it);
                        break;
                    case R.id.navigation_item_2:
                        Intent its = new Intent(addtest.this, ininreal.class);
                        its.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(its);
                        break;
                    case R.id.navigation_item_3:
                        break;
                    case R.id.navigation_item_4:
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
            connectuse x=(connectuse)addtest.this.getApplication();
            SharedPreferences settings = getSharedPreferences("teacheruse_pref", 0);
            tv.setText(settings.getString("email","XXX"));
            tv2.setText(account=settings.getString("account","XXX"));
            img=(ImageView)header.findViewById(R.id.profile_image);
            img.setImageBitmap(x.b);
        }
        //////
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
        //////////////
    }

    private void getdata() {
        String result;
        if (pos != 0 && posu != 0) {
            result = DBConnector.executeQuery("select num,title,sort from testlist  where sort='" + pos + "' and editor='" + account + "' order by sort");
        } else if (pos != 0 && posu == 0) {
            result = DBConnector.executeQuery("select num,title,sort,editor from testlist  where sort='" + pos + "' order by sort");
        } else if (pos == 0 && posu != 0) {
            result = DBConnector.executeQuery("select num,title,sort from testlist  where editor='" + account + "' order by sort");
        } else {
            result = DBConnector.executeQuery("select num,title,sort,editor from testlist order by sort");//不分類
        }
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                addqAdapter.DataHolder item = new addqAdapter.DataHolder();
                item.title1 = jsonData.getString("title");
                item.sort = jsonData.getInt("sort");//1~7 7是不分類的測驗
                item.idd = jsonData.getString("num");
                if (posu == 0) {
                    item.editor = jsonData.getString("editor");
                } else {
                    item.editor = account;
                }
                items.add(item);
            }
            Log.e("!", items.toString());
        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }

    }

    public void renew() {//刷新ADAPTER
        items.clear();
        getdata();
        addqAdapter = new addqAdapter(this, items, this, this);
        listView.setAdapter(addqAdapter);
        addqAdapter.notifyDataSetChanged();
    }


    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.del:
                Toast.makeText(addtest.this, "DEL", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(addtest.this);
                builder.setMessage("確認刪除？");
                builder.setTitle("提示");
                builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(addtest.this, "OK", Toast.LENGTH_SHORT).show();
                        int position = listView.getPositionForView(v);
                        addqAdapter.removeItem(position);
                        String x=DBConnector.executeQuery("delete from testinside where testtitleid =" + items.get(position).idd);

                        String y=DBConnector.executeQuery("delete from testlist  where num =" + items.get(position).idd);
                        Log.e("a",x.toString());
                        Log.e("aa",y.toString());

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(addtest.this, "cancel", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create().show();
                break;
            case R.id.fix:
                if (account.equals(items.get(listView.getPositionForView(v)).editor)) {//編輯者與現在用戶同樣
                    Toast.makeText(addtest.this, "FiX", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(this, addtest1.class);
                    it.putExtra("sort", String.valueOf(items.get(listView.getPositionForView(v)).sort));
                    it.putExtra("type", "fix");
                    it.putExtra("testid", items.get(listView.getPositionForView(v)).idd);
                    startActivity(it);
                } else {
                    Toast.makeText(this, "非原編輯者,無法修改", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void fabc(View v) {

        Toast.makeText(addtest.this, "FAB", Toast.LENGTH_SHORT).show();
        final View vv = LayoutInflater.from(addtest.this).inflate(R.layout.addtuse, null);
        new AlertDialog.Builder(addtest.this)
                .setTitle("ADD TEST")
                .setView(vv)
                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int sort;
                                EditText edittitle = (EditText) vv.findViewById(R.id.titlesss);
                                Spinner sortsp = (Spinner) vv.findViewById(R.id.spinner4);
                                String buff;
                                //先確認名稱沒有重複
                                if ((DBConnector.executeQuery("select num from testlist where title='" + edittitle.getText().toString() + "'")).contains(("null"))) {
                                    while(true)
                                    {/////////這邊卻認KEYIN 沒有跟其他測驗重複
                                        buff=forrand();
                                        String ss = DBConnector.executeQuery("select KEYIN from testlist where KEYIN='" +buff + "'");
                                        if (ss.contains("null")) {
                                            Log.e("!!!!!", "create unique pinforaccess");
                                            break;
                                        }
                                    }
                                    if (sortsp.getSelectedItemPosition() == 0) {
                                        sort = 7;
                                    } else sort = sortsp.getSelectedItemPosition();
                                    String res = DBConnector.executeQuery("insert into testlist(editor,sort,title,KEYIN,situation) values ('" + account + "','" + sort + "','" + edittitle.getText().toString() + "','" + buff + "','0')");
                                    Intent it = new Intent(addtest.this, addtest1.class);///測驗名稱不能一樣
                                    if (String.valueOf(sortsp.getSelectedItemPosition()).equals("0")) {
                                        it.putExtra("sort", "7");
                                    } else {
                                        it.putExtra("sort", String.valueOf(sortsp.getSelectedItemPosition()));
                                    }
                                    String xx=DBConnector.executeQuery("select num from testlist where KEYIN='" + buff + "'");
                                    try {
                                        JSONArray jsonArray = new JSONArray(xx);
                                        JSONObject jsonData = jsonArray.getJSONObject(0);
                                        it.putExtra("testid", jsonData.getString("num"));
                                        Log.e("ohooho", jsonData.getString("num"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    it.putExtra("type", "add");
                                    startActivity(it);
                                } else {
                                    Toast.makeText(addtest.this, "名稱重複，請重新編輯", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                )
                .setNegativeButton("back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }
                )
                .show();
    }

    @Override
    public void OnScroll(addqMyLinearLayout view) {
        if (mLastScrollView != null) {
            mLastScrollView.smoothScrollTo(0, 0);
        }
        mLastScrollView = view;
    }

    @Override
    public void onResume(){
        super.onResume();
       renew();
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner3:
                pos = position;
                break;
            case R.id.spinneru:
                posu = position;
                break;
        }
        renew();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    connectuse x = (connectuse) addtest.this.getApplication();
                    img.setImageBitmap(x.b);
                }
            });
        }
    }
    public void headclick(View v) {
        drawerLayout.closeDrawers();
        Intent it = new Intent(addtest.this, fixhead.class);
        startActivityForResult(it, 0);
    }


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
        return  xs.toString();

    }
}
