package com.example.continuousliving.WestGate;

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

public class WestGateBoard extends AppCompatActivity {
    private static String TAG = "phptest_MainActivity";
    private static final String TAG_WESTGATE_JSON = "westgate";
    private static final String TAG_WESTGATE_NUM = "num";
    private static final String TAG_WESTGATE_TITLE = "title";
    private static final String TAG_WESTGATE_ID = "id";
    private static final String TAG_WESTGATE_PASSWORD = "password";
    private static final String TAG_WESTGATE_NAME = "username";
    private static final String TAG_WESTGATE_TIME = "time";

    private String mJsonString;
    private ListView westgate_boardlistview;
    private WestGateListAdapter westgate_adapter;
    private List<WestGate_Content> westgate_boardlist;

    private String[] westgate_extra_title = new String[5000];
    private String[] westgate_extra_name = new String[5000];
    private String[] westgate_extra_time = new String[5000];
    private String[] westgate_extra_contents = new String[5000];
    private String[] westgate_extra_id = new String[5000];
    private String[] westgate_extra_password = new String[5000];
    private int[] westgate_extra_num = new int[5000];
    private int westgate_extra_size;
    private EditText westgate_search;

    private boolean westgate_lastitemvisibleflag = false;
    private boolean westgate_lock_listview = false;
    private int page = 0;
    private final int offset = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_west_gate_board);

        // Intent 저장값을 가져오는 부분
        westgate_extra_title = getIntent().getExtras().getStringArray("westgate_title");
        westgate_extra_name = getIntent().getExtras().getStringArray("westgate_name");
        westgate_extra_time = getIntent().getExtras().getStringArray("westgate_time");
        westgate_extra_size = getIntent().getExtras().getInt("westgate_size");
        westgate_extra_num = getIntent().getExtras().getIntArray("westgate_num");
        westgate_extra_id = getIntent().getExtras().getStringArray("westgate_id");
        westgate_extra_password = getIntent().getExtras().getStringArray("westgate_password");

        westgate_search = (EditText) findViewById(R.id.westgate_search_text);       // 검색 내용 EditText
        westgate_boardlistview = (ListView) findViewById(R.id.westgate_boardlistview);      // 화면에 보여질 ListView
        westgate_boardlist = new ArrayList<WestGate_Content>();                       // ListView에 띄워질 List 내용

        // 맨 처음 intent로 받아온 값을 설정해주는 부분

        westgate_getItem();

        // write 버튼을 누르면 westgate_write 로 넘어가는 버튼 구현
        ImageButton westgate_write_button = (ImageButton)findViewById(R.id.westgate_write_button);
        westgate_write_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WestGateBoard.this, WestGateWrite.class);
                startActivity(intent);
            }
        });



        // 새로고침 버튼을 누르면 새로 고침을 하는 기능 구현
        final ImageButton westgate_refresh_button = (ImageButton)findViewById(R.id.westgate_refresh_button);
        westgate_refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                westgate_boardlist.clear();
                Westgate_getdata westgate_refresh_task = new Westgate_getdata();
                westgate_refresh_task.execute("http://155.230.25.19/WestgateGetdata.php");

                page = 0;
                westgate_getItem();
                westgate_adapter.notifyDataSetChanged();
            }
        });


        westgate_adapter = new WestGateListAdapter(getApplicationContext(), westgate_boardlist);
        westgate_boardlistview.setAdapter(westgate_adapter);


        // 각각의 아이템을 클릭시 아이템마다 내용을 다르게 구현하기 위해 intent를 다음 화면으로 넘겨주면서 화면 전환
        westgate_boardlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), WestGateItem.class);
                intent.putExtra("westgate_title", westgate_boardlist.get(position).wgc_title);
                intent.putExtra("westgate_name", westgate_boardlist.get(position).wgc_username);
                intent.putExtra("westgate_date", westgate_boardlist.get(position).wgc_date);
                intent.putExtra("westgate_num", westgate_boardlist.get(position).wgc_num);
                intent.putExtra("westgate_id", westgate_boardlist.get(position).wgc_id);
                intent.putExtra("westgate_password", westgate_boardlist.get(position).wgc_password);

                startActivity(intent);
                westgate_refresh_button.performClick();
            }
        });

        westgate_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String search_text = westgate_search.getText().toString();
                westgate_search(search_text);
            }
        });
    }


    private void westgate_getItem(){
        westgate_lock_listview = true;
        westgate_boardlistview.setClickable(false);

        for(int i=0;i<offset;i++){
            if((page * offset) + i >= westgate_extra_size) { break; }

            westgate_boardlist.add(new WestGate_Content(
                    westgate_extra_title[(page * offset) + i],
                    westgate_extra_id[(page * offset) + i],
                    westgate_extra_password[(page * offset) + i],
                    westgate_extra_name[(page * offset) + i],
                    westgate_extra_time[(page * offset) + i],
                    westgate_extra_contents[(page * offset) + i],
                    westgate_extra_num[(page * offset) + i]
                    )
            );
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                westgate_adapter.notifyDataSetChanged();
                westgate_lock_listview = false;
                westgate_boardlistview.setClickable(true);
            }
        }, 0);
    }

    // JSON 형식으로 데이터를 받아오는 부분
    private class Westgate_getdata extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(WestGateBoard.this, "Please wait.",null, true, true);
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
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_WESTGATE_JSON);

            westgate_extra_size = jsonArray.length();
            for(int i =0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);

                Integer number = item.getInt(TAG_WESTGATE_NUM);
                String title = item.getString(TAG_WESTGATE_TITLE);
                String id = item.getString(TAG_WESTGATE_ID);
                String password = item.getString(TAG_WESTGATE_PASSWORD);
                String username = item.getString(TAG_WESTGATE_NAME);
                String time = item.getString(TAG_WESTGATE_TIME);

                westgate_extra_title[i] = title;
                westgate_extra_name[i] = username;
                westgate_extra_time[i] = time;
                westgate_extra_num[i] = number;
                westgate_extra_id[i] = id;
                westgate_extra_password[i] = password;
            }

        }catch(JSONException e){
            Log.d(TAG, "showResult : ", e);
        }

    }



    // 검색기능 구현한 method text와 비교하면서 매번 어떤 걸 보여줄지 바꿔줌
    public void westgate_search(String charText){
        westgate_boardlist.clear();

        if(charText.length()==0){
            for(int i=0;i<((page + 1) * offset);i++){
                if(i >= westgate_extra_size) { break; }
                westgate_boardlist.add(new WestGate_Content(
                        westgate_extra_title[i],
                        westgate_extra_id[i],
                        westgate_extra_password[i],
                        westgate_extra_name[i],
                        westgate_extra_time[i],
                        westgate_extra_contents[i],
                        westgate_extra_num[i])
                );
            }
        }else{
            for(int i=0;i<westgate_extra_size;i++){
                if(westgate_extra_title[i].toLowerCase().contains(charText)){
                    westgate_boardlist.add(new WestGate_Content(
                            westgate_extra_title[i],
                            westgate_extra_id[i],
                            westgate_extra_password[i],
                            westgate_extra_name[i],
                            westgate_extra_time[i],
                            westgate_extra_contents[i],
                            westgate_extra_num[i])
                    );
                }
            }
        }

        westgate_adapter.notifyDataSetChanged();
    }
}
