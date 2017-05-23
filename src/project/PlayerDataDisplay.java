package project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.LinkedList;

import javax.swing.*;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RefineryUtilities;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class displays various points in the player data
 * 
 * @author Richard Smith
 *
 */

public class PlayerDataDisplay {

	private PlayerData data;
	
	public PlayerDataDisplay(PlayerData data) {
		this.data = data;
	}
	
	//Displays all data for the player
	public void displayAll() throws JSONException, IOException{
		displaySingleGameData();
		displayTenGameData();
		displayTenChamps();
		displayRunesAndMasteries();
		displayNemeses();
		displayLosses();
	}
	
	//Displays the data from a single game
	public void displaySingleGameData() throws JSONException, IOException {
		JFrame f = new JFrame("Most Recent Game");
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
		
		JSONObject stats = data.getFirstGamePlayerStats();
		
		Object[] statArr = new Object[14];
		
		//For rounding doubles to nearest hundredth
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);
		
		int kills = stats.getJSONObject("stats").getInt("kills");
		int deaths = stats.getJSONObject("stats").getInt("deaths");
		int assists = stats.getJSONObject("stats").getInt("assists");
		int minionsKilledTotal = stats.getJSONObject("stats").getInt("minionsKilled");
		double minionsKilledPerMinute = minionsKilledTotal / (data.getSingleGameLength() / 60);
		minionsKilledPerMinute = Double.parseDouble(df.format(minionsKilledPerMinute));
		int enemyJungleKilled = stats.getJSONObject("stats").getInt("neutralMinionsKilledEnemyJungle");
		int teamJungleKilled = stats.getJSONObject("stats").getInt("neutralMinionsKilledTeamJungle");
		int totalJungleKilled = teamJungleKilled + enemyJungleKilled;
		int towerKills = stats.getJSONObject("stats").getInt("towerKills");
		int inhibKills = stats.getJSONObject("stats").getInt("inhibitorKills");
		int wardsPlaced = stats.getJSONObject("stats").getInt("wardsPlaced");
		int dmgToChamps = stats.getJSONObject("stats").getInt("totalDamageDealtToChampions");
		int goldEarned = stats.getJSONObject("stats").getInt("goldEarned");
		double goldPerMinute = goldEarned / (data.getSingleGameLength() / 60);
		goldPerMinute = Double.parseDouble(df.format(goldPerMinute));
		
		statArr[0] = kills;
		statArr[1] = deaths;
		statArr[2] = assists;
		statArr[3] = minionsKilledTotal;
		statArr[4] = minionsKilledPerMinute;
		statArr[5] = enemyJungleKilled;
		statArr[6] = teamJungleKilled;
		statArr[7] = totalJungleKilled;
		statArr[8] = towerKills;
		statArr[9] = inhibKills;
		statArr[10] = wardsPlaced;
		statArr[11] = dmgToChamps;
		statArr[12] = goldEarned;
		statArr[13] = goldPerMinute;
		
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
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		
		for (int i = 0; i <statArr.length; i++){
			JLabel l = new JLabel(statArr[i].toString() + "     ");
			l.setFont(new Font("Serif", Font.PLAIN, 32));
			l.setForeground(Color.BLACK);
			form.add(l, c);
			c.gridy++;
		}
		
		f.pack();
	}

	//Displays average data from last game games
	public void displayTenGameData() throws JSONException, IOException {
		
		//For rounding doubles to nearest hundredth
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);
		
		JFrame f = new JFrame("Ten Game Averages");
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
		
		JSONObject[] stats = data.getTenGamePlayerStats();
		double[] gameLengths = data.getGameLengthArr();
		
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
		statArr[3] = minionsKilledTotal / 10.0;
		statArr[4] = Double.parseDouble(df.format(minionsKilledPerMinute / 10.0));
		statArr[5] = enemyJungleKilled / 10.0;
		statArr[6] = teamJungleKilled / 10.0;
		statArr[7] = totalJungleKilled / 10.0;
		statArr[8] = towerKills / 10.0;
		statArr[9] = inhibKills / 10.0;
		statArr[10] = wardsPlaced / 10.0;
		statArr[11] = dmgToChamps / 10.0;
		statArr[12] = goldEarned / 10.0;
		statArr[13] = Double.parseDouble(df.format(goldPerMinute / 10.0));
			
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
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		
		for (int i = 0; i <statArr.length; i++){
			JLabel l = new JLabel(statArr[i].toString() + "     ");
			l.setFont(new Font("Serif", Font.PLAIN, 32));
			l.setForeground(Color.BLACK);
			form.add(l, c);
			c.gridy++;
		}
		
		f.pack();
		
		//Create line chart for ten game data
		//4 charts: KDA, CS, wards, GpM
		//Need to create 4 datasets
		
		//KDA dataset
		DefaultCategoryDataset datasetKDA = new DefaultCategoryDataset();
		for (int i = 0; i < stats.length; i++){
			String index = Integer.toString(i+1);
			datasetKDA.addValue(killsArr[stats.length - i - 1], "Kills", index);
			datasetKDA.addValue(deathsArr[stats.length - i - 1], "Deaths", index);
			datasetKDA.addValue(assistsArr[stats.length - i - 1], "Assists", index);
		}
		LineChart chartKDA = new LineChart("KDA Chart", "Kills, Deaths, and Assists over 10 Games", "Game #", "Value", datasetKDA);
		chartKDA.pack( );
	    RefineryUtilities.centerFrameOnScreen(chartKDA);
	    chartKDA.setVisible(true);
	    
	    //CS dataset
	    DefaultCategoryDataset datasetCS = new DefaultCategoryDataset();
	    for (int i = 0; i < stats.length; i++){
	    	datasetCS.addValue(mkpm[stats.length - i - 1], "CS per minute", Integer.toString(i+1));
	    }
	    LineChart chartCS = new LineChart("CS per Minute Chart", "CS per Minute over 10 Games", "Game #", "CS per Minute", datasetCS);
		chartCS.pack( );
	    RefineryUtilities.centerFrameOnScreen(chartCS);
	    chartCS.setVisible(true);
	    
	    //Wards dataset
	    DefaultCategoryDataset datasetWards = new DefaultCategoryDataset();
	    for (int i = 0; i < stats.length; i++){
	    	datasetWards.addValue(wardsArr[stats.length - i - 1], "Wards Placed", Integer.toString(i+1));
	    }
	    LineChart chartWards = new LineChart("Wards Placed Chart", "Wards Placed over 10 Games", "Game #", "Wards Placed", datasetWards);
		chartWards.pack( );
	    RefineryUtilities.centerFrameOnScreen(chartWards);
	    chartWards.setVisible(true);
	    
	    //GPM dataset
	    DefaultCategoryDataset datasetGPM = new DefaultCategoryDataset();
	    for (int i = 0; i < stats.length; i++){
	    	datasetGPM.addValue(gpm[stats.length - i - 1], "Gold Earned per Minute", Integer.toString(i+1));
	    }
	    LineChart chartGPM = new LineChart("Gold per Minute Chart", "Gold per Minute over 10 Games", "Game #", "Gold per Minute", datasetGPM);
		chartGPM.pack( );
	    RefineryUtilities.centerFrameOnScreen(chartGPM);
	    chartGPM.setVisible(true);
	}
	
	//Displays the last ten champions played and whether or not the user won that game
	public void displayTenChamps(){
		JFrame f = new JFrame("Recent Champions Played");
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(400, 600);
		
		JPanel main = new JPanel();
		f.add(main);
		
		JPanel form = new JPanel(new GridBagLayout());
		main.add(form);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		
		boolean[] gamesWon = data.getTenGamesWon();
		String[] champsPlayed = data.getTenChampsString();
		
		for (int i = 0; i < champsPlayed.length; i++) {
			JLabel label = new JLabel("     " + champsPlayed[i] + ": ");
			form.add(label, c);
			label.setFont(new Font("Serif", Font.PLAIN, 32));
			c.gridy ++;
		}
		
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		
		for (int i = 0; i < gamesWon.length; i++) {
			if (gamesWon[i] == true) {
				JLabel label = new JLabel("Victory     ");
				form.add(label, c);
				label.setFont(new Font("Serif", Font.PLAIN, 32));
			} else {
				JLabel label = new JLabel("Defeat     ");
				form.add(label, c);
				label.setFont(new Font("Serif", Font.PLAIN, 32));
			}
			c.gridy ++;
		}
		
		f.pack();
	} 
	
	//Displays the titles of the rune and mastery pages in a singe JFrame
	//TODO Add rune and mastery viewing functionality
	public void displayRunesAndMasteries() throws JSONException {
		
		JFrame f = new JFrame("Runes and Masteries");
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(600, 900);
		
		JPanel main = new JPanel();
		f.add(main);
		
		JPanel form = new JPanel(new GridBagLayout());
		main.add(form);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		
		//Get runes and masteries data
		JSONArray masteries = data.getMasteries();
		JSONArray runes = data.getRunes();
		
		for (int i = -1; i < masteries.length(); i++) {
			if (i == -1){
				JLabel label = new JLabel("MASTERIES  ");
				form.add(label, c);
				label.setFont(new Font("Serif", Font.BOLD, 32));
				c.gridy ++;
			} else {
				String name = masteries.getJSONObject(i).getString("name");
				JLabel label = new JLabel("     " + name + "  ");
				form.add(label, c);
				label.setFont(new Font("Serif", Font.PLAIN, 32));
				c.gridy ++;
			}
		}
		
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		
		for (int i = -1; i < runes.length(); i++) {
			if (i == -1){
				JLabel label = new JLabel("RUNES");
				form.add(label, c);
				label.setFont(new Font("Serif", Font.BOLD, 32));
				c.gridy ++;
			} else {
				String name = runes.getJSONObject(i).getString("name");
				JLabel label = new JLabel(name + "     ");
				form.add(label, c);
				label.setFont(new Font("Serif", Font.PLAIN, 32));
				c.gridy ++;
			}
		}
		
		f.pack();
	}
	
	//Displays the champs most won against and most lost against in the last ten games
	public void displayNemeses() {
		//Setting up these data structures, it seems inefficient. There has to be a better way, but I don't see it right now.
		boolean[] record = data.getTenGamesWon();
		String[][] enemies = data.getNemeses();
		
		LinkedList<String> championsWin = new LinkedList<String>();
		LinkedList<Integer> frequencyWin = new LinkedList<Integer>();
		
		LinkedList<String> championsLoss = new LinkedList<String>();
		LinkedList<Integer> frequencyLoss = new LinkedList<Integer>();
		
		//Counts the # of times each champion played against is beaten/user gets beaten by
		for (int row = 0; row < 10; row++) {
			if (record[row]) {
				for (int column = 0; column < 5; column++) {
					if (!championsWin.contains(enemies[row][column])){
						championsWin.add(enemies[row][column]);
						frequencyWin.add(1);
					} else {
						int ctr = 0;
						while (!championsWin.get(ctr).equalsIgnoreCase(enemies[row][column])) {
							ctr++;
						}
						frequencyWin.set(ctr, frequencyWin.get(ctr) + 1);
					}
				}
			} else {
				for (int column = 0; column < 5; column++) {
					if (!championsLoss.contains(enemies[row][column])){
						championsLoss.add(enemies[row][column]);
						frequencyLoss.add(1);
					} else {
						int ctr = 0;
						while (!championsLoss.get(ctr).equalsIgnoreCase(enemies[row][column])) {
							ctr++;
						}
						frequencyLoss.set(ctr, frequencyLoss.get(ctr) + 1);
					}
				}
			}
		}
		
		//Pick top three beaten/beaten by champions and display them
		int top1 = -1;
		int top1index = -1;
		int top2 = -1;
		int top2index = -1;
		int top3 = -1;
		int top3index = -1;
		
		for (int i = 0; i < frequencyWin.size(); i++) {
			if (frequencyWin.get(i) > top1){
				top3 = top2;
				top3index = top2index;
				top2 = top1;
				top2index = top1index;
				top1 = frequencyWin.get(i);
				top1index = i;
			} else if (frequencyWin.get(i) > top2){
				top3 = top2;
				top3index = top2index;
				top2 = frequencyWin.get(i);
				top2index = i;
			} else if (frequencyWin.get(i) > top3){
				top3 = frequencyWin.get(i);
				top3index = i;
			}
		}
		
		int bot1 = -1;
		int bot1index = -1;
		int bot2 = -1;
		int bot2index = -1;
		int bot3 = -1;
		int bot3index = -1;
		
		for (int i = 0; i < frequencyLoss.size(); i++) {
			if (frequencyLoss.get(i) > bot1){
				bot3 = bot2;
				bot3index = bot2index;
				bot2 = bot1;
				bot2index = bot1index;
				bot1 = frequencyLoss.get(i);
				bot1index = i;
			} else if (frequencyLoss.get(i) > bot2){
				bot3 = bot2;
				bot3index = bot2index;
				bot2 = frequencyLoss.get(i);
				bot2index = i;
			} else if (frequencyLoss.get(i) > bot3){
				bot3 = frequencyLoss.get(i);
				bot3index = i;
			}
		}

		JFrame f = new JFrame("Best and Worst Enemies");
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(600, 900);
		
		JPanel main = new JPanel();
		f.add(main);
		
		JPanel form = new JPanel(new GridBagLayout());
		main.add(form);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_START;
		
		JLabel label0 = new JLabel("User beat: # of times     ");
		label0.setFont(new Font("Serif", Font.BOLD, 32));
		form.add(label0, c);
		c.gridy++;
		
		JLabel l1 = new JLabel(championsWin.get(top1index) + ": " + top1);
		l1.setFont(new Font("Serif", Font.PLAIN, 32));
		form.add(l1, c);
		c.gridy++;
		
		JLabel l2 = new JLabel(championsWin.get(top2index) + ": " + top2);
		l2.setFont(new Font("Serif", Font.PLAIN, 32));
		form.add(l2, c);
		c.gridy++;
		
		JLabel l3 = new JLabel(championsWin.get(top3index) + ": " + top3);
		l3.setFont(new Font("Serif", Font.PLAIN, 32));
		form.add(l3, c);

		c.gridy = 0;
		c.gridx = 1;
		
		JLabel label0L = new JLabel("User was beaten by: # of times     ");
		label0L.setFont(new Font("Serif", Font.BOLD, 32));
		form.add(label0L, c);
		c.gridy++;
		
		JLabel l1L = new JLabel(championsLoss.get(bot1index) + ": " + bot1);
		l1L.setFont(new Font("Serif", Font.PLAIN, 32));
		form.add(l1L, c);
		c.gridy++;
		
		JLabel l2L = new JLabel(championsLoss.get(bot2index) + ": " + bot2);
		l2L.setFont(new Font("Serif", Font.PLAIN, 32));
		form.add(l2L, c);
		c.gridy++;
		
		JLabel l3L = new JLabel(championsLoss.get(bot3index) + ": " + bot3);
		l3L.setFont(new Font("Serif", Font.PLAIN, 32));
		form.add(l3L, c);
		
		f.pack();
	}
	
	//Displays opponent information from games you lost
	public void displayLosses(){
		boolean[] wL = data.getTenGamesWon();
		double[] enemyKDA = data.getEnemyKDA();
		
		JFrame f = new JFrame("Enemy KDA per Loss");
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(400, 600);
		
		JPanel main = new JPanel();
		f.add(main);
		
		JPanel form = new JPanel(new GridBagLayout());
		main.add(form);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		
		for (int i = 0; i < wL.length; i++) {
			if (!wL[i]) {
				JLabel label0 = new JLabel("Game " + (i+1) + ": " + enemyKDA[i]);
				label0.setFont(new Font("Serif", Font.PLAIN, 32));
				form.add(label0, c);
				c.gridy++;
			}
		}
		
		f.pack();
	}
	
	public void centerButtons(JButton b){
		b.setAlignmentX(Component.CENTER_ALIGNMENT);
	}
	
	public void centerLabels(JLabel l){
		l.setAlignmentX(Component.CENTER_ALIGNMENT);
	}
}