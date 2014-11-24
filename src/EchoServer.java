// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

/**Assignment 3 - Lab 2
 *
 * Name: Gabriel Tapuc
 * Student #: 7269083
 * 
 * Name: Christine Kandalaft
 * Student #: 7216942
 */

import ocsf.server.*;
import common.*;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
	//Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;
	ChatIF serverUI;
	static Connection database;

	//Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 */
	public EchoServer(int port) 
	{
		super(port);
	}

	/**
	 * **** Changed for E50
	 * Sets up the server with a port and a UI.
	 */ 
	public EchoServer(int port, ChatIF serverUI){
		super(port);
		this.serverUI = serverUI;
		// Set up properties file
		Properties props = new Properties();
		FileInputStream in = null;

		try {
			// Load properties and send it to getConnection()
			in = new FileInputStream("database.properties");
			props.load(in);
			getConnection(props);


		} catch (URISyntaxException | SQLException | IOException e) {
			Logger lgr = Logger.getLogger(EchoServer.class.getName());
			lgr.log(Level.WARNING, e.getMessage(), e);
			e.printStackTrace();
		}
	}


	//Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient
	(Object msg, ConnectionToClient client)
	{
		// **** Changed for E51
		try{
			if (client.getInfo("isLoggedIn").equals(true) && msg != null){
				if (msg.toString().length() >= 4){
					if (msg.toString().substring(0, 4).toUpperCase().equals("#SQL")){
						// Extract SQL command from message string
						String stm = msg.toString().substring(5, msg.toString().length());
						
						// Initialize SQL statement
						try {
							ResultSet rs = null;
							System.out.println("---> SQL command to be executed: " + stm);
							PreparedStatement pst = database.prepareStatement(stm);
							pst.executeUpdate();
							
							pst = database.prepareStatement("SELECT * FROM entries");
							rs = pst.executeQuery();
							
							ArrayList<Entry> entries = new ArrayList<Entry>();
							while (rs.next()){
								entries.add(new Entry(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getBoolean(5)));
							}
							this.sendToAllClients(entries);
							
						} catch (SQLException e) {
							Logger lgr = Logger.getLogger(EchoServer.class.getName());
				            lgr.log(Level.SEVERE, e.getMessage(), e);
						}
					}
				}
				if (msg.toString().length() >= 6){
					if (msg.toString().substring(0, 6).equals("#login")){
						client.sendToClient("SERVER> You are already logged in.");
						return;
					}
					else 
					{
						System.out.println("Message received: " + msg + " from " + client.getInfo("loginID"));
						this.sendToAllClients(client.getInfo("loginID") + "> " + msg);
					}
				}
				else{
					System.out.println("Message received: " + msg + " from " + client.getInfo("loginID"));
					this.sendToAllClients(client.getInfo("loginID") + "> " + msg);
				}
			}

		}
		catch (Exception e){ // Occurs only if it's the first time that the clients connect
			try {
				if (msg.toString().substring(0, 6).equals("#login")){
					System.out.println("Message received " + msg + " from " + client.getInetAddress());
					client.setInfo("loginID", msg.toString().trim().substring(7, msg.toString().trim().length()));
					client.setInfo("isLoggedIn", true);
					
					PreparedStatement pst = database.prepareStatement("SELECT * FROM entries");
					ResultSet rs = pst.executeQuery();
					
					ArrayList<Entry> entries = new ArrayList<Entry>();
					while (rs.next()){
						entries.add(new Entry(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getBoolean(5)));
					}
					client.sendToClient(entries);
					
					sendToAllClients(client.getInfo("loginID") + " has logged on.");
					System.out.println(client.getInfo("loginID") + " has logged on.");
					return;
				}
				else{
					client.sendToClient("ERROR> First message to server must begin with '#login'.");
					client.close();
				}
			}
			catch (Exception e1){
				try{
					client.sendToClient("ERROR: First message to server must begin with '#login'.");
					client.close();
				}
				catch(Exception e2){}
			}
		}
	}

	/**
	 * **** Changed for E50
	 * This method handles any messages from the server UI.*/
	public void handleMessageFromServerUI(Object msg){
		serverUI.display(msg.toString());
		sendToAllClients(msg);
	}

	/**
	 * This method overrides the one in the superclass.  Called
	 * when the server starts listening for connections.
	 */
	protected void serverStarted()
	{
		System.out.println
		("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass.  Called
	 * when the server stops listening for connections. ***** Changed for 50
	 */
	protected void serverStopped()
	{
		System.out.println
		("Server has stopped listening for connections.");
		sendToAllClients("WARNING - The server has stopped listening for connections.");
	}

	//**** Changed for E49
	protected void clientConnected(ConnectionToClient client) {
		System.out.println("A new client is attempting to connect to the server.");
	}

	// **** Changed for E49
	synchronized protected void clientException(
			ConnectionToClient client, Throwable exception) {
		System.out.println(client.getInfo("loginID") + " has disconnected.");
		sendToAllClients(client.getInfo("loginID") + " has disconnected.");
	}

	/**
	 * Method used to connect to the database, based on the informations in the
	 * database.properties file.
	 * 
	 * @param props Properties parameter, that contains info about the database
	 * @return
	 * @throws URISyntaxException
	 * @throws SQLException
	 */
	private static boolean getConnection(Properties props) throws URISyntaxException, SQLException {
		//URI dbUri = new URI(System.getenv("DATABASE_URL"));
		String url = props.getProperty("db.url");
		String user = props.getProperty("db.user");
		String passwd = props.getProperty("db.passwd");
		database = DriverManager.getConnection(url, user, passwd);
		return (database != null);
	} 

	//Class methods ***************************************************

	/**
	 * This method is responsible for the creation of 
	 * the server instance (there is no UI in this phase).
	 *
	 * @param args[0] The port number to listen on.  Defaults to 5555 
	 *          if no argument is entered.
	 */
	public static void main(String[] args) 
	{
		int port = 0; //Port to listen on

		try
		{
			port = Integer.parseInt(args[0]); //Get port from command line
		}
		catch(Throwable t)
		{
			port = DEFAULT_PORT; //Set port to 5555
		}

		EchoServer sv = new EchoServer(port);

		try 
		{
			sv.listen(); //Start listening for connections
		} 
		catch (Exception ex) 
		{
			System.out.println("ERROR - Could not listen for clients!");
		}
	}
}
//End of EchoServer class
