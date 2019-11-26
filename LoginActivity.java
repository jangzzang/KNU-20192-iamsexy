package com.example.continuousliving;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {
    // 로그인 화면 class

    private EditText login_idText, login_passwordText;
    private CheckBox login_checkBox;
    private Button login_button;
    private TextView login_register;
    private static String TAG = "phptest_MainActivity";
    private static final String TAG_JSON = "webnautes";
    private static final String TAG_ID = "id";
    private static final String TAG_Password = "password";
    private static final String TAG_Studentid = "studentid";
    private static final String TAG_Username = "username";
    private static final String TAG_Status = "status";
    private String mJsonString;
    private String login_checkid, login_checkpw;
    private ArrayList<HashMap<String, String>> login_arraylist;
    private Integer[] checkingStatus = new Integer[5000];
    private String[] login_idarray = new String[5000];
    private String[] StdName = new String[5000];
    private String[] StdNum = new String[5000];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        login_idText = (EditText)findViewById(R.id.login_idText);
        login_passwordText = (EditText)findViewById(R.id.login_passwordText);
        login_button = (Button)findViewById(R.id.login_button);
        login_register = (TextView)findViewById(R.id.login_register);
        login_checkBox = (CheckBox)findViewById(R.id.login_checkBox);
        login_arraylist = new ArrayList<>();

        GetData task = new GetData();
        task.execute("http://155.230.25.19/login.php");

        SaveStdInformationSharedPreference.getSharedPreferences(getApplicationContext());

        login_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                login_checkid = login_idText.getText().toString();
                login_checkpw = login_passwordText.getText().toString();
                boolean checkingpass = false;

                for(int i=0;i<login_arraylist.size();i++){
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap = login_arraylist.get(i);
                    String temp = hashMap.get(login_checkid);

                    if(temp!=null && temp.equals(login_checkpw)){
                        if(checkingStatus[i]==0){
                            checkingpass = true;
                            Toast.makeText(getApplicationContext(),"로그인 권한이 없습니다.",Toast.LENGTH_LONG).show();
                        }

                        if(checkingStatus[i]==1 || checkingStatus[i] == 2) {
                            checkingpass = true;
                            Toast.makeText(getApplicationContext(), "로그인이 완료되었습니다",Toast.LENGTH_LONG).show();
                            Intent login_buttonintent = new Intent(LoginActivity.this,MainActivity.class);
                            LoginActivity.this.startActivity(login_buttonintent);

                            if(login_checkBox.isChecked() == true) {
                                SaveStdInformationSharedPreference.setUserName(getApplicationContext(), login_idText.getText().toString());
                            } else {
                                SaveStdInformationSharedPreference.setUserName(getApplicationContext(), "");
                            }

                            SaveStdInformationSharedPreference.setStd(getApplicationContext(), login_checkid, login_checkpw, StdNum[i], StdName[i], checkingStatus[i]);
                            finish();
                        }
                    }
                }

                if(!checkingpass) {
                    Toast.makeText(getApplicationContext(), "존재하지 않는 ID입니다.", Toast.LENGTH_LONG).show();
                }

            }
        });

        login_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login_registerintent = new Intent(LoginActivity.this,RegisterActivity.class);
                login_registerintent.putExtra("idarray", login_idarray);
                LoginActivity.this.startActivity(login_registerintent);
            }
        });
    }


    private class GetData extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(LoginActivity.this, "Please wait.",null, true, true);

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if(result!=null){
                mJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            String serverURL = strings[0];

            try{
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.connect();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK){
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine())!=null){
                    sb.append(line);
                }

                bufferedReader.close();
                return sb.toString().trim();
            }
            catch(Exception e){
                Log.d(TAG, "InsertData : Error ",e);
                errorString = e.toString();
                return null;
            }
        }
    }

    private void showResult(){
        try{
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for(int i =0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);

                String id = item.getString(TAG_ID);
                String password = item.getString(TAG_Password);
                String studentid = item.getString(TAG_Studentid);
                String username = item.getString(TAG_Username);
                Integer status = item.getInt(TAG_Status);

                checkingStatus[i] = status;
                login_idarray[i] = id;
                StdName[i] = username;
                StdNum[i] = studentid;
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(id, password);
                login_arraylist.add(hashMap);


            }

        }catch(JSONException e){
            Log.d(TAG, "showResult : ", e);
        }
    }
}
