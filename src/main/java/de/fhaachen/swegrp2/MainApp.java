package de.fhaachen.swegrp2;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApp extends Application {
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public void start(Stage stage) throws Exception {
        String fxmlFile = "/fxml/PrimaryStage/SudokuScene.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = loader.load(getClass().getResourceAsStream(fxmlFile));

        Scene scene = new Scene(rootNode);

        stage.setTitle("Sudoku Löser");
        stage.setScene(scene);
        GridPane mainGridPane = (GridPane) scene.lookup("#mainGridPane");

        stage.setMinHeight(152 * 3);
        stage.setMinWidth(139 * 3);
        stage.show();

        stage.minWidthProperty().bind(((BorderPane)rootNode).widthProperty());
    }
}
