package com.danielr_shlomoc.ex1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer player;
    private Switch music;
    private MenuItem about, exit;
    private boolean playing;
    private SharedPreferences sp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startPlay = findViewById(R.id.btnPlayID);
        music = findViewById(R.id.switch1);
        sp = getSharedPreferences("MyPref" , Context.MODE_PRIVATE);
        playing = sp.getBoolean("play",false);

        // get last state of media Player and play if last time was on.
        if(playing)
            music.setChecked(true);


        startPlay.setOnClickListener(this);

        // Media player handler
        music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                playing = isChecked;
                saveChoice();

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // save state of playing music to SP
        saveChoice();
    }

    private void saveChoice(){
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("play", playing);
        editor.commit();
    }

    @Override
    // add 3 dots menu
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

         about = menu.add("About");
         exit = menu.add("Exit");

        about.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick( MenuItem item)
            {
                dialog("About");
                return true;
            }
        });

        exit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick( MenuItem item)
            {
                dialog("Exit");
                return true;
            }
        });

        return true;
    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btnPlayID){
                Intent gameActivity = new Intent(this, GameActivity.class);
                saveChoice();
                startActivity(gameActivity);
        }
    }


    // This function string that represent the type of the dialog - About or Exit and show the relevant dialog.
    private void dialog(String type)
    {
        String title, message, positive,negative;
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);


        if(type.equals("About")){
            title = "About App";
            message = "Puzzle 15 (com.danielr_shlomoc.ex1)\n\nBy Daniel Raz & Shlomo Carmi, 24/3/21.";
            positive = "OK";
            negative = null;
            myDialog.setIcon(R.drawable.android_icon);
        }
        else {
            title = "Exit App";
            message = "Do you really want to exit Puzzle 15 ?";
            positive = "YES";
            negative = "NO";
            myDialog.setIcon(R.drawable.exit_icon);
        }


        myDialog.setTitle(title);
        myDialog.setMessage(message);
        myDialog.setCancelable(false);

        myDialog.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(positive.equals("YES"))
                    finish();
            }
        });

        if(negative != null){
            myDialog.setNegativeButton(negative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }

        myDialog.show();
    }
}