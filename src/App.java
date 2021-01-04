import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import controller.*;

public class App extends Application{
    
    public static void main(String[] args) throws Exception{
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("views/NetworkBuild_GUI.fxml"));
        stage.setTitle("Network build");
        stage.setResizable(true);
        stage.setScene(new Scene(root));
        stage.show();
    }
}