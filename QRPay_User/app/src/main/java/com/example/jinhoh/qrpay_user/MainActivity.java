package com.example.jinhoh.qrpay_user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText etID, etPW;
    Button btLogin, btJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etID = (EditText) findViewById(R.id.etID);
        etPW = (EditText) findViewById(R.id.etPW);

        btLogin = (Button) findViewById(R.id.btLogin);
        btJoin = (Button) findViewById(R.id.btJoin);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DB연동
                Intent intent = new Intent(getApplicationContext(), main_formActivity.class);
                startActivity(intent);
            }
        });

        btJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), new_customerActivity.class);
                startActivity(intent);
            }
        });

    }
}
