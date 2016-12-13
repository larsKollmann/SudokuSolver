package de.fhaachen.swegrp2.controllers.GUIControllers;

import de.fhaachen.swegrp2.MainApp;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * Created by simon on 12.12.2016.
 */
public class StartSceneController {
    public void printGuiValues(ActionEvent actionEvent) {
    }

    public void importFile(ActionEvent actionEvent) {
    }

    public void test(ActionEvent actionEvent) {
        DialogStage test = new DialogStage("Dies ist ein Testhinweis. \nBeachten Sie das fehlende Bild!", "Hinweis", false, MainApp.primaryStage );
        test.showAndWait();
    }

    public void neutest(ActionEvent actionEvent) {
            DialogStage test = new DialogStage("Ist dies ein Test?", "Frage", true, MainApp.primaryStage);
            test.showAndWait();
    }
}
