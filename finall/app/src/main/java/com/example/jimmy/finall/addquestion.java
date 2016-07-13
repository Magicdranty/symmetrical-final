package com.example.jimmy.finall;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class addquestion extends AppCompatActivity implements addqMyLinearLayout.OnClickListener, addqMyLinearLayout.OnScrollListener {
    final List<addqAdapter.DataHolder> items = new ArrayList<>();
    private addqMyListView listView;
    private addqMyLinearLayout mLastScrollView;
    private addqAdapter addqAdapter;
    DrawerLayout drawerLayout;
    Spinner sp;
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addquestion);
        ///////spinner
        sp = (Spinner) findViewById(R.id.spinner3);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos = position;//0=>不分 1 國文類 ....
                renew();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        /////
        listView = (addqMyListView) findViewById(R.id.listview);
        getdata();
        addqAdapter = new addqAdapter(this, items, this, this);
        listView.setAdapter(addqAdapter);
        /////
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ///
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.getMenu().findItem(R.id.navigation_item_2).setChecked(true);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Toast.makeText(addquestion.this, menuItem.getTitle() + " pressed", Toast.LENGTH_LONG).show();
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1:
                        Intent it = new Intent(addquestion.this, list.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(it);
                        break;
                    case R.id.navigation_item_2:
                        Toast.makeText(addquestion.this, "已經在及時測驗內", Toast.LENGTH_SHORT).show();
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
            connectuse x=(connectuse)addquestion.this.getApplication();
            tv2.setText(x.accountname);
            tv.setText(x.email);
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
        int x = sp.getSelectedItemPosition();
        String result;
        if (x != 0) {
            result = DBConnector.executeQuery("select id,sort,question from question where sort='" + sp.getSelectedItemPosition() + "'");
        } else {
            result = DBConnector.executeQuery("select id,sort,question from question");
        }
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                addqAdapter.DataHolder item = new addqAdapter.DataHolder();
                item.title1 = jsonData.getString("question");
                item.sort = jsonData.getInt("sort");//只會有1~6
                item.idd = jsonData.getString("id");
                items.add(item);
            }
        } catch (Exception e) {
            Log.e("log_tag", e.toString());
        }

    }

    @Override
    public void onClick(final View v) {

        switch (v.getId()) {
            case R.id.del:
                Toast.makeText(addquestion.this, "DEL", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(addquestion.this);
                builder.setMessage("確認刪除？");
                builder.setTitle("提示");
                builder.setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(addquestion.this, "OK", Toast.LENGTH_SHORT).show();
                        int position = listView.getPositionForView(v);
                        addqAdapter.removeItem(position);
                        String x = DBConnector.executeQuery("delete from question where id =" + items.get(position).idd);

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Toast.makeText(addquestion.this, "cancel", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create().show();
                break;
            case R.id.fix:
                Toast.makeText(addquestion.this, "FiX", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(addquestion.this, addquestion1.class);
                it.putExtra("id", items.get(listView.getPositionForView(v)).idd);
                it.putExtra("fixoradd",false);
                startActivity(it);
                break;
        }
    }
    public void fabc(View v) {
        Intent it = new Intent(addquestion.this, addquestion1.class);
        it.putExtra("fixoradd",true);
        startActivity(it);
    }
    @Override
    public void OnScroll(addqMyLinearLayout view) {
        //將上一個被拉出來的收回去
        if (mLastScrollView != null) {
            mLastScrollView.smoothScrollTo(0, 0);
        }
        mLastScrollView = view;
    }
    public void renew() {//刷新ADAPTER
        items.clear();
        getdata();
        addqAdapter = new addqAdapter(this, items, this, this);
        listView.setAdapter(addqAdapter);
        addqAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_addquestion, menu);
        return true;
    }
    @Override
    public void onResume() {
        super.onResume();
        renew();
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
}
