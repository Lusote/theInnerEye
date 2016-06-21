package effects;

import rlmain.Creature;
import rlmain.RoguelikeMain;
import screens.PlayScreen;
import dungeongen.Position;

public class InstaTeleportEffect extends Effect {

	static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();
	
	private Creature creature;
	
	public InstaTeleportEffect(){
		super(null,-1,-1,stackTypes.NO);
	}

	@Override
	public boolean start(Creature cre){
		this.setCreature(cre);
		this.creature.getTile().setHasCreature(false);
		Position dest = PlayScreen.getDungeon().getLevel(this.creature.getIndexDungeonLevel()).getRandomFloorPosition();
		PlayScreen.getDungeon().getLevel(this.creature.getIndexDungeonLevel()).getTile(dest).setCreature(this.creature);
		PlayScreen.getDungeon().getLevel(this.creature.getIndexDungeonLevel()).getTile(dest).setHasCreature(true);
		this.creature.setPosition(dest);
		if(creature.isPlayer()) {
			creature.getDungeon().addMessage(messages.getString("instaTPEstart"));
		}
		return true;
	}

	@Override
	public void end() {}

	@Override
	public void update() {}

}