package screens;

import java.awt.event.KeyEvent;

import javax.swing.text.BadLocationException;

import rlmain.RoguelikeMain;
import squidpony.squidgrid.gui.SGPane;
import util.SaverLoader;
import util.Util;

public class SavedScreen implements Screen {
	
	//private Creature player;
	
	public SavedScreen(){
	}
	

	@Override 
    public void display(SGPane console) {
		Util.clearAll();
		System.out.println("You saved the game.");
		Util.printText(1, 3, messages.getString("sveScrSaved"));//"You saved the game.");
		Util.printText(1, 9, messages.getString("strScrNewGame"));//"Press n to start a new game!");
		Util.printText(1, 11, messages.getString("strScrNewGameSeed"));//"Press s to start a new game given a seed.");
		Util.printText(1, 12, messages.getString("lseScrQuit"));//"Press q to close the game.");
    	if(SaverLoader.getSaveGames().length != 0)
    		Util.printText(1, 13, messages.getString("strScrLoadGame"));//"Press l to load the saved game.");
		if(RoguelikeMain.isBlindMode())try {RoguelikeMain.getTextArea().setCaretPosition(RoguelikeMain.getTextArea().getLineStartOffset(0));} catch (BadLocationException e){e.printStackTrace();}
		console.refresh();
    }

	@Override
    public Screen reactToInput(KeyEvent key) {
		switch(key.getKeyCode()){
			case KeyEvent.VK_N:
				return new NameScreen(false);//PlayScreen(new Long(0));
			case KeyEvent.VK_S: 
				return new NameScreen(true);//SeedScreen();
			case KeyEvent.VK_L:
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