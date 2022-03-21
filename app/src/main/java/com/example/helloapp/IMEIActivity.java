package com.example.helloapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class IMEIActivity extends AppCompatActivity implements OnClickListener {

    Button btnActIMEI;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imeiactivity);

        btnActIMEI = (Button) findViewById(R.id.btnActIMEI);
        btnActIMEI.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        Intent intent;

        switch (v.getId()){
            case R.id.btnActIMEI:
                // part "get IMEI"

                //Have an  object of TelephonyManager
                TelephonyManager tm =(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                //Get IMEI Number of Phone  //////////////// for this example i only need the IMEI
                String IMEINumber=tm.getDeviceId();

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
                }*/
                textView = (TextView) findViewById(R.id.textViewIMEI);
                textView.setText(IMEINumber);
                break;
        }
    }
}