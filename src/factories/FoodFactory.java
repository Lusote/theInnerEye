package factories;

import items.Food;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import rlmain.Creature;
import rlmain.RoguelikeMain;
import screens.PlayScreen;
import squidpony.squidcolor.SColor;
import dungeongen.Dungeon;
import dungeongen.Tile;
import effects.Effect;
import effects.InstaHealEffect;

public class FoodFactory {

	public static ResourceBundle messages = RoguelikeMain.messages;
	private static Dungeon dungeon;
	
	// ADD THE FOODS HERE!!!
	public enum foodType{
		APPLE,CUCUMBER,PINNEAPLEHAMBURGER,RANDOM,CORPSE
	};
	private static List<foodType> menu = Collections.unmodifiableList(Arrays.asList(foodType.values()));
	private static final int menuSize = menu.size();	
	public static foodType getRandomFoodType(){return menu.get(PlayScreen.getTheRNG().nextInt(menuSize));}
	
	//IMPORTANT:
	//When adding a new food, remember to add it to the menu!!
	static Food newFood(Creature cre, foodType forcedType, int level){
		dungeon = PlayScreen.getDungeon();
		foodType type = forcedType;
		while(type == foodType.RANDOM || type == foodType.CORPSE)
			type = getRandomFoodType(); 
		Tile t = dungeon.getLevel(level).getRandomTile();
		ArrayList<String> desc = new ArrayList<String>();
		int lev = level;
		SColor color = SColor.WHITE;
		String name = "food";
		ArrayList<String> msgEat = new ArrayList<String>();
		String hiddenDesc ="";
		int hungerPoints = 1;
		char charact = 'E';
		ArrayList<Effect> eff = new ArrayList<Effect>();
		boolean stackeable=false;
		switch(type){
			case APPLE:
				//System.out.println("Creating an apple.");
				name = messages.getString("appleName");
				desc.add(messages.getString("appleDesc"));
				charact ='o';
				hungerPoints=400;
				color = SColor.RED;
				msgEat.add(messages.getString("appleEat1"));
				stackeable=true;
				break;
			case CUCUMBER:
				//System.out.println("Creating a cucumber.");
				name = messages.getString("cucumberName");
				desc.add(messages.getString("cucumberDesc"));
				charact = 'C';
				hungerPoints = 300;
				color = SColor.GREEN_BAMBOO;
				msgEat.add(messages.getString("cucumberEat1"));
				stackeable = true;
				break;
			case PINNEAPLEHAMBURGER:
				name = messages.getString("pinneappleHamburgerName");
				desc.add(messages.getString("pinneappleHamburgerDesc1"));
				desc.add(messages.getString("pinneappleHamburgerDesc2"));
				charact = 'o';
				hungerPoints = 6;
				color = SColor.YELLOW;
				msgEat.add(messages.getString("pinneappleHambuergerHiddenDesc"));
				stackeable = true;
				break;
		default:
			break;
		}		
		if(type==PlayScreen.getPlayer().getFavFood()){
			hiddenDesc = messages.getString("youLoveSpace")+name+messages.getString("s!");
			eff.add(new InstaHealEffect(15));
		}
		Food food = new Food(charact, color, t.getPosition(),lev, name, desc, hungerPoints,msgEat,eff, stackeable,hiddenDesc,type);
		if(cre == null)
			t.putOnTile(food);		
		else
			PlayScreen.getDungeon().getCreature(cre.getId()).getInventory().addToInventory(food);
		return food;
	}

}
