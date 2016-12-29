package de.fhaachen.swegrp2.controllers.GUIControllers.PrimaryStageControllers;

import de.fhaachen.swegrp2.MainApp;
import de.fhaachen.swegrp2.controllers.GUIControllers.DialogStage;
import de.fhaachen.swegrp2.controllers.SudokuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class PrimaryStageSharedController {
    private SudokuController controller = SudokuController.getInstance();

    @FXML
    public void close(ActionEvent actionEvent) {
        MainApp.primaryStage.close();
    }

    @FXML
    public boolean importFile(ActionEvent actionEvent) {
        Boolean  returnValue = false;

        String sourceID = actionEvent.getSource().toString();
        String fileType = sourceID.substring(sourceID.indexOf('$') + 1, sourceID.indexOf(','));

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(fileType + "-Datei importieren");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(fileType + "-Dateien",
                "*." + fileType.toLowerCase()));

        File file = fileChooser.showOpenDialog(MainApp.primaryStage);
        try {
            if(file != null) {
                controller.ImportFile(file);
                returnValue = true;
            }
        } catch (Exception e) {
            DialogStage test = new DialogStage("Die gewählte Datei ist nicht korrekt", "Fehler", false);
            test.showAndWait();
            returnValue = false;
        }
        return returnValue;
    }

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

            stage.show();
        } catch (Exception e ) {
            DialogStage error = new DialogStage("Die Hilfedatei ist beschädigt\noder nicht vorhanden.",
                    "Fehler", false);
            error.showAndWait();
        }
    }
}
