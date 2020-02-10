package com.example.movies.Acitivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.movies.Adapter.MyRecyclerAdapter;
import com.example.movies.Request.RecommendRequest;
import com.example.movies.Util.MyData;
import com.example.movies.R;
import com.example.movies.Request.MymovieRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MyMovieActivity extends Activity {
    private MyRecyclerAdapter adapter2;
    private String ID;
    private List<String> mylist = new ArrayList<>();
    private List<String> recommendlist = new ArrayList<>();
    private Button recommendbtn;
    private AlertDialog dialog;
    private TextView customid;
    private TextView customtitle;
    private ImageView customimage;
    private String moviemax;
    private float ratingmax=0;
    private Dialog customdialog;
    @Override
    protected void onCreate(Bundle savedinstanceStante) {
        super.onCreate(savedinstanceStante);
        setContentView(R.layout.activity_mymovie);
        Intent intent = getIntent();
        ID = intent.getStringExtra("아이디");
        recommendbtn = findViewById(R.id.recommendbtn);

        init();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonResponse = new JSONArray(response);
                    mylist.clear();
                    for (int i = 0; i < jsonResponse.length(); i++) {
                        mylist.add(jsonResponse.optString(i));
                    }
                    String[][] mylist2 = new String[mylist.size()][3];
                    String[] mylist3 = new String[3];
                    for(int i =0;i<mylist.size();i++){
                        mylist3 = mylist.get(i).split(",");
                        for(int j=0;j<3;j++){
                            mylist2[i][j]=mylist3[j];
                        }
                        if(ratingmax < Float.valueOf(mylist2[i][2])){   //최고선호영화
                            ratingmax = Float.valueOf(mylist2[i][2]);
                            moviemax = mylist2[i][0];
                        }
                    }
                    getData(mylist2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error:", String.valueOf(error));
            }
        };
        MymovieRequest mymovieRequest = new MymovieRequest(ID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MyMovieActivity.this);
        mymovieRequest.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(  //서버통신시간 조정
                200000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(mymovieRequest);

        recommendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success) {
                                String recommendID = jsonResponse.getString("ID");
                                String recommendMoviename = jsonResponse.getString("Moviename");
                                String recommendMovieimage = jsonResponse.getString("Movieimage");
                                recommendlist.clear();
                                recommendlist.add(recommendID);
                                recommendlist.add(recommendMoviename);
                                recommendlist.add(recommendMovieimage);
                                customdialog = new Dialog(MyMovieActivity.this);
                                customdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                customdialog.setContentView(R.layout.custom_dialog);
                                customid = customdialog.findViewById(R.id.customid);
                                customtitle = customdialog.findViewById(R.id.customtitle);
                                customimage = customdialog.findViewById(R.id.customimage);
                                recommenddialog();
                                customdialog.show();
                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(MyMovieActivity.this);
                                dialog = builder.setMessage("추천목록이 없습니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error:", String.valueOf(error));
                    }
                };
                RecommendRequest recommendRequest = new RecommendRequest(ID, moviemax,String.valueOf(ratingmax), responseListener);
                RequestQueue queue = Volley.newRequestQueue(MyMovieActivity.this);
                recommendRequest.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(  //서버통신시간 조정
                        200000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(recommendRequest);
            }
        });
    }

//    private void recommenddialog(final String[] recommendlist2) {
private void recommenddialog() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Log.e("",recommendlist.get(0)+recommendlist.get(1)+"이미지"+recommendlist.get(2)+"");
                    URL url = new URL(recommendlist.get(2));
                    URLConnection conn = url.openConnection();
                    conn.connect();
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(conn.getInputStream());
                    Bitmap bm = BitmapFactory.decodeStream(bufferedInputStream);
                    Log.e("url/",url+"");
                    customimage.setImageBitmap(bm);
                    customid.setText(""+recommendlist.get(0) + "님의 추천입니다.");
                    customtitle.setText(recommendlist.get(1));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getData(final String[][] mylist2) {
            for (int i = 0; i < mylist.size(); i++) {
                final int finalI = i;
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(mylist2[finalI][1]);
                            URLConnection conn = url.openConnection();
                            conn.connect();
                            BufferedInputStream bufferedInputStream = new BufferedInputStream(conn.getInputStream());
                            Bitmap bm = BitmapFactory.decodeStream(bufferedInputStream);
                            MyData mydata = new MyData();
                            mydata.setRdataimage(bm);
                            mydata.setRdatatext(mylist2[finalI][0]);
                            mydata.setRdatarating(Float.parseFloat(mylist2[finalI][2]));
                            adapter2.addItem(mydata);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            adapter2.notifyDataSetChanged(); // adapter의 값이 변경되었습니다.
        }

        private void init() { //무한스크롤
            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            adapter2 = new MyRecyclerAdapter();
            recyclerView.setAdapter(adapter2);
        }

}
