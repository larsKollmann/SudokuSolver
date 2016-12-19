package de.fhaachen.swegrp2.controllers.GUIControllers.PrimaryStageControllers;

import de.fhaachen.swegrp2.MainApp;
import de.fhaachen.swegrp2.controllers.GUIControllers.DialogStage;
import de.fhaachen.swegrp2.controllers.SudokuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

<<<<<<< HEAD:src/main/java/de/fhaachen/swegrp2/controllers/GUIControllers/StartSceneController.java
public class StartSceneController {
=======
/**
 * Created by simon on 12.12.2016.
 */
public class StartSceneController extends PrimaryStageSharedController {
>>>>>>> ae274fa1ea4f88e0584511543cb864aa5050e3a7:src/main/java/de/fhaachen/swegrp2/controllers/GUIControllers/PrimaryStageControllers/StartSceneController.java
    private SudokuController controller = SudokuController.getInstance();

    private void switchToSudokuScene() {
        try {
            FXMLLoader loader = new FXMLLoader();
            Parent parent = loader.load(getClass().getResourceAsStream("/fxml/PrimaryStage/SudokuScene.fxml"));
            MainApp.primaryStage.setScene(new Scene(parent));
            MainApp.primaryStage.sizeToScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void importFile(ActionEvent actionEvent) {
        super.importFile(actionEvent);

        switchToSudokuScene();
    }

    @FXML
    public void generate(ActionEvent actionEvent) {
        controller.generate();
        switchToSudokuScene();
    }

    @FXML
    public void NewSudoku(ActionEvent actionEvent) {
        String idString = actionEvent.getSource().toString();
        int size = Integer.parseInt(idString.substring(idString.indexOf('$') + 1, idString.indexOf(',')));
        controller.reset(size);
        switchToSudokuScene();
    }
}
