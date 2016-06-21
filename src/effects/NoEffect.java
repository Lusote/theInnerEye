package effects;

import rlmain.Creature;
import rlmain.RoguelikeMain;

public class NoEffect extends Effect {

	static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();
	
	public NoEffect() {
		super(null,1,0,stackTypes.NO);
	}

	@Override
	public void update(){
		Creature creature = this.getCreature();
		if(util.Util.oddsLessThan(creature.getHpRegenerationRate()))
			creature.modifyHp(1);
		if(util.Util.oddsLessThan(creature.getManaRegenerationRate()))
			creature.modifyMana(1);
	}

	@Override
	public boolean start(Creature cre){this.setCreature(cre);return true;}

	@Override
	public void end(){}	

}
