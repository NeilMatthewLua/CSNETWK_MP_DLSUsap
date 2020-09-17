import java.io.*;
import java.net.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.nio.ByteBuffer;

public class Client extends Thread{

    private Socket s;
    private DataInputStream dis;
    private DataOutputStream dos;
    private ClientInterfaceController controller;
    private boolean running; 

    public Client(Socket socket){
        // establish the connection
        this.s = socket;
        this.controller = null;
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
     * Setter for the Controller
     * @param controller the controller to connect to the client
     */
    public void setController(ClientInterfaceController controller){
        this.controller = controller;
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
            this.controller.updateUIMessage(true, strMessage);
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
            this.dos.writeUTF("FILE");
            BufferedImage buffImage = ImageIO.read(new File(image.getPath()));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            String[] arrOfStr = image.getPath().split("\\."); 
            String file_type = arrOfStr[arrOfStr.length - 1];
            this.dos.writeUTF(file_type);
            ImageIO.write(buffImage, file_type, byteArrayOutputStream);

            byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();

            // write on the output stream
            this.dos.write(size);
            this.dos.write(byteArrayOutputStream.toByteArray());
            dos.flush();
            this.controller.updateUIImage(true, buffImage, file_type);
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
    }

    /**
     * Saves an image to the local machine
     * @param image Image of the sender 
     * @param file File object to path 
     */
    public void saveImage(BufferedImage image, File file, String file_type){
        try{
            ImageIO.write(image, file_type, new File(file.getPath() + "." + file_type));
        }
        catch(Exception e){
        }
    }
    
    /**     
     * Sends a signal to the server that the user will logout and closes the thread 
     */
    public void logout() {
        try {
            // write on the output stream
            this.dos.writeUTF("LOGOUT");
            this.running = false; 
            this.dis.close();
            this.dos.close();
            this.s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        while(this.running){
            try{
                //Receives the heads up first from the server to know what kind of message will be received
                String message = "";
                if(dis.available() > 0)
                    message = dis.readUTF();
                //If message is a chat 
                if(message.equals("MESSAGE")){
                    try{
                        String chat = dis.readUTF();
                        this.controller.updateUIMessage(false, chat);
                    }
                    catch(Exception chatErr){
                        chatErr.printStackTrace();
                    }
                }
                else if(message.equals("FILE")){ //Message is an image
                    try {
                        String file_type = dis.readUTF();
                        byte[] sizeAr = new byte[4];
                        this.dis.read(sizeAr);
                        int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

                        byte[] imageAr = new byte[size];
                        int read = 0;
                        try{
                            while(read < size)
                            {
                                read += this.dis.read(imageAr,read,size-read); 
                            }
                        }
                        catch (EOFException e){
                            e.printStackTrace();
                        }
                        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));

                        this.controller.updateUIImage(false, image, file_type);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                else if (message.equals("NO CLIENT")){
                    this.controller.toggleChat(false, "Waiting for other client");
                }
                else if (message.equals("CONNECTION ESTABLISHED")){
                    this.controller.updateUIMessage(false, "Other client is connected.");
                    this.controller.toggleChat(true,"");
                }
                else if (message.equals("DISCONNECT")){
                    this.controller.toggleChat(false, "Other client Disconnected");
                }
                else if (message.equals("FAILSENDMSG")){
                    this.controller.updateUIErrorMessage(true, "Failed to send message");
                }
                else if (message.equals("FAILSENDFILE")){
                    this.controller.updateUIErrorMessage(true, "Failed to send file");
                }
                else if (message.equals("FAILRECEIVEMSG")){
                    this.controller.updateUIErrorMessage(false, "Failed to receive message");
                }
                else if (message.equals("FAILRECEIVEFILE")){
                    this.controller.updateUIErrorMessage(false, "Failed to receive file");
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}