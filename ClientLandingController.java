import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.*;
import java.net.*;

public class ClientLandingController {
    @FXML private TextField ipAddress; 
    @FXML private TextField portNumber; 
    @FXML private Label errorMessage; 
    @FXML private AnchorPane background;

    private Client client;

    //Function when user presses connect btn
    @FXML 
    public void connect(MouseEvent e) throws IOException {
        if (!isEmptyFields()) {
            String ipAddressVal = ipAddress.getText().trim(); 
            String portNumberVal = portNumber.getText().trim();
            // System.out.println(ipAddressVal);
            // System.out.println(portNumberVal);
            try{
                Socket s = new Socket(ipAddressVal, Integer.parseInt(portNumberVal));
                client = new Client(s);
                client.start();
                Stage stage = (Stage) background.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/View/Client-Interface.fxml"));
                Scene scene = new Scene(loader.load());
                stage.setScene(scene);
                //Close stage handler to logout user on stage close
                stage.setOnHiding( event -> {
                    System.out.println("Closing Stage");
                    this.client.logout(); 
                });
                ((ClientInterfaceController) loader.getController()).setClient(client); 
            }
            catch(Exception error){
                System.out.println(error); 
                String errorMsgString = "Socket connection error"; 
                setErrorMessage(errorMsgString);
            }
        }
        else {
            String errorMsgString = "Please fill out all entries"; 
            setErrorMessage(errorMsgString);
        }
    }

    //Returns true if a field is empty 
    private boolean isEmptyFields() {
        boolean emptyFields = false;
        String ipAddressVal = ipAddress.getText().trim(); 
        String portNumberVal = portNumber.getText().trim(); 
        
        if (ipAddressVal.length() == 0 || portNumberVal.length() == 0) 
            emptyFields = true;

        return emptyFields; 
    }

    //Sets the error message to be displayed
    private void setErrorMessage(String msg) {
        errorMessage.setText(msg); 
        errorMessage.setVisible(true); 
    }
}