package com.games.tictactoe;

public class TicTacToeEngine {
    
    private char[][] grid;
    private int size;
    private char xChar = 'X', oChar = 'O';
    private int count = 0;
    private boolean end;

    public TicTacToeEngine(int size){
        this.size = size;
        grid = new char[size][size];
    }


    public void setValue(int row, int col, char val){
        grid[row][col] = val;
        count++;
    }

    public boolean isSet(int row, int col){
        if(grid[row][col] == xChar || grid[row][col] == oChar)
            return true;
        return false;
    }
    public boolean end(){
        return end;
    }

    public boolean tie(){
        if(count == size*size)
            return true;
        return false;
    }

    public char whoWins(){
        char result = wins();
        if(result != '-')
            end = true;
        return result;
    }
    private char wins(){
        // checking horizontally
        for(int i=0; i<size; i++){
            int xCount = 0, oCount = 0;
            for(int j=0; j<size; j++){
                if(grid[i][j] == xChar)
                    xCount++;
                else if(grid[i][j] == oChar)
                    oCount++;
            }
            if(xCount == size)
                return xChar;
            else if(oCount == size)
                return oChar;
        }

        // checking Vertically
        for(int i=0; i<size; i++){
            int xCount = 0, oCount = 0;
            for(int j=0; j<size; j++){
                if(grid[j][i] == xChar)
                    xCount++;
                else if(grid[j][i] == oChar)
                    oCount++;
            }
            if(xCount == size)
                return xChar;
            else if(oCount == size)
                return oChar;
        }

        // checking left-diagonal 
        int xCount = 0, oCount = 0;
        for(int i=0; i<size; i++){
            if(grid[i][i] == xChar)
                xCount++;
            else if(grid[i][i] == oChar)
                oCount++;
        }
        if(xCount == size)
            return xChar;
        else if(oCount == size)
            return oChar;

        // checking right-diagonal 
        xCount = 0; oCount = 0;
        for(int j=size-1, i=0; j>=0; j--, i++){
            if(grid[i][j] == xChar)
                xCount++;
            else if(grid[i][j] == oChar)
                oCount++;
        }
        if(xCount == size)
            return xChar;
        else if(oCount == size)
            return oChar;

        return '-';
    }
}
