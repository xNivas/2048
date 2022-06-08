package com.example.a2048;

import android.util.Log;

public class Board {
    private int[][] board = {{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public int getSingleValueFromBoard(int i, int j) {
        int value = board[i][j];
        return value;
    }

    public void setSingleValueOnBoard(int i, int j, int value) {
        board[i][j] = value;
    }

    public Board() {
        this.board = board;
        for (int i = 0 ; i != 4 ; i++) {
            for (int j = 0 ; j != 4 ; j++) {
                Log.d("appLogs", "i = " + i + " j = " + j + " value: " + String.valueOf(board[i][j]));
            }
        }
    }


}
