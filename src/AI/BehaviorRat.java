package AI;

import rlmain.Creature;
import rlmain.RoguelikeMain;
import util.Util;

public class BehaviorRat extends CreatureBehavior {
	
	private static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();

	public BehaviorRat(Creature creature) {
		super(creature);
		creature.setNoise(RoguelikeMain.messages.getString("noiseRat"));
	}

	public void onUpdate(){
		//System.out.println("BehaviorRat onUpdate");
		wander(false);
		wander(false);
		if(Util.oddsLessThan(0.10))
			this.getCreature().makeNoise();
	}
}
