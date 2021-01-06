package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.brunomnsilva.smartgraph.containers.SmartGraphDemoContainer;
import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.Edge;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import views.CreateTableTruth;

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
        dialog.setTitle("Add New Edge");
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
        dialog.setTitle("Probability Table");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(v -> {
            if (v != "")
                tableTruth.createTable(g, v);
        });
    }

    private String nameNode;
    Graph<String, String> g = build_sample_digraph();
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
        SmartGraphDemoContainer gContainer = new SmartGraphDemoContainer(graphView);
        gContainer.setMinSize(1080, 728);
        gContainer.setPrefSize(1080, 728);
        gContainer.setCenter(graphView);
        drawPane.getChildren().addAll(gContainer);

        graphView.init();
        graphView.setAutomaticLayout(true);
        graphView.setVertexDoubleClickAction(graphVertex -> {
            if (!graphVertex.removeStyleClass("myVertex")) {
                graphVertex.addStyleClass("myVertex");
            }
            g.removeVertex(graphVertex.getUnderlyingVertex());
            graphView.update();
        });

        graphView.setEdgeDoubleClickAction(graphEdge -> {
            // dynamically change the style when clicked
            graphEdge.setStyle("-fx-stroke: red; -fx-stroke-width: 2;");

            // uncomment to see edges being removed after click
            Edge<String, String> underlyingEdge = graphEdge.getUnderlyingEdge();
            g.removeEdge(underlyingEdge);
            graphView.update();
        });
    }

    private Graph<String, String> build_sample_digraph() {

        Digraph<String, String> g = new DigraphEdgeList<>();

        g.insertVertex("RAIN");
        g.insertVertex("SPRINKLER");
        g.insertVertex("GRASS WET");

        g.insertEdge("RAIN", "SPRINKLER", "RAIN-SPRINKLER");
        g.insertEdge("SPRINKLER", "GRASS WET", "SPRINKLER-GRASS WET");
        g.insertEdge("RAIN", "GRASS WET", "RAIN-GRASS WET");

        return g;
    }
}
