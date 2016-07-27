package com.example.jimmy.finall;

import android.app.Application;
import android.graphics.Bitmap;
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
    String accountname,email;
    ImageView img;Bitmap b;
    public  void init()
    {
        try {
            this.soc=new Socket();
            //SocketAddress addr=new InetSocketAddress("192.168.100.2",599);
            SocketAddress addr=new InetSocketAddress("192.168.43.9",599);
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