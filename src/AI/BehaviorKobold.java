package AI;

import items.Item.itemType;
import rlmain.Creature;
import rlmain.RoguelikeMain;
import screens.PlayScreen;
import util.Util;


public class BehaviorKobold extends CreatureBehavior {
	
	private static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();

	
	public BehaviorKobold(Creature cre) {
		super(cre);
		cre.setNoise(RoguelikeMain.messages.getString("noiseKobold"));
	}

	public void onUpdate(){
		if(hasItem(itemType.MELEEWEAPON) && !hasWeaponEquipped()){
			equipAny(itemType.MELEEWEAPON);
			return;
		}
		if(canSee(PlayScreen.getPlayer().getTile()))
			hunt(PlayScreen.getPlayer());
		else{
			if(this.getCreature().isHunting()){
				this.getCreature().setHunting(false);
				PlayScreen.getDungeon().addMessage(RoguelikeMain.messages.getString("theSpace")+this.getCreature().getName()+RoguelikeMain.messages.getString("stopsHuntingYou"));
			}
			if(Util.oddsLessThan(0.10)) this.getCreature().makeNoise();
			wander(true);
		}
	}
	
}
