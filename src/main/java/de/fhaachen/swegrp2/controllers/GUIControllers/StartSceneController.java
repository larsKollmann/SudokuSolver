package de.fhaachen.swegrp2.controllers.GUIControllers;

import de.fhaachen.swegrp2.MainApp;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;

/**
 * Created by simon on 12.12.2016.
 */
public class StartSceneController {
    public void printGuiValues(ActionEvent actionEvent) {
    }

    public void importFile(ActionEvent actionEvent) {
    }

    public void test(ActionEvent actionEvent) {
        DialogStage test = new DialogStage("a", "b", MainApp.primaryStage );
        test.showAndWait();
    }
}
