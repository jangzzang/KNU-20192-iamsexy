package com.example.continuousliving.SideGate;

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

public class SideGateBoard extends AppCompatActivity {
    private static String TAG = "phptest_MainActivity";
    private static final String TAG_SIDEGATE_JSON = "sidegate";
    private static final String TAG_SIDEGATE_NUM = "num";
    private static final String TAG_SIDEGATE_TITLE = "title";
    private static final String TAG_SIDEGATE_ID = "id";
    private static final String TAG_SIDEGATE_PASSWORD = "password";
    private static final String TAG_SIDEGATE_NAME = "username";
    private static final String TAG_SIDEGATE_TIME = "time";

    private String mJsonString;
    private ListView sidegate_boardlistview;
    private SideGateListAdapter sidegate_adapter;
    private List<SideGate_Content> sidegate_boardlist;

    private String[] sidegate_extra_title = new String[5000];
    private String[] sidegate_extra_name = new String[5000];
    private String[] sidegate_extra_time = new String[5000];
    private String[] sidegate_extra_contents = new String[5000];
    private String[] sidegate_extra_id = new String[5000];
    private String[] sidegate_extra_password = new String[5000];
    private int[] sidegate_extra_num = new int[5000];
    private int sidegate_extra_size;
    private EditText sidegate_search;

    private boolean sidegate_lastitemvisibleflag = false;
    private boolean sidegate_lock_listview = false;
    private int page = 0;
    private final int offset = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_gate_board);

        // Intent 저장값을 가져오는 부분
        sidegate_extra_title = getIntent().getExtras().getStringArray("sidegate_title");
        sidegate_extra_name = getIntent().getExtras().getStringArray("sidegate_name");
        sidegate_extra_time = getIntent().getExtras().getStringArray("sidegate_time");
        sidegate_extra_size = getIntent().getExtras().getInt("sidegate_size");
        sidegate_extra_num = getIntent().getExtras().getIntArray("sidegate_num");
        sidegate_extra_id = getIntent().getExtras().getStringArray("sidegate_id");
        sidegate_extra_password = getIntent().getExtras().getStringArray("sidegate_password");

        sidegate_search = (EditText) findViewById(R.id.sidegate_search_text);       // 검색 내용 EditText
        sidegate_boardlistview = (ListView) findViewById(R.id.sidegate_boardlistview);      // 화면에 보여질 ListView
        sidegate_boardlist = new ArrayList<SideGate_Content>();                       // ListView에 띄워질 List 내용

        // 맨 처음 intent로 받아온 값을 설정해주는 부분

        sidegate_getItem();

        // write 버튼을 누르면 sidegate_write 로 넘어가는 버튼 구현
        ImageButton sidegate_write_button = (ImageButton)findViewById(R.id.sidegate_write_button);
        sidegate_write_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SideGateBoard.this, SideGateWrite.class);
                startActivity(intent);
            }
        });



        // 새로고침 버튼을 누르면 새로 고침을 하는 기능 구현
        final ImageButton sidegate_refresh_button = (ImageButton)findViewById(R.id.sidegate_refresh_button);
        sidegate_refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sidegate_boardlist.clear();
                Sidegate_getdata sidegate_refresh_task = new Sidegate_getdata();
                sidegate_refresh_task.execute("http://155.230.25.19/SidegateGetdata.php");

                page = 0;
                sidegate_getItem();
                sidegate_adapter.notifyDataSetChanged();
            }
        });


        sidegate_adapter = new SideGateListAdapter(getApplicationContext(), sidegate_boardlist);
        sidegate_boardlistview.setAdapter(sidegate_adapter);


        // 각각의 아이템을 클릭시 아이템마다 내용을 다르게 구현하기 위해 intent를 다음 화면으로 넘겨주면서 화면 전환
        sidegate_boardlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), SideGateItem.class);
                intent.putExtra("sidegate_title", sidegate_boardlist.get(position).sgc_title);
                intent.putExtra("sidegate_name", sidegate_boardlist.get(position).sgc_username);
                intent.putExtra("sidegate_date", sidegate_boardlist.get(position).sgc_date);
                intent.putExtra("sidegate_num", sidegate_boardlist.get(position).sgc_num);
                intent.putExtra("sidegate_id", sidegate_boardlist.get(position).sgc_id);
                intent.putExtra("sidegate_password", sidegate_boardlist.get(position).sgc_password);

                startActivity(intent);
                sidegate_refresh_button.performClick();
            }
        });

        sidegate_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String search_text = sidegate_search.getText().toString();
                sidegate_search(search_text);
            }
        });
    }


    private void sidegate_getItem(){
        sidegate_lock_listview = true;
        sidegate_boardlistview.setClickable(false);

        for(int i=0;i<offset;i++){
            if((page * offset) + i >= sidegate_extra_size) { break; }

            sidegate_boardlist.add(new SideGate_Content(
                            sidegate_extra_title[(page * offset) + i],
                            sidegate_extra_id[(page * offset) + i],
                            sidegate_extra_password[(page * offset) + i],
                            sidegate_extra_name[(page * offset) + i],
                            sidegate_extra_time[(page * offset) + i],
                            sidegate_extra_contents[(page * offset) + i],
                            sidegate_extra_num[(page * offset) + i]
                    )
            );
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                sidegate_adapter.notifyDataSetChanged();
                sidegate_lock_listview = false;
                sidegate_boardlistview.setClickable(true);
            }
        }, 0);
    }

    // JSON 형식으로 데이터를 받아오는 부분
    private class Sidegate_getdata extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(SideGateBoard.this, "Please wait.",null, true, true);
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
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_SIDEGATE_JSON);

            sidegate_extra_size = jsonArray.length();
            for(int i =0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);

                Integer number = item.getInt(TAG_SIDEGATE_NUM);
                String title = item.getString(TAG_SIDEGATE_TITLE);
                String id = item.getString(TAG_SIDEGATE_ID);
                String password = item.getString(TAG_SIDEGATE_PASSWORD);
                String username = item.getString(TAG_SIDEGATE_NAME);
                String time = item.getString(TAG_SIDEGATE_TIME);

                sidegate_extra_title[i] = title;
                sidegate_extra_name[i] = username;
                sidegate_extra_time[i] = time;
                sidegate_extra_num[i] = number;
                sidegate_extra_id[i] = id;
                sidegate_extra_password[i] = password;
            }

        }catch(JSONException e){
            Log.d(TAG, "showResult : ", e);
        }

    }



    // 검색기능 구현한 method text와 비교하면서 매번 어떤 걸 보여줄지 바꿔줌
    public void sidegate_search(String charText){
        sidegate_boardlist.clear();

        if(charText.length()==0){
            for(int i=0;i<((page + 1) * offset);i++){
                if(i >= sidegate_extra_size) { break; }
                sidegate_boardlist.add(new SideGate_Content(
                        sidegate_extra_title[i],
                        sidegate_extra_id[i],
                        sidegate_extra_password[i],
                        sidegate_extra_name[i],
                        sidegate_extra_time[i],
                        sidegate_extra_contents[i],
                        sidegate_extra_num[i])
                );
            }
        }else{
            for(int i=0;i<sidegate_extra_size;i++){
                if(sidegate_extra_title[i].toLowerCase().contains(charText)){
                    sidegate_boardlist.add(new SideGate_Content(
                            sidegate_extra_title[i],
                            sidegate_extra_id[i],
                            sidegate_extra_password[i],
                            sidegate_extra_name[i],
                            sidegate_extra_time[i],
                            sidegate_extra_contents[i],
                            sidegate_extra_num[i])
                    );
                }
            }
        }

        sidegate_adapter.notifyDataSetChanged();
    }
}
