import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.nio.ByteBuffer;
import javafx.stage.FileChooser;

/*
	ChatServer class 
	@attribute clients List of connections handled by the server
	@attribute logs list of logs between the two users and the server 
*/
public class Server {
	private ArrayList<Connection> clients;
	private ArrayList<Thread> clientListeners;
	private LogList logs;
	Scanner scan;
	private AtomicBoolean running = new AtomicBoolean(true);
	// isServerPrompt to ensure only one server close prompt is asked
	private AtomicBoolean isServerPromptActive = new AtomicBoolean(true);

	// Server class constructor
	public Server() {
		scan = new Scanner(System.in);
		this.clients = new ArrayList<Connection>();
		this.clientListeners = new ArrayList<Thread>();
		this.logs = new LogList();
	}

	/*
	 * Starts the server and listens on the port provided. Loops until both clients
	 * have disconnected.
	 * 
	 * @param nPort Port number the server will use
	 */
	public void start(int nPort, Server s) {
		final ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(nPort);
			System.out.println("Server: Listening on port " + nPort + "...");
			System.out.println("Server address:" + serverSocket.getInetAddress());
			while (this.running.get()) {
				if (clientListeners.size() < 2) {
					Thread acceptClient = new Thread("Listen to client") {
						public void run() {
							try {
								Socket serverEndpoint = serverSocket.accept();
								String clientAddress = serverEndpoint.getRemoteSocketAddress().toString();
								System.out.println("Server: New client connected: " + clientAddress);
								logs.add(new Log(clientAddress, "LOGIN"));

								// Add client to the pool of threads
								Connection client = new Connection(serverEndpoint, clientAddress, s);
								s.clients.add(client);
								client.start();
								if (clients.size() == 2)
									s.connectClients();
								else
									client.informNoRecipient();
							} catch (IOException e) {
								if (s.running.get())
									System.out.println("Error in accepting client.");
							}
						}
					};
					acceptClient.start();
					clientListeners.add(acceptClient);
				}

			}
			// Close server
			try {
				serverSocket.close();
				System.out.println("Server closed.");
			} catch (Exception e) {
				System.out.println("Exception closing the server " + e);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Adds the log to the log list
	 * 
	 * @param l Log to be added to the list
	 */
	public void addLog(Log l) {
		this.logs.add(l);
	}

	public boolean getRunning() {
		return this.running.get();
	}

	/*
	 * Removes specified object from client list and informs other client of
	 * disconnect
	 * 
	 * @param src Connection object to be removed
	 */
	public void removeConnection(Connection src) {
		// Remove a client listener to free up a space for another listener
		int index = this.clientListeners.size() - 1;
		this.clientListeners.get(index);
		this.clientListeners.remove(index);

		// Remove the connection
		String address = src.getDest();
		this.clients.remove(src);
		for (Connection c : this.clients)
			if (c.getSource().equals(address)) {
				//
				c.informDisconnect();
				// Remove dest address from the other client
				c.setDest(null);
			}
		// Ask user if they want to close the server
		this.promptServerClose();
	}

	/**
	 * Function to prompt user to close server and save logs
	 */
	public void promptServerClose() {
		// If server is still running and if no server prompt has been initiated
		if (this.getRunning() && isServerPromptActive.get()) {
			isServerPromptActive.set(false);
			// Ask the user if they want to save the logs
			System.out.println("Do you wish to close the server? (Y/N)");
			String choiceClose = this.scan.nextLine();
			// Close the server
			if (choiceClose.equals("Y")) {
				// Stop accepting logs
				this.logs.setAccepting(false);
				// Ask user if they want to save the logs
				System.out.println("Do you wish to save the logs? (Y/N)");
				String choiceSave = this.scan.nextLine();
				if (choiceSave.equals("Y"))
					this.logs.saveLogs();

				// Stop accepting client listeners
				this.running.set(false);
				// Close all client listeners
				for (Thread t : clientListeners) {
					t.interrupt();
				}
				// Close all client connections
				for (Connection c : clients) {
					c.informServerClose();
					c.cleanup();
					c.interrupt();
				}
			} else {
				System.out.println("Server will continue running.");
			}
			isServerPromptActive.set(true);
		}
	}

	/*
	 * Adds each client as the destination of the other
	 */
	private void connectClients() {
		String src1 = this.clients.get(0).getSource();
		String src2 = this.clients.get(1).getSource();
		// Set the source of a client as the destination of the other
		this.clients.get(0).setDest(src2);
		this.clients.get(1).setDest(src1);
		// Inform each client that a connection has been established
		this.clients.get(0).informConnection();
		this.clients.get(1).informConnection();
	}

	/*
	 * Sends a message to the specified recipient
	 * 
	 * @param recipient The recipient of the message
	 * 
	 * @param message Message to send
	 */
	public void sendMessage(String recipient, String message) {
		// Loop to find the recipient in the list
		for (Connection c : this.clients)
			if (c.getSource().equals(recipient))
				c.writeMessage(message);
	}

	/*
	 * Sends an Image to the specified recipient
	 * 
	 * @param recipient The recipient of the message
	 * 
	 * @param image Image to send
	 */
	public void sendMessage(String recipient, BufferedImage image, String file_type) {
		// Loop to find the recipient in the list
		for (Connection c : this.clients)
			if (c.getSource().equals(recipient))
				c.writeMessage(image, file_type);

	}

	/*
	 * Sends an Text File to the specified recipient
	 * 
	 * @param recipient The recipient of the message
	 * 
	 * @param message TextFile to send
	 */
	public void sendMessage(String recipient, String message, String file_type) {
		// Loop to find the recipient in the list
		for (Connection c : this.clients)
			if (c.getSource().equals(recipient))
				c.writeMessage(message, file_type);
	}

	public static void main(String[] args) {
		int nPort;
		if (args.length != 0)
			nPort = Integer.parseInt(args[0]);
		else
			nPort = 4000;
		Server s = new Server();
		s.start(nPort, s);
	}
}