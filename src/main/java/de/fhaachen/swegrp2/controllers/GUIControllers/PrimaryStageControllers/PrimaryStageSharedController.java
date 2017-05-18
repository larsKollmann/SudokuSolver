package de.fhaachen.swegrp2.controllers.GUIControllers.PrimaryStageControllers;

import de.fhaachen.swegrp2.MainApp;
import de.fhaachen.swegrp2.controllers.GUIControllers.DialogStage;
import de.fhaachen.swegrp2.controllers.SudokuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

import static de.fhaachen.swegrp2.controllers.GUIControllers.DialogStage.DialogType.ERROR;

/**
 * <p>Titel:PrimaryStageSharedController</p>
 * <p>Beschreibung: Der PrimaryStageSharedController dient der gemeinsamen Funktionalität der StartScene und der
 * SudokuScene. Dabei handelt es sich primär um die Startleiste, die die beiden Stages teilen.</p>
 */
public class PrimaryStageSharedController {
    private SudokuController controller = SudokuController.getInstance();

    @FXML
    public void close(ActionEvent actionEvent) {
        MainApp.primaryStage.close();
    }

    /**
     * Die importFile-Methode dient dem Import von Sudokus, dabei wird ein Dateiexplorer geöffnet, in dem der User das gewünschte
     * Sudoku auswählen kann
     * @param actionEvent Wird ausgelöst bei Klick auf einen der Import-Steuerelemente
     * @return Gibt zurück ob der Import erfolgreich war
     */
    @FXML
    public boolean importFile(ActionEvent actionEvent) {
        Boolean returnValue = false;

        String sourceID = actionEvent.getSource().toString();
        String fileType = sourceID.substring(sourceID.indexOf('$') + 1, sourceID.indexOf(','));

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(fileType + "-Datei importieren");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(fileType + "-Dateien", "*." + fileType.toLowerCase()));

        File file = fileChooser.showOpenDialog(MainApp.primaryStage);
        try {
            if(file != null) {
                controller.ImportFile(file);
                returnValue = true;
            }
        } catch (Exception e) {
            DialogStage test = new DialogStage("Die gewählte Datei ist nicht korrekt", "Fehler", ERROR);
            test.showAndWait();
            returnValue = false;
        }
        return returnValue;
    }

    /**
     * Öffnet die Hilfe-Ansicht
     * @param actionEvent Wird bei Klick auf "Hilfe anzeigen" ausgelöst
     */
    @FXML
    public void showHelpStage(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Parent rootNode = loader.load(getClass().getResourceAsStream("/fxml/SecondaryStage/HelpScene.fxml"));

            Scene scene = new Scene(rootNode);

            stage.setTitle("Hilfe");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/Sudoku.png")));

            stage.show();
        } catch (Exception e ) {
            DialogStage error = new DialogStage("Die Hilfedatei ist beschädigt\noder nicht vorhanden.",
                    "Fehler", ERROR);
            error.showAndWait();
        }
    }
}
