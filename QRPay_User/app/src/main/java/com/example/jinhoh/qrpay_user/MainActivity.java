package com.example.jinhoh.qrpay_user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //서버와 연결한 IP주소
    private static String IP_ADDRESS = "ec2-13-124-143-232.ap-northeast-2.compute.amazonaws.com";
    private static String TAG = "phptest";


    String ckID, ckPW;
    EditText etID, etPW;
    Button btLogin, btJoin;
    String jsonresult;

    //파싱용 변수
    String num, id, pwd, nickname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etID = (EditText) findViewById(R.id.etID);
        etPW = (EditText) findViewById(R.id.etPW);

        btLogin = (Button) findViewById(R.id.btLogin);
        btJoin = (Button) findViewById(R.id.btJoin);



        //로그인
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ckID = etID.getText().toString();
                ckPW = etPW.getText().toString();

                GetData task = new GetData();
                task.execute("http://" + IP_ADDRESS + "/getjson.php", ckID);
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

    private class GetData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            Log.d(TAG, "response - " + result);

            if (result == null) {
                Log.d(TAG, "php에러 메세지 - " + errorString);
            } else {
                //파싱으로 보낸다.
                jsonresult = result;
                showResult();

                if(ckPW.equals(pwd)){
                    Intent intent = new Intent(getApplicationContext(), main_formActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "다시 로그인해 주세요.", Toast.LENGTH_LONG).show();
                    etID.setText("");
                    etPW.setText("");
                }
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String id = (String) params[1];
            String postParameters = "id=" + id;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    //파싱하는 메소드
    private void showResult() {

        String TAG_JSON = "qrpay";
        String TAG_NUM = "num";
        String TAG_ID = "id";
        String TAG_PWD = "pwd";
        String TAG_NICKNAME = "nickname";


        try {
            JSONObject jsonObject = new JSONObject(jsonresult);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject item = jsonArray.getJSONObject(i);

                num = item.getString(TAG_NUM);
                id = item.getString(TAG_ID);
                pwd = item.getString(TAG_PWD);
                nickname = item.getString(TAG_NICKNAME);
            }


        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }

    }


}
