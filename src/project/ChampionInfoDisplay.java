package project;

import java.awt.*;
import javax.swing.*;

/**
 * This class displays the champion data
 * 
 * @author Richard Smith
 *
 */

public class ChampionInfoDisplay {
	
	private Champion[] champions;

	public ChampionInfoDisplay(ChampionInfo championInfo){
		champions = championInfo.getChampArray();
	}
	
	public void displayAll() {
		displayChampions();
		displayBigPlays();
	}
	
	public void displayChampions() {
		JFrame f = new JFrame("Champion Data");
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
		c.anchor = GridBagConstraints.CENTER;
		
		for (int i = 0; i < champions.length  -1; i++) {
			JLabel label0 = new JLabel("<html>" + "<b>" + champions[i].name + "</b>" + "<br>KDA: " + champions[i].kda + "<br>Win Rate: " + champions[i].winLoss + "<br>Big Plays Score: " + champions[i].bigPlayScore);
			label0.setFont(new Font("Serif", Font.PLAIN, 32));
			label0.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			form.add(label0, c);
			if (c.gridx >= 9) {
				c.gridy++;
				c.gridx = 0;
			} else {
				c.gridx++;
			}
		}
		
		f.pack();
	}
	
	public void displayBigPlays() {
		JFrame f = new JFrame("Big Plays");
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
		c.anchor = GridBagConstraints.CENTER;
		
		int first = 0;
		int firstIndex = -1;
		int second = 0;
		int secondIndex = -1;
		int third = 0;
		int thirdIndex = -1;
		
		for (int i = 0; i < champions.length-1; i++) {
			if (first < champions[i].bigPlayScore) {
				third = second;
				second = first;
				first = champions[i].bigPlayScore;
				
				thirdIndex = secondIndex;
				secondIndex = firstIndex;
				firstIndex = i;
			} else if (second < champions[i].bigPlayScore) {
				third = second;
				second = champions[i].bigPlayScore;
				
				thirdIndex = secondIndex;
				secondIndex = i;
			} else if (third < champions[i].bigPlayScore) {
				third = champions[i].bigPlayScore;
				
				thirdIndex = i;
			}
		}
		
		JLabel label0 = new JLabel("<html>" + "<b>" + champions[firstIndex].name + "</b>" + "<br>KDA: " + champions[firstIndex].kda + "<br>Win Rate: " + champions[firstIndex].winLoss + "<br>Big Plays Score: " + champions[firstIndex].bigPlayScore);
		label0.setFont(new Font("Serif", Font.PLAIN, 32));
		label0.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		form.add(label0, c);
		c.gridy++;
		
		JLabel label1 = new JLabel("<html>" + "<b>" + champions[secondIndex].name + "</b>" + "<br>KDA: " + champions[secondIndex].kda + "<br>Win Rate: " + champions[secondIndex].winLoss + "<br>Big Plays Score: " + champions[secondIndex].bigPlayScore);
		label1.setFont(new Font("Serif", Font.PLAIN, 32));
		label1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		form.add(label1, c);
		c.gridy++;
		
		JLabel label2 = new JLabel("<html>" + "<b>" + champions[thirdIndex].name + "</b>" + "<br>KDA: " + champions[thirdIndex].kda + "<br>Win Rate: " + champions[thirdIndex].winLoss + "<br>Big Plays Score: " + champions[thirdIndex].bigPlayScore);
		label2.setFont(new Font("Serif", Font.PLAIN, 32));
		label2.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		form.add(label2, c);
		
		f.pack();
	}
}