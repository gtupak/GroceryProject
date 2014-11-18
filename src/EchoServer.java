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
				if (msg.toString().length() >= 6){
					if (msg.toString().substring(0, 6).equals("#login")){
						client.sendToClient("SERVER> You are already logged in.");
						return;
					}
					else {
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
		catch (Exception e){ // Only if it's the first time that the clients connects
			try {
				if (msg.toString().substring(0, 6).equals("#login")){
					System.out.println("Message received " + msg + " from " + client.getInetAddress());
					client.setInfo("loginID", msg.toString().trim().substring(7, msg.toString().trim().length()));
					client.setInfo("isLoggedIn", true);
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
	//		if (msg.toString().length() >= 6){
	//			if (msg.toString().substring(0, 6).equals("#login")){
	//				try{//There will only be an exception caught when the user first logs in
	//					if (client.getInfo("isLoggedIn").equals(true)){
	//						client.sendToClient("SERVER> You are already logged in.");
	//						return;
	//					}
	//				}
	//				catch (Exception e){
	//					client.setInfo("loginID", msg.toString().substring(7, msg.toString().length()));
	//					client.setInfo("isLoggedIn", true);
	//					return;
	//				}
	//			}
	//		}
	//		if (msg != null){
	//			System.out.println("Message received: " + msg + " from " + client);
	//			this.sendToAllClients(client.getInfo("loginID") + "> " + msg);
	//		}
	//	}

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
