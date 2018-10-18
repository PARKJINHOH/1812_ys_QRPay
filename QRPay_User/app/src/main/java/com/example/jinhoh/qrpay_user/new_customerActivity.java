package com.example.jinhoh.qrpay_user;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class new_customerActivity extends AppCompatActivity {

    //서버와 연결한 IP주소
    private static String IP_ADDRESS = "ec2-13-124-143-232.ap-northeast-2.compute.amazonaws.com";
    private static String TAG = "phptest";

    EditText etNickName, etJoinId, etJoinPw, etCheckPw;
    Button btJoin, btCancel;
    String id, pwd, pwdck, nickname;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_customer);

        etJoinId = (EditText) findViewById(R.id.etJoinId);
        etJoinPw = (EditText) findViewById(R.id.etJoinPw);
        etCheckPw = (EditText) findViewById(R.id.etCheckPw);
        etNickName = (EditText) findViewById(R.id.etNickName);

        btJoin = (Button) findViewById(R.id.btJoin);
        btCancel = (Button) findViewById(R.id.btCancel);


        // 버튼 이벤트
        btJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id = etJoinId.getText().toString();
                pwd = etJoinPw.getText().toString();
                pwdck = etCheckPw.getText().toString();
                nickname = etNickName.getText().toString();

                if (pwd.equals(pwdck)) {
                    InsertData task = new InsertData();
                    task.execute("http://" + IP_ADDRESS + "/insert.php", id, pwd, nickname);

                    etJoinId.setText("");
                    etJoinPw.setText("");
                    etCheckPw.setText("");
                    etNickName.setText("");
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다.", Toast.LENGTH_LONG).show();
                }

                etJoinId.setText("");
                etJoinPw.setText("");
                etCheckPw.setText("");
                etNickName.setText("");
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(new_customerActivity.this, "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            // 1. PHP 파일을 실행시킬 수 있는 주소와 전송할 데이터를 준비합니다.
            // POST 방식으로 데이터 전달시에는 데이터가 주소에 직접 입력되지 않습니다.
            String serverURL = (String) params[0];

            String id = (String) params[1];
            String pwd = (String) params[2];
            String nickname = (String) params[3];

            // HTTP 메시지 본문에 포함되어 전송되기 때문에 따로 데이터를 준비해야 합니다.
            // 전송할 데이터는 “이름=값” 형식이며 여러 개를 보내야 할 경우에는 항목 사이에 &를 추가합니다.
            // 여기에 적어준 이름을 나중에 PHP에서 사용하여 값을 얻게 됩니다.
            String postParameters = "id=" + id + "&pwd=" + pwd + "&nickname=" + nickname;


            try {
                // 2. HttpURLConnection 클래스를 사용하여 POST 방식으로 데이터를 전송합니다.
                URL url = new URL(serverURL);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000); //5초안에 응답이 오지 않으면 예외가 발생합니다.
                httpURLConnection.setConnectTimeout(5000); //5초안에 연결이 안되면 예외가 발생합니다.
                httpURLConnection.setRequestMethod("POST"); //요청 방식을 POST로 합니다.
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                // 응답을 읽습니다.
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    // 정상적인 응답 데이터
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    // 에러 발생
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();

            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }
}
