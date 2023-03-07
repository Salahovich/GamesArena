package com.games;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class PrimaryController{

    @FXML
    private GridPane mainPane;

    @FXML 
    private TextField nameField;

    private String[] FXMLpaths = {  "TicTacToe_Scene", "Connect-4_Scene", 
                                    "Checkers_Scene", "8-Puzzle_Scene",
                                    "Mines_Scene", "Chess_Scene" 
                                };

    /*
    *   Initialize the pane elements withe the listener function. 
    */
    public void init(Stage stage) {
        for (int i = 0; i < mainPane.getChildren().size(); i++) {
            Label game = (Label) mainPane.getChildren().get(i);

            // load the root node from the corresponding FXML file. 
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(FXMLpaths[i] + ".fxml"));
            
            // set the listener to change the scene of the stage.
            game.setOnMouseClicked(event -> {
                try {
                    if(nameField.getText().length()!=0)
                        stage.setScene(new Scene(fxmlLoader.load()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
        }
    }

}
