package project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class requests the player's data from the API
 * 
 * @author Richard Smith
 *
 */

public class PlayerData {

	private String playerName;
	private String region;
	private String apiKey;
	private int playerID;
	private double singleGameLength;
	private double[] gameLengthArr = new double[10];
	private int matchIndex = 0;
	private long gameID = 0;
	
	private JSONObject firstGamePlayerStats;
	private JSONObject singleGamePlayerStats;
	private JSONArray masteryPages;
	private JSONArray runePages;
	private JSONObject tenGamePlayerStats[] = new JSONObject[10];
	private int lastChampPlayed;
	private boolean lastGameWon;
	private int tenChampsPlayed[] = new int[10];
	private boolean tenGamesWon[] = new boolean[10];
	private String tenChampsString[] = new String[10];
	private long tenGameMatchIDs[] = new long[10];
	private String tenGamePosition[] = new String[10];
	private double enemyKDA;
	private double tenGameEnemyKDA[] = new double[10];
	private int teamID;
	private int tenGameTeamID[] = new int[10];
	
	private int lastGameNemeses[] = new int[5];
	private int tenGameNemeses[][] = new int[10][5];
	private String nemesesAsString[][] = new String [10][5];

	//Constructor
	public PlayerData(String summName, String reg) throws IOException, MalformedURLException, JSONException, InterruptedException {
		// Get player name and region as direct inputs
		playerName = summName;
		region = reg;

		apiKey = "be208bdf-d6d8-411c-96ac-8a205253d220";

		getPlayerData();
		querySingleGame(matchIndex);
		queryTenGames(matchIndex);
		queryMasteries();
		queryRunes();
	}

	// Gets the playerID
	public void getPlayerData() throws IOException, MalformedURLException, JSONException, InterruptedException {
		// Replace spaces in the player name with %20 for the url
		String holderName = "";
		for (int i = 0; i < playerName.length(); i++) {
			if (playerName.charAt(i) != (' ')) {
				holderName = holderName + playerName.charAt(i);
			}
		}
		playerName = holderName;

		// URL for Riot API
		URL url = new URL("https://" + region + ".api.pvp.net/api/lol/" + region + "/v1.4/summoner/by-name/"
				+ playerName + "?api_key=" + apiKey);

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

		// Save playerID as int
		playerID = dataAsJSON.getJSONObject(playerName).getInt("id");

		// Got relevant data, close reader
		in.close();
	}

	// Query for player data for the most recent game
	public void querySingleGame(int matchIndex) throws IOException, JSONException, InterruptedException {
		// URL for Riot API
		URL url = new URL("https://" + region + ".api.pvp.net/api/lol/" + region + "/v2.2/matchlist/by-summoner/" + playerID + "?api_key=" + apiKey);

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

		// List of games played, grab first one
		JSONArray matchesAll = dataAsJSON.getJSONArray("matches");
		gameID = matchesAll.getJSONObject(matchIndex).getLong("matchId");
		
		//Get position in last ten games
		int counter = 0;
		while (counter < 10){
			tenGamePosition[counter] = matchesAll.getJSONObject(matchIndex).getString("role");
			counter++;
		}

		//Close reader
		in.close();
		
		// Request game data using the matchID
		URL url1 = new URL("https://" + region + ".api.pvp.net/api/lol/" + region + "/v2.2/match/" + gameID + "?api_key=" + apiKey);
		BufferedReader in1 = new BufferedReader(new InputStreamReader(url1.openStream()));
		
		String matchDataAsString = in1.readLine();
		JSONObject matchDataAsJSON = new JSONObject(matchDataAsString);

		// Get player participant ID to get player data
		int partID = -1;
		for (int i = 0; i < matchDataAsJSON.getJSONArray("participantIdentities").length(); i++) {
			if (matchDataAsJSON.getJSONArray("participantIdentities").getJSONObject(i).getJSONObject("player")
					.getLong("summonerId") == playerID) {
				partID = matchDataAsJSON.getJSONArray("participantIdentities").getJSONObject(i).getInt("participantId");
			}
		}
		
		// Test for error
		if (partID < 0) {
			System.out.println("Could not find player in game participants");
			System.exit(0);
		}
		

		// Got relevant data, close reader
		in1.close();
		
		// Use partID to find player statistics in the game
		singleGamePlayerStats = matchDataAsJSON.getJSONArray("participants").getJSONObject(partID-1);
		singleGameLength = matchDataAsJSON.getDouble("matchDuration");
		lastChampPlayed = matchDataAsJSON.getJSONArray("participants").getJSONObject(partID-1).getInt("championId");
		lastGameWon = matchDataAsJSON.getJSONArray("participants").getJSONObject(partID-1).getJSONObject("stats").getBoolean("winner");
		teamID = matchDataAsJSON.getJSONArray("participants").getJSONObject(partID-1).getInt("teamId");
		
		// Calculate enemy average KDA of the game
		double killH = 0;
		double deathH = 0;
		for (int i = 0; i < tenGameEnemyKDA.length; i++) {
			if (matchDataAsJSON.getJSONArray("participants").getJSONObject(i).getInt("teamId") != teamID) {
				killH += matchDataAsJSON.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").getInt("kills");
				killH += matchDataAsJSON.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").getInt("assists");
				deathH += matchDataAsJSON.getJSONArray("participants").getJSONObject(i).getJSONObject("stats").getInt("assists");
			}
		}
		enemyKDA = killH/deathH;
		
		// Record opposing team champions in lastGameNemeses[]
		int index = 0;
		int ourTeamID = matchDataAsJSON.getJSONArray("participants").getJSONObject(partID-1).getInt("teamId");
		for (int i = 0; i < matchDataAsJSON.getJSONArray("participants").length(); i++) {
			if (ourTeamID != matchDataAsJSON.getJSONArray("participants").getJSONObject(i).getInt("teamId")) {
				//Run this if the participant is NOT on our team
				lastGameNemeses[index] = matchDataAsJSON.getJSONArray("participants").getJSONObject(i).getInt("championId");
				index++;
			}
			if (index >= 5){
				break;
			}
		}
		
		if (matchIndex == 0){
			firstGamePlayerStats = matchDataAsJSON.getJSONArray("participants").getJSONObject(partID-1);
		}
	}

	//Query for player data averages over ten games
	public  void queryTenGames(int matchIndex) throws IOException, JSONException, InterruptedException {
		for (int i = 0; i < tenGamePlayerStats.length; i++) {
			try {
				TimeUnit.SECONDS.sleep(3);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			querySingleGame(i);
			tenGameMatchIDs[i] = gameID;
			tenGamePlayerStats[i] = singleGamePlayerStats;
			tenChampsPlayed[i] = lastChampPlayed;
			tenGamesWon[i] = lastGameWon;
			gameLengthArr[i] = singleGameLength;
			tenGameEnemyKDA[i] = enemyKDA;
			
			for (int ctr = 0; ctr < lastGameNemeses.length; ctr++) {
				tenGameNemeses[i][ctr] = lastGameNemeses[ctr];
			}
		}
		
		//Build array of champion names based off their champion IDs for clarity
		//Can do a lot of request to this API because it doesn't count toward rate limit :D
		//Also fill in the 2D array of all enemy champion names as Strings in nemesesAsStrings[][]
		for (int j = 0; j < tenChampsPlayed.length; j++) {
			URL staticChampData = new URL("https://global.api.pvp.net/api/lol/static-data/" + region + "/v1.2/champion/" + tenChampsPlayed[j] + "?api_key=" + apiKey);
			BufferedReader in = new BufferedReader(new InputStreamReader(staticChampData.openStream()));
			
			String champDataString = in.readLine();
			JSONObject champDataJSON = new JSONObject(champDataString);
			tenChampsString[j] = champDataJSON.getString("name");
			in.close();
			
			//New reader and everything to populate nemesesAsStrings[][]
			//Have to loop it to get all of them
			for (int k = 0; k < 5; k++) {
				URL staticChampDataAgain = new URL("https://global.api.pvp.net/api/lol/static-data/" + region + "/v1.2/champion/" + tenGameNemeses[j][k] + "?api_key=" + apiKey);
				BufferedReader inAgain = new BufferedReader(new InputStreamReader(staticChampDataAgain.openStream()));
				
				String champDataStringAgain = inAgain.readLine();
				JSONObject champDataJSONAgain = new JSONObject(champDataStringAgain);
				nemesesAsString[j][k] = champDataJSONAgain.getString("name");
				inAgain.close();
			}
			
		}
	}
	
	// Gets a list of the names of the mastery pages
	public void queryMasteries() throws IOException, MalformedURLException, JSONException {

		// URL of mastery query
		URL masteryRequest = new URL("https://" + region + ".api.pvp.net/api/lol/" + region + "/v1.4/summoner/"
				+ playerID + "/masteries" + "?api_key=" + apiKey);

		// Open new reader for new url
		BufferedReader in = new BufferedReader(new InputStreamReader(masteryRequest.openStream()));
		

		// Save data as String
		String dataAsString = in.readLine();

		// Save string as JSONObject
		JSONObject dataAsJSON = new JSONObject(dataAsString);

		// Save array of pages as an array
		JSONObject n = dataAsJSON.getJSONObject(playerID + "");
		masteryPages = n.getJSONArray("pages");
	}

	// Gets a list of the names of the rune pages
	public void queryRunes() throws IOException, MalformedURLException, JSONException {

		// URL of runes query
		URL masteryRequest = new URL("https://" + region + ".api.pvp.net/api/lol/" + region + "/v1.4/summoner/"
				+ playerID + "/runes" + "?api_key=" + apiKey);

		// Open new reader for new url
		BufferedReader in = new BufferedReader(new InputStreamReader(masteryRequest.openStream()));
		

		// Save data as String
		String dataAsString = in.readLine();

		// Save string as JSONObject
		JSONObject dataAsJSON = new JSONObject(dataAsString);

		// Save array of pages as an array
		JSONObject n = dataAsJSON.getJSONObject(playerID + "");
		runePages = n.getJSONArray("pages");
	}

	
	// Prints the rune and mastery page names to the console
	public void printRunesAndMasteries() throws IOException, MalformedURLException, JSONException {
		// Print out the mastery page names
		System.out.println("\nMASTERY PAGES\n");
		for (int i = 0; i < masteryPages.length(); i++) {
			JSONObject page = masteryPages.getJSONObject(i);
			System.out.println(page.getString("name"));
		}

		// Print out the rune page names
		System.out.println("\nRUNE PAGES\n");
		for (int i = 0; i < runePages.length(); i++) {
			JSONObject page = runePages.getJSONObject(i);
			System.out.println(page.getString("name"));
		}
	}

	// Getter for masteryPages
	public JSONArray getMasteries() {
		return masteryPages;
	}

	// Getter for runePages
	public JSONArray getRunes() {
		return runePages;
	}

	//Getter for playerID
	public int getPlayerID() {
		return playerID;
	}

	//Getter for playerName
	public String getPlayerName() {
		return playerName;
	}

	//Getter for region
	public String getRegion() {
		return region;
	}
	
	//Getter for singleGamePlayerStats
	public JSONObject getFirstGamePlayerStats(){
		return firstGamePlayerStats;
	}
	
	//Getter for tenGamePlayerStats
	public JSONObject[] getTenGamePlayerStats(){
		return tenGamePlayerStats;
	}
	
	//Getter for singleGameLength
	public double getSingleGameLength(){
		return singleGameLength;
	}
	
	//Getter for tenGamesWon
	public boolean[] getTenGamesWon(){
		return tenGamesWon;
	}
	
	//Getter for tenChampsPlayed
	public int[] getTenChampsPlayed(){
		return tenChampsPlayed;
	}
	
	//Getter for tenChampsString
	public String[] getTenChampsString(){
		return tenChampsString;
	}
	
	//Getter for gameLengthArr
	public double[] getGameLengthArr(){
		return gameLengthArr;
	}
	
	//Getter for nemesesAsString[][]
	public String[][] getNemeses(){
		return nemesesAsString;
	}
	
	//Getter for tenGameMatchIDs[]
	public String[] getTenGamePosition() {
		return tenGamePosition;
	}
	
	//Getter for tenGameEnemyKDA[]
	public double[] getEnemyKDA() {
		return tenGameEnemyKDA;
	}
}