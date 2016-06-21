package factories;

import items.Ammo;
import items.Ammo.ammoType;
import items.Item;
import items.Item.itemMaterial;

import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import rlmain.Creature;
import rlmain.RoguelikeMain;
import screens.PlayScreen;
import squidpony.squidcolor.SColor;
import dungeongen.Dungeon;
import dungeongen.Tile;

public class AmmoFactory {

	public static ResourceBundle messages = RoguelikeMain.messages;
	private static Dungeon dungeon;
	private static Random THERNG;

	/**
	 * Creates a new ammo.
	 * @param cre The creature which will receive the ammo, null if it's going to a random tile.
	 * @param type The type of the ammo.
	 * @param material The material of the ammo.
	 * @param amount The amount of ammo to be created, -1 for a random amount (10-20)
	 * @param level The level of the dungeon.
	 * @return The ammo.
	 */
	static Ammo newAmmo(Creature cre, ammoType type, itemMaterial material, int amount, int level){
		dungeon = PlayScreen.getDungeon();
		THERNG = PlayScreen.getTheRNG();
		while(type == ammoType.RANDOM)
			type = Ammo.getRandomAmmoType();
		while(material == itemMaterial.RANDOM)
			material = Item.getRandomMaterial();
		if(amount == -1)
			amount = 10+THERNG.nextInt(10);
		Ammo a = null;
		switch(type){
			case ARROW:
				a= newArrow(cre, material, amount, level);
				break;
			case BOLT:
				a = newBolt(cre, material, amount, level);
				break;
			default:
				break;
		}
		return a;
	}

	/**
	 * Creates a new arrow.
	 * @param cre The creature which will receive the arrow, null if it's going to a random tile.
	 * @param material The material of the arrow.
	 * @param amount The amount of arrows to be created
	 * @param level The level of the dungeon.
	 * @return The arrows.
	 */
	private static Ammo newArrow(Creature cre, itemMaterial material, int amount, int level){
		//System.out.println("Creating an arrow.");		
		Tile t = dungeon.getLevel(level).getRandomTile();
		String name = messages.getString("arrowName");
		ArrayList<String> desc = new ArrayList<String>();
		desc.add(messages.getString("arrowDesc"));
		Ammo arrow = new Ammo('\\', SColor.YELLOW , t.getPosition(), level, name,desc,1,null,"",Ammo.ammoType.ARROW, material);
		for(int i = 1; i < amount; i++){
			arrow.addItemToArray(new Ammo('\\', SColor.YELLOW , t.getPosition(), level, name,desc,1,null,"",Ammo.ammoType.ARROW, material));
		}				
		if(cre == null)t.putOnTile(arrow);
		else
			cre.getInventory().addToInventory(arrow);
		return arrow;
	}


	/**
	 * Creates a new bolt.
	 * @param cre The creature which will receive the bolt, null if it's going to a random tile.
	 * @param material The material of the bolt.
	 * @param amount The amount of bolts to be created
	 * @param level The level of the dungeon.
	 * @return The bolts.
	 */
	private static Ammo newBolt(Creature cre, itemMaterial material, int amount, int level){
		//System.out.println("Creating a bolt.");		
		Tile t = dungeon.getLevel(level).getRandomTile();
		String name = messages.getString("boltName");
		ArrayList<String> desc = new ArrayList<String>();
		desc.add(messages.getString("boltDesc"));
		Ammo bolt = new Ammo('\\', SColor.BROWN , t.getPosition(), level, name,desc,3,null,"",Ammo.ammoType.BOLT, material);
		for(int i = 1; i < amount; i++){
			bolt.addItemToArray(new Ammo('\\', SColor.BROWN , t.getPosition(), level, name,desc,3,null,"",Ammo.ammoType.BOLT, material));
		}
		if(cre == null)t.putOnTile(bolt);
		else
			cre.getInventory().addToInventory(bolt);			
		return bolt;
	}

}
