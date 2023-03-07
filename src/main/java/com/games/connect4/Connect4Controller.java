package com.games.connect4;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Connect4Controller implements Initializable{

    @FXML
    private GridPane theGrid;
    
    @FXML 
    private ComboBox<String> levels;
    
    @FXML
    private Label turnLabel;
    
    @FXML
    private Button resetButton;
    
    private String[] levelString = {"Easy", "Medium", "Hard"};
    private List<Label[]> columns = new ArrayList<>();
    private VBox[] columnContainers = new VBox[8];
    private Connect4Engine game = new Connect4Engine();
    private ScaleTransition[] animation = new ScaleTransition[4];

    private void addActionListeners(){
        int i=0;
        for(VBox colContainer : columnContainers){
            int index = i++;
            colContainer.setOnMouseClicked(event -> {play(index);});
        }
    }
    
    private void addHoverListeners(){
        for(VBox colContainer : columnContainers){
            colContainer.setOnMouseEntered(event -> {changeColor(colContainer);});
            colContainer.setOnMouseExited(event -> {resetColor(colContainer);});
        }
    }

    private void play(int col){
        if(game.isEnd() || !game.validCol(col) || game.isTie())
            return;

        char curr = game.getTurn();
        columns.get(col)[game.columns[col]].setText(curr+"");
        columns.get(col)[game.columns[col]].setTextFill(game.getColor());
        game.setValue(col);

        if(game.whoWins() == game.getTurn()){
            turnLabel.setTextFill(Color.LIGHTGREEN);
            colorWinningCells();
            startAnimation();
        }
        else if(game.isTie())
            turnLabel.setText("-");
        else{
            game.changeTurn();
            turnLabel.setText(game.getTurn()+"");
        }
    }

    private void changeColor(VBox col){
        col.setBackground(new Background(new BackgroundFill(Color.valueOf("#B83B5E"), null, null)));
    }
    private void resetColor(VBox col){
        col.setBackground(new Background(new BackgroundFill(Color.valueOf("#4f0a56"), null, null)));
    }

    // WinningCells util functions
    private void colorWinningCells(){
        for(int[] cell : game.getWinningCells()){
            Label currLabel = columns.get(cell[1])[cell[0]];
            currLabel.setTextFill(Color.LIGHTGREEN);
        }
    }
    private void startAnimation(){
        int i=0;
        for(int[] cell : game.getWinningCells()){
            Label currLabel = columns.get(cell[1])[cell[0]];
            ScaleTransition scale = new ScaleTransition();
            scale.setNode(currLabel);
            scale.setByX(0.5);
            scale.setByY(0.5);
            scale.setAutoReverse(true);
            scale.setCycleCount(ScaleTransition.INDEFINITE);
            scale.setDuration(Duration.millis(500));
            scale.play();
            animation[i++] = scale;
        }
    }
    private void stopAnimation(){
        for(ScaleTransition scale : animation){
            scale.jumpTo(Duration.ZERO);
            scale.stop();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        levels.getItems().addAll(levelString);

        // initialize columns with the grid children
        for(int i=0; i<theGrid.getChildren().size(); i++){
            VBox temp = (VBox) theGrid.getChildren().get(i);
            columnContainers[i] = temp;  
            Label[] tempLabel = new Label[8];
            int j=0;
            for(Node n : temp.getChildren()) 
                tempLabel[j++] = (Label) n;
            columns.add(tempLabel);
        }
        addHoverListeners();
        addActionListeners();

    }

    @FXML
    public void resetGame(){
        game = new Connect4Engine();
        for(Label[] arr : columns){
            for(int i=0; i<arr.length; i++){
                arr[i].setText("-");
                arr[i].setTextFill(Color.WHITE);
            }
        }
        turnLabel.setText(game.getTurn() + "");
        turnLabel.setTextFill(game.getColor());
        stopAnimation();
    }

}