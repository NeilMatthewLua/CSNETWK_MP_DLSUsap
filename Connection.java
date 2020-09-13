import java.io.*;
import java.net.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.nio.ByteBuffer;
import javafx.stage.FileChooser;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;

/*
    Connection class to handle individual client connections 
    @attribute source Address of the client connected
    @attribute dest Address of the other client in the server 
    @attribute socket Socket of the source client
    @attribute reader Reader stream for source client 
    @attribute writer Writer stream for source client 
*/
public class Connection extends Thread {
    private String source; 
    private String dest; 
    private Socket socket;
    private Server server;
    private DataInputStream reader;
    private DataOutputStream writer;

    /*
        Connection constructor 
        @attribute socket Socket of the client 
        @attribute source Address of the client 
        @attribute server Server object 
    */
    public Connection(Socket socket, String source, Server server) {
        this.socket = socket;
        this.source = source; 
        this.server = server; 
        try {
            this.reader = new DataInputStream(socket.getInputStream());
            this.writer = new DataOutputStream(socket.getOutputStream());
        } 
        catch (IOException e) {
            System.out.println("Error in creating IO stream from client."); 
        }
    }

    /*
        Getter for the source attribute 
        @return String source address of the connection 
    */
    public String getSource() {
        return this.source; 
    }

    /*
        Setter for the destination attribute
    */
    public void setDest(String d) {
        this.dest = d; 
    }

    /*
        Sends a message to the client using this connection 
        @param message Message to be sent 
    */
    public void writeMessage(String message) {
        System.out.println("Received message from other client: " + message); 
        this.server.addLog(new Log(this.source, "MESSAGE", this.dest, false, message)); 
        try {
            this.writer.writeUTF("MESSAGE");
            this.writer.writeUTF(message);
        }
        catch (IOException e) {
            System.out.println("Error in writing message to client."); 
        }
    }

    /*
        Sends an image to the client using this connection 
        @param message Message to be sent 
    */
    public void writeMessage(BufferedImage image) {
        System.out.println("Received am image from other client"); 
        // TODO this.server.addLog(new Log(this.source, "MESSAGE", this.dest, false, message)); 
        try {
            System.out.print("Last stop! I check if it's same so: ");
            System.out.print(bufferedImagesEqual(image, ImageIO.read(new File("C:\\Users\\Neil Matthew Lua\\Desktop\\DP.jpg"))));
            // BufferedImage buffImage = ImageIO.read(new File(image.getPath()));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", byteArrayOutputStream);

            byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
            // write on the output stream
            this.writer.writeUTF("FILE");
            this.writer.write(size);
            this.writer.write(byteArrayOutputStream.toByteArray());
            writer.flush();
        }
        catch (IOException e) {
            System.out.println("Error in writing message to client."); 
        }
    }

    /*
        Returns String-form of source and destination attributes
    */
    public String toString() {
        return "Connection:\n Source:" + this.source + '\n' + "Destination:" + this.dest; 
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
    public void run() {
        try {
            String msg;
            // Check what activity the user did
            // while (!(msg = reader.readUTF()).equals("END")) {
                // if (msg.equals("MESSAGE")) {
                //     msg = reader.readUTF();
                //     System.out.println("Received message from GUI: " + msg); 
                //     this.server.sendMessage(this.dest, msg); 
                //     this.server.addLog(new Log(this.source, "MESSAGE", this.dest, false, msg)); 
                // }
                // else if (msg.equals("FILE")) {
                    try {
                        byte[] sizeAr = new byte[4];
                        this.reader.read(sizeAr);
                        int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

                        byte[] imageAr = new byte[size];
                        this.reader.read(imageAr);

                        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
                        System.out.print("The server receives the image and before I pass it to the client, I check if it's same so: ");
                        System.out.print(bufferedImagesEqual(image, ImageIO.read(new File("C:\\Users\\Neil Matthew Lua\\Desktop\\DP.jpg"))));

                        ImageIO.write(image, "jpg", new File("C:\\Users\\Neil Matthew Lua\\Desktop\\DP2.jpg"));
                        this.server.sendMessage(this.dest, image); 
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                // }
            // }
            this.server.addLog(new Log(this.source, "LOGOUT")); 
            //Close IO streams
            socket.close(); 
            reader.close(); 
            writer.close(); 
        } catch (Exception e) {
            e.printStackTrace(); // Uncomment this if you want to look at the error thrown
        } finally {
            System.out.println("Server: Client " + socket.getRemoteSocketAddress() + " has disconnected");
        }
    }

}