package com.demo.adnroid.surroundings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.midi.MidiDeviceService;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Mainmenu extends AppCompatActivity {

    TextView tv_date;
    TextView temp_now,pm_now,rh_now;
    TextView temp_top,temp_low,temp_avg;
    TextView pm_top,pm_low,pm_avg;
    TextView rh_top,rh_low,rh_avg;

    String menuTempData = "sensorData";

    int set_temp_value_max = 30;
    int set_temp_value_min = 20;
    int set_pm_value_max = 35;
    int set_pm_value_min = 20;
    int set_rh_value_max = 60;
    int set_rh_value_min = 30;

    Handler handler = new Handler();//10秒更新
    Runnable refresh;//10秒更新

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        findviews();
    }

    private void findviews(){
        tv_date = findViewById(R.id.tv_date);
        temp_now = findViewById(R.id.temp_now);
        pm_now = findViewById(R.id.pm_now);
        rh_now = findViewById(R.id.rh_now);
        temp_top = findViewById(R.id.temp_top);
        temp_low = findViewById(R.id.temp_low);
        temp_avg = findViewById(R.id.temp_avg);
        pm_top = findViewById(R.id.pm_top);
        pm_low = findViewById(R.id.pm_low);
        pm_avg = findViewById(R.id.pm_avg);
        rh_top = findViewById(R.id.rh_top);
        rh_low = findViewById(R.id.rh_low);
        rh_avg = findViewById(R.id.rh_avg);
    }

    private void saveRefData(){
        SharedPreferences preferences = getSharedPreferences(menuTempData,MODE_PRIVATE);
        preferences.edit()
                .putString("dateLater",tv_date.getText().toString())
                .putString("temperatureNow",temp_now.getText().toString())
                .putString("pm25Now",pm_now.getText().toString())
                .putString("rhNow",rh_now.getText().toString())
                .putString("temperatureTop",temp_top.getText().toString())
                .putString("temperatureLow",temp_low.getText().toString())
                .putString("temperatureAve",temp_avg.getText().toString())
                .putString("mp25Top",pm_top.getText().toString())
                .putString("pm25Low",pm_low.getText().toString())
                .putString("pm25Ave",pm_avg.getText().toString())
                .putString("rhTop",rh_top.getText().toString())
                .putString("rhLow",rh_low.getText().toString())
                .putString("rhAve",rh_avg.getText().toString())
                .commit();
    }

    private void getRefData(){
        tv_date.setText(getSharedPreferences(menuTempData,MODE_PRIVATE).getString("dateLater",""));
        temp_now.setText(getSharedPreferences(menuTempData,MODE_PRIVATE).getString("temperatureNow",""));
        pm_now.setText(getSharedPreferences(menuTempData,MODE_PRIVATE).getString("pm25Now",""));
        rh_now.setText(getSharedPreferences(menuTempData,MODE_PRIVATE).getString("rhNow",""));
        temp_top.setText(getSharedPreferences(menuTempData,MODE_PRIVATE).getString("temperatureTop",""));
        temp_low.setText(getSharedPreferences(menuTempData,MODE_PRIVATE).getString("temperatureLow",""));
        temp_avg.setText(getSharedPreferences(menuTempData,MODE_PRIVATE).getString("temperatureAve",""));
        pm_top.setText(getSharedPreferences(menuTempData,MODE_PRIVATE).getString("mp25Top",""));
        pm_low.setText(getSharedPreferences(menuTempData,MODE_PRIVATE).getString("pm25Low",""));
        pm_avg.setText(getSharedPreferences(menuTempData,MODE_PRIVATE).getString("pm25Ave",""));
        rh_top.setText(getSharedPreferences(menuTempData,MODE_PRIVATE).getString("rhTop",""));
        rh_low.setText(getSharedPreferences(menuTempData,MODE_PRIVATE).getString("rhLow",""));
        rh_avg.setText(getSharedPreferences(menuTempData,MODE_PRIVATE).getString("rhAve",""));
    }

    public void sensor_toLogin(View view) {
        Intent intent = new Intent(Mainmenu.this,Login.class);
        startActivity(intent);//go 登入頁面
    }

    public void sensor_toDay(View view) {
        Intent intent = new Intent(Mainmenu.this,Today.class);
        startActivity(intent);//go 今日天氣
    }

    public void sensor_toSetup(View view) {
        Intent intent = new Intent(Mainmenu.this,Setupmenu.class);
        startActivity(intent);//go 環境設定
    }

    public void sensor_saving(View view) {
        saveRefData();//儲存資料至手機
        Toast.makeText(Mainmenu.this,"資料儲存成功",Toast.LENGTH_SHORT).show();
    }

    public void getMainmenuData(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url_address_list.api_get_mysql_class1)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Mainmenu.this,"連線異常,請重新檢查",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                DecimalFormat decimalFormat = new DecimalFormat("0.00");//浖點格式化
                String message = response.body().string();

                Gson gson = new Gson();

                Type listType = new TypeToken<ArrayList<esp_data>>() {}.getType();
                ArrayList<esp_data> jsonArr = gson.fromJson(message, listType);

                tv_date.setText(jsonArr.get(0).getReading_time());
                temp_now.setText(decimalFormat.format(Double.parseDouble(jsonArr.get(0).getValue1())));
                pm_now.setText(decimalFormat.format(Double.parseDouble(jsonArr.get(0).getPm25())));
                rh_now.setText(decimalFormat.format(Double.parseDouble(jsonArr.get(0).getValue2())));

                Log.d("OKHTTP",String.valueOf(jsonArr.size()));//檢查json資料筆數

                //--------------------------------------------------------------------------
                float max=0,min=50,avg;
                float sum=0;

                for(int i = 0; i<jsonArr.size(); i++){

                    float a = Float.parseFloat(jsonArr.get(i).getValue1());

                    if(a > max){ max = a; }
                    if(a < min){ min = a; }

                    sum += Float.parseFloat(jsonArr.get(i).getValue1());
                }
                avg = sum/jsonArr.size();

                temp_avg.setText(decimalFormat.format(Double.parseDouble(String.valueOf(avg))));
                temp_top.setText(decimalFormat.format(Double.parseDouble(String.valueOf(max))));
                temp_low.setText(decimalFormat.format(Double.parseDouble(String.valueOf(min))));
                //---------------------------------------------------------------------------
                float max0=0,min0=50,avg0;
                float sum0=0;

                for(int i = 0; i<jsonArr.size(); i++){

                    float a = Float.parseFloat(jsonArr.get(i).getPm25());

                    if(a > max0){ max0 = a; }
                    if(a < min0){ min0 = a; }

                    sum0 += Float.parseFloat(jsonArr.get(i).getPm25());
                }
                avg0 = sum0/jsonArr.size();

                pm_avg.setText(decimalFormat.format(Float.parseFloat(String.valueOf(avg0))));
                pm_top.setText(decimalFormat.format(Float.parseFloat(String.valueOf(max0))));
                pm_low.setText(decimalFormat.format(Float.parseFloat(String.valueOf(min0))));

                //---------------------------------------------------------------------------
                float max1=0,min1=50,avg1;
                float sum1=0;

                for(int i = 0; i<jsonArr.size(); i++){

                    float a = Float.parseFloat(jsonArr.get(i).getValue2());

                    if(a > max1){ max1 = a; }
                    if(a < min1){ min1 = a; }

                    sum1 += Float.parseFloat(jsonArr.get(i).getValue2());
                }
                avg1 = sum1/jsonArr.size();

                rh_avg.setText(decimalFormat.format(Float.parseFloat(String.valueOf(avg1))));
                rh_top.setText(decimalFormat.format(Float.parseFloat(String.valueOf(max1))));
                rh_low.setText(decimalFormat.format(Float.parseFloat(String.valueOf(min1))));

                textColorModify();//依條件,設定字體顏色
            }
        });
        //------------------------------------------------------------------------------------
    }

    public void textColorModify(){
        float t = Float.parseFloat(String.valueOf(temp_now.getText()));
        float p = Float.parseFloat(String.valueOf(pm_now.getText()));
        float r = Float.parseFloat(String.valueOf(rh_now.getText()));

        float t1 = Float.parseFloat(String.valueOf(temp_top.getText()));
        float t2 = Float.parseFloat(String.valueOf(temp_low.getText()));
        float t3 = Float.parseFloat(String.valueOf(temp_avg.getText()));

        float p1 = Float.parseFloat(String.valueOf(pm_top.getText()));
        float p2 = Float.parseFloat(String.valueOf(pm_low.getText()));
        float p3 = Float.parseFloat(String.valueOf(pm_avg.getText()));

        float r1 = Float.parseFloat(String.valueOf(rh_top.getText()));
        float r2 = Float.parseFloat(String.valueOf(rh_low.getText()));
        float r3 = Float.parseFloat(String.valueOf(rh_avg.getText()));
        //-------------------------------------------------------------------------------------
        if(t > set_temp_value_max){
            temp_now.setTextColor(Color.rgb(255,0,0));
        }else if(t < set_temp_value_min){
            temp_now.setTextColor(Color.rgb(0,0,255));
        }else{
            temp_now.setTextColor(Color.rgb(0,255,0));
        }

        if(p > set_pm_value_max){
            pm_now.setTextColor(Color.rgb(255,0,0));
        }else if(p < set_pm_value_min){
            pm_now.setTextColor(Color.rgb(0,0,255));
        }else{
            pm_now.setTextColor(Color.rgb(0,255,0));
        }

        if(r > set_rh_value_max){
            rh_now.setTextColor(Color.rgb(255,0,0));
        }else if(r < set_rh_value_min){
            rh_now.setTextColor(Color.rgb(0,0,255));
        }else{
            rh_now.setTextColor(Color.rgb(0,255,0));
        }
        //-------------------------------------------------------------------------------------
        if(t1 > set_temp_value_max){
            temp_top.setTextColor(Color.rgb(255,0,0));
        }else if(t < set_temp_value_min){
            temp_top.setTextColor(Color.rgb(0,0,255));
        }else{
            temp_top.setTextColor(Color.rgb(0,255,0));
        }

        if(t2 > set_temp_value_max){
            temp_low.setTextColor(Color.rgb(255,0,0));
        }else if(p < set_temp_value_min){
            temp_low.setTextColor(Color.rgb(0,0,255));
        }else{
            temp_low.setTextColor(Color.rgb(0,255,0));
        }

        if(t3 > set_temp_value_max){
            temp_avg.setTextColor(Color.rgb(255,0,0));
        }else if(r < set_temp_value_min){
            temp_avg.setTextColor(Color.rgb(0,0,255));
        }else{
            temp_avg.setTextColor(Color.rgb(0,255,0));
        }
        //-------------------------------------------------------------------------------------
        if(p1 > set_pm_value_max){
            pm_top.setTextColor(Color.rgb(255,0,0));
        }else if(t < set_pm_value_min){
            pm_top.setTextColor(Color.rgb(0,0,255));
        }else{
            pm_top.setTextColor(Color.rgb(0,255,0));
        }

        if(p2 > set_pm_value_max){
            pm_low.setTextColor(Color.rgb(255,0,0));
        }else if(p < set_pm_value_min){
            pm_low.setTextColor(Color.rgb(0,0,255));
        }else{
            pm_low.setTextColor(Color.rgb(0,255,0));
        }

        if(p3 > set_pm_value_max){
            pm_avg.setTextColor(Color.rgb(255,0,0));
        }else if(r < set_pm_value_min){
            pm_avg.setTextColor(Color.rgb(0,0,255));
        }else{
            pm_avg.setTextColor(Color.rgb(0,255,0));
        }
        //-------------------------------------------------------------------------------------
        if(r1 > set_rh_value_max){
            rh_top.setTextColor(Color.rgb(255,0,0));
        }else if(t < set_pm_value_min){
            rh_top.setTextColor(Color.rgb(0,0,255));
        }else{
            rh_top.setTextColor(Color.rgb(0,255,0));
        }

        if(r2 > set_rh_value_max){
            rh_low.setTextColor(Color.rgb(255,0,0));
        }else if(p < set_rh_value_min){
            rh_low.setTextColor(Color.rgb(0,0,255));
        }else{
            rh_low.setTextColor(Color.rgb(0,255,0));
        }

        if(r3 > set_rh_value_max){
            rh_avg.setTextColor(Color.rgb(255,0,0));
        }else if(r < set_rh_value_min){
            rh_avg.setTextColor(Color.rgb(0,0,255));
        }else{
            rh_avg.setTextColor(Color.rgb(0,255,0));
        }
        //-------------------------------------------------------------------------------------
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d("Mainmenu","onPostResume");

        //10秒更新-------------------------------------------------------------------------------
        refresh = new Runnable() {
            @Override
            public void run() {

                handler.postDelayed(refresh,10000);
                getMainmenuData();
                Toast.makeText(Mainmenu.this,"10秒更新資料",Toast.LENGTH_SHORT).show();
            }
        };
        handler.post(refresh);
        //10秒更新-------------------------------------------------------------------------------

        //getRefData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(refresh);//關閉10秒更新
    }
}
