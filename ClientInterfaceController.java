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
        client = new Client("localhost", 4000);
        chat_area.setWrapText(true);
    }
    
    public void setClient(String strAddress, int nPort){
        client = new Client(strAddress, nPort);
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
    public void send(MouseEvent e) throws IOException {
        try {
            String strMessage = chat_area.getText().trim();
            if(this.File == null & !strMessage.equals("")){
                client.sendMessage(strMessage);
            }
            else if (!this.File == null & !strMessage.equals("")){
                client.sendMessage(strMessage, file);
            }
            else if (!this.File == null & strMessage.equals("")){
                client.sendMessage(file);
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
        chat_area.setText("");
        chat_area.requestFocus();
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

    //     Circle img =new Circle(32,32,16);
    //     try{
    //         System.out.println(username);
    //         String path= new File(String.format("resources/user-images/%s.png", username)).toURI().toString();
    //         img.setFill(new ImagePattern(new Image(path)));
    //     }catch (Exception ex){
    //         String path= new File("resources/user-images/user.png").toURI().toString();
    //         img.setFill(new ImagePattern(new Image(path)));
    //     }

    //     img.getStyleClass().add("imageView");

    //     if(!this.username.equals(username)){

    //         tempFlow.getStyleClass().add("tempFlowFlipped");
    //         flow.getStyleClass().add("textFlowFlipped");
    //         chatBox.setAlignment(Pos.TOP_LEFT);
    //         hbox.setAlignment(Pos.CENTER_LEFT);
    //         hbox.getChildren().add(img);
    //         hbox.getChildren().add(flow);

    //     }else{
    //         text.setFill(Color.WHITE);
    //         tempFlow.getStyleClass().add("tempFlow");
    //         flow.getStyleClass().add("textFlow");
    //         hbox.setAlignment(Pos.BOTTOM_RIGHT);
    //         hbox.getChildren().add(flow);
    //         hbox.getChildren().add(img);
    //     }

    //     hbox.getStyleClass().add("hbox");
    //     Platform.runLater(() -> chatBox.getChildren().addAll(hbox));

    //     return true;

    // }

}