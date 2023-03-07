package com.games;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("Primary_Scene" + ".fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        PrimaryController controller = fxmlLoader.getController();
        controller.init(stage);
        
        stage.setScene(scene);
        stage.setTitle("Games Arena");
        stage.setResizable(false);
        stage.show();
    }
        


    public static void main(String[] args) {
        launch();
    }

}