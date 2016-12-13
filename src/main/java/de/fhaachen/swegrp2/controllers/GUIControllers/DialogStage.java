package de.fhaachen.swegrp2.controllers.GUIControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by simon on 12.12.2016.
 */
public class DialogStage extends Stage implements Initializable {

    @FXML private Label text;

    private String message;
    private Stage parentStage;

    public DialogStage(String message, String Title, Stage parentStage) {
        setTitle(Title);

        this.message = message;
        this.parentStage = parentStage;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Dialog.fxml"));
        fxmlLoader.setController(this);

        try
        {
            setScene(new Scene((Parent) fxmlLoader.load()));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    void onOkButtonAction(ActionEvent event)
    {
        close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        text.setText(message);
        initModality(Modality.WINDOW_MODAL);
        initOwner(parentStage);
    }
}