package com.games.mines;

import java.util.HashSet;
import java.util.Set;

import com.games.Timer;

import javafx.scene.control.Label;


public class MinesEngine {
    
    private int[][] grid;
    private int boardSize;
    private int bombsNum;
    private Set<int[]> bombs; 
    
    public MinesEngine(int size){
        boardSize = size;
        grid = new int[size][size];
        bombsNum = (int) ((1.0/10.0) * size * size);
        bombs = new HashSet<>();
        initBombs();
        initBoard();
    }

    private void initBombs(){
        int i = 0;
        while(i<bombsNum){
            int[] bomb = new int[]{0, 0};
            do{
                bomb[0] = (int) (Math.random() * boardSize);
                bomb[1] = (int) (Math.random() * boardSize);
            }
            while(grid[bomb[0]][bomb[1]] < 0);
            grid[bomb[0]][bomb[1]] = Integer.MIN_VALUE;
            bombs.add(bomb);
            i++;
        }
    }

    public void initBoard(){
        for(int [] bomb : bombs){
            int row = bomb[0], col = bomb[1];
            if(row + 1 < boardSize){
                grid[row+1][col]++;
                if(col+1 < boardSize)
                    grid[row+1][col+1]++;
                if(col - 1 >= 0)
                    grid[row+1][col-1]++;
            }
            if(row - 1 >= 0){
                grid[row-1][col]++;
                if(col+1 < boardSize)
                    grid[row-1][col+1]++;
                if(col - 1 >= 0)
                    grid[row-1][col-1]++;
            }
            if(col + 1 < boardSize)
                grid[row][col+1]++;
            if(col - 1 >= 0)
                grid[row][col-1]++;
        }
    }

    public int[][] getGrid(){
        return this.grid;
    }

    public Set<int[]> getBombs(){
        return this.bombs;
    }
}
