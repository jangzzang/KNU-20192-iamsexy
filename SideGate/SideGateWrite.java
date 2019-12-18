package com.example.continuousliving.SideGate;

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
import com.example.continuousliving.WestGate.WestGateWrite;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

public class SideGateWrite extends AppCompatActivity {

    private String sidegate_write_title;
    private String sidegate_write_content;
    private EditText sidegate_write_title_text;
    private EditText sidegate_write_content_text;
    private Button sidegate_write_finishbutton;


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
        setContentView(R.layout.activity_side_gate_write);

        sidegate_write_finishbutton = (Button)findViewById(R.id.sidegate_write_finishbutton);
        sidegate_write_title_text = (EditText)findViewById(R.id.sidegate_write_title);
        sidegate_write_content_text = (EditText)findViewById(R.id.sidegate_write_content);
        getLocationBtn = (Button)findViewById(R.id.sidegate_loc_button);

        sidegate_write_finishbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sidegate_write_title = sidegate_write_title_text.getText().toString();
                sidegate_write_content = sidegate_write_content_text.getText().toString();

                if(sidegate_write_title.length() == 0){
                    Toast.makeText(getApplicationContext(), "제목을 입력하십시오.", Toast.LENGTH_SHORT).show();
                }else if(sidegate_write_content.length() == 0){
                    Toast.makeText(getApplicationContext(), "내용을 입력하십시오", Toast.LENGTH_SHORT).show();
                }else{
                    Handler delayHandler = new Handler();
                    delayHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 2000);
                    Toast.makeText(getApplicationContext(), "작성을 완료하였습니다.", Toast.LENGTH_SHORT).show();
                    sidegate_insert(v);
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
                gps = new GPStracker(SideGateWrite.this);

                if (gps.isGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    Geocoder gCoder = new Geocoder(SideGateWrite.this, Locale.getDefault());
                    List<Address> addr = null;
                    try{
                        addr = gCoder.getFromLocation(latitude,longitude,1);
                        Address a = addr.get(0);

                        for (int i=0;i <= a.getMaxAddressLineIndex();i++) {
                            sidegate_write_content_text.append(a.getAddressLine(i));
                        }
                        sidegate_write_content_text.append("\n");
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                    if (addr != null) {
                        if (addr.size()==0) {
                            Toast.makeText(SideGateWrite.this,"주소정보 없음", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    gps.showSettingsAlert();
                }
            }
        });
    }

    private void sidegate_insert(View view){
        String sidegate_title = sidegate_write_title;
        String sidegate_contents = sidegate_write_content;
        String sidegate_id = SaveStdInformationSharedPreference.getStdID(SideGateWrite.this);
        String sidegate_password = SaveStdInformationSharedPreference.getStdPassword(SideGateWrite.this);
        String sidegate_username = SaveStdInformationSharedPreference.getStdName(SideGateWrite.this);
        sidegate_insertDatabase(sidegate_title, sidegate_contents, sidegate_id, sidegate_password, sidegate_username);
    }

    private void sidegate_insertDatabase(String title, String contents, String id, String password, String username){
        class sidegate_insert extends AsyncTask<String, Void, String> {
            ProgressDialog sidegate_loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                sidegate_loading = ProgressDialog.show(SideGateWrite.this, "Please Wait.", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                sidegate_loading.dismiss();
            }

            @Override
            protected String doInBackground(String... strings) {
                try{
                    String sidegate_db_title = (String)strings[0];
                    String sidegate_db_contents = (String)strings[1];
                    String sidegate_db_id = (String)strings[2];
                    String sidegate_db_password = (String)strings[3];
                    String sidegate_db_username = (String)strings[4];

                    String sidegate_db_link = "http://155.230.25.19/SidegateWrite.php";
                    String sidegate_data = URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(sidegate_db_title, "UTF-8");

                    sidegate_data += "&" + URLEncoder.encode("contents", "UTF-8") + "=" + URLEncoder.encode(sidegate_db_contents, "UTF-8");
                    sidegate_data += "&" + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(sidegate_db_id, "UTF-8");
                    sidegate_data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(sidegate_db_password, "UTF-8");
                    sidegate_data += "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(sidegate_db_username, "UTF-8");

                    URL sidegate_db_url = new URL(sidegate_db_link);
                    URLConnection sidegate_db_conn = sidegate_db_url.openConnection();

                    sidegate_db_conn.setDoOutput(true);
                    OutputStreamWriter sidegate_db_wr = new OutputStreamWriter(sidegate_db_conn.getOutputStream());

                    sidegate_db_wr.write(sidegate_data);
                    sidegate_db_wr.flush();

                    BufferedReader sidegate_db_reader = new BufferedReader(new InputStreamReader(sidegate_db_conn.getInputStream()));
                    StringBuilder sidegate_db_sb = new StringBuilder();
                    String sidegate_db_line = null;

                    while((sidegate_db_line = sidegate_db_reader.readLine())!= null){
                        sidegate_db_sb.append(sidegate_db_line);
                        break;
                    }
                    return sidegate_db_sb.toString();
                }
                catch(Exception e){
                    return new String("Exception : " + e.getMessage());
                }
            }
        }

        sidegate_insert sidegate_task = new sidegate_insert();
        sidegate_task.execute(title, contents, id, password, username);
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
