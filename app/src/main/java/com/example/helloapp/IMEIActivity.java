package com.example.helloapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.provider.Settings;
import android.provider.Settings.System;

public class IMEIActivity extends Activity implements OnClickListener {

    Button btnActIMEI;
    TextView textView;

    Context context;
    TelephonyManager tm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imeiactivity);

        btnActIMEI = (Button) findViewById(R.id.btnActIMEI);
        btnActIMEI.setOnClickListener(this);

        context = this;
        //tm = (TelephonyManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        Intent intent;

        switch (v.getId()) {
            case R.id.btnActIMEI:
                // part "get IMEI"

                //Have an  object of TelephonyManager
                //TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                //String androidID = System.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

               /* String IMEINumber = "";
                if (null != tm){
                    //Get IMEI Number of Phone  //////////////// for this example i only need the IMEI
                    IMEINumber = tm.getDeviceId(); //.getImei();
                }*/

                /*
                TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                String imei="";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    imei = telephonyManager.getImei();
                }
                else
                {
                    imei =  "";
                }

                if (Build.VERSION.SDK_INT >= 26) {
                    imei="30"; //telephonyManager.getImei();
                }
                else
                {
                    imei="20"; //telephonyManager.getDeviceId();
                }*//*
                textView = (TextView) findViewById(R.id.textViewIMEI);
                //TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                try {
                    tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        textView.setText((CharSequence) tm.getImei(1));
                    }
                } catch (Exception e) {
                    //e.getMessage();
                    //.printStackTrace();
                    textView.setText(e.getMessage());
                }*/

                String androidID = System.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
                textView = (TextView) findViewById(R.id.textViewIMEI);
                textView.setText(androidID);
                break;
        }
    }

    public String getIMEI(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
}