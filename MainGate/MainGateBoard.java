package com.example.continuousliving.MainGate;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.continuousliving.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainGateBoard extends AppCompatActivity {
    private static String TAG = "phptest_MainActivity";
    private static final String TAG_MAINGATE_JSON = "maingate";
    private static final String TAG_MAINGATE_NUM = "num";
    private static final String TAG_MAINGATE_TITLE = "title";
    private static final String TAG_MAINGATE_ID = "id";
    private static final String TAG_MAINGATE_PASSWORD = "password";
    private static final String TAG_MAINGATE_NAME = "username";
    private static final String TAG_MAINGATE_TIME = "time";

    private String mJsonString;
    private ListView maingate_boardlistview;
    private MainGateListAdapter maingate_adapter;
    private List<MainGate_Content> maingate_boardlist;

    private String[] maingate_extra_title = new String[5000];
    private String[] maingate_extra_name = new String[5000];
    private String[] maingate_extra_time = new String[5000];
    private String[] maingate_extra_contents = new String[5000];
    private String[] maingate_extra_id = new String[5000];
    private String[] maingate_extra_password = new String[5000];
    private int[] maingate_extra_num = new int[5000];
    private int maingate_extra_size;
    private EditText maingate_search;

    private boolean maingate_lastitemvisibleflag = false;
    private boolean maingate_lock_listview = false;
    private int page = 0;
    private final int offset = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gate_board);

        // Intent 저장값을 가져오는 부분
        maingate_extra_title = getIntent().getExtras().getStringArray("maingate_title");
        maingate_extra_name = getIntent().getExtras().getStringArray("maingate_name");
        maingate_extra_time = getIntent().getExtras().getStringArray("maingate_time");
        maingate_extra_size = getIntent().getExtras().getInt("maingate_size");
        maingate_extra_num = getIntent().getExtras().getIntArray("maingate_num");
        maingate_extra_id = getIntent().getExtras().getStringArray("maingate_id");
        maingate_extra_password = getIntent().getExtras().getStringArray("maingate_password");

        maingate_search = (EditText) findViewById(R.id.maingate_search_text);       // 검색 내용 EditText
        maingate_boardlistview = (ListView) findViewById(R.id.maingate_boardlistview);      // 화면에 보여질 ListView
        maingate_boardlist = new ArrayList<MainGate_Content>();                       // ListView에 띄워질 List 내용

        // 맨 처음 intent로 받아온 값을 설정해주는 부분

        maingate_getItem();

        // write 버튼을 누르면 maingate_write 로 넘어가는 버튼 구현
        ImageButton maingate_write_button = (ImageButton)findViewById(R.id.maingate_write_button);
        maingate_write_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainGateBoard.this, MainGateWrite.class);
                startActivity(intent);
            }
        });



        // 새로고침 버튼을 누르면 새로 고침을 하는 기능 구현
        final ImageButton maingate_refresh_button = (ImageButton)findViewById(R.id.maingate_refresh_button);
        maingate_refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maingate_boardlist.clear();
                Maingate_getdata maingate_refresh_task = new Maingate_getdata();
                maingate_refresh_task.execute("http://155.230.25.19/MaingateGetdata.php");

                page = 0;
                maingate_getItem();
                maingate_adapter.notifyDataSetChanged();
            }
        });


        maingate_adapter = new MainGateListAdapter(getApplicationContext(), maingate_boardlist);
        maingate_boardlistview.setAdapter(maingate_adapter);


        // 각각의 아이템을 클릭시 아이템마다 내용을 다르게 구현하기 위해 intent를 다음 화면으로 넘겨주면서 화면 전환
        maingate_boardlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), MainGateItem.class);
                intent.putExtra("maingate_title", maingate_boardlist.get(position).mgc_title);
                intent.putExtra("maingate_name", maingate_boardlist.get(position).mgc_username);
                intent.putExtra("maingate_date", maingate_boardlist.get(position).mgc_date);
                intent.putExtra("maingate_num", maingate_boardlist.get(position).mgc_num);
                intent.putExtra("maingate_id", maingate_boardlist.get(position).mgc_id);
                intent.putExtra("maingate_password", maingate_boardlist.get(position).mgc_password);

                startActivity(intent);
                maingate_refresh_button.performClick();
            }
        });

        maingate_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String search_text = maingate_search.getText().toString();
                maingate_search(search_text);
            }
        });
    }


    private void maingate_getItem(){
        maingate_lock_listview = true;
        maingate_boardlistview.setClickable(false);

        for(int i=0;i<offset;i++){
            if((page * offset) + i >= maingate_extra_size) { break; }

            maingate_boardlist.add(new MainGate_Content(
                    maingate_extra_title[(page * offset) + i],
                    maingate_extra_id[(page * offset) + i],
                    maingate_extra_password[(page * offset) + i],
                    maingate_extra_name[(page * offset) + i],
                    maingate_extra_time[(page * offset) + i],
                    maingate_extra_contents[(page * offset) + i],
                    maingate_extra_num[(page * offset) + i]
                    )
            );
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                maingate_adapter.notifyDataSetChanged();
                maingate_lock_listview = false;
                maingate_boardlistview.setClickable(true);
            }
        }, 0);
    }

    // JSON 형식으로 데이터를 받아오는 부분
    private class Maingate_getdata extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainGateBoard.this, "Please wait.",null, true, true);
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



    // JSON으로 받아온 배열을 String 배열로 나눠주면서 저장하는 부분
    private void showResult(){
        try{
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_MAINGATE_JSON);

            maingate_extra_size = jsonArray.length();
            for(int i =0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);

                Integer number = item.getInt(TAG_MAINGATE_NUM);
                String title = item.getString(TAG_MAINGATE_TITLE);
                String id = item.getString(TAG_MAINGATE_ID);
                String password = item.getString(TAG_MAINGATE_PASSWORD);
                String username = item.getString(TAG_MAINGATE_NAME);
                String time = item.getString(TAG_MAINGATE_TIME);

                maingate_extra_title[i] = title;
                maingate_extra_name[i] = username;
                maingate_extra_time[i] = time;
                maingate_extra_num[i] = number;
                maingate_extra_id[i] = id;
                maingate_extra_password[i] = password;
            }

        }catch(JSONException e){
            Log.d(TAG, "showResult : ", e);
        }

    }



    // 검색기능 구현한 method text와 비교하면서 매번 어떤 걸 보여줄지 바꿔줌
    public void maingate_search(String charText){
        maingate_boardlist.clear();

        if(charText.length()==0){
            for(int i=0;i<((page + 1) * offset);i++){
                if(i >= maingate_extra_size) { break; }
                maingate_boardlist.add(new MainGate_Content(
                        maingate_extra_title[i],
                        maingate_extra_id[i],
                        maingate_extra_password[i],
                        maingate_extra_name[i],
                        maingate_extra_time[i],
                        maingate_extra_contents[i],
                        maingate_extra_num[i])
                );
            }
        }else{
            for(int i=0;i<maingate_extra_size;i++){
                if(maingate_extra_title[i].toLowerCase().contains(charText)){
                    maingate_boardlist.add(new MainGate_Content(
                            maingate_extra_title[i],
                            maingate_extra_id[i],
                            maingate_extra_password[i],
                            maingate_extra_name[i],
                            maingate_extra_time[i],
                            maingate_extra_contents[i],
                            maingate_extra_num[i])
                    );
                }
            }
        }

        maingate_adapter.notifyDataSetChanged();
    }
}
