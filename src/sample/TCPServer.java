package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

class TCPServer extends Thread
{
    private final StringProperty message = new SimpleStringProperty("");
    int port = 0;
    boolean isWorking = true;

    TCPServer(TextField messageField, int port){
        messageField.textProperty().bindBidirectional(message);
        this.port = port;
    }

    public void stopServer(){
        isWorking = false;
    }

    @Override
    public void run()
    {
        try{
            String clientSentence;
            String capitalizedSentence;
            ServerSocket welcomeSocket = new ServerSocket(port);

            while (isWorking) {
                Socket connectionSocket = welcomeSocket.accept();
                BufferedReader inFromClient =
                        new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
                clientSentence = inFromClient.readLine();
                message.set(clientSentence);
                capitalizedSentence = clientSentence.toUpperCase() + '\n';
                outToClient.writeBytes(capitalizedSentence);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }
}