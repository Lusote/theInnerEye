package factories;

import items.GoldCoins;

import java.util.Random;

import screens.PlayScreen;
import dungeongen.Dungeon;
import dungeongen.Tile;

public class GoldFactory {

	private static Dungeon dungeon;
	private static Random THERNG;

	static GoldCoins makeSomeGold(int level) {
		dungeon = PlayScreen.getDungeon();
		THERNG = PlayScreen.getTheRNG();
		GoldCoins coin=null;
		int amount = THERNG.nextInt(98)+2;
		Tile t = dungeon.getLevel(level).getRandomTile();
		int lev = level;
		coin = new GoldCoins(t.getPosition(), lev);
		t.putOnTile(coin);
		for(int i = 1; i<amount; i++){
			coin.addItemToArray(new GoldCoins(t.getPosition(), lev));
		}
		return coin;
	}

}
