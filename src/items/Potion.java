package items;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rlmain.RoguelikeMain;
import screens.PlayScreen;
import squidpony.squidcolor.SColor;
import dungeongen.Position;
import effects.Effect;
import factories.PotionFactory;
import factories.PotionFactory.potionTypes;

public class Potion extends Item implements Serializable{
	private static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();


	public static Map<String, SColor> potionColors = null;
	public static List<String> potionAppearances;
	private static ArrayList<PotionFactory.potionTypes> identified = new ArrayList<PotionFactory.potionTypes>();
	private potionTypes potType;
	
	public Potion(char s, Position p, int lev,
			String name, ArrayList<String> de,ArrayList<Effect> e, String h,SColor col, potionTypes type) {
		super(s, null, p, lev, name, Item.itemType.POTION, de,e,true,false,true, h,-1,0);
		this.setColor(col);
		this.potType = type;
		if(potionColors == null){
			setUpPotionAppearances();
		}
		
	}
	
	/*	HOW TO USE:
			String col = potionAppearances.get(0);
			SColor color = potionColors.get(col);
	*/	
	public static void setUpPotionAppearances(){
		potionColors = new HashMap<String, SColor>();
		potionColors.put(messages.getString("redPotion"), SColor.RED);
		potionColors.put(messages.getString("yellowPotion"), SColor.YELLOW);
		potionColors.put(messages.getString("greenPotion"), SColor.GREEN);
		potionColors.put(messages.getString("cyanPotion"), SColor.CYAN);
		potionColors.put(messages.getString("bluePotion"), SColor.BLUE);
		potionColors.put(messages.getString("magentaPotion"), SColor.MAGENTA);
		potionColors.put(messages.getString("darkPotion"), SColor.BLACK_CHESTNUT_OAK);
		potionColors.put(messages.getString("greyPotion"), SColor.GREYISH_DARK_GREEN);
		potionColors.put(messages.getString("lightPotion"), SColor.WHITE);
		
		potionAppearances = new ArrayList<String>(potionColors.keySet());
		Collections.shuffle(potionAppearances, PlayScreen.getTheRNG());		
	}

	public static ArrayList<PotionFactory.potionTypes> getIdentified() {
		return identified;
	}

	public static void addIdentified(PotionFactory.potionTypes i) {
		getIdentified().add(i);
	}

	public potionTypes getPotiontype() {
		return this.potType;
	}

}
