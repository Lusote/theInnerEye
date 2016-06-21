package factories;

import items.Item;
import items.Item.itemMaterial;
import items.Weapon;
import items.Weapon.weaponType;

import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import rlmain.Creature;
import rlmain.RoguelikeMain;
import screens.PlayScreen;
import squidpony.squidcolor.SColor;
import dungeongen.Dungeon;
import dungeongen.Tile;
import effects.Effect;
import effects.IntBoostEffect;

public class WeaponFactory {

	public static ResourceBundle messages = RoguelikeMain.messages;
	private static Dungeon dungeon;
	private static Random THERNG;
	
	static Weapon newWeapon(Creature cre, Weapon.weaponType weaponT, itemMaterial material, int level){
		dungeon = PlayScreen.getDungeon();
		THERNG = PlayScreen.getTheRNG();
		while(weaponT == weaponType.RANDOM)
			weaponT = Weapon.getRandomWeaponType();
		switch(weaponT){
			case SWORD:
				return newSword(cre, material, level);
			case STAFF:
				return newStaff(cre, material, level);
			default:
				break;
		}
		return null;
	}

	
	private static Weapon newStaff(Creature cre, itemMaterial material,	int level) {
		while(material == itemMaterial.RANDOM)  
			material = Item.getRandomMaterial();
		//System.out.println("Creating a staff.");
		Tile t = dungeon.getLevel(level).getRandomTile();
		ArrayList<String> desc = new ArrayList<String>();
		ArrayList<Effect> eff = new ArrayList<Effect>();
		SColor color = SColor.WHITE;
		String name = messages.getString("woodStaffName");
		String hiddenDesc=messages.getString("blank");
		int damage = 1;
		int extraDamage=0;
		int minStr = 1;
		switch(material){
			case WOOD:
				name = messages.getString("woodStaffName");
				desc.add(messages.getString("woodStaffDesc"));
				color = SColor.BROWN;
				extraDamage =THERNG.nextInt(1);
				damage = 1;
				minStr = 2;
				eff.add(new IntBoostEffect(5));
				break;
			case IRON:
				name = messages.getString("ironStaffName");
				desc.add(messages.getString("ironStaffDesc"));
				color = SColor.GRAY;
				extraDamage =THERNG.nextInt(2);
				damage = 3;
				minStr = 4;
				eff.add(new IntBoostEffect(10));
				break;
			default:
				break;				
		}				
		hiddenDesc = messages.getString("itsaPlus")+extraDamage+messages.getString("space")+name;
		Weapon staff = new Weapon(')', color, t.getPosition(), level, Item.itemType.MAGICWEAPON,material, name,desc,damage,extraDamage,eff,1, hiddenDesc,50,minStr);
		if(cre == null)t.putOnTile(staff);	
		else{
			if(cre.isPlayer())
				staff.setCursed(false);
			cre.getInventory().addToInventory(staff);
		}
		return staff;
	}


	private static Weapon newSword(Creature cre, itemMaterial material, int level){
		while(material == itemMaterial.RANDOM)  
			material = Item.getRandomMaterial();
		//System.out.println("Creating a sword.");
		Tile t = dungeon.getLevel(level).getRandomTile();
		ArrayList<String> desc = new ArrayList<String>();
		ArrayList<Effect> eff = new ArrayList<Effect>();
		SColor color = SColor.WHITE;
		String name = "sword";
		String hiddenDesc="";
		int damage = 1;
		int extraDamage=0;
		int minStr = 1;
		switch(material){
			case WOOD:
				name = messages.getString("woodenSwordName");
				desc.add(messages.getString("woodenSwordDesc1"));
				desc.add(messages.getString("woodenSwordDesc2"));
				desc.add(messages.getString("woodenSwordDesc3"));
				desc.add(messages.getString("woodenSwordDesc4"));
				color = SColor.BROWN;
				extraDamage =THERNG.nextInt(2);
				damage = 1;
				minStr = 2;
				break;
			case IRON:
				name = messages.getString("ironSwordName");
				desc.add(messages.getString("ironSwordDesc1"));
				desc.add(messages.getString("ironSwordDesc2"));
				desc.add(messages.getString("ironSwordDesc3"));
				desc.add(messages.getString("ironSwordDesc4"));
				color = SColor.GRAY;
				extraDamage =THERNG.nextInt(3);
				damage = 2;
				minStr = 3;
				break;
			default:
				break;				
		}				
		hiddenDesc = messages.getString("itsaPlus")+extraDamage+messages.getString("space")+name;
		Weapon sword = new Weapon(')', color, t.getPosition(), level, Item.itemType.MELEEWEAPON,material, name,desc,damage,extraDamage,eff,1, hiddenDesc,50,minStr);
		if(cre == null)t.putOnTile(sword);	
		else{
			if(cre.isPlayer())
				sword.setCursed(false);
			cre.getInventory().addToInventory(sword);
		}
		return sword;
	}	
}
