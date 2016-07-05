package effects;

import rlmain.Creature;
import rlmain.RoguelikeMain;
import screens.PlayScreen;
import dungeongen.Position;

public class InstaTeleportEffect extends Effect {

	static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();
	
	public InstaTeleportEffect(){
		super(null,-1,-1,stackTypes.NO);
	}

	@Override
	public boolean start(Creature cre){
		this.setCreature(cre);
		this.getCreature().getTile().setHasCreature(false);
		Position dest = PlayScreen.getDungeon().getLevel(this.getCreature().getIndexDungeonLevel()).getRandomFloorPosition();
		PlayScreen.getDungeon().getLevel(this.getCreature().getIndexDungeonLevel()).getTile(dest).setCreature(this.getCreature());
		PlayScreen.getDungeon().getLevel(this.getCreature().getIndexDungeonLevel()).getTile(dest).setHasCreature(true);
		this.getCreature().setPosition(dest);
		if(getCreature().isPlayer()) {
			getCreature().getDungeon().addMessage(messages.getString("instaTPEstart"));
		}
		return true;
	}

	@Override
	public void end() {}

	@Override
	public void update() {}

}