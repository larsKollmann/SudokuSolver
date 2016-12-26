package de.fhaachen.swegrp2.controllers.GUIControllers;


import de.fhaachen.swegrp2.models.Help;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HelpSceneController {

    @FXML private ComboBox<Help> comboBox;
    @FXML private TextArea textArea;
    @FXML private ScrollPane scrollPane;

    private List<Help> getHelpList() throws Exception {
        JSONParser parser = new JSONParser();

        String path = "./hilfetexte.json";
        FileInputStream fis = new FileInputStream(path);
        BufferedReader in = new BufferedReader(new InputStreamReader(fis));
        Object obj = parser.parse(in);

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
    protected void initialize() throws Exception {

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


    }

    @FXML
    public void close(ActionEvent actionEvent) {
        ((Stage)((Button)actionEvent.getSource()).getScene().getWindow()).close();
    }

}

