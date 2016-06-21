package items;

import java.io.Serializable;
import java.util.ArrayList;

import rlmain.RoguelikeMain;
import squidpony.squidcolor.SColor;
import dungeongen.Position;
import effects.Effect;

public class ThrowableWeapon extends Item implements Serializable{
	private static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();

	
	private int damage;

	public ThrowableWeapon(char s, SColor c, Position p, int lev,
			String name, ArrayList<String> de, int dam, ArrayList<Effect> e, String h,int minStr) {
		super(s, c, p, lev, name, Item.itemType.THROWABLEWEAPON, de, e,true,true,true,h,-1,minStr);
		this.damage = dam;
		
	}

	public int getDamage(){return this.damage;}

	public void setDamage(int damage){this.damage = damage;	}	

}
