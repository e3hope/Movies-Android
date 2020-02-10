package com.example.movies.Acitivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.movies.R;

public class MainActivity extends Activity {

    private String ID;
    private String Password;
    @Override
    protected void onCreate(Bundle savedinstanceStante) {
        super.onCreate(savedinstanceStante);
        setContentView(R.layout.activity_main);
        final TextView MainID = findViewById(R.id.MainID);
        final TextView research = findViewById(R.id.ResearchButton);
        final TextView mymovie = findViewById(R.id.mymovieButton);
        final TextView update = findViewById(R.id.updateButton);
        final TextView logout = findViewById(R.id.logoutButton);
        Intent intent = getIntent();
        ID = intent.getStringExtra("아이디");
        Password = intent.getStringExtra("비밀번호");

        MainID.setText(ID+"님");

        research.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Research = new Intent(MainActivity.this, ResearchActivity.class);
                Research.putExtra("아이디",ID);
                MainActivity.this.startActivity(Research);
            }
        });
        mymovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MyMovie = new Intent(MainActivity.this, MyMovieActivity.class);
                MyMovie.putExtra("아이디",ID);
                MainActivity.this.startActivity(MyMovie);
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Update = new Intent(MainActivity.this, UpdateActivity.class);
                Update.putExtra("아이디",ID);
                Update.putExtra("비밀번호",Password);
                MainActivity.this.startActivity(Update);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Logout = new Intent(MainActivity.this, LoginActivity.class);
                MainActivity.this.startActivity(Logout);
            }
        });
    }
}
