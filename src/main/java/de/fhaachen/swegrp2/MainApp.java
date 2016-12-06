package de.fhaachen.swegrp2;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApp extends Application {

    private static final Logger log = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public void start(Stage stage) throws Exception {

//        log.info("Starting Hello JavaFX and Maven demonstration application");

        String fxmlFile = "/fxml/PrimaryStage/SudokuScene.fxml";
//        log.debug("Loading FXML for main view from: {}", fxmlFile);
        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));

//        log.debug("Showing JFX scene");
        //prefHeight="700.0" prefWidth="600.0"
        Scene scene = new Scene(rootNode, 600, 700);

        stage.setTitle("Sudoku Löser");
        stage.setScene(scene);
        stage.show();
    }
}
