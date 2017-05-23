package project;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ItemDataDisplay {

	private ItemData itemData;
	private int[] singleGameData;
	private int[][] tenGameData;
	private String[][] tenGameDataString;
	private String[] bestItems;
	
	public ItemDataDisplay(ItemData itemData) {
		this.itemData = itemData;
		bestItems = itemData.getTopThreeItems();
		singleGameData = itemData.getSingleGameItems();
		tenGameData = itemData.getTenGameItems();
		tenGameDataString = itemData.getTenGameItemsString();
	}
	
	public void displayAll(){
		displaySingleGame();
		displayTenGame();
	}
	
	public void displaySingleGame(){
		JFrame f = new JFrame("Item Data (Last game)");
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(800, 750);
		
		JPanel main = new JPanel();
		f.add(main);
		
		JPanel form = new JPanel(new GridBagLayout());
		main.add(form);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		
		for (int i = 0; i < 6; i++) {
			JLabel label = new JLabel("       Item #" + (i+1) + ": ");
			label.setFont(new Font("Serif", Font.PLAIN, 48));
			form.add(label, c);
			c.gridy++;
		}
		
		c.gridy = 0;
		c.gridx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		
		for (int i = 0; i < 6; i++) {
			JLabel label = new JLabel(tenGameDataString[0][i] + "       ");
			label.setFont(new Font("Serif", Font.PLAIN, 48));
			form.add(label, c);
			c.gridy++;
		}
		
		f.pack();
	}
	
	public void displayTenGame(){
		JFrame f = new JFrame("Item Data (Best Items)");
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(800, 750);
		
		JPanel main = new JPanel();
		f.add(main);
		
		JPanel form = new JPanel(new GridBagLayout());
		main.add(form);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		
		JLabel label = new JLabel(bestItems[0]);
		label.setFont(new Font("Serif", Font.PLAIN, 48));
		form.add(label, c);
		c.gridy++;
		
		JLabel label1 = new JLabel(bestItems[1]);
		label1.setFont(new Font("Serif", Font.PLAIN, 48));
		form.add(label1, c);
		c.gridy++;
		
		JLabel label2 = new JLabel(bestItems[2]);
		label2.setFont(new Font("Serif", Font.PLAIN, 48));
		form.add(label2, c);
		c.gridy++;
		
		f.pack();
	}
}