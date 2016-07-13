package com.example.jimmy.finall;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    EditText acc, pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
       //
        acc = (EditText) findViewById(R.id.editText);
        pwd = (EditText) findViewById(R.id.e2);
    }
    public void login(View v) {
        Thread thread = new Thread() {
            Bundle bundle = new Bundle();
            Message msg = new Message();
            @Override
            public void run() {
                String stracc = acc.getText().toString();
                String strpwd = pwd.getText().toString();
                String result = DBConnector.executeQuery("select * from user where account='" + stracc + "' and pwd='" + strpwd + "' and TorS='T'");
                if (result.startsWith("null")) {
                    mhandler.obtainMessage(2).sendToTarget();
                } else if (result.contains("timed out")) {
                    mhandler.obtainMessage(3).sendToTarget();
                } else {
                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        JSONObject jsonData = jsonArray.getJSONObject(0);
                        connectuse x=(connectuse)MainActivity.this.getApplicationContext();
                        x.accountname=jsonData.getString("account");
                        x.email=jsonData.getString("email");
                        mhandler.obtainMessage(1).sendToTarget();

                    } catch (Exception e) {
                        Log.e("log_tag", e.toString());
                    }
                }
                msg.setData(bundle);
                mhandler.sendMessage(msg);
            }
        };
        thread.start();
    }
    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast tos=Toast.makeText(MainActivity.this,"", Toast.LENGTH_SHORT);
            switch(msg.what)
            {
                case 1:
                    Intent it = new Intent(MainActivity.this, list.class);
                    startActivity(it);
                    break;
                case 2:
                    tos.setText("無此帳號!請重新輸入");
                    tos.show();
                    break;
                case 3:
                    tos.setText("連線逾時");
                    tos.show();
                    break;
            }
        }
    };
    public void register(View v) {
        Intent intentreg = new Intent(this, register.class);
        startActivity(intentreg);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
