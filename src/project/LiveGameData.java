package project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class gets data on a game in-progress.
 * It organizes all 10 players by team and displays some statistics
 * about them. It highlights the best players on each team.
 * 
 * @author Richard Smith
 *
 */

public class LiveGameData {
	
	private String playerID;
	private String region;
	private String apiKey;
	
	private JSONObject[] playersJSON = new JSONObject[10];
	private Player[] players = new Player[10];
	
	private Player[] teamOne = new Player[5];
	private Player[] teamTwo = new Player[5];

	public LiveGameData(String name, String reg) throws IOException, JSONException, InterruptedException {
		playerID = name;
		region = reg;
		apiKey = "be208bdf-d6d8-411c-96ac-8a205253d220";
		gameLookup();
	}
	
	// Gets game data from API
	public void gameLookup() throws IOException, JSONException, InterruptedException {
		
		URL url = new URL("https://" + region + "1.api.riotgames.com/lol/spectator/v3/active-games/by-summoner/" + playerID + "?api_key=" + apiKey);
		
		boolean tooManyRequests = true;
		BufferedReader in = null;
		// Reader for the data
		while (tooManyRequests == true){
			try {
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			
			tooManyRequests = false;
			} catch (IOException e) {
				TimeUnit.SECONDS.sleep(3);
			}
		}
		
		// Save data as String
		String dataAsString = in.readLine();

		// Turn string data to JSON Object
		JSONObject dataAsJSON = new JSONObject(dataAsString);
		
		// Populate players array
		int teamOneIndex = 0;
		int teamTwoIndex = 0;
		for (int i = 0; i < 10; i++) {
			playersJSON[i] = dataAsJSON.getJSONArray("participants").getJSONObject(i);
			players[i] = new Player(playersJSON[i], region);
			if (players[i].getTeam() == 100 && teamOneIndex < 5) {
				teamOne[teamOneIndex] = players[i];
				teamOneIndex++;
			} else {
				teamTwo[teamTwoIndex] = players[i];
				teamTwoIndex++;
			}
		}
	}
	
	// Getter for teamOne
	public Player[] getTeamOne() {
		return teamOne;
	}
	
	// Getter for teamTwo
	public Player[] getTeamTwo() {
		return teamTwo;
	}
}