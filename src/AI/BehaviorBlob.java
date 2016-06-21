package AI;

import rlmain.Creature;
import rlmain.RoguelikeMain;
import util.Util;



public class BehaviorBlob extends CreatureBehavior {
	
	private static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();

	public BehaviorBlob(Creature cre) {
		super(cre);
		cre.setNoise(RoguelikeMain.messages.getString("noiseBlob"));
	}

	public void onUpdate(){
		//explodes, maybe?
		if(Util.oddsLessThan(0.10))
			wander(false);
		if(Util.oddsLessThan(0.10))
			this.getCreature().makeNoise();
	}
	
}
