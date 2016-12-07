package de.fhaachen.swegrp2.controllers.GUIControllers;

import de.fhaachen.swegrp2.controllers.SudokuField;
import de.fhaachen.swegrp2.models.Import;
import de.fhaachen.swegrp2.models.solver.SudokuGrid;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static javafx.scene.layout.Priority.*;

public class SudokuSceneController
{
    private static final Logger log = LoggerFactory.getLogger(SudokuSceneController.class);

    // Größe des zu zeichnenden Sudokus
    private static int size = 3; // SudokuController.getInstance().getSudokuField().getSize();
    private static SudokuField field = new SudokuField(size*size);

    @FXML private GridPane mainGridPane;

    @FXML
    protected void initialize() {
        addrowcolumnconstraints(mainGridPane);
        mainGridPane.minWidthProperty().bind(mainGridPane.heightProperty());
        mainGridPane.prefWidthProperty().bind(mainGridPane.heightProperty());
        mainGridPane.maxWidthProperty().bind(mainGridPane.heightProperty());
        // SudokuFeld gewünschter Größe in Scene einfügen
        for(int xl = 0; xl < size; xl++){
            for(int yl = 0; yl < size; yl ++){
                GridPane smallgridPane = new GridPane();

                smallgridPane.setHgap(2);
                smallgridPane.setVgap(2);


                for(int xs = 0; xs < size; xs++) {
                    for (int ys = 0; ys < size; ys++) {
                        Pane pane = new Pane();
                        pane.setStyle("-fx-background-color: white");
                        smallgridPane.setVgrow(pane, ALWAYS);
                        smallgridPane.setHgrow(pane, ALWAYS);

                        Text text = new Text();
                        text.setMouseTransparent(true);
//                        /*DEBUG*/text.setText("0");
                        text.setTextAlignment(TextAlignment.CENTER);
                        text.setFont(new Font(14));
                        text.setMouseTransparent(true);
                        int[] coords = translateGridCoordstoXY(xl,yl,xs,ys);
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

    @FXML
    public void myimport(Event event) {
        Import importer = new Import();

        try {
            field = importer.importCSV("src/test/resources/size36sudoku.csv");
            fillWithCurrentSudokuField(field);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void solve(Event event) {
        SudokuGrid grid = SudokuGrid.getGrid(field.getSudokuField(), size);
        if(grid.solve()) {
            try {
                field = new SudokuField(grid.getGridAsIntArr());
                fillWithCurrentSudokuField(field);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addrowcolumnconstraints(GridPane gridPane) {
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

    private static int[] translateXYToGridCoords(int x, int y) {
        int[] ret = new int[4];

        ret[0] = x/size;
        ret[1] = y/size;

        ret[2] = x % size;
        ret[3] = y % size;

        return ret;
    }

    private static int[] translateGridCoordstoXY(int xl, int yl, int xs, int ys) {
        int x = xl * size + xs;
        int y = yl * size + ys;
        return new int[]{x, y};
    }


    public void fillWithCurrentSudokuField(SudokuField field) {
        int dim = size * size;

        for(int x = 0; x < dim; x++) {
            for(int y = 0; y < dim; y++) {
                Text text = (Text) mainGridPane.lookup("#Text" + x + "," + y);
                int number = field.getFieldValue(y,x);
                if(number != 0)
                    text.setText(number +"");
                else
                    text.setText("");
            }
        }
    }
}
