package com.example.downloadfilesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private  static final int PERMISSION_STORAGE_CODE=1000;
    EditText mUrl;
    Button mDownloadBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inititalize views with xml
        mUrl = findViewById(R.id.url);
        mDownloadBtn = findViewById(R.id.downloadUrl);



        //handle button Clicks
        mDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED) {

                     //Permission Denied , Request it
                        String[] permission ={Manifest.permission.WRITE_EXTERNAL_STORAGE};

                        //Show Pop up for Runtime
                        requestPermissions(permission,PERMISSION_STORAGE_CODE);

                } else {
                    //Permission Already granted , Download Start
                        startDownloading();

                }
            }
            else {
                //System os is less then marshmallow , Perform Download
                    startDownloading();
            }
        }

        });

    }
    private void startDownloading(){

        //get Url/text from edite text
        String url =mUrl.getText().toString().trim();
        //Create download request

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            //allow types of network to download files
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
            request.setTitle("Download"); //Set title in download notification
            request.setDescription("Downloading files... "); //Set Description in download notification
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,""+System.currentTimeMillis());
        //get the current timeStamp as file name


        //get download service and enqueue files
        DownloadManager manager =(DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

    }


    //Handle Permission Result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode){
            case PERMISSION_STORAGE_CODE:{
                if (grantResults.length >0 && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED){

                    // permission grant from popup , Perform Download
                    startDownloading();
                }else{

                    //Permission Denied from popup  show error message
                    Toast.makeText(this, "Permission Denied..!", Toast.LENGTH_SHORT).show();
                }
            }



        }
    }
}