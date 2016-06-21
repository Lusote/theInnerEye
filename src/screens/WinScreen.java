package screens;

import java.awt.event.KeyEvent;

import javax.swing.text.BadLocationException;

import rlmain.RoguelikeMain;
import squidpony.squidgrid.gui.SGPane;
import util.Util;

public class WinScreen implements Screen {

	@Override
	public void display(SGPane console) {
		Util.clearAll();
		Util.printText(1, 1, messages.getString("winScrYouWon"));//"YOU WON!!");
		Util.printText(1, 2, messages.getString("winScrHowAboutThat"));//"How about that?");
        Util.printText(1, 4, messages.getString("winScrYourPoints")+PlayScreen.getPlayer().getScore());
		Util.printText(1, 6, messages.getString("winScrTurnOffGoToBed"));//"Now turn off your computer and go to sleep");
		if(RoguelikeMain.isBlindMode())try {RoguelikeMain.getTextArea().setCaretPosition(RoguelikeMain.getTextArea().getLineStartOffset(0));} catch (BadLocationException e){e.printStackTrace();}
		console.refresh();
	}

	@Override
	public Screen reactToInput(KeyEvent key) {
        return this;
	}

}
