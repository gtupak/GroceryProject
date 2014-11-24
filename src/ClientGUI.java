
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
import javax.swing.ListSelectionModel;

import common.Entry;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.ListModel;

public class ClientGUI  {

	protected JFrame frame;
	private JTextField input;
	private static JList groceryList;
	private static ArrayList<Entry> arrayList = new ArrayList<Entry>();
	private static DefaultListModel listModel = new DefaultListModel();
	private static ArrayList<Integer> uniqueID = new ArrayList<Integer>();
	private static JLabel creatorName = new JLabel("");
	private static JLabel checkerName = new JLabel("");
	private static CheckListManager checkListManager;
	private static ListSelectionModel selModel;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
		frame.setBounds(100, 100, 458, 397);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 41, 426, 193);
		frame.getContentPane().add(scrollPane);

		groceryList = new JList(listModel);
		CheckListManager checkListManager = new CheckListManager(groceryList){
			@Override
			public void mouseClicked(MouseEvent me){ 
				int index = groceryList.locationToIndex(me.getPoint()); 
				if(index<0) 
					return; 
				if(me.getX()>groceryList.getCellBounds(index, index).x+hotspot) 
					return; 
				toggleSelection(index);
				
				// Update entry as "checked" in the database
				Entry en = arrayList.get(groceryList.getSelectedIndex());
				int id = en.getId();
				String checker = (en.isChecked()) ? "" : ClientConsole.client.getLogin();
				String cmd = "#sql UPDATE entries SET checked=" + !en.isChecked() + ", checker='"
						+ checker + "' WHERE id=" + id;
				ClientConsole.receiveGUICommand(cmd);
			} 
		};
		
		groceryList.setBackground(new Color(255, 255, 204));
		groceryList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
//				System.out.println("Mouse clicked on " + arrayList.get(uncheckedList.getSelectedIndex()));
				creatorName.setText(arrayList.get(groceryList.getSelectedIndex()).getCreator());
				checkerName.setText(arrayList.get(groceryList.getSelectedIndex()).getChecker());
			}
		});
		
		scrollPane.setViewportView(groceryList);
		
		selModel = checkListManager.getSelectionModel();
		
		input = new JTextField();
		input.setBounds(12, 311, 282, 34);
		frame.getContentPane().add(input);
		input.setColumns(10);

		JButton btnAddEntry = new JButton("Add Entry");
		btnAddEntry.setBackground(new Color(204, 255, 153));
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
		btnAddEntry.setBounds(321, 315, 117, 25);
		frame.getContentPane().add(btnAddEntry);

		JLabel lblMyGroceryList = new JLabel("My Grocery List");
		lblMyGroceryList.setBounds(165, 0, 129, 29);
		frame.getContentPane().add(lblMyGroceryList);
		
		JLabel lblCreator = new JLabel("Creator:");
		lblCreator.setBounds(23, 246, 75, 25);
		frame.getContentPane().add(lblCreator);
		
		creatorName.setBounds(91, 246, 75, 25);
		frame.getContentPane().add(creatorName);
		
		JButton btnRemoveEntry = new JButton("Remove Entry");
		btnRemoveEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				// Get clicked ID
				int id = arrayList.get(groceryList.getSelectedIndex()).getId();
				
				// Create SQL string
				String cmd = "#SQL DELETE FROM entries WHERE id=" + id;
				
				// Delete id from uniqueID list
				for (int i = 0; i < uniqueID.size(); i++){
					if (uniqueID.get(i) == id){
						uniqueID.remove(i);
						break;
					}
				}
				ClientConsole.receiveGUICommand(cmd);
			}
		});
		btnRemoveEntry.setBackground(new Color(255, 153, 102));
		btnRemoveEntry.setBounds(308, 246, 130, 25);
		frame.getContentPane().add(btnRemoveEntry);
		
		JLabel lblChecker = new JLabel("Checked by:");
		lblChecker.setBounds(12, 279, 86, 15);
		frame.getContentPane().add(lblChecker);
		
		checkerName.setBounds(121, 274, 75, 25);
		frame.getContentPane().add(checkerName);
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
		int i = 0;
		while (it.hasNext()){
			en = it.next();
			uniqueID.add(i, en.getId());
			arrayList.add(en);
			i++;
		}
		for (int ind = 0; ind<arrayList.size(); ind++){
			listModel.add(ind, arrayList.get(ind).getDescription());
			if (arrayList.get(ind).isChecked()){
				selModel.addSelectionInterval(ind, ind);
			}
			else
				selModel.removeSelectionInterval(ind, ind);
		}
	}
}
