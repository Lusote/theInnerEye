package items;

import items.Ammo.ammoType;

import java.io.Serializable;
import java.util.ArrayList;

import rlmain.RoguelikeMain;
import squidpony.squidcolor.SColor;
import dungeongen.Position;
import effects.Effect;

public class RangedWeapon extends Weapon implements Serializable{
	private static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();


	private int damage;
	private int range;
	private int numHands;
	private ammoType ammo;
	
	public RangedWeapon(char s, SColor c, Position p, int lev,
			String name, ArrayList<String> de,int damage, ArrayList<Effect> e,boolean equip, int range, ammoType ammo, String h, itemMaterial material,int minStr) {
		
		super(s,c,p,lev,Item.itemType.RANGEDWEAPON,material,name,de, damage,0,e,2, h,300,minStr);
		this.setRange(range);
		this.ammo = ammo;
		this.numHands = 2;
		
	}
	
	public int getDamage(){return this.damage;}

	public void setDamage(int damage){this.damage = damage;}

	public int getRange(){return range;}

	public void setRange(int range){this.range = range;}
	
	public String getAmmoName(ammoType type){
		return Ammo.getAmmoName(type);
	}

	public ammoType getAmmo(){return this.ammo;}
	
	public int getNumHands(){return this.numHands;}	
	
}
