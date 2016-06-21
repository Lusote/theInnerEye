package screens;

import java.awt.event.KeyEvent;

import javax.swing.text.BadLocationException;

import rlmain.Creature;
import rlmain.RoguelikeMain;
import squidpony.squidgrid.gui.SGPane;
import util.Util;

public class StatusScreen implements Screen {

	private static Creature player = PlayScreen.getPlayer();
	@Override
	public void display(SGPane console) {
		Util.clearAll();
		Util.printText(1, 0,  "Hero: "+player.getName());
		Util.printText(1, 1,  "Intelligence: "+player.getIntelligence());
		Util.printText(1, 2,  "");
		Util.printText(1, 3,  "");
		Util.printText(1, 4,  "");
		Util.printText(1, 5,  "");
		Util.printText(1, 6,  "");
		Util.printText(1, 7,  "");
		Util.printText(1, 8,  "");
		Util.printText(1, 9,  "");
		Util.printText(1, 10, "");
		Util.printText(1, 11, "");
		Util.printText(1, 12, "");
		Util.printText(1, 13, "");
		Util.printText(1, 14, "");
		Util.printText(1, 15, "");
		Util.printText(1, 16, "");

		Util.printText(1, 17, messages.getString("hlpScrLine17"));
		if(RoguelikeMain.isBlindMode())try {RoguelikeMain.getTextArea().setCaretPosition(RoguelikeMain.getTextArea().getLineStartOffset(0));} catch (BadLocationException e){e.printStackTrace();}
		console.refresh();
	}

	@Override
	public Screen reactToInput(KeyEvent key) {
		return null;
	}

}

