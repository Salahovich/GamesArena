module com.games {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.games to javafx.fxml;
    exports com.games;
}
