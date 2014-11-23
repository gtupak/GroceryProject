
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
	private ArrayList<Entry> arrayList = new ArrayList<Entry>();
	private DefaultListModel listModel = new DefaultListModel();

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
		btnAddEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				listModel.clear();
				//				arrayList.add(new Entry(arrayList.size() + 1, input.getText(), "Gabriel"));
				//				for (int i = 0; i < arrayList.size(); i++){
				//					listModel.add(i, arrayList.get(i).getDescription());
				//				}
				String cmd = "#sql INSERT INTO entries VALUES(" 
						+ (arrayList.size() + 1) 
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
	public void receiveResultSet(ResultSet rs) throws SQLException{
		listModel.clear();
		while (rs.next()){
			arrayList.add(new Entry(rs.getInt(1), rs.getString(2), rs.getString(3)));
		}
		for (int i = 0; i<arrayList.size(); i++){
			listModel.add(i, arrayList.get(i).getDescription());
		}
	}
}
