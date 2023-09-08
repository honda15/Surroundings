package com.demo.adnroid.surroundings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Today extends AppCompatActivity {

    TextView today_date;
    TextView today_loc;
    TextView today_temp;
    TextView today_humd;
    TextView today_lat;
    TextView today_lon;
    TextView today_title;

    ImageView img;

    Button today_next,today_up;
    int j = 0;
    int itemSelect = 0;
    boolean itemSelectValue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);
        findviews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Today","onStart");

        today_date.setText(getSharedPreferences("today",MODE_PRIVATE).getString("today_date",""));
        today_loc.setText(getSharedPreferences("today",MODE_PRIVATE).getString("today_loc",""));
        today_temp.setText(getSharedPreferences("today",MODE_PRIVATE).getString("today_temp",""));
        today_humd.setText(getSharedPreferences("today",MODE_PRIVATE).getString("today_humd",""));
        today_lat.setText(getSharedPreferences("today",MODE_PRIVATE).getString("today_lat",""));
        today_lon.setText(getSharedPreferences("today",MODE_PRIVATE).getString("today_lon",""));

        String temp1  = getSharedPreferences("today",MODE_PRIVATE).getString("itemNum","");
        j = Integer.parseInt(temp1);
        String temp2  = getSharedPreferences("today",MODE_PRIVATE).getString("itemSet","");
        itemSelectValue = Boolean.valueOf(temp2);

        if(itemSelectValue){
            today_loc.setTextColor(Color.rgb(255,0,0));
        }
        getWeather(j);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Today","onStop");
        SharedPreferences preferences = getSharedPreferences("today",MODE_PRIVATE);
        preferences.edit()
                .putString("itemNum", String.valueOf(j))
                .putString("itemSet", String.valueOf(itemSelectValue))
                .putString("today_date",today_date.getText().toString())
                .putString("today_loc",today_loc.getText().toString())
                .putString("today_temp",today_temp.getText().toString())
                .putString("today_humd",today_humd.getText().toString())
                .putString("today_lat",today_lat.getText().toString())
                .putString("today_lon",today_lon.getText().toString())
                .commit();
    }

    public void onPause(){
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Today","onDestroy");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        today_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lock = "已設定地區,無法跳頁顯示";
                if(itemSelectValue) {
                    Toast.makeText(Today.this,lock,Toast.LENGTH_SHORT).show();

                }else{
                    j++;
                    if (j > 11) { j = 0; }
                    getWeather(j);
                    changeDrawable();
                }
            }
        });

        today_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(itemSelectValue){
                    Toast.makeText(Today.this,"已設定地區,無法跳頁顯示",Toast.LENGTH_SHORT).show();
                }else {
                    j--;
                    if (j < 0) { j = 11; }
                    getWeather(j);
                    changeDrawable();
                }
            }
        });
        Log.d("Today","onPostResume");

    }

    int [] drawableList = {R.drawable.today0,R.drawable.today1,R.drawable.today2,R.drawable.today3,
            R.drawable.today4,R.drawable.today5,R.drawable.today6,R.drawable.today7,
            R.drawable.today8,R.drawable.today9,R.drawable.today10,R.drawable.today11,};

    public void changeDrawable(){
        img.setImageResource(drawableList[j]);
    }

    public void findviews(){
        today_date = findViewById(R.id.tv_today_date);
        today_loc = findViewById(R.id.tv_today_loc);
        today_temp = findViewById(R.id.tv_today_temp);
        today_humd = findViewById(R.id.tv_today_humd);
        today_lat = findViewById(R.id.tv_today_lat);
        today_lon = findViewById(R.id.tv_today_lon);
        today_next = findViewById(R.id.btn_today_next);
        today_up = findViewById(R.id.btn_today_up);
        today_title = findViewById(R.id.today_title);
        img = findViewById(R.id.imageView);
    }


    public void today_update_Click(View view) {
            Log.d("Today","onClick_update");
            getWeather(j);
    }

    public void today_previous(View view) {
        finish();
    }

    void getWeather(final int i){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(url_address_list.api_get_weather_class1)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                Log.d("Today",e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String[] tokens = e.toString().split(":");
                        Toast.makeText(Today.this,"連線結果回應:" + tokens[1],Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Log.d("Today", response.toString());//debug
                String message = response.body().string();
                Log.d("Today", message);//debug
                //--------------------------------------------------------------------------------
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<weather>>() {}.getType();
                final ArrayList<weather> w = gson.fromJson(message, listType);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        today_date.setText(w.get(i).getObsTime());
                        today_loc.setText(w.get(i).getLocationName());
                        today_temp.setText(w.get(i).getTEMP());
                        today_humd.setText(w.get(i).getHUMD());
                        today_lat.setText(w.get(i).getLat());
                        today_lon.setText(w.get(i).getLon());
                    }
                });

            }
        });
    }

    public void today_gototop(View view) {
        Intent intent = new Intent(Today.this,Login.class);
        startActivity(intent);
    }


    public void today_setItem(View view) {
        Log.d("today","today_setItem");
        itemSelect();
    }

    void itemSelect(){

        if(itemSelectValue) {
            today_loc.setTextColor(Color.rgb(255, 255, 255));
            itemSelect = 0;
            itemSelectValue = false ;

        }else{
            today_loc.setTextColor(Color.rgb(255, 0, 0));
            itemSelect = j;
            itemSelectValue = true ;
        }
    }
}


