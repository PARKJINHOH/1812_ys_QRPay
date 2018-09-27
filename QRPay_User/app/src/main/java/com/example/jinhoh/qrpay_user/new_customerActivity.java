package com.example.jinhoh.qrpay_user;

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
                try {
//                    if (password.equals(ckpassword)) {
//                        String sql = "insert into member values(?,?);";
//                        Object[] args = {id, password};
//                        mydb.execSQL(sql, args);
//                        mydb.close();
//                        try {
//                            String coinsql = "insert into coin(coin_id) values(?);";
//                            Object[] argscoin = {id};
//                            coindb.execSQL(coinsql, argscoin);
//                            coindb.close();
//
//                            String coinpricesql = "insert into coinprice(coin_price_id) values(?);";
//                            coinpricedb.execSQL(coinpricesql, argscoin);
//                            coinpricedb.close();
//
//
//                        } catch (Exception e) {
//                            coindb.close();
//                            coinpricedb.close();
//                            Toast.makeText(getApplicationContext(), "DB생성 실패", Toast.LENGTH_LONG).show();
//                        }
//                        Toast.makeText(getApplicationContext(), id + "님 회원가입을 축하드립니다", Toast.LENGTH_LONG).show();
//                        finish();
//                    } else {
//                        edtMemberPW.setText("");
//                        edtMemberPWCheck.setText("");
//                        Toast.makeText(getApplicationContext(), "비밀번호를 확인해주세요.", Toast.LENGTH_LONG).show();
//                        mydb.close();
//                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "동일한 사용자가 있습니다.", Toast.LENGTH_LONG).show();
                }
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
