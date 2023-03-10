module com.games {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.games to javafx.fxml;
    exports com.games;
    
    opens com.games.tictactoe to javafx.fxml;
    exports com.games.tictactoe;

    opens com.games.connect4 to javafx.fxml;
    exports com.games.connect4;

    opens com.games.eightpuzzle to javafx.fxml;
    exports com.games.eightpuzzle;

    opens com.games.mines to javafx.fxml;
    exports com.games.mines;
}
