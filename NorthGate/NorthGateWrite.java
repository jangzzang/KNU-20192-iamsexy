package com.example.continuousliving.NorthGate;

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
import com.example.continuousliving.R;
import com.example.continuousliving.SaveStdInformationSharedPreference;
import com.example.continuousliving.SideGate.SideGateWrite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

public class NorthGateWrite extends AppCompatActivity {

    private String northgate_write_title;
    private String northgate_write_content;
    private EditText northgate_write_title_text;
    private EditText northgate_write_content_text;
    private Button northgate_write_finishbutton;


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
        setContentView(R.layout.activity_north_gate_write);

        northgate_write_finishbutton = (Button)findViewById(R.id.northgate_write_finishbutton);
        northgate_write_title_text = (EditText)findViewById(R.id.northgate_write_title);
        northgate_write_content_text = (EditText)findViewById(R.id.northgate_write_content);
        getLocationBtn = (Button)findViewById(R.id.northgate_loc_button);

        northgate_write_finishbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                northgate_write_title = northgate_write_title_text.getText().toString();
                northgate_write_content = northgate_write_content_text.getText().toString();

                if(northgate_write_title.length() == 0){
                    Toast.makeText(getApplicationContext(), "제목을 입력하십시오.", Toast.LENGTH_SHORT).show();
                }else if(northgate_write_content.length() == 0){
                    Toast.makeText(getApplicationContext(), "내용을 입력하십시오", Toast.LENGTH_SHORT).show();
                }else{
                    Handler delayHandler = new Handler();
                    delayHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 2000);
                    Toast.makeText(getApplicationContext(), "작성을 완료하였습니다.", Toast.LENGTH_SHORT).show();
                    northgate_insert(v);
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
                gps = new GPStracker(NorthGateWrite.this);

                if (gps.isGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    Geocoder gCoder = new Geocoder(NorthGateWrite.this, Locale.getDefault());
                    List<Address> addr = null;
                    try{
                        addr = gCoder.getFromLocation(latitude,longitude,1);
                        Address a = addr.get(0);

                        for (int i=0;i <= a.getMaxAddressLineIndex();i++) {
                            northgate_write_content_text.append(a.getAddressLine(i));
                        }
                        northgate_write_content_text.append("\n");
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                    if (addr != null) {
                        if (addr.size()==0) {
                            Toast.makeText(NorthGateWrite.this,"주소정보 없음", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    gps.showSettingsAlert();
                }
            }
        });
    }

    private void northgate_insert(View view){
        String northgate_title = northgate_write_title;
        String northgate_contents = northgate_write_content;
        String northgate_id = SaveStdInformationSharedPreference.getStdID(NorthGateWrite.this);
        String northgate_password = SaveStdInformationSharedPreference.getStdPassword(NorthGateWrite.this);
        String northgate_username = SaveStdInformationSharedPreference.getStdName(NorthGateWrite.this);
        northgate_insertDatabase(northgate_title, northgate_contents, northgate_id, northgate_password, northgate_username);
    }

    private void northgate_insertDatabase(String title, String contents, String id, String password, String username){
        class northgate_insert extends AsyncTask<String, Void, String> {
            ProgressDialog northgate_loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                northgate_loading = ProgressDialog.show(NorthGateWrite.this, "Please Wait.", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                northgate_loading.dismiss();
            }

            @Override
            protected String doInBackground(String... strings) {
                try{
                    String northgate_db_title = (String)strings[0];
                    String northgate_db_contents = (String)strings[1];
                    String northgate_db_id = (String)strings[2];
                    String northgate_db_password = (String)strings[3];
                    String northgate_db_username = (String)strings[4];

                    String northgate_db_link = "http://155.230.25.19/NorthgateWrite.php";
                    String northgate_data = URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(northgate_db_title, "UTF-8");

                    northgate_data += "&" + URLEncoder.encode("contents", "UTF-8") + "=" + URLEncoder.encode(northgate_db_contents, "UTF-8");
                    northgate_data += "&" + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(northgate_db_id, "UTF-8");
                    northgate_data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(northgate_db_password, "UTF-8");
                    northgate_data += "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(northgate_db_username, "UTF-8");

                    URL northgate_db_url = new URL(northgate_db_link);
                    URLConnection northgate_db_conn = northgate_db_url.openConnection();

                    northgate_db_conn.setDoOutput(true);
                    OutputStreamWriter northgate_db_wr = new OutputStreamWriter(northgate_db_conn.getOutputStream());

                    northgate_db_wr.write(northgate_data);
                    northgate_db_wr.flush();

                    BufferedReader northgate_db_reader = new BufferedReader(new InputStreamReader(northgate_db_conn.getInputStream()));
                    StringBuilder northgate_db_sb = new StringBuilder();
                    String northgate_db_line = null;

                    while((northgate_db_line = northgate_db_reader.readLine())!= null){
                        northgate_db_sb.append(northgate_db_line);
                        break;
                    }
                    return northgate_db_sb.toString();
                }
                catch(Exception e){
                    return new String("Exception : " + e.getMessage());
                }
            }
        }

        northgate_insert northgate_task = new northgate_insert();
        northgate_task.execute(title, contents, id, password, username);
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
