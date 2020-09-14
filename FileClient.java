/*
    LUA, Neil Matthew N.
    TANTING, Kurt Bradley D.
*/

import java.net.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.nio.ByteBuffer;
public class FileClient
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
        String sServerAddress = args[0];
        int nPort = Integer.parseInt(args[1]);
        try
        {
            Socket clientEndpoint = new Socket(sServerAddress, nPort);
            System.out.println("Client: Connecting to server at " +
            clientEndpoint.getRemoteSocketAddress() + "\n");
            System.out.println("Client: Connected to server at " +
            clientEndpoint.getRemoteSocketAddress() + "\n");
            //DataInputStream for Reading
            DataInputStream reader = new
            DataInputStream(clientEndpoint.getInputStream());

            byte[] sizeAr = new byte[4];
            reader.read(sizeAr);
            int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

            byte[] imageAr = new byte[size];
            int read = 0;
            while(read<size)
            {
                read += reader.read(imageAr,read,size-read); 
            }
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));

            System.out.print("The server receives the image and before I pass it to the client, I check if it's same so: ");
            System.out.println(bufferedImagesEqual(image, ImageIO.read(new File("C:\\Users\\Neil Matthew Lua\\Desktop\\DP.jpg"))));

            ImageIO.write(image, "jpg", new File("C:\\Users\\Neil Matthew Lua\\Desktop\\DP3.jpg"));
            
            System.out.println("Client: Downloaded file \"Recieved.txt\"\n");
            clientEndpoint.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            System.out.println("Client: Connection is terminated.");
        }
    }
}
// /*
//     LUA, Neil Matthew N.
//     TANTING, Kurt Bradley D.
// */

// import java.net.*;
// import java.io.*;
// public class FileClient
// {
//     public static void main(String[] args)
//     {
//         String sServerAddress = args[0];
//         int nPort = Integer.parseInt(args[1]);
//         try
//         {
//             Socket clientEndpoint = new Socket(sServerAddress, nPort);
//             System.out.println("Client: Connecting to server at " +
//             clientEndpoint.getRemoteSocketAddress() + "\n");
//             System.out.println("Client: Connected to server at " +
//             clientEndpoint.getRemoteSocketAddress() + "\n");
//             //DataInputStream for Reading
//             DataInputStream disReader = new
//             DataInputStream(clientEndpoint.getInputStream());

//             //Since file size is unknown, set random integer size
//             byte[] contents = new byte[10000];
        
//             //Initialize FileOutputStream for the file name
//             FileOutputStream fos = new FileOutputStream("Received.txt");
            
//             //Hold the total number of bytes read
//             int bytesRead = 0; 
            
//             //While there's still content to be read, continue to write inside the file
//             while((bytesRead = disReader.read(contents))!=-1)
//                 fos.write(contents, 0, bytesRead); 

//             fos.flush(); 
            
//             System.out.println("Client: Downloaded file \"Recieved.txt\"\n");
//             clientEndpoint.close();
//         }
//         catch (Exception e)
//         {
//             e.printStackTrace();
//         }
//         finally
//         {
//             System.out.println("Client: Connection is terminated.");
//         }
//     }
// }