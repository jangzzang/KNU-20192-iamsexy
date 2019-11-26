package com.example.continuousliving;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class RegisterActivity extends AppCompatActivity {
    // 회원가입 화면 class

    private EditText register_idText, register_passwordText, register_studentidText, register_usernameText, register_phonenumText;
    private String[] register_idarray = new String[5000];
    private boolean register_checkingid = false;
    private boolean register_finish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_idText = (EditText)findViewById(R.id.register_idText);
        register_passwordText = (EditText)findViewById(R.id.register_passwordText);
        register_studentidText = (EditText)findViewById(R.id.register_studentID);
        register_usernameText = (EditText)findViewById(R.id.register_useName);
        register_phonenumText = (EditText)findViewById(R.id.register_phonenum);
        Button register_button = (Button)findViewById(R.id.register_button);
        register_idarray = getIntent().getExtras().getStringArray("idarray");

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_insert(v);
            }
        });
    }

    public void register_insert(View view){
        String register_id = register_idText.getText().toString();
        String register_password = register_passwordText.getText().toString();
        String register_studentid = register_studentidText.getText().toString();
        String register_username = register_usernameText.getText().toString();
        String register_phonenum = register_phonenumText.getText().toString();

        for(int i =0; i < register_idarray.length ; i++){
            if(register_id.equals(register_idarray[i])) { register_checkingid = true; }
        }

        if(!register_checkingid) {
            Intent register_loginintent = new Intent(RegisterActivity.this,LoginActivity.class);
            register_insertDatabase(register_id, register_password, register_studentid, register_username, register_phonenum);
            Toast.makeText(getApplicationContext(), "신청이 완료되었습니다.", Toast.LENGTH_LONG).show();
            RegisterActivity.this.startActivity(register_loginintent);
        }
        else{
            Toast.makeText(getApplicationContext(), "중복되는 아이디 입니다.", Toast.LENGTH_LONG).show();
            register_checkingid = false;
        }
    }

    public void register_insertDatabase(String id, String password, String studentid, String username, String phonenum){
        class register_insert extends AsyncTask<String, Void, String>{
            ProgressDialog register_loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                register_loading = ProgressDialog.show(RegisterActivity.this, "Please Wait.", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                register_loading.dismiss();
            }

            @Override
            protected String doInBackground(String... strings) {
                try{
                    String register_db_id = (String)strings[0];
                    String register_db_password = (String)strings[1];
                    String register_db_studentid = (String)strings[2];
                    String register_db_username = (String)strings[3];
                    String register_db_phonenum = (String)strings[4];

                    String register_db_link = "http://155.230.25.19/insert.php";
                    String register_data = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(register_db_id, "UTF-8");

                    register_data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(register_db_password, "UTF-8");
                    register_data += "&" + URLEncoder.encode("studentid", "UTF-8") + "=" + URLEncoder.encode(register_db_studentid, "UTF-8");
                    register_data += "&" + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(register_db_username, "UTF-8");
                    register_data += "&" + URLEncoder.encode("phonenum","UTF-8") + "=" + URLEncoder.encode(register_db_phonenum, "UTF-8");

                    URL register_db_url = new URL(register_db_link);
                    URLConnection register_db_conn = register_db_url.openConnection();

                    register_db_conn.setDoOutput(true);
                    OutputStreamWriter register_db_wr = new OutputStreamWriter(register_db_conn.getOutputStream());

                    register_db_wr.write(register_data);
                    register_db_wr.flush();

                    BufferedReader register_db_reader = new BufferedReader(new InputStreamReader(register_db_conn.getInputStream()));
                    StringBuilder register_db_sb = new StringBuilder();
                    String register_db_line = null;

                    while((register_db_line = register_db_reader.readLine())!= null){
                        register_db_sb.append(register_db_line);
                        break;
                    }
                    return register_db_sb.toString();
                }
                catch(Exception e){
                    return new String("Exception : " + e.getMessage());
                }
            }
        }

        register_insert register_task = new register_insert();
        register_task.execute(id, password, studentid, username, phonenum);
    }
}
