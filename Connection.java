import java.io.*;
import java.net.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.nio.ByteBuffer;

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
        this.dest = null; 
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
        Getter for the dest attribute 
        @return String destination address of the connection 
    */
    public String getDest() {
        return this.dest; 
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
     
    /**
        Informs client that there is no recipient on the other end
    */
    public void informNoRecipient() {
        try {
            this.writer.writeUTF("NO CLIENT");
            this.writer.writeUTF("Message from server: No other client connected.");
        }
        catch (IOException e) {
            System.out.println("Error in notifying client of disconnection."); 
        }
    }

    /*
        Informs client that their recipient has disconnected
    */
    public void informDisconnect() {
        try {
            this.writer.writeUTF("DISCONNECT");
            this.writer.writeUTF("Message from server: Client: " + this.dest + " has disconnected."); 
            this.server.addLog(new Log(this.dest, "DISCONNECTED")); 
        }
        catch (IOException e) {
            System.out.println("Error in notifying client."); 
        }
    }

    /*
        Informs client that a connection with another client has been established 
    */
    public void informConnection() {
        try {
            this.writer.writeUTF("CONNECTION ESTABLISHED"); 
            this.writer.writeUTF("Message from server: Client: " + this.dest + " is now connected. \n You may begin chatting.");
        }
        catch (IOException e) {
            System.out.println("Error in notifying client."); 
        }
    }

    /*
        Returns String-form of source and destination attributes
    */
    public String toString() {
        return "Connection:\n Source:" + this.source + '\n' + "Destination:" + this.dest; 
    }

    /*
        Performs I/O closing and server calls when connection is closed
    */
    private void cleanup() {
        this.server.addLog(new Log(this.source, "LOGOUT")); 
        this.server.removeConnection(this); 
        try {
            //Close IO streams
            socket.close(); 
            reader.close(); 
            writer.close(); 
        } 
        catch (IOException e) {
            System.out.println(e); 
        }
    }

    @Override
    public void run() {
        try {
            String msg;
            // Check what activity the user did
            while (!(msg = reader.readUTF()).equals("LOGOUT")) {
                if (msg.equals("MESSAGE")) {
                    msg = reader.readUTF();
                    System.out.println("Received message from GUI: " + msg); 
                    this.server.sendMessage(this.dest, msg); 
                    this.server.addLog(new Log(this.source, "MESSAGE", this.dest, false, msg)); 
                }
                else if (msg.equals("FILE")) {
                    try {
                        byte[] sizeAr = new byte[4];
                        this.reader.read(sizeAr);
                        int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

                        byte[] imageAr = new byte[size];
                        int read = 0;
                        try{
                            while(read < size)
                            {
                                read += this.reader.read(imageAr,read,size-read); 
                            }
                        }
                        catch (EOFException e){
                            e.printStackTrace();
                        }
                        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
                        this.server.sendMessage(this.dest, image); 
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            //Perform final cleanup before closing
            this.cleanup(); 
        } catch (Exception e) {
            e.printStackTrace(); // Uncomment this if you want to look at the error thrown
        } finally {
            System.out.println("Server: Client " + socket.getRemoteSocketAddress() + " has disconnected");
        }
    }

}