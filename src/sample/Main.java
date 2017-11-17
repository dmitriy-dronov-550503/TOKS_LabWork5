package sample;

import TokenRing.TokenRing;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.concurrent.TimeUnit;

public class Main extends Application {

    TokenRing tokenRing;
    Controller controller = new Controller();

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        Label portInLabel = new Label("This port:");
        Label portOutLabel = new Label("Connect to port:");
        Label ipLabel = new Label("Send to IP:");
        Label portDestinationLabel = new Label("Destination port:");
        Label priorityLabel = new Label("Priority:");
        Label messageInLabel = new Label("Message from port:");
        Label messagePurposeLabel = new Label("Purpose message:");
        Label messageOutLabel = new Label("Message OUT:");
        TextField portInField = controller.portInField;
        TextField portOutField = controller.portOutField;
        TextField ipField = controller.ipField;
        TextField portDestinationField = controller.portDestinationField;
        TextField priorityField = controller.priorityField;
        TextField messageInField = controller.messageInField;
        TextField messagePurposeField = controller.messagePurposeField;
        CheckBox monitorCheck = controller.monitorCheck;
        Button startButton = controller.startButton;
        TextField messageOutField = controller.messageOutField;
        Button sendButton = controller.sendButton;

        root.add(portInLabel, 0, 0);
        root.add(portInField, 1, 0);
        root.add(portOutLabel, 0, 1);
        root.add(portOutField, 1, 1);
        root.add(ipLabel, 0, 2);
        root.add(ipField, 1, 2);
        root.add(portDestinationLabel, 0, 3);
        root.add(portDestinationField, 1, 3);
        root.add(priorityLabel, 0, 4);
        root.add(priorityField, 1, 4);
        root.add(monitorCheck, 1, 5);

        root.add(startButton, 1, 6);
        root.add(messageInLabel, 0, 7);
        root.add(messageInField, 1, 7);


        root.add(messagePurposeLabel, 0, 8);
        root.add(messagePurposeField, 1, 8);
        root.add(messageOutLabel, 0, 9);
        root.add(messageOutField, 1, 9);
        root.add(sendButton, 1, 10);


        primaryStage.setTitle("Token Ring");
        primaryStage.setScene(new Scene(root, 300, 320));
        primaryStage.show();
    }

    @Override
    public void stop() {
        if(tokenRing!=null) tokenRing.stop();
        try {
            super.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
