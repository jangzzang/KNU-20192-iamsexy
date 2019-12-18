package com.example.continuousliving;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.continuousliving.MainGate.MainGateBoard;
import com.example.continuousliving.NorthGate.NorthGateBoard;
import com.example.continuousliving.SideGate.SideGateBoard;
import com.example.continuousliving.WestGate.WestGateBoard;

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

public class MainActivity extends AppCompatActivity {
    private Button north_gate_button;
    private Button side_gate_button;
    private Button main_gate_button;
    private Button west_gate_button;

    private static String TAG = "phptest_MainActivity";
    private static final String TAG_NORTHGATE_JSON = "northgate";
    private static final String TAG_NORTHGATE_NUM = "num";
    private static final String TAG_NORTHGATE_TITLE = "title";
    private static final String TAG_NORTHGATE_ID = "id";
    private static final String TAG_NORTHGATE_PASSWORD = "password";
    private static final String TAG_NORTHGATE_NAME = "username";
    private static final String TAG_NORTHGATE_TIME = "time";
    private int northgate_array_size;
    private String mJsonString;


    private String[] Northgate_extra_title = new String[5000];
    private String[] Northgate_extra_name = new String[5000];
    private String[] Northgate_extra_time = new String[5000];
    private String[] Northgate_extra_id = new String[5000];
    private String[] Northgate_extra_password = new String[5000];
    private int[] Northgate_extra_num = new int[5000];
    private long endtime = 0;

    private ArrayList<HashMap<String, String>> Northgate_arraylist;

    private static final String TAG_SIDEGATE_JSON = "sidegate";
    private static final String TAG_SIDEGATE_NUM = "num";
    private static final String TAG_SIDEGATE_TITLE = "title";
    private static final String TAG_SIDEGATE_ID = "id";
    private static final String TAG_SIDEGATE_PASSWORD = "password";
    private static final String TAG_SIDEGATE_NAME = "username";
    private static final String TAG_SIDEGATE_TIME = "time";
    private int sidegate_array_size;


    private String[] Sidegate_extra_title = new String[5000];
    private String[] Sidegate_extra_name = new String[5000];
    private String[] Sidegate_extra_time = new String[5000];
    private String[] Sidegate_extra_id = new String[5000];
    private String[] Sidegate_extra_password = new String[5000];
    private int[] Sidegate_extra_num = new int[5000];

    private ArrayList<HashMap<String, String>> Sidegate_arraylist;

    private static final String TAG_MAINGATE_JSON = "sidegate";
    private static final String TAG_MAINGATE_NUM = "num";
    private static final String TAG_MAINGATE_TITLE = "title";
    private static final String TAG_MAINGATE_ID = "id";
    private static final String TAG_MAINGATE_PASSWORD = "password";
    private static final String TAG_MAINGATE_NAME = "username";
    private static final String TAG_MAINGATE_TIME = "time";
    private int maingate_array_size;

    private String[] Maingate_extra_title = new String[5000];
    private String[] Maingate_extra_name = new String[5000];
    private String[] Maingate_extra_time = new String[5000];
    private String[] Maingate_extra_id = new String[5000];
    private String[] Maingate_extra_password = new String[5000];
    private int[] Maingate_extra_num = new int[5000];

    private ArrayList<HashMap<String, String>> Maingate_arraylist;

    private static final String TAG_WESTGATE_JSON = "sidegate";
    private static final String TAG_WESTGATE_NUM = "num";
    private static final String TAG_WESTGATE_TITLE = "title";
    private static final String TAG_WESTGATE_ID = "id";
    private static final String TAG_WESTGATE_PASSWORD = "password";
    private static final String TAG_WESTGATE_NAME = "username";
    private static final String TAG_WESTGATE_TIME = "time";
    private int westgate_array_size;

    private String[] Westgate_extra_title = new String[5000];
    private String[] Westgate_extra_name = new String[5000];
    private String[] Westgate_extra_time = new String[5000];
    private String[] Westgate_extra_id = new String[5000];
    private String[] Westgate_extra_password = new String[5000];
    private int[] Westgate_extra_num = new int[5000];

    private ArrayList<HashMap<String, String>> Westgate_arraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        north_gate_button = (Button)findViewById(R.id.north_gate_button);
        side_gate_button = (Button)findViewById(R.id.side_gate_button);
        main_gate_button = (Button)findViewById(R.id.main_gate_button);
        west_gate_button = (Button)findViewById(R.id.west_gate_button);

        Northgate_arraylist = new ArrayList<>();
        Northgate_getdata Northgate_getdata = new Northgate_getdata();
        Northgate_getdata.execute("http://155.230.25.19/NorthgateGetdata.php");

        Sidegate_arraylist = new ArrayList<>();
        Sidegate_getdata Sidegate_getdata = new Sidegate_getdata();
        Sidegate_getdata.execute("http://155.230.25.19/SidegateGetdata.php");

        Maingate_arraylist = new ArrayList<>();
        Maingate_getdata Maingate_getdata = new Maingate_getdata();
        Maingate_getdata.execute("http://155.230.25.19/MaingateGetdata.php");

        Westgate_arraylist = new ArrayList<>();
        Westgate_getdata Westgate_getdata = new Westgate_getdata();
        Westgate_getdata.execute("http://155.230.25.19/WestgateGetdata.php");

        // 북문 버튼 클릭 시 구현 부분
        north_gate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NorthGateBoard.class);
                intent.putExtra("northgate_title",Northgate_extra_title);
                intent.putExtra("northgate_name",Northgate_extra_name);
                intent.putExtra("northgate_time",Northgate_extra_time);
                intent.putExtra("northgate_size",Northgate_arraylist.size());
                intent.putExtra("northgate_num",Northgate_extra_num);
                intent.putExtra("northgate_id",Northgate_extra_id);
                intent.putExtra("northgate_password",Northgate_extra_password);
                Northgate_arraylist.clear();
                Northgate_getdata northgate_getdata = new Northgate_getdata();
                northgate_getdata.execute("http://155.230.25.19/NorthgateGetdata.php");
                startActivity(intent);
            }
        });


        // 쪽문 버튼 클릭 시 구현 부분
        side_gate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SideGateBoard.class);
                intent.putExtra("sidegate_title",Sidegate_extra_title);
                intent.putExtra("sidegate_name",Sidegate_extra_name);
                intent.putExtra("sidegate_time",Sidegate_extra_time);
                intent.putExtra("sidegate_size",Sidegate_arraylist.size());
                intent.putExtra("sidegate_num",Sidegate_extra_num);
                intent.putExtra("sidegate_id",Sidegate_extra_id);
                intent.putExtra("sidegate_password",Sidegate_extra_password);
                Sidegate_arraylist.clear();
                Sidegate_getdata sidegate_getdata = new Sidegate_getdata();
                sidegate_getdata.execute("http://155.230.25.19/SidegateGetdata.php");
                startActivity(intent);
            }
        });

        main_gate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainGateBoard.class);
                intent.putExtra("maingate_title",Maingate_extra_title);
                intent.putExtra("maingate_name",Maingate_extra_name);
                intent.putExtra("maingate_time",Maingate_extra_time);
                intent.putExtra("maingate_size",Maingate_arraylist.size());
                intent.putExtra("maingate_num",Maingate_extra_num);
                intent.putExtra("maingate_id",Maingate_extra_id);
                intent.putExtra("maingate_password",Maingate_extra_password);
                Maingate_arraylist.clear();
                Maingate_getdata maingate_getdata = new Maingate_getdata();
                maingate_getdata.execute("http://155.230.25.19/MaingateGetdata.php");
                startActivity(intent);
            }
        });

        west_gate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WestGateBoard.class);
                intent.putExtra("westgate_title",Westgate_extra_title);
                intent.putExtra("westgate_name",Westgate_extra_name);
                intent.putExtra("westgate_time",Westgate_extra_time);
                intent.putExtra("westgate_size",Westgate_arraylist.size());
                intent.putExtra("westgate_num",Westgate_extra_num);
                intent.putExtra("westgate_id",Westgate_extra_id);
                intent.putExtra("westgate_password",Westgate_extra_password);
                Westgate_arraylist.clear();
                Westgate_getdata westgate_getdata = new Westgate_getdata();
                westgate_getdata.execute("http://155.230.25.19/WestgateGetdata.php");
                startActivity(intent);
            }
        });
    }

    // JSON형식으로 공지사항의 제목, 작성자, 시간, id, 비밀번호, 글num을 intent로 넘겨주기 위해 받아오는 부분
    // 이게 공지사항 activity에서 구현을 해놓으면 글이 바로 안뜸..
    // 그래서 intent로 넘겨주고 처음 보이게하는 부분을 바로 보여지게 하려고 main에서 땡겨옴
    private class Northgate_getdata extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this, "Please wait.",null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if(result!=null){
                mJsonString = result;
                northshowResult();
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

    private void northshowResult(){
        try{
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_NORTHGATE_JSON);

            northgate_array_size = jsonArray.length();
            for(int i =0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);

                Integer number = item.getInt(TAG_NORTHGATE_NUM);
                String title = item.getString(TAG_NORTHGATE_TITLE);
                String id = item.getString(TAG_NORTHGATE_ID);
                String password = item.getString(TAG_NORTHGATE_PASSWORD);
                String username = item.getString(TAG_NORTHGATE_NAME);
                String time = item.getString(TAG_NORTHGATE_TIME);

                Northgate_extra_title[i] = title;
                Northgate_extra_name[i] = username;
                Northgate_extra_time[i] = time;
                Northgate_extra_num[i] = number;
                Northgate_extra_id[i] = id;
                Northgate_extra_password[i] = password;

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(id, password);
                Northgate_arraylist.add(hashMap);
            }

        }catch(JSONException e){
            Log.d(TAG, "showResult : ", e);
        }
    }


    private class Sidegate_getdata extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this, "Please wait.",null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if(result!=null){
                mJsonString = result;
                sideshowResult();
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

    private void sideshowResult(){
        try{
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_SIDEGATE_JSON);

            sidegate_array_size = jsonArray.length();
            for(int i =0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);

                Integer number = item.getInt(TAG_SIDEGATE_NUM);
                String title = item.getString(TAG_SIDEGATE_TITLE);
                String id = item.getString(TAG_SIDEGATE_ID);
                String password = item.getString(TAG_SIDEGATE_PASSWORD);
                String username = item.getString(TAG_SIDEGATE_NAME);
                String time = item.getString(TAG_SIDEGATE_TIME);

                Sidegate_extra_title[i] = title;
                Sidegate_extra_name[i] = username;
                Sidegate_extra_time[i] = time;
                Sidegate_extra_num[i] = number;
                Sidegate_extra_id[i] = id;
                Sidegate_extra_password[i] = password;

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(id, password);
                Sidegate_arraylist.add(hashMap);
            }

        }catch(JSONException e){
            Log.d(TAG, "showResult : ", e);
        }
    }


    private class Maingate_getdata extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this, "Please wait.",null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if(result!=null){
                mJsonString = result;
                mainshowResult();
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

    private void mainshowResult(){
        try{
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_MAINGATE_JSON);

            maingate_array_size = jsonArray.length();
            for(int i =0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);

                Integer number = item.getInt(TAG_MAINGATE_NUM);
                String title = item.getString(TAG_MAINGATE_TITLE);
                String id = item.getString(TAG_MAINGATE_ID);
                String password = item.getString(TAG_MAINGATE_PASSWORD);
                String username = item.getString(TAG_MAINGATE_NAME);
                String time = item.getString(TAG_MAINGATE_TIME);

                Maingate_extra_title[i] = title;
                Maingate_extra_name[i] = username;
                Maingate_extra_time[i] = time;
                Maingate_extra_num[i] = number;
                Maingate_extra_id[i] = id;
                Maingate_extra_password[i] = password;

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(id, password);
                Maingate_arraylist.add(hashMap);
            }

        }catch(JSONException e){
            Log.d(TAG, "showResult : ", e);
        }
    }


    private class Westgate_getdata extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this, "Please wait.",null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if(result!=null){
                mJsonString = result;
                westshowResult();
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

    private void westshowResult(){
        try{
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_WESTGATE_JSON);

            westgate_array_size = jsonArray.length();
            for(int i =0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);

                Integer number = item.getInt(TAG_WESTGATE_NUM);
                String title = item.getString(TAG_WESTGATE_TITLE);
                String id = item.getString(TAG_WESTGATE_ID);
                String password = item.getString(TAG_WESTGATE_PASSWORD);
                String username = item.getString(TAG_WESTGATE_NAME);
                String time = item.getString(TAG_WESTGATE_TIME);

                Westgate_extra_title[i] = title;
                Westgate_extra_name[i] = username;
                Westgate_extra_time[i] = time;
                Westgate_extra_num[i] = number;
                Westgate_extra_id[i] = id;
                Westgate_extra_password[i] = password;

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(id, password);
                Westgate_arraylist.add(hashMap);
            }

        }catch(JSONException e){
            Log.d(TAG, "showResult : ", e);
        }
    }
}
