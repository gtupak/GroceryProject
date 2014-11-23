package create.gui;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class GroceryListGUI extends JFrame {
	private JFrame GroceryList;
	private StringPanel currentPanel;
	public GroceryListGUI() {
		currentPanel = new StringPanel();
		setupFrame();
	}
	
	private void setupFrame(){
		setBounds(50, 50, 400, 500);
		this.setContentPane(currentPanel);
	}

	public static void main(String[] args){
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try{
					GroceryListGUI window = new GroceryListGUI();
					window.setVisible(true);
				
				}catch (Exception e){
					e.printStackTrace();
				};
			}
		}
		);
	
}
}
			
		
	

