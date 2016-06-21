package effects;

import rlmain.Creature;
import rlmain.RoguelikeMain;
import screens.PlayScreen;

public class TheSyweroffIdolEffect extends Effect {

	static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();

	public TheSyweroffIdolEffect() {
		super(null,1,0,stackTypes.AMOUNT);
	}

	@Override
	public void update(){
		Creature creature = this.getCreature();
		if(util.Util.oddsLessThan(0.20))
			creature.modifyHp(-1);
		creature.modifyHunger(-1);
	}

	@Override
	public boolean start(Creature cre){
		this.setCreature(cre);
		PlayScreen.getDungeon().addMessage(messages.getString("getIdolEffect"));
		return true;
	}

	@Override
	public void end() {
	}

	
	

}
