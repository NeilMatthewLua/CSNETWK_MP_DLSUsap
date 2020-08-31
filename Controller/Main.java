package Controller; 

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Font.loadFont(Main.class.getResource("../CSS/Lato-Bold.ttf").toExternalForm(),20);
        Font.loadFont(Main.class.getResource("../CSS/Lato-Medium.ttf").toExternalForm(),20);
        Font.loadFont(Main.class.getResource("../CSS/Lato-Regular.ttf").toExternalForm(),20);
        Parent root = FXMLLoader.load(getClass().getResource("../View/Client-Landing.fxml"));
        primaryStage.setTitle("DLSUsap");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}