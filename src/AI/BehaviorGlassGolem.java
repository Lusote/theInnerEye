package AI;

import rlmain.Creature;
import rlmain.RoguelikeMain;
import screens.PlayScreen;
import util.Util;


public class BehaviorGlassGolem extends CreatureBehavior {
	
	private static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();

	
	public BehaviorGlassGolem(Creature cre) {
		super(cre);
		cre.setNoise(RoguelikeMain.messages.getString("noiseGlassGolem"));
	}

	public void onUpdate(){
		if(canSee(PlayScreen.getPlayer().getTile()))
			hunt(PlayScreen.getPlayer());
		else{
			if(this.getCreature().isHunting()){
				this.getCreature().setHunting(false);
				PlayScreen.getDungeon().addMessage(RoguelikeMain.messages.getString("theSpace")+this.getCreature().getName()+RoguelikeMain.messages.getString("stopsHuntingYou"));
			}
			if(Util.oddsLessThan(0.10)) this.getCreature().makeNoise();
			if(!this.getCreature().isAnyoneArround())
				wander(false);
		}
	}
	
}
