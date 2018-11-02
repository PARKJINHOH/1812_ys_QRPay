package com.example.jinhoh.qrpay_user;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class main_formActivity extends AppCompatActivity {

    //서버와 연결한 IP주소
    private static String IP_ADDRESS = "ec2-13-124-143-232.ap-northeast-2.compute.amazonaws.com";
    private static String TAG = "LOG";

    ImageView IvQRCodeScan, Ivrefresh;
    String num, id, nickname;
    TextView MYnickname, MYmoney;
    Uri uri;
    String jsonresult, usermoney;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_form);

        MYnickname = (TextView) findViewById(R.id.MYnickname);
        MYmoney = (TextView) findViewById(R.id.MYmoney);
        Ivrefresh = (ImageView) findViewById(R.id.Ivrefresh);

        IvQRCodeScan = (ImageView) findViewById(R.id.IvQRCodeScan);

        Intent intent = getIntent();
        num = intent.getStringExtra("num");
        id = intent.getStringExtra("id");
        nickname = intent.getStringExtra("nickname");

        MYnickname.setText(nickname + "님");

        GetMoneyData task = new GetMoneyData();
        task.execute("http://" + IP_ADDRESS + "/user_money.php", id);

        Ivrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetMoneyData task = new GetMoneyData();
                task.execute("http://" + IP_ADDRESS + "/user_money.php", id);
            }
        });

        //QR코드 스캔
        IvQRCodeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQRcode();
            }
        });
    }

    // qr코드 스캔 메소드
    public void startQRcode() {
        new IntentIntegrator(this).initiateScan();
    }

    // qr코드 스캔된 뒤, 호출 된 값
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result == null) {
                // 취소됨
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                // 스캔된 QRCode --> result.getContents()
                try {
                    Log.d(TAG, "QRScan - " + result.getContents() + "id=" + id + "num=" + num);
                    uri = Uri.parse(result.getContents() + "id=" + id + "num=" + num);
                    Intent intenturi = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intenturi);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private class GetMoneyData extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(main_formActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            Log.d(TAG, "response - " + result);

            //서버에 error가 있으면 json을 못받아 올 때,(null값이라도 받으면 else로)
            if (result == null) {
                Log.d(TAG, "php에러 메세지 - " + errorString);
            } else {
                //파싱으로 보낸다.
                jsonresult = result;
                showResult();
                MYmoney.setText("잔액: " + usermoney + "원");
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
        String TAG_MONEY = "money";

        try {
            JSONObject jsonObject = new JSONObject(jsonresult);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                usermoney = item.getString(TAG_MONEY);
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

}
