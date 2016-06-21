package util;

import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

import rlmain.Creature;
import rlmain.RoguelikeMain;
import screens.PlayScreen;
import squidpony.squidcolor.SColor;
import squidpony.squidgrid.gui.SGPane;
import effects.Effect;

public abstract class Util {

	public static ResourceBundle messages = RoguelikeMain.messages;
	public static ArrayList<String>emptyStringList = new ArrayList<String>();
	public static ArrayList<Effect>emptyEffectList = new ArrayList<Effect>();
	
	public static boolean isBetween(double ratio, double lower, double upper){return lower <= ratio && ratio <= upper;}

	// Returns true 50% of the time
	public static boolean getHeadsOrTails(){return (PlayScreen.getTheRNG().nextDouble()>0.5);}
	
	public static boolean oddsLessThan(Double odds){return (PlayScreen.getTheRNG().nextDouble()<odds);}
	
	public static String padRight(String s, int n) {
	     return String.format("%1$-" + n + "s", s);  
	}

	public static String padLeft(String s, int n) {
	    return String.format("%1$" + n + "s", s);  
	}
	
	public static void printText(int x, int y,String text){
		RoguelikeMain.getConsole().placeHorizontalString(x,y,text);
		if(RoguelikeMain.isBlindMode()) 
			insertInTextArea(text);
	}
	
	public static void printText(int x, int y, String text, SColor fontColor,
			SColor backColor) {
		RoguelikeMain.getConsole().placeHorizontalString(x, y, text, fontColor, backColor);
		if(RoguelikeMain.isBlindMode()) insertInTextArea(text);		
		
	}
	
	public static void clearTextArea(){
		if(!RoguelikeMain.isBlindMode()) 
			return;
		RoguelikeMain.getTextArea().setText("");
		//try {
		//	RoguelikeMain.getDoc().remove(0, RoguelikeMain.getDoc().getLength());
		//} catch (BadLocationException e) {
		//	// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}		
	}

	public static void clearAll(){
		if(RoguelikeMain.isBlindMode()) clearTextArea();
		SGPane console = RoguelikeMain.getConsole();
		for(int i = 0 ; i<console.getGridWidth(); i++){
			for(int j = 0 ; j<console.getGridHeight(); j++){
				console.clearCell(i, j);
			}
		}
	}
	
	public static void insertInTextArea(String s){
		insertInTA(s);
	}
	
	public static void insertInTextAreaWithBlanks(String s){
		insertInTAWB(s);
	}
	
	private static void insertInTA(String s){
		if(!RoguelikeMain.isBlindMode() || s=="") 
			return;
		RoguelikeMain.getTextArea().append(s.trim()+"\n");
		/*try {
			RoguelikeMain.getDoc().insertString(RoguelikeMain.getDoc().getLength(), s.trim()+"\n", RoguelikeMain.getDoc().getStyle("regular"));
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	private static void insertInTAWB(String s){
		if(!RoguelikeMain.isBlindMode()) 
			return;
		RoguelikeMain.getTextArea().append(s.trim()+"\n");
		/*try {
			RoguelikeMain.getDoc().insertString(RoguelikeMain.getDoc().getLength(), s.trim()+"\n", RoguelikeMain.getDoc().getStyle("regular"));
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}
	
	public static void insertDungeonTA(int row, StringBuilder s){
		/*int endMessages = RoguelikeMain.getDoc().getLength();
		try {
			RoguelikeMain.getDoc().insertString(RoguelikeMain.getDoc().getLength(), s.toString(),  RoguelikeMain.getDoc().getStyle("monospace"));
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int length = RoguelikeMain.getDoc().getLength() - endMessages;

	    StyleContext sc = new StyleContext();
	    final Style cwStyle = sc.addStyle("ConstantWidth", null);
	    StyleConstants.setFontFamily(cwStyle, "monospaced");
		RoguelikeMain.getDoc().setCharacterAttributes(endMessages, length, cwStyle , false);*/
		
		JTextArea tA = RoguelikeMain.getTextArea();
		System.out.println("insertDungeonTA, Line: "+row);

		tA.append(s.toString().replace('\n', '\n'));
	
		try {
			tA.insert(s.toString(), RoguelikeMain.getTextArea().getLineStartOffset(row-1));
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	/*public static void insertCharInTextArea(int row, int column,String s){
		if(!RoguelikeMain.isBlindMode()) return;
		/*if(s == "\n"){
			RoguelikeMain.getTextArea().append("\n");
			return;
		}*//*
		JTextArea tA = RoguelikeMain.getTextArea();
		try {
			tA.insert(s, RoguelikeMain.getTextArea().getLineStartOffset(row-1) +column);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		/*if(!RoguelikeMain.isBlindMode()) 
			return;
		if(s == "\n"){
			RoguelikeMain.getTextArea().append("\n");
			return;
		}			
		RoguelikeMain.getTextArea().append(s.trim());*//*
	}
	
	public static Robot getRobot() {
		try {
			return new Robot();
		} catch (AWTException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}*/
	

	public static SColor getBackColorLife() {
		double ratio = (double)PlayScreen.getPlayer().getHp() /(double)PlayScreen.getPlayer().getMaxHealth();
		SColor backColor = SColor.RED;
		if(util.Util.isBetween(ratio, 0.75, 1)){
			backColor = SColor.GREEN;
		}
		if(util.Util.isBetween(ratio, 0.5, 0.75)){
			backColor = SColor.YELLOW;
		}
		if(util.Util.isBetween(ratio, 0.25, 0.5)){
			backColor = SColor.ORANGE;
		}	
		return backColor;
	}
	
	public static String getHungerDesc(){
		if(PlayScreen.getPlayer().getHunger()<0)
			return "Starving";
		double ratio = (double)PlayScreen.getPlayer().getHunger() /(double)PlayScreen.getPlayer().getMaxHunger();
		if(isBetween(ratio, 0.95, 1))
			return messages.getString("engorded");
		if(isBetween(ratio, 0.75, 0.95))
			return messages.getString("veryFull");
		if(isBetween(ratio, 0.6, 75))
			return messages.getString("full");
		if(isBetween(ratio, 0.4, 0.6))
			return messages.getString("blank");
		if(isBetween(ratio, 0.25, 0.4))
			return messages.getString("hungry");
		if(isBetween(ratio, 0.125, 0.25))
			return messages.getString("veryHungry");
		if(isBetween(ratio, 0, 0.125))
			return messages.getString("nearStarving");
		return messages.getString("OK");
	}

	public static String getEffectsText() {
		StringBuilder toReturn = new StringBuilder();
		Creature player = PlayScreen.getPlayer();
		if(player.isBlind())
			toReturn.append(messages.getString("blindedSpace"));
		if(player.isPoisoned())
			toReturn.append(messages.getString("poisonedSpace"));
		return toReturn.toString();
	}
	
	public static String getXPtext(){
		Creature player  = PlayScreen.getPlayer();
		double xpEndLevel = (double)(Math.pow(player.getLevel(), 1.5) * 20);
		double xpStartLevel = (player.getLevel() == 1) ? 0 : (double)(Math.pow(player.getLevel()-1, 1.5) * 20);
		double xp = player.getXp();
		if(xp == 0) return messages.getString("0%");
		return (Math.round((xp-xpStartLevel)/(xpEndLevel-xpStartLevel)*100) + messages.getString("%"));
	}
	
}
