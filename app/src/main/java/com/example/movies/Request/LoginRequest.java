package com.example.movies.Request;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    final  static private String URL="http://35.223.196.237/Login.php";
    private Map<String,String> parameters;

    public LoginRequest(String ID,String Password,Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("ID",ID);
        parameters.put("Password",Password);

    }
    @Override
    public Map<String,String> getParams(){
        return parameters;
    }
}
