package com.danielr_shlomoc.ex1;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.EventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toast.makeText(this, "bla bla", Toast.LENGTH_SHORT).show();


        Button startPlay = findViewById(R.id.btnPlayID);

        startPlay.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem menuItem1 = menu.add("About");
        MenuItem menuItem2 = menu.add("Exit");
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPlayID:
                Intent gameActivity = new Intent(this, GameActivity.class);
                startActivity(gameActivity);
                break;

        }
    }
}