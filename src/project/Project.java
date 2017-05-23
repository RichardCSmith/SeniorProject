package project;

import org.jfree.ui.RefineryUtilities;

/**
 * This program was created to assist players of League of Legends improve their gameplay.
 * It accesses API data and analyzes it to provide comparative feedback to focus on areas
 * where the player needs to improve. It can display the user's statistics, it can provide
 * the user with a side-by-side comparison between himself and another player, it can
 * provide itemization analysis, and it can provide live-game strategy by finding the
 * user's current opponents' strengths and weaknesses.
 * 
 * @author Richard Smith
 * Began: February 2017
 * Last Edit: May 2017
 */

public class Project {
	
	//Create to main menu, which will handle all user input
	public static void main (String[] args){
		new Gui();
	}
}