package de.fhaachen.swegrp2.controllers.GUIControllers;

import de.fhaachen.swegrp2.models.Help;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class HelpSceneController {

    @FXML private ComboBox<Help> comboBox;
    @FXML private TextArea textArea;

    private List<Help> getHelpList() throws Exception {
        JSONParser parser = new JSONParser();

        Object obj = parser.parse(new FileReader(getClass().getResource("/hilfetexte.json").getPath()));

        JSONObject jsonObject = (JSONObject) obj;

        JSONArray hilfetexte = (JSONArray) jsonObject.get("hilfetexte");

        List<Help> helpList = new ArrayList<>();

        for (Object hilfethema : hilfetexte) {
            String titel = ((JSONObject) hilfethema).get("Titel").toString();
            String hilfetext = ((JSONObject) hilfethema).get("Hilfetext").toString();
            helpList.add(new Help(titel, hilfetext));
        }
        return helpList;
    }

    @FXML
    protected void initialize() throws Exception {

        List<Help> list = getHelpList();
        for (Help  help: list) {
            comboBox.getItems().add(help);
        }

        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> textArea.setText(newValue.text));
    }

    @FXML
    public void close(ActionEvent actionEvent) {
        ((Stage)((Button)actionEvent.getSource()).getScene().getWindow()).close();
    }
}

