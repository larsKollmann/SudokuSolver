package de.fhaachen.swegrp2.controllers;


import de.fhaachen.swegrp2.models.Help;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class HelpSceneController {

    public List<Help> getHelpList() throws Exception {
        JSONParser parser = new JSONParser();

        Object obj = parser.parse(new FileReader(getClass().getResource("/hilfetexte.json").getPath()));
        JSONObject jsonObject = (JSONObject) obj;

        JSONArray hilfetexte = (JSONArray) jsonObject.get("hilfetexte");

        List<Help> helpList = new ArrayList<>();

        for(int i = 0; i < hilfetexte.size(); i++) {
            String titel =  ((JSONObject) hilfetexte.get(i)).get("Titel").toString();
            String hilfetext =  ((JSONObject) hilfetexte.get(i)).get("Hilfetext").toString();
            System.out.println(titel);
            helpList.add(new Help(titel,hilfetext));
        }
        return helpList;
    }


}

