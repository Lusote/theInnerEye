
package screens;

import items.Item;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.text.BadLocationException;

import rlmain.Creature;
import rlmain.RoguelikeMain;
import squidpony.squidgrid.gui.SGPane;
import util.Util;
import dungeongen.Dungeon;

public class ExamineInventoryScreen implements Screen {

	private Creature player;
	private String letters;
	
	protected Screen use(Item item) {
		Dungeon dun = PlayScreen.getDungeon();
		for(String s : item.getDescription())
			dun.addMessage(s);
		return this;
	}
	
	public ExamineInventoryScreen(){
		this.player = PlayScreen.getPlayer();
		this.setLetters("qwertyuiopasdfghjklzxcvbnm");
	}
	
	@Override
	public void display(SGPane console) {
		ArrayList<Item> inv = this.getPlayer().getInventory().getItems();
		Util.clearTextArea();;
		Util.printText(0, 0, messages.getString("exIScrWhatToExamine"));
		//int sizeInv = inv.size();
		int row = 1;
		String letters = this.letters;
		String toShow;
		String equipped = blank;
		String n;
		int number = 0;
		for(Item i : inv){
			equipped = blank;
			n = blank;
			number = i.getOtherItems().size()+1;
			if(number>1) n = x+number;
			if(i.isEquipped()) equipped = equippedText;
			toShow = Character.toString(letters.charAt(row-1));
			toShow = toShow.concat(dash+i.getName()+equipped+n);
			Util.printText(10, row, toShow);
			row++;
		}
		if(RoguelikeMain.isBlindMode())try {RoguelikeMain.getTextArea().setCaretPosition(RoguelikeMain.getTextArea().getLineStartOffset(0));} catch (BadLocationException e){e.printStackTrace();}
		console.refresh();

	}

	@Override
	public Screen reactToInput(KeyEvent key) {
		char c = key.getKeyChar();

		ArrayList<Item> items = getPlayer().getInventory().getItems();
		
		if (letters.indexOf(c) > -1 
				&& items.size() > letters.indexOf(c)
				&& items.get(letters.indexOf(c)) != null){
			return use(items.get(letters.indexOf(c)));
		} else if (key.getKeyCode() == KeyEvent.VK_ESCAPE) {
			return null;
		} else {
			return this;
		}
	}

	public String getLetters(){return letters;}

	public void setLetters(String letters){this.letters = letters;}
	
	public Creature getPlayer(){return player;}

}