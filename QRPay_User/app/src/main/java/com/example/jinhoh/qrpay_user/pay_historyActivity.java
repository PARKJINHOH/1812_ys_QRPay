package com.example.jinhoh.qrpay_user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class pay_historyActivity extends AppCompatActivity {

    //서버와 연결한 IP주소
    private static String IP_ADDRESS = "ec2-13-209-98-128.ap-northeast-2.compute.amazonaws.com";
    private static String TAG = "phptest";

    Button BTback;
    ImageView deleteHistory;

    //리스트뷰
    ListView listView;
    ListViewAdapter listViewAdapter;
    ArrayList<pay_historyBean> pay_historyBeans;
    pay_historyBean historyBean;

    String jsonresult;
    String id;

    //json 뽑아온거
    String orderName;
    String orderPrice;
    String orderStore;
    String orderDate;

    GeOrderHistory task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_history);

        listView = (ListView) findViewById(R.id.ListView);
        BTback = (Button) findViewById(R.id.BTback);
        deleteHistory = (ImageView) findViewById(R.id.deleteHistory);


        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        task = new GeOrderHistory();
        task.execute("http://" + IP_ADDRESS + "/user_orderHistory.php", id);

        // 돌아가기 버튼
        BTback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deleteHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteOrderHistory del = new DeleteOrderHistory();
                del.execute("http://" + IP_ADDRESS + "/user_orderHistoryDel.php", id);
                recreate();
            }
        });
    }

    private class GeOrderHistory extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(pay_historyActivity.this,
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

    private class DeleteOrderHistory extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(pay_historyActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();

            Log.d(TAG, "response - " + result);

            Toast.makeText(getApplicationContext(), "주문 내역이 삭제되었습니다.", Toast.LENGTH_LONG).show();
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

    private void showResult() {

        String TAG_JSON = "orderHistory";

        String TAG_ORDERNAME = "orderHis_orderName";
        String TAG_PRICE = "orderHis_price";
        String TAG_STORE = "orderHis_store";
        String TAG_DATE = "orderHis_date";

        try {
            JSONObject jsonObject = new JSONObject(jsonresult);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            pay_historyBeans = new ArrayList<pay_historyBean>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                orderName = item.getString(TAG_ORDERNAME);
                orderPrice = item.getString(TAG_PRICE);
                orderStore = item.getString(TAG_STORE);
                orderDate = item.getString(TAG_DATE);

                SimpleDateFormat dateParserIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                SimpleDateFormat dateParserOut = new SimpleDateFormat("yyyy/MM/dd HH시 mm분");
                try {
                    Log.d("orderDate1", orderDate);
                    Date inputDate = dateParserIn.parse(orderDate);
                    orderDate = dateParserOut.format(inputDate);
                    Log.d("orderDate2", orderDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                historyBean = new pay_historyBean(orderDate, orderStore, orderName, orderPrice);
                pay_historyBeans.add(historyBean);
            }

            listViewAdapter = new ListViewAdapter(getApplicationContext(), pay_historyBeans);
            listView.setAdapter(listViewAdapter);
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }
}
