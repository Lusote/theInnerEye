package screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.text.BadLocationException;

import rlmain.RoguelikeMain;
import squidpony.squidgrid.gui.SGPane;
import util.Util;

public class HighScoreScreen implements Screen {

	private ArrayList<String> scores;
	
	public HighScoreScreen(){
		Util.clearTextArea();
		ArrayList<String> highscores = util.ScoreFileManager.getScores();
		setScores(highscores);
	}
	
	@Override
	public void display(SGPane console) {
		Util.clearAll();
		Util.printText(0, 0, messages.getString("hScScrGreatestHeros"));
		int row = 1;
		String toShow;
		for(int i = 0; i < 20; i++){
			toShow = blank;
			if(i == scores.size())
				break;
			toShow = util.Util.padLeft(Integer.toString(i+1),3) + dash+scores.get(i);
			Util.printText(10, row+1, toShow);
			row++;
		}
		if(RoguelikeMain.isBlindMode())try {RoguelikeMain.getTextArea().setCaretPosition(RoguelikeMain.getTextArea().getLineStartOffset(0));} catch (BadLocationException e){e.printStackTrace();}
		console.refresh();

	}

	@Override
	public Screen reactToInput(KeyEvent key) { 
		if (key.getKeyCode() == KeyEvent.VK_ESCAPE)
			return new StartScreen();
		else
			return this;
	}

	public ArrayList<String> getScores(){return scores;}

	public void setScores(ArrayList<String> scores){this.scores = scores;}

}


