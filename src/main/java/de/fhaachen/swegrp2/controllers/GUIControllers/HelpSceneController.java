package de.fhaachen.swegrp2.controllers.GUIControllers;


import de.fhaachen.swegrp2.MainApp;
import de.fhaachen.swegrp2.models.Help;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelpSceneController {

    @FXML private ComboBox<Help> comboBox;
    @FXML private TextArea textArea;
    @FXML private ScrollPane scrollPane;

    private List<Help> getHelpList() throws Exception {
        JSONParser parser = new JSONParser();

        Object obj = parser.parse(new FileReader(getClass().getResource("/hilfetexte.json").getPath()));
        JSONObject jsonObject = (JSONObject) obj;

        JSONArray hilfetexte = (JSONArray) jsonObject.get("hilfetexte");

        List<Help> helpList = new ArrayList<>();

        for(int i = 0; i < hilfetexte.size(); i++) {
            String titel =  ((JSONObject) hilfetexte.get(i)).get("Titel").toString();
            String hilfetext =  ((JSONObject) hilfetexte.get(i)).get("Hilfetext").toString();
            helpList.add(new Help(titel,hilfetext));
        }
        return helpList;
    }

    @FXML
    protected void initialize() {
        try {
            List<Help> list = getHelpList();
            for (Help  help: list) {
                comboBox.getItems().add(help);
            }

            comboBox.valueProperty().addListener(new ChangeListener<Help>() {
                @Override
                public void changed(ObservableValue<? extends Help> observable, Help oldValue, Help newValue) {
                    textArea.setText(newValue.text);
                }
            });


        } catch (Exception e) {
            DialogStage error = new DialogStage("Die Hilfedatei ist beschödigt\noder nicht vorhanden.", "Fehler", false, MainApp.primaryStage);
            error.showAndWait();
        }
    }


}

