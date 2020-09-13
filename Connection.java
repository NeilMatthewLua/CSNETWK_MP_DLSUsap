import java.io.*;
import java.net.*;

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
        Returns String-form of source and destination attributes
    */
    public String toString() {
        return "Connection:\n Source:" + this.source + '\n' + "Destination:" + this.dest; 
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

                }
            }
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