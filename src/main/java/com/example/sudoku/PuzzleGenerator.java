package com.example.sudoku;

import java.util.Random;
import java.util.ArrayList;

//creates a solution for the puzzle
public class PuzzleGenerator {
    Random random  = new Random();
    public int[][] generateSolved() {
        int[][] solution = new int[9][9];
        fillDiagonalBoxes(solution);
        solve(solution);
        return solution;
    }
    //3x3 regions in diagonal
    private void fillDiagonalBoxes(int[][] board) {
        for (int i = 0; i < 9; i += 3) {
            fillBox(board, i, i);
        }
    }
    private void fillBox(int[][] board, int row, int col) {
        ArrayList<Integer>numbers = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            numbers.add(i);
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (numbers.isEmpty()) {
                    return;
                }
                int index = random.nextInt(numbers.size());
                board[row + i][col + j] = numbers.get(index);
                numbers.remove(index);
            }
        }
    }

    public int[][] createPuzzle(int[][] solution) {
        int[][] puzzle = new int[9][9];
        //copy solution
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                puzzle[i][j] = solution[i][j];
            }
        }
        //removing 30 numbers from puzzle
        int numBlanks = 30;
        boolean blanks[][] = new boolean[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                blanks[i][j] = true;
            }
        }
        while (numBlanks > 0) {
            int row = random.nextInt(9);
            int col = random.nextInt(9);
            if (blanks[row][col]) {
                blanks[row][col] = false;
                puzzle[row][col] = 0;
                numBlanks--;
            }
        }
        return puzzle;     
    }

    /* since three diagonal boxes can be filled independently of 
     * each other, only need to check boxes and then the rest of
     * the puzzle can be filled recursively by trying each number
     * until board is populated */

    private boolean solve(int[][] board) {
        for (int i = 0; i < 9; i++) { //rows
            for (int j = 0; j < 9; j++) { //cols
                if (board[i][j] == 0) {
                    for (int k = 1; k <= 9; k++) {
                        if (isPuzzle(board, i, j, k)) {
                            board[i][j] = k;
                            if (solve(board) == true) {
                                return true;
                            }
                            //if not true
                            board[i][j] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isPuzzle(int[][] board, int row, int col, int value) {
        //checking if number exists anywhere in the row
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == value) {
                return false;
            }
        }
        //for cols
        for (int i = 0; i < 9; i++) {
            if (board[i][col] == value) {
                return false;
            }
        }

        //indexes for 3x3 box regions
        int boxrow = row - row % 3;
        int boxcol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                //check if num appears in the same box already
                if (board[boxrow + i][boxcol + j] == value) {
                    return false;
                }
            }
        }
        return true;
    }
}
