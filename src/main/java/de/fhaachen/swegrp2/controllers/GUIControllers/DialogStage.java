package de.fhaachen.swegrp2.controllers.GUIControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by simon on 12.12.2016.
 */
public class DialogStage extends Stage implements Initializable {

//    @FXML
//    private TextField addressTextBox;

    @FXML private Label text;
//
//    @FXML
//    private Button okButton;
//
//    @FXML
//    private TextField nameTextBox;

    private String defaultName;
    private String defaultAddress;
    private Stage parentStage;

    public DialogStage(String defaultName, String defaultAddress, Stage parentStage) {
        setTitle("This is a test dialog");

        this.defaultName = defaultName;
        this.defaultAddress = defaultAddress;
        this.parentStage = parentStage;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Dialog.fxml"));
        fxmlLoader.setController(this);

        // Nice to have this in a load() method instead of constructor, but this seems to be de-facto standard.
        try
        {
            setScene(new Scene((Parent) fxmlLoader.load()));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
//
//    @FXML
//    void onOkButtonAction(ActionEvent event)
//    {
//        close();
//    }
//
//    public String getName()
//    {
//        return nameTextBox.getText();
//    }
//
//    public String getAddress()
//    {
//        return addressTextBox.getText();
//    }

    /*
     * Called when FXML file is load()ed (via FXMLLoader.load()).  It will execute before the form is shown.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        text.setText(defaultName);
        initModality(Modality.WINDOW_MODAL);
        initOwner(parentStage);
//        nameTextBox.setText(defaultName);
//        addressTextBox.setText(defaultAddress);
    }
}