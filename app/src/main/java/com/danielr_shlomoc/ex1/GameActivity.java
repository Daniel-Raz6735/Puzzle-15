package com.danielr_shlomoc.ex1;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    private int moves;
    private int time;
    private boolean playing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        sp = getSharedPreferences("gamePref", Context.MODE_PRIVATE);
        moves_counter = findViewById(R.id.move_counter);
        time_counter = findViewById(R.id.time_counter);
        restart = findViewById(R.id.btn_re_start);
        restart.setOnClickListener(this);
        initialize_btns();
        restart_game(null);
        moves = 0;
        time = 0;


        sp = getSharedPreferences("MyPref" , Context.MODE_PRIVATE);
        playing = sp.getBoolean("play",false);

        // get last state of media Player and play if last time was on.
        if(playing){
            player =  MediaPlayer.create(this, R.raw.janji);
            player.setLooping(true);
            player.start();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        start_timer();
        load_game();
    }

    @Override
    protected void onPause() {
        super.onPause();
        save_game();
    }

    @Override
    protected void onDestroy() {
        stopPlaying();
        super.onDestroy();
        sp.edit().clear();
        sp.edit().apply();
    }

    public void remove_color(TextView t) {
        t.setBackgroundResource(R.drawable.border_black_white_bcg);
        t.setTextColor(getResources().getColor(R.color.transparent));
        t.setText("");
        t.setEnabled(false);
    }

    public void save_game() {
        SharedPreferences.Editor editor = sp.edit();
        //save board
        int[][] board = game.getBoard();
        editor.putBoolean("board",true);
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                editor.putInt("btn" + i + j, board[i][j]);
                Log.i("test_changes", "btn" + i + j);
            }
        }

        editor.apply();
    }

    public void load_game() {
        SharedPreferences.Editor editor = sp.edit();
        //save board
        if(sp.getBoolean("board" ,false)){


        int[][] board = game.getBoard();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                int num = sp.getInt("btn" + i + j,-1);


                editor.putInt("btn" + i + j, board[i][j]);
                Log.i("test_changes", "btn" + i + j);
            }
        }
        }

        editor.apply();
    }

    public void start_timer() {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {

                        Thread.sleep(1000);
                        time++;
                        int minutes = time/60;
                        int seconds = time%60;

                        runOnUiThread(new Runnable() {
                            public void run() {
                                time_counter.setText("Time: "+ String.format("%02d", minutes) +":"+String.format("%02d", seconds));
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void initialize_btns() {
        //connects the buttons to the btns array and sets them the listener
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
        t.setBackgroundResource(R.drawable.btn_bcg);
        t.setTextColor(getResources().getColor(R.color.btn_txt_clr));
        t.setText(num + "");
        t.setEnabled(true);
    }

    public void update_moves() {
        moves ++;
        moves_counter.setText("Moves: "+String.format("%04d", moves));
    }

    public boolean move(int x, int y) {
        int[] res = game.play_move(x, y);
        if (res == null)
            return false;
        Log.d("debug","res[0]: "+res[0]+" res[1]: "+res[1]+" res[2]: "+res[2]);
        int i = res[0], j = res[1];
        if (i > SIZE || j > SIZE)
            return false;
        color_view(btns[i][j], res[2]);
        remove_color(btns[x][y]);
        update_moves();
        return true;

    }

    public void color_board(int[][] new_board) {
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

    public void restart_game(int [][] board) {

        if(board == null){

            game = new GameBoard();
            board = game.getBoard();
        }
        color_board(board);
        moves = 0;
        time = 0;
        moves_counter.setText("Moves: 0000");
        time_counter.setText("Time: 0000");
    }


    @Override
    public void onClick(View v) {
        boolean is_board_btn = false;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (v.getId() == btns[i][j].getId()) {
                    move(i, j);
                    is_board_btn = true;
                }
            }
        }
        if (!is_board_btn) {
            if (v.getId() == restart.getId())
                restart_game(null);
        }
    }

    private void stopPlaying() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }






//    public void switchBtn(int row_S, int col_s, int row_t, int col_t) {
//
////        new Thread(new Runnable() {
////            public void run() {
////                moveBtn(row_S, col_s, row_t, col_t);//3, 1, 2, 1
////
////            }
////        }).start();
////        new Thread(new Runnable() {
////            public void run() {
////                moveBtn(row_t, col_t+2,row_S, col_s);// 2, 0, 3, 1
////            }
////        }).start();
//
//    }
//
//    public void moveBtn(int r1, int c1, int r2, int c2) {
//        ViewGroup src = getLayout(r1);
//        ViewGroup trg = getLayout(r2);
//        View srcTxt = src.getChildAt(c1);
////        runOnUiThread(new Runnable() {
////            public void run() {
////                src.removeViewAt(c1);
////                trg.addView(srcTxt, c2);
////            }
////        });
//
//    }
//
//////        ViewGroup root = findViewById(R.id.son_layout4);
////        ViewGroup root = getLayout(3);
////        ViewGroup rootq = getLayout(2);
////
//////        ViewGroup rootq = findViewById(R.id.son_layout3);
////        View a = root.getChildAt(2);//15
//////        View b = root.getChildAt(3);//-
//////         view a = rootq.getChildAt(c2);
////        root.removeViewAt(2);//15
////        root.addView(a,0);//
//////        View b = root.getChildAt(3);//-
//////        root.removeViewAt(2);
//////        root.addView(b,1);
//
//
//
//    public ViewGroup getLayout(int row) {
//        ViewGroup v;
//        switch (row) {
//            case (0):
//                v = findViewById(R.id.son_layout1);
//                break;
//            case (1):
//                v = findViewById(R.id.son_layout2);
//                break;
//            case (2):
//                v = findViewById(R.id.son_layout3);
//                break;
//            case (3):
//                v = findViewById(R.id.son_layout4);
//                break;
//            default:
//                v = null;
//        }
//        return v;
//
//
//    }
}