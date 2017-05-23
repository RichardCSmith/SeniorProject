package project;

import java.io.*;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;

import org.json.*;

/**
 * This class gets all champions played during a season specified by the user. This class
 * relies on the STATS-V1.3 part of the LoL API and will be deprecated without replacement
 * in July 2017. Enjoy it while it lasts.
 * 
 * @author Richard Smith
 *
 */

public class ChampionInfo {
	
	private String playerName;
	private String region;
	private String apiKey;
	private int seasonInt;
	private String season;

	private Champion[] champArray;
	
	public ChampionInfo(String name, String reg, int year) throws IOException, JSONException{
		playerName = name;
		
		// Replace spaces in the player name with %20 for the url
		String holderName = "";
		for (int i = 0; i < playerName.length(); i++) {
			if (playerName.charAt(i) != (' ')) {
				holderName = holderName + playerName.charAt(i);
			}
		}
		playerName = holderName;
		
		region = reg;
		apiKey = "be208bdf-d6d8-411c-96ac-8a205253d220";
		seasonInt = year;
		season = "";
		
		if (seasonInt == 2013) {
			season = "SEASON3";
		} else if (seasonInt == 2014) {
			season = "SEASON2014";
		} else if (seasonInt == 2015) {
			season = "SEASON2015";
		} else if (seasonInt == 2016) {
			season = "SEASON2016";
		} else {
			season = "SEASON2017";
		}
		
		calculate();
	}
	
	// Calculates the info on all the champions played during the specified season
	// Info includes win rate and KDA. Also includes "Big Plays" which are defined as
	// instances where the player has killed multiple enemies in a short span of time.
	// Ex. Double kills, triple kill, etc.
	public void calculate() throws IOException, JSONException{
		
		//For rounding doubles to nearest hundredth
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);
		
		// URL for API requests
		URL url = new URL("https://" + region + ".api.riotgames.com/api/lol/" + region + "/v1.3/stats/by-summoner/" + playerName + "/ranked?season=" + season + "&api_key=" + apiKey);
		
		// Make API request
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		
		// Save data as String
		String dataAsString = in.readLine();

		// Turn string data to JSON Object
		JSONObject dataAsJSON = new JSONObject(dataAsString);
		
		// Close reader after saving data
		in.close();
		
		// Get champions JSONArray from data
		JSONArray champions = dataAsJSON.getJSONArray("champions");
		
		// Define champions[]
		int length = champions.length();
		champArray = new Champion[length];
		
		for (int i = 0; i < champions.length(); i++) {
			JSONObject champ = champions.getJSONObject(i);
			JSONObject stats = champ.getJSONObject("stats");
			
			int id = champ.getInt("id");
			
			double winLoss = ((double) stats.getInt("totalSessionsWon")) 
					/ ((double) stats.getInt("totalSessionsPlayed"));
			
			double kda = (((double) stats.getInt("totalChampionKills")) + ((double) stats.getInt("totalAssists")))
					/ ((double) stats.getInt("totalDeathsPerSession"));
			if (Double.isInfinite(kda)) {
				kda = 1.0;
			}
			kda = Double.parseDouble(df.format(kda));
			winLoss = Double.parseDouble(df.format(winLoss));
			
			int doubleKills = stats.getInt("totalDoubleKills");
			int tripleKills = stats.getInt("totalTripleKills");
			int quadraKills = stats.getInt("totalQuadraKills");
			int pentaKills = stats.getInt("totalPentaKills");
			
			if (id != 0){
				Champion champion = new Champion(id, winLoss, kda, doubleKills, tripleKills, quadraKills, pentaKills);
				
				champArray[i] = champion;
			}
		}
	}
	
	// Getter for champArray
	public Champion[] getChampArray() {
		return champArray;
	}
}