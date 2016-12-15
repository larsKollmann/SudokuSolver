package de.fhaachen.swegrp2.controllers.GUIControllers;

import de.fhaachen.swegrp2.controllers.SudokuController;
import de.fhaachen.swegrp2.controllers.SudokuField;
import de.fhaachen.swegrp2.models.Import;
//import de.fhaachen.swegrp2.models.solver.SudokuGrid;
import de.fhaachen.swegrp2.models.solver.SudokuGrid;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;


import static java.lang.Math.sqrt;
import static javafx.scene.layout.Priority.*;

public class SudokuSceneController
{
    private SudokuController control = SudokuController.getInstance();

    private static class GuiPropertiesBySize{
        int subFieldSize;
        int mainSize;

        int fontSize;

        int stageMinHeight;
        int stageMinWidth;

        GuiPropertiesBySize(int subFieldSize, int fontSize, int stageMinHeight, int stageMinWidth) {
            this.subFieldSize = subFieldSize;
            this.mainSize = subFieldSize*subFieldSize;
            this.fontSize = fontSize;
            this.stageMinHeight = stageMinHeight;
            this.stageMinWidth = stageMinWidth;
        }
    }

    static GuiPropertiesBySize[] guiprop = new GuiPropertiesBySize[] {
            new GuiPropertiesBySize(3, 22, 495, 415),
            new GuiPropertiesBySize(4, 20, 615, 540),
            new GuiPropertiesBySize(5, 17, 765, 688),
            new GuiPropertiesBySize(6, 14, 910, 830)
    };

    @FXML private GridPane mainGridPane;

    @FXML
    protected void initialize() {
        mainGridPane.minWidthProperty().bind(mainGridPane.heightProperty());
        mainGridPane.prefWidthProperty().bind(mainGridPane.heightProperty());
        mainGridPane.maxWidthProperty().bind(mainGridPane.heightProperty());

        drawGrid();
    }

    private void drawGrid() {
        int size = control.getSubFieldsize();

        addrowcolumnconstraints(mainGridPane);
        // SudokuFeld gewünschter Größe in Scene einfügen
        for(int xl = 0; xl < size; xl++){
            for(int yl = 0; yl < size; yl ++){
                GridPane smallgridPane = new GridPane();

                smallgridPane.setHgap(2);
                smallgridPane.setVgap(2);


                for(int xs = 0; xs < size; xs++) {
                    for (int ys = 0; ys < size; ys++) {
                        int[] coords = translateGridCoordstoXY(xl,yl,xs,ys,size);

                        Pane pane = new Pane();
                        pane.setStyle("-fx-background-color: white");
                        pane.setId("Pane" + coords[0] + "," + coords[1]);

                        smallgridPane.setVgrow(pane, ALWAYS);
                        smallgridPane.setHgrow(pane, ALWAYS);

                        Text text = new Text();
                        text.setMouseTransparent(true);
//                        /*DEBUG*/text.setText("0");
                        text.setTextAlignment(TextAlignment.CENTER);
                        text.setFont(Font.font(null, FontWeight.BOLD, 14));
                        text.setMouseTransparent(true);
                        text.setId("Text" + coords[0] + "," + coords[1]);

                        smallgridPane.add(pane, xs, ys);
                        smallgridPane.add(text, xs, ys);

                        smallgridPane.setHalignment(text, HPos.CENTER);
                        smallgridPane.setValignment(text, VPos.CENTER);
                        smallgridPane.setHgrow(text, ALWAYS);
                        smallgridPane.setVgrow(text, ALWAYS);
                    }
                }

                addrowcolumnconstraints(smallgridPane);

                mainGridPane.add(smallgridPane, xl, yl);
            }
        }
    }

    private void redrawGrid() throws IOException {
        mainGridPane.getChildren().clear();
        mainGridPane.getColumnConstraints().clear();
        mainGridPane.getRowConstraints().clear();

        drawGrid();
//
//        Scene scene = mainGridPane.getScene();
//        Stage stage = (Stage) scene.getWindow();
//        stage.setMinHeight(152 * size);
//        stage.setMinWidth(139 * size);
    }

    private void addrowcolumnconstraints(GridPane gridPane) {
        int size = control.getSubFieldsize();
        for(int i = 0; i < size; i++) {
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
        int[] ret = new int[4];

        ret[0] = x/size;
        ret[1] = y/size;

        ret[2] = x % size;
        ret[3] = y % size;
        return ret;
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

    private void fillWithCurrentSudokuField(Color textcolor, String cssPanecolor) {
        int dim = control.getSize();

        for(int x = 0; x < dim; x++) {
            for(int y = 0; y < dim; y++) {
                Text text = (Text) mainGridPane.lookup("#Text" + x + "," + y);
                Pane pane = (Pane) mainGridPane.lookup("#Pane" + x + "," + y);
                int number = control.getFieldValue(y, x);
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


    @FXML
    public void changeSize(ActionEvent actionEvent) throws IOException {
        String sourceID = actionEvent.getSource().toString();
        int size = Integer.parseInt(sourceID.substring(sourceID.indexOf('=') + 10, sourceID.indexOf(',')));

        control.reset(size*size);
        redrawGrid();
    }

    @FXML
    public void solve(Event event) {
        control.solve();

        fillWithCurrentSudokuField();
    }

    @FXML
    public void importFile(ActionEvent actionEvent) {

        String sourceID = actionEvent.getSource().toString();
        String fileType = sourceID.substring(sourceID.indexOf('$') + 1, sourceID.indexOf(','));

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(fileType + "-Datei importieren");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(fileType + "-Dateien", "*." + fileType.toLowerCase()));

        File file = fileChooser.showOpenDialog((Stage)mainGridPane.getScene().getWindow());
        try {
            control.ImportFile(file);

            redrawGrid();
            fillWithCurrentSudokuField(Color.BLUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void exportFile(ActionEvent actionEvent) {
    }

    @FXML
    public void exportPDF(ActionEvent actionEvent) {
        WritableImage image = mainGridPane.snapshot(new SnapshotParameters(), null);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("-PDF exportieren");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF-Dateien", "*.pdf"));
        File pdfFile = fileChooser.showSaveDialog((Stage)mainGridPane.getScene().getWindow());
        File pngFile = new File("temp.png");


        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", pngFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PDDocument doc = null;
        doc = new PDDocument();
        PDPage page = new PDPage();
        doc.addPage(page);
        try{
            BufferedImage awtImage = ImageIO.read( new File( pngFile.getPath() ));
            PDImageXObject pdImageXObject = LosslessFactory.createFromImage(doc, awtImage);
            PDPageContentStream contentStream = new PDPageContentStream(doc, page, true, false);
            contentStream.drawImage(pdImageXObject, 50, 50, 500, 500);
            contentStream.close();
            doc.save( pdfFile.getPath() );
            doc.close();
        } catch (Exception io){
            System.out.println(" -- fail --" + io);
        }

    }

    @FXML
    /*DEBUG*/ public void printGuiValues(ActionEvent actionEvent) {
        Stage stage = (Stage)mainGridPane.getScene().getWindow();
        System.out.println(stage);
        System.out.println("W: " + stage.getWidth() + " H: " + stage.getHeight());
    }


    public void generate(ActionEvent actionEvent) {
        control.generate();

        fillWithCurrentSudokuField(Color.BROWN);
    }

    public void clearField(ActionEvent actionEvent) {
        control.clear();
        fillWithCurrentSudokuField();
    }

    private void changeFieldColor(int x, int y, Color color) {
        Text text = (Text) mainGridPane.lookup("#Text" + x + "," + y);
        Pane pane = (Pane) mainGridPane.lookup("#Pane" + x + "," + y);
        text.setFill(color);
        pane.setStyle("-fx-background-color: lightgrey");
    }

    public void markConflictFields(ActionEvent actionEvent) {
        List<int[]> conflictTuples = control.getConflicts();
        for (int [] conflict:
             conflictTuples) {
            changeFieldColor(conflict[1],conflict[0],Color.RED);
        }

    }

}
