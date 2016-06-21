package factories;

import items.Potion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import rlmain.Creature;
import rlmain.RoguelikeMain;
import screens.PlayScreen;
import dungeongen.Dungeon;
import dungeongen.Position;
import dungeongen.Tile;
import effects.BlindEffect;
import effects.Effect;
import effects.InstaHealEffect;

public class PotionFactory {

	public static ResourceBundle messages = RoguelikeMain.messages;
	private static Dungeon dungeon;
	private static boolean createdPotions = false;
	public enum potionTypes{
		HEALING, BLIND, RANDOM;
	}
	private static final List<potionTypes> potionTypesValues = Collections.unmodifiableList(Arrays.asList(potionTypes.values()));
	
	private static final int potionTypesSize = potionTypesValues.size();
	
	private static potionTypes getRandomType(){return potionTypesValues.get(PlayScreen.getTheRNG().nextInt(potionTypesSize));}
	
	static Potion newPotion(Creature cre,potionTypes type, int level){
		dungeon = PlayScreen.getDungeon();
		while(type == potionTypes.RANDOM)  
			type = getRandomType();
		if(!createdPotions) {
			items.Potion.setUpPotionAppearances();
			createdPotions=true;
		}
		Tile t = dungeon.getLevel(level).getRandomTile();
		switch(type){
			case HEALING:
				return newHealingPotion(t.getPosition(), level, cre);
			case BLIND:
				return newBlindPotion(t.getPosition(), level, cre);
			default:
				break;
		}
		return null;
	}

	private static Potion newHealingPotion(Position p, int lev, Creature cre) {
		ArrayList<Effect> eff = new ArrayList<Effect>();
		eff.add(new InstaHealEffect(20));
		ArrayList<String> desc = new ArrayList<String>();
		desc.add(messages.getString("A")+Potion.potionAppearances.get(0)+messages.getString("dot"));
		Potion toReturn = new Potion('!', p, lev, Potion.potionAppearances.get(0), desc, eff, 
				messages.getString("healingPotName"), Potion.potionColors.get(Potion.potionAppearances.get(0)),potionTypes.HEALING);
		if(cre != null)
			dungeon.getCreature(cre.getId()).getInventory().addToInventory(toReturn);
		else
			dungeon.getLevel(lev).getTile(p).putOnTile(toReturn);	
		return toReturn;
	}

	private static Potion newBlindPotion(Position p, int lev, Creature cre) {
		ArrayList<Effect> eff = new ArrayList<Effect>();
		eff.add(new BlindEffect(50));
		ArrayList<String> desc = new ArrayList<String>();
		desc.add("A "+Potion.potionAppearances.get(1)+".");
		Potion toReturn = new Potion('!', p, lev, Potion.potionAppearances.get(1), desc, eff, 
				messages.getString("blindPotName"), Potion.potionColors.get(Potion.potionAppearances.get(1)),potionTypes.BLIND);
		if(cre != null)
			dungeon.getCreature(cre.getId()).getInventory().addToInventory(toReturn);
		else
			dungeon.getLevel(lev).getTile(p).putOnTile(toReturn);	
		return toReturn;
	}

}
