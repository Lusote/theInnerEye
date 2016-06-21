package effects;

import rlmain.Creature;
import rlmain.RoguelikeMain;

public class BlindEffect extends Effect {

	static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();
	
	public BlindEffect(int duration){
		super(null, duration, 0,stackTypes.DURATION);
	}

	public void update(){
		this.modifyDuration(-1);
	}

	public boolean start(Creature cre){
		this.setCreature(cre);
		Creature creature = this.getCreature();
		if(creature.isPlayer()) 
			creature.getDungeon().addMessage(messages.getString("blindEstart"));
		creature.setVisionRadius(0);
		return true;
	}

	@Override
	public void end() {
		Creature creature = this.getCreature();
		if(creature.isPlayer()) 
			creature.getDungeon().addMessage(messages.getString("blindEend"));
		creature.setVisionRadius(creature.getVisionRadiusDef());
	}

}