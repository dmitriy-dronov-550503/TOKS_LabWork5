package sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Main extends Application {

    TCPServer tcpServer;

    public static void sendToServer(String message, int port) throws Exception {
        Socket clientSocket = new Socket("localhost", port);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outToServer.writeBytes(message);
        clientSocket.close();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{


        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        Label portInLabel = new Label("This port:");
        Label portOutLabel = new Label("Send to port:");
        Label messageInLabel = new Label("Message from port:");
        TextField portInField = new TextField();
        TextField portOutField = new TextField();
        TextField messageInField = new TextField();
        Button startButton = new Button("Start");
        startButton.setOnAction(event -> {
            tcpServer = new TCPServer(messageInField, Integer.parseInt(portInField.getText()));
            tcpServer.start();
        });

        Label messageOutLabel = new Label("Message OUT:");
        TextField messageOutField = new TextField();
        Button sendButton = new Button("Send");
        sendButton.setOnAction(event -> {
            try {
                sendToServer(messageOutField.getText(), Integer.parseInt(portOutField.getText()));
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        });

        root.add(portInLabel,0, 0);
        root.add(portInField, 1, 0);
        root.add(portOutLabel, 0, 1);
        root.add(portOutField, 1, 1);

        root.add(messageInLabel, 0, 2);
        root.add(messageInField, 1, 2);
        root.add(startButton, 1, 3);

        root.add(messageOutLabel, 0, 4);
        root.add(messageOutField,1,4);
        root.add(sendButton, 1, 5);


        primaryStage.setTitle("Token Ring");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop(){
        if(tcpServer!=null){
            tcpServer.stopServer();
        }
    }
}
