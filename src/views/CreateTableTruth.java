package views;

import java.util.*;

import com.brunomnsilva.smartgraph.graph.Graph;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

public class CreateTableTruth {

    public class Data {
        private String t, f;

        public Data(String t, String f) {
            this.t = t;
            this.f = f;
        }

        public String getT() {
            return t;
        }

        public void setT(String t) {
            this.t = t;
        }

        public String getF() {
            return f;
        }

        public void setF(String f) {
            this.f = f;
        }
    }

    public static Map<String, List<Data>> listData = new HashMap<>();

    public void CreateTable(Graph<String, String> g, String v) {
//        if(listData != null)
//        System.out.println(listData.get(0).getT());
        TableView<Integer> table = new TableView<>();
        Scene scene = new Scene(new Group());
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Table truth of " + v);
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
            String s = Integer.toBinaryString(i);
            while (s.length() < countParent)
                s = "0" + s;
            for (int j = 0; j < countParent; j++) {
                if (s.charAt(j) == '0')
                    listItem.get(j).add("F");
                else
                    listItem.get(j).add("T");
            }

        }
//        System.out.println("size: " + listData.get(0).getF());
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
            col.setOnEditCommit(new EventHandler<CellEditEvent<Integer, String>>() {
                @Override
                public void handle(CellEditEvent<Integer, String> event) {
                    System.out.println(event.getNewValue());
                }
            });
            col.setCellFactory(TextFieldTableCell.forTableColumn());
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
        trueColumn.setOnEditCommit(new EventHandler<CellEditEvent<Integer, String>>() {
            @Override
            public void handle(CellEditEvent<Integer, String> event) {
                System.out.println("Column T: Row = " + event.getTablePosition().getRow() + "; Value = " + event.getNewValue());
                listData.get(v).get(event.getTablePosition().getRow()).setT(event.getNewValue());
            }
        });

        // false column
        TableColumn<Integer, String> falseColumn;
        falseColumn = new TableColumn<>("F");
        falseColumn.setMinWidth(50);

        falseColumn.setCellValueFactory(cellData -> {
            Integer rowIndex = cellData.getValue();
            return new ReadOnlyStringWrapper(listData.get(v).get(rowIndex).getF());
        });
        falseColumn.setOnEditCommit(new EventHandler<CellEditEvent<Integer, String>>() {
            @Override
            public void handle(CellEditEvent<Integer, String> event) {
                System.out.println("Column F: Row = " + event.getTablePosition().getRow() + "; Value = " + event.getNewValue());
                listData.get(v).get(event.getTablePosition().getRow()).setF(event.getNewValue());
            }
        });
        // add to table
        vertexTarget.getColumns().addAll(trueColumn, falseColumn);
        table.getColumns().add(vertexTarget);

//        TableColumn trueColumn = new TableColumn<>("T");
//        TableColumn falseColumn = new TableColumn<>("F");
//        trueColumn.setMinWidth(50);
//        falseColumn.setMinWidth(50);
//
//        trueColumn.setCellValueFactory(
//                new PropertyValueFactory<Data, Double>("T"));
//        trueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
////        table.setItems(FXCollections.observableArrayList(listData));
//        trueColumn.setCellValueFactory(new PropertyValueFactory<Data, String>("t"));
//
//        trueColumn.setOnEditCommit(
//                new EventHandler<CellEditEvent<Data, Double>>() {
//                    @Override
//                    public void handle(CellEditEvent<Data, Double> t) {
//                        System.out.println("Column T: Row = " + t.getTablePosition().getRow() + "; Value = " + t.getNewValue());
//                    }
//                }
//        );
//        falseColumn.setCellValueFactory(
//                new PropertyValueFactory<Data, Double>("F"));
//        falseColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//        falseColumn.setOnEditCommit(
//                new EventHandler<CellEditEvent<Data, Double>>() {
//                    @Override
//                    public void handle(CellEditEvent<Data, Double> t) {
//
//                        System.out.println("Column T: Row = " + t.getTablePosition().getRow() + "; Value = " + t.getNewValue());
//                    }
//                }
//        );
//
//
//        vertexTarget.getColumns().addAll(trueColumn, falseColumn);
//        table.getColumns().addAll(vertexTarget);

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
//         table.setItems(FXCollections.observableArrayList(s));
//         table.getItems().add(FXCollections.observableArrayList(s).getClass());
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(table);
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        stage.setScene(scene);
        stage.show();
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