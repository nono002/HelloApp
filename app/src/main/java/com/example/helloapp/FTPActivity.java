package com.example.helloapp;
/*
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class FTPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftpactivity);
    }
}
*/

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class FTPActivity extends Activity implements OnClickListener {

    private static final String TAG = "FTPActivity";
    private static final String TEMP_FILENAME = "TAGtest.txt";
    private Context cntxt = null;

    private MyFTPClientFunctions ftpclient = null;
    public FTPClient mFTPClient = null;

    private Button btnLoginFtp, btnUploadFile, btnDisconnect, btnExit, btnUpload;
    private EditText edtHostName, edtUserName, edtPassword;
    private ProgressDialog pd;

    private String[] fileList;

    SharedPreferences sPref;

    final String SAVED_HOST_NAME = "192.168.0.102";
    final String SAVED_USER_NAME = "sa";
    final String SAVED_PASSWORD = "sa";

    /*
    private Handler handler = new Handler(){

        public void handleMessage(android.os.Message msg) {

            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
            if (msg.what == 0) {
                getFTPFileList();
            } else if (msg.what == 1) {
                showCustomDialog(fileList);
            } else if (msg.what == 2) {
                Toast.makeText(FTPActivity.this, "Uploaded Successfully!",
                        Toast.LENGTH_LONG).show();
            } else if (msg.what == 3) {
                Toast.makeText(FTPActivity.this, "Disconnected Successfully!",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(FTPActivity.this, "Unable to Perform Action!",
                        Toast.LENGTH_LONG).show();
            }

        }

    };
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftpactivity);

        cntxt = this.getBaseContext();

        edtHostName = (EditText) findViewById(R.id.edtHostName);
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

        loadSettings();

        /*
        ((EditText) findViewById(R.id.edtHostName)).setText("192.168.0.102"); //edtHostName.getText().toString().trim();
        ((EditText) findViewById(R.id.edtUserName)).setText("sa"); //edtUserName.getText().toString().trim();
        ((EditText) findViewById(R.id.edtPassword)).setText("sa"); //edtPassword.getText().toString().trim();
        */

        btnLoginFtp = (Button) findViewById(R.id.btnLoginFtp);
        btnUploadFile = (Button) findViewById(R.id.btnUploadFile);
        btnDisconnect = (Button) findViewById(R.id.btnDisconnectFtp);
        btnExit = (Button) findViewById(R.id.btnExit);
        btnUpload = (Button) findViewById(R.id.btnUpLoadFtp);

        btnLoginFtp.setOnClickListener(this);
        btnUploadFile.setOnClickListener(this);
        btnDisconnect.setOnClickListener(this);
        btnExit.setOnClickListener(this);
        btnUpload.setOnClickListener(this);

        // Create a temporary file. You can use this to upload
        createDummyFile();

        ftpclient = new MyFTPClientFunctions();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLoginFtp:
                if (isOnline(FTPActivity.this)) {
                    connectToFTPAddress();
                } else {
                    Toast.makeText(FTPActivity.this,"Please check your internet connection!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnUploadFile:
                pd = ProgressDialog.show(FTPActivity.this, "", "Uploading...",true, false);
                new Thread(new Runnable() {
                    public void run() {
                        boolean status = false;
                        status = ftpclient.ftpUpload(
                                Environment.getExternalStorageDirectory()
                                        + "/TAGFtp/" + TEMP_FILENAME,
                                TEMP_FILENAME, "/", cntxt);
                        if (status == true) {
                            Log.d(TAG, "Upload success");
                            //handler.sendEmptyMessage(2);
                            Toast.makeText(FTPActivity.this, "Uploaded Successfully!", Toast.LENGTH_LONG).show();
                        } else {
                            Log.d(TAG, "Upload failed");
                            //handler.sendEmptyMessage(-1);
                            Toast.makeText(FTPActivity.this, "Unable Success!", Toast.LENGTH_LONG).show();
                        }
                    }
                }).start();
                break;
            case R.id.btnUpLoadFtp:
                mFTPClient = new FTPClient();
                final String host = edtHostName.getText().toString().trim();
                final String username = edtUserName.getText().toString().trim();
                final String password = edtPassword.getText().toString().trim();
                final String Directory = "$$TEST$$";

                // 1. XML parsing (write invoice, docs)
                // 2. Read/Write FTP

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            mFTPClient.connect(host, 21);
                            boolean status = mFTPClient.login(username, password);
                            mFTPClient.changeWorkingDirectory("/");
                            mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
                            mFTPClient.enterLocalPassiveMode();
                            mFTPClient.makeDirectory(Directory);
                            mFTPClient.storeFile(Directory + "/test.txt", null);
                            mFTPClient.logout();
                            mFTPClient.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


                /*
                BufferedInputStream buffIn = null;
                File file = null;
                try {
                    buffIn = new BufferedInputStream(new FileInputStream(file));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }*/

                /*try {
                    buffIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                break;
            case R.id.btnDisconnectFtp:
                pd = ProgressDialog.show(FTPActivity.this, "", "Disconnecting...",
                        true, false);

                new Thread(new Runnable() {
                    public void run() {
                        ftpclient.ftpDisconnect();
                        //handler.sendEmptyMessage(3);
                    }
                }).start();

                break;
            case R.id.btnExit:
                this.finish();
                break;
        }

    }

    private void connectToFTPAddress() {

        final String host = edtHostName.getText().toString().trim();
        final String username = edtUserName.getText().toString().trim();
        final String password = edtPassword.getText().toString().trim();

        if (host.length() < 1) {
            Toast.makeText(FTPActivity.this, "Please Enter Host Address!", Toast.LENGTH_LONG).show();
        } else if (username.length() < 1) {
            Toast.makeText(FTPActivity.this, "Please Enter User Name!", Toast.LENGTH_LONG).show();
        } else if (password.length() < 1) {
            Toast.makeText(FTPActivity.this, "Please Enter Password!", Toast.LENGTH_LONG).show();
        } else {

            pd = ProgressDialog.show(FTPActivity.this, "", "Connecting...", true, false);

            new Thread(new Runnable() {
                public void run() {
                    boolean status = false;
                    status = ftpclient.ftpConnect(host, username, password, 21);
                    if (status == true) {
                        Log.d(TAG, "Connection Success");
                        Toast.makeText(FTPActivity.this, "Connection success!", Toast.LENGTH_LONG).show();
                        //onPause();
                        getFTPFileList();
                        //handler.sendEmptyMessage(0);
                    } else {
                        Log.d(TAG, "Connection failed");
                        Toast.makeText(FTPActivity.this, "Unable to Perform Action!", Toast.LENGTH_LONG).show();
                        //handler.sendEmptyMessage(-1);
                        onPause();
                    }
                }
            }).start();
        }
    }

    private void getFTPFileList() {
        pd = ProgressDialog.show(FTPActivity.this, "", "Getting Files...", true, false);

        new Thread(new Runnable() {

            @Override
            public void run() {
                fileList = ftpclient.ftpPrintFilesList("/");
                Toast.makeText(FTPActivity.this, "Show list!", Toast.LENGTH_LONG).show();
                //handler.sendEmptyMessage(1);
            }
        }).start();
    }

    public void createDummyFile() {

        try {
            File root = new File(Environment.getExternalStorageDirectory(),
                    "TAGFtp");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, TEMP_FILENAME);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append("Hi this is a sample file to upload for android FTP client example from TheAppGuruz!");
            writer.flush();
            writer.close();
            Toast.makeText(this, "Saved : " + gpxfile.getAbsolutePath(),
                    Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    private void showCustomDialog(String[] fileList) {
        // custom dialog
        final Dialog dialog = new Dialog(FTPActivity.this);
        dialog.setContentView(R.layout.custom);
        dialog.setTitle("/ Directory File List");

        TextView tvHeading = (TextView) dialog.findViewById(R.id.tvListHeading);
        tvHeading.setText(":: File List ::");

        if (fileList != null && fileList.length > 0) {
            ListView listView = (ListView) dialog
                    .findViewById(R.id.lstItemList);
            ArrayAdapter<String> fileListAdapter = new ArrayAdapter<String>(
                    this, android.R.layout.simple_list_item_1, fileList);
            listView.setAdapter(fileListAdapter);
        } else {
            tvHeading.setText(":: No Files ::");
        }

        Button dialogButton = (Button) dialog.findViewById(R.id.btnOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveSettings();
    }

    private void saveSettings() {
        sPref = getPreferences(MODE_PRIVATE);
        Editor ed = sPref.edit();
        ed.putString(SAVED_HOST_NAME, edtHostName.getText().toString());
        ed.putString(SAVED_USER_NAME, edtUserName.getText().toString());
        ed.putString(SAVED_PASSWORD, edtPassword.getText().toString());
        ed.commit();
        Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show();
    }

    private void loadSettings() {
        sPref = getPreferences(MODE_PRIVATE);
        String savedHostName = sPref.getString(SAVED_HOST_NAME, "192.168.0.102");
        String savedUserName = sPref.getString(SAVED_USER_NAME, "sa");
        String savedPassword = sPref.getString(SAVED_PASSWORD, "sa");
        edtHostName.setText(savedHostName);
        edtUserName.setText(savedUserName);
        edtPassword.setText(savedPassword);
        Toast.makeText(this, "Settings loaded", Toast.LENGTH_SHORT).show();
    }

}