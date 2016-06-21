package effects;

import items.Item;
import rlmain.Creature;
import rlmain.RoguelikeMain;

public class RemoveWeaponCurseEffect extends Effect {

	static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();
	
	
	public RemoveWeaponCurseEffect(){
		super(null,-1,0,stackTypes.NO);
	}

	@Override
	public void update(){
		this.modifyDuration(-1);
	}

	@Override
	public boolean start(Creature cre){
		this.setCreature(cre);
		Creature creature = this.getCreature();
		Item i = creature.getWeapon();
		if(!creature.isPlayer())
			return false;
		if(i == null || !i.isCursed()){
			creature.getDungeon().addMessage(messages.getString("removeAllCurseEstartFalse"));
			return false;
		}
		if(i.isCursed()){
			creature.getDungeon().addMessage(messages.getString("removeWeaponCursestartTrue"));
			return true;
		}
		return false;
	}

	@Override
	public void end(){}
}
