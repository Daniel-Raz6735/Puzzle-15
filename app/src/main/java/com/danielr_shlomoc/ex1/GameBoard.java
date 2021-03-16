package com.danielr_shlomoc.ex1;

import android.util.Size;

public class GameBoard {
    private final int SIZE = 4;
    private int[][] board;


    public GameBoard(int[][] new_board){
        board = new int[SIZE][SIZE];

        for( int i=0; i < SIZE;i++){
            System.arraycopy(new_board[i], 0, board[i], 0, SIZE);
        }
    }

    public int [] play_move(int x, int y){
        int[] res = null;
        //move left
        if(y>0) {
            if (board[x][y-1] == 0) {
                res = new int[]{x, y - 1, board[x][y]};
            }
        }
        // move right
        if(y<SIZE-1){
            if(board[x][y+1]==0)
                res = new int[] {x,y+1, board[x][y]};
        }
        // move up
        if(x>0){
            if(board[x-1][y]==0)
                res = new int[] {x-1,y, board[x][y]};
        }
        // move down
        if(x<SIZE-1){
            if(board[x+1][y]==0)
                res = new int[] {x+1,y, board[x][y]};
                board[x][y] = board[x+1][y];
        }
        if(res != null){
            board[x][y] = 0;
            board[res[0]][res[1]] = res[2];
        }
        return res;

    }
    public int [][] getBoard(){
        int [][] res = new int[SIZE][SIZE];
        for(int i=0; i<SIZE;i++)
            System.arraycopy(board[i], 0, res[i], 0, SIZE);
        return res;
    }
}
