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
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class inrealtime extends AppCompatActivity implements View.OnClickListener {
    DrawerLayout drawerLayout;
    NavigationView view;
    Button b5, b6, b7, b8;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inrealtime);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        b5 = (Button) findViewById(R.id.button5);
        b6 = (Button) findViewById(R.id.button6);
        b7 = (Button) findViewById(R.id.button7);
        b8 = (Button) findViewById(R.id.button8);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);
        b8.setOnClickListener(this);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        view = (NavigationView) findViewById(R.id.navigation_view);
        view.getMenu().findItem(R.id.navigation_item_1).setChecked(true);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                            drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1:
                           Toast.makeText(inrealtime.this, "已經在主選單內", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_item_2:
                          Intent it = new Intent(inrealtime.this, ininreal.class);
                          startActivity(it);
                        break;
                    case R.id.navigation_item_3:
                          Intent it3 = new Intent(inrealtime.this, forgrade.class);
                          startActivity(it3);
                        break;
                    case R.id.navigation_item_4:
                          Intent it4 = new Intent(inrealtime.this, ballot.class);
                          startActivity(it4);
                        break;
                    case R.id.navigation_item_5:
                        break;
                }
                return true;
            }
        });
        ////以下是更新帳戶名
        if (view.getHeaderCount() > 0) {//如果有header 更新用戶名、email、頭貼
            View header = view.getHeaderView(0);
            TextView tv = (TextView) header.findViewById(R.id.textView2);
            TextView tv2 = (TextView) header.findViewById(R.id.name);
            connectuse x = (connectuse) inrealtime.this.getApplication();
            SharedPreferences settings = getSharedPreferences("teacheruse_pref", 0);
            tv2.setText(settings.getString("account", "XXX"));
            tv.setText(settings.getString("email", "XXX"));
            img = (ImageView) header.findViewById(R.id.profile_image);
            img.setImageBitmap(x.b);
        }
///
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(inrealtime.this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
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
    @Override
    public void onClick(View v) {//中間的功能按鈕
        switch (v.getId()) {
            case R.id.button5:
                Intent it5 = new Intent(this, ininreal.class);
                startActivity(it5);
                break;
            case R.id.button6:
                Intent it6 = new Intent(this, addquestion.class);
                startActivity(it6);
                break;
            case R.id.button7:
                Intent it7 = new Intent(this, addtest.class);
                startActivity(it7);
                break;
            case R.id.button8:
                Intent it8 = new Intent(this, forgrade.class);
                startActivity(it8);
                break;

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    connectuse x = (connectuse) inrealtime.this.getApplication();
                    img.setImageBitmap(x.b);
                }
            });
        }
    }
    public void headclick(View v) {
        drawerLayout.closeDrawers();
        Intent it = new Intent(inrealtime.this, fixhead.class);
        startActivityForResult(it, 0);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {   //確定按下退出鍵and防止重複按下退出鍵
            new AlertDialog.Builder(inrealtime.this)
                    .setTitle("警告")
                    .setMessage("是否要回到登入畫面")
                    .setPositiveButton("我要離開", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();

        }
        return false;
    }
}
