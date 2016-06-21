package screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.text.BadLocationException;

import rlmain.RoguelikeMain;
import squidpony.squidcolor.SColor;
import squidpony.squidgrid.gui.SGPane;
import util.Util;
import dungeongen.Dungeon;

public class MessagesScreen implements Screen {

	private Dungeon dungeon;
	private int selected=0;
	
	public MessagesScreen(){
		this.dungeon = PlayScreen.getDungeon();
	}
	
	@Override
	public void display(SGPane console){
		Util.clearAll();
		int end;
		clean(console);
		ArrayList<String> messages = dungeon.getMessages();
		int numMess = messages.size()-1;
		if(messages.size()>console.getGridHeight()){
			end = console.getGridHeight();
		}
		else end = messages.size()-1;
		SColor backSelected = SColor.DARK_GRAY;
		SColor backUnSelected = SColor.BLACK;
		for(int i =0; i <= end;i++){
			if(selected == i)
				Util.printText(0, i, messages.get(numMess-i), SColor.WHITE, backSelected);
			else
				Util.printText(0, i, messages.get(numMess-i), SColor.WHITE, backUnSelected);
			//numMess--;
		}
		if(RoguelikeMain.isBlindMode())try {RoguelikeMain.getTextArea().setCaretPosition(RoguelikeMain.getTextArea().getLineStartOffset(0));} catch (BadLocationException e){e.printStackTrace();}
		console.refresh();
	}

	private void clean(SGPane console){
		for(int i = 0; i < console.getGridWidth(); i++)
			for(int j = 0; j < console.getGridHeight(); j++)
				console.clearCell(i, j);
		console.refresh();
	}

	@Override
	public Screen reactToInput(KeyEvent key) {
		if (key.getKeyCode() == KeyEvent.VK_ESCAPE)return null;
		//if (key.getKeyCode() == KeyEvent.VK_DOWN) if(selected < Math.min(dungeon.getMessages().size()-17, 44))selected++;
		//if (key.getKeyCode() == KeyEvent.VK_UP) if(selected>0)selected--;
		return this;
	}

}
