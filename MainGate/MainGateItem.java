package com.example.continuousliving.MainGate;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.continuousliving.LoginActivity;
import com.example.continuousliving.R;
import com.example.continuousliving.SaveStdInformationSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

public class MainGateItem extends AppCompatActivity {
    // JSON파싱 할 때 모든 값 저장 되는 String 변수
    // mJsonString에 글 내용 전체 파싱 내용
    // mJsonString2에 댓글 내용 전체 파싱 내용
    private String mJsonString;

    // 쓸데 없는 TAG..
    private static String TAG = "phptest_MainActivity";

    // 글 내용 JSON 파싱을 위한 상수 문자열
    private static final String TAG_MAINGATE_JSON = "maingate";
    private static final String TAG_MAINGATE_NUM = "num";
    private static final String TAG_MAINGATE_TITLE = "contents";

    private static final String TAG_PHONE_JSON = "phonenum";

    private static final String TAG_WRITER_ID = "id";
    private static final String TAG_WRITER_PASSWORD = "password";
    private static final String TAG_WRITER_PHONENUM = "number";

    private String[] writerid = new String[5000];
    private String[] writerpassword = new String[5000];
    private String[] writerphonenum = new String[5000];
    private int writernum = 0;
    private int numberindex;

    // 얘는 쭉 글 번호가 저장되는 넘버
    private int checkingnum;

    // XML 과 연동되는 변수들
    private TextView maingate_item_title;
    private TextView maingate_item_name;
    private TextView maingate_item_date;
    private TextView maingate_item_content;
    private String maingate_item_id;
    private String maingate_item_password;
    private Button maingate_item_delete;
    private Button maingate_item_change;
    private ScrollView maingate_scrollview;
    private Button maingate_item_phonenum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gate_item);

        Intent intent = getIntent();

        // 먼저 글 내용을 JSON 파싱해옴
        MainGate_item_getdata maingate_item_task = new MainGate_item_getdata();
        maingate_item_task.execute("http://155.230.25.19/MaingateItemGetdata.php");

        // 모든 변수들 XML과 연동하는 부분

        maingate_item_title = (TextView)findViewById(R.id.maingate_item_title);
        maingate_item_name = (TextView)findViewById(R.id.maingate_item_name);
        maingate_item_date = (TextView)findViewById(R.id.maingate_item_date);
        maingate_item_content = (TextView)findViewById(R.id.maingate_item_content);
        maingate_item_delete = (Button)findViewById(R.id.maingate_item_delete);
        maingate_item_change = (Button)findViewById(R.id.maingate_item_change);
        maingate_scrollview = (ScrollView) findViewById(R.id.maingate_item_scrollview);
        maingate_item_phonenum = (Button)findViewById(R.id.maingate_item_getphonenum);


        // Intent 이전 값 받아오는 부분
        checkingnum = intent.getExtras().getInt("maingate_num");
        maingate_item_id = getIntent().getExtras().getString("maingate_id");
        maingate_item_password = getIntent().getExtras().getString("maingate_password");
        maingate_item_title.setText(intent.getStringExtra("maingate_title"));
        maingate_item_name.setText(intent.getStringExtra("maingate_name"));
        maingate_item_date.setText(intent.getStringExtra("maingate_date"));

        //
        GetPhoneNum task = new GetPhoneNum();
        task.execute("http://155.230.25.19/calling.php");
        //

        maingate_item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(maingate_item_id.equals(SaveStdInformationSharedPreference.getStdID(MainGateItem.this).toString()) && maingate_item_password.equals(SaveStdInformationSharedPreference.getStdPassword(MainGateItem.this).toString())){
                    maingate_item_delete_Database(checkingnum);
                    Toast.makeText(getApplicationContext(),"게시글이 삭제가 되었습니다..", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"게시글 삭제 권한이 없습니다..", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // 게시글을 수정할 때 버튼 기능 구현, 수정권한이 없으면 수정 못하도록 만들어놓음
        maingate_item_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(maingate_item_id.equals(SaveStdInformationSharedPreference.getStdID(MainGateItem.this).toString()) && maingate_item_password.equals(SaveStdInformationSharedPreference.getStdPassword(MainGateItem.this).toString())){
                    Intent notice_change_intent = new Intent(MainGateItem.this, MainGateChange.class);
                    notice_change_intent.putExtra("prevtitle", maingate_item_title.getText().toString());
                    notice_change_intent.putExtra("prevcontents", maingate_item_content.getText().toString());
                    notice_change_intent.putExtra("prevnum" ,checkingnum);
                    startActivity(notice_change_intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"게시글 수정 권한이 없습니다..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //
        maingate_item_phonenum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetPhoneNum task = new GetPhoneNum();
                task.execute("http://155.230.25.19/getphonenum.php");
                boolean findchk = false;
                numberindex = 0;

                for(int i = 0; i < writernum; i++) {
                    if(writerid[i].compareTo(maingate_item_id) == 0 && writerpassword[i].compareTo(maingate_item_password) == 0){
                        numberindex = i;
                        findchk = true;
                        break;
                    }
                }

                if(!findchk) {
                    Toast.makeText(MainGateItem.this, "등록된 전화번호가 없습니다", Toast.LENGTH_SHORT);
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainGateItem.this);
                    builder.setTitle("전화를 하시겠습니까?");
                    builder.setIcon(android.R.drawable.ic_menu_call);
                    builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + writerphonenum[numberindex]));
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(),"취소되었습니다.",Toast.LENGTH_LONG).show();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        //
    }

    // 게시글의 내용을 intent로 받아오면 용량이 어마어마해지므로 여기서는 그냥 받아오기로 하였음
    // JSON형식으로 글 key값과 content만 받아와서 checkingnum(intent로 받아온 글의 number)과 같으면 setText해주는 방식
    private class MainGate_item_getdata extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainGateItem.this, "Please wait.",null, true, true);
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
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_MAINGATE_JSON);

            for(int i =0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);

                Integer number = item.getInt(TAG_MAINGATE_NUM);
                String contents = item.getString(TAG_MAINGATE_TITLE);


                // 얘는 그냥 for문 돌면서 checkingnum 과 동일하면 Text를 바꾸게 해놓음
                if(number == checkingnum){
                    maingate_item_content.setText(contents);
                }
            }

        }catch(JSONException e){
            Log.d(TAG, "showResult : ", e);
        }
    }


    private void maingate_item_delete_Database(int primarykey){
        class maingate_item_delete extends AsyncTask<String, Void, String> {
            ProgressDialog maingate_item_loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                maingate_item_loading = ProgressDialog.show(MainGateItem.this, "Please Wait.", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                maingate_item_loading.dismiss();
            }

            @Override
            protected String doInBackground(String... strings) {
                try{
                    String notice_item_delete_db_num = strings[0];

                    String notice_item_delete_db_link = "http://155.230.25.19/MaingateDelete.php?no=" + notice_item_delete_db_num;
                    String notice_item_delete_db_data = URLEncoder.encode("no", "UTF-8") + "=" + URLEncoder.encode(notice_item_delete_db_num, "UTF-8");

                    URL notice_item_delete_db_url = new URL(notice_item_delete_db_link);
                    URLConnection notice_item_delete_db_conn = notice_item_delete_db_url.openConnection();

                    notice_item_delete_db_conn.setDoOutput(true);
                    OutputStreamWriter notice_item_delete_db_wr = new OutputStreamWriter(notice_item_delete_db_conn.getOutputStream());

                    notice_item_delete_db_wr.write(notice_item_delete_db_data);
                    notice_item_delete_db_wr.flush();

                    BufferedReader notice_item_delete_db_reader = new BufferedReader(new InputStreamReader(notice_item_delete_db_conn.getInputStream()));
                    StringBuilder notice_item_delete_db_sb = new StringBuilder();
                    String notice_item_delete_db_line = null;

                    while((notice_item_delete_db_line = notice_item_delete_db_reader.readLine())!= null){
                        notice_item_delete_db_sb.append(notice_item_delete_db_line);
                        break;
                    }
                    return notice_item_delete_db_sb.toString();
                }
                catch(Exception e){
                    return new String("Exception : " + e.getMessage());
                }
            }
        }

        maingate_item_delete maingate_item_task = new maingate_item_delete();
        maingate_item_task.execute(Integer.toString(primarykey));
    }



    //
    private class GetPhoneNum extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainGateItem.this, "Please wait.",null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            if(result!=null){
                mJsonString = result;
                showPhonenum();
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

    private void showPhonenum(){
        try{
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_PHONE_JSON);
            writernum = 0;

            for(int i =0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);

                String id = item.getString(TAG_WRITER_ID);
                String password = item.getString(TAG_WRITER_PASSWORD);
                String phonenum = item.getString(TAG_WRITER_PHONENUM);

                writerid[i] = id;
                writerpassword[i] = password;
                writerphonenum[i] = phonenum;
                writernum++;
            }

        }catch(JSONException e){
            Log.d(TAG, "showResult : ", e);
        }
    }
    //
}
