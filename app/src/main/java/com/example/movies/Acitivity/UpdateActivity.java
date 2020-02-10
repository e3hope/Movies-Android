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
import com.example.movies.Request.UpdateRequest;

import org.json.JSONObject;

public class UpdateActivity extends Activity {
    private String ID;
    private String Password;
    private String UPassword;
    private String NEWPassword1;
    private String NEWPassword2;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedinstanceStante) {
        super.onCreate(savedinstanceStante);
        setContentView(R.layout.activity_update);
        final EditText EPW=findViewById(R.id.UPWedit);
        final Button PWOKButton=(Button)findViewById(R.id.PWOKButton);
        final Button UpdateButton = findViewById(R.id.UpdateButton);
        final EditText NewPassword = findViewById(R.id.NewPWedit);
        final EditText NewPassword2 = findViewById(R.id.NewPW2edit);
        final TextView UID = findViewById(R.id.UID);

        Intent intent = getIntent();
        ID = intent.getStringExtra("아이디");
        Password = intent.getStringExtra("비밀번호");
        UID.setText(ID+"님의");
        PWOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UPassword = EPW.getText().toString();
                if(UPassword.equals(Password)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
                    dialog = builder.setMessage("변경하실 비밀번호를 입력해주세요.")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    NewPassword.setVisibility(View.VISIBLE);
                    NewPassword2.setVisibility(View.VISIBLE);
                    UpdateButton.setVisibility(View.VISIBLE);
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
                    dialog = builder.setMessage("다시 입력해주시길 바랍니다.")
                            .setNegativeButton("확인", null)
                            .create();
                    dialog.show();
                }
            }
        });
        UpdateButton.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                NEWPassword1 = NewPassword.getText().toString();
                NEWPassword2 = NewPassword2.getText().toString();
                if(NEWPassword1.equals("")||NEWPassword2.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
                    dialog = builder.setMessage("빈칸일 수 없습니다.")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }
                if(NEWPassword1.equals(NEWPassword2)) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) { //회원등록 성공
                                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
                                    dialog = builder.setMessage("비밀번호변경에 성공했습니다")
                                            .setPositiveButton("확인", null)
                                            .create();
                                    dialog.show();
                                    finish();
                                } else { //회원등록 실패
                                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
                                    dialog = builder.setMessage("비밀번호변경에 실패했습니다")
                                            .setNegativeButton("확인", null)
                                            .create();
                                    dialog.show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    UpdateRequest registerRequest = new UpdateRequest(ID,NEWPassword1, responseListener); //회원등록으로 보내줌
                    RequestQueue queue = Volley.newRequestQueue(UpdateActivity.this);
                    queue.add(registerRequest);
                }
            }
        });
    }
}
