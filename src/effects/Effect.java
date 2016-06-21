package effects;

import java.io.Serializable;
import java.util.ResourceBundle;

import rlmain.Creature;
import rlmain.RoguelikeMain;

public abstract class Effect implements Serializable{

	public static ResourceBundle messages = RoguelikeMain.messages;
	static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();
	
	private Creature cre;
	private int id;
	private int duration;
	private int amount;
	private int originalDuration;
	private int originalAmount;
	private stackTypes stackType;
	private static int numberEffectsmade = 0;
	public enum stackTypes {DURATION, AMOUNT, NO}
	
	
	public Effect(Creature cre, int duration, int amount, stackTypes stackType){
		this.cre = cre;
		this.duration = duration;
		this.originalDuration = duration;		
		this.amount = amount;
		this.originalAmount = amount;
		this.stackType = stackType;
		this.id = numberEffectsmade;
		numberEffectsmade++;
	}
	
	public void setCreature(Creature cre){this.cre = cre;}
	
	public Creature getCreature(){return this.cre;}

	public void setDuration(int duration){this.duration = duration;}
	
	public int getDuration(){return this.duration;}	
	
	public void modifyDuration(int amount){this.setDuration(this.getDuration()+amount);}

	public void setAmount(int amount){this.amount = amount;}
	
	public int getAmount(){return this.amount;}
	
	/**
	 * Updates the effect each turn.
	 */
	public abstract void update();
	
	/**
	 * Initializes the effect.
	 * @param cre Creature which has the effect.
	 * @return True if the effect has started, false otherwise.
	 */
	public abstract boolean start(Creature cre);
	
	/**
	 * Ends the effect.
	 */
	public abstract void end();

	public stackTypes getStacktype(){return this.stackType;}

	public int getOriginalDuration(){return originalDuration;}

	public void setOriginalDuration(int originalDuration){this.originalDuration = originalDuration;}

	public int getOriginalAmount(){return originalAmount;}

	public void setOriginalAmount(int originalAmount){this.originalAmount = originalAmount;}

	public int getId(){return id;}

}
