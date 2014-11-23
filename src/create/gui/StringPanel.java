package create.gui;

import javax.swing.JPanel;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JTextField;
import java.awt.Checkbox;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import java.awt.Scrollbar;
import javax.swing.JList;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

public class StringPanel extends JPanel {
	//private JPanel Item;
	private JPanel listing;
	private JTextField textField;
	private JLabel itemList;
	JCheckBox checkbox1;
	public StringPanel() {
		setupPanel();
	}
	
	private void setupPanel(){
		setBackground(new Color(245, 255, 250));
		
		JButton btnNewButton = new JButton("Add Item");
		btnNewButton.setBounds(215, 335, 102, 23);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
		    itemList.setText(textField.getText());
		    textField.setText("");
		    add(checkbox1);

			}
		});
		setLayout(null);
		
		JList list = new JList();
		list.setBounds(8, 100, 309, 140);
		add(list);
		add(btnNewButton);
		
		JLabel lblGroceryList = new JLabel("Grocery List");
		lblGroceryList.setBounds(8, 22, 201, 23);
		lblGroceryList.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 16));
		add(lblGroceryList);
		
		textField = new JTextField();
		textField.setBounds(8, 335, 201, 23);
		add(textField);
		textField.setColumns(10);
		
		itemList = new JLabel("");
		itemList.setBounds(8, 60, 309, 29);
		add(itemList);
		
		
		JButton btnRemoveItem = new JButton("Remove Item");
		btnRemoveItem.setBounds(215, 369, 102, 23);
		btnRemoveItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		add(btnRemoveItem);
		
		
		checkbox1 = new JCheckBox("");
		checkbox1.setBackground(new Color(240, 255, 255));
		checkbox1.setBounds(225, 58, 21, 23);
		checkbox1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
			}

		});
		}
}
