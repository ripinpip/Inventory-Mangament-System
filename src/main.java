/*
The folder with the javadoc comments can be found in the src folder with the other classes.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class main extends Application {

    public void start(Stage stage) {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
            Scene mainFormScene = new Scene(root);
            stage.setScene(mainFormScene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) { launch(args);}
}