import java.io.*;
import java.net.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.nio.ByteBuffer;
import javafx.stage.FileChooser;

public class Client extends Thread{

    private Socket s;
    private  DataInputStream dis;
    private DataOutputStream dos;
    private ClientInterfaceController controller;

    public Client (Socket socket){
        // establish the connection
        this.s = socket;
        this.controller = null;
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
            System.out.println("I sent this: " + strMessage);
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
            // this.dos.writeUTF("FILE");
            BufferedImage buffImage = ImageIO.read(new File(image.getPath()));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(buffImage, "jpg", byteArrayOutputStream);

            byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();

            System.out.print("Just before I send, I check if it's same so: ");
            System.out.print(bufferedImagesEqual(buffImage, ImageIO.read(new File("C:\\Users\\Neil Matthew Lua\\Desktop\\DP.jpg"))));

            // write on the output stream
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
    public void saveImage(BufferedImage image, File file){
        try{
            ImageIO.write(image, "jpg", new File(file.getPath() + ".jpg"));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean bufferedImagesEqual(BufferedImage img1, BufferedImage img2) {
        if (img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight()) {
            for (int x = 0; x < img1.getWidth(); x++) {
                for (int y = 0; y < img1.getHeight(); y++) {
                    if (img1.getRGB(x, y) != img2.getRGB(x, y))
                        return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public void run(){
        while(true){
            try{
                //Receives the heads up first from the server to know what kind of message will be received
                String message = dis.readUTF();
                //If message is a chat 
                if(message.equals("MESSAGE")){
                    try{
                        String chat = dis.readUTF();
                        System.out.println("I received this: " + chat);
                        this.controller.updateUIMessage(false, chat);
                    }
                    catch(Exception chatErr){
                        chatErr.printStackTrace();
                    }
                }
                else if(message.equals("FILE")){ //Message is an image
                    try {
                        byte[] sizeAr = new byte[4];
                        this.dis.read(sizeAr);
                        int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

                        byte[] imageAr = new byte[size];
                        this.dis.read(imageAr);

                        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
                        System.out.print("By the time I receive an image, I check if it's same so: ");
                        System.out.print(bufferedImagesEqual(image, ImageIO.read(new File("C:\\Users\\Neil Matthew Lua\\Desktop\\DP.jpg"))));

                        this.controller.updateUIImage(false, image);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}