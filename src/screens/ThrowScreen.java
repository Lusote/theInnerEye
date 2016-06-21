package screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.text.BadLocationException;

import rlmain.Creature;
import rlmain.RoguelikeMain;
import squidpony.squidgrid.gui.SGPane;
import util.Util;

/*
 * TODO:
 * 	Check for more than one Pages, if there are A LOT of items/creatures.
 * 
 * 
 */
public class ThrowScreen implements Screen{

	private Creature player;
	private String letters;
	
	public ThrowScreen(){
		this.player = PlayScreen.getPlayer();
		this.setLetters("qwertyuiopasdfghjklzxcvbnm");
	}
	
	@Override
	public void display(SGPane console) {
		ArrayList<Creature> inv = this.getPlayer().getCreaturesOnView();
		Util.clearTextArea();
		Util.printText(0, 0, messages.getString("fWeScrWhatToShootAt"));
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
		ArrayList<Creature> things = getPlayer().getCreaturesOnView();
		if (letters.indexOf(c) > -1 && things.size() > letters.indexOf(c)
				&& things.get(letters.indexOf(c)) != null)
			return use(things.get(letters.indexOf(c)));
		else 
			if (key.getKeyCode() == KeyEvent.VK_ESCAPE)	return null;
			else return this;
	}

	protected Screen use(Creature thing) {
		System.out.println("You want to throw "+this.getPlayer().getQuiveredWeapon().getName()+" to "+thing.getName());
		this.player.throwAttack((Creature) thing);
		//new AnimationScreen(new AnimatedFlyingItem(this.getPlayer().getQuiveredWeapon().getSymbol(),this.getPlayer().getQuiveredWeapon().getColor(),this.getPlayer().getPosition(),this.getPlayer().getPosition(),thing.getPosition()));
		return null;
	}
	
	public String getLetters(){return letters;}

	public void setLetters(String letters){this.letters = letters;}
	
	public Creature getPlayer(){return player;}

}
