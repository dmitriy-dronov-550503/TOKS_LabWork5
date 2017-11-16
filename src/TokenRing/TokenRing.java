package TokenRing;

import com.google.gson.Gson;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import sample.Controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TokenRing {
    int portThis = 0;
    int portOut = 0;
    int portDestination = 0;
    int priority = 0;
    volatile boolean isReaderWorking = true;
    boolean isMonitor = false;
    String IP = "localhost";
    Stack<String> messages = new Stack<>();
    final StringProperty message = new SimpleStringProperty("");
    final StringProperty messagePurpose = new SimpleStringProperty("");
    Timer tokenTimeoutTimer;
    private TokenRingReader tokenRingReader = new TokenRingReader(this);

    public TokenRing(TextField messageField, TextField purposeField, String IP, int portThis, int portOut, int priority) {
        messageField.textProperty().bindBidirectional(message);
        purposeField.textProperty().bindBidirectional(messagePurpose);
        this.IP = IP;
        this.portThis = portThis;
        this.portOut = portOut;
        this.priority = priority;
    }

    static void sendToPort(TokenRingPackage trPackage, String IP, int toPort) {
        try {
            Socket clientSocket = new Socket(IP, toPort);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeBytes(trPackage.getPackage());
            clientSocket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initialToken() {
        TokenRingPackage trPackage = new TokenRingPackage();
        trPackage.INFO = "MONITOR";
        trPackage.SA = trPackage.DA = portThis;
        trPackage.P = priority;
        sendToPort(trPackage, IP, portOut);
    }

    public void addMessage(String m, int portDestination) {
        messages.push(m);
        this.portDestination = portDestination;
    }

    public void setMonitor() {
        priority = Integer.MAX_VALUE;
        initialToken();
        runTokenTimeoutControl();
    }

    void runTokenTimeoutControl() {
        TimerTask tokenTimeoutTask = new TimerTask() {
            @Override
            public void run() {
                initialToken();
            }
        };
        tokenTimeoutTimer = new Timer();
        tokenTimeoutTimer.schedule(tokenTimeoutTask, 4000, 4000);
    }

    public void start() {
        tokenRingReader.start();
    }

    public void stop() {
        try {
            isReaderWorking = false;
            if(tokenTimeoutTimer!=null) tokenTimeoutTimer.cancel();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try{
            tokenRingReader.stopReader();
        } catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    protected void finalize() {
        stop();
    }
}