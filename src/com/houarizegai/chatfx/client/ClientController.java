package com.houarizegai.chatfx.client;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ClientController implements Initializable {

    @FXML
    private VBox root;

    @FXML
    private JFXListView msgNodes;

    @FXML
    private JFXTextField msgField;

    /* Start Msg variables */
    
    public static Socket s;
    public static DataInputStream din;
    public static DataOutputStream dout;

    /* End Msg variables */


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        root.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                sendMsg();
            }
        });

        (new Thread() {
            @Override
            public void run() {
                try {
                    s = new Socket("127.0.0.1", 1201);
                    din = new DataInputStream(s.getInputStream());
                    dout = new DataOutputStream(s.getOutputStream());

                    String msgin = "";
                    while (!msgin.equals("exit")) {
                        msgin = din.readUTF();
                        addMsg(msgin, true);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @FXML
    private void sendMsg() {
        if (msgField.getText() == null || msgField.getText().trim().isEmpty()) {
            return;
        }

        try {
            dout.writeUTF(msgField.getText().trim());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        addMsg(msgField.getText().trim(), false);
        msgField.setText(null);
    }

    @FXML
    private void emojiChooser() {

    }

    private void addMsg(String msg, boolean senderIsServer) {
        Label lbl = new Label(msg);
        lbl.setStyle("-fx-font-size: 16px;"
                + "-fx-background-color: #" + (senderIsServer ? "B00020" : "2196f3") + ";"
                + "-fx-text-fill: #FFF;"
                + "-fx-background-radius:25;"
                + "-fx-padding: 10px;");
        lbl.setWrapText(true);
        lbl.setMaxWidth(400);
        
        HBox container = new HBox(lbl);
        container.setPrefHeight(40);
        container.setAlignment(Pos.CENTER_LEFT);
        container.setPadding(new Insets(0, 10, 0, 10));

        msgNodes.getItems().add(container);
    }

}
