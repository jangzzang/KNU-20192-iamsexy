package com.example.continuousliving.MainGate;

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

public class MainGateChange extends AppCompatActivity {

    private String maingate_change_title;
    private String maingate_change_content;
    private EditText maingate_change_title_text;
    private EditText maingate_change_content_text;
    private Button maingate_change_finishbutton;

    private int maingate_change_primarykey;
    private String maingate_change_prevtitle;
    private String maingate_change_prevcontents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gate_change);

        maingate_change_finishbutton = (Button)findViewById(R.id.maingate_change_finishbutton_x);
        maingate_change_title_text = (EditText)findViewById(R.id.maingate_change_title_x);
        maingate_change_content_text = (EditText)findViewById(R.id.maingate_change_content_x);

        // 수정하려던 글의 원래 제목, 내용, key값을 intent로 넘겨받아옴
        maingate_change_prevtitle = getIntent().getExtras().getString("prevtitle");
        maingate_change_prevcontents = getIntent().getExtras().getString("prevcontents");
        maingate_change_primarykey = getIntent().getExtras().getInt("prevnum");


        // EditText부분을 추가해줌
        maingate_change_title_text.append(maingate_change_prevtitle);
        maingate_change_content_text.append(maingate_change_prevcontents);


        // 수정완료 버튼을 누르면 기능을 구현한 부분
        maingate_change_finishbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maingate_change_title = maingate_change_title_text.getText().toString();
                maingate_change_content = maingate_change_content_text.getText().toString();

                if(maingate_change_title.length() == 0){
                    Toast.makeText(getApplicationContext(), "제목을 입력하십시오.", Toast.LENGTH_SHORT).show();
                }else if(maingate_change_content.length() == 0){
                    Toast.makeText(getApplicationContext(), "내용을 입력하십시오", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "수정 완료", Toast.LENGTH_SHORT).show();
                    maingate_change_insert(v);
                    finish();
                }
            }
        });
    }


    // 해당 method가 실행 시에 변수가 바뀌면서 database에 change를 요청함
    private void maingate_change_insert(View view){
        String maingate_change_nexttitle = maingate_change_title;
        String maingate_change_nextcontents = maingate_change_content;
        maingate_changeDatabase(maingate_change_nexttitle, maingate_change_nextcontents, maingate_change_primarykey);
    }


    // php파일과 연결하여 바뀌는 부분을 UPDATE query문으로 내용을 갱신함
    private void maingate_changeDatabase(String title, String contents, int primarykey){
        class maingate_change extends AsyncTask<String, Void, String> {
            ProgressDialog maingate_change_loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                maingate_change_loading = ProgressDialog.show(MainGateChange.this, "Please Wait.", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                maingate_change_loading.dismiss();
            }

            @Override
            protected String doInBackground(String... strings) {
                try{
                    String notice_change_db_title = (String)strings[0];
                    String notice_change_db_contents = (String)strings[1];
                    String notice_change_db_num = (String)strings[2];

                    String notice_change_db_link = "http://155.230.25.19/MaingateRewrite.php";
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

        maingate_change maingate_change_task = new maingate_change();
        maingate_change_task.execute(title, contents, Integer.toString(primarykey));
    }
}
