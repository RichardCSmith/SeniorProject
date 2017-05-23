package project;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LiveGameDataDisplay {
	
	private LiveGameData gameData;
	
	public LiveGameDataDisplay(LiveGameData data) {
		gameData = data;
	}

	public void displayAll(){
		displayCurrentGame();
	}
	
	public void displayCurrentGame(){
		JFrame f = new JFrame("Live Game");
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
		
		Player[] teamOne = gameData.getTeamOne();
		Player[] teamTwo = gameData.getTeamTwo();
		
		for (int i = 0; i < 5; i++) {
			JLabel label0 = new JLabel(teamOne[i].toString());
			label0.setFont(new Font("Serif", Font.PLAIN, 32));
			label0.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			form.add(label0, c);
			c.gridx++;
		}
		c.gridx = 0;
		c.gridy++;
		
		for (int i = 0; i < 5; i++) {
			JLabel label0 = new JLabel(teamTwo[i].toString());
			label0.setFont(new Font("Serif", Font.PLAIN, 32));
			label0.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			form.add(label0, c);
			c.gridx++;
		}
		
		f.pack();
	}
}