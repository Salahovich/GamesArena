package com.games.eightpuzzle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EightPuzzleEngine {
    public final int[][] tiles;
	private final int n;
    private boolean end;

	public EightPuzzleEngine(int boardSize) {
		this.tiles = new int[boardSize][boardSize];
		this.n = boardSize;
        shuffle();
		
	}

	// create a board from an n-by-n array of tiles,
	// where tiles[row][col] = tile at (row, col)
    public EightPuzzleEngine(int[][] curr) {
		this.tiles = curr;
		this.n = curr.length;
		
	}

    // shuffle board
    public void shuffle(){
        int high = n*n-1;
        Set<Integer> set = new HashSet<>();
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                int random = (int) (Math.random()*(high+1));
                while(set.contains(random))
                    random = (int) (Math.random()*(high+1));
                tiles[i][j] = random;
                set.add(random);
            }
        }
    }

    public int[] play(int i, int j){
        int[] result = null;
        if(i+1 < n && tiles[i+1][j] == 0){
            result = new int[]{i+1, j};
            exchange(i, j, i+1, j);
        }
        else if(i-1 >= 0 && tiles[i-1][j] == 0){
            result = new int[]{i-1, j};
            exchange(i, j, i-1, j);
        }
        else if(j+1 < n && tiles[i][j+1] == 0){
            result = new int[]{i, j+1};
            exchange(i, j, i, j+1);
        }
        else if(j-1 >= 0 && tiles[i][j-1] == 0){
            result = new int[]{i, j-1};
            exchange(i, j, i, j-1);
        }
        return result;
    }

    public void exchange(int i, int j, int m, int n){
        int temp = tiles[i][j];
        tiles[i][j] = tiles[m][n];
        tiles[m][n] = temp;
    }
	// board dimension n
	public int dimension() {
		return n;
	}

    public boolean isEnd(){
        return end;
    }
    
	// number of tiles out of place
	public int hamming() {
		int hammingCount = 0;
		int count = 1;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (tiles[i][j] != count && tiles[i][j] != 0)
					hammingCount++;
				count++;
			}
		}

		return hammingCount;
	}

	// sum of Manhattan distances between tiles and goal
	public int manhattan() {
		int manhattanResult = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				int row, col;
				if (tiles[i][j] == 0)
					continue;
				row = tiles[i][j] % n != 0 ? tiles[i][j] / n : tiles[i][j] / n - 1;
				col = tiles[i][j] % n != 0 ? (tiles[i][j] % n) - 1 : n - 1;
				manhattanResult += Math.abs(i - row) + Math.abs(j - col);
			}
		}
		return manhattanResult;
	}

	// is this board the goal board?
	public boolean isGoal() {

		int counter = 1;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i == n - 1 && j == n - 1)
					break;
				if (tiles[i][j] != counter)
					return false;
				counter++;
			}
		}
        end = true;
		return true;
	}

	// does this board equal y?
	public boolean equals(Object y) {

		if (this == y)
			return true;
		
		if (y == null)
			return false;

		if (y.getClass() != this.getClass())
			return false;

            EightPuzzleEngine that = (EightPuzzleEngine) y;
		if (that.dimension() != this.dimension())
			return false;

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (this.tiles[i][j] != that.tiles[i][j])
					return false;
			}
		}
		return true;
	}

	// all neighboring boards
	public Iterable<EightPuzzleEngine> neighbors() {
		List<EightPuzzleEngine> list = new ArrayList<>();

		int zeroRow = 0, zeroCol = 0;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (tiles[i][j] == 0) {
					zeroRow = i;
					zeroCol = j;
				}
			}
		}
		// adding the neighbors
		if (zeroRow - 1 >= 0)
			list.add(getNewBoard(zeroRow, zeroCol, zeroRow - 1, zeroCol));
		if (zeroRow + 1 < n)
			list.add(getNewBoard(zeroRow, zeroCol, zeroRow + 1, zeroCol));
		if (zeroCol - 1 >= 0)
			list.add(getNewBoard(zeroRow, zeroCol, zeroRow, zeroCol - 1));
		if (zeroCol + 1 < n)
			list.add(getNewBoard(zeroRow, zeroCol, zeroRow, zeroCol + 1));

		return list;
	}

	// a board that is obtained by exchanging any pair of tiles
	public EightPuzzleEngine twin() {
		int firstRow = 0, firstCol = 0;
		int secondRow = 0, secondCol = 0;
		boolean first = true, second = true;;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (tiles[i][j] != 0 && first) {
					firstRow = i;
					firstCol = j;
					first = false;
				}else if(tiles[i][j] != 0 && second) {
					secondRow = i;
					secondCol = j;
					second = false;
				} else if(!first && !second)
					break;
			}
		}
		
		return getNewBoard(firstRow, firstCol, secondRow, secondCol);
	   	
	}

	private EightPuzzleEngine getNewBoard(int i1, int j1, int i2, int j2) {
		// creating new array
		int[][] curr = new int[this.dimension()][this.dimension()];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				curr[i][j] = tiles[i][j];
			}
		}
		
		// exchanging the values
		EightPuzzleEngine theBoard = new EightPuzzleEngine(curr);
		int temp = theBoard.tiles[i1][j1];
		theBoard.tiles[i1][j1] = theBoard.tiles[i2][j2];
		theBoard.tiles[i2][j2] = temp;
		return theBoard;
	}
	
	// string representation of this board
	public String toString() {
		StringBuilder representation = new StringBuilder();
		representation.append(tiles.length);
		representation.append('\n');

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (j != n - 1)
					representation.append(tiles[i][j] + " ");
				else
					representation.append(tiles[i][j]);
			}
			representation.append('\n');
		}
		return representation.toString();
	}

}
