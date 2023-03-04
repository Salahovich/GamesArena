package com.games;

import java.io.IOException;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        Pane pane = new Pane();

        ReadOnlyDoubleProperty widthProperty = pane.widthProperty();
        widthProperty.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldVal, Number newVal) {

                System.out.println("widthProperty changed from "
                        + oldVal.doubleValue() + " to " + newVal.doubleValue());
            }
        });

        DoubleProperty prefWidthProperty = pane.prefWidthProperty();
        prefWidthProperty.addListener(
                (prop, oldVal, newVal) -> {
                    System.out.println("prefWidthProperty changed from "
                            + oldVal.doubleValue() + " to " + newVal.doubleValue());
                });

        prefWidthProperty.set(123);

        Scene scene = new Scene(pane, 1024, 800, true);
        stage.setScene(scene);
        stage.setTitle("2D Example");

        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}