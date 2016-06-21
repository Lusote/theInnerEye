package dungeongen;
import java.io.Serializable;
import java.util.*;

import rlmain.RoguelikeMain;
import screens.PlayScreen;
   
/*
*	TODO:
*/

public class Room implements Serializable{
	private static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();

	private Position upLeft;
	private Position bottomLeft;
	private Position upRight;
	private Position bottomRight;
	private int index;
	private ArrayList<Position> doors = new ArrayList<Position>();
	private ArrayList<Position> walls = new ArrayList<Position>();


	/** Constructor of a Room.
	 * @param uL The upper-left Position
	 * @param bR The bottom-right Position
	 * @param ind The index of the room.
	 */
	public Room(Position uL, Position bR, int ind){
		this.index = ind;
		this.upLeft  = uL;
		this.bottomRight = bR;
		this.bottomLeft = new Position(uL.getX(),bR.getY());
		this.upRight = new Position(bR.getX(),uL.getY());
		this.walls = uL.getPositionNW().getSquare(bR.getPositionSE());
		//addDoors() adds to this.walls the tiles needed
		this.doors = addDoors(this.walls);
	}

	/**
	 * Picks two random positions within the walls of a room and makes them doors. It avoids picking corners or two together positions
	 * @param walls The walls of the Room.
	 * @return A list of the doors.
	 */
	public ArrayList<Position> addDoors(ArrayList<Position> walls){
		ArrayList<Position> toReturn = new ArrayList<Position>();
		Position p,p2;
		int randomIndex = this.getRNG().nextInt(walls.size());
		while(toReturn.size()!=2){
			p = walls.get(randomIndex);
			// Is valid and it's not a corner
			if(p.isValidPositionForDoor() && !isCorner(p, walls)){
				if(toReturn.size()==0)	toReturn.add(p);
				else{
					// Avoids having two doors together.
					p2 = toReturn.get(0);
					if(!p2.getNeighbors(4).contains(p) && p!=p2) toReturn.add(p);
				}
			}
			randomIndex = this.getRNG().nextInt(walls.size());
		}
		return toReturn;
	}
	
	public int getWide(){	return 1+(-(this.getRoomUpperLeft().getX()- this.getRoomBottomRight().getX()));}
	
	public int getHeight(){	return 1+(-(this.getRoomUpperLeft().getY()- this.getRoomBottomRight().getY()));}
	
	public boolean isCorner(Position p, ArrayList<Position> walls){
		return !(walls.contains(p.getPositionN()) &&
				 walls.contains(p.getPositionS())) 
						||	
				(walls.contains(p.getPositionE()) &&
				 walls.contains(p.getPositionW()));	
	}

	public ArrayList<Position> getFloor(){
		ArrayList<Position> toReturn = new ArrayList<Position>();
		toReturn.addAll(this.getRoomUpperLeft().getSolidSquare(this.getRoomBottomRight()));
		return toReturn;
	}
	
	public ArrayList<Position> getFloorAndDoors(){
		ArrayList<Position> toReturn = new ArrayList<Position>();
		toReturn.addAll(this.getRoomUpperLeft().getSolidSquare(this.getRoomBottomRight()));
		/*System.out.println("Getting doors.");
		System.out.println("getting: "+this.getDoors().get(0).toString());
		System.out.println("getting: "+this.getDoors().get(1).toString());
		System.out.println();*/
		toReturn.addAll(this.getDoors());
		return toReturn;
	}

	public void addWallPosition(Position p){
		ArrayList<Position> walls = this.getWalls();
		walls.add(p);
	}

	public ArrayList<Position> getWalls(){
		return this.walls;
	}

	public ArrayList<Position> getDoors(){
		return this.doors;
	}

	public Position getRoomUpperLeft(){
		return this.upLeft;
	}

	public Position getRoomUpperRight(){
		return this.upRight;
	}

	public Position getRoomBottomLeft(){
		return this.bottomLeft;
	}

	public Position getRoomBottomRight(){
		return this.bottomRight;
	}

	public Random getRNG(){return Dungeon.getRNG();}

	/**
	 * Checks if a room has been completely explored.
	 * @return True if the room is completely explored, false otherwise
	 */
	public boolean isAllExplored(){
		Tile t;
		Room r = PlayScreen.getPlayer().getPosition().getRoom(PlayScreen.getPlayer().getIndexDungeonLevel());
		int index = r.getIndex();
		ArrayList<Position> pos = PlayScreen.getDungeon().getLevel(PlayScreen.getPlayer().getIndexDungeonLevel()).getRooms().get(index).getFloorAndDoors();
		for(Position p : pos){
			t = PlayScreen.getDungeon().getLevel(PlayScreen.getPlayer().getIndexDungeonLevel()).getTile(p);
			if(!t.isExplored())return false;
		}		
		return true;
	}

	private int getIndex() {return this.index;}
	
}
