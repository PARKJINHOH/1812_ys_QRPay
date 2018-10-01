package com.example.jinhoh.qrpay_user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class new_customerActivity extends AppCompatActivity {

    EditText etNickName, etJoinId, etJoinPw, etCheckPw;
    Button btJoin, btCancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_customer);

        etNickName = (EditText) findViewById(R.id.etNickName);
        etJoinId = (EditText) findViewById(R.id.etJoinId);
        etJoinPw = (EditText) findViewById(R.id.etJoinPw);
        etCheckPw = (EditText) findViewById(R.id.etCheckPw);

        btJoin = (Button) findViewById(R.id.btJoin);
        btCancel = (Button) findViewById(R.id.btCancel);

        btJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (etJoinPw.equals(etCheckPw)) {
//                    try {
//
//                    } catch (Exception e) {
//                        Toast.makeText(getApplicationContext(), "동일한 사용자가 있습니다.", Toast.LENGTH_LONG).show();
//                    }
//                } else {
//                    etJoinPw.setText("");
//                    etCheckPw.setText("");
//                    Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show();
//                }

            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
