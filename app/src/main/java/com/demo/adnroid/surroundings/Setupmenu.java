package com.demo.adnroid.surroundings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Setupmenu extends AppCompatActivity {

    EditText set_temperature,set_pm25,set_humidity;

    Switch aSwitch;
    Switch sw_temp,sw_pm25,sw_rh;

    String sw_temp_data = "off",sw_pm25_data = "off",sw_rh_data = "off";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setupmenu);

        findviews();

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Log.d("Setupmenu","onCheckedChanged");

                if(isChecked){
                    set_temperature.setText(getSharedPreferences("sensorData", MODE_PRIVATE).getString("temperatureAve", ""));
                    set_pm25.setText(getSharedPreferences("sensorData",MODE_PRIVATE).getString("pm25Ave",""));
                    set_humidity.setText(getSharedPreferences("sensorData",MODE_PRIVATE).getString("rhAve",""));
                }else{
                    set_temperature.setText("");
                    set_pm25.setText("");
                    set_humidity.setText("");
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("Setupmenu","onStart");

        set_temperature.setText(getSharedPreferences("setup",MODE_PRIVATE).getString("temperature",""));
        set_pm25.setText(getSharedPreferences("setup",MODE_PRIVATE).getString("pm25",""));
        set_humidity.setText(getSharedPreferences("setup",MODE_PRIVATE).getString("humidity",""));

        sw_temp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    sw_temp_data = "on";
                }else{
                    sw_temp_data = "off";
                }
            }
        });

        sw_pm25.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    sw_pm25_data = "on";
                }else{
                    sw_pm25_data = "off";
                }
            }
        });

        sw_rh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    sw_rh_data = "on";
                }else {
                    sw_rh_data = "off";
                }
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d("Setupmenu","onPostResume");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d("Setupmenu","onStop");

        SharedPreferences preferences = getSharedPreferences("setup",MODE_PRIVATE);

        preferences.edit()
                .putString("temperature",set_temperature.getText().toString())
                .putString("pm25",set_pm25.getText().toString())
                .putString("humidity",set_humidity.getText().toString())
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Setupmenu","onDestroy");
    }

    private void findviews(){
        set_temperature = findViewById(R.id.et_temperture);
        set_pm25 = findViewById(R.id.et_pm25);
        set_humidity = findViewById(R.id.et_Humidity);
        aSwitch = findViewById(R.id.switchAuto);
        sw_temp = findViewById(R.id.sw_temp);
        sw_pm25 = findViewById(R.id.sw_pm25);
        sw_rh = findViewById(R.id.sw_rh);
    }


    public void setup_set(View view) {
        setup_db_write();
    }

    public void setup_clear(View view) {
        set_temperature.setText("");
        set_pm25.setText("");
        set_humidity.setText("");
    }

    public void setup_next(View view) {
        Intent intent = new Intent(Setupmenu.this,Mainmenu.class);
        startActivity(intent);
    }

    public void setup_return(View view) {
        Intent intent = new Intent(Setupmenu.this,Login.class);
        startActivity(intent);
    }

    public void setup_db_write(){
        String temp_temperature = set_temperature.getText().toString();
        String temp_pm25 = set_pm25.getText().toString();
        String temp_humidity = set_humidity.getText().toString();

        if(temp_temperature.equals("")||temp_humidity.equals("")||temp_pm25.equals("")){
            Toast.makeText(this,"不可為空白資料",Toast.LENGTH_LONG).show();

        }else {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url_address_list.api_get_set_class1 + "temper=" + temp_temperature + "&pm25=" + temp_pm25 + "&humid=" + temp_humidity +"&fan=" + sw_temp_data + "&dehum=" + sw_rh_data + "&filter=" + sw_pm25_data)
                    .build();

            Call call = client.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(Setupmenu.this, "網路連線異常", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String message = response.body().string().trim();

                    Log.d("Setupmenu", message);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (message.equals("true")) {
                                Toast.makeText(Setupmenu.this, "資料設定成功", Toast.LENGTH_LONG).show();

                            } else if (message.equals("false")) {
                                Toast.makeText(Setupmenu.this, "資料設定失敗", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(Setupmenu.this, "伺服器回應異常", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });
        }

    }
}
