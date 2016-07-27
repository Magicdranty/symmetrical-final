package com.example.jimmy.finall;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class fixhead extends AppCompatActivity implements View.OnClickListener {
    FloatingActionButton f1, f2, f3;
    ImageView img;
    Boolean ispick = false;
    private File tempFile;
    connectuse con;
    String net="http://192.168.43.9/";
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 從相冊中選擇
    private static final int PHOTO_REQUEST_CUT = 3;// 結果
    //////
    ProgressDialog dialog = null;
    TextView tv;
    final String uploadFilePath = "/mnt/sdcard/mypic/";
   // final String uploadFileName=con.accountname ;
   String uploadFileName;
    String upLoadServerUri = null;
    int serverResponseCode = 0;
    //////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fixhead);
        img = (ImageView) findViewById(R.id.profile_image);
        con = (connectuse) this.getApplication();
        tempFile= new File(Environment.getExternalStorageDirectory() + "/mypic",
                con.accountname+".png");
        init();

        uploadFileName=con.accountname+".png" ;
        f1 = (FloatingActionButton) findViewById(R.id.menu_item1);
        f2 = (FloatingActionButton) findViewById(R.id.menu_item2);
        f3 = (FloatingActionButton) findViewById(R.id.menu_item3);
        f1.setOnClickListener(this);
        f2.setOnClickListener(this);
        f3.setOnClickListener(this);
        f1.setEnabled(false);
        tv = (TextView) findViewById(R.id.textView);
        upLoadServerUri = net+"UploadToServer.php";
    }

    ////////////
    public void init() {
        if (Environment.getExternalStorageState()//確定SD卡可讀寫
                .equals(Environment.MEDIA_MOUNTED))
        {
            File sdFile = android.os.Environment.getExternalStorageDirectory();
            String path = sdFile.getPath() + File.separator + "mypic";
            File dirFile = new File(path);
            if(!dirFile.exists()){//如果資料夾不存在
                dirFile.mkdir();//建立資料夾
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String imageUrl=( net+"uploads/"+con.accountname+".png");
                try {
                    Log.e("!@!@!@", imageUrl);
                    URL url = new URL(imageUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    final  Bitmap b = BitmapFactory.decodeStream(input);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            img.setImageBitmap(b);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
    //////////
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_item1:
                Toast.makeText(fixhead.this, "1", Toast.LENGTH_SHORT).show();
                dialog = ProgressDialog.show(fixhead.this, "", "Uploading file...", true);
                new Thread(new Runnable() {
                    public void run() {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                tv.setText("uploading started.....");
                            }
                        });

                        uploadFile(uploadFilePath + "" + uploadFileName);
                    }
                }).start();
                break;
            case R.id.menu_item2:
                Toast.makeText(fixhead.this, "2", Toast.LENGTH_SHORT).show();
                Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 指定調用相機拍照後照片的儲存路徑
                cameraintent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(tempFile));//將路徑轉成URI
                startActivityForResult(cameraintent, PHOTO_REQUEST_TAKEPHOTO);
                break;
            case R.id.menu_item3:
                Toast.makeText(fixhead.this, "3", Toast.LENGTH_SHORT).show();
                Intent it = new Intent(Intent.ACTION_GET_CONTENT);
                //開啟Pictures畫面Type設定為image
                it.setType("image/*");
                //使用Intent.ACTION_GET_CONTENT這個Action                                            //會開啟選取圖檔視窗讓您選取手機內圖檔
                it.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(it, PHOTO_REQUEST_GALLERY);
                break;

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:// 當選擇拍照時調用
                startPhotoZoom(Uri.fromFile(tempFile));
                Log.e("~~~~", "caminin");
                ispick = false;
                break;
            case PHOTO_REQUEST_GALLERY:// 當選擇從本地獲取圖片時
                // 做非空判斷，當我們覺得不滿意想重新剪裁的時候便不會報異常，下同
                if (data != null) {
                    startPhotoZoom(data.getData());
                    Log.e("~~pickinin~~", data.getData().toString());
                    ispick = true;
                }
                break;
            case PHOTO_REQUEST_CUT:// 返回的結果
                if (data != null) {
                    if (ispick) {
                        sentPicToNext(data, true);
                    } else sentPicToNext(data, false);
                    Log.e("~~~~", "~~~~");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是設置在開启的intent中設置顯示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是寬高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁圖片的寬高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        Log.e("!!!!~", uri.toString());
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    private void sentPicToNext(Intent picdata, Boolean ip) {
        Bundle bundle = picdata.getExtras();
        if (bundle != null) {
            Bitmap photo = bundle.getParcelable("data");
            if (photo == null) {
                img.setImageResource(R.drawable.zzz);
            } else {
                f1.setEnabled(true);
                img.setImageBitmap(photo);
                ImageView pf = (ImageView) findViewById(R.id.profile_image);
                pf.setImageBitmap(photo);
                Log.e("!W!S", tempFile.getAbsolutePath());
            }
            if (ip)//如果試用選的 要另存新檔 如果是照相前面已經存好了
            {
                img.setDrawingCacheEnabled(true);
                Bitmap bmp = img.getDrawingCache();
                try {
                    tempFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(tempFile);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ispick = false;
            }

        }
    }

    public int uploadFile(String sourceFileUri) {
        Log.e("datasendtoupload", sourceFileUri);
        String fileName = sourceFileUri;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {

            dialog.dismiss();

            Log.e("uploadFile", "Source File not exist :"
                    + uploadFilePath + "" + uploadFileName);

            runOnUiThread(new Runnable() {
                public void run() {
                    tv.setText("Source File not exist :"
                            + uploadFilePath + "" + uploadFileName);
                }
            });

            return 0;
        } else {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");// 设定传送的内容类型是可序列化的java对象(如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());
//!!!!!!!!!!!
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=uploaded_file;filename="
                        + fileName + "" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                Log.e("!inin","inin");
                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {

                    runOnUiThread(new Runnable() {
                        public void run() {

                            String msg = "File Upload Completed."
                                    + uploadFileName;
                            f1.setEnabled(false);
                            connectuse con = (connectuse) fixhead.this.getApplication();
                            BitmapDrawable mDrawable =  (BitmapDrawable) img.getDrawable();
                            con.b =mDrawable.getBitmap();
                            tv.setText(msg);
                            Toast.makeText(fixhead.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                dialog.dismiss();
                ex.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        tv.setText("MalformedURLException Exception : check script url.");
                        Toast.makeText(fixhead.this, "MalformedURLException",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {

                dialog.dismiss();
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    public void run() {
                        tv.setText("Got Exception : see logcat ");
                        Toast.makeText(fixhead.this, "Got Exception : see logcat ",
                                Toast.LENGTH_SHORT).show();
                    }
                });
                Log.e("Upload file to server", "Exception : " + e.toString());
            }
            dialog.dismiss();
            return serverResponseCode;

        } // End else block
    }

}
