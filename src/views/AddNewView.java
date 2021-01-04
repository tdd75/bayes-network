package views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AddNewView {

    public void addNewNode() {
        try {
            Stage stage = new Stage(StageStyle.DECORATED);
            Parent root = FXMLLoader.load(getClass().getResource("AddNewNode.fxml"));
            stage.setTitle("Add new node");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void addNewEdge() {
        try {
            Stage stage = new Stage(StageStyle.DECORATED);
            Parent root = FXMLLoader.load(getClass().getResource("AddNewEdge.fxml"));
            stage.setTitle("Add new edge");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
