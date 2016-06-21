package dungeongen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

import rlmain.RoguelikeMain;
import screens.PlayScreen;
import util.Astar;

public class Level implements Serializable{
	
	private static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();
	private int index;
	private int gridHeight;
	private int gridWidth;
	private Tile[][] grid; 
	private ArrayList<Room> rooms;
	private HashSet<Position> floorPositions;
	private HashSet<Position> floorAndWallsPositions;
    private Position stairsUp;
    private Position stairsDown;


	/**
	 * Constructor for the level.
	 * @param ind The index of the level, being 0 the first level, where the game is started.
	 * @param gW The width of the grid.
	 * @param gH The height of the grid.
	 * @param rng The RNG.
	 */
	public Level(int ind, int gW, int gH){
		this.rooms = new ArrayList<Room>();
		this.floorPositions = new HashSet<Position>();
		this.floorAndWallsPositions = new HashSet<Position>();
		Position origin = new Position(0,0);
		Position endend = new Position(gW,gH);
		ArrayList<Position> border = origin.getSquare(endend);
		floorAndWallsPositions.addAll(border);
		Tile t;
		this.index = ind;
		this.gridHeight = gH;
		this.gridWidth = gW;
		this.grid = new Tile[gW][gH];
		for(int i = 0; i <gW ;i++){
		 	for(int j=0;j<gH;j++){
		 		this.grid[i][j] = new Tile(new Position(i,j));
		 		t = this.grid[i][j];
		 		t.setLevel(ind);
		 	}
		 }
		 this.stairsDown = null;
		 this.stairsUp = null;
	}
	
	/**
	 * Resets a level, for when it fails at create the number of rooms required.
	 */
	public void resetLevel(){
		System.out.println("RESETING LEVEL");
		this.rooms = new ArrayList<Room>();
		this.floorPositions = new HashSet<Position>();
		this.floorAndWallsPositions = new HashSet<Position>();
		Position origin = new Position(0,0);
		Position endend = new Position(this.gridWidth, this.gridHeight);
		ArrayList<Position> border = origin.getSquare(endend);
		floorAndWallsPositions.addAll(border);
		this.grid = new Tile[this.gridWidth+1][this.gridHeight+1];
		for(int i = 0; i <=this.gridWidth ;i++){
		 	for(int j=0;j<=this.gridHeight;j++){
		 		this.grid[i][j] = new Tile(new Position(i,j));
		 	}
		 }
		 this.stairsDown = null;
		 this.stairsUp = null;
		
	}

 	/**
 	 * Create a room in a level.
 	 * @return The room created, null if it couldn't be created.
 	 */
 	public Room createRoom(){
 		Room r = null;
		Tile t;
		// Positions for room creation.
		HashSet<Position> used = this.getFloorAndWallsPosition();
		ArrayList<Position> roomPos = this.getRoomPositions(used);
		int index = this.getRooms().size();
		if(roomPos.size() == 2){
			r = new Room(roomPos.get(0),roomPos.get(1), index);
			// We set the symbol on the floor tiles.
			for(Position p : r.getFloor()){
				t = getTile(p);
				if(t!=null){
					t.setSymbol('.');
					addFloorPosition(p);
					addFloorOrWallsPosition(p);
					t.setWalkable(true);
				}
			}
			// 	We add the tiles to the Sets
			this.floorAndWallsPositions.addAll(r.getWalls());
			//	this.floorAndWallsPositions.addAll(r.getDoors());
			for(Position p : r.getWalls()){
				this.getTile(p).setSymbol('#');
			}
			// 	Change the doors tiles symbols.
			for(Position p : r.getDoors()){
				this.getTile(p).setSymbol('d');
				getTile(p).setWalkable(true);
				getTile(p).setDoor(true);
			}
			addRoom(r);
		}
		return r;
	}

	/**
	 * Deletes a room.
	 * @param indexRoom The index of the room to be deleted.
	 */
	public void deleteRoom(int indexRoom){
		Room toDelete = this.getRooms().get(indexRoom);
		ArrayList<Position> floor = toDelete.getFloor();
		ArrayList<Position> walls = toDelete.getWalls();
		this.getFloorPositions().removeAll(floor);
		this.getFloorAndWallsPosition().removeAll(floor);
		this.getFloorAndWallsPosition().removeAll(walls);
		this.getRooms().remove(indexRoom);
	}

	// Returns uL and bR
	/**
	 * Obtains the upper left and bottom right positions for a room.
	 * @param invalid The invalid positions. The already used ones.
	 * @return A list of the room's positions.
	 */
	public ArrayList<Position> getRoomPositions(HashSet<Position> invalid){
		boolean finded = false;
		int tries = 100;
		ArrayList<Position> toReturn = new ArrayList<Position>();
		while(!finded && tries >0){
			toReturn.clear();
			int randomIndexULX = this.getRNG().nextInt(this.getGridWidth()-1)+1;
			int randomIndexULY = this.getRNG().nextInt(this.getGridHeight()-1)+1;
			int randomIndexBRX = randomIndexULX + this.getRNG().nextInt(6)+4;		
			int randomIndexBRY = randomIndexULY  + this.getRNG().nextInt(5)+2;
			Position uL = new Position(randomIndexULX,randomIndexULY);
			Position bR = new Position(randomIndexBRX, randomIndexBRY);
			while(!uL.isValidPositionForRoom() ||
				!bR.isValidPositionForRoom()){
					randomIndexULX = this.getRNG().nextInt(this.getGridWidth()-1)+1;
					randomIndexULY = this.getRNG().nextInt(this.getGridHeight()-1)+1;
					randomIndexBRX = randomIndexULX + this.getRNG().nextInt(6)+4;		
					randomIndexBRY = randomIndexULY  + this.getRNG().nextInt(5)+2;
					uL = new Position(randomIndexULX,randomIndexULY);
					bR = new Position(randomIndexBRX, randomIndexBRY);
			}
			toReturn.add(uL);
			toReturn.add(bR);

			// Now we check the tiles the room will use.
			// roomWallsAndFloor is the room plus two extra tiles on every direction
			ArrayList<Position> roomWallsAndFloor  = uL.getPositionNW().getPositionNW().getPositionNW().getSolidSquare(bR.getPositionSE().getPositionSE().getPositionSE());
			finded = true;
			for(Position p : roomWallsAndFloor){
				// Checks with ALL the positions used on the level.
				if(invalid.contains(p)){
					//System.out.println("ERROR Creating Room: Position already used.");
					tries--;
					finded = false;
					break;
				}
			}
		}
		return toReturn;
	}
	
	// Returns the path between two positions
	/**
	 * Gets the path between two given positions using A*. Used to link Rooms. Modifies the Level list of floor and wall tiles.
	 * 
	 * @param start The start Position.
	 * @param end The end of the path.
	 * @param numNeighbors The number of the usable neighbors, for the A*.
	 * @return a list of the Positions of the path, null if it wasn't impossible to create one.
	 */
	public ArrayList<Position> getPath(Position start, Position end, int numNeighbors){
		HashSet<Position> walls = new HashSet<Position>();
		walls.addAll(this.getFloorAndWallsPosition());
		ArrayList<Position> toReturn = Astar.getPath(start, end, numNeighbors, walls);
		if(toReturn != null)
			for(Position p : toReturn){
				this.addFloorPosition(p);
				this.getFloorAndWallsPosition().addAll(p.getNeighbors(8));
			}
		return toReturn;
	}
		
	/**
	 * Returns a walkable path between two positions using ALL the tiles.
	 * 
	 * @param start The start Position.
	 * @param end The end of the path.
	 * @param numNeighbors The number of the usable neighbors, for the A*.
	 * @returnA list of the positions for the path.
	 */
	public ArrayList<Position> getWalkingPath(Position start, Position end, int numNeighbors){
		HashSet<Position> walls = new HashSet<Position>();
		Position pos;
		for(int i = 0; i <this.gridWidth ;i++){
		 	for(int j=0;j<this.gridHeight;j++){
		 		pos = new Position(i, j);
		 		if(getTile(pos).getSymbol()=='#')
		 			walls.add(pos);
		 	}
		 }		
		ArrayList<Position> toReturn = Astar.getPath(start, end, numNeighbors, walls);
		//Creature player = PlayScreen.getPlayer();
		//System.out.println("Player is on: "+PlayScreen.getPlayer().getPosition().toString());
		//System.out.println("Player goes to: "+PlayScreen.getPlayer().getDestiny().toString());
		toReturn.remove(start);
		return toReturn;
	}
	
	/**
	 * Returns a walkable path between two positions using only the already explored tiles.
	 * 
	 * @param start The start Position.
	 * @param end The end of the path.
	 * @param numNeighbors The number of the usable neighbors, for the A*.
	 * @returnA list of the positions for the path.
	 */
	public ArrayList<Position> getWalkingKnownPath(Position start, Position end, int numNeighbors){
		HashSet<Position> walls = new HashSet<Position>();
		Position pos;
		for(int i = 0; i <this.gridWidth ;i++){
		 	for(int j=0;j<this.gridHeight;j++){
		 		pos = new Position(i, j);
		 		if(getTile(pos).getSymbol()=='#' || !getTile(pos).isExplored())
		 			walls.add(pos);
		 	}
		 }		
		ArrayList<Position> toReturn = Astar.getPath(start, end, numNeighbors, walls);
		//Creature player = PlayScreen.getPlayer();
		//System.out.println("Player is on: "+PlayScreen.getPlayer().getPosition().toString());
		//System.out.println("Player goes to: "+PlayScreen.getPlayer().getDestiny().toString());
		toReturn.remove(start);
		return toReturn;
	}
	
	/**
	 * Links the doors of two rooms, creating a walkable path.
	 * @param first The first room to be linked.
	 * @param second The second room.
	 * @return True if it went ok, false if it is impossible to link them.
	 */
	public boolean linkRooms(Room first, Room second){
		Position start = first.getDoors().get(1);
		this.getTile(start).setSymbol('.');
		Position end = 	second.getDoors().get(0);
		this.getTile(end).setSymbol('.');
		ArrayList<Tile> tilesPath = new ArrayList<Tile>();
		HashSet<Position> walls = this.getFloorAndWallsPosition();
		walls.remove(end); 
		ArrayList<Position> path = this.getPath(start, end, 4);
		if(path!= null){
			for(Position p : path){
				Tile t = this.getTile(p);
				t.setSymbol('.');
				t.setWalkable(true);
				tilesPath.add(t);
			}	
			return true;
		}
		return false;
	}
	
	/**
	 * Create a given number of rooms in a level.
	 * @param number The number of rooms to be created.
	 * @return False if it was impossible to create that many rooms, true if it went OK.
	 */
	public boolean createRooms(int number){
		//int tries = 0;
		int triesLeft = 100;
		boolean done=false;
		while(this.getNumRooms() != number && triesLeft > 0 && !done){
			if(this.getNumRooms() == 0){
				if(this.createRoom() == null){
					this.resetLevel();
					//tries++;
					triesLeft--;
					System.out.println("ERROR CREATING ROOM: "+this.getNumRooms()+". "+triesLeft);
					continue;
				}
				else{ 
					if(number == 1){
						//System.out.println("SUCCESS!! Levels created: "+tries);
						return true;
					}
				}
			}
			if(this.getNumRooms() < number){
				if(this.createRoom() != null){
					boolean linked = this.linkRooms(this.getRooms().get(this.getNumRooms()-2), this.getRooms().get(this.getNumRooms()-1));
					if(!linked){
						System.out.println("ERROR LINKING ROOMS "+(this.getNumRooms()-2)+" and "+(this.getNumRooms()-1)+". "+triesLeft);
						this.deleteRoom(this.getNumRooms()-1);
						this.resetLevel();
						//tries++;
						triesLeft--;
						continue;
					}
				}
				else{
					System.out.println("ERROR CREATING ROOM: "+this.getNumRooms()+". "+triesLeft);
					this.resetLevel();
					//tries++;
					triesLeft--;
					continue;
				}
			}
			if(this.getNumRooms() == number){
				boolean linked = this.linkRooms(this.getRooms().get(this.getNumRooms()-1), this.getRooms().get(0));
				if(!linked){
					System.out.println("ERROR LINKING ROOMS "+(this.getNumRooms()-1)+" and 0"+". "+triesLeft);
					this.deleteRoom(this.getNumRooms()-1);
					this.resetLevel();
					//tries++;
					triesLeft--;
					continue;
				}
				else{
					System.out.println("WIN. Rooms created: "+ this.getNumRooms()+". "+triesLeft);
					done=true;
				}
			}
		}
		if(done) return true;
		System.out.println("FAIL. Couldn't create "+number+" rooms connected."+". "+triesLeft);
		this.resetLevel();
		return false;
	}
	
	public void addRoom(Room r){
		this.rooms.add(r);
	}

	public int getNumRooms(){
		return this.getRooms().size();
	}

	public Tile[][] getGrid(){
		return this.grid;
	}
	
	public void modifyGrid(Position p, Tile t){
		this.grid[p.getX()][p.getY()] = t;
	}

	public ArrayList<Room> getRooms(){
		return this.rooms;
	}	

	public HashSet<Position> getFloorAndWallsPosition(){
		return this.floorAndWallsPositions;
	}

	public void addFloorOrWallsPosition(Position p){
		this.floorAndWallsPositions.add(p);
	}

	public HashSet<Position> getFloorPositions(){
		return this.floorPositions;
	}

	public void addFloorPosition(Position p){
		this.floorPositions.add(p);
	}

	public Tile getTile(Position p){
		if(p==null)return null;
		return this.grid[p.getX()][p.getY()];
	}

	public int getIndexLevel(){
		return this.index;
	}
	
	/** Gets a list of the unexplored positions of a level.
	 * @return A list of the unexplored positions of a level.
	 */
	public ArrayList<Position> getUnexploredFloorPositions(){
		ArrayList<Position> toReturn = new ArrayList<Position>();
		Tile t;
		Position p;
		for(int i = 0; i <this.gridWidth ;i++){
		 	for(int j=0;j<this.gridHeight;j++){
		 		p = new Position(i,j);
		 		t = this.getTile(p);
		 		if(t.getSymbol()=='.' && !t.isExplored())
		 			toReturn.add(p);
		 	}
		 }
		toReturn.remove(PlayScreen.getPlayer().getPosition());
		Collections.shuffle(toReturn, PlayScreen.getTheRNG());
		//Collections.sort(toReturn, Position.WALKING_DISTANCE_TO_PLAYER_NEAR);
		return toReturn;
	}
	
	/**
	 * Gets a position of a random floor Tile from the level.
	 * @return The position of the random tile.
	 */
	public Position getRandomFloorPosition(){
		Position p = null;
		ArrayList<Position> positions = new ArrayList<Position>(this.getFloorPositions());
		int numPos = positions.size();
		int randomPos;
		while(p==null){
			randomPos = this.getRNG().nextInt(numPos);
			if(this.getTile(positions.get(randomPos)).getThingsOnTile().isEmpty() && !this.getTile(positions.get(randomPos)).hasCreature()){
				p = positions.get(randomPos);
			}
		}
		return p;
	}

	public Tile getRandomTile(){
		Position p = this.getRandomFloorPosition();
		return this.getTile(p);
	}

	public Tile getRandomEmptyTile(){
		Position p = this.getRandomFloorPosition();
		while(this.getTile(p).getCreature()!=null){
			p = this.getRandomFloorPosition();
		}
		return this.getTile(p);
	}
	
	public Tile getEmptyNeighborTile(Tile t){
		Position p = t.getPosition();
		for(Position neigh : p.getNeighbors(8)){
			if(!this.getTile(neigh).hasCreature())
				return this.getTile(neigh);
		}
		for(Position neigh : p.getNeighbors(8)){
			for(Position neigh2 : neigh.getNeighbors(8))
				if(!this.getTile(neigh2).hasCreature())
					return this.getTile(neigh2);
		}
		return null;
	}
	
	public void setStairs(){
		//System.out.println("Number of rooms at the time setStairs is called: "+this.getNumRooms());
		this.setStairsDown();
		this.setStairsUp();
	}
	
	public Position getStairsUp(){
		return this.stairsUp;
	}

	public Position setStairsUp(){
		int num = this.getRNG().nextInt(this.getNumRooms());
		while(num<0)
			num = this.getRNG().nextInt(this.getNumRooms());
		Room r = this.getRooms().get(num);
		num = this.getRNG().nextInt(r.getFloor().size()-1);
		while(num<0)
			num = this.getRNG().nextInt(r.getFloor().size());
		Position p = r.getFloor().get(num);
		this.stairsUp = p;
		if(this.getStairsDown()!= null && this.getStairsDown().equals(p)){
			this.getTile(stairsUp).setSymbol('X');
		}else this.getTile(stairsUp).setSymbol('<');
		return stairsUp;
	}

	public Position getStairsDown(){
		if(isLastLevel()) return null;
		return this.stairsDown;
	}

	public Position setStairsDown(){
		if(isLastLevel()){
			return null;
		}
		int num = this.getRNG().nextInt(this.getNumRooms());
		while(num<0){
			num = this.getRNG().nextInt(this.getNumRooms());
		}
		Room room = this.getRooms().get(num);
		num = this.getRNG().nextInt(room.getFloor().size()-1);
		while(num<0){
			num = this.getRNG().nextInt(room.getFloor().size());
		}
		Position p = room.getFloor().get(num);
		this.stairsDown = p;
		if(this.getStairsUp()!= null && this.getStairsUp().equals(p)){
			this.getTile(stairsDown).setSymbol('X');
		}
		else {
			this.getTile(stairsDown).setSymbol('>');
		}
		return stairsDown;
	}
	
	public boolean isLastLevel(){
		boolean toReturn = (this.index == PlayScreen.getNumberOfLevels()-1);
		return toReturn;
	}

	public int getGridHeight(){
		return this.gridHeight;
	}

	public int getGridWidth(){
		return this.gridWidth;
	}

	public Random getRNG(){return PlayScreen.getTheRNG();}

}
