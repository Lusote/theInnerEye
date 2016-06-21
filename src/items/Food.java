package items;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rlmain.RoguelikeMain;
import squidpony.squidcolor.SColor;
import dungeongen.Position;
import effects.Effect;
import factories.FoodFactory.foodType;

public class Food extends Item implements Serializable{
	private static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();


	private int nutritionalValue;
	private ArrayList<String> messages;
	private foodType type;
	
	public Food(char s, SColor c, Position p, int lev, String name, ArrayList<String> de, int value, ArrayList<String> messages,List<Effect> e,boolean stackeable, String h, foodType type){
		super(s, c, p, lev, name, Item.itemType.FOOD, de,e, false,false,stackeable,h,0,0);
		this.nutritionalValue = value;
		this.messages = messages;
		this.setFoodType(type);
	}

	public int getNutritionalValue() {
		return this.nutritionalValue;
	}

	public ArrayList<String> getMessage(){return this.messages;}

	public foodType getFoodType() {
		return type;
	}

	public void setFoodType(foodType type) {
		this.type = type;
	}

}
