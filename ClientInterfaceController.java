import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;

public class ClientInterfaceController implements Initializable{

    @FXML private Button logout_btn;
    @FXML private Button send_btn;
    @FXML private Button attach_btn;
    @FXML private Label  file_label;
    @FXML private TextArea chat_area;

    private Client client;
    private File file;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        chat_area.setWrapText(true);
    }
    
    //Set a controller for a client
    public void setClient(Client client){
        this.client = client;
        this.client.setController(this);
    }
    
    // //Upon clicking the attach button
    @FXML
    public void attach(MouseEvent e) throws IOException{
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        file_label.setText("Image attached: " + selectedFile.getPath());
        this.file = selectedFile;
    }

    //Upon clicking the send button
    @FXML
    public void send(MouseEvent e) throws IOException{
        try {
            String strMessage = chat_area.getText().trim();
            if(this.file == null & !(strMessage.equals(""))){
                client.sendMessage(strMessage);
            }
            else if (!(this.file == null) & strMessage.equals("")){
                client.sendMessage(this.file);
            }
            else if (!(this.file == null) & !strMessage.equals("")){
                client.sendMessage(strMessage, this.file);
            }

            
            file_label.setText("");
            chat_area.setText("");
            chat_area.requestFocus();
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    @FXML
    public void saveImage(MouseEvent e) throws IOException{
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        
        File file = fileChooser.showSaveDialog(null);
    }
    
    public void updateUI(){
        System.out.println("CONTROLLER HAS RECEIVED THE NUDGE");
    }

    // @Override
    // public boolean update(String username,String message) throws RemoteException {
    //     Text text=new Text(message);

    //     text.setFill(Color.WHITE);
    //     text.getStyleClass().add("message");
    //     TextFlow tempFlow=new TextFlow();

    //     if(!this.username.equals(username)){
    //         Text txtName=new Text(username + "\n");
    //         txtName.getStyleClass().add("txtName");
    //         tempFlow.getChildren().add(txtName);
    //     }

    //     tempFlow.getChildren().add(text);
    //     tempFlow.setMaxWidth(200);

    //     TextFlow flow=new TextFlow(tempFlow);

    //     HBox hbox=new HBox(12);

    //     if(!this.username.equals(username)){
    //         tempFlow.getStyleClass().add("tempFlowFlipped");
    //         flow.getStyleClass().add("textFlowFlipped");
    //         chatBox.setAlignment(Pos.TOP_LEFT);
    //         hbox.setAlignment(Pos.CENTER_LEFT);
    //         hbox.getChildren().add(flow);

    //     }else{
    //         text.setFill(Color.WHITE);
    //         tempFlow.getStyleClass().add("tempFlow");
    //         flow.getStyleClass().add("textFlow");
    //         hbox.setAlignment(Pos.BOTTOM_RIGHT);
    //         hbox.getChildren().add(flow);
    //     }

    //     hbox.getStyleClass().add("hbox");

    //     return true;

    // }

}