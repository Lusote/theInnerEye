package screens;

import items.Item;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.text.BadLocationException;

import rlmain.Creature;
import rlmain.Drawable;
import rlmain.RoguelikeMain;
import squidpony.squidgrid.gui.SGPane;
import util.Util;
import dungeongen.Dungeon;

/*
 * TODO:
 * 	Check for more than one Pages, if there are A LOT of items/creatures.
 * 
 * 
 */
public class ExamineMapScreen implements Screen{

	private Creature player;
	private String letters;
	
	public ExamineMapScreen(){
		this.player = PlayScreen.getPlayer();
		this.setLetters("qwertyuiopasdfghjklzxcvbnm");
	}
	
	protected Screen use(Drawable item) {
		Dungeon dun = item.getDungeon();
		for(String s : item.getDescription())
			dun.addMessage(s);
		if(dun.getLevel(PlayScreen.getPlayer().getIndexDungeonLevel()).getTile(item.getPosition()).getThingsOnTile().size()>1){
			dun.addMessage(messages.getString("someThingsUnder"));
		}
		return null;
	}
	
	
	@Override
	public void display(SGPane console){
		Dungeon dun = PlayScreen.getDungeon();
		ArrayList<Drawable> inv = this.getPlayer().getEverythingOnView();
		Util.clearTextArea();
		Util.printText(0, 0, messages.getString("exIScrWhatToExamine"));
		int row = 1;
		String letters = "qwertyuiopasdfghjklzxcvbnm";
		String toShow;
		String name;
		String n;
		String other;
		for(Drawable i : inv){
			n=blank;
			other=blank;
			name = i.getName();
			if(i instanceof Item){
				Item item =(Item) i;
				name = item.getName();
				if(item.isStackeable()){
					int number = item.getOtherItems().size();
					if(number>=1) n = spaceX +(number+1);
				}
			}
			// WHAT? This should NOT work.
			if(dun.getLevel(this.getPlayer().getIndexDungeonLevel()).getTile(i.getPosition()).getThingsOnTile().size()>1){
				other = messages.getString("exMScrAndSomeOtherThings");
			}
			toShow = Character.toString(letters.charAt(row-1));
			toShow = toShow.concat(dash+name+n+other);
			Util.printText(10, row, toShow);
			row++;
		}
		if(RoguelikeMain.isBlindMode())try {RoguelikeMain.getTextArea().setCaretPosition(RoguelikeMain.getTextArea().getLineStartOffset(0));} catch (BadLocationException e){e.printStackTrace();}
		console.refresh();
	}

	@Override
	public Screen reactToInput(KeyEvent key) {
		char c = key.getKeyChar();

		ArrayList<Drawable> things = getPlayer().getEverythingOnView();
		
		if (letters.indexOf(c) > -1 
				&& things.size() > letters.indexOf(c)
				&& things.get(letters.indexOf(c)) != null){
			return use(things.get(letters.indexOf(c)));
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
