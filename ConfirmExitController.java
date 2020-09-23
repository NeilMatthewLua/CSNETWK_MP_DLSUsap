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
import javafx.scene.control.Button;


public class ConfirmExitController {

    @FXML private Label notice_label;
    
    @FXML private Button yes_btn;

    @FXML private Button no_btn;

    private Client client;

    // Sets a client for the controller
    public void setClient(Client client){
        this.client = client;
    }
    
    @FXML
    public void confirmLogout(MouseEvent e){
        this.client.setRunning(false);
        Stage stage = (Stage) yes_btn.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    public void noLogout(MouseEvent e){
        Stage stage = (Stage) yes_btn.getScene().getWindow();
        stage.close();
        e.consume();
    }

}