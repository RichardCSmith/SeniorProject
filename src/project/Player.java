package project;

import java.io.*;
import java.math.RoundingMode;
import java.net.*;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class defines the player object that is used in LiveGameData class.
 * NOTE: This class will only work for the duration of the 2017 season. It
 * will need updating for the next season.
 * 
 * @author Richard Smith
 *
 */

public class Player {

	private String name;
	private int id;
	private int team;
	private double winRate;
	private double kda;
	private String apiKey;
	private String region;
	
	public Player(JSONObject playerJSON, String reg) throws JSONException, IOException, InterruptedException {
		apiKey = "be208bdf-d6d8-411c-96ac-8a205253d220";
		this.region = reg;
		this.id = playerJSON.getInt("summonerId");
		this.name = playerJSON.getString("summonerName");
		this.team = playerJSON.getInt("teamId");
		this.winRate = -1.0;
		this.kda = -1.0;
		findWR();
	}
	
	public void findWR() throws IOException, JSONException, InterruptedException {
		URL url = new URL("https://" + region + ".api.riotgames.com/api/lol/" + region + "/v1.3/stats/by-summoner/" + id + "/ranked?season=SEASON2017&api_key=" + apiKey);
		
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
		
		String dataAsString = in.readLine();

		JSONObject dataAsJSON = new JSONObject(dataAsString);
		
		int length = dataAsJSON.getJSONArray("champions").length();
		
		JSONObject totalData = null;
		
		for (int killme = 0; killme < dataAsJSON.getJSONArray("champions").length(); killme++){
			if (dataAsJSON.getJSONArray("champions").getJSONObject(killme).getInt("id") == 0){
				totalData = dataAsJSON.getJSONArray("champions").getJSONObject(killme);
			}
		}
		
		if (totalData.getInt("id") != 0) {
			System.out.println("Error getting average data");
		}
		
		//For rounding doubles to nearest hundredth
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);
		
		this.kda = ((double) (totalData.getJSONObject("stats").getInt("totalChampionKills") + totalData.getJSONObject("stats").getInt("totalAssists"))) / ((double) totalData.getJSONObject("stats").getInt("totalDeathsPerSession"));
		this.winRate = ((double) totalData.getJSONObject("stats").getInt("totalSessionsWon")) / ((double) totalData.getJSONObject("stats").getInt("totalSessionsPlayed"));
		
		this.kda = Double.parseDouble(df.format(this.kda));
		this.winRate = Double.parseDouble(df.format(this.winRate));
	}
	
	public String toString(){
		String playerString = "<html><b>" + name + "</b><br>Win Ratio: " + winRate + "<br>KDA: " + kda + "</html>";
		return playerString;
	}
	
	public int getTeam(){
		return this.team;
	}
}