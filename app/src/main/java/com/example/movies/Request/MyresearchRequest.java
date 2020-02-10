package com.example.movies.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MyresearchRequest extends StringRequest {

    final  static private String URL="http://35.223.196.237/Myresearch.php";
    private Map<String,String> parameters;
    public MyresearchRequest(String ID, String Moviename, String Movieimage, String Rating, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("ID",ID);
        parameters.put("Moviename",Moviename);
        parameters.put("Movieimage",Movieimage);
        parameters.put("Rating",Rating);
    }

    @Override
    public Map<String,String> getParams(){
        return parameters;
    }
}
