/*
    LUA, Neil Matthew N.
    TANTING, Kurt Bradley D.
*/

import java.net.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.nio.ByteBuffer;
public class FileServer
{
    public static boolean bufferedImagesEqual(BufferedImage img1, BufferedImage img2) {
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

    public static void main(String[] args)
    {
        int nPort = Integer.parseInt(args[0]);
        System.out.println("Server: Listening on port " + args[0] + "...\n");
        ServerSocket serverSocket;
        Socket serverEndpoint;

        try
        {
            //Initialize the sockets
            serverSocket = new ServerSocket(nPort);
            serverEndpoint = serverSocket.accept();
            System.out.println("Server: Listening to port: " +
            serverEndpoint.getRemoteSocketAddress() + "....\n");
            DataOutputStream dos = new
            DataOutputStream(serverEndpoint.getOutputStream());

            try {
                BufferedImage buffImage = ImageIO.read(new File("C:\\Users\\Neil Matthew Lua\\Desktop\\DP.jpg"));
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(buffImage, "jpg", byteArrayOutputStream);
    
                byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
    
                System.out.print("Just before I send, I check if it's same so: ");
                System.out.println(bufferedImagesEqual(buffImage, ImageIO.read(new File("C:\\Users\\Neil Matthew Lua\\Desktop\\DP.jpg"))));
    
                // write on the output stream
                dos.write(size);
                dos.write(byteArrayOutputStream.toByteArray());
                dos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // System.out.println("Server: Sending file \"Download.txt\" (" + size + " bytes)\n");
            
            // dosWriter.flush(); 
            serverEndpoint.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            System.out.println("Server: Connection is terminated.");
        }
    }
}
// /*
//     LUA, Neil Matthew N.
//     TANTING, Kurt Bradley D.
// */

// import java.net.*;
// import java.io.*;
// public class FileServer
// {
//     public static void main(String[] args)
//     {
//         int nPort = Integer.parseInt(args[0]);
//         System.out.println("Server: Listening on port " + args[0] + "...\n");
//         ServerSocket serverSocket;
//         Socket serverEndpoint;

//         try
//         {
//             //Initialize the sockets
//             serverSocket = new ServerSocket(nPort);
//             serverEndpoint = serverSocket.accept();
//             System.out.println("Server: Listening to port: " +
//             serverEndpoint.getRemoteSocketAddress() + "....\n");

//             //File object
//             File file = new File("Download.txt");
//             FileInputStream fis = new FileInputStream(file);
//             BufferedInputStream bis = new BufferedInputStream(fis); 

//             //DataOutputStream to write
//             DataOutputStream dosWriter = new
//             DataOutputStream(serverEndpoint.getOutputStream());

//             //Holder of the size of the file
//             byte[] contents;
//             long fileLength = file.length(); 
//             long current = 0;
            
//             int size = 10000;
//             //Calculate the size of the file
//             // while(current != fileLength){ 
//                 if(fileLength - current >= size)
//                     current += size;    
//                 else{ 
//                     size = (int)(fileLength - current); 
//                     current = fileLength;
//                 } 
//             // } 

//             contents = new byte[size]; 
//             //Read the file from 0 to the last byte of the file, place it in contents
//             fis.read(contents, 0, size); 
//             dosWriter.write(contents);
//             System.out.println("Server: Sending file \"Download.txt\" (" + size + " bytes)\n");
            
//             dosWriter.flush(); 
//             serverEndpoint.close();
//         }
//         catch (Exception e)
//         {
//             e.printStackTrace();
//         }
//         finally
//         {
//             System.out.println("Server: Connection is terminated.");
//         }
//     }
// }