package TokenRing;

import com.google.gson.Gson;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class TokenRingReader extends Thread {
    TokenRing tokenRing;
    TokenRingPackage trPackage;
    volatile Socket connectionSocket;
    volatile BufferedReader inFromClient;
    volatile ServerSocket serverSocket;

    public TokenRingReader(TokenRing tokenRing) {
        this.tokenRing = tokenRing;
    }

    private void takeToken(TokenRingPackage trPackage) {
        trPackage.SA = tokenRing.portThis;
        trPackage.DA = tokenRing.portDestination;
        trPackage.T = false;
        trPackage.INFO = tokenRing.messages.pop();
    }

    private void resolvePriority(TokenRingPackage trPackage) {
        if (tokenRing.priority >= trPackage.P && trPackage.T) {
            takeToken(trPackage);
        } else {
            if (tokenRing.priority >= trPackage.R) trPackage.R = tokenRing.priority;
        }

    }

    private void makeDecision(TokenRingPackage trPackage) {
        if (trPackage.DA == tokenRing.portThis) {
            tokenRing.messagePurpose.set(trPackage.INFO);
            trPackage.A = true;
        }
        if (trPackage.SA == tokenRing.portThis && trPackage.A) {
            trPackage.SA = 0;
            trPackage.DA = 0;
            trPackage.INFO = "";
            trPackage.P = trPackage.R;
            trPackage.R = 0;
            trPackage.T = true;
        } else {
            if (!tokenRing.messages.empty()) {
                resolvePriority(trPackage);
            }
        }
    }

    public void stopReader() throws Exception {
        serverSocket.close();
        if(connectionSocket!=null) connectionSocket.close();
        if(inFromClient!=null) inFromClient.close();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(tokenRing.portThis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (tokenRing.isReaderWorking) {
            try {
                try {
                    connectionSocket = serverSocket.accept();
                } catch (Exception ex) {
                    System.out.println("Socket closed");
                    throw new Exception("Thread break");
                }
                if (tokenRing.isMonitor) tokenRing.tokenTimeoutTimer.cancel();
                inFromClient =
                        new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                String clientSentence = inFromClient.readLine();
                trPackage = new Gson().fromJson(clientSentence, TokenRingPackage.class);
                tokenRing.message.set("TOKEN " + trPackage.INFO);
                makeDecision(trPackage);
                TimeUnit.MILLISECONDS.sleep(1000);
                tokenRing.message.set("");
                if (tokenRing.isMonitor) tokenRing.runTokenTimeoutControl();
                TokenRing.sendToPort(trPackage, tokenRing.IP, tokenRing.portOut);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
