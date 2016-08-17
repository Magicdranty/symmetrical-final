package com.example.jimmy.finall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;

public class ininwait extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, RadioGroup.OnCheckedChangeListener {
    String account, pft = "5678", date, func = "test";
    String pfa;
    connectuse connect;
    Socket soc;
    BufferedWriter bwt;
    BufferedReader brt;
    Boolean flag = true;
    TextView people;
    LinearLayout fortest;
    TextView sectitle, sec;
    RadioGroup group, gp;
    SeekBar seek;//MAX 31
    int TYPE = 1;//1 競賽 3測驗限總時　2 測驗限間距

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ininwait);
        Intent it = getIntent();
        pfa = it.getStringExtra("pfa");
        pft = it.getStringExtra("pft");
        connect = (connectuse) this.getApplication();
        SharedPreferences settings = getSharedPreferences("teacheruse_pref", 0);
        account = settings.getString("account", "XXX");

        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMdd");
        date = sDateFormat.format(new java.util.Date());
        ///////////////
        TextView tv = (TextView) findViewById(R.id.textViews);
        people = (TextView) findViewById(R.id.textView3);
        tv.setText(pfa);
        thread t = new thread();
        t.start();
        fortest = (LinearLayout) findViewById(R.id.fort1);
        sectitle = (TextView) findViewById(R.id.textView4);
        sec = (TextView) findViewById(R.id.sec);
        group = (RadioGroup) this.findViewById(R.id.radiogroup);
        group.setOnCheckedChangeListener(this);

        gp = (RadioGroup) findViewById(R.id.rg);
        gp.setOnCheckedChangeListener(this);

        seek = (SeekBar) findViewById(R.id.seek);
        seek.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radioButton:
                fortest.setVisibility(View.VISIBLE);
                if (gp.getCheckedRadioButtonId() == R.id.radioButton3) {
                    sectitle.setText("總測驗時間:");
                    seek.setMax(1800);//總測驗時間最長30分鐘
                    TYPE = 3;
                } else {
                    sectitle.setText("測驗間距時間:");
                    seek.setMax(30);//競賽模式最長30秒
                    TYPE = 2;
                }
                seek.setProgress(3);
                sec.setText("3");
                break;
            case R.id.radioButton2:
                TYPE = 1;
                fortest.setVisibility(View.GONE);
                sectitle.setText("競賽間距時間:");
                seek.setProgress(3);
                seek.setMax(30);//競賽模式最長30秒
                sec.setText("3");
                break;
            case R.id.radioButton3:
                TYPE = 3;
                sectitle.setText("總測驗時間:");
                seek.setMax(1800);//總測驗時間最長30分鐘
                seek.setProgress(30);
                sec.setText("30");
                break;
            case R.id.radioButton4:
                TYPE = 2;
                sectitle.setText("測驗間距時間:");
                seek.setMax(31);//競賽模式最長30秒
                seek.setProgress(3);
                sec.setText("3");
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (TYPE) {//1 競賽 3測驗限總時　2 測驗限間距
            case 1:
                if (progress < 3) {
                    seek.setProgress(3);
                    sec.setText("3 sec");
                } else {
                    sec.setText(String.valueOf(progress) + " sec");
                }
                break;
            case 2:
                if (progress < 3) {
                    seek.setProgress(3);
                    sec.setText("3 sec");
                } else if (progress == 31) {
                    sec.setText("unlimited");
                } else {
                    sec.setText(String.valueOf(progress) + " sec");
                }

                break;
            case 3:
                if (progress < 30) {
                    seek.setProgress(30);
                    sec.setText("30 sec");
                } else if (progress == 1800) {
                    sec.setText("unlimited");
                } else if ((progress / 60) >= 1) {
                    sec.setText(progress / 60 + " min " + (progress - (progress / 60) * 60) + " sec");
                }
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    class thread extends Thread {
        @Override
        public void run() {
            soc = connect.getSocket();
            brt = connect.getread();
            bwt = connect.getwrite();
            try {
                if (soc.isConnected()) {
                    bwt.write(account + "\n" + func + "\n" + pfa + "\n");
                    bwt.flush();
                    Log.e("!!!!!!", account + func + pfa);
                    while (flag) {
                        String x = brt.readLine().toString();
                        handler.obtainMessage(1, x).sendToTarget();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    people.setText(msg.obj.toString());
                    break;
            }
        }
    };

    public void start(View v) {
        if (Integer.parseInt(people.getText().toString()) > 0) {
            try {
                String x = DBConnector.executeQuery("Insert into record(taccount,pinfortest,date,pinforaccess) values ('" + account + "','" + pft + "','" + date + "','" + pfa + "')");
                bwt.write("start\n" + TYPE + "\n");
                bwt.flush();
                flag = false;//按下開始 就停止更新人數
                if (TYPE == 1)//rece條兼具
                {
                    bwt.write(String.valueOf(seek.getProgress()) + "\n");
                    bwt.flush();
                } else if (TYPE != 1) {//1 競賽 3測驗限總時　2 測驗限間距

                    bwt.write(String.valueOf(seek.getProgress()) + "\n");
                    bwt.flush();
                    soc.close();
                    Log.e("soc", "CLOSE");//地一種測驗按下開始 就切斷socket
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (TYPE == 1) {
                Toast.makeText(ininwait.this, "RACE", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(ininwait.this, ininrace.class);
                it.putExtra("accesspin", pfa);
                startActivity(it);
            } else {
                Toast.makeText(ininwait.this, "test", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(ininwait.this, ininreal.class);
                it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(it);
            }
        }else {Toast.makeText(ininwait.this,"目前沒有學生 請稍候...",Toast.LENGTH_SHORT).show();}


    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {   //確定按下退出鍵
            try {
                soc.close();
                finish();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
