
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import client.ChatClient;
import common.ChatIF;
import common.Entry;

public class ServerTest {

	private static  EchoServer server;
	private static ChatClient client;

	public static void main(String[] args) throws InterruptedException{
		server = new EchoServer(server.DEFAULT_PORT, new ChatIF(){

			@Override
			public void display(String message) {
				// TODO Auto-generated method stub

			}

			@Override
			public void displayGUI(ArrayList<Entry> msg) {
				// TODO Auto-generated method stub

			}

		});

		try 
		{
			server.listen(); //Start listening for connections
		} 
		catch (Exception ex) 
		{
			System.out.println("ERROR - Could not listen for clients!");
		}
		try {
			client = new ChatClient("localhost", server.DEFAULT_PORT, new ChatIF(){

				@Override
				public void display(String message) {
					//					System.out.println("Message received:" + message);
				}

				@Override
				public void displayGUI(ArrayList<Entry> msg) {
					Iterator<Entry> it = msg.iterator();
					Entry en;
					System.out.println();
					System.out.println("Current entries in database: ");
					while (it.hasNext()){
						en = it.next(); 
						System.out.println("ID: " + en.getId() 
								+ "; Description: " + en.getDescription() 
								+ "; Creator: " + en.getCreator()
								+ "; Is Checked: " + en.isChecked()
								+ "; Checker: " + en.getChecker()
								);
					}
				}

			}, "testCase");

			client.openConnection();

		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("---TEST 1: Adding entires---");
		client.handleMessageFromClientUI("#login " + client.getLogin());
		client.handleMessageFromClientUI("#sql INSERT INTO entries VALUES(0, 'Item list 0', '"+ client.getLogin()+"', null, false)");
		client.handleMessageFromClientUI("#sql INSERT INTO entries VALUES(1, 'Item list 1', '"+ client.getLogin()+"', null, false)");
		client.handleMessageFromClientUI("#sql INSERT INTO entries VALUES(2, 'Item list 2', '"+ client.getLogin()+"', null, false)");
		Thread.sleep(2000);

		System.out.println();
		System.out.println("---TEST 2: Deleting entry with ID 0---");
		client.handleMessageFromClientUI("#sql DELETE FROM entries WHERE id=0");
		Thread.sleep(2000);
		
		System.out.println();
		System.out.println("---TEST 3: Editing entry with ID 1---");
		client.handleMessageFromClientUI("#sql UPDATE entries SET entry='Item list 1 --#-- EDITED' WHERE id=1");
		Thread.sleep(2000);
			
		System.out.println();
		System.out.println("---TEST 4: Checking entry with ID 2---");
		client.handleMessageFromClientUI("#sql UPDATE entries SET checked=TRUE, checker='" 
				+ client.getLogin() + "' WHERE id=2");
		Thread.sleep(2000);

		System.out.println();
		System.out.println("---TEST 5: Unchecking entry with ID 2---");
		client.handleMessageFromClientUI("#sql UPDATE entries SET checked=FALSE, checker=null WHERE id=2");

	}
}
