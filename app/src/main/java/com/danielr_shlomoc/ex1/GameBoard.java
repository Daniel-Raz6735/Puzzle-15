package com.danielr_shlomoc.ex1;

import android.util.Size;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//import java.util.*;

public class GameBoard {
    private final int SIZE = 4;
    private int[][] board;
    private final int[][] goalState = {{1,2,3,4},{5,6,7,8},{9,10,11,12},{13,14,15,0}};
    public int invCount =0;
    public int x =0;




    public GameBoard(){
        board = new int[SIZE][SIZE];

        randomBoard();
    }

    // This function random a solvable board.
    public void randomBoard() {
        List<Integer> list;
        do{

            list = new ArrayList<Integer>();

            for(int i =0 ;i<=15;i++)
                list.add(i);

            Collections.shuffle(list);

            int i = 0;

            for(int row = 0 ;row<this.board.length;row++)
                for(int col =0; col< this.board.length;col++) {
                    this.board[row][col] = list.get(i);
                    i++;
                }
        }while(!isSolvable(this.board, list));



    }

    public int [] play_move(int x, int y){
        // i,j indexes of 0 in the board
        int i = findXPosition()[1], j = findXPosition()[2];
        boolean flag = false;


        if(x == i) {

            int diff = y -j;

            // move right or left
            if(diff == -1 || diff == 1)
                flag  = true;

        }

        if( j == y) {

            int diff = x -i;

            // move up or down
            if(diff == -1 || diff ==1 )
                flag = true;
        }

        if(!flag)
            return null;

        board[i][j] = board[x][y];
        board[x][y] = 0;

        return new int[]{i,j,board[i][j]};

    }

    public boolean checkForWin() {
        return Arrays.deepEquals(this.board, this.goalState);
    }

    private int getInvCount(List<Integer> list)
    {
        final int  N = this.SIZE;
        int inv_count = 0;
        for (int i = 0; i < N * N - 1; i++)
        {
            for (int j = i + 1; j < N * N; j++)
            {
                // count pairs(i, j) such that i appears
                // before j, but i > j.
                if (list.get(i) > list.get(j)&&list.get(i)>0&&list.get(j)>0)
                    inv_count++;
            }
        }
        return inv_count;
    }

    // find Position of blank from bottom
    public int[] findXPosition()
    {
        final int N = this.SIZE;
        // start from bottom-right corner of matrix
        for (int i = N - 1; i >= 0; i--)
            for (int j = N - 1; j >= 0; j--)
                if (this.board[i][j] == 0)
                    return new int[] {N - i,i,j};

        return null;
    }

    // This function returns true if given
    // instance of N*N - 1 puzzle is solvable
    private boolean isSolvable(int puzzle[][], List<Integer> list)
    {

        // Count inversions in given puzzle
        int invCount = getInvCount(list);
        int pos = findXPosition()[0];
        this.invCount=invCount;
        this.x=pos;
        if (pos%2 != 0)
            return !(invCount%2 != 0);
        else
            return invCount%2 != 0;

    }

    public int [][] getBoard(){
        int [][] res = new int[SIZE][SIZE];
        for(int i=0; i<SIZE;i++)
            System.arraycopy(board[i], 0, res[i], 0, SIZE);
        return res;
    }
}
