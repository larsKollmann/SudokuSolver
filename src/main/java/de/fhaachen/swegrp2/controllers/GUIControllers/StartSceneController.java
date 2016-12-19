package de.fhaachen.swegrp2.controllers.GUIControllers;

import de.fhaachen.swegrp2.MainApp;
import de.fhaachen.swegrp2.controllers.SudokuController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

import java.io.IOException;

public class StartSceneController {
    private SudokuController controller = SudokuController.getInstance();

    @FXML
    public void close(ActionEvent actionEvent) {
        MainApp.primaryStage.close();
    }

    @FXML
    public void printGuiValues(ActionEvent actionEvent) {
    }

    @FXML
    public void importFile(ActionEvent actionEvent) {
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

    public void generate(ActionEvent actionEvent) {
        controller.generate();

        try {
            FXMLLoader loader = new FXMLLoader();
            Parent parent = loader.load(getClass().getResourceAsStream("/fxml/PrimaryStage/SudokuScene.fxml"));
            MainApp.primaryStage.setScene(new Scene(parent));
            MainApp.primaryStage.sizeToScene();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
