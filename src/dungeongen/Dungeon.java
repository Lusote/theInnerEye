package dungeongen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import rlmain.Creature;
import rlmain.RoguelikeMain;
import screens.PlayScreen;

	public class Dungeon implements Serializable{

	private static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();
	private int numOfLevels;
	private int numRoomsInLevel;
	private int indexLastLevelInitialized;
	private int lHeight,lWidth;
	private List<Level> allLevels;
	private List<Creature> creatures;
	private List<String> messages;
	private int numTurns;
	private static Dungeon uniqueDun;

	/**
	 * Creates a Dungeon.
	 * 
	 * @param numLevels The number of levels that the dungeon is going to have.
	 * @param roomsInLevel The maximum number of rooms for a level.
	 * @param width The maximum width of a level.
	 * @param height The maximum height of a level.
	 */
	private Dungeon(int numLevels, int roomsInLevel, int width, int height){
		this.numOfLevels = numLevels;
		this.numRoomsInLevel = roomsInLevel;
		this.lWidth = width;
		this.lHeight = height;
		this.allLevels = new ArrayList<Level>();
		this.creatures = new ArrayList<Creature>();
		this.messages = new ArrayList<String>();
		for(int i=0;i<numLevels;i++){
			initializeOneLevel(i,roomsInLevel);
			//this.allLevels.add(lev); initializeOneLevel already adds it.
			System.out.println("Created Level: "+i);
		}
		this.indexLastLevelInitialized = numLevels;
		for(int i = 0 ; i <=21; i++){
			this.messages.add("");
		}
		this.numTurns = 0;
	}

	/**
	 * Initializes a level. Creates a maximum of numRooms, less if needed.
	 * @param index The index of the level to create, being 0 the first level, where the game starts.
	 * @param numRooms The maximum number of rooms for the level.
	 * @return A fully initialized level.
	 */
	private Level initializeOneLevel(int index, int numRooms){
		int sizeGridX = this.getLevelWidth();
		int sizeGridY = this.getLevelHeight();
		System.out.println();
		Level oneLevel = new Level(index, sizeGridX, sizeGridY);
		Position p;
		Tile t;
		for(int i=0; i<sizeGridX; i++){
			for(int j=0; j<sizeGridY; j++){
				p = new Position(i,j);
				t = new Tile(p);
				t.setSymbol('#');
				t.setLevel(index);
		 		oneLevel.modifyGrid(p,t);
			}		
		}

		int numRoomsToMake = numRooms+getRNG().nextInt(4);
		int numItersMax = 500;
		boolean roomsMade = oneLevel.createRooms(numRoomsToMake);
		while(!roomsMade && numItersMax >0){
			System.out.println("Iters restantes: "+numItersMax);
			roomsMade = oneLevel.createRooms(numRoomsToMake);
			numItersMax--;
		}
		oneLevel.setStairs();
		this.addLevel(oneLevel);
		this.setLastLevelInitialized(index);
		for(int i=0; i<sizeGridX; i++){
			for(int j=0; j<sizeGridY; j++){
				p = new Position(i,j);
				t = oneLevel.getTile(p);
				t.setLevel(index);
		 		oneLevel.modifyGrid(p,t);
			}		
		}
		for(int i=0; i<sizeGridX; i++){
			for(int j=0; j<sizeGridY; j++){
				p = new Position(i,j);
				t = oneLevel.getTile(p);
				if(t.getSymbol()!='#')
					t.setWalkable(true);
			}		
		}
		return oneLevel;
	}

	/**
	 * Gets the list of creatures in the dungeon.
	 * @return A list of the creatures in the dungeon.
	 */
	public ArrayList<Creature> getCreatures(){
		return (ArrayList<Creature>) this.creatures;
	}
	
	/**
	 * Adds a created creature to the dungeon, for updating purposes.
	 * @param cre The creature to be added.
	 */
	public void addCreature(Creature cre){
		this.creatures.add(cre);
	}
	
	public void setCreatures(ArrayList<Creature> cres){
		this.creatures = cres;
	}

	/**
	 * Get the list of creatures without the player.
	 * @return The list of creatures without the player.
	 */
	public ArrayList<Creature> getCreaturesButPlayer(){
		ArrayList<Creature> toReturn = this.getCreatures();
		Iterator<Creature> it = toReturn.iterator();
		while(it.hasNext()){
			if(it.next().isPlayer()){
				it.remove();
				break;
			}
		}
		return toReturn;
	}
	
	/**
	 * Gets a creature from its unique id.
	 * @param id Id of the creature .
	 * @return The creature.
	 */
	public Creature getCreature(int id) {
		//System.out.println("Creatures: "+this.getCreatures().size() );
		for(Creature c : this.getCreatures()){
			if(c.getId() == id) return c;
		}
		return null;
	}
	
	/**
	 * Iterates over the list of creatures, updating them.
	 */
	public void updateCreatures(){
		this.numTurns++;
		ArrayList<Creature> list = this.getCreatures();
		for(int i = 0; i < list.size(); i++){
			if(list.get(i)!=null)
				list.get(i).update();
		}
	}
	
	/**
	 * Deletes a creature, for now its just used when its killed
	 * @param c The creature to delete.
	 */
	public void delete(Creature c){
		Tile t =this.getLevel(c.getIndexDungeonLevel()).getTile(c.getPosition());
		t.setHasCreature(false);
		t.setCreature(null);
		this.getCreatures().remove(c);
	}
	
	public Level getLevel(int lev){
		return this.allLevels.get(lev);
	}

	public int getLevelWidth(){
		return this.lWidth;
	}

	public int getLevelHeight(){
		return this.lHeight;
	}
	
	/**
	 * Adds a message to the list of messages to print.
	 * @param m The message to print.
	 */
	public void addMessage(String m){
		if(m.length() < 80){
			this.messages.add(m);
			//rlmain.RoguelikeMain.getSpeaker().speak(m);
		}else{
			this.messages.add(m.substring(0, 80));
			this.messages.add(m.substring(80, 80));
			System.out.println("MESSAGE TOO LONG. NEEDS TO BE CUT.");
		}
	}
	
	public ArrayList<String> getMessages(){return (ArrayList<String>) this.messages;}

	public int getNumOfLevels(){return numOfLevels;}

	public void setNumOfLevels(int numOfLevels){this.numOfLevels = numOfLevels;}

	public int getLastLevelInitialized(){return indexLastLevelInitialized;}

	public void setLastLevelInitialized(int lastLevelInitialized){this.indexLastLevelInitialized = lastLevelInitialized;}

	public List<Level> getLevels(){return this.allLevels;}
	
	public void addLevel(Level l){this.getLevels().add(l);}

	public int getNumRoomsInLevel(){return numRoomsInLevel;}

	public void setNumRoomsInLevel(int numRoomsInLevel){this.numRoomsInLevel = numRoomsInLevel;}
	
	public int getNumTurns(){return this.numTurns;}
	
	public static Random getRNG(){return PlayScreen.getTheRNG();}
	
	public int getRandInt(){return getRNG().nextInt();}
	
	public int getRandInt(int n){return getRNG().nextInt(n);}
	
	public double getRandDouble(){return getRNG().nextDouble();}

	/**
	 * Singleton management of the Dungeon. Ensures that there is only ONE dungeon for game.
	 * 
	 * @param numOfLevels The number of levels that the dungeon is going to have.
	 * @param roomsPerLevel The maximum number of rooms for a level.
	 * @param worldWidth The maximum width of a level.
	 * @param worldHeight The maximum height of a level.
	 * @return The dungeon created.
	 */
	public static Dungeon getDungeon(int numOfLevels, int roomsPerLevel, int worldWidth,int worldHeight){
		if(uniqueDun != null) return uniqueDun;
		else uniqueDun = new Dungeon(numOfLevels,roomsPerLevel, worldWidth, worldHeight);
		return uniqueDun;
	}
	public static void resetDungeon() {
		uniqueDun = null;		
	}
}
