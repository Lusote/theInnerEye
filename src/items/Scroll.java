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
import factories.ScrollFactory;
import factories.ScrollFactory.ScrollTypes;

public class Scroll extends Item implements Serializable{
	private static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();


	public static Map<String, SColor> scrollNames = null;
	public static List<String> scrollAppearances;
	private static ArrayList<ScrollFactory.ScrollTypes> identified = new ArrayList<ScrollFactory.ScrollTypes>();
	private ScrollTypes scrollType;
	
	public Scroll(char s, Position p, int lev,
			String name, ArrayList<String> de,ArrayList<Effect> e, String h,SColor col, ScrollTypes type) {
		super(s, null, p, lev, name, Item.itemType.SCROLL, de,e,true,false,true, h,-1,0);
		this.setColor(col);
		this.scrollType = type;
		if(scrollNames == null){
			setUpScrollAppearances();
		}
		
	}
	
	/*	HOW TO USE:
			String col = potionAppearances.get(0);
			SColor color = potionColors.get(col);
	*/
	
	public static void setUpScrollAppearances(){
		scrollNames = new HashMap<String, SColor>();
		scrollNames.put(messages.getString("scrollName1"), SColor.RED);
		scrollNames.put(messages.getString("scrollName2"), SColor.YELLOW);
		scrollNames.put(messages.getString("scrollName3"), SColor.GREEN);
		scrollNames.put(messages.getString("scrollName4"), SColor.CYAN);
		scrollNames.put(messages.getString("scrollName5"), SColor.BLUE);
		scrollNames.put(messages.getString("scrollName6"), SColor.MAGENTA);
		scrollNames.put(messages.getString("scrollName7"), SColor.BLACK_CHESTNUT_OAK);
		scrollNames.put(messages.getString("scrollName8"), SColor.GREYISH_DARK_GREEN);
		scrollNames.put(messages.getString("scrollName9"), SColor.WHITE);
		
		scrollAppearances = new ArrayList<String>(scrollNames.keySet());
		Collections.shuffle(scrollAppearances, PlayScreen.getTheRNG());		
	}

	public static ArrayList<ScrollFactory.ScrollTypes> getIdentified() {
		return identified;
	}

	public static void addIdentified(ScrollFactory.ScrollTypes i) {
		getIdentified().add(i);
	}

	public ScrollTypes getScrolltype() {
		return this.scrollType;
	}

}
