package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.brunomnsilva.smartgraph.containers.SmartGraphDemoContainer;
import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.Vertex;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import views.CreateTableTruth;
import model.GraphModel;

public class MainController implements Initializable {
    @FXML
    private Pane drawPane;

    @FXML
    private void addNewNode() {
        TextInputDialog dialog = new TextInputDialog("" + (g.vertices().size() + 1));
        dialog.setTitle("Add new node");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter name node:");
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            this.nameNode = result.get();
            if (this.nameNode != "") {
                try {
                    g.insertVertex(this.nameNode);
                    graphView.update();
                } catch (Exception e) {
                    addFail();
                }
            }
        }
    }

    @FXML
    private void addNewEdge() {
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Login Dialog");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        List<String> listNode = new ArrayList<>();
        g.vertices().forEach((e) -> {
            listNode.add(e.element());
        });
        ComboBox outbound = new ComboBox<>(FXCollections.observableArrayList(listNode));
        ComboBox inbound = new ComboBox<>(FXCollections.observableArrayList(listNode));

        grid.add(new Label("Choose outbound vertex:"), 0, 0);
        grid.add(outbound, 1, 0);
        grid.add(new Label("Choose inbount vertex:"), 0, 1);
        grid.add(inbound, 1, 1);
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK)
                if (outbound.getValue() != null && inbound.getValue() != null
                        && inbound.getValue() != outbound.getValue()) {
                    return new Pair<>(outbound.getValue().toString(), inbound.getValue().toString());
                } else {
                    addFail();
                }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        if (result.isPresent()) {
            System.out.println(
                    "Outbound vertex: " + result.get().getKey() + ", Inbound vertex: " + result.get().getValue());
            try {
                g.insertEdge(result.get().getKey(), result.get().getValue(),
                        result.get().getKey() + "-" + result.get().getValue());
                graphView.update();
            } catch (Exception e) {
                addFail();
            }
        }
    }

    @FXML
    private void start() {
        System.out.println("start compute");
        tableTruth.createAllTable(g);
    }

    @FXML
    private void probability() {
        List<String> listNode = new ArrayList<>();
        g.vertices().forEach((e) -> {
            listNode.add(e.element());
        });
        ChoiceDialog<String> dialog = new ChoiceDialog<>("", listNode);
        dialog.setHeaderText(null);
        dialog.setContentText("Choose vertex: ");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(v -> {
            if (v != "")
                tableTruth.CreateTable(g, v);
        });
    }

    private GraphModel graphModel = new GraphModel();
    private String nameNode;
    Graph<String, String> g = build_sample_digraph();
    // Graph<String, String> g = new DigraphEdgeList<>();
    SmartPlacementStrategy strategy = new SmartCircularSortedPlacementStrategy();
    SmartGraphPanel<String, String> graphView = new SmartGraphPanel<>(g, strategy);
    CreateTableTruth tableTruth = new CreateTableTruth();

    private void addFail() {
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setHeaderText(null);
        errorAlert.setContentText("Add fail");
        errorAlert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        g.vertices().forEach((e) -> {
            System.out.println(e.element());
        });
        // System.out.println(existsEdge);
        System.out.println((g.vertices()));
        System.out.println(g.vertices().getClass());
        SmartGraphDemoContainer gContainer = new SmartGraphDemoContainer(graphView);
        gContainer.setMinSize(1080, 728);
        gContainer.setPrefSize(1080, 728);
        gContainer.setCenter(graphView);
        drawPane.getChildren().addAll(gContainer);

        graphView.init();

        graphView.setVertexDoubleClickAction(graphVertex -> {
            System.out.println("Vertex contains element: " + graphVertex.getUnderlyingVertex().element());
            if (!graphVertex.removeStyleClass("myVertex")) {
                graphVertex.addStyleClass("myVertex");
            }
            System.out.println(graphVertex.getUnderlyingVertex());
            g.removeVertex(graphVertex.getUnderlyingVertex());
            graphView.update();
        });

        graphView.setEdgeDoubleClickAction(graphEdge -> {
            System.out.println("Edge contains element: " + graphEdge.getUnderlyingEdge().element());
            // dynamically change the style when clicked
            graphEdge.setStyle("-fx-stroke: red; -fx-stroke-width: 2;");

            // uncomment to see edges being removed after click
            Edge<String, String> underlyingEdge = graphEdge.getUnderlyingEdge();
            System.out.println(underlyingEdge);
            g.removeEdge(underlyingEdge);
            graphView.update();
        });
    }

    private Graph<String, String> build_sample_digraph() {

        Digraph<String, String> g = new DigraphEdgeList<>();

        g.insertVertex("A");
        g.insertVertex("B");
        g.insertVertex("C");
        g.insertVertex("D");
        g.insertVertex("E");
        g.insertVertex("F");

        g.insertEdge("A", "B", "A-B");
        g.insertEdge("B", "A", "B-A");
        g.insertEdge("A", "C", "A-C");
        g.insertEdge("A", "D", "A-D");
        g.insertEdge("B", "C", "B-C");
        g.insertEdge("C", "D", "C-D");
        g.insertEdge("B", "E", "B-E");
        g.insertEdge("F", "D", "D-F");

        return g;
    }
}
