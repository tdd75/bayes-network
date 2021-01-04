package views;

import java.util.ArrayList;
import java.util.List;

import com.brunomnsilva.smartgraph.graph.Graph;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class CreateTableTruth {    

    public void CreateTable(Graph<String, String> g, String v) {
        TableView<List<String>> table = new TableView<>();
        Scene scene = new Scene(new Group());
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Table truth of " + v);
        table.setEditable(true);

        List<String> listVertex = findParentVertex(g, v);
        for (String vertex : listVertex) {
            TableColumn col = new TableColumn<>(vertex);            
            table.getColumns().add(col);
        }

        int countRows = (int) Math.pow(2, listVertex.size());
        int countParent = listVertex.size();
        List<List<String>> listItem = new ArrayList<>();
        for (int i = 0; i < countRows; i++) {
            String s = Integer.toBinaryString(i);
            while (s.length() < countParent)
                s = "0" + s;
            List<String> item = new ArrayList<>();
            for (int j = 0; j < countParent; j++) {
                if (s.charAt(j) == '0')
                    item.add("F");
                else
                    item.add("T");
            }
            listItem.add(item);
             System.out.println(item);
            // table.getItems().add(i, item);
            // table.setItems(FXCollections.observableArrayList(item));
            // System.out.println(listItem);
            // Object[] item = listItem.toArray();
            // table.setItems(item);
        }
        table.setItems(FXCollections.observableArrayList(listItem));

        TableColumn vertexTarget = new TableColumn<>(v);
        TableColumn trueColumn = new TableColumn<>("T");

//         trueColumn.setCellValueFactory(celldata -> celldata.getClass());
        // trueColumn.setOnEditCommit(new EventHandler<Event>(){
            // @Override
            // public void handle(CellEditEvent t) {
            //     ((book))
            // }
        // });
        TableColumn falseColumn = new TableColumn<>("F");
        vertexTarget.getColumns().addAll(trueColumn, falseColumn);
        table.getColumns().addAll(vertexTarget);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(table);
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        stage.setScene(scene);
        stage.show();
    }

    public void createAllTable(Graph<String, String> g) {
        System.out.println("Create all table");
        TableView table = new TableView<>();
        Scene scene = new Scene(new Group());
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Table truth ");
        table.setEditable(true);
        // TableColumn col1 = new TableColumn<String>("Rain");
        // TableColumn col2 = new TableColumn<String>("grass");
        // table.getColumns().addAll(col1, col2);
//        String s[] = {"1", "2"};
//        System.out.println(FXCollections.observableArrayList(s).getClass());
//        ObservableList<String> data = FXCollections.observableArrayList(
//            "1","2"
//        );
//        table.setItems(data);
        // table.setItems(FXCollections.observableArrayList(s));
        // table.getItems().add(FXCollections.observableArrayList(s).getClass());
//        final VBox vbox = new VBox();
//        vbox.setSpacing(5);
//        vbox.setPadding(new Insets(10));
//        vbox.getChildren().addAll(table);
//        ((Group) scene.getRoot()).getChildren().addAll(vbox);
//        stage.setScene(scene);
//        stage.show();
    }

    public List<String> findParentVertex(Graph<String, String> g, String v) {
        List<String> result = new ArrayList<>();
        System.out.println(g.edges().size());
        g.edges().forEach((e) -> {
            String[] inout = e.element().split("-");
            System.out.println(inout[0] + " " + inout[1]);
            if (inout[1].equals(v))
                result.add(inout[0]);
        });
        return result;
    }
}