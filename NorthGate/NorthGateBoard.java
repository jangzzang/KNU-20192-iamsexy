package com.example.continuousliving.NorthGate;

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

public class NorthGateBoard extends AppCompatActivity {
    private static String TAG = "phptest_MainActivity";
    private static final String TAG_NORTHGATE_JSON = "northgate";
    private static final String TAG_NORTHGATE_NUM = "num";
    private static final String TAG_NORTHGATE_TITLE = "title";
    private static final String TAG_NORTHGATE_ID = "id";
    private static final String TAG_NORTHGATE_PASSWORD = "password";
    private static final String TAG_NORTHGATE_NAME = "username";
    private static final String TAG_NORTHGATE_TIME = "time";

    private String mJsonString;
    private ListView northgate_boardlistview;
    private NorthGateListAdapter northgate_adapter;
    private List<NorthGate_Content> northgate_boardlist;

    private String[] northgate_extra_title = new String[5000];
    private String[] northgate_extra_name = new String[5000];
    private String[] northgate_extra_time = new String[5000];
    private String[] northgate_extra_contents = new String[5000];
    private String[] northgate_extra_id = new String[5000];
    private String[] northgate_extra_password = new String[5000];
    private int[] northgate_extra_num = new int[5000];
    private int northgate_extra_size;
    private EditText northgate_search;

    private boolean northgate_lastitemvisibleflag = false;
    private boolean northgate_lock_listview = false;
    private int page = 0;
    private final int offset = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_north_gate_board);

        // Intent 저장값을 가져오는 부분
        northgate_extra_title = getIntent().getExtras().getStringArray("northgate_title");
        northgate_extra_name = getIntent().getExtras().getStringArray("northgate_name");
        northgate_extra_time = getIntent().getExtras().getStringArray("northgate_time");
        northgate_extra_size = getIntent().getExtras().getInt("northgate_size");
        northgate_extra_num = getIntent().getExtras().getIntArray("northgate_num");
        northgate_extra_id = getIntent().getExtras().getStringArray("northgate_id");
        northgate_extra_password = getIntent().getExtras().getStringArray("northgate_password");

        northgate_search = (EditText) findViewById(R.id.northgate_search_text);       // 검색 내용 EditText
        northgate_boardlistview = (ListView) findViewById(R.id.northgate_boardlistview);      // 화면에 보여질 ListView
        northgate_boardlist = new ArrayList<NorthGate_Content>();                       // ListView에 띄워질 List 내용

        // 맨 처음 intent로 받아온 값을 설정해주는 부분

        northgate_getItem();

        // write 버튼을 누르면 northgate_write 로 넘어가는 버튼 구현
        ImageButton northgate_write_button = (ImageButton)findViewById(R.id.northgate_write_button);
        northgate_write_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NorthGateBoard.this, NorthGateWrite.class);
                startActivity(intent);
            }
        });



        // 새로고침 버튼을 누르면 새로 고침을 하는 기능 구현
        final ImageButton northgate_refresh_button = (ImageButton)findViewById(R.id.northgate_refresh_button);
        northgate_refresh_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                northgate_boardlist.clear();
                Northgate_getdata northgate_refresh_task = new Northgate_getdata();
                northgate_refresh_task.execute("http://155.230.25.19/NorthgateGetdata.php");

                page = 0;
                northgate_getItem();
                northgate_adapter.notifyDataSetChanged();
            }
        });


        northgate_adapter = new NorthGateListAdapter(getApplicationContext(), northgate_boardlist);
        northgate_boardlistview.setAdapter(northgate_adapter);


        // 각각의 아이템을 클릭시 아이템마다 내용을 다르게 구현하기 위해 intent를 다음 화면으로 넘겨주면서 화면 전환
        northgate_boardlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), NorthGateItem.class);
                intent.putExtra("northgate_title", northgate_boardlist.get(position).ngc_title);
                intent.putExtra("northgate_name", northgate_boardlist.get(position).ngc_username);
                intent.putExtra("northgate_date", northgate_boardlist.get(position).ngc_date);
                intent.putExtra("northgate_num", northgate_boardlist.get(position).ngc_num);
                intent.putExtra("northgate_id", northgate_boardlist.get(position).ngc_id);
                intent.putExtra("northgate_password", northgate_boardlist.get(position).ngc_password);

                startActivity(intent);
                northgate_refresh_button.performClick();
            }
        });

        northgate_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String search_text = northgate_search.getText().toString();
                northgate_search(search_text);
            }
        });
    }


    private void northgate_getItem(){
        northgate_lock_listview = true;
        northgate_boardlistview.setClickable(false);

        for(int i=0;i<offset;i++){
            if((page * offset) + i >= northgate_extra_size) { break; }

            northgate_boardlist.add(new NorthGate_Content(
                            northgate_extra_title[(page * offset) + i],
                            northgate_extra_id[(page * offset) + i],
                            northgate_extra_password[(page * offset) + i],
                            northgate_extra_name[(page * offset) + i],
                            northgate_extra_time[(page * offset) + i],
                            northgate_extra_contents[(page * offset) + i],
                            northgate_extra_num[(page * offset) + i]
                    )
            );
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                page++;
                northgate_adapter.notifyDataSetChanged();
                northgate_lock_listview = false;
                northgate_boardlistview.setClickable(true);
            }
        }, 0);
    }

    // JSON 형식으로 데이터를 받아오는 부분
    private class Northgate_getdata extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(NorthGateBoard.this, "Please wait.",null, true, true);
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
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_NORTHGATE_JSON);

            northgate_extra_size = jsonArray.length();
            for(int i =0;i<jsonArray.length();i++){
                JSONObject item = jsonArray.getJSONObject(i);

                Integer number = item.getInt(TAG_NORTHGATE_NUM);
                String title = item.getString(TAG_NORTHGATE_TITLE);
                String id = item.getString(TAG_NORTHGATE_ID);
                String password = item.getString(TAG_NORTHGATE_PASSWORD);
                String username = item.getString(TAG_NORTHGATE_NAME);
                String time = item.getString(TAG_NORTHGATE_TIME);

                northgate_extra_title[i] = title;
                northgate_extra_name[i] = username;
                northgate_extra_time[i] = time;
                northgate_extra_num[i] = number;
                northgate_extra_id[i] = id;
                northgate_extra_password[i] = password;
            }

        }catch(JSONException e){
            Log.d(TAG, "showResult : ", e);
        }

    }



    // 검색기능 구현한 method text와 비교하면서 매번 어떤 걸 보여줄지 바꿔줌
    public void northgate_search(String charText){
        northgate_boardlist.clear();

        if(charText.length()==0){
            for(int i=0;i<((page + 1) * offset);i++){
                if(i >= northgate_extra_size) { break; }
                northgate_boardlist.add(new NorthGate_Content(
                        northgate_extra_title[i],
                        northgate_extra_id[i],
                        northgate_extra_password[i],
                        northgate_extra_name[i],
                        northgate_extra_time[i],
                        northgate_extra_contents[i],
                        northgate_extra_num[i])
                );
            }
        }else{
            for(int i=0;i<northgate_extra_size;i++){
                if(northgate_extra_title[i].toLowerCase().contains(charText)){
                    northgate_boardlist.add(new NorthGate_Content(
                            northgate_extra_title[i],
                            northgate_extra_id[i],
                            northgate_extra_password[i],
                            northgate_extra_name[i],
                            northgate_extra_time[i],
                            northgate_extra_contents[i],
                            northgate_extra_num[i])
                    );
                }
            }
        }

        northgate_adapter.notifyDataSetChanged();
    }
}
