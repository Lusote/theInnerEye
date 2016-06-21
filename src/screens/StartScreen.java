package screens;

import java.awt.event.KeyEvent;

import javax.swing.text.BadLocationException;

import rlmain.RoguelikeMain;
import squidpony.squidgrid.gui.SGPane;
import util.SaverLoader;
import util.Util;

public class StartScreen implements Screen {
	
	@Override
	public void display(SGPane console) {
		Util.clearAll();
		Util.printText(1, 1, messages.getString("strScrWelcome"));
		Util.printText(1, 2, messages.getString("strScrNewGame"));
		Util.printText(1, 3, messages.getString("strScrNewGameSeed"));
		
    	if(SaverLoader.getSaveGames().length > 0)
    		Util.printText(1, 4, messages.getString("strScrLoadGame"));
		if(util.ScoreFileManager.isThereAScoresFile())
			Util.printText(1, 5, messages.getString("strScrHighScores"));

		Util.printText(1, 25, messages.getString("VERSION")+ " 0.9.08");
		if(RoguelikeMain.isBlindMode())try {RoguelikeMain.getTextArea().setCaretPosition(RoguelikeMain.getTextArea().getLineStartOffset(0));} catch (BadLocationException e){e.printStackTrace();
		}
		console.refresh();
	}

	@Override
	public Screen reactToInput(KeyEvent key) {
		switch(key.getKeyCode()){
		case KeyEvent.VK_N: return new NameScreen(false);
		case KeyEvent.VK_S: return new NameScreen(true);
		case KeyEvent.VK_H: 
			if(util.ScoreFileManager.isThereAScoresFile())
				return new HighScoreScreen();
		case KeyEvent.VK_L:
	    	if(SaverLoader.getSaveGames().length > 0){
				return new LoadScreen();
	    	}else 
	    		return this;
		}
		return this;
	}

}
