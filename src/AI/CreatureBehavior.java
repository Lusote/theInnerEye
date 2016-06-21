package AI;

import items.Item;
import items.Item.itemType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;

import rlmain.Creature;
import rlmain.RoguelikeMain;
import screens.PlayScreen;
import util.Line;
import dungeongen.Position;
import dungeongen.Tile;
import factories.CreatureFactory.creatureTypes;

public abstract class CreatureBehavior implements Serializable{

	public static ResourceBundle messages = RoguelikeMain.messages;	
	private static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();

	private Creature creature;
	
	public CreatureBehavior(Creature cre) {
		this.creature = cre;
	}

	/**
	 * Should be implemented on each IA class. The method wich controls the actions of the creature on each turn.
	 */
	public abstract void onUpdate();
	
	/**
	 * Basic controller for the movement. It checks if the position given is walkable, and attacks a creature if there is one in said position.
	 * 
	 * @param pos The position where the creature is going to move.
	 */
	public void onStep(Position pos){
		Creature cre = this.getCreature();
		int level = cre.getIndexDungeonLevel();
		Tile newTile = cre.getDungeon().getLevel(level).getTile(pos);
		if(newTile.isWalkable()){
			if(newTile.hasCreature() && !this.getCreature().equals(newTile.getCreature())) 
				cre.meleeAttack(newTile.getCreature());
			else{
				Tile oldTile = cre.getTile();
				oldTile.setHasCreature(false);
				newTile.setHasCreature(true);
				oldTile.setCreature(null);
				newTile.setCreature(this.getCreature());
				cre.setPosition(pos);
			}
		}
	}
	
	/**
	 * Moves the creature one step in a random direction.
	 * 
	 * @param hurtsEnemies If false, doesen't move if the destination has a creature already.
	 */
	public void wander(boolean hurtsEnemies){
		int dx = this.getCreature().getDungeon().getRandInt(3)-1;
		int dy = this.getCreature().getDungeon().getRandInt(3)-1;
		Position crePos = this.getCreature().getPosition();
		Position p = new Position(crePos.getX()+dx, crePos.getY()+dy);
		while(crePos == p){
			dx = this.getCreature().getDungeon().getRandInt(3)-1;
			dy = this.getCreature().getDungeon().getRandInt(3)-1;
			p = new Position(crePos.getX()+dx, crePos.getY()+dy);			
		}
		if(p.isValidPosition()){
			//No one dares to hurt the glass golem.
			//Also, there are pacific creatures.
			Tile tileDestiny = PlayScreen.getDungeon().getLevel(this.getCreature().getIndexDungeonLevel()).getTile(p);
			if( tileDestiny.hasCreature() && (tileDestiny.getCreature().getType() == creatureTypes.GLASSGOLEM  || !hurtsEnemies)){				
				return;
			}
			else
				this.getCreature().moveBy(dx, dy);
		}
	}
	/**
	 * Moves the creature one step towards the target or atttacks it if it is next to him.
	 * 
	 * @param target The creature that is being hunted.
	 */
	public void hunt(Creature target){
		if(!this.getCreature().isHunting()){
			this.getCreature().setHunting(true);
			System.out.println(this.getCreature().getName()+" is hunting!!!");
			PlayScreen.getDungeon().addMessage(messages.getString("theSpace")+this.getCreature().getName()+messages.getString("isHunting"));
		}
		ArrayList<Position> neighbors = this.getCreature().getPosition().getNeighbors(8);
		List<Position> l = new Line(this.getCreature().getPosition(), target.getPosition()).getPoints();
		for(Position p : neighbors){
			if(l.contains(p)) 
				if(p.equals(target.getPosition()))
					this.getCreature().meleeAttack(target);
				else
					this.getCreature().moveTo(p);
		}
		
	}	
	
	public Creature getCreature(){return this.creature;}

	/**
	 * Only used by the player (for now), moves the player between levels of the dungeon.
	 * @param n 1 goes a level up, 0 goes a level down.
	 */
	public void changeDungeonLevel(int n) {}

	/**
	 * Checks if a creature can see a Tile from its current position.
	 * @param t The tile being checked
	 * @return True if can see the tile, false otherwise.
	 */
	public boolean canSee(Tile t) {
		if(this.getCreature().getIndexDungeonLevel() != t.getLevel())
			return false;
		Position p = t.getPosition();
		Creature cre = this.getCreature();
		int dist = (int) ((Math.pow((cre.getTile().getPosition().getX()  - p.getX()),2 )) +
				(Math.pow((cre.getTile().getPosition().getY()  - p.getY()),2 )));
		if(dist > Math.pow(cre.getVisionRadius(),2)){
			return false;
		}
		HashSet<Position> floor = PlayScreen.getDungeon().getLevel(cre.getIndexDungeonLevel()).getFloorPositions();
		for(Position pos : new Line(cre.getPosition(),p)){
			if(!floor.contains(pos) && !pos.equals(p))
					return false;
		}
		return true;
	}
	
	/**
	 * Checks if the creature has an item on its inventory.
	 * @param type The type of the item.
	 * @return true if it has it, false otherwise.
	 */
	public boolean hasItem(itemType type){return this.getCreature().getInventory().isThereA(type);}
	
	/**
	 * Checks if the creature has a weapon equipped.
	 * @return True if it has, false otherwise.
	 */
	public boolean hasWeaponEquipped(){return this.getCreature().getWeapon()!=null;}
	
	/**
	 * Equips any item of a given type.
	 * 
	 * @param type The type of the item to equip.
	 */
	public void equipAny(itemType type){
		Item i = this.getCreature().getInventory().getA(type);
		this.getCreature().equip(i);
	}
}
