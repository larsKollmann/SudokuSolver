package de.fhaachen.swegrp2.controllers.GUIControllers.PrimaryStageControllers;

import de.fhaachen.swegrp2.MainApp;
import de.fhaachen.swegrp2.controllers.GUIControllers.DialogStage;
import de.fhaachen.swegrp2.controllers.SudokuController;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static javafx.geometry.Pos.CENTER;
import static javafx.scene.layout.Priority.ALWAYS;

public class SudokuSceneController extends PrimaryStageSharedController {
    private SudokuController controller = SudokuController.getInstance();

    @FXML private GridPane mainGridPane;
    @FXML private Button mainSolveButton;
    @FXML private RadioMenuItem setSizeTo3;
    @FXML private RadioMenuItem setSizeTo4;
    @FXML private RadioMenuItem setSizeTo5;
    @FXML private RadioMenuItem setSizeTo6;

    private Color colorImported = Color.BLACK;
    private Color colorInserted = Color.BLACK;
    private Color colorGenerated = Color.BLUE;
    private Color colorSolved = Color.BLACK;
    private Color colorConflicts = Color.RED;

    private CellEvent cellEvent = new CellEvent();

    private class CellEvent implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            String p = event.getSource().toString();
            int x = Integer.parseInt(p.substring(p.indexOf("$") + 1, p.indexOf(',')));
            int y = Integer.parseInt(p.substring(p.indexOf(',') + 1, p.indexOf(']')));

            int[] gridCoords = translateXYToGridCoords(x, y);
            GridPane subGrid = (GridPane) mainGridPane.lookup("#SubGrid$" + gridCoords[0] + "," + gridCoords[1]);

            TextField textField = new TextField();

            String text = controller.getFieldValue(y, x) + "";
            if (!Objects.equals(text, "0"))
                textField.setText(text);

            textField.setPrefHeight(Integer.MAX_VALUE);
            textField.setMinHeight(40);
            textField.setMinWidth(40);
            textField.setAlignment(CENTER);

            textField.setOnAction(event1 -> mainGridPane.requestFocus());
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

        switch(controller.getSubFieldsize()) {
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

        fillWithCurrentSudokuField(colorGenerated, true);
    }

    private void tryUpdate(String textinput, int x, int y) {
        int newvalue;
        int max = controller.getSize();

        try {
            if (textinput == null || StringUtils.isBlank(textinput))
                newvalue = 0;
            else
                newvalue = Integer.parseInt(textinput);

            if (newvalue < 0 || newvalue > max) throw new IOException();

            controller.setFieldValue(y, x, newvalue);
            Text text = (Text) mainGridPane.lookup("#Text$" + x + "," + y);
            text.setText((newvalue == 0 ? "" : newvalue) + "");
            text.setFill(colorInserted);
        } catch (Exception e) {
            DialogStage error = new DialogStage(
                    "Die eingegebene Zahl ist ungültig!\nEs können nur Zahlen zwischen 1 und " + max + " eingegeben werden.",
                    "Fehler", false);
            error.showAndWait();
        }
    }

    private void drawGrid() {
        int size = controller.getSubFieldsize();

        addrowcolumnconstraints(mainGridPane);
        for (int xl = 0; xl < size; xl++) {
            for (int yl = 0; yl < size; yl++) {
                GridPane smallgridPane = new GridPane();

                smallgridPane.setHgap(2);
                smallgridPane.setVgap(2);
                smallgridPane.setId("SubGrid$" + xl + "," + yl);

                for (int xs = 0; xs < size; xs++) {
                    for (int ys = 0; ys < size; ys++) {
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
                        text.setFont(Font.font(null, FontWeight.BOLD, 14));
                        text.setId("Text$" + coords[0] + "," + coords[1]);

                        smallgridPane.add(pane, xs, ys);
                        smallgridPane.add(text, xs, ys);

                        GridPane.setHalignment(text, HPos.CENTER);
                        GridPane.setValignment(text, VPos.CENTER);
                    }
                }

                addrowcolumnconstraints(smallgridPane);

                mainGridPane.add(smallgridPane, xl, yl);
            }
        }

        int[] stageHeightBySize = new int[]{496, 564, 781, 980};
        MainApp.primaryStage.setMinHeight(stageHeightBySize[size - 3]);
        MainApp.primaryStage.sizeToScene();
    }

    private void redrawGrid() {
        mainGridPane.getChildren().clear();
        mainGridPane.getColumnConstraints().clear();
        mainGridPane.getRowConstraints().clear();

        drawGrid();
    }

    private void addrowcolumnconstraints(GridPane gridPane) {
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
                Text text = (Text) mainGridPane.lookup("#Text$" + x + "," + y);
                int number = controller.getFieldValue(y, x);
                if (!all && text.getText().equals(number + ""))
                    continue;

                if (number != 0)
                    text.setText(number + "");
                else
                    text.setText("");

                text.setFill(textcolor);
                Pane pane = (Pane) mainGridPane.lookup("#Pane$" + x + "," + y);
                pane.setStyle("-fx-background-color: white");
            }
        }
    }

    private void resetConflictCells() {
        int size  = controller.getSize();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
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

    //onAction methods
    @FXML
    public void clearField(ActionEvent actionEvent) {
        DialogStage confirmDialog = new DialogStage("Sudoku zurücksetzen?\nNicht exportierte Änderungen gehen verloren.",
                "Hinweis", true);
        boolean result = confirmDialog.showAndWaitGetResult();

        if (result) {
            controller.clear();
            fillWithCurrentSudokuField(Color.BLACK, true);
        }
    }

    @FXML
    public void generate(ActionEvent actionEvent) {
        controller.generate();
        fillWithCurrentSudokuField(colorGenerated, true);
    }

    @FXML
    public void solve(Event event) {
        if (controller.solve()) {
            fillWithCurrentSudokuField(colorSolved, false);
            resetConflictCells();
        } else {
            DialogStage test = new DialogStage("Das Sudoku ist nicht lösbar", "Fehler", false);
            test.showAndWait();
        }
    }

    @FXML
    public void markConflictCells(ActionEvent actionEvent) {
        resetConflictCells();
        List<int[]> conflictTuples = controller.getConflicts();
        for (int[] conflict : conflictTuples) {
            changeTextColor(conflict[1], conflict[0], colorConflicts);
        }
    }

    @FXML
    public void changeSize(ActionEvent actionEvent) throws IOException {
        RadioMenuItem menuItem = (RadioMenuItem)actionEvent.getSource();
        String sourceID = menuItem.toString();
        int size = Integer.parseInt(sourceID.substring(sourceID.indexOf('=') + 10, sourceID.indexOf(',')));

        controller.reset(size * size);
        redrawGrid();

        menuItem.setSelected(true);
    }

    @FXML
    public boolean importFile(ActionEvent actionEvent) {
        if(super.importFile(actionEvent)){
            redrawGrid();
            fillWithCurrentSudokuField(colorImported, true);
            return true;
        }
        return false;
    }

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
            controller.exportFile(file);
        } catch (Exception e) {
            DialogStage error = new DialogStage("Fehler!", "Fehler", false);
            error.showAndWait();
        }
    }

    @FXML
    public void exportPDF(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("PDF-Datei exportieren");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF-Dateien", "*.pdf"));

        File pdfFile = fileChooser.showSaveDialog(mainGridPane.getScene().getWindow());
        try {
            controller.exportPDF(pdfFile,mainGridPane.snapshot(new SnapshotParameters(),null));
        } catch (Exception e) {
            DialogStage error = new DialogStage("Fehler!", "Fehler", false);
            error.showAndWait();
        }
    }
}
