package effects;

import rlmain.Creature;
import rlmain.RoguelikeMain;

public class HealingEffect extends Effect {

	static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();
	
	
	public HealingEffect(int duration, int amountPerTurn){
		super(null,duration,amountPerTurn,stackTypes.DURATION);
	}
	@Override
	public void update(){
		this.modifyDuration(-1);
		this.getCreature().modifyHp(this.getAmount());
	}

	@Override
	public boolean start(Creature cre){ this.setCreature(cre);return true;}

	@Override
	public void end(){}
	

}
