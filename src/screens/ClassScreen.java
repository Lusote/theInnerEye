package screens;

import java.awt.event.KeyEvent;

import javax.swing.text.BadLocationException;

import rlmain.RoguelikeMain;
import rlmain.Creature.creatureClasses;
import squidpony.squidgrid.gui.SGPane;
import util.Util;

public class ClassScreen implements Screen {
	
	private boolean seed;
	private String name;
	public ClassScreen(boolean seed, String name){
		Util.clearTextArea();
		this.seed = seed;
		this.name = name;
	}
	
	@Override
	public void display(SGPane console) {
		Util.clearAll();
		Util.printText(1, 1, messages.getString("clsScrChooseClass"));
		Util.printText(1, 2, messages.getString("blank"));
		Util.printText(1, 3, messages.getString("clsScrWarrior1"));
		Util.printText(1, 4, messages.getString("clsScrWarrior2"));
		Util.printText(1, 5, messages.getString("blank"));
		Util.printText(1, 6, messages.getString("clsScrArcher1"));
		Util.printText(1, 7, messages.getString("clsScrArcher2"));
		Util.printText(1, 8, messages.getString("blank"));
		Util.printText(1, 9, messages.getString("clsScrWhiteMage1"));
		Util.printText(1, 10, messages.getString("clsScrWhiteMage2"));
		
		console.refresh();
		if(RoguelikeMain.isBlindMode())try {RoguelikeMain.getTextArea().setCaretPosition(RoguelikeMain.getTextArea().getLineStartOffset(0));} catch (BadLocationException e){e.printStackTrace();}
	}

	@Override
	public Screen reactToInput(KeyEvent key) {
		switch(key.getKeyCode()){
			case KeyEvent.VK_1:
				if(seed) return new SeedScreen(creatureClasses.WARRIOR, name);
				else return new PlayScreen(new Long(0), creatureClasses.WARRIOR, name);
			case KeyEvent.VK_2:
				if(seed) return new SeedScreen(creatureClasses.ARCHER, name);
				else return new PlayScreen(new Long(0), creatureClasses.ARCHER, name);
			case KeyEvent.VK_3:
				if(seed) return new SeedScreen(creatureClasses.WHITEMAGE, name);
				else return new PlayScreen(new Long(0), creatureClasses.WHITEMAGE, name);
		}
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
