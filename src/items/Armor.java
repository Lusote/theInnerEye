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

public class Armor extends Item implements Serializable{
	private static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();


	private int armorValue;
	private itemMaterial material;
	public enum armorType{
		SHIELD, HELMET, CHEST, BOOTS, RANDOM;
	};
	private static final List<armorType> armorTypeValues = Collections.unmodifiableList(Arrays.asList(armorType.values()));
	
	private static final int armorTypeSize = armorTypeValues.size();
	
	public static armorType getRandomArmorType(){return armorTypeValues.get(PlayScreen.getTheRNG().nextInt(armorTypeSize));}
	
	public Armor(char s, SColor c, Position p, int lev,
			String name, Item.itemType type,itemMaterial material, ArrayList<String> de, int aV,int extraAV,ArrayList<Effect> e,String h, int t, int minStr) {
		super(s, c, p, lev, name, type, de,e,false,true,false,h, t,minStr);
		this.setMaterial(material);
		this.setExtraArmorValue(extraAV);
		this.setArmorValue(aV+extraAV);
	}

	public int getArmorValue(){return armorValue;}

	public void setArmorValue(int aV){this.armorValue = aV;	}

	public int getExtraArmorValue() {
		return super.getExtraValue();
	}

	public void setExtraArmorValue(int extraArmorValue) {
		super.setExtraValue(extraArmorValue);
	}	

	public int getTurnsToKnow() {
		return super.getTurnsToKnow();
	}

	public void setTurnsToKnow(int turnsToKnow) {
		super.setTurnsToKnow(turnsToKnow);
	}

	public itemMaterial getMaterial() {
		return material;
	}

	public void setMaterial(itemMaterial material) {
		this.material = material;
	}

}
