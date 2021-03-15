package com.danielr_shlomoc.ex1;

import android.util.Size;

public class GameBoard {
    private final int SIZE = 4;
    private int[][] board;


    public GameBoard(int[][] new_board){
        board = new int[SIZE][SIZE];

//        for( int i=0; i < SIZE;i++){
//            for( int j = 0; j < SIZE; j++) {
//                    board[i][j] = new_board[i][j];
//
//            }
//        }
    }

    public int [] play_move(int x, int y){
       int [] a = {x,y-1,20};

        return a;

    }
}
