package com.example.continuousliving.NorthGate;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.continuousliving.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class NorthGateChange extends AppCompatActivity {

    private String northgate_change_title;
    private String northgate_change_content;
    private EditText northgate_change_title_text;
    private EditText northgate_change_content_text;
    private Button northgate_change_finishbutton;

    private int northgate_change_primarykey;
    private String northgate_change_prevtitle;
    private String northgate_change_prevcontents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_north_gate_change);

        northgate_change_finishbutton = (Button)findViewById(R.id.northgate_change_finishbutton_x);
        northgate_change_title_text = (EditText)findViewById(R.id.northgate_change_title_x);
        northgate_change_content_text = (EditText)findViewById(R.id.northgate_change_content_x);

        // 수정하려던 글의 원래 제목, 내용, key값을 intent로 넘겨받아옴
        northgate_change_prevtitle = getIntent().getExtras().getString("prevtitle");
        northgate_change_prevcontents = getIntent().getExtras().getString("prevcontents");
        northgate_change_primarykey = getIntent().getExtras().getInt("prevnum");


        // EditText부분을 추가해줌
        northgate_change_title_text.append(northgate_change_prevtitle);
        northgate_change_content_text.append(northgate_change_prevcontents);


        // 수정완료 버튼을 누르면 기능을 구현한 부분
        northgate_change_finishbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                northgate_change_title = northgate_change_title_text.getText().toString();
                northgate_change_content = northgate_change_content_text.getText().toString();

                if(northgate_change_title.length() == 0){
                    Toast.makeText(getApplicationContext(), "제목을 입력하십시오.", Toast.LENGTH_SHORT).show();
                }else if(northgate_change_content.length() == 0){
                    Toast.makeText(getApplicationContext(), "내용을 입력하십시오", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "수정 완료", Toast.LENGTH_SHORT).show();
                    northgate_change_insert(v);
                    finish();
                }
            }
        });
    }


    // 해당 method가 실행 시에 변수가 바뀌면서 database에 change를 요청함
    private void northgate_change_insert(View view){
        String northgate_change_nexttitle = northgate_change_title;
        String northgate_change_nextcontents = northgate_change_content;
        northgate_changeDatabase(northgate_change_nexttitle, northgate_change_nextcontents, northgate_change_primarykey);
    }


    // php파일과 연결하여 바뀌는 부분을 UPDATE query문으로 내용을 갱신함
    private void northgate_changeDatabase(String title, String contents, int primarykey){
        class northgate_change extends AsyncTask<String, Void, String> {
            ProgressDialog northgate_change_loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                northgate_change_loading = ProgressDialog.show(NorthGateChange.this, "Please Wait.", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                northgate_change_loading.dismiss();
            }

            @Override
            protected String doInBackground(String... strings) {
                try{
                    String notice_change_db_title = (String)strings[0];
                    String notice_change_db_contents = (String)strings[1];
                    String notice_change_db_num = (String)strings[2];

                    String notice_change_db_link = "http://155.230.25.19/NorthgateRewrite.php";
                    String notice_change_db_data = URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(notice_change_db_title, "UTF-8");
                    notice_change_db_data += "&" + URLEncoder.encode("contents", "UTF-8") + "=" + URLEncoder.encode(notice_change_db_contents, "UTF-8");
                    notice_change_db_data += "&" + URLEncoder.encode("num", "UTF-8") + "=" + URLEncoder.encode(notice_change_db_num, "UTF-8");

                    URL notice_change_db_url = new URL(notice_change_db_link);
                    URLConnection notice_change_db_conn = notice_change_db_url.openConnection();

                    notice_change_db_conn.setDoOutput(true);
                    OutputStreamWriter notice_change_db_wr = new OutputStreamWriter(notice_change_db_conn.getOutputStream());

                    notice_change_db_wr.write(notice_change_db_data);
                    notice_change_db_wr.flush();

                    BufferedReader notice_change_db_reader = new BufferedReader(new InputStreamReader(notice_change_db_conn.getInputStream()));
                    StringBuilder notice_change_db_sb = new StringBuilder();
                    String notice_change_db_line = null;

                    while((notice_change_db_line = notice_change_db_reader.readLine())!= null){
                        notice_change_db_sb.append(notice_change_db_line);
                        break;
                    }
                    return notice_change_db_sb.toString();
                }
                catch(Exception e){
                    return new String("Exception : " + e.getMessage());
                }
            }
        }

        northgate_change northgate_change_task = new northgate_change();
        northgate_change_task.execute(title, contents, Integer.toString(primarykey));
    }
}
