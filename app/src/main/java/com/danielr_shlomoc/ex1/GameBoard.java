package com.danielr_shlomoc.ex1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class GameBoard {
    private final int SIZE = 4; //contains the size of the game
    private final int[][] goalState = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 0}}; //expected game result
    private final int[][] board; // contains the game board
    private int[] x_location; // an array with the location of the blank tile location


    public GameBoard() {
        board = new int[SIZE][SIZE];
        randomBoard();
    }

    public void randomBoard() {
        // This function creates a random solvable board.
        List<Integer> list = new ArrayList<>();
        int i,j;
        for (i = 0; i < SIZE * SIZE; i++)  //create and shuffle
            list.add(i);
        Collections.shuffle(list);

        int zero_index = list.indexOf(0);
        x_location = new int[]{zero_index / SIZE, zero_index % SIZE};
        if (!isSolvable(list)) {
            j = (SIZE * SIZE) - 1;//set j to max index;
            /*if not solvable find 2 non zero elements and swap them */
            if (list.get(j) == 0)
                j--;
            i = j - 1;
            if (list.get(i) == 0)
                i--;
            Collections.swap(list, i, j);
        }

        //put the solvable list in the game board
        for (int row = 0; row < this.board.length; row++)
            for (int col = 0; col < this.board.length; col++) {
                this.board[row][col] = list.get(row*this.SIZE+col);
            }


    }

    public int[] play_move(int row, int col) {
        /*
        test if the given indexes are a valid move in the game
        returns an array { value of the tile moved, new location row, new location column}
        */

        int i = x_location[0], j = x_location[1]; // i,j indexes of the blank tile in the board
        boolean flag = false;

        if (row == i) {
            int diff = col - j;
            // move right or left
            if (diff == -1 || diff == 1)
                flag = true;
        }

        if (j == col) {
            int diff = row - i;
            // move up or down
            if (diff == -1 || diff == 1)
                flag = true;
        }

        if (!flag) //illegal move
            return null;

        board[i][j] = board[row][col];
        board[row][col] = 0;
        this.x_location = new int[]{row, col}; //save the new location of the empty tile
        return new int[]{i, j, board[i][j]};

    }

    public boolean checkForWin() {
        //test the board and see if it is complete
        return Arrays.deepEquals(this.board, this.goalState);
    }

    private int getInvCount(List<Integer> list) {
        //get number of inversions in the board
        final int N = this.SIZE;
        int inv_count = 0;
        for (int i = 0; i < N * N - 1; i++) {
            for (int j = i + 1; j < N * N; j++) {
                // count pairs(i, j) such that i appears
                // before j, but i > j.
                if (list.get(i) > list.get(j) && list.get(i) > 0 && list.get(j) > 0)
                    inv_count++;
            }
        }
        return inv_count;
    }


    // This function returns true if given
    // instance of N*N - 1 puzzle is solvable
    private boolean isSolvable(List<Integer> list) {
        /*
        tests a board to see if it solvable. a board will be considered solvable if it has
         or 1.even amount of inversions and the blank tile is an odd number of rows from the bottom
         (last row is considered one)
         or 2.odd amount of inversions and the blank tile is an even number of rows from the bottom*/

        // Count inversions in given puzzle
        int invCount = getInvCount(list);
        int lines_from_bottom = this.SIZE - x_location[0];
        return lines_from_bottom % 2 == 0 ^ invCount % 2 == 0;//XOR

    }

    public int[][] getBoard() {
        //board getter
        int[][] res = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++)
            System.arraycopy(board[i], 0, res[i], 0, SIZE);
        return res;
    }
}
