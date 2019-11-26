package com.example.continuousliving;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class LoginCheckActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_check);
        if(SaveStdInformationSharedPreference.getUserName(LoginCheckActivity.this).length() == 0) {
            // call Login Activity
            intent = new Intent(LoginCheckActivity.this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        } else {
            // Call Next Activity
            intent = new Intent(LoginCheckActivity.this, MainActivity.class);
            //다음 액티비티에 자료 넘겨줌
            intent.putExtra("STD_NUM", SaveStdInformationSharedPreference.getUserName(this).toString());
            startActivity(intent);
            this.finish();
        }

    }
}
