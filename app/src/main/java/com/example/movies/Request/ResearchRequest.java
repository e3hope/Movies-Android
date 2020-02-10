package com.example.movies.Request;

import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class ResearchRequest extends StringRequest {
    final  static private String URL="http://35.223.196.237:5050/genre";
    private Map<String,String> parameters;

    public ResearchRequest(String genre, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("genre",genre);
    }
    @Override
    public Map<String,String> getParams(){
        return parameters;
    }
}