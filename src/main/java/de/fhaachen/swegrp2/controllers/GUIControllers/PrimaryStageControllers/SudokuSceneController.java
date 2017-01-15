package de.fhaachen.swegrp2.controllers.GUIControllers.PrimaryStageControllers;

import de.fhaachen.swegrp2.MainApp;
import de.fhaachen.swegrp2.controllers.GUIControllers.DialogStage;
import de.fhaachen.swegrp2.controllers.SudokuController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static de.fhaachen.swegrp2.controllers.GUIControllers.DialogStage.DialogType.*;
import static javafx.geometry.Pos.CENTER;
import static javafx.scene.layout.Priority.ALWAYS;

/**
 * <p>Titel: SodukuScene</p>
 * <p>Beschreibung: Dient der Steuerung der SudokuScene</p>
 */
public class SudokuSceneController extends PrimaryStageSharedController {
    private SudokuController controller = SudokuController.getInstance();
    private int currentSize;
    private DoubleProperty fontSize = new SimpleDoubleProperty(14);

    @FXML private GridPane mainGridPane;
    @FXML private Button mainSolveButton;
    @FXML private RadioMenuItem setSizeTo3;
    @FXML private RadioMenuItem setSizeTo4;
    @FXML private RadioMenuItem setSizeTo5;
    @FXML private RadioMenuItem setSizeTo6;

    private Color colorInserted = Color.BLACK;
    private Color colorImported = colorInserted;
    private Color colorGenerated = new Color(0.4, 0.4, 0.4, 1);
    private Color colorSolved = Color.GREEN;
    private Color colorConflicts = Color.RED;

    private CellEvent cellEvent = new CellEvent();

    private class CellEvent implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            Pane pane = (Pane) event.getSource();
            String p = pane.toString();
            int x = Integer.parseInt(p.substring(p.indexOf("$") + 1, p.indexOf(',')));
            int y = Integer.parseInt(p.substring(p.indexOf(',') + 1, p.indexOf(']')));

            if(controller.getCellIsGenerated(y,x)) {
                pane.setStyle("-fx-background-color: Red");
                mainGridPane.requestFocus();
                return;
            }

            int[] gridCoords = translateXYToGridCoords(x, y);
            GridPane subGrid = (GridPane) mainGridPane.lookup("#SubGrid$" + gridCoords[0] + "," + gridCoords[1]);

            TextField textField = new TextField();
            textField.setStyle("-fx-padding: 5px;");

            String text = controller.getFieldValue(y, x) + "";
            if (!Objects.equals(text, "0"))
                textField.setText(text);

            textField.setPrefHeight(Integer.MAX_VALUE);
            textField.setMinHeight(40);
            textField.setMinWidth(40);
            textField.setAlignment(CENTER);

            textField.setOnAction(ActionEvent -> mainGridPane.requestFocus());
            textField.setOnKeyPressed(KeyEvent -> {
                if (KeyEvent.getCode() == KeyCode.ESCAPE) {
                    textField.setText(text);
                    mainGridPane.requestFocus();
                }
            });
            textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    tryUpdate(textField.getText(), x, y);
                    subGrid.getChildren().remove(textField);
                }
            });
            // force the field to be numeric only
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });

            subGrid.add(textField, gridCoords[2], gridCoords[3]);
            GridPane.setHalignment(textField, HPos.CENTER);
            textField.requestFocus();
            textField.selectAll();
        }
    }

    @FXML
    protected void initialize() {
        mainGridPane.minWidthProperty().bind(mainGridPane.heightProperty());
        mainGridPane.prefWidthProperty().bind(mainGridPane.heightProperty());
        mainGridPane.maxWidthProperty().bind(mainGridPane.heightProperty());

        mainSolveButton.maxWidthProperty().bind(mainGridPane.widthProperty());

        drawGrid();
        MainApp.primaryStage.minWidthProperty().bind(mainGridPane.widthProperty().add(40));
        setSizeSelector();

        if(controller.getSystemGenerated())
            fillWithCurrentSudokuField(colorGenerated, true);
        else
            fillWithCurrentSudokuField(colorImported, true);
    }

    private void tryUpdate(String textinput, int x, int y) {
        int newvalue;
        int max = controller.getSize();
        Boolean delete = false;

        try {
            if (textinput == null || StringUtils.isBlank(textinput)) {
                newvalue = 0;
                delete = true;
            }
            else
                newvalue = Integer.parseInt(textinput);

            if (!delete && (newvalue <= 0 || newvalue > max)) throw new IOException();

            controller.setFieldValue(y, x, newvalue);
            Text text = (Text) mainGridPane.lookup("#Text$" + x + "," + y);
            text.setText((newvalue == 0 ? "" : newvalue) + "");
            text.setFill(colorInserted);

            if(!controller.getWasSolved() && controller.getIsSolved()) {
                DialogStage sucess = new DialogStage("Das Sudoku wurde erfolgreich gelöst!", "Glückwunsch!", INFO);
                sucess.showAndWait();
            }
        } catch (Exception e) {
            DialogStage error = new DialogStage(
                    "Die eingegebene Zahl ist ungültig!\nEs können nur Zahlen zwischen 1 und " + max + " eingegeben werden.",
                    "Fehler", ERROR);
            error.showAndWait();
        }
    }

    private void drawGrid() {
        currentSize = controller.getSubFieldsize();

        addRowColumnConstraints(mainGridPane);
        for (int xl = 0; xl < currentSize; xl++) {
            for (int yl = 0; yl < currentSize; yl++) {
                GridPane smallGridPane = new GridPane();

                smallGridPane.setHgap(2);
                smallGridPane.setVgap(2);
                smallGridPane.setId("SubGrid$" + xl + "," + yl);

                fontSize.bind(mainGridPane.heightProperty().divide(currentSize * currentSize + 10));
                mainGridPane.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString(), "; -fx-background-color: black"));

                for (int xs = 0; xs < currentSize; xs++) {
                    for (int ys = 0; ys < currentSize; ys++) {
                        int[] coords = translateGridCoordstoXY(xl, yl, xs, ys);

                        Pane pane = new Pane();
                        pane.setStyle("-fx-background-color: white");
                        pane.setId("Pane$" + coords[0] + "," + coords[1]);
                        pane.addEventHandler(MouseEvent.MOUSE_CLICKED, cellEvent);
                        pane.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> ((Pane) event.getSource()).setStyle("-fx-background-color: #dbdbdb"));
                        pane.addEventHandler(MouseEvent.MOUSE_EXITED, event -> ((Pane) event.getSource()).setStyle("-fx-background-color: white"));

                        Text text = new Text();
                        text.setMouseTransparent(true);
                        text.setTextAlignment(TextAlignment.CENTER);
                        text.setId("Text$" + coords[0] + "," + coords[1]);

                        smallGridPane.add(pane, xs, ys);
                        smallGridPane.add(text, xs, ys);

                        GridPane.setHalignment(text, HPos.CENTER);
                        GridPane.setValignment(text, VPos.CENTER);
                    }
                }

                addRowColumnConstraints(smallGridPane);

                mainGridPane.add(smallGridPane, xl, yl);
            }
        }

        int[] stageHeightBySize = new int[]{496, 564, 781, 980};
        MainApp.primaryStage.setMinHeight(stageHeightBySize[currentSize - 3]);
        MainApp.primaryStage.sizeToScene();

        int[] stageWidthBySize = new int[]{419, 486, 705, 900};
        MainApp.primaryStage.setWidth(stageWidthBySize[currentSize - 3]);
    }

    private void redrawGrid() {
        mainGridPane.getChildren().clear();
        mainGridPane.getColumnConstraints().clear();
        mainGridPane.getRowConstraints().clear();

        drawGrid();
    }

    private void addRowColumnConstraints(GridPane gridPane) {
        int size = controller.getSubFieldsize();
        for (int i = 0; i < size; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setHgrow(ALWAYS);
            col.setMinWidth(15);
            col.setPrefWidth(15);
            RowConstraints row = new RowConstraints();
            row.setVgrow(ALWAYS);
            row.setMinHeight(15);
            row.setPrefHeight(15);
            gridPane.getColumnConstraints().add(col);
            gridPane.getRowConstraints().add(row);
        }
    }

    private void setSizeSelector() {
        switch(currentSize) {
            case 3:
                setSizeTo3.setSelected(true);
                break;
            case 4:
                setSizeTo4.setSelected(true);
                break;
            case 5:
                setSizeTo5.setSelected(true);
                break;
            case 6:
                setSizeTo6.setSelected(true);
                break;
        }
    }

    private int[] translateXYToGridCoords(int x, int y) {
        int subFieldsize = controller.getSubFieldsize();
        return new int[]{x / subFieldsize, y / subFieldsize, x % subFieldsize, y % subFieldsize};
    }

    private int[] translateGridCoordstoXY(int xl, int yl, int xs, int ys) {
        int subFieldsize = controller.getSubFieldsize();
        int x = xl * subFieldsize + xs;
        int y = yl * subFieldsize + ys;
        return new int[]{x, y};
    }

    private void fillWithCurrentSudokuField(Color textcolor, Boolean all) {
        int size = controller.getSize();

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Pane pane = (Pane) mainGridPane.lookup("#Pane$" + x + "," + y);
                pane.setStyle("-fx-background-color: white");

                Text text = (Text) mainGridPane.lookup("#Text$" + x + "," + y);
                int number = controller.getFieldValue(y, x);
                if (!all && text.getText().equals(number + ""))
                    continue;

                if (number != 0)
                    text.setText(number + "");
                else
                    text.setText("");

                text.setFill(textcolor);
            }
        }
    }

    private void resetConflictCells() {
        int size  = controller.getSize();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                mainGridPane.lookup("#Pane$" + x + "," + y).setStyle("-fx-background-color: white");
                Text text = (Text) mainGridPane.lookup("#Text$" + x + "," + y);
                if(text.getFill() == colorConflicts) {
                    text.setFill(colorInserted);
                }
            }
        }
    }

    private void changeTextColor(int x, int y, Color color){
        Text text = (Text) mainGridPane.lookup("#Text$" + x + "," + y);
        Pane pane = (Pane) mainGridPane.lookup("#Pane$" + x + "," + y);
        if(text.getFill() == colorInserted)
            text.setFill(color);
        pane.setStyle("-fx-background-color: MistyRose");
    }

    /**
     * Dient dem Zurücksetzen eines Sudokus. Nach Bestätigung des Dialogs wird das aktuelle Sudokufeld geleert.
     * @param actionEvent Wird aufgerufen durch einen Klick auf "Sudoku zurücksetzen"
     */
    //onAction methods
    @FXML
    public void clearField(ActionEvent actionEvent) {
        DialogStage confirmDialog = new DialogStage("Sudoku zurücksetzen?\nNicht exportierte Änderungen gehen verloren.",
                "Hinweis", CONFIRM);
        boolean result = confirmDialog.showAndWaitGetResult();

        if (result) {
            controller.clear();
            fillWithCurrentSudokuField(Color.BLACK, true);
        }
    }

    /**
     * Ruft die Generate-Methode des Controllers auf und füllt das aktuelle Sudokufeld mit dem generierten Sudoku
     * @param actionEvent "Wird aufgerufen durch einen Klick auf "Sudoku zufällig generieren"
     */
    @FXML
    public void generate(ActionEvent actionEvent) {
        controller.generate();
        fillWithCurrentSudokuField(colorGenerated, true);
    }

    /**
     * Löst das aktuelle Sudoku-Feld über die Solver-Funktionalität des SudokuControllers.
     * Ist das Sudoku nicht lösbar oder das Feld leer, wird eine Fehlermeldung angezeigt.
     * @param event Wird aufgerufen durch einen Klick auf "Lösen"
     */
    @FXML
    public void solve(Event event) {
        if (controller.solve()) {
            fillWithCurrentSudokuField(colorSolved, false);
            resetConflictCells();
        } else {
            DialogStage unsolvable = new DialogStage("Das Sudoku ist nicht lösbar", "Fehler", ERROR);
            unsolvable.showAndWait();
        }
    }

    /**
     * Wird durch einen Klick auf "Eingaben überprüfen" aufgerufen. Prüft alle aktuellen Eingaben auf die Sudoku-Regeln und markiert
     * die Felder, die diese verletzen.
     * @param actionEvent Wird aufgerufen durch einen Klick auf "Eingaben überprüfen"
     */
    @FXML
    public void markConflictCells(ActionEvent actionEvent) {
        mainGridPane.requestFocus();
        resetConflictCells();
        List<int[]> conflictTuples = controller.getConflicts();
        for (int[] conflict : conflictTuples) {
            changeTextColor(conflict[1], conflict[0], colorConflicts);
        }
        if(conflictTuples.size() == 0) {
            DialogStage noErrors = new DialogStage("Es wurden keine Fehler gefunden.", "Hinweis", INFO);
            noErrors.showAndWait();
        }
    }

    /**
     * Wird aufgerufen über die Startleiste, Größe ändern und die anschließende Auswahl der gewünschten Größe.
     * Das aktuelle Feld wird gelöscht, danach wird die Größe des Feldes geändert und das neue Feld erzeugt.
     * @param actionEvent Wird aufgerufen durch einen Klick auf eine Größenauswahl über die Startleiste
     */
    @FXML
    public void changeSize(ActionEvent actionEvent) {
        mainGridPane.requestFocus();
        RadioMenuItem menuItem = (RadioMenuItem)actionEvent.getSource();
        String sourceID = menuItem.toString();
        int size = Integer.parseInt(sourceID.substring(sourceID.indexOf('=') + 10, sourceID.indexOf(',')));

        controller.reset(size * size);
        redrawGrid();

        menuItem.setSelected(true);
    }

    /**
     * Dient dem Import eines neuen Sudokus über eines der drei Dateiformate.
     * Es wird die Import-Methode des SudokuControllers aufgerufen.
     * @param actionEvent Wird ausgelöst bei Klick auf einen der Import-Steuerelemente
     * @return Gibt zurück ob der Import erfolgreich war
     */
    @FXML
    public boolean importFile(ActionEvent actionEvent) {
        if(super.importFile(actionEvent)){
            if(currentSize != controller.getSubFieldsize())
                redrawGrid();
            fillWithCurrentSudokuField(colorImported, true);
            setSizeSelector();
            return true;
        }
        return false;
    }

    /**
     * Dient dem Export des aktuellen Sudokus. Ruft den Dateiexplorer des Systems auf. Nachdem der Nutzer den Speiecherort und den Dateinamen
     * festgelegt hat wird die Export-Methode des SudokuControllers aufegrufen.
     * @param actionEvent Wird aufgerufen durch einen Klick auf ein Steuerelement unter Export, das dem gewünschten Dateiformat entspricht (außer PDF)
     */
    @FXML
    public void exportFile(ActionEvent actionEvent) {
        String sourceID = actionEvent.getSource().toString();
        String fileType = sourceID.substring(sourceID.indexOf('$') + 1, sourceID.indexOf(','));

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(fileType + "-Datei exportieren");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(fileType + "-Dateien",
                "*." + fileType.toLowerCase()));

        File file = fileChooser.showSaveDialog(MainApp.primaryStage);
        try {
            if(file != null)
                controller.exportFile(file);
        } catch (Exception e) {
            DialogStage error = new DialogStage("Fehler!", "Fehler", ERROR);
            error.showAndWait();
        }
    }

    /**
     * Der User kann den Speicherort der zu exportierenden PDF aussuchen. Danach wird die PDF-Export-Methode des SudokuControllers aufgerufen.
     * @param actionEvent Wird aufgerufen bei Auswahl des Steuerelementes Export, PDF-Datei.
     */
    @FXML
    public void exportPDF(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("PDF-Datei exportieren");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF-Dateien", "*.pdf"));

        File pdfFile = fileChooser.showSaveDialog(mainGridPane.getScene().getWindow());
        try {
            if(pdfFile != null)
                controller.exportPDF(pdfFile,mainGridPane.snapshot(new SnapshotParameters(),null));
        } catch (Exception e) {
            DialogStage error = new DialogStage("Fehler!", "Fehler", ERROR);
            error.showAndWait();
        }
    }
}
