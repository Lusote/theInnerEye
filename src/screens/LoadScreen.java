package screens;

import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.text.BadLocationException;

import rlmain.RoguelikeMain;
import squidpony.squidgrid.gui.SGPane;
import util.SaverLoader;
import util.Util;

public class LoadScreen implements Screen {

	private String letters;
	
	public LoadScreen(){
		this.setLetters("qwertyuiopasdfghjklz");
	}
	
	@Override
	public void display(SGPane console) {
		Util.clearAll();
		File[] files = SaverLoader.getSaveGames();		
		Util.printText(0, 0, messages.getString("loaScrWhatToLoad"));
		int row = 1;
		String letters = this.letters;
		String toShow;
		File f;
		for(int i = 0; i < 20; i++){
			if(i == files.length)
				break;
			f = files[i];
			toShow = Character.toString(letters.charAt(row-1));
			toShow = toShow.concat(dash+f.getName());
			Util.printText(10, row, toShow);
			row++;
		}
		if(RoguelikeMain.isBlindMode())try {RoguelikeMain.getTextArea().setCaretPosition(RoguelikeMain.getTextArea().getLineStartOffset(0));} catch (BadLocationException e){e.printStackTrace();}
		console.refresh();

	}

	@Override
	public Screen reactToInput(KeyEvent key) {
		char c = key.getKeyChar();

		File[] files = SaverLoader.getSaveGames();	
		
		if (letters.indexOf(c) > -1 
				&& files.length > letters.indexOf(c)
				&& files[letters.indexOf(c)] != null){
			return use(files[letters.indexOf(c)].getName());
		} else if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
			return null;
		} else {
			return this;
		}
	}
	

	protected Screen use(String name){
		return new PlayScreen(null, null, name);
	}

	public String getLetters(){return letters;}

	public void setLetters(String letters){this.letters = letters;}

}


