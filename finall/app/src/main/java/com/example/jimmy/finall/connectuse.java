package com.example.jimmy.finall;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class connectuse extends Application {
    private Socket soc;
    private BufferedReader brt;
    private BufferedWriter bwt;
   // String accountname,email;
    //String net="140.118.148.151";
   // String net="140.118.148.168";
    ImageView img;Bitmap b;
    public  void init()
    {
        try {
            this.soc=new Socket();SharedPreferences settings = getSharedPreferences("teacheruse_pref", 0);
            Log.e(settings.getString("net","XXX"),"AAA");

            SocketAddress addr=new InetSocketAddress(settings.getString("net","XXX"),509);
            soc.connect(addr,2000);
            brt=new BufferedReader(new InputStreamReader(soc.getInputStream(), "UTF-8"));
            bwt = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream(), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Socket getSocket() {
        return soc;
    }

    public BufferedWriter getwrite()
    {
        return bwt;
    }

    public BufferedReader getread()
    {
        return brt;
    }


}