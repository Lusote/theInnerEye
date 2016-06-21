package dungeongen;
import items.Item;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.ListIterator;

import rlmain.Creature;
import rlmain.Drawable;
import rlmain.RoguelikeMain;
import screens.PlayScreen;

/*
*	TODO:
*/

public class Tile implements Serializable{
	private static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();

	private boolean isExplored;
	private boolean isOnView;
	private boolean isWalkable;
	private boolean hasCreature;
	private boolean isDoor;
	private int level;
	private ArrayList<Drawable> thingsOnTile; 
	private char symbol;
	private Position pos;
	private Creature creature;

	public Tile(Position p){
		this.isExplored = false;
		this.isOnView = false;
		this.setHasCreature(false);
		this.thingsOnTile = new ArrayList<Drawable>();
		this.symbol = '#';
		this.pos = p;
		this.isWalkable = false;
		this.creature = null;
	}

	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
    	if (other == this) return true;
	    if (!(other instanceof Tile))
	    	return false;
	    Tile another = (Tile)other;
	    if ((another.getPosition().getX() == this.getPosition().getX()) && (another.getPosition().getY() == this.getPosition().getY())) 
        	return true;
	    else return false;
	}

	@Override
	public int hashCode(){
		final int prime = 41;
		int result =1;
		result = result + this.getPosition().getX() * prime;
		result = result + this.getPosition().getY() * prime;
		return result;
	}
	
	/**
	 * Gets the tiles' neighbors.
	 * @param numNeighbors 4 (NSWE) or 8(NSWE and combinations).
	 * @return The list of the neighbors.
	 */
	public ArrayList<Tile> getNeighbors(int numNeighbors){
		ArrayList<Tile> toReturn = new ArrayList<Tile>();
		Level l = PlayScreen.getDungeon().getLevel(this.level);
		Position p = this.getPosition();
		ArrayList<Position> neigh = p.getNeighbors(numNeighbors);
		for(Position pos : neigh)
			toReturn.add(l.getTile(pos));
		return toReturn;
	}

	public Position getPosition(){
		return this.pos;
	}

	public void setPosition(Position p){
		this.pos = p;
	}

	public boolean isExplored(){
		return this.isExplored;
	}

	public void setExplored(boolean value){
		this.isExplored = value;
	}

	public char getSymbol(){
		return this.symbol;
	}

	public void setSymbol(char givenSymbol){
		this.symbol=givenSymbol;
	}

	public boolean getIsOnView(){
		return this.isOnView;
	}

	public void setOnView(boolean value){
		this.isOnView = value;
	}

	public void setCreature(Creature c){
		this.creature = c;
	}
	
	public Creature getCreature(){
		return this.creature;
	}
	
	/**
	 * Puts a Drawable in a tile, checking if it is an stackeable object, and there are more of it (combining them)
	 * @param thing The Drawable to put in the tile.
	 */
	public void putOnTile(Drawable thing){
		//System.out.println("Putting something on a tile.");
		if(!(thing instanceof Item)){
			// UNIMPLEMENTED. NOT AN ITEM
			System.out.println("ERROR: PutOnTile. Not an item.");
			return;
		}
		Item itemToPut = (Item) thing;
		//System.out.println("PutOnTile: Putting an Item on a Tile. It has other "+itemToPut.getOtherItems().size()+" attached.");
		if(!itemToPut.isStackeable()){ 				//If it doesen't stack, just add it.
			this.getThingsOnTile().add(itemToPut);
			//System.out.println("PutOnTile: Item not Stackeable.");
			return;
		}
		// IT IS STACKEABLE
		
		//--------------
		Item itemOnFloor = null;
		for(Item itemOnFloo : this.getItemsOnTile())
			if(itemToPut.getName() == itemOnFloo.getName())
				itemOnFloor = itemOnFloo;
		
		if(itemOnFloor==null){ 				//If there isnt another, just add it.
			//System.out.println("PutOnTile: There is nothing like this on the tile");
			this.getThingsOnTile().add(itemToPut);
			return;
		}
		// IT IS STACKEABLE AND THERE IS ANOTHER LIKE IT ON THE TILE
		
		//--------------
		if(itemToPut.getOtherItems().isEmpty())
			itemOnFloor.addItemToArray(itemToPut); // If it has no other items attached, just add it to the array
		// THE ITEM TO PUT HAS OTHER ITEMS ATTACHED
		
		ListIterator<Item> iteratorOtherItemsOnItemToPut = itemToPut.getOtherItems().listIterator(); // Sorry for that name
		
		while(iteratorOtherItemsOnItemToPut.hasNext()){
			itemOnFloor.addItemToArray(iteratorOtherItemsOnItemToPut.next());
		}
		
		
		
		
		
		
		return;		
		/*
		if(thing instanceof Item){
			Item itemToPut = (Item) thing;
			//System.out.println("Putting on "+this.getPosition().toString()+" a "+itemToDrop.getName());
			//System.out.println("Which has attached "+itemToDrop.getOtherItems().size()+" other items.");
			ArrayList<Item> itemsOnTile = this.getItemsOnTile();
			ListIterator<Item> iteratorItemsOnFloor = itemsOnTile.listIterator();
			while(iteratorItemsOnFloor.hasNext()){
				Item next = iteratorItemsOnFloor.next();
				if(next.isStackeable() && itemToPut.getName() == next.getName()){
					if(itemToPut.getOtherItems()!=null){
						ListIterator<Item> iterator = itemToPut.getOtherItems().listIterator();
						while(iterator.hasNext()){
							next.addItemToArray(iterator.next());
						}
					}
					itemToPut.removeArray();
					next.addItemToArray(itemToPut);
					return;
				}
				
			}
			//There was no item like this on the floor
			this.thingsOnTile.add(itemToPut);
			thing.setPosition(this.getPosition());
			return;
		}
		this.thingsOnTile.add(thing);
		*/
	}

	public ArrayList<Drawable> getThingsOnTile(){
		return this.thingsOnTile;
	}
	
	public ArrayList<Item> getItemsOnTile(){
		if(this.getThingsOnTile().isEmpty()) return new ArrayList<Item>();
		ArrayList<Item> toReturn = new ArrayList<Item>();
		for(Object o : this.getThingsOnTile()){
			if(o instanceof Item)
				toReturn.add((Item)o);
		}
		return toReturn;
	}

	public boolean isTileEmpty(){
		return this.thingsOnTile.isEmpty();
	}
	
	public void removeThing(int i){
		this.getThingsOnTile().remove(i);
	}
	
	public boolean isWalkable(){
		return this.isWalkable;
	}

	public void setWalkable(boolean walk){
		this.isWalkable=walk;
	}

	public boolean hasCreature() {
		return this.hasCreature;
	}

	public void setHasCreature(boolean hasCreature) {
		this.hasCreature = hasCreature;
	}

	public boolean isDoor() {
		return isDoor;
	}

	public void setDoor(boolean isDoor) {
		this.isDoor = isDoor;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void putCreatureInTile(Creature cre) {
		this.setHasCreature(true);
		this.setCreature(cre);
	}

}
