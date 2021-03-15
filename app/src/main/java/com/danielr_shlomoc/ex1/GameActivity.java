package com.danielr_shlomoc.ex1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private final int SIZE = 4;
    private TextView[][] btns;
    private GameBoard game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        btns = new TextView[SIZE][SIZE];
        int[][] tes = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 0}};
        game = new GameBoard(tes);
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

        color_board(tes);

//        switchBtn(3, 1, 2, 1);
//        swapBtn(1,2,3,0);

    }

    public void remove_color(TextView t) {
        t.setBackgroundResource(R.drawable.border_black_white_bcg);
        t.setTextColor(getResources().getColor(R.color.transparent));
        t.setText("");
    }

    public void color_view(TextView t, int num) {
        t.setBackgroundResource(R.drawable.btn_bcg);
        t.setTextColor(getResources().getColor(R.color.btn_txt_clr));
        t.setText(num + "");
    }

    public boolean move(int x, int y) {
        int[] res = game.play_move(x, y);
        if (res == null)
            return false;
        int i = res[0], j = res[1];
        if (i > SIZE || j > SIZE)
            return false;
        color_view(btns[i][j], res[2]);
        remove_color(btns[x][y]);
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
        if (!!is_board_btn) {

            switch (v.getId()) {
                case R.id.btnPlayID:
                    Intent gameActivity = new Intent(this, GameActivity.class);
                    startActivity(gameActivity);
                    break;

            }
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