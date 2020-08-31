import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import java.awt.event.MouseEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;

public class ClientInterfaceController implements Initializable{

    @FXML Button logout_btn;
    @FXML Button send_btn;
    @FXML Button attach_btn;
    @FXML TextArea chat_area;

    Client client;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        client = new Client("localhost", 4000);
    }
    
    public void setClient(String strAddress, int nPort){
        client = new Client(strAddress, nPort);
    }
    
    // //Upon clicking the attach button
    // @FXML
    // public void attach(MouseEvent e) throws IOException{
    //     FileChooser fileChooser = new FileChooser();
    //     fileChooser.setTitle("Open Resource File");
    //     fileChooser.getExtensionFilters().addAll(
    //             new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
    //     File selectedFile = fileChooser.showOpenDialog(null);
    // }

    //Upon clicking the send button
    @FXML
    public void send(MouseEvent e) throws IOException {
        System.out.println("HERE");
//         try {
//             if(txtMsg.getText().trim().equals("")) return;
// //TODO Send a message
//         } catch (RemoteException e) {
//             e.printStackTrace();
//         }
//         txtMsg.setText("");
//         txtMsg.requestFocus();
    }

}