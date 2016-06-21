package rlmain;

import java.util.Comparator;
import java.util.List;

import screens.PlayScreen;
import squidpony.squidcolor.SColor;
import dungeongen.Dungeon;
import dungeongen.Position;

public abstract interface Drawable {

	public char getSymbol();
	public void setSymbol(char c);
	public SColor getColor();
	public void setColor(SColor c);
	public Position getPosition();
	public void setPosition(Position p);
	public int getIndexDungeonLevel();
	public void setIndexDungeonLevel(int l);
	public Dungeon getDungeon();
	public List<String> getDescription();
	public String getName();
		
	public static final Comparator<Drawable> DISTANCE_TO_PLAYER =  new Comparator<Drawable>() {
		public int compare(Drawable d1, Drawable d2) {
			//System.out.println("Comparing "+d1.getPosition().toString()+" and "+d2.getPosition().toString());
			return	d1.getPosition().distance(PlayScreen.getPlayer().getPosition()).compareTo(d2.getPosition().distance(PlayScreen.getPlayer().getPosition()));
		}
	};
	
}
