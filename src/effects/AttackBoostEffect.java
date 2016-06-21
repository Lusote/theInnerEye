package effects;

import rlmain.Creature;
import rlmain.RoguelikeMain;

public class AttackBoostEffect extends Effect {

	static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();
	
	public AttackBoostEffect(int amount, int duration, stackTypes stackType){
		super(null,duration,amount,(stackType == null) ? stackTypes.DURATION : stackType);
	}

	@Override
	public void update(){
		this.modifyDuration(-1);
	}

	@Override
	public boolean start(Creature cre){
		this.setCreature(cre);
		Creature creature = this.getCreature();
		creature.setAttack(creature.getAttack()+this.getAmount());
		return true;
	}

	@Override
	public void end() {
		Creature creature = this.getCreature();
		creature.setAttack(creature.getAttack()-this.getAmount());
	}
}
