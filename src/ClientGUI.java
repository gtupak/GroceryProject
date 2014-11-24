
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import common.Entry;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ClientGUI  {

	private JFrame frame;
	private JTextField input;
	private JList uncheckedList;
	private static ArrayList<Entry> arrayList = new ArrayList<Entry>();
	private static DefaultListModel listModel = new DefaultListModel();
	private static ArrayList<Integer> uniqueID = new ArrayList<Integer>();
	
	/**
	 * Launch the application.
	 */
	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI window = new ClientGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ClientGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 458, 341);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 41, 426, 132);
		frame.getContentPane().add(scrollPane);

		uncheckedList = new JList(listModel);
		uncheckedList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("Mouse clicked on entry nr. " + arrayList.get(uncheckedList.getSelectedIndex()).getId());
			}
		});

		scrollPane.setViewportView(uncheckedList);

		input = new JTextField();
		input.setBounds(12, 254, 282, 34);
		frame.getContentPane().add(input);
		input.setColumns(10);

		JButton btnAddEntry = new JButton("Add Entry");
		
		// When Add Entry button is clicked: send #sql command with unique ID
		btnAddEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Clear the list first
				listModel.clear();

				// Find uniqueID
				int id = 0;
					for (int i = 0; i < uniqueID.size(); i++){
						if (id == uniqueID.get(i)){ // If id is found in the uniqueID list,
							id++;					// Increment id
							i = -1;					// Restart iterating
						}
					}
					uniqueID.add(id); // Adds id to the index
				
				String cmd = "#sql INSERT INTO entries VALUES(" 
						+ id 
						+ ", '" + input.getText() + "'"
						+ ", '" + ClientConsole.client.getLogin() + "'"
						+ ", null, false)"; 
				ClientConsole.receiveGUICommand(cmd);
			}
		});
		btnAddEntry.setBounds(321, 258, 117, 25);
		frame.getContentPane().add(btnAddEntry);

		JLabel lblMyGroceryList = new JLabel("My Grocery List");
		lblMyGroceryList.setBounds(165, 0, 129, 29);
		frame.getContentPane().add(lblMyGroceryList);
	}

	/**
	 * Updates the list with the table received from the database as a ResultSet
	 * @param rs
	 * @throws SQLException
	 */
	public static void receiveEntries(ArrayList<Entry> list){
		listModel.clear();
		arrayList.clear();
		uniqueID.clear();
		Iterator<Entry> it = list.iterator();
		uniqueID = new ArrayList<Integer>();
		Entry en;
		while (it.hasNext()){
			en = it.next();
			uniqueID.add(en.getId());
			arrayList.add(en);
		}
		for (int i = 0; i<arrayList.size(); i++){
			listModel.add(i, arrayList.get(i).getDescription());
		}
	}
}
