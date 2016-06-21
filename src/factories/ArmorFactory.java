package factories;

import items.Armor;
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

public class ArmorFactory {
	
	public static ResourceBundle messages = RoguelikeMain.messages;	
	private static Dungeon dungeon;
	private static Random THERNG;
	

	/**
	 * Creates a new shield.
	 * @param cre The creature which will receive the shield, null if it's going to a random tile.
	 * @param material The material of the shield.
	 * @param level The level of the dungeon.
	 * @return The shield.
	 */
	static Armor newShield(Creature cre, itemMaterial material, int level){
		dungeon = PlayScreen.getDungeon();
		THERNG = PlayScreen.getTheRNG();
		while(material == itemMaterial.RANDOM)
			material = Item.getRandomMaterial();
		//System.out.println("Creating a shield.");
		Tile t = dungeon.getLevel(level).getRandomTile();
		ArrayList<String> desc = new ArrayList<String>();
		SColor color = SColor.WHITE;
		String name = "shield";
		String hiddenDesc="";
		char c = 'o';
		int armorValue = 1;
		int extraArmor = 0;
		int minStr = 1;
		switch(material){
			case WOOD:
				name = messages.getString("woodShieldName");
				desc.add(messages.getString("woodShieldDesc"));
				color = SColor.BROWN;
				extraArmor =THERNG.nextInt(2);
				armorValue = 1;
				minStr = 2;
				break;

			case IRON:
				name = messages.getString("ironShieldName");
				desc.add(messages.getString("ironShieldDesc"));
				color = SColor.GREYISH_DARK_GREEN;
				extraArmor =THERNG.nextInt(4);
				armorValue = 3;
				minStr = 4;
				break;
			default:
				break;
		}
		hiddenDesc = messages.getString("itsaPlus")+extraArmor+messages.getString("space")+name;
		Armor shield = new Armor(c,color,t.getPosition(),level,name,Item.itemType.SHIELD,material,desc,armorValue,extraArmor,null,hiddenDesc,50,minStr);
		if(cre == null)t.putOnTile(shield);
		else{
			if(cre.isPlayer())
				shield.setCursed(false);
			PlayScreen.getDungeon().getCreature(cre.getId()).getInventory().addToInventory(shield);	
		}
		return shield;	
	}

	/**
	 * Creates a new helmet.
	 * @param cre The creature which will receive the helmet, null if it's going to a random tile.
	 * @param material The material of the helmet.
	 * @param level The level of the dungeon.
	 * @return The helmet.
	 */
	static Armor newHelmet(Creature cre, Item.itemMaterial material, int level){
		dungeon = PlayScreen.getDungeon();
		THERNG = PlayScreen.getTheRNG();
		while(material == itemMaterial.RANDOM)
			material = Item.getRandomMaterial();
		//System.out.println("Creating a helmet.");
		Tile t = dungeon.getLevel(level).getRandomTile();
		ArrayList<String> desc = new ArrayList<String>();
		SColor color = SColor.WHITE;
		String name = "helmet";
		String hiddenDesc="";
		char c = '[';
		int armorValue = 1;
		int extraArmor = 0;
		int minStr = 1;
		switch(material){
			case WOOD:
				name = messages.getString("woodHelmetName");
				desc.add(messages.getString("woodHelmetDesc"));
				color = SColor.BROWN;
				extraArmor =THERNG.nextInt(2);
				armorValue = 1 + extraArmor;
				minStr = 2;
				break;

			case IRON:
				name = messages.getString("ironHelmetName");
				desc.add(messages.getString("ironHelmetDesc"));
				color = SColor.GREYISH_DARK_GREEN;
				extraArmor =THERNG.nextInt(3);
				armorValue = 2 + extraArmor;
				minStr = 4;
				break;
			default:
				break;
		}
		hiddenDesc = messages.getString("itsaPlus")+extraArmor+messages.getString("space")+name;
		Armor helmet = new Armor(c,color,t.getPosition(),level,name,Item.itemType.HELMET,material,desc,armorValue,extraArmor,null,hiddenDesc,50,minStr);
		if(cre == null)t.putOnTile(helmet);	
		else{
			if(cre.isPlayer())
				helmet.setCursed(false);
			PlayScreen.getDungeon().getCreature(cre.getId()).getInventory().addToInventory(helmet);
		}	
		return helmet;	
	}
}
