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

public class Weapon extends Item implements Serializable{
	private static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();

	private int damage;
	private int numHands;
	private itemMaterial material;
	public ArrayList<String> desc = new ArrayList<String>();
	public enum weaponType{
		SWORD, STAFF, RANDOM;
	};
	private static final List<weaponType> weaponTypeValues = Collections.unmodifiableList(Arrays.asList(weaponType.values()));
	
	private static final int weaponTypeSize = weaponTypeValues.size();
	
	public static weaponType getRandomWeaponType(){return weaponTypeValues.get(PlayScreen.getTheRNG().nextInt(weaponTypeSize));}
		
	public Weapon(char symbol, SColor color, Position pos, int level,itemType type, itemMaterial material,
			String name, ArrayList<String> desc, int dam, int extraDam,ArrayList<Effect> eff, int numhands,String hiddenName,int turns, int minStr) {
		
		super(symbol, color, pos, level, name, type, desc,eff,false,true,false,hiddenName,turns,minStr);
		this.damage = dam;
		this.setMaterial(material);
		this.setExtraDamage(extraDam);
		this.setDamage(dam+extraDam);
		this.numHands = numhands;
		this.setTurnsToKnow(turns);
	}

	public int getDamage(){return this.damage;}

	public void setDamage(int damage){this.damage = damage;}	
	
	public int getNumHands(){return this.numHands;}

	public int getTurnsToKnow(){return super.getTurnsToKnow();}

	public void setTurnsToKnow(int turnsToKnow){super.setTurnsToKnow(turnsToKnow);}

	public int getExtraDamage(){return super.getExtraValue();}

	public void setExtraDamage(int extraDamage){super.setExtraValue(extraDamage);}

	public itemMaterial getMaterial(){return material;}

	public void setMaterial(itemMaterial material){this.material = material;}
}
