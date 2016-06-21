package items;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import rlmain.RoguelikeMain;
import screens.PlayScreen;
import squidpony.squidcolor.SColor;
import dungeongen.Position;
import effects.Effect;

public class Ammo extends Item implements Serializable{
	private static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();

	
	private int damage;
	public enum ammoType{
		ARROW, BOLT, RANDOM;
	};
	private static final List<ammoType> ammoTypeValues = Collections.unmodifiableList(Arrays.asList(ammoType.values()));
	
	private static final int ammoTypeSize = ammoTypeValues.size();
	
	public static ammoType getRandomAmmoType(){return ammoTypeValues.get(PlayScreen.getTheRNG().nextInt(ammoTypeSize));}
	
	private ammoType ammoT;
	private itemMaterial material;
	
	public Ammo(char s, SColor c, Position p, int lev,
			String name, ArrayList<String> de, int dam, ArrayList<Effect> e, String h, ammoType type, itemMaterial material) {
		super(s, c, p, lev, name, Item.itemType.AMMO, de, e,true,false,true,h,0,0);
		this.damage = dam;
		this.ammoT = type;
		this.setMaterial(material);
	}
	
	public static String getAmmoName(ammoType type){
		switch (type) {
		case ARROW:
			return RoguelikeMain.messages.getString("arrowName");
		case BOLT:
			return RoguelikeMain.messages.getString("boltName");
		default:
			break;
		}
		return "ammo";
	}

	public ammoType getAmmoType(){return this.ammoT;}
	
	public void setAmmoType(ammoType t){this.ammoT=t;}
	
	public int getDamage(){return this.damage;}

	public void setDamage(int damage){this.damage = damage;	}

	public itemMaterial getMaterial() {
		return material;
	}

	public void setMaterial(itemMaterial material) {
		this.material = material;
	}	

}
