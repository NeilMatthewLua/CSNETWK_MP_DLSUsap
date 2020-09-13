import java.io.*;
import java.net.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.nio.ByteBuffer;
import javafx.stage.FileChooser;

public class Client implements Runnable{

    private Socket s;
    private DataInputStream dis;
    private DataOutputStream dos;
    private boolean running; 

    public Client (Socket socket){
        // establish the connection
        this.s = socket;
        this.running = true; 
        // obtaining input and out streams
        try{
            this.dis = new DataInputStream(this.s.getInputStream());
            this.dos = new DataOutputStream(this.s.getOutputStream());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Getter for the Socket
     * @return Socket
     */
    public Socket getSocket(){
        return this.s;
    }

    /**
     * Sends a message to the server towards to the other client
     * @param strMessage Message of the sender 
     */
    public void sendMessage(String strMessage) throws UnknownHostException, IOException{
        try {
            // write on the output stream
            this.dos.writeUTF("MESSAGE");
            this.dos.writeUTF(strMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends an image the server towards to the other client
     * @param image Image of the sender 
     */
    public void sendMessage(File image) throws UnknownHostException, IOException{
        try {
            BufferedImage buffImage = ImageIO.read(new File(image.getPath()));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(buffImage, "jpg", byteArrayOutputStream);

            byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
            // write on the output stream
            this.dos.writeUTF("FILE");
            this.dos.write(size);
            this.dos.write(byteArrayOutputStream.toByteArray());
            dos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a message and image the server towards to the other client
     * @param strMessage Message of the sender 
     * @param image Image of the sender 
     */
    public void sendMessage(String strMessage, File image) throws UnknownHostException, IOException{
        this.sendMessage(strMessage);
        this.sendMessage(image);
        // try {
        //     // write on the output stream
        //     this.dos.writeUTF("MESSAGE");
        //     this.dos.writeUTF(message);
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }


        // try {
        //         BufferedImage image = ImageIO.read(new File(image.getPath()));
        //         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //         ImageIO.write(image, "jpg", byteArrayOutputStream);

        //         byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
        //         // write on the output stream
        //         this.dos.writeUTF("FILE");
        //         this.dos.write(size);
        //         this.dos.write(byteArrayOutputStream.toByteArray());
        //         dos.flush();
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
    }

    /**
     * Sends a signal to the server that the user will logout and closes the thread 
     */
    public void logout() {
        try {
            // write on the output stream
            this.dos.writeUTF("LOGOUT");
            this.running = false; 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        while(this.running){
            try{
                //Receives the heads up first from the server to know what kind of message will be received
                String message = dis.readUTF();
                //If message is a chat 
                if(message.equals("MESSAGE")){
                    try{
                        String chat = dis.readUTF();
                    }
                    catch(Exception chatErr){
                        chatErr.printStackTrace();
                    }
                }
                else if(message.equals("FILE")){ //Message is an image
                    try{
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("Save Image");
                        
                        File file = fileChooser.showSaveDialog(null); //Launch save image window file explorer
                        if (file != null) {
                            try {
                                byte[] sizeAr = new byte[4];
                                this.dis.read(sizeAr);
                                int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

                                byte[] imageAr = new byte[size];
                                this.dis.read(imageAr);

                                BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));

                                ImageIO.write(image, "jpg", file);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                    catch(Exception fileErr){
                        fileErr.printStackTrace();
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}