package com.example.jimmy.finall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

public class ballot extends AppCompatActivity implements NumberPicker.OnValueChangeListener {
    DrawerLayout drawerLayout;NavigationView view;ImageView img;
    TextView hundred, ten, sec;
    NumberPicker np, np1;
    Button bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ballot);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        view = (NavigationView) findViewById(R.id.navigation_view);
        view.getMenu().findItem(R.id.navigation_item_1).setChecked(true);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_1:
                        Intent it = new Intent(ballot.this, ininreal.class);
                        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(it);
                        break;
                    case R.id.navigation_item_2:
                        Intent it2 = new Intent(ballot.this, ininreal.class);
                       // it2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(it2);
                        break;
                    case R.id.navigation_item_3:
                        Intent it3 = new Intent(ballot.this, forgrade.class);
                        // it2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(it3);
                        break;
                    case R.id.navigation_item_4:
                        Toast.makeText(ballot.this, "已經在抽籤系統內", Toast.LENGTH_SHORT).show();
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
            connectuse x=(connectuse)ballot.this.getApplication();
            SharedPreferences settings = getSharedPreferences("teacheruse_pref", 0);
            tv.setText(settings.getString("email","XXX"));
            tv2.setText(settings.getString("account","XXX"));
            img=(ImageView)header.findViewById(R.id.profile_image);
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
        /////
        hundred = (TextView) findViewById(R.id.textView);
        ten = (TextView) findViewById(R.id.textView2);
        sec = (TextView) findViewById(R.id.textView3);
        bt = (Button) findViewById(R.id.button);
        np = (NumberPicker) findViewById(R.id.numberPicker);
        np.setOnValueChangedListener(this);
        np.setMaxValue(999);
        np.setMinValue(0);
        np.setValue(10);
        np1 = (NumberPicker) findViewById(R.id.numberPicker2);
        np1.setOnValueChangedListener(this);
        np1.setMaxValue(999);
        np1.setMinValue(0);
        np1.setValue(10);

    }


    public void c(View v) {
        bt.setClickable(false);
        final  int temp= rand();
        Log.e("!!!!!!!!", String.valueOf(temp));
        new CountDownTimer(6000, 50) {
            @Override
            public void onFinish() {
                bt.setClickable(true);
              sec.setText(String.valueOf(temp%10));
            }

            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished > 4000) {
                    sec.setText(String.valueOf((int) (Math.random() * 10)));
                    ten.setText(String.valueOf((int) (Math.random() * 10)));
                    hundred.setText(String.valueOf((int) (Math.random() * 10)));
                } else if (millisUntilFinished > 2000) {
                     hundred.setText(String.valueOf(temp/100));
                    ten.setText(String.valueOf((int) (Math.random() * 10)));
                    sec.setText(String.valueOf((int) (Math.random() * 10)));
                } else {
                    ten.setText(String.valueOf(temp/10%10));
                    sec.setText(String.valueOf((int) (Math.random() * 10)));
                }

            }

        }.start();
    }

    public int rand() {
        int from = np.getValue();
        int to = np1.getValue();
        int temp=0;
        while(true)
        {
            if(to>100)
            {
                temp=(int)(Math.random()* 1000);
            }else {temp=(int)(Math.random()* 100);}

            if(temp>=from && temp<=to )
            {
                break;
            }
        }
        return  temp;
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        switch (picker.getId()) {
            case R.id.numberPicker: {
                if (newVal != 999) {
                    np1.setValue(newVal + 1);
                } else np1.setValue(999);
                np1.setMinValue(newVal);
            }
        }
    }
}
