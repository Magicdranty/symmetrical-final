package com.example.jimmy.finall;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class register extends AppCompatActivity {
    EditText acc, pwd, email, phones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        acc = (EditText) findViewById(R.id.editText3);
        pwd = (EditText) findViewById(R.id.editText4);
        email = (EditText) findViewById(R.id.editText5);
        phones = (EditText) findViewById(R.id.editText6);
    }
    public void back(View v) {
        finish();
    }
    public void startreg(View v) {
        Thread thread = new Thread() {
            Message msg = new Message();
            @Override
            public void run() {
                String stracc = acc.getText().toString();
                String strpwd = pwd.getText().toString();
                String stremail = email.getText().toString();
                String strphone = phones.getText().toString();
                String check = DBConnector.executeQuery("select * from user where account='" + stracc + "'");
                if (!(check.startsWith("null"))) {
                   msg.what=1;
                } else {
                    String result = DBConnector.executeQuery("insert into user(account,pwd,email,phone,TorS) values('" + stracc + "','" + strpwd + "','" + stremail + "','" + strphone + "','T')");
                    msg.what=2;
                }
                mhandler.sendMessage(msg);
            }
        };
        thread.start();
    }
    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast tos = Toast.makeText(register.this, "", Toast.LENGTH_SHORT);
            switch (msg.what) {
                case 2:
                    tos.setText("註冊成功 請重新登入");
                    tos.show();
                    finish();
                    break;
                case 1:
                    tos.setText("已有此帳號!請重新輸入");
                    tos.show();
                    break;
            }
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
