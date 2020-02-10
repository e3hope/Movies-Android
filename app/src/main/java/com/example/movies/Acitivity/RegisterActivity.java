package com.example.movies.Acitivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.movies.R;
import com.example.movies.Request.RegisterRequest;
import com.example.movies.Request.ValidateRequest;

import org.json.JSONObject;


public class RegisterActivity extends Activity {
    private String ID;
    private String Password;
    private String Name;
    private String Email;
    private AlertDialog dialog;
    private boolean validate = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText REID= findViewById(R.id.RIDedit);
        final EditText REPassword=findViewById(R.id.RPWedit);
        final EditText REName=findViewById(R.id.RNamedit);
        final EditText REEmail=findViewById(R.id.REmailedit);
        Button OK=findViewById(R.id.OKButton);
        final Button VaildateButton=(Button)findViewById(R.id.CHButton);
        //REName.setPrivateImeOptions("defaultInputMode=korean;");

        VaildateButton.setOnClickListener(new View.OnClickListener() { //중복체크버튼
            @Override
            public void onClick(View v) {
                String ID = REID.getText().toString();
                if (validate) {
                    return;
                }
                if (ID.equals("")) { //빈칸입력시
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("빈칸일 수 없습니다.")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 있는 아이디입니다.")
                                        .setPositiveButton("확인",null)
                                        .create();
                                dialog.show();
                                REID.setEnabled(false);
                                validate = true;
                                REID.setBackgroundColor(getResources().getColor(R.color.colorGray));
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("사용할 수 없는 아이디입니다.")
                                        .setPositiveButton("확인",null)
                                        .create();
                                dialog.show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequset = new ValidateRequest(ID,responseListener); //중복확인으로 넘겨버림
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(validateRequset);
            }
        });
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ID = REID.getText().toString();
                Password = REPassword.getText().toString();
                Name = REName.getText().toString();
                Email = REEmail.getText().toString();

                if(!validate){//중복체크확인
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("먼저 중복체크를 해주세요")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }
                if(ID.equals("")||Password.equals("")||Name.equals("")||Email.equals("")){ //빈칸있을시 안됨
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    dialog = builder.setMessage("빈칸 없이 입력해주세요")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) { //회원등록 성공
                               AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("회원등록에 성공했습니다")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                finish();
                              } else { //회원등록 실패
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                dialog = builder.setMessage("회원등록에 실패했습니다")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                RegisterRequest registerRequest = new RegisterRequest(ID,Password,Name,Email,responseListener); //회원등록으로 보내줌
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });

    }
    @Override
    protected void onStop(){ //회원등록창이 꺼지게 되면
        super.onStop();
        if(dialog !=null){
            dialog.dismiss();
            dialog =null;
        }
    }
}
