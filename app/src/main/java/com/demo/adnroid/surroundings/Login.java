package com.demo.adnroid.surroundings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceControl;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Login extends AppCompatActivity {

    EditText user_name;
    EditText pass_word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findviews();
    }

    void findviews(){
        user_name = findViewById(R.id.et_user);
        pass_word = findViewById(R.id.et_passd);
    }

    public void send(View view) {
        //------------------------------------------------------------------------------------------
        String user_tmp = user_name.getText().toString();
        String passd_tmp = pass_word.getText().toString();

        if (user_tmp.equals("") || passd_tmp.equals("")) {
            Toast.makeText(Login.this, "帳號或密碼不可為空", Toast.LENGTH_SHORT).show();

        } else {
            //--------------------------------------------------------------------------------------
            OkHttpClient client = new OkHttpClient();//OKHttp POST

            FormBody formBody = new FormBody.Builder()
                    .add("user", user_name.getText().toString())
                    .add("password", pass_word.getText().toString())
                    .build();

            Request request = new Request.Builder()
                    .url(url_address_list.api_check_account_class1)
                    .post(formBody)
                    .build();

            Call call = client.newCall(request);
            //--------------------------------------------------------------------------------------
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    Log.d("Login",e.toString());//debug

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(Login.this, "伺服器連線失敗", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String message = response.body().string().trim();

                    Log.d("Login", message);//debug

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (message.equals("true")) {
                                Toast.makeText(Login.this, "帳號登入成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login.this,Mainmenu.class);
                                startActivity(intent);

                            }else if(message.equals("false")){
                                Toast.makeText(Login.this, "帳號登入失敗", Toast.LENGTH_LONG).show();

                            }else{
                                Toast.makeText(Login.this, "資料庫異常回應", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }


    }

    public void clear(View view) {

        user_name.setText("");

        pass_word.setText("");
    }

    public void build(View view) {

        Intent intent = new Intent(Login.this,Build.class);

        startActivity(intent);
    }
}

