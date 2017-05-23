package project;

/**
 * This class gets the items bought in the most recent game as well as
 * the top three items with the most wins in the last 10 games
 * 
 * @author Richard Smith
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemData {
	
	private String playerName;
	private String region;
	private String apiKey;
	private int playerID;
	private int mIndex= 0;
	private long gameID;
	private JSONObject playerStats;
	
	private int[] singleGameItems = new int[6];
	private int[][] tenGameItems = new int[10][6];
	
	private String[][] tenGameItemsString = new String[10][6];
	
	private boolean[] winLoss = new boolean[10];
	
	private String[] topThreeItems = new String[3];

	public ItemData(String name, String regionArg) throws InterruptedException, JSONException, IOException{
		playerName = name;
		region = regionArg;
		apiKey = "be208bdf-d6d8-411c-96ac-8a205253d220";
		
		PlayerData pData = new PlayerData(playerName, region);
		winLoss = pData.getTenGamesWon();
		
		playerID();
		itemDataSingleGame(mIndex);
		itemDataTenGames(mIndex);
		itemsToString();
		itemWinRates();
		
		
		
	}
	
	public void playerID() throws InterruptedException, JSONException, IOException{
		// Replace spaces in the player name with %20 for the url
				String holderName = "";
				for (int i = 0; i < playerName.length(); i++) {
					if (playerName.charAt(i) != (' ')) {
						holderName = holderName + playerName.charAt(i);
					}
				}
				playerName = holderName;

				// URL for Riot API
				URL url = new URL("https://" + region + ".api.pvp.net/api/lol/" + region + "/v1.4/summoner/by-name/" + playerName + "?api_key=" + apiKey);

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
	
	public void itemDataSingleGame(int matchIndex) throws InterruptedException, IOException, JSONException{
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

		//Close reader
		in.close();

		// Request game data using the matchID
		URL url1 = new URL("https://" + region + ".api.pvp.net/api/lol/" + region + "/v2.2/match/" + gameID + "?api_key=" + apiKey);
		boolean tooManyRequests1 = true;
		BufferedReader in1 = null;
		// Reader for the data
		while (tooManyRequests1 == true){
			try {
			in1 =  new BufferedReader(new InputStreamReader(url1.openStream()));
			tooManyRequests1 = false;
			} catch (IOException e) {
				TimeUnit.SECONDS.sleep(3);
			}
		}
				
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
				
		//Use partID to find player statistics in the game
		playerStats = matchDataAsJSON.getJSONArray("participants").getJSONObject(partID-1).getJSONObject("stats");
		
		//Add the items played to singleGameItems[]
		for (int i = 0; i < singleGameItems.length; i++) {
			singleGameItems[i] = playerStats.getInt("item" + i);
		}
	}
	
	public void itemDataTenGames(int matchIndex) throws InterruptedException, IOException, JSONException{
		for (int i = 0; i < 10; i++) {
			itemDataSingleGame(matchIndex);
			matchIndex++;
			for (int j = 0; j < singleGameItems.length; j++){
				tenGameItems[i][j] = singleGameItems[j];
			}
		}
	}
	
	public void itemsToString() throws InterruptedException, IOException, JSONException{
		
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 6; j++) {
				int itemID = tenGameItems[i][j];
				
				if (itemID != 0){
				// URL for Riot API
				URL url = new URL("https://global.api.riotgames.com/api/lol/static-data/" + region + "/v1.2/item/" + itemID + "?api_key=" + apiKey);
				boolean tooManyRequests3 = true;
				BufferedReader in = null;

				// Reader for the data
				//while (tooManyRequests3 == true){
					//try {
					in = new BufferedReader(new InputStreamReader(url.openStream()));
							
					//tooManyRequests3 = false;
					//} catch (IOException e) {
					//	TimeUnit.SECONDS.sleep(3);
					//	System.out.println("god help me");
					//}
				//}

				// Save data as String
				String dataAsString = in.readLine();

				// Turn string data to JSON Object
				JSONObject dataAsJSON = new JSONObject(dataAsString);
				
				//Get name of item as String
				String sName = dataAsJSON.getString("name");
				
				//Add string name to 2D arr of String names
				tenGameItemsString[i][j] = sName;
				
				//Close reader
				in.close();
				} else {
					tenGameItemsString[i][j] = "No item in this slot";
				}
			}
		}
	}
	
	public void itemWinRates() {
		LinkedList<String> itemList = new LinkedList<String>();
		LinkedList<Integer> itemCount = new LinkedList<Integer>();
		
		for (int i = 0; i < winLoss.length; i++) {
			if (winLoss[i]) {
				for (int j = 0; j < 6; j++) {
					if (!itemList.contains(tenGameItemsString[i][j]) && !tenGameItemsString[i][j].equals("No item in this slot")) {
						itemList.add(tenGameItemsString[i][j]);
						itemCount.add(1);
					} else {
						if (!tenGameItemsString[i][j].equals("No item in this slot")) {
							int temp = itemList.indexOf(tenGameItemsString[i][j]);
							int temp2 = itemCount.get(temp);
							itemCount.set(temp, temp2 + 1);
						}
					}
				}
			}
		}
		
		int first = 0;
		String firstString = "";
		int second = 0;
		String secondString = "";
		int third = 0;
		String thirdString = "";
		
		for (int qq = 0; qq < itemCount.size(); qq++){
			if (itemCount.get(qq) > first) {
				third = second;
				second = first;
				first = itemCount.get(qq);
				
				thirdString = secondString;
				secondString = firstString;
				firstString = itemList.get(qq);
			} else if (itemCount.get(qq) > second) {
				third = second;
				second = itemCount.get(qq);
				
				thirdString = secondString;
				secondString = itemList.get(qq);
			} else if (itemCount.get(qq) > third) {
				third = itemCount.get(qq);
				
				thirdString = itemList.get(qq);
			}
		}
		topThreeItems[0] = firstString + ": " + first;
		topThreeItems[1] = secondString + ": " + second;
		topThreeItems[2] = thirdString + ": " + third;
	}
	
	//Getter for singleGameItems[]
	public int[] getSingleGameItems(){
		return singleGameItems;
	}
	
	//Getter for tenGameItems[]
	public int[][] getTenGameItems(){
		return tenGameItems;
	}
	
	//Getter for tenGameItems as strings
	public String[][] getTenGameItemsString(){
		return tenGameItemsString;
	}
	
	//Getter for topThreeItems[]
	public String[] getTopThreeItems(){
		return topThreeItems;
	}
}