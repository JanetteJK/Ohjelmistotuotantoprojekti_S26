package view;
import java.io.IOException;

import controller.signInController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class SignIn extends Application {
     signInController controller;

    @Override
    public void init(){
        System.out.println("INIT CALLED\n");
    }

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/loginUi.fxml"));
        Parent root = fxmlLoader.load();
        controller = fxmlLoader.getController();
        stage.setTitle("Flashers");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[]args) {
        launch(args);
    }
}
