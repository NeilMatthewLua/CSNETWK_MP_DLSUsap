import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ScrollPane;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.application.Platform;
import javafx.event.ActionEvent; 
import javafx.event.EventHandler; 
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.nio.ByteBuffer;
import javafx.stage.FileChooser;
import static javafx.stage.Modality.APPLICATION_MODAL;

public class ClientInterfaceController implements Initializable{

    @FXML private Button logout_btn;
    @FXML private Button send_btn;
    @FXML private Button attach_btn;
    @FXML private Label  file_label;
    @FXML private Label  label_msg;
    @FXML private TextArea chat_area;
    @FXML private ScrollPane scroll_chat;
    @FXML private VBox chat_box;
    @FXML private AnchorPane background;
    @FXML private AnchorPane chat_pane;

    private Client client;
    private File file;
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        chat_area.setWrapText(true);
        scroll_chat.vvalueProperty().bind(chat_box.heightProperty());
        chat_box.setPadding(new Insets(10, 50, 10, 50));
    }
  
    //Set a controller for a client
    public void setClient(Client client){
        this.client = client;
        this.client.setController(this);
    }
    
    //Upon clicking the attach button
    @FXML
    public void attach(MouseEvent e) throws IOException{
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Image Files", "*.png", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(null);
        System.out.println("HERE " + selectedFile);
        if (selectedFile != null){
            file_label.setText("Image attached: " + selectedFile.getPath());
            this.file = selectedFile;
        }
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
    public void updateUIMessage(boolean isSent, String strMessage){   
        Text text=new Text(strMessage);

        text.setFill(Color.BLACK);
        this.attachtoUI(isSent, text);
    }

    @FXML
    public void updateUIErrorMessage(boolean isSent, String strMessage){   
        Text text=new Text(strMessage);

        text.setFill(Color.RED);
        this.attachtoUI(isSent, text);
    }
    
    //Upon clicking the logout button 
    @FXML 
    public void logout(MouseEvent e) throws IOException {
        this.logout_client();
    }

    public void logout_client(){
        try{
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/View/Confirm-Exit.fxml"));
            Scene scene = new Scene(loader.load(),575,575);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(APPLICATION_MODAL);
           ((ConfirmExitController) loader.getController()).setClient(this.client);
            stage.setX(350);
            stage.setY(30);
            stage.showAndWait();
   
            if(this.client.getRunning() == false){
                this.client.logout(); 
                try {
                    Stage stage_2 = (Stage) this.background.getScene().getWindow();
                    FXMLLoader loader_2 = new FXMLLoader();
                    loader_2.setLocation(getClass().getResource("/View/Client-Landing.fxml"));
                    Scene scene_2 = new Scene(loader_2.load());
                    stage_2.setScene(scene_2); 
                    stage_2.setOnCloseRequest(event -> {
                        stage_2.close();
                    });
                }
                catch (Exception error) {
                    System.out.println(error); 
                }
            }
          }
          catch(IOException event){
            System.out.println("Error");
          }
    }
    
    @FXML
    public void updateUIImage(boolean isSent, BufferedImage image, String file_type){   
        // create a Button 
        Button save_button = new Button("Download Image"); 
  
        // create an Event Handler 
        EventHandler<ActionEvent> event1 =  
         new EventHandler<ActionEvent>() { 
  
            public void handle(ActionEvent e) 
            { 
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Image");
                
                File file = fileChooser.showSaveDialog(null);
  
                if (file != null) { 
                    client.saveImage(image, file, file_type);
                } 
            } 
        }; 
  
        save_button.setOnAction(event1); 
        
        HBox hbox=new HBox(12);
        hbox.setPadding(new Insets(15, 12, 15, 12));
        
        if(isSent){
            Text text=new Text("You sent an image");
    
            text.setFill(Color.BLACK);
            hbox.setAlignment(Pos.BOTTOM_RIGHT);
            hbox.getChildren().add(text);
        }else{
            chat_box.setAlignment(Pos.TOP_LEFT);
            hbox.setAlignment(Pos.CENTER_LEFT);
            hbox.getChildren().add(save_button);
        }
        this.file = null;
        Platform.runLater(() -> chat_box.getChildren().add(hbox));
    }

    @FXML
    public void attachtoUI(boolean isSent, Text text){  
        HBox hbox=new HBox(12);
        hbox.setPadding(new Insets(15, 12, 15, 12));

        if(isSent){
            hbox.setAlignment(Pos.BOTTOM_RIGHT);
        }else{
            chat_box.setAlignment(Pos.TOP_LEFT);
            hbox.setAlignment(Pos.CENTER_LEFT);
        }
        hbox.getChildren().add(text);
        Platform.runLater(() -> chat_box.getChildren().add(hbox));
    }

    @FXML
    public void toggleChat(boolean isVisible, String strMessage){
        Platform.runLater(() -> label_msg.setText(strMessage));
        Platform.runLater(() -> chat_pane.setVisible(isVisible));
    }
}