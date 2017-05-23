package project;

import java.io.*;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class defines the Champions object used by the ChampioInfo class
 * 
 * @author Richard Smith
 *
 */

public class Champion {
	
	// Basic champion info
	// Many are public because it's easier than making getter methods for all of them
	// TODO take the time to make getter methods for the sake of good coding practices
	
	// id of champion, used to find the name
	private int id;
	// name of champion
	public String name;
	// win/loss ratio using this champions (percentage)
	public double winLoss;
	// Kill Death Assist ratio for champion : (Kills + Assists)/Deaths
	public double kda;
	// # of double kills earned with this champion
	public int doubleKills;
	// # of triple kills
	public int tripleKills;
	// # of quadra kills
	public int quadraKills;
	// # of penta kills
	public int pentaKills;
	// Score calculated from the double/triple/quadra/penta kills
	public int bigPlayScore;
	
	private String apiKey;
	private String region;
	
	public Champion(int id, double winLoss, double kda, int doubleKills, int tripleKills, int quadraKills, int pentaKills) throws IOException, JSONException {
		
		apiKey = "be208bdf-d6d8-411c-96ac-8a205253d220";
		region = "na1";
		
		this.id = id;
		this.winLoss = winLoss;
		this.kda = kda;
		this.doubleKills = doubleKills;
		this.tripleKills = tripleKills;
		this.quadraKills = quadraKills;
		this.pentaKills = pentaKills;
		this.bigPlayScore = calculateScore();
		this.name = name(this.id);
	}
	
	// Gets name from id
	public String name(int id) throws IOException, JSONException {		
		URL url = new URL("https://" + region + ".api.riotgames.com/lol/static-data/v3/champions/" + id + "?api_key=" + apiKey);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		
		String dataAsString = in.readLine();

		JSONObject dataAsJSON = new JSONObject(dataAsString);
		
		String name = dataAsJSON.getString("name");
		
		in.close();
		return name;
	}
	
	// Calculates bigPlayScore from # of multi-kills
	public int calculateScore() {
		int score = 0;
		
		score += (this.doubleKills) + (this.tripleKills * 3) + (this.quadraKills * 5) + (this.pentaKills * 10);
		
		return score;
	}
}