package de.fhaachen.swegrp2.controllers.GUIControllers;

import de.fhaachen.swegrp2.MainApp;
import de.fhaachen.swegrp2.controllers.SudokuController;
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


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;


import static javafx.scene.layout.Priority.*;

public class SudokuSceneController {
    private SudokuController controller = SudokuController.getInstance();

    @FXML private GridPane mainGridPane;

    @FXML
    protected void initialize() {
        mainGridPane.minWidthProperty().bind(mainGridPane.heightProperty());
        mainGridPane.prefWidthProperty().bind(mainGridPane.heightProperty());
        mainGridPane.maxWidthProperty().bind(mainGridPane.heightProperty());

        drawGrid();
        fillWithCurrentSudokuField(Color.BLUE);
    }

    private void drawGrid() {
        int size = controller.getSubFieldsize();

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
        int size = controller.getSubFieldsize();
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
        int dim = controller.getSize();

        for(int x = 0; x < dim; x++) {
            for(int y = 0; y < dim; y++) {
                Text text = (Text) mainGridPane.lookup("#Text" + x + "," + y);
                Pane pane = (Pane) mainGridPane.lookup("#Pane" + x + "," + y);
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


    @FXML
    public void close(ActionEvent actionEvent) {
        MainApp.primaryStage.close();
    }

    @FXML
    public void changeSize(ActionEvent actionEvent) throws IOException {
        String sourceID = actionEvent.getSource().toString();
        int size = Integer.parseInt(sourceID.substring(sourceID.indexOf('=') + 10, sourceID.indexOf(',')));

        controller.reset(size*size);
        redrawGrid();
    }

    @FXML
    public void solve(Event event) {
        controller.solve();

        fillWithCurrentSudokuField();
    }

    @FXML
    public void importFile(ActionEvent actionEvent) {

        String sourceID = actionEvent.getSource().toString();
        String fileType = sourceID.substring(sourceID.indexOf('$') + 1, sourceID.indexOf(','));

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(fileType + "-Datei importieren");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(fileType + "-Dateien", "*." + fileType.toLowerCase()));

        File file = fileChooser.showOpenDialog(MainApp.primaryStage);
        try {
            controller.ImportFile(file);

            redrawGrid();
            fillWithCurrentSudokuField(Color.BLUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void exportFile(ActionEvent actionEvent) {
    }

    private void exportSnapshotAsPNG(String path) throws IOException{
        WritableImage image = mainGridPane.snapshot(new SnapshotParameters(), null);
        File pngFile = new File(path);
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", pngFile);
    }

    private void insertPNGintoPDF(BufferedImage png,PDDocument pdf) throws Exception {
        PDPage page = new PDPage();
        pdf.addPage(page);
        PDImageXObject pdImageXObject = LosslessFactory.createFromImage(pdf, png);
        PDPageContentStream contentStream = new PDPageContentStream(pdf, page, true, false);
        contentStream.drawImage(pdImageXObject, 50, 150, 500, 500);
        contentStream.close();
    }
    @FXML
    public void exportPDF(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("-PDF exportieren");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF-Dateien", "*.pdf"));

        File pdfFile = fileChooser.showSaveDialog((Stage)mainGridPane.getScene().getWindow());
        File pngFile = new File("temp.png");

        try {
            exportSnapshotAsPNG(pngFile.getPath());
            BufferedImage png = ImageIO.read(pngFile);
            PDDocument pdf = new PDDocument();
            insertPNGintoPDF(png,pdf);
            pdf.save( pdfFile.getPath() );
            pdf.close();
            pngFile.delete();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    /*DEBUG*/ public void printGuiValues(ActionEvent actionEvent) {
        Stage stage = (Stage)mainGridPane.getScene().getWindow();
        System.out.println(stage);
        System.out.println("W: " + stage.getWidth() + " H: " + stage.getHeight());
    }

    @FXML
    public void generate(ActionEvent actionEvent) {
        controller.generate();

        fillWithCurrentSudokuField(Color.BROWN);
    }

    public void clearField(ActionEvent actionEvent) {
        controller.clear();
        fillWithCurrentSudokuField();
    }

    private void changeFieldColor(int x, int y, Color color) {
        Text text = (Text) mainGridPane.lookup("#Text" + x + "," + y);
        Pane pane = (Pane) mainGridPane.lookup("#Pane" + x + "," + y);
        text.setFill(color);
        pane.setStyle("-fx-background-color: lightgrey");
    }

    public void markConflictFields(ActionEvent actionEvent) {
        List<int[]> conflictTuples = controller.getConflicts();
        for (int [] conflict:
             conflictTuples) {
            changeFieldColor(conflict[1],conflict[0],Color.RED);
        }

    }

}
