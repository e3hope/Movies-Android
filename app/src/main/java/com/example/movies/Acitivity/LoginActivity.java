package com.example.movies.Acitivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.movies.R;
import com.example.movies.Request.LoginRequest;

import org.json.JSONObject;

public class LoginActivity extends Activity {

    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView RGButton = findViewById(R.id.RGButton);

        RGButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reintent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(reintent);
            }
        });
        final EditText EID = findViewById(R.id.IDedit);
        final EditText EPW = findViewById(R.id.PWedit);
        final Button LGButton = findViewById(R.id.LGButton);

        LGButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ID = EID.getText().toString();
                final String Password = EPW.getText().toString();

                Response.Listener<String> responseLister = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                dialog = builder.setMessage("로그인에 성공했습니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                Intent Maintent = new Intent(LoginActivity.this, MainActivity.class);
                                Maintent.putExtra("아이디",ID);
                                Maintent.putExtra("비밀번호",Password);
                                LoginActivity.this.startActivity(Maintent);
                                finish();
                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                dialog = builder.setMessage("계정을 다시 확인하세요.")
                                        .setNegativeButton("다시시도", null)
                                        .create();
                                dialog.show();
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(ID,Password,responseLister);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(dialog !=null){
            dialog.dismiss();
            dialog=null;
        }
    }
}
