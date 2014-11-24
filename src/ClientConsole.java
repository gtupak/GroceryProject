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

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import client.*;
import common.*;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ServerConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version July 2000
 */
public class ClientConsole implements ChatIF 
{
	//Class variables *************************************************

	/**
	 * The default port to connect on.
	 */
	final public static int DEFAULT_PORT = 5555;

	//Instance variables **********************************************

	/**
	 * The instance of the client that created this ConsoleChat.
	 */
	static ChatClient client;


	//Constructors ****************************************************

	/**
	 * Constructs an instance of the ClientConsole UI.
	 *
	 * @param host The host to connect to.
	 * @param port The port to connect on.
	 */
	public ClientConsole(String host, int port, String loginID) 
	{
		try 
		{
			client= new ChatClient(host, port, this, loginID);
		} 
		catch(IOException exception) 
		{
			System.out.println("Error: Can't setup connection!"
					+ " Terminating client.");
			System.exit(1);
		}
	}


	//Instance methods ************************************************

	/**
	 * This method waits for input from the console.  Once it is 
	 * received, it sends it to the client's message handler.
	 */
	public void accept() 
	{
		try
		{
			BufferedReader fromConsole = 
					new BufferedReader(new InputStreamReader(System.in));
			String message;

			while (true) 
			{

				message = fromConsole.readLine();
				// **** Changed for E50 - added if/else statement

				if(message.equals("#quit")){
					System.out.println("The client will now quit gracefully.");
					client.closeConnection();
					client.quit();
				}
				else if (message.equals("#logoff")){
					client.closeConnection();
					System.out.println("Client disconnected from the server.");
				}
				else if (message.length() >= 8 && message.substring(0, 8).equals("#sethost")){
					if (client.isConnected()){
						System.out.println("Cannot set host: you are still connected to the server.");
					}
					else{
						client.setHost(message.substring(9, message.length()));
						System.out.println("Host set to " + client.getHost());
					}
				}
				else if (message.length() >= 8 && message.substring(0, 8).equals("#setport")){
					if (client.isConnected()){
						System.out.println("Cannot set port: you are still connected to the server.");
					}
					else{
						client.setPort(Integer.parseInt(message.substring(9, message.length())));
						System.out.println("Port set to " + client.getPort());
					}
				}
				else if (message.equals("#login")){
					client.openConnection();
					client.handleMessageFromClientUI("#login " + client.getLogin());
				}
				else if (message.equals("#gethost")){
					System.out.println("The host is " + client.getHost());
				}
				else if (message.equals("#getport")){
					System.out.println("The port is " + client.getPort());
				}
				else{
					client.handleMessageFromClientUI(message);
				}
			}
		} 
		catch (Exception ex) 
		{
			System.out.println
			("Unexpected error while reading from console!");
		}
	}

	/**
	 * This method overrides the method in the ChatIF interface.  It
	 * displays a message onto the screen.
	 *
	 * @param message The string to be displayed.
	 */
	public void display(String message) 
	{
		System.out.println(message);
	}

	public static void receiveGUICommand(String cmd){
		client.handleMessageFromClientUI(cmd);
	}

	//Class methods ***************************************************

	/**
	 * This method is responsible for the creation of the Client UI.
	 *
	 * @param args[0] The host to connect to.
	 */
	public static void main(String[] args) 
	{
		String host = "";
		String login = "";
		// **** Changed for E49
		int port = DEFAULT_PORT;  //The port number

		try
		{
			login = args[0];
			host = args[1];
			port = Integer.parseInt(args[2]);
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			if (e.getLocalizedMessage().equals("0")){
				System.out.println("ERROR - No login ID specified Connection aborted.");
				return;
			}
			else if (e.getLocalizedMessage().equals("1")){
				host = "localhost";
			}
		}
		ClientGUI.main(args);
		ClientConsole chat= new ClientConsole(host, port, login);
		System.out.println("> Cannot open connection. Awaiting command.");
		chat.accept();  //Wait for console data
	}


	@Override
	public void displayGUI(ArrayList<Entry> msg) {
		ClientGUI.receiveEntries(msg);
		
	}
}
//End of ConsoleChat class
