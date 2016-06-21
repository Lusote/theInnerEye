package effects;

import rlmain.Creature;
import rlmain.RoguelikeMain;

public class IntBoostEffect extends Effect {

	static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();
	
	private Creature creature;
	private stackTypes stacktype;
	
	public IntBoostEffect(int amount){
		super(null, 1,amount, stackTypes.AMOUNT);
	}
	
	@Override
	public void update(){}

	public boolean start(Creature cre){
		this.creature = cre;
		creature.setIntelligence(creature.getIntelligence()+this.getAmount());
		return true;
	}

	@Override
	public void end() {
		this.creature.setIntelligence(creature.getIntelligence()-this.getAmount());
		this.creature = null;
	}
	
	public stackTypes getStacktype() {
		return stacktype;
	}

	public void setStacktype(stackTypes stacktype) {
		this.stacktype = stacktype;
	}

}

