package screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.text.BadLocationException;

import rlmain.Creature;
import rlmain.RoguelikeMain;
import rlmain.Spell;
import squidpony.squidgrid.gui.SGPane;
import util.Util;

/*
 * TODO:
 * 	Check for more than one Pages, if there are A LOT of items/creatures.
 * 
 * 
 */
public class CastScreen implements Screen{

	private Creature player;
	private String letters;
	
	public CastScreen(){
		this.player = PlayScreen.getPlayer();
		this.setLetters("qwertyuiopasdfghjklzxcvbnm");
	}
	
	@Override
	public void display(SGPane console) {
		Util.clearTextArea();
		ArrayList<Spell> spellList = player.getSpellsKnown();
		Util.printText(0, 0, messages.getString("cstStrSelectSpell"));
		int row = 1;
		String letters = "qwertyuiopasdfghjklzxcvbnm";
		String toShow;
		for(Spell s : spellList){
			toShow = Character.toString(letters.charAt(row-1));
			toShow = toShow.concat(dash
					+s.getName()
					+dash
					+s.getManaCost()
					+messages.getString("spacemp"))
					+dash
					+s.getMinimumIntelligence()
					+messages.getString("spaceINT");
			Util.printText(10, row, toShow);
			row++;
		}
		console.refresh();
		if(RoguelikeMain.isBlindMode())try {RoguelikeMain.getTextArea().setCaretPosition(RoguelikeMain.getTextArea().getLineStartOffset(0));} catch (BadLocationException e){e.printStackTrace();}
	}

	@Override
	public Screen reactToInput(KeyEvent key) {
		char c = key.getKeyChar();
		ArrayList<Spell> things = getPlayer().getSpellsKnown();
		if (letters.indexOf(c) > -1 && things.size() > letters.indexOf(c)
				&& things.get(letters.indexOf(c)) != null)
			return use(things.get(letters.indexOf(c)));
		else 
			if (key.getKeyCode() == KeyEvent.VK_ESCAPE)	return null;
			else return this;
	}

	protected Screen use(Spell thing) {
		if(player.getIntelligence() < thing.getMinimumIntelligence()){
			PlayScreen.getDungeon().addMessage(messages.getString("cstScrNotEnoughInt"));
			return null;			
		}
		if(player.getMana()<thing.getManaCost()){
			PlayScreen.getDungeon().addMessage(messages.getString("cstScrNotEnoughMana"));
			return null;
		}
		return new TargetSpellScreen(thing);
	}
	
	public String getLetters(){return letters;}

	public void setLetters(String letters){this.letters = letters;}
	
	public Creature getPlayer(){return player;}

}

