package com.example.jimmy.finall;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ininreal);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sp = (Spinner) findViewById(R.id.inspinner);
        sp1 = (Spinner) findViewById(R.id.inspinner1);
        sp.setOnItemSelectedListener(this);
        sp1.setOnItemSelectedListener(this);
        ///
        lv = (ListView) findViewById(R.id.inlistView);
        inadt = new ininadapter(this, items);
        lv.setAdapter(inadt);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ininadapter.ViewHolder holder = (ininadapter.ViewHolder) view.getTag();
                if (items.get(position).yesno == true) {
                    DBConnector.executeQuery("update testlist set situation='0' where num='" + items.get(position).idd + "'");
                    items.get(position).yesno = false;
                } else {
                    DBConnector.executeQuery("update testlist set situation='1' where num='" + items.get(position).idd + "'");
                    items.get(position).yesno = true;
                }
                renew();
            }
        });
        ////
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.getMenu().findItem(R.id.navigation_item_2).setChecked(true);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Toast.makeText(ininreal.this, menuItem.getTitle() + " pressed", Toast.LENGTH_LONG).show();
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1:
                        Intent it = new Intent(ininreal.this, list.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(it);
                        break;
                    case R.id.navigation_item_2:
                        Toast.makeText(ininreal.this, "已經在及時測驗內", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_item_3:
                        break;
                    case R.id.navigation_item_4:
                        break;
                    case R.id.navigation_item_5:
                        break;
                    case R.id.navigation_item_6:
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
            tv2.setText(account = x.accountname);
            tv.setText(email = x.email);
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
    }

    private void getdata() {
        String result;
        if (pos != 0 && posu != 0) {
            result = DBConnector.executeQuery("select numb,title,sort,situation from testlist where sort='" + pos + "' and editor='" + account + "' order by sort");
        } else if (pos != 0 && posu == 0) {
            result = DBConnector.executeQuery("select num,title,sort,situation from testlist where sort='" + pos + "'");
        } else if (pos == 0 && posu != 0) {
            result = DBConnector.executeQuery("select  num,title,sort,situation from testlist where editor='" + account + "' order by sort");
        } else {
                result = DBConnector.executeQuery("select  num,title,sort,situation from testlist order by sort");
        }
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                ininadapter.DataHolder item = new ininadapter.DataHolder();
                item.intitle = jsonData.getString("title");
                item.insort = jsonData.getInt("sort");//只會有1~6
                item.idd = jsonData.getString("num");
                if (jsonData.getString("situation").equals("1")) {
                    item.yesno = true;
                } else item.yesno = false;
                items.add(item);
            }
        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ininreal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        Log.e("@@@@!",String .valueOf(position));
        renew();
    }

    public void renew() {//刷新ADAPTER
        Log.e("RENNNEWWW","!!!!!!!!!");
        items.clear();
        getdata();
        inadt = new ininadapter(this, items);
        lv.setAdapter(inadt);
        inadt.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
