package screens;

import java.awt.event.KeyEvent;

import javax.swing.text.BadLocationException;

import rlmain.RoguelikeMain;
import squidpony.squidgrid.gui.SGPane;
import util.SaverLoader;
import util.Util;

public class LoseScreen implements Screen {
	
	//private Creature player;
	private String causeOfDeath;
	
	public LoseScreen(String death){
		System.out.println("YOU LOST.");
		causeOfDeath = death;
	}
	

	@Override 
    public void display(SGPane console) {
		int numMessages = PlayScreen.getDungeon().getMessages().size();
		Util.clearTextArea();
		Util.printText(1, 3, messages.getString("lseScrYouLost"));
        Util.printText(1, 4, causeOfDeath);
        Util.printText(1, 5, messages.getString("lseScrBetterLuck"));
        Util.printText(1, 6, messages.getString("lseScrScore")+PlayScreen.getPlayer().getScore());
        //Util.printText(1, 6, "Turns: "+PlayScreen.getDungeon().getNumTurns());
        //Util.printText(1, 7, "Gold: "+ PlayScreen.getPlayer().getInventory().getAmountOfGold());
		Util.printText(1, 9, messages.getString("strScrNewGame"));
		Util.printText(1, 11, messages.getString("strScrNewGameSeed"));
		Util.printText(1, 12, messages.getString("lseScrQuit"));
    	if(SaverLoader.getSaveGames().length > 0)
    		Util.printText(1, 13, messages.getString("strScrLoadGame"));
		if(util.ScoreFileManager.isThereAScoresFile())
    		Util.printText(1, 15, messages.getString("strScrHighScores"));
		for(int i = 1;i<21;i++)
			if(PlayScreen.getDungeon().getMessages().size()>numMessages-i)
				Util.printText(1, 16+i, PlayScreen.getDungeon().getMessages().get(numMessages-i));
		if(RoguelikeMain.isBlindMode())try {RoguelikeMain.getTextArea().setCaretPosition(RoguelikeMain.getTextArea().getLineStartOffset(0));} catch (BadLocationException e){e.printStackTrace();}
		console.refresh();
    }

	@Override
    public Screen reactToInput(KeyEvent key) {
		switch(key.getKeyCode()){
			case KeyEvent.VK_N:
				System.out.println("You pressed n.");
				return new NameScreen(false);//PlayScreen(new Long(0));
			case KeyEvent.VK_S: 
				return new NameScreen(true);//SeedScreen();
			case KeyEvent.VK_H: 
				if(util.ScoreFileManager.isThereAScoresFile())
					return new HighScoreScreen();
			case KeyEvent.VK_L:
				PlayScreen.resetGame();
		    	if(SaverLoader.getSaveGames().length != 0){
					return new LoadScreen();
		    	}else 
		    		return this;
			case KeyEvent.VK_Q:
				RoguelikeMain.exitGame();
				return null;
		}
		return this;
    }

}
