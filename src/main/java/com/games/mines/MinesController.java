package com.games.mines;

import java.net.URL;
import java.security.Timestamp;
import java.util.Iterator;
import java.util.ResourceBundle;
import com.games.Timer;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class MinesController implements Initializable{
    
    @FXML
    private GridPane board; 

    @FXML 
    private Button resetButton, hintButton;
    
    @FXML
    private Label minesNum;
    
    @FXML
    private Label timerLabel;

    @FXML
    private RadioButton easy, medium, hard;
    
    @FXML 
    private ToggleGroup difficulty;

    private MinesEngine game;
    private int boardSize = 10;
    private Label[][] labelBoard;
    private boolean[][] clicked;
    private boolean[][] rightClicked;
    private boolean end; 
    private int flagCount;
    private int bombsCount;
    private int clickedCount; 
    private int hints; 
    private FadeTransition[] fades; 

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        easy.setSelected(true);
        addRadioListeners();
        initBoard(boardSize);
       
    }

    private void addRadioListeners(){
        easy.setOnAction(event -> {initBoard(10);});
        medium.setOnAction(event -> {initBoard(15);});
        hard.setOnAction(event -> {initBoard(25);});
    }

    private void addListeners(){
        for(int i=0; i<boardSize; i++){
            for(int j=0; j<boardSize; j++){
                int row = i, col = j;
                labelBoard[i][j].setOnMouseClicked(event -> {
                    if(event.getButton() == MouseButton.SECONDARY){
                        if(end)
                            return;
                        else if(!rightClicked[row][col] && flagCount > 0){
                            labelBoard[row][col].setBackground(new Background(new BackgroundFill(Color.valueOf("#f9ed69"), null, null)));
                            rightClicked[row][col] = true;
                            minesNum.setText(--flagCount+"");
                        }
                        else if(rightClicked[row][col]){
                            labelBoard[row][col].setBackground(new Background(new BackgroundFill(Color.valueOf("#4f0a56"), null, null)));
                            rightClicked[row][col] = false;
                            minesNum.setText(++flagCount+"");
                        }
                    }else if(event.getButton() == MouseButton.PRIMARY){
                        if(end)
                            return;
                        else if(game.getGrid()[row][col] < 0){
                            labelBoard[row][col].setBackground(new Background(new BackgroundFill(Color.RED, null, null)));
                            end = true;
                        }else if(game.getGrid()[row][col]!=0 && !clicked[row][col]){
                            labelBoard[row][col].setText(game.getGrid()[row][col] + "");
                            labelBoard[row][col].setBackground(new Background(new BackgroundFill(Color.valueOf("#6A2C70"), null, null)));
                            clicked[row][col] = true;
                            clickedCount--;
                        }else
                            dfs(row, col);
                        if(clickedCount == bombsCount){
                            end = true;
                            startAnimation();
                        }
                    }
                });
                labelBoard[i][j].setOnMouseEntered(event -> {
                    if(end)
                        return;
                    if(!clicked[row][col] && !rightClicked[row][col])
                        labelBoard[row][col].setBackground(new Background(new BackgroundFill(Color.valueOf("#400e46"), null, null)));
                });
                labelBoard[i][j].setOnMouseExited(event -> {
                    if(end)
                        return;
                    if(!clicked[row][col] && !rightClicked[row][col])
                        labelBoard[row][col].setBackground(new Background(new BackgroundFill(Color.valueOf("#4f0a56"), null, null)));
                });
            }
        }
    }

    @FXML
    public void resetGame(){
        stopAnimation();
        initBoard(boardSize);
    }

    private void startAnimation(){
        Iterator<int[]> bombs = game.getBombs().iterator();
        int[] bomb = null;
        int i = 0;
        while(bombs.hasNext()){
            bomb = bombs.next();
            FadeTransition fade = new FadeTransition();
            fade.setNode(labelBoard[bomb[0]][bomb[1]]);
            labelBoard[bomb[0]][bomb[1]].setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, null, null)));
            fade.setFromValue(1);
            fade.setToValue(0);
            fade.setAutoReverse(true);
            fade.setCycleCount(ScaleTransition.INDEFINITE);
            fade.setDuration(Duration.millis(500));
            fade.play();
            fades[i++] = fade;
        }
    }
    private void stopAnimation(){
        for(FadeTransition f : fades){
            if(f == null)
                continue;
            f.jumpTo(Duration.ZERO);
            f.stop();
        }
    }
    @FXML
    public void hint(){
        if(end || hints == 0)
            return;
        int row = 0, col = 0;
        do{
            row = (int) (Math.random() * boardSize);
            col = (int) (Math.random() * boardSize);
        }while(game.getGrid()[row][col] < 0 || clicked[row][col]);
        labelBoard[row][col].setBackground(new Background(new BackgroundFill(Color.LIGHTSKYBLUE, null, null)));
        hints--;
    }

    private void dfs(int row, int col){
        if(row >= boardSize || row < 0 || col >= boardSize || col < 0)
            return; 
        if(game.getGrid()[row][col] != 0 || clicked[row][col])
            return;
        labelBoard[row][col].setBackground(new Background(new BackgroundFill(Color.valueOf("#6A2C70"), null, null)));
        clicked[row][col] = true;
        clickedCount--;
        dfs(row+1, col);
        dfs(row-1, col);
        dfs(row, col+1);
        dfs(row, col-1);
    }

    private void initObjects(){
        this.game = new MinesEngine(boardSize);
        this.labelBoard = new Label[boardSize][boardSize];
        this.clicked = new boolean[boardSize][boardSize];
        this.rightClicked = new boolean[boardSize][boardSize];
        this.end = false;
        this.bombsCount = game.getBombs().size();
        this.flagCount = bombsCount;
        this.hints = bombsCount;
        this.clickedCount = boardSize*boardSize;
        this.minesNum.setText(flagCount+"");
        this.fades = new FadeTransition[bombsCount];
    }

    private void initBoard(int size){
        board.getChildren().clear();
        board.getColumnConstraints().clear();
        board.getRowConstraints().clear();
        
        boardSize  = size;
        initObjects();

        double currWidth = board.getPrefWidth() - ((boardSize-1) * 2);      
        double currHeight = board.getPrefHeight() - ((boardSize-1) * 2);
        String style = "-fx-font-size: 20px; -fx-font-family: \"Gotham Medium\"";
        
        // Constructing the new grid with the perfect size
        for(int i=0; i<boardSize; i++){
            board.getColumnConstraints().add(new ColumnConstraints(Math.round(currWidth/boardSize)));
            board.getRowConstraints().add(new RowConstraints(Math.round(currHeight/boardSize)));
            for(int j=0; j<boardSize; j++){
                Label curr = new Label();
                curr.setBackground(new Background(new BackgroundFill(Color.valueOf("#4f0a56"), null, null)));
                curr.setPrefSize(Math.round(currWidth/boardSize), Math.round(currHeight/boardSize));
                curr.setTextFill(Color.WHITE);
                curr.setAlignment(Pos.CENTER);
                curr.setStyle(style);
                board.add(curr, j, i);
                labelBoard[i][j] = curr;
            }
        }
        addListeners();
    }
}
