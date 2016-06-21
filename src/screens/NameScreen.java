package screens;

import java.awt.event.KeyEvent;

import javax.swing.text.BadLocationException;

import rlmain.RoguelikeMain;
import squidpony.squidgrid.gui.SGPane;
import util.Util;

public class NameScreen implements Screen {
	
	private String name;
	private boolean seed;
	private static String possibleKeys = "QWERTYUIOPASDFGHJKLÑZXCVBNMqwertyuiopasdfghjklñzxcvbnm0123456789_ "; 
	
	public NameScreen(boolean seed){
		Util.clearTextArea();
		this.name = util.ConfigFileManager.getDefaultName();
		this.setSeed(seed);
	}

	@Override
	public void display(SGPane console) {
		Util.clearAll();
		Util.printText(10, 10, messages.getString("nmeScrWhatName"));
		//if(firstTime)rlmain.RoguelikeMain.getSpeaker().speak("Enter seed");
		//firstTime = false;
		Util.printText(10, 12, getName());
		Util.printText(10, 13, messages.getString("nmeScrUnderline"));

		if(RoguelikeMain.isBlindMode())try {RoguelikeMain.getTextArea().setCaretPosition(RoguelikeMain.getTextArea().getLineStartOffset(0));} catch (BadLocationException e){e.printStackTrace();}
		console.refresh();

	}

	@Override
	public Screen reactToInput(KeyEvent key) {
		switch(key.getKeyCode()){
			case KeyEvent.VK_BACK_SPACE: removeLast(); 
			 	return this;
			case KeyEvent.VK_ENTER:
				util.ConfigFileManager.setDefaultName(getName());
				return new ClassScreen(seed, name);
		}
		if(possibleKeys.contains(""+key.getKeyChar())){
			addToSeed(key.getKeyChar());
			return this;
		}
		return this;
	}
	
	private void setName(String s){this.name = s;}
	
	private String getName(){return name;}
	
	private void addToSeed(char c){
		if(name.length()<10)
			setName(name + c);
	}
	
	private void removeLast(){
		if(getName().length()>0)
			setName(getName().substring(0, getName().length()-1));
	}

	public boolean getSeed() {
		return seed;
	}

	public void setSeed(boolean seed) {
		this.seed = seed;
	}

}
