package com.games.connect4;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;

public class Connect4Engine{

    private int boardSize = 8;
    private int turn; 
    public  int[] columns;
    private Color xColor = Color.valueOf("#f9ed69"), oColor = Color.valueOf("#F08A5D");
    private char[][] board;
    private char xChar = 'X', oChar = 'O';
    private List<int[]> winningCells; 
    private int gameTurns = boardSize*boardSize;
    private boolean end;


    public Connect4Engine(){
        board = new char[boardSize][boardSize];
        columns = new int[boardSize];
        for(int i=0; i<boardSize; i++)
            columns[i] = boardSize-1;
        winningCells = new ArrayList<>();
    }

    public void changeTurn(){
        turn = turn==0?1:0;
    }

    public char getTurn(){
        return this.turn==0?xChar:oChar;
    }

    public Color getColor(){
        return turn==0?xColor:oColor;
    }

    public List<int[]> getWinningCells(){
        return this.winningCells;
    }

    public boolean isEnd(){
        return end;
    }

    public void setValue(int col){
        if(columns[col] < 0 || gameTurns == 0)
            return;
        board[columns[col]--][col] = getTurn();
        gameTurns--;
    }

    public boolean isTie(){
        return gameTurns==0;
    }
    public boolean validCol(int col){
        return columns[col] >= 0;
    }
    public char whoWins(){
        char item = checkWinning();
        if(item != '-')
            end = true;
        return item;
    }

    public char checkWinning(){

        // checking Horizontally
        for(int i=0; i<boardSize; i++){
            int xCount = 0, oCount = 0; 
            for(int j=0; j<boardSize; j++){
                if(board[i][j] == xChar){
                    oCount = 0;
                    xCount++;
                }else if(board[i][j] == oChar){
                    xCount = 0;
                    oCount++;
                }else{
                    oCount = 0;
                    xCount = 0;
                }
                if(xCount == 4 || oCount == 4){
                    int m = 4;
                    while(--m >= 0)
                        winningCells.add(new int[]{i, j-m});
                    return xCount==4?xChar:oChar;
                }
            }
        }

        // checking Vertically
        for(int i=0; i<boardSize; i++){
            int xCount = 0, oCount = 0; 
            for(int j=0; j<boardSize; j++){
                if(board[j][i] == xChar){
                    oCount = 0;
                    xCount++;
                }else if(board[j][i] == oChar){
                    xCount = 0;
                    oCount++;
                }else{
                    oCount = 0;
                    xCount = 0;
                }
                if(xCount == 4 || oCount == 4){
                    int m = 4;
                    while(--m >= 0)
                        winningCells.add(new int[]{j-m, i});
                    return xCount==4?xChar:oChar;
                }
            }
        }

        // checking Left Diagonal
        int xCount = 0, oCount = 0; 
        for(int i=0, j=boardSize-1; i<boardSize; i++, j--){
            if(board[i][j] == xChar){
                oCount = 0;
                xCount++;
            }else if(board[i][j] == oChar){
                xCount = 0;
                oCount++;
            }else{
                oCount = 0;
                xCount = 0;
            }
            if(xCount == 4 || oCount == 4){
                int m = 4;
                while(--m >= 0)
                    winningCells.add(new int[]{i-m, j+m});
                return xCount==4?xChar:oChar;
            }
        }

        // checking Right Diagonal
        xCount = 0; oCount = 0; 
        for(int i=0, j=0; i<boardSize; i++, j++){
            if(board[i][j] == xChar){
                oCount = 0;
                xCount++;
            }else if(board[i][j] == oChar){
                xCount = 0;
                oCount++;
            }else{
                oCount = 0;
                xCount = 0;
            }
            if(xCount == 4 || oCount == 4){
                int m = 4;
                while(--m >= 0)
                    winningCells.add(new int[]{i-m, j-m});
                return xCount==4?xChar:oChar;
            }
        }

        // tie;
        return '-';
    }
}