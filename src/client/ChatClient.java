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

package client;

import ocsf.client.*;
import common.*;

import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
	//Instance variables **********************************************

	/**
	 * The interface type variable.  It allows the implementation of 
	 * the display method in the client.
	 */
	ChatIF clientUI; 
	private String loginID; // ***** Added for E51

	//Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host The server to connect to.
	 * @param port The port number to connect on.
	 * @param clientUI The interface type variable.
	 */

	// ****** Changed for E51
	public ChatClient(String host, int port, ChatIF clientUI, String loginID) 
			throws IOException 
	{
		super(host, port); //Call the superclass constructor
		this.clientUI = clientUI;
		this.loginID = loginID;
		openConnection();
		//    sendToServer("#login " + loginID);
	}


	//Instance methods ************************************************

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	public void handleMessageFromServer(Object msg) 
	{
		clientUI.display(msg.toString());
	}

	/**
	 * This method handles all data coming from the UI            
	 *
	 * @param message The message from the UI.    
	 */
	public void handleMessageFromClientUI(String message)
	{

		try
		{
			sendToServer(message);
		}
		catch(IOException e)
		{
			clientUI.display
			("Could not send message to server.  Terminating client.");
			quit();
		}
	}

	/**
	 * Changed for E49
	 */
	protected void connectionException(Exception exception){
		clientUI.display("WARNING: Abnormal termination of connection.");
	}
	
	/**
	 * This method terminates the client.
	 */
	public void quit()
	{
		try
		{
			closeConnection();
		}
		catch(IOException e) {}
		System.exit(0);
	}

	//***** Get and Set methods for userID
	public void setLogin(String login) {loginID = login;}

	public String getLogin() {return loginID;}

}
//End of ChatClient class
