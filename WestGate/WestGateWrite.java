package com.example.continuousliving.WestGate;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

public class WestGateWrite extends AppCompatActivity {

    private String westgate_write_title;
    private String westgate_write_content;
    private EditText westgate_write_title_text;
    private EditText westgate_write_content_text;
    private Button westgate_write_finishbutton;


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
        setContentView(R.layout.activity_west_gate_write);

        westgate_write_finishbutton = (Button)findViewById(R.id.westgate_write_finishbutton);
        westgate_write_title_text = (EditText)findViewById(R.id.westgate_write_title);
        westgate_write_content_text = (EditText)findViewById(R.id.westgate_write_content);
        getLocationBtn = (Button)findViewById(R.id.westgate_loc_button);

        westgate_write_finishbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                westgate_write_title = westgate_write_title_text.getText().toString();
                westgate_write_content = westgate_write_content_text.getText().toString();

                if(westgate_write_title.length() == 0){
                    Toast.makeText(getApplicationContext(), "제목을 입력하십시오.", Toast.LENGTH_SHORT).show();
                }else if(westgate_write_content.length() == 0){
                    Toast.makeText(getApplicationContext(), "내용을 입력하십시오", Toast.LENGTH_SHORT).show();
                }else{
                    Handler delayHandler = new Handler();
                    delayHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 2000);
                    Toast.makeText(getApplicationContext(), "작성을 완료하였습니다.", Toast.LENGTH_SHORT).show();
                    westgate_insert(v);
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
                gps = new GPStracker(WestGateWrite.this);

                if (gps.isGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    Geocoder gCoder = new Geocoder(WestGateWrite.this, Locale.getDefault());
                    List<Address> addr = null;
                    try{
                        addr = gCoder.getFromLocation(latitude,longitude,1);
                        Address a = addr.get(0);

                        for (int i=0;i <= a.getMaxAddressLineIndex();i++) {
                            westgate_write_content_text.append(a.getAddressLine(i));
                        }
                        westgate_write_content_text.append("\n");
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                    if (addr != null) {
                        if (addr.size()==0) {
                            Toast.makeText(WestGateWrite.this,"주소정보 없음", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    gps.showSettingsAlert();
                }
            }
        });
    }

    private void westgate_insert(View view){
        String westgate_title = westgate_write_title;
        String westgate_contents = westgate_write_content;
        String westgate_id = SaveStdInformationSharedPreference.getStdID(WestGateWrite.this);
        String westgate_password = SaveStdInformationSharedPreference.getStdPassword(WestGateWrite.this);
        String westgate_username = SaveStdInformationSharedPreference.getStdName(WestGateWrite.this);
        westgate_insertDatabase(westgate_title, westgate_contents, westgate_id, westgate_password, westgate_username);
    }

    private void westgate_insertDatabase(String title, String contents, String id, String password, String username){
        class westgate_insert extends AsyncTask<String, Void, String> {
            ProgressDialog westgate_loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                westgate_loading = ProgressDialog.show(WestGateWrite.this, "Please Wait.", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                westgate_loading.dismiss();
            }

            @Override
            protected String doInBackground(String... strings) {
                try{
                    String westgate_db_title = (String)strings[0];
                    String westgate_db_contents = (String)strings[1];
                    String westgate_db_id = (String)strings[2];
                    String westgate_db_password = (String)strings[3];
                    String westgate_db_username = (String)strings[4];

                    String westgate_db_link = "http://155.230.25.19/WestgateWrite.php";
                    String westgate_data = URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(westgate_db_title, "UTF-8");

                    westgate_data += "&" + URLEncoder.encode("contents", "UTF-8") + "=" + URLEncoder.encode(westgate_db_contents, "UTF-8");
                    westgate_data += "&" + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(westgate_db_id, "UTF-8");
                    westgate_data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(westgate_db_password, "UTF-8");
                    westgate_data += "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(westgate_db_username, "UTF-8");

                    URL westgate_db_url = new URL(westgate_db_link);
                    URLConnection westgate_db_conn = westgate_db_url.openConnection();

                    westgate_db_conn.setDoOutput(true);
                    OutputStreamWriter westgate_db_wr = new OutputStreamWriter(westgate_db_conn.getOutputStream());

                    westgate_db_wr.write(westgate_data);
                    westgate_db_wr.flush();

                    BufferedReader westgate_db_reader = new BufferedReader(new InputStreamReader(westgate_db_conn.getInputStream()));
                    StringBuilder westgate_db_sb = new StringBuilder();
                    String westgate_db_line = null;

                    while((westgate_db_line = westgate_db_reader.readLine())!= null){
                        westgate_db_sb.append(westgate_db_line);
                        break;
                    }
                    return westgate_db_sb.toString();
                }
                catch(Exception e){
                    return new String("Exception : " + e.getMessage());
                }
            }
        }

        westgate_insert westgate_task = new westgate_insert();
        westgate_task.execute(title, contents, id, password, username);
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
