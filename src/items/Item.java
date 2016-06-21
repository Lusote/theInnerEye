package items;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;

import rlmain.Drawable;
import rlmain.RoguelikeMain;
import screens.PlayScreen;
import squidpony.squidcolor.SColor;
import dungeongen.Dungeon;
import dungeongen.Position;
import effects.Effect;

public class Item implements Drawable, Serializable {

	public static ResourceBundle messages = RoguelikeMain.messages;
	private static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();

	private char symbol;
	private SColor color;
	private Position pos;
	private int level;
	private int extraValue;
	private String name;
	private ArrayList<String> desc;
	private int id;
	private static int numberItemsMade = 0;
	private itemType type;
	private String hiddenName;
	private boolean identified;
	private boolean equipped;
	private boolean cursed;
	private boolean curseKnown;
	private List<Effect> effects;
	private boolean equippable;
	private boolean throwable;
	private boolean stack;
	private ArrayList<Item> others;
	private int turnsToKnow;
	private int minStr;
	public enum itemType{
		MELEEWEAPON,RANGEDWEAPON, MAGICWEAPON, ARMOR, HELMET, SHIELD, CHEST, BOOTS, MISSILE, THROWABLEWEAPON, POTION, AMMO, FOOD, SCROLL,GOLD,
		THESYWEROFFIDOL;
	};
	private static ArrayList<itemType> cursableItems = getCursableItems();
	public enum itemMaterial{
		WOOD, IRON, RANDOM;
	};
	private static final List<itemMaterial> itemMaterialValues = Collections.unmodifiableList(Arrays.asList(itemMaterial.values()));
	
	private static final int itemMaterialSize = itemMaterialValues.size();
	
	public static itemMaterial getRandomMaterial(){return itemMaterialValues.get(PlayScreen.getTheRNG().nextInt(itemMaterialSize));}
	
	public Item(char symbol, SColor color, Position pos, int lev, String name, itemType type, ArrayList<String> desc, List<Effect> eff, 
					boolean throwable, boolean equip,boolean stackeable, String hiddenName, int turnsToKnow, int minStr){
		this.name = name;
		this.setDesc(desc);
		this.setType(type);
		this.symbol = symbol;
		this.color = color;
		//this.pos = p;
		this.level = lev;
		this.id = numberItemsMade;
		numberItemsMade++;
		this.extraValue = -1;
		this.turnsToKnow = turnsToKnow;
		this.cursed = false;
		if(cursableItems.contains(this.type)){
			if(util.Util.oddsLessThan(0.10))
				this.cursed = true;
		}
		this.curseKnown = false;
		this.setEquipped(false);
		this.setEffects(eff);
		identified = false;
		this.setThrowable(throwable);
		this.setEquippable(equip);
		this.stack = stackeable;
		this.hiddenName = hiddenName;
		this.minStr = minStr;
		if(stackeable) 
			this.others = new ArrayList<Item>();
	}

	private static ArrayList<itemType> getCursableItems() {
		ArrayList<itemType> toReturn = new ArrayList<itemType>();
		toReturn.add(itemType.MELEEWEAPON);
		toReturn.add(itemType.RANGEDWEAPON);
		toReturn.add(itemType.ARMOR);
		toReturn.add(itemType.HELMET);
		toReturn.add(itemType.SHIELD);
		toReturn.add(itemType.CHEST);
		toReturn.add(itemType.BOOTS);
		return toReturn;
	}

	public void setArrayItems(){this.others = new ArrayList<Item>();}
	
	public void removeArray(){this.others=null;}
	
	/**
	 * @return The list of items attached to the original item. 
	 */
	public List<Item> getOtherItems(){
		if(others!=null) return others;
		setArrayItems();
		return others;
	}
	
	/**
	 * Adds another item to the stack.
	 * @param itemToAdd The item to Add.
	 */
	public void addItemToArray(Item itemToAdd){
		if(this.getOtherItems() == null)
			this.setArrayItems();
		if(itemToAdd.getOtherItems() == null || itemToAdd.getOtherItems().isEmpty()){
			itemToAdd.setArrayItems();
			this.getOtherItems().add(itemToAdd);
			return;
		}
		ListIterator<Item> iter = itemToAdd.getOtherItems().listIterator();
		while(iter.hasNext()){
			Item nextItem = iter.next();
			this.getOtherItems().add(nextItem);
			iter.remove();
		}
		this.getOtherItems().add(itemToAdd);
	}
	

	public int getItemID(){return this.id;}
	
	public boolean isIdentifiable(){return this.hiddenName!="";}
		
	public boolean isIdentified(){return this.identified;}
	
	public void setIdentified(boolean i){this.identified = i;}
	
	public String getName(){
		String toReturn = this.name	;
		if(this instanceof Potion){
			Potion p = (Potion)this;
			if(Potion.getIdentified().contains(p.getPotiontype()))
				return  this.getHiddenDesc();
		}
		if(this instanceof Scroll){
			Scroll scr = (Scroll)this;
			if(Scroll.getIdentified().contains(scr.getScrolltype()))
				return  this.getHiddenDesc();
		}
		if(this.isIdentifiable() && this.isIdentified()){
				toReturn = messages.getString("plus")+extraValue+messages.getString("space")+this.name;
		}
		if(cursableItems.contains(this.getType())){
			if(this.isCurseKnown()){
				if(this.isCursed())
					toReturn = messages.getString("cursedSpace") + toReturn;
				else
					toReturn = messages.getString("uncursedSpace") + toReturn;
			}		
		}
		return toReturn;
	}
	
	@Override
	public char getSymbol(){return this.symbol;}
	
	@Override
	public void setSymbol(char s){this.symbol = s;}

	@Override
	public SColor getColor(){return this.color;}

	@Override
	public void setColor(SColor c){this.color = c;}

	@Override
	public Position getPosition(){return pos;}

	@Override
	public void setPosition(Position pos){this.pos = pos;}

	@Override
	public int getIndexDungeonLevel(){return this.level;}

	@Override
	public void setIndexDungeonLevel(int l){this.level = l;}

	@Override
	public Dungeon getDungeon(){return PlayScreen.getDungeon();}
	
	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
    	if (other == this) return true;
	    if (!(other instanceof Item))
	    	return false;
	    Item another = (Item)other;
	    if ((another.getItemID() == this.getItemID())) 
        	return true;
	    else return false;
	}

	@Override
	public int hashCode(){
		final int prime = 41;
		int result =1;
		result = result + this.getItemID() * prime;
		return result;
	}

	public itemType getType(){return type;}

	public void setType(itemType type2){this.type = type2;}

	public void setDesc(ArrayList<String> desc){this.desc = desc;}
	
	@Override
	public ArrayList<String> getDescription(){
		if(this instanceof GoldCoins){
			return (new ArrayList<String>(Arrays.asList( messages.getString("justSpace")+(this.getOtherItems().size()+1)+messages.getString("goldCoinsDesc"))));
		}
		return desc;
	}

	public boolean isEquipped(){return equipped;}

	public void setEquipped(boolean equiped){this.equipped = equiped;}

	public boolean isThrowable(){return throwable;}

	public void setThrowable(boolean throwable){this.throwable = throwable;}

	public boolean isEquippable(){return equippable;}

	public void setEquippable(boolean equippable){this.equippable = equippable;}
	
	public boolean isStackeable(){return this.stack;}

	public String getHiddenDesc(){return hiddenName;}

	public void setHiddenDesc(String hiddenDesc){this.hiddenName = hiddenDesc;}

	public List<Effect> getEffects(){return effects;}

	public void setEffects(List<Effect> effects){this.effects = effects;}

	public int getExtraValue(){return extraValue;}

	public void setExtraValue(int extraValue){this.extraValue = extraValue;}

	public int getTurnsToKnow(){return turnsToKnow;}

	public void setTurnsToKnow(int turnsToKnow){this.turnsToKnow = turnsToKnow;}

	public boolean isCursed(){return cursed;}

	public void setCursed(boolean cursed){this.cursed = cursed;}

	public boolean isCurseKnown(){return curseKnown;}

	public void setCurseKnown(boolean curseKnown){this.curseKnown = curseKnown;}

	public int getMinStr(){return minStr;}

	public void setMinStr(int minStr){this.minStr = minStr;}
	
}
