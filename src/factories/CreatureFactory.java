package factories;

import items.Item.itemMaterial;
import items.Weapon.weaponType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import rlmain.Creature;
import rlmain.Creature.creatureClasses;
import rlmain.RoguelikeMain;
import rlmain.Spell;
import screens.PlayScreen;
import squidpony.squidcolor.SColor;
import util.Util;
import AI.BehaviorBlob;
import AI.BehaviorGlassGolem;
import AI.BehaviorKobold;
import AI.BehaviorPlayer;
import AI.BehaviorRat;
import dungeongen.Dungeon;
import dungeongen.Position;
import dungeongen.Tile;
import effects.Effect;
import effects.NoEffect;
import effects.PoisonEffect;

public class CreatureFactory {

	public static ResourceBundle messages = RoguelikeMain.messages;
	private static Dungeon dungeon;
	static Creature uniquePlayer; // Singleton players
	public enum creatureTypes{
		RAT, KOBOLD, BLOB, GLASSGOLEM, RANDOM, PLAYER;
	}
	private static final List<creatureTypes> creatureTypesValues = Collections.unmodifiableList(Arrays.asList(creatureTypes.values()));
	
	private static final int creatureTypesSize = creatureTypesValues.size();
	
	private static creatureTypes getRandomType(){return creatureTypesValues.get(PlayScreen.getTheRNG().nextInt(creatureTypesSize));}

	/**
	 * Creates a new Player, calling Playerbuilder
	 * @param playerClass The class of the player
	 * @param name The name of the player.
	 * @return The Player
	 */
	public static Creature newPlayer(creatureClasses playerClass, String name){		
		if(uniquePlayer == null){
			uniquePlayer = playerBuilder(playerClass, name);
		}
		return uniquePlayer;
	}
	
	private static Creature playerBuilder(creatureClasses playerClass, String name){
		dungeon = PlayScreen.getDungeon();
		Tile t = dungeon.getLevel(0).getTile(dungeon.getLevel(0).getStairsUp());
		ArrayList<Effect> eff = new ArrayList<Effect>(Arrays.asList(new NoEffect()));
		Creature player = new Creature();
		player.setIndexDungeonLevel(0);
		player.setSymbol('@');
		player.setColor(SColor.GREEN_YELLOW);
		player.setPosition(t.getPosition());
		player.setMaxHealth(100);
		player.setHp(100);
		player.setMaxMana(100);
		player.setMana(100);
		player.setAttack(10);
		player.setDefense(10);
		player.setDextery(10);
		player.setDodge(3);
		player.setName(name);
		player.setIntelligence(10);
		player.setBehavior(new BehaviorPlayer(player));
		player.setVisionRadius(9);
		player.setVisionRadiusDef(9);
		player.setHearingRadius(9);
		player.setHearingRadiusDef(9);
		player.setEffects(eff);
		player.setClas(playerClass);
		player.setCorpeseEffects(Util.emptyEffectList);
		player.setMaxHunger(player.getMaxHealth()*10);
		player.setHunger(player.getMaxHealth()*5);
		player.setXp(0);
		player.setFavFood();
		player.setLevel(1);
		player.setHPRegenerationRate(0.1);
		player.setManaRegenerationRate(0.5);
		player.setScore(0);
		player.setSpellsKnown(new ArrayList<Spell>());
		player.setHunting(false);
		player.setTraveling(false);
		player.setDestiny(null);
		player.setEnemiesSawLastTurn(null);
		player.setNewInventory(20);
		player.setHelmet(null);
		player.setWeapon(null);
		player.setShield(null);
		player.setChestArmor(null);
		player.setFeet(null);
		player.setQuiveredWeapon(null);	
		player.setMemory(new ArrayList<HashMap<Position, Character>>(Arrays.asList(new HashMap<Position, Character>())));
		player.setType(creatureTypes.PLAYER);
		t.putCreatureInTile(player);
		
		System.out.println("Player created in: "+t.getPosition().toString());
		dungeon.addMessage(messages.getString("playerGreetings"));
		dungeon.addCreature(player);
		player.setClassAtributes(playerClass);
		return player;
	}
	
	public static Creature newCreature(creatureTypes type, int level){
		dungeon = PlayScreen.getDungeon();
		//System.out.println("Creating Creatures. On CreatureFactory.");
		while(type == creatureTypes.RANDOM || type == creatureTypes.PLAYER)  
			type = getRandomType();
		switch(type){
			case RAT:
				return newRat(messages.getString("nameRat"), level);
			case KOBOLD:
				return newKobold(messages.getString("nameKobold"), level);
			case BLOB:
				return newBlob(messages.getString("nameBlob"), level);
			case GLASSGOLEM:
				return newGlassGolem(messages.getString("nameGlassGolem"),level);
			default:
				return newRat(messages.getString("nameRat"), level);
		}
	}
	
	private static Creature newRat(String name, int level){
		Tile t = dungeon.getLevel(level).getRandomEmptyTile();
		Creature rat = ratBuilder(name, level, t);
		t.putCreatureInTile(rat);
		System.out.println("Creating rat: "+rat.getId()+" in "+rat.getPosition().toString()+" Level: "+level);
		return rat;
	}
	
	private static Creature ratBuilder(String name, int level, Tile t){
		ArrayList<String> desc = new ArrayList<String>(Arrays.asList(messages.getString("descRat")));
		ArrayList<Effect> eff = new ArrayList<Effect>(Arrays.asList(new NoEffect()));
		ArrayList<String> eatMsg = new ArrayList<String>(Arrays.asList(messages.getString("eatRatmsg")));
		
		Creature rat = new Creature();
		rat.setName(messages.getString("nameRat"));
		rat.setBehavior(new BehaviorRat(rat));
		rat.setIndexDungeonLevel(level);
		rat.setSymbol('r');
		rat.setColor(SColor.BROWNER);
		rat.setPosition(t.getPosition());
		rat.setMaxHealth(5);
		rat.setHp(5);
		rat.setAttack(2);
		rat.setDefense(1);
		rat.setDextery(1);
		rat.setDodge(1);
		rat.setNutrValue(20);
		rat.setXp(10);
		rat.setEatCorpseMessage(eatMsg);
		rat.setDescription(desc);
		rat.setEffects(eff);
		rat.setCorpseEffect(Util.emptyEffectList);
		rat.setHPRegenerationRate(0.05);
		rat.setNoise(messages.getString("noiseRat"));
		rat.setType(creatureTypes.RAT);
		
		PlayScreen.getDungeon().getCreatures().add(rat);	
		return rat;
	}
	
	private static Creature newKobold(String name, int level){
		Tile t = dungeon.getLevel(level).getRandomEmptyTile();
		Creature kob = koboldBuilder(name, level, t);
		t.putCreatureInTile(kob);	
		System.out.println("Creating kobold: "+kob.getId()+" in "+kob.getPosition().toString()+" Level: "+level);
		return kob;
	}
	
	public static Creature koboldBuilder(String name, int level, Tile t){
		ArrayList<String> desc = new ArrayList<String>(Arrays.asList(messages.getString("descKobold")));
		ArrayList<Effect> eff = new ArrayList<Effect>(Arrays.asList(new NoEffect()));
		ArrayList<String> eatMsg = new ArrayList<String>(Arrays.asList(messages.getString("eatKoboldmsg")));
		ArrayList<Effect> corpseEff = new ArrayList<Effect>(Arrays.asList(new PoisonEffect(10, 2)));
		
		Creature kobold = new Creature();
		kobold.setIndexDungeonLevel(level);
		kobold.setSymbol('k');
		kobold.setColor(SColor.LIME_GREEN);
		kobold.setPosition(t.getPosition());
		kobold.setMaxHealth(5);
		kobold.setHp(5);
		kobold.setAttack(3);
		kobold.setDefense(3);
		kobold.setDextery(1);
		kobold.setDodge(2);
		kobold.setName(messages.getString("nameKobold"));
		kobold.setNutrValue(10);
		kobold.setXp(20);
		kobold.setEatCorpseMessage(eatMsg);
		kobold.setDescription(desc);
		kobold.setEffects(eff);
		kobold.setCorpeseEffects(corpseEff);
		kobold.setHPRegenerationRate(0.1);
		kobold.setBehavior(new BehaviorKobold(kobold));
		kobold.setVisionRadius(5);
		kobold.setNewInventory(10);
		kobold.setNoise(messages.getString("noiseKobold"));
		kobold.setType(creatureTypes.KOBOLD);
		if(Util.getHeadsOrTails()){
			kobold.setWeapon(null);
			kobold.setWeapon(PlayScreen.getFactory().getWeapon(kobold, weaponType.SWORD,itemMaterial.RANDOM, 0));
		}
		PlayScreen.getDungeon().getCreatures().add(kobold);	
		return kobold;		
	}
	
	private static Creature newBlob(String name, int level){
		Tile t = dungeon.getLevel(level).getRandomEmptyTile();
		Creature blob = blobBuilder(name, level, t);
		t.putCreatureInTile(blob);	
		System.out.println("Creating blob: "+blob.getId()+" in "+blob.getPosition().toString()+" Level: "+level);
		return blob;
	}
	
	public static Creature blobBuilder(String name, int level, Tile t){
		ArrayList<String> desc = new ArrayList<String>(Arrays.asList(messages.getString("descBlob")));
		ArrayList<Effect> eff = new ArrayList<Effect>(Arrays.asList(new NoEffect()));
		ArrayList<String> eatMsg = new ArrayList<String>(Arrays.asList(messages.getString("eatBlobmsg")));
		ArrayList<Effect> corpseEff = Util.emptyEffectList;
		
		Creature blob = new Creature();
		blob.setIndexDungeonLevel(level);
		blob.setSymbol('b');
		blob.setColor(SColor.AMBER);
		blob.setPosition(t.getPosition());
		blob.setMaxHealth(30);
		blob.setHp(30);
		blob.setAttack(3);
		blob.setDefense(8);
		blob.setDefense(3);
		blob.setDextery(0);
		blob.setDodge(0);
		blob.setName(messages.getString("nameBlob"));
		blob.setNutrValue(10);
		blob.setXp(15);
		blob.setEatCorpseMessage(eatMsg);
		blob.setDescription(desc);
		blob.setEffects(eff);
		blob.setCorpeseEffects(corpseEff);
		blob.setHPRegenerationRate(0.1);
		blob.setBehavior(new BehaviorBlob(blob));
		blob.setNoise(messages.getString("noiseBlob"));
		blob.setVisionRadius(5);
		blob.setType(creatureTypes.BLOB);
		PlayScreen.getDungeon().getCreatures().add(blob);	
		return blob;	
	
	}

	public static Creature newGlassGolem(String name, int level){
		Tile t = dungeon.getLevel(level).getRandomEmptyTile();
		Creature glassGolem = glassGolemBuilder(name, level, t);
		t.putCreatureInTile(glassGolem);	
		System.out.println("Creating glass golem: "+glassGolem.getId()+" in "+glassGolem.getPosition().toString()+" Level: "+level);
		return glassGolem;		
	}
	
	public static Creature glassGolemBuilder(String name, int level, Tile t){
		ArrayList<String> desc = new ArrayList<String>(Arrays.asList(messages.getString("descGlassGolem")));
		ArrayList<Effect> eff = new ArrayList<Effect>(Arrays.asList(new NoEffect()));
		ArrayList<String> eatMsg = new ArrayList<String>(Arrays.asList(messages.getString("eatGlassGolemmsg")));
		ArrayList<Effect> corpseEff = Util.emptyEffectList;
		
		Creature glassGolem = new Creature();
		glassGolem.setIndexDungeonLevel(level);
		glassGolem.setSymbol('G');
		glassGolem.setColor(SColor.CYAN);
		glassGolem.setPosition(t.getPosition());
		glassGolem.setMaxHealth(1);
		glassGolem.setHp(1);
		glassGolem.setAttack(999);
		glassGolem.setDefense(1);
		glassGolem.setDextery(999);
		glassGolem.setDodge(0);
		glassGolem.setName(messages.getString("nameGlassGolem"));
		glassGolem.setNutrValue(1);
		glassGolem.setEatCorpseMessage(eatMsg);
		glassGolem.setDescription(desc);
		glassGolem.setEffects(eff);
		glassGolem.setXp(7);
		glassGolem.setCorpeseEffects(corpseEff);
		glassGolem.setHPRegenerationRate(0.1);
		glassGolem.setBehavior(new BehaviorGlassGolem(glassGolem));
		glassGolem.setNoise(messages.getString("noiseGlassGolem"));
		glassGolem.setVisionRadius(20);
		glassGolem.setType(creatureTypes.GLASSGOLEM);
		PlayScreen.getDungeon().getCreatures().add(glassGolem);	
		return glassGolem;	
	
	}
}
