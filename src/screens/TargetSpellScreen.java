package screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.text.BadLocationException;

import effects.Effect;
import rlmain.Creature;
import rlmain.RoguelikeMain;
import rlmain.Spell;
import squidpony.squidgrid.gui.SGPane;
import util.Util;

/*
 * TODO:
 * 	Check for more than one Pages, if there are A LOT of items/creatures.
 */
public class TargetSpellScreen implements Screen{

	private Creature player;
	private String letters;
	private Spell spell;
	
	public TargetSpellScreen(Spell sp){
		this.player = PlayScreen.getPlayer();
		this.setLetters("qwertyuiopasdfghjklzxcvbnm");
		this.setSpell(sp);
	}
	
	@Override
	public void display(SGPane console) {
		ArrayList<Creature> inv = new ArrayList<Creature>();
		inv.add(player);
		inv.addAll(this.getPlayer().getCreaturesOnView());
		Util.clearTextArea();
		Util.printText(0, 0, messages.getString("fWeScrWhatToShootAt"));//"Select your target.");
		int row = 1;
		String letters = "qwertyuiopasdfghjklzxcvbnm";
		String toShow;
		for(Creature i : inv){
			toShow = Character.toString(letters.charAt(row-1));
			toShow = toShow.concat(" - "+i.getName());
			Util.printText(10, row, toShow);
			row++;
		}
		if(RoguelikeMain.isBlindMode())try {RoguelikeMain.getTextArea().setCaretPosition(RoguelikeMain.getTextArea().getLineStartOffset(0));} catch (BadLocationException e){e.printStackTrace();}
		console.refresh();
	}

	@Override
	public Screen reactToInput(KeyEvent key) {
		char c = key.getKeyChar();
		ArrayList<Creature> things = new ArrayList<Creature>();
		things.add(player);
		things.addAll(this.getPlayer().getCreaturesOnView());
		if (letters.indexOf(c) > -1 && things.size() > letters.indexOf(c)
				&& things.get(letters.indexOf(c)) != null)
			return use(things.get(letters.indexOf(c)));
		else 
			if (key.getKeyCode() == KeyEvent.VK_ESCAPE)	return null;
			else return this;
	}

	protected Screen use(Creature thing) {
		System.out.println(messages.getString("tspScrYouCast")+spell.getName()+messages.getString("toSpaced")+thing.getName());
		for(Effect ef : spell.getEffects()){
			thing.addEffect(ef);
			if(!thing.isPlayer())
				player.getDungeon().addMessage(messages.getString("tspScrYouCast")+spell.getName()+messages.getString("onSpaced")+thing.getName());
		}
		player.modifyMana(-spell.getManaCost());
		return null;
	}
	
	public String getLetters(){return letters;}

	public void setLetters(String letters){this.letters = letters;}
	
	public Creature getPlayer(){return player;}

	public Spell getSpell(){return spell;}

	public void setSpell(Spell spell){this.spell = spell;}

}
