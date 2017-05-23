package project;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.*;
import java.math.RoundingMode;
import java.net.*;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * This class gets all the data necessary for specific champion feedback.
 * The user can enter a character name and the program will provide their
 * statistics using that character as well as some tips for improving
 * based on the data. This class only looks at the 2017 season.
 * 
 * @author Richard Smith
 *
 */

public class ChampionFeedback {
	private String apiKey;
	
	// User generated variables
	private int playerId;
	private String region;
	private String champion;
	
	// Program generated data about champion
	// Data outside id and timesPlayed are averages across all games
	private int championId;
	private double kda;
	private double winRate;
	private double damageTaken;
	private double damageDone;
	private double goldEarned;
	private int numTimesPlayed;
	
	public ChampionFeedback(int pId, String reg, String championName) throws IOException, JSONException{
		apiKey = "be208bdf-d6d8-411c-96ac-8a205253d220";

		playerId = pId;
		region = reg;
		champion = championName;
		championId = id(champion);
		data(championId);
		display();
	}
	
	public int id(String stringName) throws IOException, JSONException{
		int id= -1;
		
		URL url = new URL("https://global.api.riotgames.com/api/lol/static-data/NA/v1.2/champion?api_key=" + apiKey);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		
		String dataAsString = in.readLine();

		JSONObject dataAsJSON = new JSONObject(dataAsString);
		
		JSONObject champion = dataAsJSON.getJSONObject("data").getJSONObject(stringName);
		
		id = champion.getInt("id");
		
		return id;
	}
	
	public void data(int id) throws IOException, JSONException{
		URL url = new URL("https://" + region + ".api.riotgames.com/api/lol/" + region + "/v1.3/stats/by-summoner/" + playerId + "/ranked?season=SEASON2017&api_key=" + apiKey);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		
		String dataAsString = in.readLine();

		JSONObject dataAsJSON = new JSONObject(dataAsString);
		
		JSONArray champions = dataAsJSON.getJSONArray("champions");
		int length = champions.length();
		for (int i = 0; i < length; i++){
			if (champions.getJSONObject(i).getInt("id") == id) {
				
				//For rounding doubles to nearest hundredth
				DecimalFormat df = new DecimalFormat("#.##");
				df.setRoundingMode(RoundingMode.CEILING);
				
				if (champions.getJSONObject(i).getJSONObject("stats").getInt("totalDeathsPerSession") != 0) {
					kda = (((double) champions.getJSONObject(i).getJSONObject("stats").getInt("totalChampionKills")) + ((double) champions.getJSONObject(i).getJSONObject("stats").getInt("totalAssists")))
							/ ((double) champions.getJSONObject(i).getJSONObject("stats").getInt("totalDeathsPerSession"));
				} else {
					kda = (((double) champions.getJSONObject(i).getJSONObject("stats").getInt("totalChampionKills")) + ((double) champions.getJSONObject(i).getJSONObject("stats").getInt("totalAssists")))
							/ (1.0);
				}
				winRate = ((double) champions.getJSONObject(i).getJSONObject("stats").getInt("totalSessionsWon")) 
						/ ((double) champions.getJSONObject(i).getJSONObject("stats").getInt("totalSessionsPlayed"));
				
				damageTaken = ((double) champions.getJSONObject(i).getJSONObject("stats").getInt("totalDamageTaken")) 
						/ ((double) champions.getJSONObject(i).getJSONObject("stats").getInt("totalSessionsPlayed"));
				
				damageDone = ((double) champions.getJSONObject(i).getJSONObject("stats").getInt("totalDamageDealt")) 
						/ ((double) champions.getJSONObject(i).getJSONObject("stats").getInt("totalSessionsPlayed"));
				
				goldEarned = ((double) champions.getJSONObject(i).getJSONObject("stats").getInt("totalGoldEarned")) 
						/ ((double) champions.getJSONObject(i).getJSONObject("stats").getInt("totalSessionsPlayed"));
				
				numTimesPlayed = champions.getJSONObject(i).getJSONObject("stats").getInt("totalSessionsPlayed");
				
				kda = Double.parseDouble(df.format(kda));
				System.out.println("hello there");
				winRate = Double.parseDouble(df.format(winRate));
				System.out.println("hello there");
				damageTaken = Double.parseDouble(df.format(damageTaken));
				System.out.println("hello there");
				damageDone = Double.parseDouble(df.format(damageDone));
				System.out.println("hello there");
				goldEarned = Double.parseDouble(df.format(goldEarned));
				System.out.println("hello there");
				
				break;
			}
		}
	}
	
	public String toString(){
		String playerString = "<html><b>" + champion + 
				"</b><br>Times played: " + numTimesPlayed +
				"<br>Win Ratio: " + winRate + 
				"<br>KDA: " + kda + 
				"<br>Damage dealt (avg): " + damageDone + 
				"<br>Damage taken (avg): " + damageTaken +
				"<br>Gold earned (avg): " + goldEarned +
				"</html>";
		return playerString;
	}
	
	// Included display method here instead of new class
	// because this class is pretty short.
	public void display() {
		JFrame f = new JFrame("In-depth Champion Data");
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
		
		JLabel label0 = new JLabel(toString());
		label0.setFont(new Font("Serif", Font.PLAIN, 48));
		form.add(label0, c);
		
		c.gridy++;
		
		form.add(advice(), c);
		
		c.gridy++;
		
		JLabel label1 = new JLabel("<html>General tip: Players with a positive, team focused attitude<br>win 10% more games than the average player!</html>");
		label1.setFont(new Font("Serif", Font.PLAIN, 32));
		label1.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		form.add(label1, c);
	}
	
	// This method generates a label of advice to place underneath the champion info
	public JLabel advice(){
		String tip = "No tips available, you're doing great!";
		
		if (numTimesPlayed < 10) {
			tip = "<html>You have very few games played with this champion.<br>The key to improvement is familiarity.</html>";
		} else if (kda < 2.0) {
			tip = "<html>You're dying too often.<br>Play safe and know your enemy's damage potential.</html>";
		} else if (goldEarned < 7000) {
			tip = "<html>You aren't earning much gold per match.<br>Try to focus on CS more.</html>";
		} else if(damageDone < 10000) {
			tip = "<html>You aren't dealing very much damage to the enemy.<br>Try to find more opportunities to hit your opponents.</html>";
		} else if (damageTaken > 10000) {
			tip = "<html>You're taking a lot of damage.<br>Try to position yourself out of range of major enemy abilties.</html>";
		} else if (winRate < 0.5) {
			tip = "<html>You just aren't winning games.<br>Try to focus on objectives.<br>Don't let games in which you are ahead drag out. End it quick!</html>";
		}
		
		JLabel label0 = new JLabel(tip);
		label0.setFont(new Font("Serif", Font.PLAIN, 32));
		label0.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		return label0;
	}
}