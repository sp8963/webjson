package com.example.pingliu.webjson;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ProgressDialog mDialog;
    ListView mListView;
    MainAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading Data...");
        mListView = (ListView) findViewById(R.id.main_listview);
        mAdapter = new MainAdapter(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        loadData();
    }
    private void loadData(){
        String urlString = "http://disp.cc/api/hot_text.json";
        mDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(urlString, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                mDialog.dismiss();
                mDialog.setCancelable(false);
                Toast.makeText(getApplicationContext(),
                        "Success!", Toast.LENGTH_LONG).show();

                Log.d("Hot Text:", response.toString());

                if( response.has("err") && response.optInt("err")!=0 ){
                    Toast.makeText(getApplicationContext(),"Data error", Toast.LENGTH_LONG).show();
                }
                JSONArray list = response.optJSONArray("list");
                if(list==null){
                    Toast.makeText(getApplicationContext(),"Data error", Toast.LENGTH_LONG).show();
                }
                mAdapter.updateData(list);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject error) {
                mDialog.dismiss();
                mDialog.setCancelable(false);
                Toast.makeText(getApplicationContext(),
                        "Error: " + statusCode + " " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
                // Log error message
                Log.e("Hot Text:", statusCode + " " + e.getMessage());
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> a, View view, int position, long id) {
        //從成員mainAdapter中用getItem取出第position項的資料，存成jsonObject
        JSONObject jsonObject = (JSONObject) a.getAdapter().getItem(position);
        //取出我們要的兩個資料 bi 和 ti
        String bi = jsonObject.optString("bi","");
        String ti = jsonObject.optString("ti","");

        // 建立一個 Intent 用來表示要從現在這頁跳到文章閱讀頁 TextActivity
        Intent textIntent = new Intent(this, TextActivity.class);
        // 將要傳遞的資料放進 Intent
        textIntent.putExtra("bi", bi);
        textIntent.putExtra("ti", ti);

        // 使用準備好的 Intent 來開啟新的頁面
        startActivity(textIntent);
    }
}
