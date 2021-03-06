package de.fhaachen.swegrp2.controllers.GUIControllers;

import de.fhaachen.swegrp2.MainApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Die Klasse stellt den Prototypen der Fehler- bzw. Fragedialoge dar.
 * Es kann ein Dialog für Fehler erstellt werden, welcher nur bestätigt werden kann oder ein Auswahl-Dialog der einen Ja bzw. Nein-Aktion auslösen kann.
 */
public class DialogStage extends Stage implements Initializable {
    public enum DialogType {ERROR, CONFIRM, INFO}

    @FXML private ImageView imageView;
    @FXML private Label text;
    @FXML private Button Ok;
    @FXML private Button Ja;
    @FXML private Button Nein;

    private String message;
    private Stage parentStage;
    private DialogType dialogType;
    private Image image;
    private boolean clicked;

    public boolean showAndWaitGetResult() {
        showAndWait();
        return clicked;
    }

    public DialogStage(String message, String title, DialogType dialogType) {
        setTitle(title);

        this.message = message;
        this.dialogType = dialogType;
        this.parentStage = MainApp.primaryStage;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Dialog.fxml"));
        fxmlLoader.setController(this);

        try {
            setScene(new Scene(fxmlLoader.load()));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stellt die Ok-Action des Fehlerdialogs dar
     */
    @FXML
    public void onOkButtonAction(ActionEvent actionEvent){
        this.clicked = true;
        close();
    }

    /**
     * Stellt die Nein-Action des Auswahldialogs dar
     */
    @FXML
    public void onNoButtonAction(ActionEvent actionEvent)  {
        this.clicked = false;
        close();
    }

    /**
     * Stellt die Ja-Action des Auswahldialogs dar
     */
    @FXML
    public void onYesButtonAction(ActionEvent actionEvent) {
        this.clicked = true;
        close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        switch (dialogType){
            case INFO:
                image = new Image(getClass().getResourceAsStream("/images/exclamation.png"));
                Ja.setVisible(false);
                Nein.setVisible(false);
                Ok.setDefaultButton(true);
                break;
            case ERROR:
                image = new Image(getClass().getResourceAsStream("/images/error.png"));
                Ja.setVisible(false);
                Nein.setVisible(false);
                Ok.setDefaultButton(true);
                break;
            case CONFIRM:
                image = new Image(getClass().getResourceAsStream("/images/question.png"));
                Ok.setVisible(false);
                Nein.setDefaultButton(true);
                break;
        }

        imageView.setImage(image);
        text.setText(message);
        initModality(Modality.WINDOW_MODAL);
        initOwner(parentStage);
        setResizable(false);
    }
}
