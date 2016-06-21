package effects;

import rlmain.Creature;
import rlmain.RoguelikeMain;

public class InstaHealEffect extends Effect {

	static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();
	
	public InstaHealEffect(int amount){
		super(null,0,amount,stackTypes.NO);
	}

	@Override
	public void update(){
		this.modifyDuration(-1);
	}

	@Override
	public boolean start(Creature cre){
		this.setCreature(cre);
		Creature creature = this.getCreature();
		if(creature.isPlayer()) creature.getDungeon().addMessage(messages.getString("instaHealEstart"));
		creature.modifyHp(this.getAmount());
		return true;
	}

	@Override
	public void end(){}
}
