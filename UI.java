import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class UI extends Application {
    private String songDir = "song/";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Damao Download");

        //root
        VBox root = new VBox();

        //stack
        HBox stack1 = new HBox();
        HBox stack2 = new HBox();
        HBox stack3 = new HBox();
        stack1.setAlignment(Pos.CENTER);
        stack1.setPadding(new Insets(30, 15, 5, 15));
        stack2.setAlignment(Pos.CENTER);
        stack2.setPadding(new Insets(5, 15, 5, 15));
        stack3.setAlignment(Pos.CENTER);
        stack3.setPadding(new Insets(5, 15, 5, 15));

        //label in stack1
        Label urlLabel = new Label("URL:");
        GridPane container1 = new GridPane();
        container1.setPadding(new Insets(5, 10, 5, 25));
        container1.getChildren().add(urlLabel);

        //textField in stack1
        TextField urlInputField = new TextField();
        urlInputField.setPrefWidth(300);
        GridPane container2 = new GridPane();
        container2.setPadding(new Insets(5, 25, 5, 10));
        container2.getChildren().add(urlInputField);

        //test progress bar
         Label message = new Label("");

        //button1 in stack2
        Button btn1 = new Button();
        btn1.setText("Download!");
        btn1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String url = urlInputField.getText().trim();

                if (url.isEmpty()) {
                    message.setText("No URL, Miao!");
                    return;
                }else if(!url.matches("^(https:\\/\\/music\\.)163.com/#/song\\?id=\\d{9}$")) {
                    message.setText("URL Not Valid, Miao!");
                    return;
                }else{
                    MusicDownloader m = new MusicDownloader();
                    m.run(url);
                    message.setText("Completed, Miao!");
                }

            }
        });
        GridPane container3 = new GridPane();
        container3.setPadding(new Insets(5, 15, 5, 15));
        container3.getChildren().add(btn1);

        //button2 in stack 2
        Button btn2 = new Button();
        btn2.setText("Open Folder!");
        btn2.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    File dir = new File(songDir);
                    if (!dir.exists())
                        dir.mkdir();
                    Desktop.getDesktop().open(dir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        GridPane container4 = new GridPane();
        container4.setPadding(new Insets(5, 15, 5, 15));
        container4.getChildren().add(btn2);


        //add all containers to stack
        stack1.getChildren().add(container1);
        stack1.getChildren().add(container2);
        stack2.getChildren().add(container3);
        stack2.getChildren().add(container4);
        stack3.getChildren().add(message);

        //add all stack to root
        root.getChildren().add(stack1);
        root.getChildren().add(stack2);
        root.getChildren().add(stack3);

        //create scene
        Scene scene = new Scene(root, 450, 200);
        scene.getStylesheets().add("css/material-fx-v0_3.css");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("image/damao.jpg"));
        primaryStage.show();
    }
}