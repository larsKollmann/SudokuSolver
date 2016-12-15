package de.fhaachen.swegrp2.controllers.GUIControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by simon on 12.12.2016.
 */
public class DialogStage extends Stage implements Initializable {

    @FXML private ImageView imageView;
    @FXML private Label text;
    @FXML private Button Ok;
    @FXML private Button Ja;
    @FXML private Button Nein;

    private String message;
    private Stage parentStage;
    private Boolean isConfirmDialog;

    public DialogStage(String message, String Title, Boolean isConfirmDialog, Stage parentStage) {
        setTitle(Title);

        this.message = message;
        this.parentStage = parentStage;
        this.isConfirmDialog = isConfirmDialog;

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
    public void onOkButtonAction(ActionEvent event) {
        close();
    }

    public void onNoButtonAction(ActionEvent actionEvent) throws IOException {
        close();
    }

    public void onYesButtonAction(ActionEvent actionEvent) {
        close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        if(isConfirmDialog) {
            Ok.setVisible(false);
        }
        else {
            Ja.setVisible(false);
            Nein.setVisible(false);
        }

        text.setText(message);
        initModality(Modality.WINDOW_MODAL);
        initOwner(parentStage);
        setResizable(false);
    }

}