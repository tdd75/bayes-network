package views;

import com.brunomnsilva.smartgraph.graph.Graph;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateTableTruth {

    public static Map<String, List<Data>> listData = new HashMap<>();

    public void createTable(Graph<String, String> g, String v) {
        TableView<Integer> table = new TableView<>();
        Scene scene = new Scene(new Group());
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle(v);
        stage.setResizable(false);
        table.setEditable(true);
        List<String> listVertex = findParentVertex(g, v);

        //
        int countRows = (int) Math.pow(2, listVertex.size());
        int countParent = listVertex.size();
        // init rows
        for (int i = 0; i < countRows; i++) {
            table.getItems().add(i);
        }
        // init list
        List<List<String>> listItem = new ArrayList<>();
        for (int j = 0; j < countParent; j++) {
            List<String> tmp = new ArrayList<>();
            listItem.add(tmp);
        }
        // init value list
        for (int i = 0; i < countRows; i++) {
            // listItem
            StringBuilder s = new StringBuilder(Integer.toBinaryString(i));
            while (s.length() < countParent)
                s.insert(0, "0");
            for (int j = 0; j < countParent; j++) {
                if (s.charAt(j) == '0')
                    listItem.get(j).add("F");
                else
                    listItem.get(j).add("T");
            }

        }
        if(listData.get(v) == null)
        {
            List<Data> tmp = new ArrayList<>();
            for (int i = 0; i < countRows; i++) {
                Data data = new Data("", "");
                tmp.add(data);
            }
            listData.put(v, tmp);
        }

        for (int k = 0; k < listVertex.size(); ++k) {
            TableColumn<Integer, String> col = new TableColumn<>(listVertex.get(k));
            int finalK = k;
            col.setCellValueFactory(cellData -> {
                Integer rowIndex = cellData.getValue();
                return new ReadOnlyStringWrapper(listItem.get(finalK).get(rowIndex));
            });
            table.getColumns().add(col);
        }

        // data
        TableColumn vertexTarget = new TableColumn<>(v);
        // true column
        TableColumn<Integer, String> trueColumn;
        trueColumn = new TableColumn<>("T");
        trueColumn.setMinWidth(50);
        trueColumn.setCellValueFactory(cellData -> {
            Integer rowIndex = cellData.getValue();
            return new ReadOnlyStringWrapper(listData.get(v).get(rowIndex).getT());
        });
        trueColumn.setOnEditCommit(event -> listData.get(v).get(event.getTablePosition().getRow()).setT(event.getNewValue()));
        trueColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // false column
        TableColumn<Integer, String> falseColumn;
        falseColumn = new TableColumn<>("F");
        falseColumn.setMinWidth(50);

        falseColumn.setCellValueFactory(cellData -> {
            Integer rowIndex = cellData.getValue();
            return new ReadOnlyStringWrapper(listData.get(v).get(rowIndex).getF());
        });
        falseColumn.setOnEditCommit(event -> listData.get(v).get(event.getTablePosition().getRow()).setF(event.getNewValue()));
        falseColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        // add to table
        vertexTarget.getColumns().addAll(trueColumn, falseColumn);
        table.getColumns().add(vertexTarget);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(table);
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        stage.setScene(scene);
        stage.show();
    }

    public void createAllTable(Graph<String, String> g) {
        g.vertices().forEach((e) -> createTable(g, e.element()));
    }

    public List<String> findParentVertex(Graph<String, String> g, String v) {
        List<String> result = new ArrayList<>();
        g.edges().forEach((e) -> {
            String[] inout = e.element().split("-");
            if (inout[1].equals(v))
                result.add(inout[0]);
        });
        return result;
    }
}