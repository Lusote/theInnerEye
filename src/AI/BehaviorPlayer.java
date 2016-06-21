package AI;

import items.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import rlmain.Creature;
import rlmain.RoguelikeMain;
import screens.PlayScreen;
import util.Util;
import dungeongen.Dungeon;
import dungeongen.Level;
import dungeongen.Position;
import dungeongen.Tile;

public class BehaviorPlayer extends CreatureBehavior {

	public static ResourceBundle messages = RoguelikeMain.messages;
	private static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();	
	
	public BehaviorPlayer(Creature cre) {
		super(cre);
	}
	
	public void onUpdate(){
		this.getCreature().modifyHunger(-1);
		if(this.getCreature().getHunger()<0 && Util.getHeadsOrTails()){
			this.getCreature().modifyHp(-1);
		}
		
		ArrayList<Item> items = this.getCreature().getInventory().getEquippedItems();
		for(Item i : items){
			if(i.isIdentifiable() && i.getTurnsToKnow()>0){
				i.setTurnsToKnow(i.getTurnsToKnow()-1);
				if(i.getTurnsToKnow()==0){
					PlayScreen.getDungeon().addMessage(messages.getString("playerKnowItemBetter"));
					i.setIdentified(true);
					PlayScreen.getDungeon().addMessage(i.getHiddenDesc());
					ArrayList<String> d = i.getDescription();
					ArrayList<String> aux = new ArrayList<String>();
					aux.add(messages.getString("itsaSpace")+i.getName());
					d.remove(0);
					aux.addAll(d);
					i.setDesc(aux);
					i.setTurnsToKnow(-1);	
				}
			}
		}
	}
	
	public void onStep(Position pos){
		updateMemory();
		Creature player = this.getCreature();
		int level = player.getIndexDungeonLevel();
		Tile newTile = player.getDungeon().getLevel(level).getTile(pos);
		if(!newTile.isWalkable()){
			player.getDungeon().addMessage(messages.getString("plyScrYouHitAWall"));
			System.out.println(messages.getString("plyScrYouHitAWall"));
		}else{
			if(newTile.isDoor()){
				PlayScreen.getDungeon().addMessage(messages.getString("playerOnADoor"));
			}
			if(newTile.hasCreature()) 
				player.meleeAttack(newTile.getCreature());
			else{
				printMessagesForTile(newTile);
				Tile oldTile = player.getTile();
				oldTile.setHasCreature(false);
				newTile.setHasCreature(true);
				oldTile.setCreature(null);
				newTile.setCreature(this.getCreature());
				player.setPosition(pos);
				this.getCreature().getDungeon().updateCreatures();
			}
		}
	}
	
	/**
	 * Controls the memory of the player, which remembers the items that the player saw in the floor. 
	 * Updates the memory with the tiles on view and forgets the tiles that are now empty.
	 */
	public void updateMemory(){
		Creature player = this.getCreature();
		Dungeon dun = PlayScreen.getDungeon();
		Level lev = dun.getLevel(player.getIndexDungeonLevel());
		HashMap<Position, Character> memory = player.getMemory().get(player.getIndexDungeonLevel());
		
		for(Position pos : player.getFloorPositionsOnView()){
			if(memory.containsKey(pos)){
				if(lev.getTile(pos).isTileEmpty())
					memory.remove(pos);
				else
					memory.put(pos,lev.getTile(pos).getThingsOnTile().get(lev.getTile(pos).getThingsOnTile().size()-1).getSymbol());					
			}
			else
				if(!lev.getTile(pos).isTileEmpty())
					memory.put(pos,lev.getTile(pos).getThingsOnTile().get(lev.getTile(pos).getThingsOnTile().size()-1).getSymbol());
		}
	}
	
	/**
	 * Prints the automatic messages for a given tile.
	 * @param t The tile from which the messages must be generated.
	 */
	public void printMessagesForTile(Tile t){
		Dungeon dun = PlayScreen.getDungeon();
		if(dun.getLevel(PlayScreen.getPlayer().getIndexDungeonLevel()).getStairsDown()!=null &&	dun.getLevel(PlayScreen.getPlayer().getIndexDungeonLevel()).getStairsDown().equals(t.getPosition())){
			dun.addMessage(messages.getString("playerDownstairsHere"));
		}
		if(dun.getLevel(PlayScreen.getPlayer().getIndexDungeonLevel()).getStairsUp().equals( t.getPosition())){
			dun.addMessage(messages.getString("playerUpstairsHere"));
		}
		
		
		if(t.isTileEmpty())
			return;
		if(t.getThingsOnTile().size() == 1){
			Item i = (Item)t.getThingsOnTile().get(0);
			String n=messages.getString("blank");
			int number = i.getOtherItems().size();
			if(number>=1) n = messages.getString("x")+(number+1);
			dun.addMessage(messages.getString("thereIsASpace")+i.getName()+n+messages.getString("spaceHere"));
		}else{
			Item i = (Item)t.getThingsOnTile().get(t.getThingsOnTile().size()-1);
			String n="";
			int number = i.getOtherItems().size();
			if(number>=1) n = messages.getString("x")+(number+1);
			dun.addMessage(messages.getString("thereisaSpace")+i.getName()+n+messages.getString("spaceHere"));
			dun.addMessage(messages.getString("moreThanOneItemHere"));
		}
	}
	
	// 1 for UP
	// 0 for DOWN
	public void changeDungeonLevel(int up){
		Dungeon dun = PlayScreen.getDungeon();
		Creature player = this.getCreature();
		int indexNewLevel;
		if(up==1) 
			indexNewLevel = player.getIndexDungeonLevel()-1;
		else 
			indexNewLevel = player.getIndexDungeonLevel()+1;
		
		if((player.getTile().getSymbol() == '>' || player.getTile().getSymbol() == 'X') && up == 0){
			player.getMemory().add(new HashMap<Position,Character>());
			System.out.println("DUNGEON LEVEL DOWN. New level: "+indexNewLevel);
			Tile oldTile = player.getTile();
			oldTile.setCreature(null);
			oldTile.setHasCreature(false);
			player.setPosition(null);
			Tile newTile = dun.getLevel(indexNewLevel).getTile((dun.getLevel(indexNewLevel).getStairsUp()));
			newTile.setHasCreature(true);
			newTile.setCreature(player);
			player.setPosition(newTile.getPosition());
			player.setIndexDungeonLevel(indexNewLevel);
			dun.addMessage(messages.getString("playerDungeonLevelDown"));
			dun.updateCreatures();
			
		}
		else if((player.getTile().getSymbol() == '<' || this.getCreature().getTile().getSymbol() == 'X')&& up == 1){
			if(player.getIndexDungeonLevel() == -1){
				System.out.println("You're out.");
				return;
			}
			System.out.println("DUNGEON LEVEL UP. New level: "+indexNewLevel);
			Tile newTile = dun.getLevel(indexNewLevel).getTile((dun.getLevel(indexNewLevel).getStairsDown()));
			Tile oldTile = player.getTile();
			oldTile.setCreature(null);
			oldTile.setHasCreature(false);
			newTile.setCreature(player);
			newTile.setHasCreature(true);
			player.setPosition(newTile.getPosition());
			player.setIndexDungeonLevel(indexNewLevel);
			dun.addMessage(messages.getString("playerDungeonLevelUp"));
			dun.updateCreatures();			
		}
		else{
			if(up == 1){			
				System.out.println("There are no UP stairs here!");
				dun.addMessage(messages.getString("playerHitCeiling"));
			}
			else{			
				System.out.println("There are no DOWN stairs here!");	
				dun.addMessage(messages.getString("playerScratchFloor"));				
			}
		}

	}

}


