
package com.example.movies.Acitivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.movies.Adapter.RecyclerAdapter;
import com.example.movies.Util.ChangeRating;
import com.example.movies.Util.Data;
import com.example.movies.R;
import com.example.movies.Request.MyresearchRequest;
import com.example.movies.Request.ResearchRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class ResearchActivity extends Activity {

    private ArrayAdapter adapter;
    private Spinner spinner;
    private String genre;
    private Integer genrepos;
    private String ID;
    private String rating2;
    private RecyclerAdapter adapter2;
    private AlertDialog dialog;
    private String Moviename;
    private String Movieimage;
    private List<String> listTitle = new ArrayList<>();
    private List<String> listImage = new ArrayList<>();
    public List<String> listgenre = new ArrayList<>();
    private ProgressBar progressBar;
    private TextView progressview;

    @Override
    protected void onCreate(Bundle savedinstanceStante) {
        super.onCreate(savedinstanceStante);
        setContentView(R.layout.activity_research);


        adapter = ArrayAdapter.createFromResource(this, R.array.genre, android.R.layout.simple_dropdown_item_1line);
        spinner = findViewById(R.id.Sgenre);
        spinner.setAdapter(adapter);
        Intent intent = getIntent();
        ID = intent.getStringExtra("아이디");
        spinner.setPrompt("장르를 골라주세요");
        progressBar= findViewById(R.id.progressbar);
        progressview = findViewById(R.id.progressview);
        init();
        getData();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                if (position != 0) {
                    genrepos = spinner.getSelectedItemPosition();
                    if (genrepos > 3) {
                        genre = String.valueOf(genrepos + 1);
                    } else {
                        genre = String.valueOf(genrepos);
                    }
                    Log.e("클릭확인", genre);
                    progressBar.setVisibility(View.VISIBLE);
                    progressview.setVisibility(View.VISIBLE);
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                progressBar.setVisibility(View.GONE);
                                progressview.setVisibility(View.GONE);
                                clearData();
                                listgenre.clear();
                                listTitle.clear();
                                listImage.clear();
                                JSONArray jsonResponse = new JSONArray(response);
                                for (int i = 0; i < jsonResponse.length(); i++) {
                                    if (i % 2 == 0) {
                                        listTitle.add(jsonResponse.optString(i));
                                    } else {
                                        listImage.add(jsonResponse.optString(i));
                                    }
                                }
                                getData();
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
                    ResearchRequest researchRequest = new ResearchRequest(genre, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(ResearchActivity.this);
                    researchRequest.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(  //서버통신시간 조정
                            300000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue.add(researchRequest);
                } else {
                    progressBar.setVisibility(View.GONE);
                    progressview.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        adapter2.setChangeRating(new ChangeRating() {
            @Override
            public void changerating(int pos, float rating) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResearchActivity.this);
                                dialog = builder.setMessage("등록되었습니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResearchActivity.this);
                                dialog = builder.setMessage("수정되었습니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                rating2 = Float.toString(rating);
                Moviename = listTitle.get(pos);
                Movieimage = listImage.get(pos);
                Log.e("123", "아이디:" + ID + "이미지" + Movieimage + "이름" + Moviename + "점수" + rating);
                MyresearchRequest myresearchRequset = new MyresearchRequest(ID, Moviename, Movieimage, rating2, responseListener); //중복확인으로 넘겨버림
                RequestQueue queue = Volley.newRequestQueue(ResearchActivity.this);
                queue.add(myresearchRequset);
            }
        });
    }

    private void clearData() {
        Data data = new Data();
        adapter2.removeItem(data);
    }

    private void getData() {
        for (int i = 0; i < listTitle.size(); i++) {
            final int finalI = i;
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(listImage.get(finalI));
                        URLConnection conn = url.openConnection();
                        conn.connect();
                        BufferedInputStream bufferedInputStream = new BufferedInputStream(conn.getInputStream());
                        Bitmap bm = BitmapFactory.decodeStream(bufferedInputStream);
                        Data data = new Data();
                        data.setRdataimage(bm);
                        data.setRdatatext(listTitle.get(finalI));
                        adapter2.addItem(data);
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
        adapter2 = new RecyclerAdapter();
        recyclerView.setAdapter(adapter2);
        progressBar.setVisibility(View.GONE);
        progressview.setVisibility(View.GONE);
    }
}
