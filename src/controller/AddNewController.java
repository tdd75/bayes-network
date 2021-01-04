package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddNewController implements Initializable {
    @FXML
    private TextField nameNodeTf;

    @FXML
    private void confirm() {
        System.out.println(nameNodeTf.getText());
        System.out.println("add new node confirm");
    }

    @FXML
    private void cancle() {        
        System.out.println("cancle add");
    }

    @FXML
    private void selectRootNode() {
        System.out.println("select root");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("add new");
    }
}
