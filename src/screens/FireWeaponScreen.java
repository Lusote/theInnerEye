package screens;

import items.RangedWeapon;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.text.BadLocationException;

import rlmain.Creature;
import rlmain.Drawable;
import rlmain.RoguelikeMain;
import squidpony.squidgrid.gui.SGPane;
import util.Util;

/*
 * TODO:
 * 	Check for more than one Pages, if there are A LOT of items/creatures.
 * 
 * 
 */
public class FireWeaponScreen implements Screen{

	private Creature player;
	private String letters;
	private int range;
	
	public FireWeaponScreen(){
		this.player = PlayScreen.getPlayer();;
		this.setLetters("qwertyuiopasdfghjklzxcvbnm");
		RangedWeapon r = (RangedWeapon) player.getWeapon();
		this.range = r.getRange();
	}
	
	@Override
	public void display(SGPane console) {
		ArrayList<Creature> inv = this.getPlayer().getCreaturesOnRange(range);
		Util.clearTextArea();
		Util.printText(0, 0, messages.getString("fWeScrWhatToShootAt"));
		int row = 1;
		String letters = "qwertyuiopasdfghjklzxcvbnm";
		String toShow;
		for(Drawable i : inv){
			toShow = Character.toString(letters.charAt(row-1));
			toShow = toShow.concat(dash+i.getName());
			Util.printText(10, row, toShow);
			row++;
		}
		if(RoguelikeMain.isBlindMode())try {RoguelikeMain.getTextArea().setCaretPosition(RoguelikeMain.getTextArea().getLineStartOffset(0));} catch (BadLocationException e){e.printStackTrace();}
		console.refresh();
	}

	@Override
	public Screen reactToInput(KeyEvent key) {
		char c = key.getKeyChar();
		ArrayList<Creature> things = getPlayer().getCreaturesOnRange(range);
		if (letters.indexOf(c) > -1 && things.size() > letters.indexOf(c)
				&& things.get(letters.indexOf(c)) != null)
			return use(things.get(letters.indexOf(c)));
		else 
			if (key.getKeyCode() == KeyEvent.VK_ESCAPE)	return null;
			else return this;
	}

	protected Screen use(Creature thing) {
		//System.out.println("You want to fire with your "+this.getPlayer().getWeapon().getName()+" to "+thing.getName());
		RangedWeapon w = (RangedWeapon) this.getPlayer().getWeapon();
		if(this.getPlayer().getInventory().getAMissile(w.getAmmo())!=null){
			this.player.rangedAttack(thing);
		}
		return null;
	}
	
	public String getLetters(){return letters;}

	public void setLetters(String letters){this.letters = letters;}
	
	public Creature getPlayer(){return player;}

}
