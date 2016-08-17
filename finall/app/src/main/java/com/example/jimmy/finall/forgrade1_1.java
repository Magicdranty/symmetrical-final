package com.example.jimmy.finall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class forgrade1_1 extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ListView lv;
    private forgradeadapter1_1 adt;
    ImageView img;
    NavigationView view;
    String pfa;
    final List<forgradeadapter1_1.DataHolder> items = new ArrayList<>();

    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgrade1_1);
        settings = getSharedPreferences("teacheruse_pref", 0);
        Intent it = getIntent();
        pfa = it.getStringExtra("pfa");

        //////////////////////////////////////
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        view = (NavigationView) findViewById(R.id.navigation_view);
        view.getMenu().findItem(R.id.navigation_item_3).setChecked(true);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Toast.makeText(forgrade1_1.this, menuItem.getTitle() + " pressed", Toast.LENGTH_LONG).show();
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1:
                        Intent it = new Intent(forgrade1_1.this, inrealtime.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(it);
                        break;
                    case R.id.navigation_item_2:
                        Intent its = new Intent(forgrade1_1.this, ininreal.class);
                        its.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(its);
                        break;
                    case R.id.navigation_item_3:
                        Toast.makeText(forgrade1_1.this, "已經在成機查詢系統內", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_item_4:
                        Intent it4 = new Intent(forgrade1_1.this, ballot.class);
                        it4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(it4);
                        break;
                    case R.id.navigation_item_5: break;

                }
                return true;
            }
        });
        ////以下是更新帳戶名
        if (view.getHeaderCount() > 0) {
            View header = view.getHeaderView(0);
            TextView tv = (TextView) header.findViewById(R.id.textView2);
            TextView tv2 = (TextView) header.findViewById(R.id.name);
            tv.setText(settings.getString("email","XXX"));
            tv2.setText(settings.getString("account","XXX"));
            img=(ImageView)header.findViewById(R.id.profile_image);
            connectuse x = (connectuse) forgrade1_1.this.getApplication();
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
        lv = (ListView) findViewById(R.id.listView);
        getdata();
        adt = new forgradeadapter1_1(this, items);
        lv.setAdapter(adt);
    }

    private void getdata() {
        String result;
        result = DBConnector.executeQuery("select grade,saccount from grade where testpfa='" + pfa + "' order by grade");
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                final forgradeadapter1_1.DataHolder item = new forgradeadapter1_1.DataHolder();
                item.saccount = jsonData.getString("saccount");
                item.score = jsonData.getString("grade");


                String imageUrl = ("http://" + settings.getString("net","XXX") + "/uploads/" + "s" + item.saccount + ".png");
                try {
                    Log.e("!@!@!@", imageUrl);
                    URL url = new URL(imageUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    int status = connection.getResponseCode();
                    if (status == 404) {
                        Resources res = getResources();
                        item.head = BitmapFactory.decodeResource(res, R.drawable.zzz);
                    } else {
                        InputStream input = connection.getInputStream();
                        final Bitmap b = BitmapFactory.decodeStream(input);
                        item.head = b;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                items.add(item);


            }
        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgrade1_1, menu);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    connectuse x = (connectuse) forgrade1_1.this.getApplication();
                    img.setImageBitmap(x.b);
                }
            });
        }
    }

    public void headclick(View v) {
        drawerLayout.closeDrawers();
        Intent it = new Intent(forgrade1_1.this, fixhead.class);
        startActivityForResult(it, 0);
    }
}
