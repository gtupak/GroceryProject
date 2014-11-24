import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import common.ChatIF;
import common.Entry;

/**Assignment 3 - Lab 2
*
* Name: Gabriel Tapuc
* Student #: 7269083
* 
* Name: Christine Kandalaft
* Student #: 7216942
*/

/**
 * This class is for the question E50.
 */

public class ServerConsole implements ChatIF {

	EchoServer server;
	final public static int DEFAULT_PORT = 5555;

	/**The constructor starts the server.*/
	public ServerConsole(int port)
	{
		server= new EchoServer(port, this);
		try 
		{
			server.listen(); //Start listening for connections
		} 
		catch (Exception ex) 
		{
			System.out.println("ERROR - Could not listen for clients!");
		}
	}

	@Override
	public void display(String message) {
		System.out.println("SERVER MSG> " + message);
	}

	/**
	 * This method waits for input from the console. <br>Once it is 
	 * received, it sends it to the all the clients and to the end-user
	 * of the server.
	 */
	public void accept(){
		try
		{
			BufferedReader fromConsole = 
					new BufferedReader(new InputStreamReader(System.in));
			String message;
			while (true) 
			{
				message = fromConsole.readLine();
				if (message.equals("#quit")){
					System.out.println("The server will quit gracefully.");
					server.close();
				}
				else if(message.equals("#stop")){
					server.stopListening();
				}
				else if (message.equals("#close")){
					server.stopListening();
					server.close();
				}
				else if (message.equals("#start")){
					if (!server.isListening()){
						System.out.println("Starting the system.");
						server.listen();
					}else{
						System.out.println("The system is already started.");
					}
				}
				else if (message.equals("#getPort")){
					System.out.println("The port is "+ server.getPort());
				}
				else if (message.length() >= 8 && message.substring(0, 8).equals("#setport")){
					if (!server.isListening()){
						try {
							String port = message.substring(message.indexOf(' ') + 1,
									message.length());
							port = port.trim();
							int iPort = Integer.valueOf(port);
							server.setPort(iPort);
							System.out.println("Port changed to: " + port);
						} catch (Exception e) {
							System.out.println("Invalid port.");
						}
					} 
					else {
						System.out.println("Please logoff before you set a new port");
					}
				}
				else{
					server.handleMessageFromServerUI(message);
				}
			}
		}
		catch(Exception e){
			System.out.println
			("Unexpected error while reading from console!");
		}
	}

	public static void main(String[] args) throws IOException{
		int port = DEFAULT_PORT;

		try
		{
			port = Integer.parseInt(args[0]);
		}
		catch(ArrayIndexOutOfBoundsException e){}
		catch(NumberFormatException e){
			System.out.println("Could not parse port. Please enter an integer or leave blank for port# 5555.");
			System.out.println();
			System.out.println("Program will set up on default port #5555.");
		}
		ServerConsole console = new ServerConsole(port);
		console.accept();
	}
	
	
	/**
	 * Currently not used.
	 */
	@Override
	public void displayGUI(ArrayList<Entry> msg) {
		// TODO Auto-generated method stub
		
	}
}


