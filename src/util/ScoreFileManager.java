package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import rlmain.Creature;
import screens.PlayScreen;

public abstract class ScoreFileManager {
	
	public static final Comparator<String> HIGH_SCORE =  new Comparator<String>() {
		public int compare(String HS1, String HS2) {
			Integer i = Integer.parseInt(HS1.substring(0, HS1.indexOf("-")-1).trim());
			Integer j = Integer.parseInt(HS2.substring(0, HS2.indexOf("-")-1).trim());
			return	j.compareTo(i);
		}
	};
	
	public static boolean isThereAScoresFile(){
		File scores = new File("highScores");
		return scores.exists();
	}
	
	public static void addScore(){
		Creature player = PlayScreen.getPlayer();
		int score = player.getScore();
		String name =player.getName();
		String toWrite = util.Util.padLeft(Integer.toString(score), 6) +" - "+name;
		File scoreFile = new File ("highScores");
		Writer output = null;
		try {
			output = new BufferedWriter(new FileWriter(scoreFile, true));
			output.append(toWrite+"\n");
			output.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<String> getScores(){
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(new File("highScores")));
			String line;
			ArrayList<String>toReturn = new ArrayList<String>();
			while ((line = br.readLine()) != null) {
				toReturn.add(line);
			}
			br.close();
			Collections.sort(toReturn, HIGH_SCORE);
			writeAllScores(toReturn);
			return toReturn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;		
	}

	private static void writeAllScores(ArrayList<String> scores){
		File scoreFile = new File ("highScores");
		scoreFile.delete();
		Writer output = null;
		try {
			output = new BufferedWriter(new FileWriter(scoreFile, true));
			for(int i = 0; i < 20; i++){
				if(i==scores.size())
					break;
				output.append(scores.get(i)+"\n");
			}
			output.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
