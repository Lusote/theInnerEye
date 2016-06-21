package screens;

import java.awt.event.KeyEvent;

import javax.swing.text.BadLocationException;

import rlmain.RoguelikeMain;
import squidpony.squidgrid.gui.SGPane;
import util.Util;

public class HelpScreen implements Screen {

	@Override
	public void display(SGPane console) {
		Util.clearAll();
		Util.printText(1, 0,  messages.getString("hlpScrLine0"));
		Util.printText(1, 1,  messages.getString("hlpScrLine1"));
		Util.printText(1, 2,  messages.getString("hlpScrLine2"));
		Util.printText(1, 3,  messages.getString("hlpScrLine3"));
		Util.printText(1, 4,  messages.getString("hlpScrLine4"));
		Util.printText(1, 5,  messages.getString("hlpScrLine5"));
		Util.printText(1, 6,  messages.getString("hlpScrLine6"));
		Util.printText(1, 7,  messages.getString("hlpScrLine7"));
		Util.printText(1, 8,  messages.getString("hlpScrLine8"));
		Util.printText(1, 9,  messages.getString("hlpScrLine9"));
		Util.printText(1, 10, messages.getString("hlpScrLine10"));
		Util.printText(1, 11, messages.getString("hlpScrLine11"));
		Util.printText(1, 12, messages.getString("hlpScrLine12"));
		Util.printText(1, 13, messages.getString("hlpScrLine13"));
		Util.printText(1, 14, messages.getString("hlpScrLine14"));
		Util.printText(1, 15, messages.getString("hlpScrLine15"));
		Util.printText(1, 16, messages.getString("hlpScrLine16"));
		Util.printText(1, 17, messages.getString("hlpSrcLine18"));
		Util.printText(1, 18, messages.getString("hlpSrcLine19"));

		Util.printText(1, 20, messages.getString("hlpScrLine17"));
		if(RoguelikeMain.isBlindMode())try {RoguelikeMain.getTextArea().setCaretPosition(RoguelikeMain.getTextArea().getLineStartOffset(0));} catch (BadLocationException e){e.printStackTrace();}
		console.refresh();
	}

	@Override
	public Screen reactToInput(KeyEvent key) {
		return null;
	}

}

