package sample;

import TokenRing.TokenRing;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class Controller {
    private TokenRing tokenRing;
    TextField portInField = new TextField();
    TextField portOutField = new TextField();
    TextField ipField = new TextField("localhost");
    TextField portDestinationField = new TextField();
    TextField priorityField = new TextField();
    TextField messageInField = new TextField();
    TextField messagePurposeField = new TextField();
    TextField messageOutField = new TextField();
    CheckBox monitorCheck = new CheckBox("is monitor");
    Button startButton = new Button("Start");
    Button sendButton = new Button("Send");

    public Controller(){
        monitorCheck.setOnAction(event -> monitorCheckAction());
        startButton.setOnAction(event -> startButtonAction());
        sendButton.setOnAction(event -> sendButtonAction());
    }

    private void monitorCheckAction(){
        boolean f = monitorCheck.isSelected();
        priorityField.setEditable(!f);
        if (f) priorityField.setText("" + Integer.MAX_VALUE);
        else priorityField.setText("");
    }
    private void startButtonAction(){
        if (startButton.getText().equals("Start")) {
            try{
                tokenRing = new TokenRing(messageInField, messagePurposeField,
                        ipField.getText(),
                        Integer.parseInt(portInField.getText()),
                        Integer.parseInt(portOutField.getText()),
                        Integer.parseInt(priorityField.getText())
                );
                tokenRing.start();
                if (monitorCheck.isSelected()) {
                    tokenRing.setMonitor();
                }
                startButton.setText("Stop");
            }
            catch (NumberFormatException ex){
                alert("Please, fill all fields.");
            }
        } else {
            tokenRing.stop();
            startButton.setText("Start");
        }
    }
    private void sendButtonAction(){
        try {
            tokenRing.addMessage(messageOutField.getText(), Integer.parseInt(portDestinationField.getText()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void alert(String message){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void stop(){
        if(tokenRing!=null) tokenRing.stop();
    }
}
