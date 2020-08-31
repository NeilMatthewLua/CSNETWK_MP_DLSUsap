package Controller; 

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

public class ClientLandingController {
    @FXML private TextField ipAddress; 
    @FXML private TextField portNumber; 
    @FXML private Label errorMessage; 
    @FXML private AnchorPane background; 

    //Function when user presses connect btn
    @FXML 
    public void connect(MouseEvent e) throws IOException {
        if (!isEmptyFields()) {
            Stage stage = (Stage) background.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../View/Client-Interface.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);

            // String ipAddressVal = ipAddress.getText().trim(); 
            // String portNumberVal = portNumber.getText().trim();
            // ((ClientInterfaceController) loader.getController()).setClient(ipAddressVal, portNumberVal); 
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