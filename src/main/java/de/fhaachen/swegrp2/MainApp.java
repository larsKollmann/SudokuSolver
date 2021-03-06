package de.fhaachen.swegrp2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * <p><b>Titel:</b> MainApp </p>
 * <p><b>Beschreibung:</b> Zentrale Klasse der Applikation,
 * setzt die nötigen Parameter und startet die GUI.</p>
 */
public class MainApp extends Application {
    /**
     * Java main, startet die Applikation.
     * @param args Argumente, die dem Programm beim Start übergeben werden können. Nicht benutzt.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Das globale Fenster der JavaFX Anwendung.
     */
    public static Stage primaryStage;

    /**
     * Start Methode, die von Applikation geerbt wird.
     * Lädt für das Fenster nötige Dateien, setzt Parameter und startet dies.
     * @param stage Das Hauptfenster der Anwendung
     * @throws Exception Wird vom loader geworfen, wenn das Laden nötiger Dateien fehlschlägt.
     */
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        String fxmlFile = "/fxml/PrimaryStage/StartScene.fxml";

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/Sudoku.png")));
        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = loader.load(getClass().getResourceAsStream(fxmlFile));

        Scene scene = new Scene(rootNode);

        stage.setTitle("Sudoku Löser");
        stage.setScene(scene);
        stage.setResizable(false);

        stage.show();
    }
}
