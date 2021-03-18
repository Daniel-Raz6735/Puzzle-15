package com.danielr_shlomoc.ex1;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private final int SIZE = 4;
    private SharedPreferences sp;
    private TextView[][] btns;
    private TextView moves_counter;
    private TextView time_counter;
    private GameBoard game;
    private Button restart;
    private MediaPlayer player;
    private int play_location;
    private int moves;
    private int time;
    private boolean playing;
    private boolean alive;
    private boolean game_over;
    private Thread time_thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        playing = sp.getBoolean("play", false);
        play_location = 0;
        time_thread = null;
        initialize_btns();
        restart_game(null);
        moves = 0;
        time = 0;
        game_over = false;

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!game_over)
            start_timer();
        // get last state of media Player and play if last time was on.
        if (playing) {
            player = MediaPlayer.create(this, R.raw.janji);
            player.setLooping(true);
            player.seekTo(play_location);
            player.start();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        alive = false;
        stopPlaying();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sp.edit().clear();
        sp.edit().apply();
    }

    @Override
    public void onClick(View v) {

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (v.getId() == btns[i][j].getId()) {
                    if (play_move(i, j))
                        test_won();
                }
            }
        }
        if (v.getId() == restart.getId()) {
            restart_game(null);
            game_over = false;

        }
    }

    private void stopPlaying() {
        //stop the music from playing
        if (player != null) {
            play_location = player.getCurrentPosition();
            player.stop();
            player.release();
            player = null;
        }
    }

    public void remove_color(TextView t) {
        //remove the color from a button
        t.setBackgroundResource(R.drawable.border_black_white_bcg);
        t.setTextColor(getResources().getColor(R.color.transparent));
        t.setText("");
        t.setClickable(false);
    }

    public void start_timer() {
        //sets a timer in the app to show the user the amount of time he is playing
        if (time_thread == null) {
            //if timer is already set
            alive = true;
            time_thread = new Thread(new Runnable() {
                public void run() {
                    while (alive) {
                        try {
                            Thread.sleep(1000);
                            time++;
                            int minutes = time / 60;
                            int seconds = time % 60;
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    time_counter.setText("Time: " + String.format("%02d", minutes) + ":" + String.format("%02d", seconds));
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    time_thread = null;
                }
            });
            time_thread.start();
        }
    }

    public void initialize_btns() {
        //connects all of the buttons to the variables and sets them a listener
        moves_counter = findViewById(R.id.move_counter);
        time_counter = findViewById(R.id.time_counter);
        restart = findViewById(R.id.btn_re_start);
        restart.setOnClickListener(this);
        // connect the board
        btns = new TextView[SIZE][SIZE];
        TextView[] row = btns[0];
        row[0] = findViewById(R.id.txt_1);
        row[1] = findViewById(R.id.txt_2);
        row[2] = findViewById(R.id.txt_3);
        row[3] = findViewById(R.id.txt_4);
        row = btns[1];
        row[0] = findViewById(R.id.txt_5);
        row[1] = findViewById(R.id.txt_6);
        row[2] = findViewById(R.id.txt_7);
        row[3] = findViewById(R.id.txt_8);
        row = btns[2];
        row[0] = findViewById(R.id.txt_9);
        row[1] = findViewById(R.id.txt_10);
        row[2] = findViewById(R.id.txt_11);
        row[3] = findViewById(R.id.txt_12);
        row = btns[3];
        row[0] = findViewById(R.id.txt_13);
        row[1] = findViewById(R.id.txt_14);
        row[2] = findViewById(R.id.txt_15);
        row[3] = findViewById(R.id.txt_16);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                btns[i][j].setOnClickListener(this);
            }
        }
    }

    public void color_view(TextView t, int num) {
        //color the background of a button to be a colored button with the number appearing in it
        t.setBackgroundResource(R.drawable.btn_bcg);
        t.setTextColor(getResources().getColor(R.color.btn_txt_clr));
        t.setText(num + "");
        t.setClickable(true);
    }

    public void update_moves() {
        //add moves to the counter
        moves++;
        moves_counter.setText("Moves: " + String.format("%04d", moves));
    }

    public void test_won() {
        //test to see if the game is finished
        if (game.checkForWin()) {
            game_over = true;
            for (int i = 0; i < SIZE; i++) {//make all buttons un clickable
                for (int j = 0; j < SIZE; j++) {
                    btns[i][j].setClickable(false);
                }
            }
            Toast.makeText(this, "Game Over - Puzzle Solved!", Toast.LENGTH_LONG).show();
            alive = false;
        }
    }

    public boolean play_move(int x, int y) {
        // ask the game class if the move is legal if so play the move otherwise return null
        int[] res = game.play_move(x, y);
        if (res == null)
            return false;
        int i = res[0], j = res[1];
        if (i > SIZE || j > SIZE)
            return false;
        color_view(btns[i][j], res[2]);
        remove_color(btns[x][y]);
        update_moves();
        return true;
    }

    public void color_board(int[][] new_board) {
        //color all buttons on the board except for the blank tile that should be blank
        int run = new_board.length;
        if (run > SIZE)
            run = SIZE;
        for (int i = 0; i < run; i++) {
            int run_inside = new_board[i].length;
            if (run_inside > SIZE)
                run_inside = SIZE;
            for (int j = 0; j < run_inside; j++) {
                if (new_board[i][j] > 0)
                    color_view(btns[i][j], new_board[i][j]);
                else
                    remove_color(btns[i][j]);
            }
        }
    }

    public void restart_game(int[][] board) {
        //restart the game and play start a new game if none is provided
        if (board == null) {
            game = new GameBoard();
            board = game.getBoard();
        }
        start_timer();
        color_board(board);
        game_over = false;
        moves = 0;
        time = 0;
        moves_counter.setText("Moves: 0000");
        time_counter.setText("Time: 00:00");
    }


}