package effects;

import items.Item;
import rlmain.Creature;
import rlmain.RoguelikeMain;

public class RemoveAllCurseEffect extends Effect {

	static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();
	
	public RemoveAllCurseEffect(){
		super(null,-1,-1,stackTypes.NO);
	}
	@Override
	public void update(){
		this.modifyDuration(-1);;
	}

	@Override
	public boolean start(Creature cre){
		this.setCreature(cre);
		Creature creature = this.getCreature();
		boolean toReturn = false;
		for(Item i : creature.getInventory().getItems()){
			if(i.isCursed()){
				i.setCursed(false);
				toReturn = true;
			}
			i.setCurseKnown(true);
		}
		if(creature.isPlayer() && toReturn) creature.getDungeon().addMessage(messages.getString("removeAllCurseEstartTrue"));
		if(!toReturn)creature.getDungeon().addMessage(messages.getString("removeAllCurseEstartFalse"));
		return toReturn;
	}

	@Override
	public void end() {		
	}

}
