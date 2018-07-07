package com.mrubel.supercoolcamera;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity  implements  MediaScannerConnection.MediaScannerConnectionClient{

    ImageView iv;
    Button take_photo;
    int flag = 0;
    Bitmap b;
    MediaScannerConnection conn;

    public final static String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    public static final int REQUEST_PERMISSION_KEY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv = (ImageView) findViewById(R.id.my_image);
        take_photo = (Button) findViewById(R.id.take_photo);

        if(!Function.hasPermissions(getBaseContext(), PERMISSIONS)){
            ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS, REQUEST_PERMISSION_KEY);

        }


        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flag == 0) {

                    // -- code for taking photo --

                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(i, 99);

                } else if(flag == 1) {

                    //--- code for saving photo ---

                    //savePhotoToMySdCard(b);
                    savePhoto(b);

                    Toast.makeText(getApplicationContext(), "Photo saved to sd card!", Toast.LENGTH_SHORT).show();

                    flag = 0;
                    take_photo.setText("Take Photo");

                }

            }
        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 99 && resultCode == RESULT_OK && data != null){

            b = (Bitmap) data.getExtras().get("data");

            iv.setImageBitmap(b);

            flag = 1;
            take_photo.setText("Save Photo");

        }

    }


    private void savePhotoToMySdCard(Bitmap bit){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String pname = sdf.format(new Date());


        String root = Environment.getExternalStorageDirectory().toString();
        File folder = new File(root+"/SCC_Photos");
        folder.mkdirs();
        if (folder.exists ()) folder.delete ();

        File mydir = new File(Environment.getExternalStorageDirectory() + "/mydirSCC_photos/");
        if(!mydir.exists()) {
            mydir.mkdirs();


        }
        else {
            if (mydir.exists ()) mydir.delete ();
            Log.e("error", "dir. already exists");
        }

        File my_file = new File(mydir, pname+".png");
        try {
            Log.e("File", "in file");

            FileOutputStream stream = new FileOutputStream(my_file);
            bit.compress(Bitmap.CompressFormat.PNG, 80, stream);
            stream.flush();
            stream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("error", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("error", e.getMessage());
        }


    }


    private void savePhoto(Bitmap bitmap){
        String root = Environment.getExternalStorageDirectory().toString();
        File newDir = new File(root + "/Aamader_Khabar");
        newDir.mkdirs();
        Random gen = new Random();
        int n = 10000;
        n = gen.nextInt(n);
        String fotoname = "Photo-"+ n +".jpg";
        File file = new File (newDir, fotoname);
        if (file.exists ()) file.delete ();
        try {
            MediaScannerConnection.scanFile(MainActivity.this, new String[] { file.getPath() }, new String[] { "image/JPEG" }, null);

            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

            Toast.makeText(getApplicationContext(), "Saved to your folder", Toast.LENGTH_SHORT ).show();
            //startScan();

        } catch (Exception e) {

        }

    }

    private void startScan()
    {


        if(conn!=null) conn.disconnect();
        conn = new MediaScannerConnection(MainActivity.this,MainActivity.this);
        conn.connect();
    }


    @Override
    public void onMediaScannerConnected() {
        try{
            conn.scanFile(Environment.getExternalStorageDirectory().toString(), "image/*");
        } catch (java.lang.IllegalStateException e){
        }
    }

    @Override
    public void onScanCompleted(String s, Uri uri) {
        conn.disconnect();

    }
}
