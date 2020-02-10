package com.example.movies.Request;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    final  static private String URL="http://35.223.196.237/Register.php";
    private Map<String,String> parameters;

    public RegisterRequest(String ID,String Password,String Name,String Email,Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("ID",ID);
        parameters.put("Password",Password);
        parameters.put("Name",Name);
        parameters.put("Email",Email);
    }

    @Override
    public Map<String,String> getParams() {
        return parameters;
    }
}
