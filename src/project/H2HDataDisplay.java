package project;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import javax.swing.*;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class displays relevant information to comparing two users side by side (head to head -> H2H)
 * The statistics being compared are selected from a menu
 * 
 * @author Richard Smith
 *
 */

public class H2HDataDisplay {
	
	private PlayerData p1;
	private PlayerData p2;
	
	public H2HDataDisplay(PlayerData playerOne, PlayerData playerTwo){
		p1 = playerOne;
		p2 = playerTwo;
	}
	
	public void displayAll(){
		try {
			displayStatAverageSBS();
		} catch (JSONException e) {
			System.out.println("Error in H2HDataDisplay showing stat average SBS");
			e.printStackTrace();
		}
	}
	
	public void displayStatAverageSBS() throws JSONException{
		
		//Variables for disparities
		double p1KDA = 0;
		double p2KDA = 0;
		
		double p1CS = 0;
		double p2CS = 0;
		
		double p1GPM = 0;
		double p2GPM = 0;
		
		double p1Wards = 0;
		double p2Wards = 0;
		//End variables for disparities
		
		JSONObject[] stats;
		double[] gameLengths;
		
		//For rounding doubles to nearest hundredth
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);
		
		JFrame f = new JFrame("Ten Game Comparison");
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(800, 750);
		
		JPanel main = new JPanel();
		f.add(main);
		
		JPanel form = new JPanel(new GridBagLayout());
		main.add(form);
		
		GridBagConstraints c = new GridBagConstraints();
		
		//Datasets for charts
		DefaultCategoryDataset datasetKDA = new DefaultCategoryDataset();
		DefaultCategoryDataset datasetCS = new DefaultCategoryDataset();
		
		for (int qq = 0; qq < 2; qq++) {
			if (qq == 0) {
				//Player 1 first
				stats = p1.getTenGamePlayerStats();
				gameLengths = p1.getGameLengthArr();
			} else {
				//Player 2 second
				stats = p2.getTenGamePlayerStats();
				gameLengths = p2.getGameLengthArr();
			}
	
			c.gridx = 0;
			if (qq == 1) {
				c.gridx = 2;
			}
			c.gridy = 0;
			c.anchor = GridBagConstraints.LINE_END;
			
			int kills = 0;
			int deaths = 0;
			int assists = 0;
			int minionsKilledTotal = 0;
			double minionsKilledPerMinute = 0.0;
			int enemyJungleKilled = 0;
			int teamJungleKilled = 0;
			int totalJungleKilled = 0;
			int towerKills = 0;
			int inhibKills = 0;
			int wardsPlaced = 0;
			int dmgToChamps = 0;
			int goldEarned = 0;
			double goldPerMinute = 0.0;
			
			//Create some arrays to store data to make it easier to create dataset for line chart
			int[] killsArr = new int[stats.length];
			int[] deathsArr = new int[stats.length];
			int[] assistsArr = new int[stats.length];
			double[] mkpm = new double[stats.length];
			int[] wardsArr = new int[stats.length];
			double[] gpm = new double[stats.length];
			
			for (int i = 0; i <stats.length; i++) {
				kills += stats[i].getJSONObject("stats").getInt("kills");
				killsArr[i] = stats[i].getJSONObject("stats").getInt("kills");
				deaths += stats[i].getJSONObject("stats").getInt("deaths");
				deathsArr[i] = stats[i].getJSONObject("stats").getInt("deaths");
				assists += stats[i].getJSONObject("stats").getInt("assists");
				assistsArr[i] = stats[i].getJSONObject("stats").getInt("assists");
				minionsKilledTotal += stats[i].getJSONObject("stats").getInt("minionsKilled");
				minionsKilledPerMinute += stats[i].getJSONObject("stats").getInt("minionsKilled") / (gameLengths[i] / 60);
				mkpm[i] = Double.parseDouble(df.format(stats[i].getJSONObject("stats").getInt("minionsKilled") / (gameLengths[i] / 60)));
				enemyJungleKilled += stats[i].getJSONObject("stats").getInt("neutralMinionsKilledEnemyJungle");
				teamJungleKilled += stats[i].getJSONObject("stats").getInt("neutralMinionsKilledTeamJungle");
				totalJungleKilled += stats[i].getJSONObject("stats").getInt("neutralMinionsKilledTeamJungle") + stats[i].getJSONObject("stats").getInt("neutralMinionsKilledEnemyJungle");
				towerKills += stats[i].getJSONObject("stats").getInt("towerKills");
				inhibKills += stats[i].getJSONObject("stats").getInt("inhibitorKills");
				wardsPlaced += stats[i].getJSONObject("stats").getInt("wardsPlaced");
				wardsArr[i] = stats[i].getJSONObject("stats").getInt("wardsPlaced");
				dmgToChamps += stats[i].getJSONObject("stats").getInt("totalDamageDealtToChampions");
				goldEarned += stats[i].getJSONObject("stats").getInt("goldEarned");
				goldPerMinute += stats[i].getJSONObject("stats").getInt("goldEarned") / (gameLengths[i] / 60);
				gpm[i] = Double.parseDouble(df.format(stats[i].getJSONObject("stats").getInt("goldEarned") / (gameLengths[i] / 60)));
			}
			
			Object[] statArr = new Object[14];
			
			statArr[0] = kills / 10.0;
			statArr[1] = deaths / 10.0;
			statArr[2] = assists / 10.0;
			if (qq == 0) {
				p1KDA = ((double)(statArr[0]) + (double)(statArr[2])) / (double)(statArr[1]);
			} else {
				p2KDA = ((double)(statArr[0]) + (double)(statArr[2])) / (double)(statArr[1]);
			}
			statArr[3] = minionsKilledTotal / 10.0;
			statArr[4] = Double.parseDouble(df.format(minionsKilledPerMinute / 10.0));
			if (qq == 0) {
				p1CS = (double)(statArr[4]);
			} else {
				p2CS = (double)(statArr[4]);
			}
			statArr[5] = enemyJungleKilled / 10.0;
			statArr[6] = teamJungleKilled / 10.0;
			statArr[7] = totalJungleKilled / 10.0;
			statArr[8] = towerKills / 10.0;
			statArr[9] = inhibKills / 10.0;
			statArr[10] = wardsPlaced / 10.0;
			if (qq == 0) {
				p1Wards = (double)(statArr[10]);
			} else {
				p2Wards = (double)(statArr[10]);
			}
			statArr[11] = dmgToChamps / 10.0;
			statArr[12] = goldEarned / 10.0;
			statArr[13] = Double.parseDouble(df.format(goldPerMinute / 10.0));
			if (qq == 0) {
				p1GPM = (double)(statArr[13]);
			} else {
				p2GPM = (double)(statArr[13]);
			}
			
			if (qq == 0){
				JLabel labelTitle = new JLabel("Summoner 1");
				labelTitle.setFont(new Font("Serif", Font.BOLD, 32));
				form.add(labelTitle, c);
				c.gridy++;
			} else {
				JLabel labelTitle = new JLabel("Summoner 2");
				labelTitle.setFont(new Font("Serif", Font.BOLD, 32));
				form.add(labelTitle, c);
				c.gridy++;
			}
			
			JLabel label0 = new JLabel("Kills: ");
			label0.setFont(new Font("Serif", Font.PLAIN, 32));
			form.add(label0, c);
			c.gridy++;
			
			JLabel label1 = new JLabel("Deaths: ");
			label1.setFont(new Font("Serif", Font.PLAIN, 32));
			form.add(label1, c);
			c.gridy++;
			
			JLabel label2 = new JLabel("Assists: ");
			label2.setFont(new Font("Serif", Font.PLAIN, 32));
			form.add(label2, c);
			c.gridy++;
			
			JLabel label3 = new JLabel("Total Minions Killed: ");
			label3.setFont(new Font("Serif", Font.PLAIN, 32));
			form.add(label3, c);
			c.gridy++;
			
			JLabel label4 = new JLabel("Minions Killed per Minute: ");
			label4.setFont(new Font("Serif", Font.PLAIN, 32));
			form.add(label4, c);
			c.gridy++;
			
			JLabel label5 = new JLabel("     Enemy Jungle Monsters Taken: ");
			label5.setFont(new Font("Serif", Font.PLAIN, 32));
			form.add(label5, c);
			c.gridy++;
			
			JLabel label6 = new JLabel("Team Jungle Monsters Taken: ");
			label6.setFont(new Font("Serif", Font.PLAIN, 32));
			form.add(label6, c);
			c.gridy++;
			
			JLabel label7 = new JLabel("Total Jungle Monsters Taken: ");
			label7.setFont(new Font("Serif", Font.PLAIN, 32));
			form.add(label7, c);
			c.gridy++;
			
			JLabel label8 = new JLabel("Towers Destroyed: ");
			label8.setFont(new Font("Serif", Font.PLAIN, 32));
			form.add(label8, c);
			c.gridy++;
			
			JLabel label9 = new JLabel("Inhibitors Destroyed: ");
			label9.setFont(new Font("Serif", Font.PLAIN, 32));
			form.add(label9, c);
			c.gridy++;
			
			JLabel label10 = new JLabel("Wards Placed: ");
			label10.setFont(new Font("Serif", Font.PLAIN, 32));
			form.add(label10, c);
			c.gridy++;
			
			JLabel label11 = new JLabel("Damage Done to Champions: ");
			label11.setFont(new Font("Serif", Font.PLAIN, 32));
			form.add(label11, c);
			c.gridy++;
			
			JLabel label12 = new JLabel("Total Gold Earned: ");
			label12.setFont(new Font("Serif", Font.PLAIN, 32));
			form.add(label12, c);
			c.gridy++;
			
			JLabel label13 = new JLabel("Gold Earned per Minute: ");
			label13.setFont(new Font("Serif", Font.PLAIN, 32));
			form.add(label13, c);
			c.gridy++;
	
			c.gridx = 1;
			if (qq == 1) {
				c.gridx = 3;
			}
			c.gridy = 1;
			c.anchor = GridBagConstraints.LINE_START;
			
			for (int i = 0; i <statArr.length; i++){
				JLabel l = new JLabel(statArr[i].toString() + "     ");
				l.setFont(new Font("Serif", Font.PLAIN, 32));
				l.setForeground(Color.BLACK);
				form.add(l, c);
				c.gridy++;
			}
			
			f.pack();
			
			//Charts below
			
			if (qq == 0){
				//KDA dataset
				for (int i = 0; i < stats.length; i++){
					String index = Integer.toString(i+1);
					double pointKDA = ((double)(killsArr[stats.length - i - 1] + assistsArr[stats.length - i - 1])) / (double)(deathsArr[stats.length - i - 1]);
					datasetKDA.addValue(pointKDA, "Summoner 1", index);
				}
			    
			    //CS dataset
			    for (int i = 0; i < stats.length; i++){
			    	datasetCS.addValue(mkpm[stats.length - i - 1], "Summoner 1", Integer.toString(i+1));
			    }
			} else {
				//KDA dataset
				for (int i = 0; i < stats.length; i++){
					String index = Integer.toString(i+1);
					double pointKDA = (killsArr[stats.length - i - 1] + assistsArr[stats.length - i - 1]) / deathsArr[stats.length - i - 1];
					datasetKDA.addValue(pointKDA, "Summoner 2", index);
				}
			    
			    //CS dataset
			    for (int i = 0; i < stats.length; i++){
			    	datasetCS.addValue(mkpm[stats.length - i - 1], "Summoner 2", Integer.toString(i+1));
			    }
			}
		}
		
		LineChart chartKDA = new LineChart("KDA Chart", "KDA comparison over 10 games", "Game #", "KDA", datasetKDA);
		chartKDA.pack( );
	    RefineryUtilities.centerFrameOnScreen(chartKDA);
	    chartKDA.setVisible(true);
	    
	    LineChart chartCS = new LineChart("CS per Minute Chart", "CS per Minute comparison over 10 Games", "Game #", "CS per Minute", datasetCS);
		chartCS.pack( );
	    RefineryUtilities.centerFrameOnScreen(chartCS);
	    chartCS.setVisible(true);
	    
	    
	    //Create window for noteworthy disparities
		JFrame f1 = new JFrame("Noteworthy differences");
		f1.setVisible(true);
		f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f1.setSize(600, 600);
		
		JPanel main1 = new JPanel();
		f1.add(main1);
		
		JPanel form1 = new JPanel(new GridBagLayout());
		main1.add(form1);
		
		GridBagConstraints c1 = new GridBagConstraints();
		c1.gridx = 0;
		c1.gridy = 0;
		c1.anchor = GridBagConstraints.CENTER;
		
		boolean kdaDiff = false;
		boolean csDiff = false;
		boolean wardsDiff = false;
		boolean gpmDiff = false;
		
		if (Math.abs(p1KDA - p2KDA) > 0.5){
			kdaDiff = true;
		}
		
		if (Math.abs(p1CS - p2CS) > 1){
			csDiff = true;
		}
		
		if (Math.abs(p1Wards - p2Wards) > 3){
			wardsDiff = true;
		}
		
		if (Math.abs(p1GPM - p2GPM) > 30){
			gpmDiff = true;
		}
		
		if (!kdaDiff && !csDiff && !wardsDiff && !gpmDiff){
			JLabel diffLabel = new JLabel("No notable differences");
			diffLabel.setFont(new Font("Serif", Font.PLAIN, 32));
			form1.add(diffLabel, c1);
		}
		
		if (kdaDiff) {
			JLabel diffLabel = new JLabel("Notable KDA Difference: " + df.format(p1KDA) + " vs " + df.format(p2KDA));
			diffLabel.setFont(new Font("Serif", Font.PLAIN, 32));
			form1.add(diffLabel, c1);
			c1.gridy++;
		}
		
		if (csDiff) {
			JLabel diffLabel = new JLabel("Notable CS Difference: " + df.format(p1CS) + " vs " + df.format(p2CS));
			diffLabel.setFont(new Font("Serif", Font.PLAIN, 32));
			form1.add(diffLabel, c1);
			c1.gridy++;
		}
		
		if (wardsDiff) {
			JLabel diffLabel = new JLabel("Notable Wards Difference: " + df.format(p1Wards) + " vs " + df.format(p2Wards));
			diffLabel.setFont(new Font("Serif", Font.PLAIN, 32));
			form1.add(diffLabel, c1);
			c1.gridy++;
		}
		
		if (gpmDiff) {
			JLabel diffLabel = new JLabel("Notable GPM Difference: " + df.format(p1GPM) + " vs " + df.format(p2GPM));
			diffLabel.setFont(new Font("Serif", Font.PLAIN, 32));
			form1.add(diffLabel, c1);
			c1.gridy++;
		}
		
		f1.pack();
	}
}