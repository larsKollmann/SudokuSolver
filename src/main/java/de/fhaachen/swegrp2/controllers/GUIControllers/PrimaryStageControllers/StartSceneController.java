package de.fhaachen.swegrp2.controllers.GUIControllers.PrimaryStageControllers;

import de.fhaachen.swegrp2.MainApp;
import de.fhaachen.swegrp2.controllers.SudokuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class StartSceneController extends PrimaryStageSharedController {
    private SudokuController controller = SudokuController.getInstance();

    private void switchToSudokuScene() {
        try {
            FXMLLoader loader = new FXMLLoader();
            Parent parent = loader.load(getClass().getResourceAsStream("/fxml/PrimaryStage/SudokuScene.fxml"));
            MainApp.primaryStage.setScene(new Scene(parent));
            MainApp.primaryStage.setResizable(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public boolean importFile(ActionEvent actionEvent) {
        if(super.importFile(actionEvent))  {
            switchToSudokuScene();
            return true;
        }
        return false;
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
