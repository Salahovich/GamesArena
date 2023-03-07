package com.games.tictactoe;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;

public class TicTacToeController implements Initializable{

    @FXML  
    GridPane grid;

    @FXML
    ComboBox<Integer> gridSizeOptions;

    @FXML
    Label playerTurn;

    

    private Integer[] sizes = {3,6};
    private int gridSize = 3;
    private int turn = 0;
    private TicTacToeEngine game;

    @FXML
    public void resetGame(){
        changeGrid();
    }   

    public void addListeners(){
        for(int i=0; i<grid.getChildren().size(); i++){
            Label n = (Label) grid.getChildren().get(i);
            int row = GridPane.getRowIndex(n);
            int col = GridPane.getColumnIndex(n);
            n.setOnMouseClicked(event -> {
                char symbol = turn==0?'X':'O';
                if(!game.isSet(row, col) && !game.end()){
                    game.setValue(row, col, symbol); 
                    n.setText(symbol+"");
                    playerTurn.setText(turn==0?"O":"X");
                    n.setTextFill(turn==0?Color.valueOf("#F9ED69"):Color.valueOf("#F08A5D"));
                    changeTurn();
                    if(game.whoWins() == symbol){
                        playerTurn.setText(symbol+"");
                        playerTurn.setTextFill(Color.valueOf("#13ff00"));
                    }
                    else if(game.tie())
                        playerTurn.setText("-");
                }
                
            });
        }
    }

    public void changeTurn(){
        this.turn = turn==1?0:1; 
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // init the combo-box with the values; 
        gridSizeOptions.getItems().addAll(sizes);
        gridSizeOptions.setValue(3); 
        game = new TicTacToeEngine(gridSize);
        //grid.getScene().getStylesheets().add("")
       changeGrid();
    }

    @FXML
    public void changeGrid(){
        
        // remove the grid children and constraints on size;
        grid.getChildren().clear();
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();
        
        gridSize = gridSizeOptions.getSelectionModel().getSelectedItem();
        game = new TicTacToeEngine(gridSize);
        playerTurn.setTextFill(Color.valueOf("#f9ed69"));
        this.turn = 0;
        
        double currWidth = grid.getPrefWidth();      
        double currHeight = grid.getPrefHeight();
        String style = "-fx-font-size: 60px; -fx-font-family: \"Gotham Medium\"";

        // Constructing the new grid with the perfect size
        for(int i=0; i<gridSize; i++){
            for(int j=0; j<gridSize; j++){
                Label curr = new Label("-");
                curr.setTextFill(Color.WHITE);
                curr.setStyle(style);
                curr.setMinSize(currWidth/gridSize, currHeight/gridSize);
                curr.setAlignment(Pos.CENTER);
                grid.add(curr, j, i);
            }
            grid.getColumnConstraints().add(new ColumnConstraints(currWidth/gridSize));
            grid.getRowConstraints().add(new RowConstraints(currHeight/gridSize));
        }

        addListeners();
    }
}
