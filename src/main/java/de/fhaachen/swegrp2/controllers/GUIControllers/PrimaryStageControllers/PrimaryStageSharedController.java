package de.fhaachen.swegrp2.controllers.GUIControllers.PrimaryStageControllers;

import de.fhaachen.swegrp2.MainApp;
import de.fhaachen.swegrp2.controllers.GUIControllers.DialogStage;
import de.fhaachen.swegrp2.controllers.SudokuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Created by simon on 17.12.2016.
 */
public class PrimaryStageSharedController {
    SudokuController controller = SudokuController.getInstance();

    @FXML
    public void close(ActionEvent actionEvent) {
        MainApp.primaryStage.close();
    }

    @FXML
    public void importFile(ActionEvent actionEvent) {

        String sourceID = actionEvent.getSource().toString();
        String fileType = sourceID.substring(sourceID.indexOf('$') + 1, sourceID.indexOf(','));

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(fileType + "-Datei importieren");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(fileType + "-Dateien", "*." + fileType.toLowerCase()));

        File file = fileChooser.showOpenDialog(MainApp.primaryStage);
        try {
            controller.ImportFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openHelpStage(ActionEvent actionEvent) {
    }

    //DEBUG
    @FXML
    public void printGuiValues(ActionEvent actionEvent) {
        Stage stage = MainApp.primaryStage;
        System.out.println(stage);
        System.out.println("W: " + stage.getWidth() + " H: " + stage.getHeight());
    }

    @FXML
    public void test(ActionEvent actionEvent) {
        DialogStage test = new DialogStage("Dies ist ein Testhinweis. \nBeachten Sie das fehlende Bild!", "Hinweis", false, MainApp.primaryStage );
        test.showAndWait();
    }

    @FXML
    public void neutest(ActionEvent actionEvent) {
        DialogStage test = new DialogStage("Ist dies ein Test?", "Frage", true, MainApp.primaryStage);
        test.showAndWait();
    }
}
