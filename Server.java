import java.net.*;
import java.io.*;
import java.util.ArrayList; 

/*
	ChatServer class 
	@attribute clients List of connections handled by the server
	@attribute logs list of logs between the two users and the server 
*/
public class Server
{
	private ArrayList<Connection> clients; 
	private ArrayList<Log> logs;  

	//Server class constructor 
	public Server() {
		this.clients = new ArrayList<Connection>();
		this.logs = new ArrayList<Log>();
	}
	
	/*
		Starts the server and listens on the port provided. Loops until both clients have disconnected.
		@param nPort Port number the server will use 
	*/
	public void start(int nPort) {
		ServerSocket serverSocket;
		Socket serverEndpoint;
		try 
		{
			serverSocket = new ServerSocket(nPort);
			boolean running = true; 
			while(running) {
				//Accept clients until two 
				if (clients.size() < 2) {
					System.out.println("Server: Listening on port " + nPort + "...");
					serverEndpoint = serverSocket.accept();
					String clientAddress = serverEndpoint.getRemoteSocketAddress().toString();
					System.out.println("Server: New client connected: " + clientAddress);
					logs.add(new Log(clientAddress, "LOGIN"));

					//Add client to the pool of threads
					Connection client = new Connection(serverEndpoint, clientAddress, this); 
					clients.add(client); 
					client.start();
					
					if (clients.size() == 2) 
						this.connectClients(); 
					else 
						client.informNoRecipient(); 
				}	
			}
			
			//Close IO streams when closing the server 
			try {
				serverSocket.close();
			}
			catch(Exception e) {
				System.out.println("Exception closing the server and clients: " + e);
			}
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

	/*
		Adds the log to the log list 
		@param l Log to be added to the list
	*/
	public void addLog(Log l) {
		this.logs.add(l); 
	}

	/*
		Removes specified object from client list and informs other client of disconnect 
		@param src Connection object to be removed
	*/
	public void removeConnection(Connection src) {
		String address = src.getDest(); 
		this.clients.remove(src); 
		System.out.println(clients.size()); 
		for (Connection c : this.clients) 
			if (c.getSource().equals(address)) {
				//
				c.informDisconnect(); 
				//Remove dest address from the other client
				c.setDest(null); 
			}
	} 

	/*
		Adds each client as the destination of the other
	*/
	private void connectClients() {
		String src1 = this.clients.get(0).getSource(); 
		String src2 = this.clients.get(1).getSource();
		//Set the source of a client as the destination of the other
		this.clients.get(0).setDest(src1);
		this.clients.get(1).setDest(src2); 
		//Inform each client that a connection has been established
		this.clients.get(0).informConnection(); 
		this.clients.get(1).informConnection(); 
	}

	/*
		Sends a message to the specified recipient 
		@param recipient The recipient of the message
		@param message Message to send 
	*/
	public void sendMessage(String recipient, String message) {
		System.out.println("Sending " + message + " to " + recipient); 
		//Loop to find the recipient in the list
		for (Connection c : this.clients) 
			if (c.getSource().equals(recipient))
				c.writeMessage(message); 
	}

	public static void main(String[] args) {
		int nPort; 
		if (args.length != 0) 
			nPort = Integer.parseInt(args[0]);
		else 
			nPort = 4000; 
		Server s = new Server(); 
		s.start(nPort); 
	}
}