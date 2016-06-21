package rlmain;

import items.Ammo;
import items.Ammo.ammoType;
import items.GoldCoins;
import items.Item;
import items.Item.itemType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.ResourceBundle;

import screens.PlayScreen;

public class Inventory implements Serializable{

	public static ResourceBundle messages = RoguelikeMain.messages;
	private static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();

	private ArrayList<Item> items;
	private int maxCapacity;
	//private Position pos;
	
	public Inventory(int max){
		items = new ArrayList<Item>(max);
		this.maxCapacity = max;
		//this.pos = p;
	}

	public int getInvCapacity(){return this.maxCapacity;}
	
	public int getNumItems(){return this.items.size();}
	
	public ArrayList<Item> getItems(){return items;}
	
	public Item get(int i){return items.get(i);}	
	
	/**
	 * Adds an item to the inventory, when possible. Stacks the item whith another items if possible.
	 * @param i The item to add.
	 */
	public void addToInventory(Item i){
		if(this.isFull()){
			//if(this.pos == PlayScreen.getPlayer().getPosition())
				PlayScreen.getDungeon().addMessage(messages.getString("yourInventoryIsSOFullTheSpace")+i.getName()+messages.getString("spaceDissapearedDot"));
			System.out.println("Inventory full.");
			return;
		}
		if(!i.isStackeable())
			this.getItems().add(i);
		else{
			if(this.getNamesOfStackeableItems().contains(i.getName())){
				//System.out.println("You already had "+(this.getA(i.getName()).getOtherItems().size()+1)+" "+i.getName()+".");
				this.getA(i.getName()).addItemToArray(i);
				//System.out.println("And now you have "+(this.getA(i.getName()).getOtherItems().size()+1));
			}else{
				//System.out.println("You have no "+i.getName()+" so you pick one (or more).");
				if(i.getOtherItems()==null)i.setArrayItems();
				this.getItems().add(i);
				//System.out.println("And now you have "+(this.getA(i.getName()).getOtherItems().size()+1));
			}
		}
	}
	
	public void remove(Item item){items.remove(item);}

	public boolean isFull(){return items.size() == this.maxCapacity;}
	
	public boolean contains(Item item) {
		for(Item i : this.items)
			if(item.getItemID() == i.getItemID()) return true;
		return false;
	}
	
	public boolean isThereA(String name){
		for(Item i : this.items)
			if(i.getName() == name) return true;
		return false;
	}
	
	public boolean isThereA(itemType type){
		for(Item i : this.items)
			if(i.getType() == type) 
				return true;
		return false;
		
	}

	public Item getA(String name) {
		for(Item i : this.items)
			if(i.getName() == name) return i;
		return null;
	}

	public Item getA(itemType type) {
		for(Item i : this.items)
			if(i.getType() == type) return i;
		return null;
	}
	
	public Item getAMissile(ammoType type){
		for(Item i : this.items){
			if(i instanceof items.Ammo){
				Ammo aux = (Ammo)i;
				if( aux.getAmmoType() == type)
					return i;		
			}
		}
		return null;
	}
	
	public ArrayList<Item> getThrowableItems(){
		ArrayList<Item> toReturn = new ArrayList<Item>();
		for(Item i: this.items)
			if(i.getType() == Item.itemType.THROWABLEWEAPON) 
				toReturn.add(i);
		return toReturn;
	}
	
	public ArrayList<Item> getEdibleItems(){
		ArrayList<Item> toReturn = new ArrayList<Item>();
		for(Item i: this.items)
			if(i.getType() == Item.itemType.FOOD) 
				toReturn.add(i);
		return toReturn;
	}
	
	public ArrayList<Item> getQuaffeableItems(){
		ArrayList<Item> toReturn = new ArrayList<Item>();
		for(Item i: this.items)
			if(i.getType() == Item.itemType.POTION) 
				toReturn.add(i);
		return toReturn;
	}
	
	public ArrayList<Item> getReadableItems(){
		ArrayList<Item> toReturn = new ArrayList<Item>();
		for(Item i: this.items)
			if(i.getType() == Item.itemType.SCROLL) 
				toReturn.add(i);
		return toReturn;
	}
	
	public ArrayList<Item> getEquippedItems(){
		ArrayList<Item> toReturn = new ArrayList<Item>();
		for(Item i: this.items)
			if(i.isEquipped()) 
				toReturn.add(i);
		return toReturn;
	}
	
	public ArrayList<Item> getEquippableItems(){
		ArrayList<Item> toReturn = new ArrayList<Item>();
		for(Item i: this.items)
			if(i.isEquippable()) 
				toReturn.add(i);
		return toReturn;
	}

	public ArrayList<Item> getUnequippedItems() {
		ArrayList<Item> toReturn = new ArrayList<Item>();
		for(Item i: this.items)
			if(i.isEquippable() && !i.isEquipped()) 
				toReturn.add(i);
		return toReturn;
	}
	
	public ArrayList<Item> getStackeableItems(){
		ArrayList<Item> toReturn = new ArrayList<Item>();
		for(Item i: this.items)
			if(i.isStackeable()) 
				toReturn.add(i);
		return toReturn;
	}
	
	public ArrayList<String> getNamesOfStackeableItems(){
		ArrayList<String> toReturn = new ArrayList<String>();
		for(Item i: this.items)
			if(i.isStackeable()) 
				toReturn.add(i.getName());
		return toReturn;
	}

	public boolean hasTheSyweroffIdol() {
		for(Item i : this.items){
			if(i.getType() == itemType.THESYWEROFFIDOL)
				return true;
		}
		return false;
	}

	public int getAmountOfGold(){
		for(Item i : this.items)
			if(i instanceof GoldCoins)
				return i.getOtherItems().size()+1;
		return 0;
	}

	
	
	
}