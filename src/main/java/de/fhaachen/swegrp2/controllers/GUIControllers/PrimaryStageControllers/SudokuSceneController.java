package de.fhaachen.swegrp2.controllers.GUIControllers.PrimaryStageControllers;

import de.fhaachen.swegrp2.MainApp;
import de.fhaachen.swegrp2.controllers.GUIControllers.DialogStage;
import de.fhaachen.swegrp2.controllers.SudokuController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
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
import javafx.stage.Stage;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static javafx.geometry.Pos.CENTER;
import static javafx.scene.layout.Priority.ALWAYS;
import static javafx.scene.layout.Priority.NEVER;


public class SudokuSceneController extends PrimaryStageSharedController {
    private SudokuController controller = SudokuController.getInstance();
    private CellEvent cellEvent = new CellEvent();


    private Color imported = Color.BLACK;
    private Color inserted = Color.BLACK;
    private Color generated = Color.BLUE;
    private Color solved = Color.BLACK;
    private Color conflicts = Color.RED;

    private class CellEvent implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            String p = event.getSource().toString();
            int x = Integer.parseInt(p.substring(p.indexOf("$") + 1, p.indexOf(',')));
            int y = Integer.parseInt(p.substring(p.indexOf(',') + 1, p.indexOf(']')));

            int[] gridCoords = translateXYToGridCoords(x, y, controller.getSubFieldsize());
            GridPane subGrid = (GridPane) mainGridPane.lookup("#SubGrid$" + gridCoords[0] + "," + gridCoords[1]);

            TextField textField = new TextField();

            String text = controller.getFieldValue(y, x) + "";
            if (!Objects.equals(text, "0"))
                textField.setText(text);

            textField.setPrefHeight(1000);
            textField.setMinHeight(40);
            textField.setMinWidth(40);
            textField.autosize();
            textField.setAlignment(CENTER);

            textField.setOnAction(event1 -> {
                mainGridPane.requestFocus();
            });
            textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    tryUpdate(textField.getText(), x, y);
                    subGrid.getChildren().remove(textField);
                }
            });
            // force the field to be numeric only
            textField.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (!newValue.matches("\\d*")) {
                        textField.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                }
            });

            subGrid.add(textField, gridCoords[2], gridCoords[3]);
            GridPane.setHalignment(textField, HPos.CENTER);
            textField.requestFocus();
            textField.selectAll();
        }
    }

    @FXML
    private GridPane mainGridPane;
    @FXML
    private Button mainSolveButton;

    @FXML
    protected void initialize() {
        mainGridPane.minWidthProperty().bind(mainGridPane.heightProperty());
        mainGridPane.prefWidthProperty().bind(mainGridPane.heightProperty());
        mainGridPane.maxWidthProperty().bind(mainGridPane.heightProperty());

        mainSolveButton.maxWidthProperty().bind(mainGridPane.widthProperty());

        drawGrid();
        fillWithCurrentSudokuField(generated);
    }

    private void tryUpdate(String textinput, int x, int y) {
        int newvalue = 0;
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
            changeTextColor(x,y,inserted);
        } catch (Exception e) {
            DialogStage error = new DialogStage(
                    "Die eingegebene Zahl ist ungültig!\nEs können nur Zahlen zwischen 1 und " + max + " eingegeben werden.",
                    "Fehler", false, MainApp.primaryStage);
            error.showAndWait();
        }
    }

    private void drawGrid() {
        int size = controller.getSubFieldsize();

        addrowcolumnconstraints(mainGridPane);
        // SudokuFeld gewünschter Größe in Scene einfügen
        for (int xl = 0; xl < size; xl++) {
            for (int yl = 0; yl < size; yl++) {
                GridPane smallgridPane = new GridPane();

                smallgridPane.setHgap(2);
                smallgridPane.setVgap(2);
                smallgridPane.setId("SubGrid$" + xl + "," + yl);

                for (int xs = 0; xs < size; xs++) {
                    for (int ys = 0; ys < size; ys++) {
                        int[] coords = translateGridCoordstoXY(xl, yl, xs, ys, size);

                        Pane pane = new Pane();
                        pane.setStyle("-fx-background-color: white");
                        pane.setId("Pane$" + coords[0] + "," + coords[1]);
                        pane.addEventHandler(MouseEvent.MOUSE_CLICKED, cellEvent);
                        pane.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> ((Pane) event.getSource()).setStyle("-fx-background-color: #dbdbdb"));
                        pane.addEventHandler(MouseEvent.MOUSE_EXITED, event -> ((Pane) event.getSource()).setStyle("-fx-background-color: white"));

                        GridPane.setVgrow(pane, NEVER);
                        GridPane.setHgrow(pane, NEVER);

                        Text text = new Text();
                        text.setMouseTransparent(true);
                        text.setTextAlignment(TextAlignment.CENTER);
                        text.setFont(Font.font(null, FontWeight.BOLD, 14));
                        text.setId("Text$" + coords[0] + "," + coords[1]);

                        smallgridPane.add(pane, xs, ys);
                        smallgridPane.add(text, xs, ys);

                        GridPane.setHalignment(text, HPos.CENTER);
                        GridPane.setValignment(text, VPos.CENTER);
                        GridPane.setHgrow(text, ALWAYS);
                        GridPane.setVgrow(text, ALWAYS);
                    }
                }

                addrowcolumnconstraints(smallgridPane);

                mainGridPane.add(smallgridPane, xl, yl);
            }
        }

        switch(size) {
            case 3:
                MainApp.primaryStage.setMinWidth(418);
                MainApp.primaryStage.setMinHeight(496);
                break;
            case 4:
                MainApp.primaryStage.setMinWidth(485);
                MainApp.primaryStage.setMinHeight(564);
                break;
            case 5:
                MainApp.primaryStage.setMinWidth(704);
                MainApp.primaryStage.setMinHeight(781);
                break;
            case 6:
                MainApp.primaryStage.setMinWidth(900);
                MainApp.primaryStage.setMinHeight(980);
                break;
        }
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
            col.setPrefWidth(10);
            RowConstraints row = new RowConstraints();
            row.setVgrow(ALWAYS);
            row.setPrefHeight(10);
            gridPane.getColumnConstraints().add(col);
            gridPane.getRowConstraints().add(row);
        }
    }

    private static int[] translateXYToGridCoords(int x, int y, int size) {
        return new int[]{x / size, y / size, x % size, y % size};
    }

    private static int[] translateGridCoordstoXY(int xl, int yl, int xs, int ys, int size) {
        int x = xl * size + xs;
        int y = yl * size + ys;
        return new int[]{x, y};
    }

    private void fillWithCurrentSudokuField() {
        fillWithCurrentSudokuField(Color.BLACK, "white");
    }

    private void fillWithCurrentSudokuField(Color color) {
        fillWithCurrentSudokuField(color, "white");
    }

    //TODO: Felderfarben inkonsistent
    private void fillWithCurrentSudokuField(Color textcolor, String cssPanecolor) {
        int dim = controller.getSize();

        for (int x = 0; x < dim; x++) {
            for (int y = 0; y < dim; y++) {
                Text text = (Text) mainGridPane.lookup("#Text$" + x + "," + y);
                Pane pane = (Pane) mainGridPane.lookup("#Pane$" + x + "," + y);
                int number = controller.getFieldValue(y, x);
                if (!text.getText().equals(number + "")) {
                    text.setFill(textcolor);
                    pane.setStyle("-fx-background-color: " + cssPanecolor);
                    if (number != 0)
                        text.setText(number + "");
                    else
                        text.setText("");
                }
            }
        }
    }

    private void changeFieldColor(int x, int y, Color color) {
        Text text = (Text) mainGridPane.lookup("#Text$" + x + "," + y);
        Pane pane = (Pane) mainGridPane.lookup("#Pane$" + x + "," + y);
        text.setFill(color);
        pane.setStyle("-fx-background-color: lightgrey");
    }

    private void changeTextColor(int x, int y, Color color){
        Text text = (Text) mainGridPane.lookup("#Text$" + x + "," + y);
        Pane pane = (Pane) mainGridPane.lookup("#Pane$" + x + "," + y);
        text.setFill(color);
    }

    //onAction methods
    public void clearField(ActionEvent actionEvent) {
        DialogStage test = new DialogStage("Sudoku zurücksetzen?\nNicht exportierte Änderungen gehen verloren.", "Hinweis", true, MainApp.primaryStage);
        boolean result = test.showAndWaitGetResult();

        if (result) {
            controller.clear();
            fillWithCurrentSudokuField();
        }
    }

    @FXML
    public void generate(ActionEvent actionEvent) {
        controller.clear();
        fillWithCurrentSudokuField();
        controller.generate();
        fillWithCurrentSudokuField(generated);
    }

    @FXML
    public void solve(Event event) {
        boolean solved = controller.solve();
        fillWithCurrentSudokuField(this.solved);
        if(!solved) {
            DialogStage test = new DialogStage("Das Sudoku ist nicht lösbar", "Fehler", false, MainApp.primaryStage );
            test.showAndWait();
        }
    }

    @FXML
    public void markConflictFields(ActionEvent actionEvent) {
        List<int[]> conflictTuples = controller.getConflicts();
        for (int[] conflict : conflictTuples) {
            changeTextColor(conflict[1], conflict[0], conflicts);
        }
    }

    @FXML
    public void changeSize(ActionEvent actionEvent) throws IOException {
        String sourceID = actionEvent.getSource().toString();
        int size = Integer.parseInt(sourceID.substring(sourceID.indexOf('=') + 10, sourceID.indexOf(',')));
        controller.reset(size * size);
        redrawGrid();
    }

    @FXML
    public boolean importFile(ActionEvent actionEvent) {
        if(super.importFile(actionEvent)){
            redrawGrid();
            fillWithCurrentSudokuField(imported);
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
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(fileType + "-Dateien", "*." + fileType.toLowerCase()));

        File file = fileChooser.showSaveDialog(MainApp.primaryStage);
        try {
            controller.exportFile(file);
        } catch (Exception e) {
            DialogStage test = new DialogStage("Fehler!", "Fehler", false, MainApp.primaryStage );
            test.showAndWait();
        }
    }

    @FXML
    public void exportPDF(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("-PDF exportieren");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF-Dateien", "*.pdf"));

        File pdfFile = fileChooser.showSaveDialog(mainGridPane.getScene().getWindow());
        try {
            controller.exportPDF(pdfFile,mainGridPane.snapshot(new SnapshotParameters(),null));
        } catch (Exception e) {
            DialogStage test = new DialogStage("Fehler!", "Fehler", false, MainApp.primaryStage );
            test.showAndWait();
        }
    }


}
