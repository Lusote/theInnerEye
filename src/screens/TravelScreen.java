package screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.text.BadLocationException;

import rlmain.Creature;
import rlmain.RoguelikeMain;
import squidpony.squidgrid.gui.SGPane;
import util.Util;
import dungeongen.Dungeon;
import dungeongen.Position;

/*
 * TODO:
 * 	Check for more than one Pages, if there are A LOT of items/creatures.
 * 
 * 
 */
public class TravelScreen implements Screen{

	private Creature player;
	private String letters;
	
	public TravelScreen(){
		this.player = PlayScreen.getPlayer();
		this.setLetters("><z");
	}
	
	protected Screen use(Position p) {
		if(!player.getCreaturesOnView().isEmpty()){
			return null;
		}
		Dungeon dun = PlayScreen.getDungeon();
		String dest = messages.getString("trvScrUnknown");
		if(p == dun.getLevel(player.getIndexDungeonLevel()).getStairsDown())
			dest = messages.getString("trvScrDownstairs");
		if(p == dun.getLevel(player.getIndexDungeonLevel()).getStairsUp())
			dest = messages.getString("trvScrUpstairs");
		dun.addMessage(messages.getString("trvScrYouTravellingTo")+dest);
		player.setTraveling(true);
		player.setDestiny(p);
		return null;
	}
	
	
	@Override
	public void display(SGPane console){
		Dungeon dun = PlayScreen.getDungeon();
		Util.clearTextArea();
		ArrayList<Position> destiny = new ArrayList<Position>();
		Position dStairs = dun.getLevel(player.getIndexDungeonLevel()).getStairsDown();
		Position uStairs = dun.getLevel(player.getIndexDungeonLevel()).getStairsUp();
		if(dun.getLevel(PlayScreen.getPlayer().getIndexDungeonLevel()).getTile(dStairs)!=null && dun.getLevel(PlayScreen.getPlayer().getIndexDungeonLevel()).getTile(dStairs).isExplored())
			destiny.add(dStairs);
		if(dun.getLevel(PlayScreen.getPlayer().getIndexDungeonLevel()).getTile(uStairs).isExplored())
			destiny.add(uStairs);
		ArrayList<Position> unexpPos = dun.getLevel(player.getIndexDungeonLevel()).getUnexploredFloorPositions();
		if(unexpPos.size()>0)destiny.add(unexpPos.get(0));
		Util.printText(0, 0, messages.getString("trvScrWhereToTravel"));
		for(Position i : destiny){
			if(i == dStairs){
				Util.printText(10, 1, messages.getString("trvScrDownstairsOption"));
				continue;
			}
			if(i == uStairs){
				Util.printText(10, 2, messages.getString("trvScrUpstairsOption"));
				continue;
			}
			if(i == unexpPos.get(0))
				Util.printText(10, 3, messages.getString("trvScrUnknownOption"));			
		}
		if(RoguelikeMain.isBlindMode())try {RoguelikeMain.getTextArea().setCaretPosition(RoguelikeMain.getTextArea().getLineStartOffset(0));} catch (BadLocationException e){e.printStackTrace();}
		console.refresh();
	}

	@Override
	public Screen reactToInput(KeyEvent key) {
		char c = key.getKeyChar();
		Dungeon dun = PlayScreen.getDungeon();
		Position dStairs = dun.getLevel(player.getIndexDungeonLevel()).getStairsDown();
		Position uStairs = dun.getLevel(player.getIndexDungeonLevel()).getStairsUp();
		ArrayList<Position> things = new ArrayList<Position>();
		things.add(dun.getLevel(PlayScreen.getPlayer().getIndexDungeonLevel()).getStairsDown());
		things.add(dun.getLevel(PlayScreen.getPlayer().getIndexDungeonLevel()).getStairsUp());
		ArrayList<Position> unexpPos = dun.getLevel(PlayScreen.getPlayer().getIndexDungeonLevel()).getUnexploredFloorPositions();
		if(unexpPos.size()>0)things.add(unexpPos.get(0));
		
		if(c == '>'){
			if(!dun.getLevel(PlayScreen.getPlayer().getIndexDungeonLevel()).getTile(dStairs).isExplored()){
				dun.addMessage(messages.getString("trvScrDownstairsNotFound"));
				return null;
			}else return use(dStairs);		
		}
		if(c == '<'){
			if(!dun.getLevel(PlayScreen.getPlayer().getIndexDungeonLevel()).getTile(uStairs).isExplored()){
				dun.addMessage(messages.getString("trvSrcUpstairsNotFound"));
				return null;
			}else return use(uStairs);	
		}
		if(c == 'z'){
			if(unexpPos.isEmpty()){
				dun.addMessage(messages.getString("trvScrNothingElseToExplore"));
				return null;
			}else use(unexpPos.get(0));
		}
		if (key.getKeyCode() == KeyEvent.VK_ESCAPE)
			return null;
		else return this;
	}

	public String getLetters(){return letters;}

	public void setLetters(String letters){this.letters = letters;}
	
}
