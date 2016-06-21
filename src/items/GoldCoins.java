package items;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.ResourceBundle;

import rlmain.RoguelikeMain;
import squidpony.squidcolor.SColor;
import dungeongen.Position;

public class GoldCoins extends Item implements Serializable{
	public static ResourceBundle messages = RoguelikeMain.messages;
	private static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();


	public GoldCoins(Position p, int lev) {
		super('$', SColor.GOLD, p, lev, messages.getString("goldCoinsName"), Item.itemType.GOLD, null, null,false,false,true,"",-1,0);
		ArrayList<String> de = new ArrayList<String>();
		de.add(messages.getString("goldCoinsDesc"));
		this.setDesc(de);		
	}
}
