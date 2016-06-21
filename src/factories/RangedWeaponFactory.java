package factories;

import items.Ammo.ammoType;
import items.Item.itemMaterial;
import items.RangedWeapon;

import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import rlmain.Creature;
import rlmain.RoguelikeMain;
import screens.PlayScreen;
import squidpony.squidcolor.SColor;
import dungeongen.Dungeon;
import dungeongen.Tile;

public class RangedWeaponFactory {

	public static ResourceBundle messages = RoguelikeMain.messages;
	private static Dungeon dungeon;
	private static Random THERNG;

	static RangedWeapon newRangedWeapon(Creature cre, int forcedtype, int level){
		dungeon = PlayScreen.getDungeon();
		THERNG = PlayScreen.getTheRNG();
		int type = forcedtype == -1? THERNG.nextInt(1) : forcedtype;
		RangedWeapon a = null;
		switch(type){
		case 0:
			a = newBow(cre, level);
			break;
		}
		return a;
	}	


	private static RangedWeapon newBow(Creature cre, int level) {
		//System.out.println("Creating a bow.");		
		Tile t = dungeon.getLevel(level).getRandomTile();
		ArrayList<String> desc = new ArrayList<String>();
		desc.add(messages.getString("bowDesc"));
		RangedWeapon bow = new RangedWeapon(')', SColor.BROWNER, t.getPosition(), level, 
				messages.getString("bowName"), desc, 3,null,true, 5, ammoType.ARROW,"",itemMaterial.WOOD,3);
		if(cre == null)t.putOnTile(bow);
		else{
			if(cre.isPlayer())
				bow.setCursed(false);
			cre.getInventory().addToInventory(bow);
		}
		return bow;
	}
}
