import java.io.*;
import java.net.*;

public class Client
{
    private String strAddress;
    private int nPort;

    public Client (String strAddress, int nPort){
        this.strAddress = strAddress;
        this.nPort = nPort;
    }

    /**
     * Getter for the IP Address of the server
     * @return Servers IP Address 
     */
    public String getAddress(){
        return this.strAddress;
    }

    /**
     * Getter for the port number
     * @return Port Number
     */
    public int getPort(){
        return this.nPort;
    }

    /**
     * Sends a message to the server towards to the other client
     * @param strMessage Message of the sender 
     */
    public void sendMessage(String strMessage) throws UnknownHostException, IOException{
         // establish the connection
        Socket s = new Socket(this.strAddress, this.nPort);

        // obtaining input and out streams
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());

        // sendMessage thread
        Thread sendMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {
                try {
                    // write on the output stream
                    dos.writeUTF(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        sendMessage.start();

        // // readMessage thread
        // Thread readMessage = new Thread(new Runnable()
        // {
        //     @Override
        //     public void run() {
        //         try {
        //             // read the message sent to this client
        //             String msg = dis.readUTF();
        //             System.out.println(msg);
        //             dis.close();
        //         } catch (IOException e) {
        //             e.printStackTrace();
        //         }
        //     }
        // });
        // readMessage.start();
    }

    /**
     * Sends an image the server towards to the other client
     * @param image Image of the sender 
     */
    public void sendMessage(File image) throws UnknownHostException, IOException{
         // establish the connection
        Socket s = new Socket(this.strAddress, this.nPort);

        // obtaining input and out streams
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());

        // sendMessage thread
        Thread sendMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {
                try {
                        BufferedImage image = ImageIO.read(new File(image.getPath()));
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        ImageIO.write(image, "jpg", byteArrayOutputStream);

                        byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
                        dos.write(size);
                        dos.write(byteArrayOutputStream.toByteArray());
                        dos.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        sendMessage.start();

        // // readMessage thread
        // Thread readMessage = new Thread(new Runnable()
        // {
        //     @Override
        //     public void run() {
        //         try {
        //             // read the message sent to this client
        //             String msg = dis.readUTF();
        //             System.out.println(msg);
        //             dis.close();
        //         } catch (IOException e) {
        //             e.printStackTrace();
        //         }
        //     }
        // });
        // readMessage.start();
    }

    /**
     * Sends a message and image the server towards to the other client
     * @param strMessage Message of the sender 
     * @param image Image of the sender 
     */
    public void sendMessage(File image) throws UnknownHostException, IOException{
         // establish the connection
        Socket s = new Socket(this.strAddress, this.nPort);

        // obtaining input and out streams
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());
        
        // sendMessage thread
        Thread sendMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {
                try {
                    // write on the output stream
                    dos.writeUTF(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        sendMessage.start();

        // sendMessage thread
        Thread sendMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {
                try {
                        BufferedImage image = ImageIO.read(new File(image.getPath()));
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        ImageIO.write(image, "jpg", byteArrayOutputStream);

                        byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
                        dos.write(size);
                        dos.write(byteArrayOutputStream.toByteArray());
                        dos.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        sendMessage.start();

        // // readMessage thread
        // Thread readMessage = new Thread(new Runnable()
        // {
        //     @Override
        //     public void run() {
        //         try {
        //             // read the message sent to this client
        //             String msg = dis.readUTF();
        //             System.out.println(msg);
        //             dis.close();
        //         } catch (IOException e) {
        //             e.printStackTrace();
        //         }
        //     }
        // });
        // readMessage.start();
    }
    

    // public static void main(String args[]) throws UnknownHostException, IOException
    // {
    //     String sServerAddress = args[0];
    //     int nPort = Integer.parseInt(args[1]);
    //     String sender = args[2];
    //     String message = sender + " " + args[3];
    //     // establish the connection
    //     Socket s = new Socket(sServerAddress, nPort);

    //     // obtaining input and out streams
    //     DataInputStream dis = new DataInputStream(s.getInputStream());
    //     DataOutputStream dos = new DataOutputStream(s.getOutputStream());
    //     System.out.println(sender + ": Connecting to server at " + s.getRemoteSocketAddress());
    //     System.out.println(sender + ": Connected to server at " + s.getRemoteSocketAddress());

    //     // sendMessage thread
    //     Thread sendMessage = new Thread(new Runnable()
    //     {
    //         @Override
    //         public void run() {
    //             try {
    //                 // write on the output stream
    //                 dos.writeUTF(message);
    //             } catch (Exception e) {
    //                 e.printStackTrace();
    //             }
    //         }
    //     });

    //     // readMessage thread
    //     Thread readMessage = new Thread(new Runnable()
    //     {
    //         @Override
    //         public void run() {
    //             try {
    //                 // read the message sent to this client
    //                 String msg = dis.readUTF();
    //                 System.out.println(msg);
    //                 dis.close();
    //             } catch (IOException e) {
    //                 e.printStackTrace();
    //             }
    //         }
    //     });
    //     sendMessage.start();
    //     readMessage.start();
    // }
}