package com.example.movies.Request;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdateRequest extends StringRequest {
    final  static private String URL="http://35.223.196.237/Update.php";
    private Map<String,String> parameters;

    public UpdateRequest(String ID,String Password, Response.Listener<String>listener){
        super(Request.Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("ID",ID);
        parameters.put("Password",Password);
    }
    @Override
    public Map<String,String> getParams(){
        return parameters;
    }
}

