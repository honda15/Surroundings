package com.demo.adnroid.surroundings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Build extends AppCompatActivity {

    EditText user_data;
    EditText passd_data;
    EditText phone_data;
    EditText email_data;
    EditText remark_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build);

        findviews();
    }

    void findviews(){
        user_data = findViewById(R.id.et_user_data);
        passd_data = findViewById(R.id.et_passd_data);
        phone_data = findViewById(R.id.et_phone_data);
        email_data = findViewById(R.id.et_email_data);
        remark_data = findViewById(R.id.et_remark_data);
    }

    public void return_top(View view) {
        finish();
    }

    public void data_clear(View view) {
        user_data.setText("");
        passd_data.setText("");
        phone_data.setText("");
        email_data.setText("");
        remark_data.setText("");
    }

    public void data_build(View view) {
        String user_tmp = user_data.getText().toString();
        String passd_tmp = passd_data.getText().toString();
        String phone_tmp = phone_data.getText().toString();
        String email_tmp = email_data.getText().toString();
        String remark_tmp = remark_data.getText().toString();
        //------------------------------------------------------------------------------------------
        if(user_tmp.equals("")||passd_tmp.equals("")||phone_tmp.equals("")||email_tmp.equals("")||remark_tmp.equals("")){

            Toast.makeText(Build.this,"填寫資料不可為空",Toast.LENGTH_LONG).show();

            //------------------------------------------------------------------------------------------
        }else {

            OkHttpClient client = new OkHttpClient();
            FormBody formBody = new FormBody.Builder()

                    .add("user", user_data.getText().toString())
                    .add("password", passd_data.getText().toString())
                    .add("phone", phone_data.getText().toString())
                    .add("email", email_data.getText().toString())
                    .add("remark", remark_data.getText().toString())
                    .build();

            Request request = new Request.Builder()
                    .url(url_address_list.api_post_data_class1)
                    .post(formBody)
                    .build();

            Call call = client.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("message", e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    //Log.d("message", response.toString());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(Build.this)
                                    .setTitle("帳號建立結果")
                                    .setMessage("個人資料已建立")
                                    .setPositiveButton("回首頁",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    Intent intent = new Intent(Build.this, Login.class);
                                                    startActivity(intent);
                                                }
                                            })
                                    .show();

                        }
                    });

                }
            });
        }
    }
}
