package factories;

import items.Scroll;

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
import effects.InstaTeleportEffect;
import effects.RemoveAllCurseEffect;
import effects.RemoveWeaponCurseEffect;

public class ScrollFactory {

	public static ResourceBundle messages = RoguelikeMain.messages;
	private static Dungeon dungeon;
	private static boolean createdScrolls = false;
	public enum ScrollTypes{
		HEALING, BLIND, TELEPORT, REMOVECURSEALL, REMOVECURSEWEAPON, RANDOM;
	}
	private static final List<ScrollTypes> ScrollTypesValues = Collections.unmodifiableList(Arrays.asList(ScrollTypes.values()));
	
	private static final int ScrollTypesSize = ScrollTypesValues.size();
	
	private static ScrollTypes getRandomType(){return ScrollTypesValues.get(PlayScreen.getTheRNG().nextInt(ScrollTypesSize));}
	
	//WHEN ADDING MORE SCROLLS:
	// add them to the enum
	// when creating the object toReturn in their own method, be careful with the  Scroll.scrollAppearances.get(I),
	//	you need to increment the I
	
	static Scroll newScroll(Creature cre, ScrollTypes type, int level){
		dungeon = PlayScreen.getDungeon();
		while(type == ScrollTypes.RANDOM)  
			type = getRandomType();
		//System.out.println("Creating new scroll!! FT: "+type);
		if(!createdScrolls) {
			items.Scroll.setUpScrollAppearances();
			createdScrolls=true;
		}
		Tile t = dungeon.getLevel(level).getRandomTile();
		switch(type){
			case HEALING:
				return newHealingScroll(t.getPosition(), level, cre);
			case BLIND:
				return newBlindScroll(t.getPosition(), level, cre);
			case TELEPORT:
				return newInstaTPScroll(t.getPosition(), level, cre);
			case REMOVECURSEALL:
				return newRemoveAllCursesScroll(t.getPosition(), level, cre);
			case REMOVECURSEWEAPON:
				return newRemoveWeaponCurseScroll(t.getPosition(), level, cre);
			default:
				break;
		}
		return null;
	}



	private static Scroll newHealingScroll(Position p, int lev, Creature cre) {
		ArrayList<Effect> eff = new ArrayList<Effect>();
		eff.add(new InstaHealEffect(20));
		ArrayList<String> desc = new ArrayList<String>();
		desc.add(messages.getString("A")+Scroll.scrollAppearances.get(0)+messages.getString("spaceScroll"));
		Scroll toReturn = new Scroll('?', p, lev, Scroll.scrollAppearances.get(0), desc, eff, 
				messages.getString("healingScrName"), Scroll.scrollNames.get(Scroll.scrollAppearances.get(0)),ScrollTypes.HEALING);
		if(cre != null)
			dungeon.getCreature(cre.getId()).getInventory().addToInventory(toReturn);
		else
			dungeon.getLevel(lev).getTile(p).putOnTile(toReturn);	
		return toReturn;
	}

	private static Scroll newBlindScroll(Position p, int lev, Creature cre) {
		ArrayList<Effect> eff = new ArrayList<Effect>();
		eff.add(new BlindEffect(50));
		ArrayList<String> desc = new ArrayList<String>();
		desc.add(messages.getString("A")+Scroll.scrollAppearances.get(1)+messages.getString("spaceScroll"));
		Scroll toReturn = new Scroll('?', p, lev, Scroll.scrollAppearances.get(1), desc, eff, 
				messages.getString("blindScrName"), Scroll.scrollNames.get(Scroll.scrollAppearances.get(1)),ScrollTypes.BLIND);
		if(cre != null)
			dungeon.getCreature(cre.getId()).getInventory().addToInventory(toReturn);
		else
			dungeon.getLevel(lev).getTile(p).putOnTile(toReturn);	
		return toReturn;
	}
	
	private static Scroll newInstaTPScroll(Position p, int lev, Creature cre) {
		ArrayList<Effect> eff = new ArrayList<Effect>();
		eff.add(new InstaTeleportEffect());
		ArrayList<String> desc = new ArrayList<String>();
		desc.add(messages.getString("A")+Scroll.scrollAppearances.get(2)+messages.getString("spaceScroll"));
		Scroll toReturn = new Scroll('?', p, lev, Scroll.scrollAppearances.get(2), desc, eff, 
				messages.getString("teleportScrName"), Scroll.scrollNames.get(Scroll.scrollAppearances.get(2)),ScrollTypes.TELEPORT);
		if(cre != null)
			dungeon.getCreature(cre.getId()).getInventory().addToInventory(toReturn);
		else
			dungeon.getLevel(lev).getTile(p).putOnTile(toReturn);	
		return toReturn;
	}
	
	private static Scroll newRemoveAllCursesScroll(Position p, int lev, Creature cre) {
		ArrayList<Effect> eff = new ArrayList<Effect>();
		eff.add(new RemoveAllCurseEffect());
		ArrayList<String> desc = new ArrayList<String>();
		desc.add(messages.getString("A")+Scroll.scrollAppearances.get(3)+messages.getString("spaceScroll"));
		Scroll toReturn = new Scroll('?', p, lev, Scroll.scrollAppearances.get(3), desc, eff, 
				messages.getString("removeCurseAllScrName"), Scroll.scrollNames.get(Scroll.scrollAppearances.get(3)),ScrollTypes.REMOVECURSEALL);
		if(cre != null)
			dungeon.getCreature(cre.getId()).getInventory().addToInventory(toReturn);
		else
			dungeon.getLevel(lev).getTile(p).putOnTile(toReturn);	
		return toReturn;
	}

	private static Scroll newRemoveWeaponCurseScroll(Position p,int lev, Creature cre) {
		ArrayList<Effect> eff = new ArrayList<Effect>();
		eff.add(new RemoveWeaponCurseEffect());
		ArrayList<String> desc = new ArrayList<String>();
		desc.add(messages.getString("A")+Scroll.scrollAppearances.get(4)+messages.getString("spaceScroll"));
		Scroll toReturn = new Scroll('?', p, lev, Scroll.scrollAppearances.get(4), desc, eff, 
				messages.getString("removeCurseWeaponScrName"), Scroll.scrollNames.get(Scroll.scrollAppearances.get(4)),ScrollTypes.REMOVECURSEWEAPON);
		if(cre != null)
			dungeon.getCreature(cre.getId()).getInventory().addToInventory(toReturn);
		else
			dungeon.getLevel(lev).getTile(p).putOnTile(toReturn);	
		return toReturn;
	}

}
