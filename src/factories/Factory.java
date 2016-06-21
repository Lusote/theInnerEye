package factories;

import items.Ammo;
import items.Ammo.ammoType;
import items.Armor;
import items.Food;
import items.GoldCoins;
import items.Item;
import items.Item.itemMaterial;
import items.Item.itemType;
import items.Potion;
import items.RangedWeapon;
import items.Scroll;
import items.ThrowableWeapon;
import items.Weapon;
import items.Weapon.weaponType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import rlmain.Creature;
import rlmain.Creature.creatureClasses;
import rlmain.RoguelikeMain;
import screens.PlayScreen;
import squidpony.squidcolor.SColor;
import dungeongen.Dungeon;
import dungeongen.Tile;
import effects.Effect;
import factories.CreatureFactory.creatureTypes;
import factories.FoodFactory.foodType;
import factories.PotionFactory.potionTypes;
import factories.ScrollFactory.ScrollTypes;

public class Factory implements Serializable{

	public static ResourceBundle messages = RoguelikeMain.messages;
	private static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();
	
	private static Factory uniqueFactory;

	private Factory() {
	}
	
	public static Factory getFactory(){
		if(uniqueFactory != null) return uniqueFactory;
		else uniqueFactory = new Factory();
		return uniqueFactory;
	}

	public void makeALLTheCreatures() {
		Dungeon dun = PlayScreen.getDungeon();
		int creaturesToMake;
		int creaturesMade = 0;
		System.out.println("Creatures per Level:");
		for(int i = 0; i < dun.getNumOfLevels(); i++){
			creaturesToMake = 4 + PlayScreen.getTheRNG().nextInt(4);
			creaturesMade = 0;
			while(creaturesMade < creaturesToMake){
				getCreature(creatureTypes.RANDOM, i);
				creaturesMade++;
			}
			System.out.println("Level "+i+": "+creaturesMade+" creatures made.");
		}		
	}

	public void makeALLTheFood() {
		Dungeon dun = PlayScreen.getDungeon();
		int caloriesMade = 0;
		int tilesPerLevel = 0;
		System.out.println("Food per Level:");
		for(int i = 0; i < dun.getNumOfLevels(); i++){
			tilesPerLevel = dun.getLevel(i).getFloorPositions().size();
			while(caloriesMade < tilesPerLevel){
				caloriesMade += getFood(null, foodType.RANDOM, i).getNutritionalValue();
			}
			System.out.println("Level "+i+": "+caloriesMade+" calories made.");
			caloriesMade = 0;
		}
	}

	public void makeALLTheItems() {
		Dungeon dun = PlayScreen.getDungeon();
		int itemsToMake = 3 + PlayScreen.getTheRNG().nextInt(3);
		int itemsMade = 0;
		System.out.println("Items per Level:");
		for(int i = 0; i < dun.getNumOfLevels(); i++){
			while(itemsMade < itemsToMake){
				getNewRandomItem(i);
				itemsMade++;
			}
			System.out.println("Level "+i+": "+itemsMade+" items made.");
			itemsMade=0;
		}		
		forgeTheSyweroffIdol();
	}
	
	private void forgeTheSyweroffIdol() {
		Dungeon dun = PlayScreen.getDungeon();
		int lastLevel = dun.getNumOfLevels()-1;
		Tile t = dun.getLevel(lastLevel).getRandomTile();
		ArrayList<String> desc = new ArrayList<String>();
		List<Effect> e = new ArrayList<Effect>();
		desc.add(messages.getString("theIdolDesc"));
		Item theSyweroffRing = new Item('&', SColor.BLACK, t.getPosition(), lastLevel,messages.getString("theIdolName"), 
				itemType.THESYWEROFFIDOL, desc, e, false, false, false, null, -1,0);
		t.putOnTile(theSyweroffRing);
	}

	public Item getNewRandomItem(int level){
		int it = PlayScreen.getTheRNG().nextInt(9);
		Item toReturn = null;
		switch(it){
			case 0:
				return getWeapon(null, weaponType.RANDOM, itemMaterial.RANDOM, level);
			case 1:
				return getShield(null, itemMaterial.RANDOM, level);
			case 2:
				return getHelmet(null, itemMaterial.RANDOM, level);
			case 3: 
				return getAmmo(null, ammoType.RANDOM, itemMaterial.RANDOM, -1, level);
			case 4:
				return getPotion(null, potionTypes.RANDOM, level);
			case 5: 
				return getScroll(null, ScrollTypes.RANDOM, level);
			case 6:
				return getThrowableWeapon(null, -1, -1, level);
			case 7: 
				return getRangedWeapon(null, -1, level);
			case 8: 
				return getGoldCoins(level);
		}
		return toReturn;
	}
	
	public Creature getCreature(creatureTypes type, int level){
		return factories.CreatureFactory.newCreature(type, level);
	}
	
	public Food getFood(Creature cre, foodType type, int level){
		return factories.FoodFactory.newFood(cre,type, level);
	}
	
	public Weapon getWeapon(Creature cre, weaponType type, itemMaterial material, int level){
		return factories.WeaponFactory.newWeapon(cre, type, material, level);
	}
	
	public Armor getShield(Creature cre, itemMaterial material, int level){
		return factories.ArmorFactory.newShield(cre,itemMaterial.RANDOM, level);
	}
	
	public Armor getHelmet(Creature cre, itemMaterial material, int level){
		return factories.ArmorFactory.newHelmet(cre,itemMaterial.RANDOM, level);
	}
	
	public Ammo getAmmo(Creature cre, ammoType type, itemMaterial material, int amount, int level){
		return factories.AmmoFactory.newAmmo(cre, type, material, amount, level);
	}
	
	public Potion getPotion(Creature cre, potionTypes type, int level){
		return factories.PotionFactory.newPotion(cre, type, level);
	}
	
	public Scroll getScroll(Creature cre, ScrollTypes type, int level){
		return factories.ScrollFactory.newScroll(cre, type, level);
	}
	
	public ThrowableWeapon getThrowableWeapon(Creature cre, int type, int amount, int level){
		return factories.ThrowableWeaponFactory.newThrowableWeapon(cre, type, amount, level);
	}
	
	public RangedWeapon getRangedWeapon(Creature cre, int type, int level){
		return factories.RangedWeaponFactory.newRangedWeapon(cre, type, level);
	}
	
	public GoldCoins getGoldCoins(int level){
		return factories.GoldFactory.makeSomeGold(level);
	}

	public static void resetFactory(){		
		uniqueFactory = null;		
		CreatureFactory.uniquePlayer = null;
	}

	public Creature newPlayer(creatureClasses playerClass, String playerName) {
		// TODO Auto-generated method stub
		return CreatureFactory.newPlayer(playerClass, playerName);
	}
}
