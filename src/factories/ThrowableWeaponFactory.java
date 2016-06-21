package factories;

import items.ThrowableWeapon;

import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

import rlmain.Creature;
import rlmain.RoguelikeMain;
import screens.PlayScreen;
import squidpony.squidcolor.SColor;
import dungeongen.Dungeon;
import dungeongen.Tile;

public class ThrowableWeaponFactory {

	public static ResourceBundle messages = RoguelikeMain.messages;
	private static Dungeon dungeon;
	private static Random THERNG;

	static ThrowableWeapon newThrowableWeapon(Creature cre, int forcedtype, int amount, int level){
		dungeon = PlayScreen.getDungeon();
		THERNG = PlayScreen.getTheRNG();
		int type = forcedtype == -1? THERNG.nextInt(1) : forcedtype;
		ThrowableWeapon a = null;
		if(amount == -1)
			amount = 10+THERNG.nextInt(10);
		switch(type){
		case 0:
			a= newRock(cre, amount, level);
			break;
		}
		return a;
	}

	private static ThrowableWeapon newRock(Creature cre, int amount, int level){
		Tile t = dungeon.getLevel(level).getRandomTile();
		ArrayList<String> desc = new ArrayList<String>();
		desc.add(messages.getString("rockDesc"));
		ThrowableWeapon rock = new ThrowableWeapon('*', SColor.DARK_GRAY, t.getPosition(), level, messages.getString("rockName"),desc,1,null,"",1);
		for(int i = 1; i < amount; i++){
			rock.addItemToArray(new ThrowableWeapon('*', SColor.DARK_GRAY, t.getPosition(), level, messages.getString("rockName"),desc,1,null,"",1));
		}
		if(cre == null)t.putOnTile(rock);
		else
			cre.getInventory().addToInventory(rock);
		return rock;
	}
}
