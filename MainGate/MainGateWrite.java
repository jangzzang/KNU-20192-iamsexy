package com.example.continuousliving.MainGate;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.continuousliving.GPStracker;
import com.example.continuousliving.NorthGate.NorthGateWrite;
import com.example.continuousliving.R;
import com.example.continuousliving.SaveStdInformationSharedPreference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

public class MainGateWrite extends AppCompatActivity {

    private String maingate_write_title;
    private String maingate_write_content;
    private EditText maingate_write_title_text;
    private EditText maingate_write_content_text;
    private Button maingate_write_finishbutton;

    private Button getLocationBtn;
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;
    private GPStracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gate_write);

        maingate_write_finishbutton = (Button)findViewById(R.id.maingate_write_finishbutton);
        maingate_write_title_text = (EditText)findViewById(R.id.maingate_write_title);
        maingate_write_content_text = (EditText)findViewById(R.id.maingate_write_content);
        getLocationBtn = (Button)findViewById(R.id.maingate_loc_button);

        maingate_write_finishbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maingate_write_title = maingate_write_title_text.getText().toString();
                maingate_write_content = maingate_write_content_text.getText().toString();

                if(maingate_write_title.length() == 0){
                    Toast.makeText(getApplicationContext(), "제목을 입력하십시오.", Toast.LENGTH_SHORT).show();
                }else if(maingate_write_content.length() == 0){
                    Toast.makeText(getApplicationContext(), "내용을 입력하십시오", Toast.LENGTH_SHORT).show();
                }else{
                    Handler delayHandler = new Handler();
                    delayHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 2000);
                    Toast.makeText(getApplicationContext(), "작성을 완료하였습니다.", Toast.LENGTH_SHORT).show();
                    maingate_insert(v);
                    finish();
                }
            }
        });

        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPermission){
                    callPermission();
                    return;
                }
                gps = new GPStracker(MainGateWrite.this);

                if (gps.isGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    Geocoder gCoder = new Geocoder(MainGateWrite.this, Locale.getDefault());
                    List<Address> addr = null;
                    try{
                        addr = gCoder.getFromLocation(latitude,longitude,1);
                        Address a = addr.get(0);

                        for (int i=0;i <= a.getMaxAddressLineIndex();i++) {
                            maingate_write_content_text.append(a.getAddressLine(i));
                        }
                        maingate_write_content_text.append("\n");
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                    if (addr != null) {
                        if (addr.size()==0) {
                            Toast.makeText(MainGateWrite.this,"주소정보 없음", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    gps.showSettingsAlert();
                }
            }
        });
    }

    private void maingate_insert(View view){
        String maingate_title = maingate_write_title;
        String maingate_contents = maingate_write_content;
        String maingate_id = SaveStdInformationSharedPreference.getStdID(MainGateWrite.this);
        String maingate_password = SaveStdInformationSharedPreference.getStdPassword(MainGateWrite.this);
        String maingate_username = SaveStdInformationSharedPreference.getStdName(MainGateWrite.this);
        maingate_insertDatabase(maingate_title, maingate_contents, maingate_id, maingate_password, maingate_username);
    }

    private void maingate_insertDatabase(String title, String contents, String id, String password, String username){
        class maingate_insert extends AsyncTask<String, Void, String> {
            ProgressDialog maingate_loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                maingate_loading = ProgressDialog.show(MainGateWrite.this, "Please Wait.", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                maingate_loading.dismiss();
            }

            @Override
            protected String doInBackground(String... strings) {
                try{
                    String maingate_db_title = (String)strings[0];
                    String maingate_db_contents = (String)strings[1];
                    String maingate_db_id = (String)strings[2];
                    String maingate_db_password = (String)strings[3];
                    String maingate_db_username = (String)strings[4];

                    String maingate_db_link = "http://155.230.25.19/MaingateWrite.php";
                    String maingate_data = URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(maingate_db_title, "UTF-8");

                    maingate_data += "&" + URLEncoder.encode("contents", "UTF-8") + "=" + URLEncoder.encode(maingate_db_contents, "UTF-8");
                    maingate_data += "&" + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(maingate_db_id, "UTF-8");
                    maingate_data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(maingate_db_password, "UTF-8");
                    maingate_data += "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(maingate_db_username, "UTF-8");

                    URL maingate_db_url = new URL(maingate_db_link);
                    URLConnection maingate_db_conn = maingate_db_url.openConnection();

                    maingate_db_conn.setDoOutput(true);
                    OutputStreamWriter maingate_db_wr = new OutputStreamWriter(maingate_db_conn.getOutputStream());

                    maingate_db_wr.write(maingate_data);
                    maingate_db_wr.flush();

                    BufferedReader maingate_db_reader = new BufferedReader(new InputStreamReader(maingate_db_conn.getInputStream()));
                    StringBuilder maingate_db_sb = new StringBuilder();
                    String maingate_db_line = null;

                    while((maingate_db_line = maingate_db_reader.readLine())!= null){
                        maingate_db_sb.append(maingate_db_line);
                        break;
                    }
                    return maingate_db_sb.toString();
                }
                catch(Exception e){
                    return new String("Exception : " + e.getMessage());
                }
            }
        }

        maingate_insert maingate_task = new maingate_insert();
        maingate_task.execute(title, contents, id, password, username);
    }

    private void callPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_ACCESS_FINE_LOCATION);
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_ACCESS_COARSE_LOCATION);
        }
        else {
            isPermission = true;
        }
    }
}