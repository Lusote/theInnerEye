package dungeongen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.Set;

import rlmain.RoguelikeMain;
import screens.PlayScreen;

public class Position implements Serializable{
	public static ResourceBundle messages = RoguelikeMain.messages;
	private static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();

	private int i;
	private int j;
	private static int gridWidth = 79;
	private static int gridHeight = 23;


	/**
	 * Constructor.
	 * @param x 
	 * @param y
	 */
	public Position(int x, int y){
		this.i = x;
		this.j = y;
	}
	
	/**
	 * Creates the relative position between p and p2
	 * @param p
	 * @param p2
	 */
	public Position(Position p, Position p2){
		this.i = p.getX() - p2.getX();
		this.j =p2.getY() - p.getY();
	}
	
	// Farthest first
	public static final Comparator<Position> MAX_DISTANCE_TO_PLAYER =  new Comparator<Position>() {
		public int compare(Position p1, Position p2) {
			return p2.distance(PlayScreen.getPlayer().getPosition()).compareTo(p1.distance(PlayScreen.getPlayer().getPosition()));
		}
	};
	
	// Nearest first
	public static final Comparator<Position> MIN_DISTANCE_TO_PLAYER =  new Comparator<Position>() {
		public int compare(Position p1, Position p2) {
			return p1.distance(PlayScreen.getPlayer().getPosition()).compareTo(p2.distance(PlayScreen.getPlayer().getPosition()));
		}
	};
		
	/**
	 * Calculates the real distance between two positions.
	 * @param b The second position.
	 * @return The distance.
	 */
	public Double distance(Position b){
		Position a = this;
		return new Double(Math.sqrt(Math.pow(b.getX()-a.getX(),2) + Math.pow(b.getY()-a.getY(),2)));
	}	
	
	public String toString(){
		return this.getX()+", "+this.getY();
	}

	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
    	if (other == this) return true;
	    if (!(other instanceof Position))
	    	return false;
	    Position another = (Position)other;
	    if ((another.getX() == this.getX()) && (another.getY() == this.getY())) 
        	return true;
	    else return false;
	}

	@Override
	public int hashCode(){
		final int prime = 41;
		int result =1;
		result = result + this.getX() * prime;
		result = result + this.getY() * prime;
		return result;
	}
	
	/**
	 * Returns the room which contains a given position
	 * @param level The level of the dungeon that we are checking.
	 * @return The index of the room, null if it doesn't belong to any room.+
	 */
	public Room getRoom(int level){
		for(Room r : PlayScreen.getDungeon().getLevel(level).getRooms()){
			if(r.getFloorAndDoors().contains(this))
				return r;
		}
		return null;
	}

	/**
	 * Checks if a position is within the map's limits.
	 * @return True if it is within the map's limits, false otherwise.
	 */
	public boolean isValidPosition(){
		if( 0 <= this.getX() && this.getX() < gridWidth &&
			0 <= this.getY() && this.getY() < gridHeight ){
			return true;
		}
		return false;
	}

	/**
	 * Checks if a position is within the map's limits for a door, considering that it needs space for a corridor and the wall.
	 * @return True if it's a valid position for a door, false otherwise.
	 */
	public boolean isValidPositionForDoor(){
		if( 2 <= this.getX() && this.getX() <= gridWidth-2 &&
			2 <= this.getY() && this.getY() <= gridHeight-2 ){
			return true;
		}
		return false;
	}

	/**
	 * Checks if a position is within the map's limits for a room, considering that it needs space for the wall.
	 * @return True if it's a valid position for a room, false otherwise.
	 */
	public boolean isValidPositionForRoom(){
		if( 1 <= this.getX() && this.getX() <= gridWidth-1  &&
			1 <= this.getY() && this.getY() <= gridHeight-1
			){
			return true;
		}
		return false;
	}

	public boolean isPositionUsed(Set<Position> posUsed){
		if(posUsed.contains(this)){
			return true;
		}
		else return false;
	}

	/**
	 * Get the neighbors of a  Position, in a determinate order.
	 * @param numNeighbors Can be 4 (NSWE) or 8 (NSWE and its combinations)
	 * @return The list of neighbors, in a determinate order.
	 */
	public ArrayList<Position> getNeighbors(int numNeighbors){
		ArrayList<Position> toReturn = new ArrayList<Position>();
		if(this.getPositionN().isValidPosition()) toReturn.add(this.getPositionN());
		if(numNeighbors == 8 && this.getPositionNE().isValidPosition()) toReturn.add(this.getPositionNE());
		if(this.getPositionE().isValidPosition()) toReturn.add(this.getPositionE());
		if(numNeighbors == 8 && this.getPositionSE().isValidPosition()) toReturn.add(this.getPositionSE());
		if(this.getPositionS().isValidPosition()) toReturn.add(this.getPositionS());
		if(numNeighbors == 8 && this.getPositionSW().isValidPosition()) toReturn.add(this.getPositionSW());
		if(this.getPositionW().isValidPosition()) toReturn.add(this.getPositionW());
		if(numNeighbors == 8 && this.getPositionNW().isValidPosition()) toReturn.add(this.getPositionNW());
		return toReturn;
	}

	/** Gets a solid square of Positions, from the upper-left Position
	 * @param bR The bottom-right position.
	 * @return The list of positions within the square.
	 */
	public ArrayList<Position> getSolidSquare(Position bR){
		Position uL = this;
		ArrayList<Position> toReturn = new ArrayList<Position>();
		for(int i = uL.getX(); i <= bR.getX(); i++){
			for(int j = uL.getY(); j <= bR.getY();j ++){
				toReturn.add(new Position(i,j));
			}
		}
		return toReturn;
	}

	/** Gets, from the upper-left Position, the border of a square.
	 * @param bR The bottom-right position.
	 * @return The list of positions of the border of the square.
	 */
	public ArrayList<Position> getSquare(Position bR){
		ArrayList<Position> toReturn = this.getSolidSquare(bR);
		ArrayList<Position> toRemove = this.getPositionSE().getSolidSquare(bR.getPositionNW());
		toReturn.removeAll(toRemove);
		return toReturn;
	}
	
	public Position getPositionN(){
		Position p = new Position(this.getX(), this.getY()-1);
		return p;
	}

	public Position getPositionNE(){
		Position p = new Position(this.getX()+1, this.getY()-1);
		return p;
	}

	public Position getPositionE(){
		Position p = new Position(this.getX()+1, this.getY());
		return p;
	}	

	public Position getPositionSE(){
		Position p = new Position(this.getX()+1, this.getY()+1);
		return p;
	}

	
	public Position getPositionS(){
		Position p = new Position(this.getX(), this.getY()+1);
		return p;
	}

	public Position getPositionSW(){
		Position p = new Position(this.getX()-1, this.getY()+1);
		return p;
	}
	
	public Position getPositionW(){
		Position p = new Position(this.getX()-1, this.getY());
		return p;
	}

	public Position getPositionNW(){
		Position p = new Position(this.getX()-1, this.getY()-1);
		return p;
	}

	public int getX(){
		return this.i;
	}

	public int getY(){
		return this.j;
	}

	public void setX(int pos){
		this.i=pos;
	}

	public void setY(int pos){
		this.j=pos;
	}
	
	/**
	 * Gets the relative position to another Position, in coordinates.
	 * @param pos The position to which we're calculating the relative Position.
	 * @return The relative position.
	 */ 
	public Position getRelativeDetailedPosition(Position pos){
		return new Position(pos.getX()-this.getX(), this.getY()-pos.getY());
	}

	/**
	 * Gets the relative position to another Position, in text.
	 * @param pos The position to which we're calculating the relative Position.
	 * @return The description of relative position.
	 */ 
	public String getRelativePosition(Position position){
		int x = position.getX()-this.getX();
		int y = this.getY()-position.getY();
		if(x==0)
			if(y>0)	return messages.getString("north");
			else 	return messages.getString("south");
		if(y==0)
			if(x>0)	return messages.getString("east");
			else 	return messages.getString("west");
		if(x == -y)
			if(x>0)	return messages.getString("southeast");
			else	return messages.getString("northwest");
		if(x == y)
			if(x>0) return messages.getString("northeast");
			else 	return messages.getString("southwest");
		
		if(Math.abs(x) <= Math.abs(y/4)) 
			if(y>0)	return messages.getString("northish");
			else 	return messages.getString("southish");
		if(Math.abs(y) <= Math.abs(x/4)) 
			if(x>0)	return messages.getString("eastish");
			else 	return messages.getString("westish");
		
		if(x>0) {
			if(y>0)	return messages.getString("northeastish");
			else 	return messages.getString("southeastish");}
		else if(y>0)return messages.getString("northwestish");
			   else return messages.getString("southwestish");
	}
	
}
