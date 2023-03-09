package com.games.eightpuzzle;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class EightPuzzleController implements Initializable{
    
    @FXML 
    GridPane board;
    
    @FXML 
    Button resetButton, shuffleButton, solveButton;
    
    @FXML
    private Label clicksLabel;

    private Label[][] labelBoard;
    private int boardSize = 3; 
    private EightPuzzleEngine game;
    private Solver solver;
    private ScaleTransition[] animation; 
    private int clicks;
    private Color emptyColor = Color.valueOf("#f9ed69");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.game = new EightPuzzleEngine(boardSize);
        this.solver = new Solver(this.game);
        this.labelBoard = new Label[boardSize][boardSize];
        this.animation = new ScaleTransition[boardSize*boardSize-1];

        int i=0, j=0;
        for(Node n : board.getChildren()){
            Label l = (Label) n;
            
            l.setText(game.tiles[i][j]+"");
            if(game.tiles[i][j] == 0)
                l.setTextFill(emptyColor);
            labelBoard[i][j++] = l;
            if(j == 3){
                i++;    j=0;
            }
        }
        addListeners();
    }

    public void addListeners(){
        for(int i=0; i<boardSize; i++){
            for(int j=0; j<boardSize; j++){
                int row = i, col = j;
                labelBoard[i][j].setOnMouseClicked(event -> {play(row, col);});
            }
        }
    }

    public void play(int i, int j){
        int[] zeroIndex = game.play(i, j);
        if(zeroIndex != null && !game.isEnd()){
            String temp = labelBoard[i][j].getText();
            labelBoard[i][j].setText(labelBoard[zeroIndex[0]][zeroIndex[1]].getText() + "");
            labelBoard[zeroIndex[0]][zeroIndex[1]].setText(temp);
            if(game.isGoal())
                startAnimation();
            clicksLabel.setText(++clicks + "");
            labelBoard[i][j].setTextFill(emptyColor);
            labelBoard[zeroIndex[0]][zeroIndex[1]].setTextFill(Color.WHITE);
        }
    }
    public void addHoverListeners(){

    }

    public void startAnimation(){
        int count = 0;
        for(int i=0; i<boardSize; i++){
            for(int j=0; j<boardSize; j++){
                Label currLabel = labelBoard[i][j];
                ScaleTransition scale = new ScaleTransition();
                scale.setNode(currLabel);
                scale.setByX(0.5);
                scale.setByY(0.5);
                scale.setAutoReverse(true);
                scale.setCycleCount(ScaleTransition.INDEFINITE);
                scale.setDuration(Duration.millis(500));
                scale.play();
                if(i != boardSize - 1 || j != boardSize - 1)
                    animation[count++] = scale;
            }
        }
    }

    public void stopAnimation(){
        for(ScaleTransition scale : animation){
            if(scale == null)
                continue;
            scale.jumpTo(Duration.ZERO);
            scale.stop();
        }
    }

    @FXML 
    public void shuffleGame(){
        do{
            game.shuffle();
            solver = new Solver(game);
        }while(!solver.isSolvable());

        for(int i=0; i<boardSize; i++){
            for(int j=0; j<boardSize; j++){
                labelBoard[i][j].setText(game.tiles[i][j] + "");
            }
        }
        clicks = 0;
        clicksLabel.setText(clicks + "");
        stopAnimation();
    }

    @FXML 
    public void solveGame(){
        
    }

    private void solveGameHelper(){
        List<EightPuzzleEngine> result = (LinkedList<EightPuzzleEngine>) solver.solution();
        for(EightPuzzleEngine sol : result){
            for(int i=0; i<boardSize; i++){
                for(int j=0; j<boardSize; j++){
                    labelBoard[i][j].setText(sol.tiles[i][j] + "");
                }
            }
        }
    }




    
}
