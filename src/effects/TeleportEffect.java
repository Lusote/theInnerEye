package effects;

import rlmain.Creature;
import rlmain.RoguelikeMain;
import screens.PlayScreen;
import dungeongen.Position;

public class TeleportEffect extends Effect {

	static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();
	
	public TeleportEffect(int duration){
		super(null,duration, 0, stackTypes.NO);
	}
	
	@Override
	public void update(){		
		int i = this.getDuration()-1;
		this.setDuration(i);
	}

	@Override
	public boolean start(Creature cre){
		this.setCreature(cre);
		Creature creature = this.getCreature();
		if(creature.isPlayer())
			creature.getDungeon().addMessage(messages.getString("TPEstart"));
		return true;
	}

	@Override
	public void end() {
		Creature creature = this.getCreature();
		if(creature.isPlayer()) 
			creature.getDungeon().addMessage(messages.getString("instaTPEstart"));
		creature.getTile().setHasCreature(false);
		Position dest = PlayScreen.getDungeon().getLevel(creature.getIndexDungeonLevel()).getRandomFloorPosition();
		PlayScreen.getDungeon().getLevel(creature.getIndexDungeonLevel()).getTile(dest).setCreature(creature);
		PlayScreen.getDungeon().getLevel(creature.getIndexDungeonLevel()).getTile(dest).setHasCreature(true);
		creature.setPosition(dest);
	}

}