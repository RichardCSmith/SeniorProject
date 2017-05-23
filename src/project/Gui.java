package project;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.util.Map;

import javax.swing.*;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * This class displays the main menu and handles the buttons
 * @author Richard Smith
 *
 */

public class Gui {

	// Declare GUI components
	private JButton playerDataButton;
	private JButton h2hDataButton;
	private JButton itemDataButton;
	private JButton liveDataButton;
	private JButton championButton;
	private JButton inDepthButton;
	
	public Gui(){
		makeGUI();
	}
	
	//Main Menu
	public void makeGUI(){
		JFrame f = new JFrame("LoL API Project");
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(600, 500);
		
		JPanel main = new JPanel();
		f.add(main);
		
		JPanel form = new JPanel(new GridBagLayout());
		main.add(form);
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.CENTER;
		
		//Make the button and the label
		playerDataButton = new JButton("Fetch Player Data");
		playerDataButton.setFont(new Font("Serif", Font.PLAIN, 24));
		playerDataButton.setPreferredSize(new Dimension(350, 50));
		h2hDataButton = new JButton("Fetch Head to Head Data");
		h2hDataButton.setFont(new Font("Serif", Font.PLAIN, 24));
		h2hDataButton.setPreferredSize(new Dimension(350, 50));
		itemDataButton = new JButton("Fetch Item Data");
		itemDataButton.setFont(new Font("Serif", Font.PLAIN, 24));
		itemDataButton.setPreferredSize(new Dimension(350, 50));
		liveDataButton = new JButton("Fetch Live Data");
		liveDataButton.setFont(new Font("Serif", Font.PLAIN, 24));
		liveDataButton.setPreferredSize(new Dimension(350, 50));
		championButton = new JButton("Fetch Champion Data");
		championButton.setFont(new Font("Serif", Font.PLAIN, 24));
		championButton.setPreferredSize(new Dimension(350, 50));
		inDepthButton = new JButton("Fetch Specific Champion Data");
		inDepthButton.setFont(new Font("Serif", Font.PLAIN, 24));
		inDepthButton.setPreferredSize(new Dimension(350, 50));
		
		JLabel title = new JLabel("LoL API Project");
		title.setFont(new Font("Serif", Font.PLAIN, 32));
		
		
		form.add(title, c);
		c.gridy++;
		form.add(playerDataButton, c);
		c.gridy++;
		form.add(h2hDataButton, c);
		c.gridy++;
		form.add(itemDataButton, c);
		c.gridy++;
		form.add(liveDataButton, c);
		c.gridy++;
		form.add(championButton, c);
		c.gridy++;
		form.add(inDepthButton, c);
		
		playerDataButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object src = e.getSource();
				
				//Fetch player data and display it
				if (src == playerDataButton){
					try {
						//Get inputs
						String name = JOptionPane.showInputDialog("Please enter your summoner name:");
						String region = JOptionPane.showInputDialog("Please enter the region you play in:");
						
						//Fetch data
						PlayerData playerData = new PlayerData(name, region);
						
						//Display data
						PlayerDataDisplay display = new PlayerDataDisplay(playerData);
						display.displayAll();
						f.setVisible(false);
					} catch (IOException e1) {
						System.out.print("Error creating new PlayerData");
						e1.printStackTrace();
						System.exit(0);
					} catch (JSONException e2) {
						System.out.println("Error creating JSONObject");
						e2.printStackTrace();
						System.exit(0);
					} catch (InterruptedException e1) {
						System.out.println("Time error.");
						e1.printStackTrace();
					}
				}
			}
		});
		
		h2hDataButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object src = e.getSource();
				if (src == h2hDataButton) {
					try {
						//Get inputs
						String user = JOptionPane.showInputDialog("Please enter player 1's summoner name:");
						String opponent = JOptionPane.showInputDialog("Please enter the player 2's summoner name: ");
						String userRegion = JOptionPane.showInputDialog("Please enter the region player 1 plays in:");
						String oppRegion = JOptionPane.showInputDialog("Please enter the region player 2 plays in:");
						
						//Fetch data
						PlayerData playerData = new PlayerData(user, userRegion);
						PlayerData oppData = new PlayerData(opponent, oppRegion);
						
						//Display data
						H2HDataDisplay display = new H2HDataDisplay(playerData, oppData);
						display.displayAll();
						f.setVisible(false);
					} catch (IOException e1) {
						System.out.print("Error creating new H2HData");
						e1.printStackTrace();
						System.exit(0);
					} catch (JSONException e2) {
						System.out.println("Error creating JSONObject");
						e2.printStackTrace();
						System.exit(0);
					} catch (InterruptedException e1) {
						System.out.println("Time error.");
						e1.printStackTrace();
					}
				}
			}
		});
		
		itemDataButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object src = e.getSource();
				
				//Fetch player data and display it
				if (src == itemDataButton){
					try {
						//Get inputs
						String name = JOptionPane.showInputDialog("Please enter your summoner name:");
						String region = JOptionPane.showInputDialog("Please enter the region you play in:");
						
						//Fetch data
						ItemData itemData = new ItemData(name, region);
						
						//Display data
						ItemDataDisplay display = new ItemDataDisplay(itemData);
						display.displayAll();
						f.setVisible(false);
					} catch (IOException e1) {
						System.out.print("Error creating new ItemData");
						e1.printStackTrace();
						System.exit(0);
					} catch (JSONException e2) {
						System.out.println("Error creating JSONObject");
						e2.printStackTrace();
						System.exit(0);
					} catch (InterruptedException e1) {
						System.out.println("Time error.");
						e1.printStackTrace();
					}
				}
			}
		});
		
		championButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object src = e.getSource();
				
				//Fetch player data and display it
				if (src == championButton){
					try {
						//Get inputs
						String name = JOptionPane.showInputDialog("Please enter your summoner ID (Not name):");
						String region = JOptionPane.showInputDialog("Please enter the region you play in:");
						int year = Integer.parseInt(JOptionPane.showInputDialog("Please enter the season you want data from (2013 - 2017):"));
						
						while (year != 2013 && year != 2014 && year != 2015 && year != 2016 && year != 2017) {
							year = Integer.parseInt(JOptionPane.showInputDialog("Please enter a valid year (2013 - 2017):"));
						}
						
						//Fetch data
						ChampionInfo championInfo = new ChampionInfo(name, region, year);
						
						//Display data
						ChampionInfoDisplay display = new ChampionInfoDisplay(championInfo);
						display.displayAll();
						f.setVisible(false);
					} catch (IOException e1) {
						System.out.print("Error creating new ChampionInfo");
						e1.printStackTrace();
						System.exit(0);
					} catch (JSONException e2) {
						System.out.println("Error creating JSONObject");
						e2.printStackTrace();
						System.exit(0);
					}
				}
			}
		});
		
		liveDataButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object src = e.getSource();
				
				//Fetch player data and display it
				if (src == liveDataButton){
					//Get inputs
					String name = JOptionPane.showInputDialog("Please enter your summoner ID (Not name):");
					String region = JOptionPane.showInputDialog("Please enter the region you play in:");
					
					//Fetch data
					LiveGameData data;
					try {
						data = new LiveGameData(name, region);
					
					
					//Display data
					LiveGameDataDisplay display = new LiveGameDataDisplay(data);
					display.displayAll();
					f.setVisible(false);
					} catch (IOException | JSONException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		
		inDepthButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object src = e.getSource();
				
				//Fetch player data and display it
				if (src == inDepthButton){
					try {
						//Get inputs
						String name = JOptionPane.showInputDialog("Please enter your player ID (not name):");
						String region = JOptionPane.showInputDialog("Please enter the region you play in:");
						String champion = JOptionPane.showInputDialog("Please enter the champion you want info on:");
						
						int nameInt = Integer.parseInt(name);
						
						//Fetch data
						ChampionFeedback championFeedback = new ChampionFeedback(nameInt, region, champion);
						f.setVisible(false);
					} catch (IOException e1) {
						System.out.print("Error creating new PlayerData");
						e1.printStackTrace();
						System.exit(0);
					} catch (JSONException e2) {
						System.out.println("Error creating JSONObject");
						e2.printStackTrace();
						System.exit(0);
					}
				}
			}
		});
	}
}