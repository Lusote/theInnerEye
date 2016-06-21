package rlmain;

import items.Ammo.ammoType;
import items.Armor;
import items.Food;
import items.Item;
import items.Item.itemMaterial;
import items.Item.itemType;
import items.Potion;
import items.RangedWeapon;
import items.Scroll;
import items.ThrowableWeapon;
import items.Weapon;
import items.Weapon.weaponType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import screens.PlayScreen;
import squidpony.squidcolor.SColor;
import util.Line;
import util.Util;
import AI.CreatureBehavior;
import dungeongen.Dungeon;
import dungeongen.Level;
import dungeongen.Position;
import dungeongen.Tile;
import effects.BlindEffect;
import effects.Effect;
import effects.InstaDamageEffect;
import effects.InstaHealEffect;
import effects.PoisonEffect;
import effects.TheSyweroffIdolEffect;
import factories.Factory;
import factories.CreatureFactory.creatureTypes;
import factories.FoodFactory.foodType;

public class Creature implements Drawable, Serializable{
	
	public static ResourceBundle messages = RoguelikeMain.messages;
	private static final long serialVersionUID = RoguelikeMain.getSerialVersionUID(); 
	
	private char symbol;
	private SColor color;
	private Position pos;
	private int maxHealth;
	private int hp;
	private int maxMana;
	private int mana;
	private int id;
	public static int creaturesMade=0;
	private int dungeonLevel;
	private int attack;
	private int defense;
	private int strength;
	private int dextery;
	private int dodge;
	private int intelligence;
	private CreatureBehavior ai;
	private String name;
	private int visionRadius;
	private int visionRadiusDef;
	private Inventory inventory;
	private Armor head;
	private Weapon weapon;
	private Armor shield;
	private Armor chest;
	private Armor feet;
	private Item quiver;
	private List<Effect> effects;
	private int hungerLevel;
	private int maxHunger = 1000;
	private int nutrValue;
	private List<String> eatMessage;
	private List<Effect> corpseEffects;
	private List<String> description;
	private int xp;
	private int level;
	private String noise;
	private int hearingRadius;
	private int hearingRadiusDef;
	private boolean hunting;
	private foodType favFood;
	private List<Creature> enemiesSawLastTurn;
	private boolean isTraveling;
	private Position destiny;
	public enum creatureClasses {WARRIOR, ARCHER, WHITEMAGE;};
	private creatureClasses clas;
	private double hpRegenerationRate;
	private double manaRegenerationRate;
	private int score;
	private double luck;
	private List<Spell> spellsKnown;
	private ArrayList<HashMap<Position, Character>> memory;
	public enum actions{
		MELEEATTACK, MISSMELEEATTACK,  THROWATTACK, MISSTHROWATTACK, RANGEDATTACK, MISSRANGEDATTACK, READ, QUAFF, CAST, EQUIP, UNEQUIP, KILL; 
	};
	private creatureTypes type;
	
	public Creature(){
		this.setId(creaturesMade);
		creaturesMade++;
		this.setManaRegenerationRate(0);
		this.setLuck(0.01);
	}
	
	public void setBehavior(CreatureBehavior b){this.ai = b;}
	
	public CreatureBehavior getBehavior(){return this.ai;}
	
	public void moveTo(Position p){this.ai.onStep(p);}
	
	/**
	 * Checks if an attack hits the target, calculating different things in function of the type of attack.
	 * @param target The creature target.
	 * @param action The type of attack.
	 * @return True if it hits, false otherwise.
	 */
	public boolean didItHit(Creature target, actions action){
		int attackDextery = this.getDextery();
		int targetDodge = target.getDodge();
		int luckFactor = Util.oddsLessThan(this.getLuck()) ? 1 : 0;
		switch(action){
			case MELEEATTACK:
				if(attackDextery+(PlayScreen.getTheRNG().nextInt(5))+luckFactor  >  targetDodge +(PlayScreen.getTheRNG().nextInt(5))) 
					return true;
				else 
					return false;
			case RANGEDATTACK:
				if(attackDextery+(PlayScreen.getTheRNG().nextInt(5))+luckFactor  >  targetDodge +(PlayScreen.getTheRNG().nextInt(5))) 
					return true;
				else 
					return false;
			case THROWATTACK:
				if(attackDextery+(PlayScreen.getTheRNG().nextInt(5))+luckFactor  >  targetDodge +(PlayScreen.getTheRNG().nextInt(5))) 
					return true;
				else 
					return false;
			default:
		}
		return false;
	}
	
	public void meleeAttack(Creature target){
		/*CHECKS*/
		if(this == target){
			System.out.println("AUTODAMAGING! The "+this.getName()+" on level "+this.getIndexDungeonLevel()+" attacks the "+target.getName()+" on level "+target.getIndexDungeonLevel());
			//return;
		}else
			System.out.println("The "+this.getName()+" on level "+this.getIndexDungeonLevel()+" attacks the "+target.getName()+" on level "+target.getIndexDungeonLevel());
		if(this.getIndexDungeonLevel()!= target.getIndexDungeonLevel()){
			System.out.println("ERROR trying to attack from. "+this.getPosition().toString()); 
			return;
		}
		/*ENDCHECKS*/
		
		Creature attacker = this;
		//MISS
		if(!attacker.didItHit(target, actions.MELEEATTACK)){
			if(target.isPlayer())
				getDungeon().addMessage(messages.getString("youDodgeTheSpace")+attacker.getName()+messages.getString("spaceAttack"));
			if(attacker.isPlayer()){
				getDungeon().addMessage(messages.getString("theSpace")+target.getName()+messages.getString("spaceDodgesYourAttack"));
				attacker.getDungeon().updateCreatures();
				attacker.modifyHunger(-5);
				attacker.addScore(actions.MISSMELEEATTACK);
			}
			return;
		}
		
		
		//HIT
		int weaponMod = 0;
		weaponMod = this.getWeapon()==null ? 0: this.getWeapon().getDamage();
		int maxDamage = Math.max(0, (this.getAttack()+weaponMod) - target.getDefense());
		int realDamage = (int)(getDungeon().getRandDouble() * maxDamage)+1;
		//System.out.println(messages.getString("theSpace")+this.getName()+" attacks the "+target.getName()+messages.getString("spaceForSpace")+realDamage+" hp, with a weaponMod of "+weaponMod+messages.getString("dot"));
		
		//This is where the % of success will go
		target.modifyHp(-realDamage);
		//
		if(this.isPlayer()){ 
			this.getDungeon().updateCreatures();
			this.modifyHunger(-5);
			this.addScore(actions.MELEEATTACK);
			getDungeon().addMessage(messages.getString("youHitTheSpace")+target.name+messages.getString("spaceForSpace")+realDamage+messages.getString("hp")+messages.getString("dot"));
			if(target.getHp()<=0){
				this.modifyXp(target.getXp());
				this.getDungeon().addMessage(messages.getString("youKillTheSpace")+target.name+messages.getString("commaGainingSpace")+target.getXp()+messages.getString("spacexpdot"));
			}
		}
		if(target.isPlayer())
			getDungeon().addMessage(messages.getString("theSpace")+this.getName()+messages.getString("spaceHitsYouForSpace")+realDamage+messages.getString("hp")+messages.getString("dot"));		
	}
		
	public void throwAttack(Creature target){
		boolean hit = false;
		if((this.getDextery()+PlayScreen.getTheRNG().nextInt(5)) > (target.getDodge()+PlayScreen.getTheRNG().nextInt(5))){
			hit = true;
		}
		ThrowableWeapon w = (ThrowableWeapon) this.getQuiveredWeapon();
		//ThrowableWeapon weaponToThrow;
		int maxDamage = w.getDamage();
		int realDamage = (int)(this.getDungeon().getRandDouble() * maxDamage)+1;
		System.out.println(messages.getString("theSpace")+this.getName()+" RANGED attacks the "+target.getName()+messages.getString("spaceForSpace")+realDamage+" hp, with a "+w.getName()+messages.getString("dot"));
		if(hit){
			target.modifyHp(-realDamage);
			if(this.isPlayer()){ 
				this.getDungeon().updateCreatures();
				this.modifyHunger(-5);
				this.getDungeon().addMessage(messages.getString("youRangedHitTheSpace")+target.name+messages.getString("spaceForSpace")+realDamage+messages.getString("hp")+messages.getString("dot"));
				if(target.getHp()<=0){
					this.modifyXp(target.getXp());
					this.getDungeon().addMessage(messages.getString("youKillTheSpace")+target.name+messages.getString("commaGainingSpace")+target.getXp()+messages.getString("spacexpdot"));
				}
			}
			if(target.isPlayer())
				this.getDungeon().addMessage(messages.getString("theSpace")+this.getName()+messages.getString("spaceRangedHitsYouForSpace")+realDamage+messages.getString("hp")+messages.getString("dot"));
		}
		else{
			if(this.isPlayer()){
				this.modifyHunger(-5);
				getDungeon().addMessage(messages.getString("theSpace")+target.getName()+messages.getString("spaceDodgesTheSpace")+w.getName()+messages.getString("dot"));
			}
			else
				getDungeon().addMessage(messages.getString("youDodgeTheSpace")+this.getName()+messages.getString("apostropheSSpace")+w.getName()+messages.getString("dot"));
		}
		if(!w.isStackeable() || w.getOtherItems().isEmpty()){
			System.out.println("Removing from inventory last "+w.getName()+messages.getString("dot"));
			this.getInventory().remove(w);
			w.setEquipped(false);
			this.setQuiveredWeapon(null);
		}else{
			System.out.println("Removing from inventory "+w.getName()+" number "+(w.getOtherItems().size()-1));
			w.getOtherItems().remove(w.getOtherItems().size()-1);
		}
		if(this.getInventory().isThereA(w.getName())){
			this.setQuiveredWeapon(this.getInventory().getA(w.getName()));
		}
		ArrayList<Position> pos = target.getPosition().getNeighbors(8);
		Position p = pos.get(this.getDungeon().getRandInt(pos.size()));
		Tile t = this.getDungeon().getLevel(this.getIndexDungeonLevel()).getTile(p);
		if(t.getSymbol()=='.'){
			//w.setPosition(p);
			if(!w.isStackeable() || w.getOtherItems().isEmpty())
				t.putOnTile(w);
			else t.putOnTile(w.getOtherItems().get(w.getOtherItems().size()-1));
		}
	}

	public void rangedAttack(Creature target){
		boolean hit = false;
		if((this.getDextery()+PlayScreen.getTheRNG().nextInt(5)) > (target.getDodge()+PlayScreen.getTheRNG().nextInt(5))){
			hit = true;
		}
		RangedWeapon w = (RangedWeapon) this.getWeapon();
		Item a = this.getInventory().getAMissile(w.getAmmo());
		Item ammoToFire;
		if(hit){
			int maxDamage = w.getDamage();
			int realDamage = (int)(this.getDungeon().getRandDouble() * maxDamage)+1;
			System.out.println(messages.getString("theSpace")+this.getName()+" RANGED attacks the "+target.getName()+messages.getString("spaceForSpace")+realDamage+" hp, with a "+w.getName()+messages.getString("dot"));
			target.modifyHp(-realDamage);
			if(this.isPlayer()){ 
				this.getDungeon().updateCreatures();
				this.modifyHunger(-5);
				this.getDungeon().addMessage(messages.getString("youRangedHitTheSpace")+target.name+messages.getString("spaceForSpace")+realDamage+messages.getString("hp")+messages.getString("dot"));
				if(target.getHp()<=0){
					this.modifyXp(target.getXp());
					this.getDungeon().addMessage(messages.getString("youKillTheSpace")+target.name+messages.getString("commaGainingSpace")+target.getXp()+messages.getString("spacexpdot"));
				}
			}
			if(target.isPlayer())
				this.getDungeon().addMessage(messages.getString("theSpace")+this.getName()+messages.getString("spaceRangedHitsYouForSpace")+realDamage+messages.getString("hp")+messages.getString("dot"));
		}
		else{
			if(this.isPlayer())
				getDungeon().addMessage(messages.getString("theSpace")+target.getName()+messages.getString("spaceDodgesTheSpace")+a.getName()+messages.getString("dot"));
			else
				getDungeon().addMessage(messages.getString("youDodgeTheSpace")+this.getName()+messages.getString("apostropheSSpace")+a.getName()+messages.getString("dot"));
			
		}
		if(!a.isStackeable() || a.getOtherItems().isEmpty()){
			ammoToFire = a;
			this.getInventory().remove(a);
		}
		else{
			ammoToFire = a.getOtherItems().get(a.getOtherItems().size()-1);
			a.getOtherItems().remove(a.getOtherItems().size()-1);
		}
		ArrayList<Position> pos = target.getPosition().getNeighbors(8);
		Position p = pos.get(this.getDungeon().getRandInt(pos.size()));
		Tile t = this.getDungeon().getLevel(this.getIndexDungeonLevel()).getTile(p);
		if(t.getSymbol()=='.'){
			System.out.println("The missile didn't break!");
			ammoToFire.setPosition(p);
			t.putOnTile(ammoToFire);
		}
	}
	
	/**
	 * Update the effects on the creature, and then calls the AI for the action on this turn.
	 */
	public void update(){
		this.updateEffects();
		this.ai.onUpdate();
	}
	
	public void changeDungeonLevel(int n){this.ai.changeDungeonLevel(n);}
	
	public void moveBy(int i, int j){
		Position p = new Position(this.pos.getX()+i, this.pos.getY()+j);
		moveTo(p);
	}

	public boolean canWalkOn(Position position) {
		return this.getDungeon().getLevel(this.getIndexDungeonLevel()).getTile(position).isWalkable();}
	
	/**
	 * Picks the object ON TOP of the list of objects on the tile, and adds it to the inventory.
	 */
	public void pick(){
		Creature cre = this;
		Dungeon dun = cre.getDungeon();
		Inventory inv = this.getInventory();
		Tile t = cre.getTile();
		if(cre.isPlayer()){
			if(t.isTileEmpty())
				dun.addMessage(messages.getString("nothingToPick"));
			else{
				if(inv.isFull()){
					dun.addMessage(messages.getString("inventoryFull"));
				}else{
					Item i = (Item) t.getThingsOnTile().get(t.getThingsOnTile().size()-1);
					if(i.getType() == itemType.THESYWEROFFIDOL ){
						for(Creature c : dun.getCreatures()){
							if(!c.isPlayer()){
								c.setHp(c.getMaxHealth());
								c.setAttack(c.getAttack()+50);
								System.out.println(messages.getString("theSpace")+c.getName()+" has "+c.getAttack()+" attack.");
							}
						}
						PlayScreen.getPlayer().addEffect(new TheSyweroffIdolEffect());
					}
					if(t.getThingsOnTile().size() == 1){
						String n="";
						int number = i.getOtherItems().size();
						if(number>=1) n = messages.getString("spaceX")+(number+1);
						dun.addMessage(messages.getString("youPickASpace")+i.getName()+n+messages.getString("dot"));
						System.out.println("You pick the item: "+i.getItemID());
					}else{
						dun.addMessage(messages.getString("moreThanOneItemHere"));		
						dun.addMessage(messages.getString("youPickASpace")+i.getName()+messages.getString("spaceThatIsOnTopDot"));	
					}
					inv.addToInventory(i);
					t.removeThing(t.getThingsOnTile().size()-1);			
					dun.updateCreatures();		
				}
			}			
		}else{
			Item i = (Item) t.getThingsOnTile().get(t.getThingsOnTile().size()-1);
			if(inv.isFull()){
				return;
			}else{
				inv.addToInventory(i);
				t.removeThing(0);
				System.out.println("The"+cre.getName()+" picks the item: "+i.getItemID());
			}
		}			
	}

	/**
	 * Drop the selected item in the TOP of the pile of objects on the tile.
	 * @param item Item to drop.
	 */
	public void drop(Item item){
		if(this.isPlayer() && item.isEquipped() && item.isEquipped()){
			this.getDungeon().addMessage(messages.getString("theSpace")+item.getName()+messages.getString("spaceIsStickToSpace")+messages.getString("youDot"));
			return;
		}
		Tile t = this.getTile();		
		if(item.isEquipped()) this.unequip(item);
		this.getInventory().remove(item);
		t.putOnTile(item);
		System.out.println("You drop a "+item.getName()+messages.getString("dot"));
		if(!item.getOtherItems().isEmpty()){
			System.out.println("It had other "+item.getOtherItems().size()+" attached.");
		}
		if(this.isPlayer()){ 
			String n=messages.getString("blank");
			int number = item.getOtherItems().size()+1;
			if(number>1) n = messages.getString("x")+number;
			this.getDungeon().addMessage(messages.getString("youDropASpace")+item.getName()+n);
			this.getDungeon().updateCreatures();
		}
	}
	
	public boolean isPlayer(){
		return this.getType() == creatureTypes.PLAYER;
	}
	
	public ArrayList<Position> getFloorPositionsOnView(){
		Position aux;
		Level l = this.getDungeon().getLevel(this.getIndexDungeonLevel());
		Tile t;
		ArrayList<Position> toReturn = new ArrayList<Position>();
		for(int i = 0; i < this.getDungeon().getLevelWidth(); i++){
			for(int j=0; j < this.getDungeon().getLevelHeight(); j++){
				aux = new Position(i, j);
				t = l.getTile(aux);
				if(t.getIsOnView())
					if(l.getFloorPositions().contains(aux))
						toReturn.add(aux);
			}
		}
		return toReturn;
	}
	
	public ArrayList<Tile> getFloorTilesOnView(){
		Level l = this.getDungeon().getLevel(this.getIndexDungeonLevel());
		ArrayList<Position> pos = this.getFloorPositionsOnView();
		ArrayList<Tile> toReturn = new ArrayList<Tile>();
		for(Position p : pos){
			toReturn.add(l.getTile(p));
		}
		return toReturn;
	}
	
	public List<Effect> getEffects(){return this.effects;}
	
	public void setEffects(List<Effect> eff){
		this.effects = eff;
		for(Effect ef : eff)
			ef.setCreature(this);
	}
	
	/**
	 * Add an effect to a creature.
	 * @param newEff The effect to be added.
	 * @return True if the effect started, false otherwise.
	 */
	public boolean addEffect(Effect newEff){
		for(Effect existingEff : this.getEffects()){
			if(existingEff.getClass() == newEff.getClass() && existingEff.getStacktype() == newEff.getStacktype()){
				switch(existingEff.getStacktype()){
				case DURATION:
					if(existingEff.getOriginalAmount() == newEff.getOriginalAmount())
						existingEff.modifyDuration(newEff.getDuration());
					return true;					
				case AMOUNT:
					if(existingEff.getOriginalDuration() == newEff.getOriginalDuration())
						existingEff.setAmount(existingEff.getAmount()+newEff.getAmount());
					return true;					
				case NO:
					break;
				}
			}
		}
		this.getEffects().add(newEff);
		return newEff.start(this);		
	}
	
	public void updateEffects(){
		Iterator<Effect> it = this.getEffects().iterator();
		while(it.hasNext()){
			Effect e = (Effect) it.next();
			if(e.getDuration()<0){
				e.end();
				it.remove();
			}
			else e.update();
		}
	}
	
	public void removeEffect(int id){
		Iterator<Effect> it = this.getEffects().iterator();
		while(it.hasNext()){
			Effect e = (Effect) it.next();
			if(e.getId() == id){
				e.end();
				it.remove();
				return;
			}
		}
	}
	
	public void removeItemEffectsFromPlayer(Item i){
		if(i.getEffects()!=null && !i.getEffects().isEmpty()){				
			List<Integer> effectsIdToRemove = new ArrayList<Integer>();				
			for(Effect efW : i.getEffects())
				for(Effect efP : this.getEffects())
					if(efW.getId() == efP.getId())
						effectsIdToRemove.add(efW.getId());
			for(int id : effectsIdToRemove)
				this.removeEffect(id);					
		}
	}
	
	public Inventory getInventory(){return this.inventory;}
	
	public void setNewInventory(int cap){this.inventory = new Inventory(cap);}
	
	public boolean canSee(Tile t){return ai.canSee(t);}
	
	public int getVisionRadius(){return this.visionRadius;}
	
	public void setVisionRadius(int v){this.visionRadius = v;}

	public int getMaxHealth(){return maxHealth;}

	public void setMaxHealth(int maxHealth){
		this.maxHealth = maxHealth;
		this.maxHunger = maxHealth*10;
	}
	
	public int getHp(){return this.hp;}
	
	public void setHp(int value){this.hp = value;}
	
	public void modifyHp(int value){
		this.hp += value;
		if(this.getHp()>this.getMaxHealth()) this.hp=this.getMaxHealth();
	//	System.out.println("New hp: "+this.getHp());
		if(this.getHp() < 1){
			System.out.println(this.getName()+" DIED on level "+this.getIndexDungeonLevel());
			this.leaveCorpse();
			this.getDungeon().delete(this);
		}
	}
	
	public void modifyMana(int value){
		this.mana+=value;
		if(this.getMana()>this.getMaxMana())
			this.mana = this.maxMana;
	}
	
	public boolean hasEnoughMana(int value){return (this.mana > value);}
	
	public void modifyMaxHP(int amount){this.maxHealth+=amount;}
	
	public void leaveCorpse(){
		if(!Util.getHeadsOrTails())
			return;
		ArrayList<String> corpseDesc = new ArrayList<String>();
		corpseDesc.add(messages.getString("aNastySpace")+getName()+messages.getString("sCorpse")+messages.getString("dot"));
		Food corpse = new Food('%', getColor(), getPosition(), this.getIndexDungeonLevel(),
				getName()+messages.getString("sCorpse"),corpseDesc, getNutrValue(), getEatCorpseMessage(),getCorpseEffects(),false,messages.getString("blank"),foodType.CORPSE);
		Tile t = this.getTile();
		if(t.isTileEmpty()){
			t.putOnTile(corpse);
		}		
	}

	public ArrayList<String> getEatCorpseMessage(){return (ArrayList<String>) this.eatMessage;}
	
	public void setEatCorpseMessage(ArrayList<String> mess){this.eatMessage = mess;}

	public List<Effect> getCorpseEffects(){return this.corpseEffects;}
	
	public void setCorpeseEffects(List<Effect>eff){this.corpseEffects = eff;}
	
	public int getNutrValue(){return this.nutrValue;}
	
	public void setNutrValue(int value){this.nutrValue = value;}

	public int getHunger(){return this.hungerLevel;}
	
	public void setHunger(int h){this.hungerLevel=h;}

	public int getMaxHunger(){return this.maxHunger;}
	
	public void setMaxHunger(int h){this.maxHunger=h;}
	
	public void modifyHunger(int value){		
		int hunger = this.getHunger();
		int newHunger = hunger+value;
		if(newHunger<=this.maxHunger)this.setHunger(newHunger);
		else this.setHunger(maxHunger);
	}
	
	public int getAttack(){return attack;}

	public void setAttack(int attack){this.attack = attack;}

	public int getDefense(){return defense;}

	public void setDefense(int defense){this.defense = defense;}
	
	public String getName(){return this.name;}
	
	public void setName(String n){this.name = n;}
	
	public Tile getTile(){return this.getDungeon().getLevel(this.getIndexDungeonLevel()).getTile(getPosition());}
	
	@Override
	public char getSymbol(){return this.symbol;}
	
	@Override
	public void setSymbol(char s){this.symbol = s;}

	@Override
	public SColor getColor(){return this.color;}

	@Override
	public void setColor(SColor c){this.color = c;}

	@Override
	public Position getPosition(){return pos;}

	@Override
	public void setPosition(Position pos){
		this.pos = pos;
	}

	@Override
	public int getIndexDungeonLevel() {return this.dungeonLevel;}

	@Override
	public void setIndexDungeonLevel(int l) {this.dungeonLevel = l;}

	@Override
	public Dungeon getDungeon(){return PlayScreen.getDungeon();}
	
	@Override
	public List<String> getDescription() {return this.description;}

	public void setDescription(List<String> desc){this.description = desc;}
		
	/**
	 * Equips an item, if possible. It will unequip an object AND equip the item given, if necesary, spending 2 turns in the action.
	 * @param item The item to equip.
	 */
	public void equip(Item item) {
		switch(item.getType()){
			case MELEEWEAPON:
			case RANGEDWEAPON:
			case MAGICWEAPON:
				if(this.getWeapon()!=null && this.getWeapon().isCursed() && this.isPlayer()){
					this.getDungeon().addMessage(messages.getString("yourSpace")+this.getWeapon().getName()+messages.getString("spaceIsStickToSpace")+messages.getString("yourHandDot"));
					return;
				}
				Weapon w = (Weapon) item;
				if(w.getNumHands()==2){
					if(this.isPlayer()) this.getDungeon().addMessage(messages.getString("youNeedTwoHandsSpace")+w.getName()+messages.getString("dot"));
					this.unequip(this.getShield());
				}
				this.unequip(this.getWeapon());
				this.setWeapon(w);
				this.getDungeon().updateCreatures();
				if(this.isPlayer()){
					this.getDungeon().addMessage(messages.getString("youEquipASpace")+w.getName()+messages.getString("dot"));
					w.setCurseKnown(true);
					this.getDungeon().addMessage(messages.getString("itsaSpace")+w.getName());	
				}
				for(Effect e : w.getEffects())
					this.addEffect(e);
				return;
			
			case CHEST:
				if(this.getChestArmor()!=null && this.getChestArmor().isCursed() && this.isPlayer()){
					this.getDungeon().addMessage(messages.getString("yourSpace")+this.getChestArmor().getName()+messages.getString("spaceIsStickToSpace")+messages.getString("youDot"));
					return;
				}
				Armor a = (Armor) item;
				this.unequip(this.getChestArmor());
				this.setChestArmor(a);
				this.getDungeon().updateCreatures();
				if(this.isPlayer()){
					this.getDungeon().addMessage(messages.getString("youEquipASpace")+a.getName()+messages.getString("dot"));
					if(a.isCursed()){
						a.setCurseKnown(true);
						this.getDungeon().addMessage(messages.getString("itsaSpace")+a.getName());
					}else{
						a.setCurseKnown(true);
						this.getDungeon().addMessage(messages.getString("itsaSpace")+a.getName());						
					}
				}
				return;
			
			case HELMET:
				if(this.getHelmet()!=null && this.getHelmet().isCursed() && this.isPlayer()){
					this.getDungeon().addMessage(messages.getString("yourSpace")+this.getHelmet().getName()+messages.getString("spaceIsStickToSpace")+messages.getString("yourHeadDot"));
					return;
				}
				a = (Armor) item;
				this.unequip(this.getHelmet());
				this.setHelmet(a);
				this.getDungeon().updateCreatures();
				if(this.isPlayer()){
					this.getDungeon().addMessage(messages.getString("youEquipASpace")+a.getName()+messages.getString("dot"));
					if(a.isCursed()){
						a.setCurseKnown(true);
						this.getDungeon().addMessage(messages.getString("itsaSpace")+a.getName());
					}else{
						a.setCurseKnown(true);
						this.getDungeon().addMessage(messages.getString("itsaSpace")+a.getName());						
					}
				}
				return;
			
			case SHIELD:
				if(this.getWeapon()!=null && this.getWeapon().isCursed() && this.isPlayer()){
					this.getDungeon().addMessage(messages.getString("yourSpace")+this.getWeapon().getName()+messages.getString("spaceIsStickToSpace")+messages.getString("yourHandDot"));
					return;
				}
				if(this.getShield()!=null && this.getShield().isCursed() && this.isPlayer()){
					this.getDungeon().addMessage(messages.getString("yourSpace")+this.getShield().getName()+messages.getString("spaceIsStickToSpace")+messages.getString("yourHandDot"));
					return;
				}
				if(this.getWeapon()!=null && this.getWeapon().getNumHands()==2){
					if(this.isPlayer())	this.getDungeon().addMessage(messages.getString("youNeedTwoHandsSpace")+this.getWeapon().getName()+messages.getString("commaYouCantUseAShieldDot"));
					return;
				}
				a = (Armor) item;
				this.unequip(this.getShield());
				this.setShield(a);
				this.getDungeon().updateCreatures();
				if(this.isPlayer()){
					this.getDungeon().addMessage(messages.getString("youEquipASpace")+a.getName()+messages.getString("dot"));
					if(a.isCursed()){
						a.setCurseKnown(true);
						this.getDungeon().addMessage(messages.getString("itsaSpace")+a.getName());
					}else{
						a.setCurseKnown(true);
						this.getDungeon().addMessage(messages.getString("itsaSpace")+a.getName());						
					}
				}
				return;
			
			case BOOTS:
				if(this.getFeet()!=null && this.getFeet().isCursed() && this.isPlayer()){
					this.getDungeon().addMessage(messages.getString("yourSpace")+this.getFeet().getName()+messages.getString("spaceAreStickToYourFeetDot"));
					return;
				}
				a = (Armor) item;
				this.unequip(this.getFeet());
				this.setFeet(a);
				this.getDungeon().updateCreatures();
				if(this.isPlayer()){
					this.getDungeon().addMessage(messages.getString("youEquipASpace")+a.getName()+messages.getString("dot"));
					if(a.isCursed()){
						a.setCurseKnown(true);
						this.getDungeon().addMessage(messages.getString("itsaSpace")+a.getName());
					}else{
						a.setCurseKnown(true);
						this.getDungeon().addMessage(messages.getString("itsaSpace")+a.getName());						
					}
				}
				return;
			
			case THROWABLEWEAPON:
			case MISSILE:
				this.unequip(this.getQuiveredWeapon());
				this.setQuiveredWeapon(item);
				this.getDungeon().updateCreatures();
				if(this.isPlayer()) this.getDungeon().addMessage(messages.getString("youQuiverSpace")+item.getName()+messages.getString("dot"));
				return;
		
			default:
				System.out.println("ERROR: You are trying to equip an invalid item.");
				break;				
		}
	}
	
	/**
	 * Unequips an item, if possible.
	 * @param item The equipped item to remove.
	 */
	public void unequip(Item item){
		if(item == null) return;
		if(item.isCursed()){
			this.getDungeon().addMessage(messages.getString("youCantTakeOffYourSpace")+ item.getName());
			return;
		}
		removeItemEffectsFromPlayer(item);
		item.setEquipped(false);
		Item.itemType type = item.getType();
		if(this.isPlayer())
			this.getDungeon().updateCreatures();
		if(type == Item.itemType.MELEEWEAPON || type == Item.itemType.RANGEDWEAPON || type == Item.itemType.MAGICWEAPON){
			Weapon w = (Weapon) item;
			this.setWeapon(null);
			if(this.isPlayer()) this.getDungeon().addMessage(messages.getString("youUnequipASpace")+w.getName()+messages.getString("dot"));
			return;
		}
		if(type == Item.itemType.CHEST){
			Armor a = (Armor) item;
			this.setChestArmor(null);
			if(this.isPlayer()) this.getDungeon().addMessage(messages.getString("youUnequipASpace")+a.getName()+messages.getString("dot"));
			return;
		}
		if(type == Item.itemType.HELMET){
			Armor a = (Armor) item;
			this.setHelmet(null);
			if(this.isPlayer()) this.getDungeon().addMessage(messages.getString("youUnequipASpace")+a.getName()+messages.getString("dot"));
			return;
		}
		if(type == Item.itemType.SHIELD){
			Armor a = (Armor) item;
			this.setShield(null);
			if(this.isPlayer()) this.getDungeon().addMessage(messages.getString("youUnequipASpace")+a.getName()+messages.getString("dot"));
			return;
		}
		if(type == Item.itemType.BOOTS){
			Armor a = (Armor) item;
			this.setFeet(null);
			if(this.isPlayer()) this.getDungeon().addMessage(messages.getString("youUnequipSpace")+a.getName()+messages.getString("dot"));
			return;
		}
		if(type == Item.itemType.THROWABLEWEAPON
				|| type == Item.itemType.MISSILE){
			this.setQuiveredWeapon(null);
			if(this.isPlayer()) this.getDungeon().addMessage(messages.getString("youRemoveSpace")+item.getName()+messages.getString("fromYourQuiverDot"));
			return;
		}
	}

	public void quaff(Potion pot){
		Potion potToDrink = pot;
		if(!pot.getOtherItems().isEmpty())
			potToDrink = (Potion) pot.getOtherItems().get(pot.getOtherItems().size()-1);
		if(this.isPlayer()){
			this.getDungeon().addMessage(messages.getString("youQuaffASpace")+potToDrink.getName()+messages.getString("dot"));
			if(!Potion.getIdentified().contains(potToDrink.getPotiontype())){
				this.getDungeon().addMessage(messages.getString("itsaSpace")+potToDrink.getHiddenDesc()+messages.getString("dot"));
				ArrayList<String> d = new ArrayList<String>(); 
				d.add(messages.getString("itsaSpace")+potToDrink.getHiddenDesc()+messages.getString("dot"));
				potToDrink.setDesc(d);
				Potion.addIdentified(potToDrink.getPotiontype());	
			}
		}
		for(Effect e : potToDrink.getEffects())
			this.addEffect(e);
		this.eliminateItem(pot);
		this.getDungeon().updateCreatures();
	}

	public void read(Scroll scr){
		if(this.isBlind()){
			return;
		}		
		boolean identified = false;
		for(Effect e : scr.getEffects()){
			if(this.addEffect(e)==true)
				identified = true;
		}
		this.eliminateItem(scr);
		if(this.isPlayer()){
			this.getDungeon().addMessage(messages.getString("youReadASpace")+scr.getName()+messages.getString("dot"));
			if(!Scroll.getIdentified().contains(scr.getScrolltype()) && identified){
				this.getDungeon().addMessage(messages.getString("itsaSpace")+scr.getHiddenDesc()+messages.getString("dot"));
				ArrayList<String> d = new ArrayList<String>(); 
				d.add(messages.getString("itsaSpace")+scr.getHiddenDesc()+messages.getString("dot"));
				scr.setDesc(d);
				Scroll.addIdentified(scr.getScrolltype());	
			}
		}
		this.getDungeon().updateCreatures();
	}

	public void eat(Food item) {
		if(this.hungerLevel < 0.9*maxHunger){
			if(this.isPlayer()){
				Iterator<String> it = item.getMessage().iterator();
				while(it.hasNext()){
					this.getDungeon().addMessage(it.next());
				}
				if(item.getFoodType()==PlayScreen.getPlayer().getFavFood())
					this.getDungeon().addMessage(item.getHiddenDesc());
			}
			Food foodToEat = item;
			/*if(item.getOtherItems().isEmpty())	this.eliminateItem(item);
			else item.getOtherItems().remove(item.getOtherItems().size()-1);*/
			for(Effect e : foodToEat.getEffects())
				this.addEffect(e);
			this.eliminateItem(item);
			this.modifyHunger(item.getNutritionalValue());
			this.getDungeon().updateCreatures();
		}
		else if(this.isPlayer())this.getDungeon().addMessage(messages.getString("youAreTooFull"));
	}

	/**
	 * Eliminates an item if is not stackeable or the stack is empty. Eliminates one item from the stack otherwise.
	 * @param item The item to eliminate.
	 */
	public void eliminateItem(Item item){
		if(item.getOtherItems().isEmpty()){
			if(item.isEquipped()) this.unequip(item);
			this.getInventory().remove(item);
		}
		else item.getOtherItems().remove(item.getOtherItems().size()-1);
	}

	/**
	 * Consumes a turn doing nothing. All effects still apply.
	 */
	public void waitATurn(){
		this.getDungeon().addMessage(messages.getString("youWaitForAMoment"));
		this.getDungeon().updateCreatures();
	}

	public Weapon getWeapon(){return weapon;}

	public void setWeapon(Weapon weapon){
		this.weapon = weapon;
		if(weapon != null) this.weapon.setEquipped(true);
	}

	public Armor getChestArmor(){return chest;}

	public void setChestArmor(Armor chest){
		this.chest = chest;
		if(chest!=null) this.chest.setEquipped(true);
	}

	public Armor getHelmet(){return head;}

	public void setHelmet(Armor helmet){
		this.head = helmet;
		if(head!=null) this.head.setEquipped(true);
	}

	public Armor getShield(){return shield;}

	public void setShield(Armor shield){
		this.shield = shield;
		if(shield!=null) this.shield.setEquipped(true);
	}

	public Armor getFeet(){return feet;}

	public void setFeet(Armor feet){
		this.feet = feet;
		if(feet!=null) this.feet.setEquipped(true);
	}

	public Item getQuiveredWeapon(){return quiver;}

	public void setQuiveredWeapon(Item missiles){
		this.quiver = missiles;
		if(missiles!=null)this.quiver.setEquipped(true);}

	public ArrayList<Drawable> getEverythingOnView() {
		ArrayList<Tile> tiles = this.getFloorTilesOnView();
		ArrayList<Drawable> toReturn = new ArrayList<Drawable>();
		for(Tile t : tiles)
			if(t.hasCreature() && t != this.getTile())
				toReturn.add(t.getCreature());
		for(Tile t : tiles)
			if(!t.isTileEmpty() && t != this.getTile()){
				Drawable thing = (Drawable) t.getThingsOnTile().get(t.getThingsOnTile().size()-1);
				thing.setPosition(t.getPosition());
				toReturn.add(thing);
			}
		if(!this.getTile().isTileEmpty()){
			Drawable thing = (Drawable) getTile().getThingsOnTile().get(getTile().getThingsOnTile().size()-1);
			thing.setPosition(this.getTile().getPosition());
			toReturn.add(thing);
		}
		System.out.println();
		Collections.sort(toReturn, Drawable.DISTANCE_TO_PLAYER);
		return toReturn;
	}

	public ArrayList<Creature> getCreaturesOnView(){
		ArrayList<Tile> tiles = this.getFloorTilesOnView();
		ArrayList<Creature> toReturn = new ArrayList<Creature>();
		for(Tile t : tiles){
			if(t.hasCreature() && t != this.getTile()){
				toReturn.add(t.getCreature());
			}
		}
		Collections.sort(toReturn, Drawable.DISTANCE_TO_PLAYER);
		return toReturn;
	}

	public ArrayList<Drawable> getObjectsOnView(){
		ArrayList<Tile> tiles = this.getFloorTilesOnView();
		ArrayList<Drawable> toReturn = new ArrayList<Drawable>();
		for(Tile t : tiles){
			for(Drawable thing : t.getThingsOnTile())
				thing.setPosition(t.getPosition());
			toReturn.addAll(t.getThingsOnTile());
		}
		Collections.sort(toReturn, Drawable.DISTANCE_TO_PLAYER);
		return toReturn;
	}

	public ArrayList<Creature> getCreaturesOnRange(int range) {
		ArrayList<Tile> tiles = this.getFloorTilesOnView();
		ArrayList<Creature> toReturn = new ArrayList<Creature>();
		for(Tile t : tiles){
			if(t.hasCreature() && t != this.getTile() && new Line(this.getPosition(), t.getPosition()).getPoints().size()<=range+1){
				toReturn.add(t.getCreature());
			}
		}
		Collections.sort(toReturn, Drawable.DISTANCE_TO_PLAYER);
		return toReturn;
	}

	public void setCorpseEffect(ArrayList<Effect> e){this.corpseEffects = e;}
	
	public void addCorpseEffect(Effect e){this.corpseEffects.add(e);}

	public int getXp(){return xp;}
	
	public void setXp(int value){this.xp = value;}

	public void modifyXp(int amount){
		this.xp += amount;
		if(xp > (int)(Math.pow(this.getLevel(), 1.5) * 20)){
			getDungeon().addMessage(messages.getString("levelUp"));
			this.level++;
			this.modifyMaxHP(15);
		}
	}
	
	/**
	 * Alerts the player if a creature in hearing range makes a noise, gives a string indicating which noise was and where it come from.
	 */
	public void makeNoise(){
		if(this.isPlayer()){
			return;
		}
		Creature player = PlayScreen.getPlayer();
		if(this.getIndexDungeonLevel()!=player.getIndexDungeonLevel())return;
		//System.out.println(this.getName()+" make noise at "+ this.getPosition().distance(player.getPosition())+" tiles.");
		if(player.getHearingRadius() >= this.getPosition().distance(player.getPosition()))
			getDungeon().addMessage(messages.getString("youHearSomethingSpace")+this.noise+messages.getString("spaceToTheSpace")+player.getPosition().getRelativePosition(this.getPosition()));
	}

	public int getLevel() {return this.level;}
	
	public void setLevel(int level){this.level = level;}
	
	public void setNoise(String noise){this.noise = noise;}

	public int getHearingRadius(){return this.hearingRadius;}
	
	public void setHearingRadius(int n){this.hearingRadius=n;}

	public boolean isHunting(){return hunting;}

	public void setHunting(boolean hunting){this.hunting = hunting;}

	public List<Creature> getEnemiesSawLastTurn(){	return enemiesSawLastTurn;}

	public void setEnemiesSawLastTurn(ArrayList<Creature> enemiesSawLastTurn){this.enemiesSawLastTurn = enemiesSawLastTurn;}

	public foodType getFavFood(){return favFood;}

	public void setFavFood(){
		this.favFood = factories.FoodFactory.getRandomFoodType();
		while(this.favFood == foodType.RANDOM || this.favFood == foodType.CORPSE)
			this.favFood = factories.FoodFactory.getRandomFoodType();
	}

	public int getHearingRadiusDef(){return hearingRadiusDef;}

	public void setHearingRadiusDef(int hearingRadiusDef){this.hearingRadiusDef = hearingRadiusDef;}

	public int getVisionRadiusDef(){return visionRadiusDef;}

	public void setVisionRadiusDef(int visionRadiusDef){this.visionRadiusDef = visionRadiusDef;}

	public boolean isBlind(){
		for(Effect e : this.getEffects())
			if(e instanceof BlindEffect)
				return true;
		return false;
	}

	public boolean isPoisoned(){
		for(Effect e : this.getEffects())
			if(e instanceof PoisonEffect)
				return true;
		return false;
	}

	public boolean isTraveling(){return isTraveling;}

	public void setTraveling(boolean isTraveling){this.isTraveling = isTraveling;}

	public Position getDestiny(){return destiny;}

	public void setDestiny(Position destiny){this.destiny = destiny;}

	public void travelOneStep() {
		if(this.isPlayer()){
			Creature player = PlayScreen.getPlayer();
			ArrayList<Position> steps = null;
			if(player.getDestiny() != PlayScreen.getDungeon().getLevel(player.getIndexDungeonLevel()).getStairsDown() &&
					player.getDestiny() != PlayScreen.getDungeon().getLevel(player.getIndexDungeonLevel()).getStairsUp()){
				steps = getDungeon().getLevel(player.getIndexDungeonLevel()).getWalkingPath(player.getPosition(), player.getDestiny(), 8);
			}else{
				steps = getDungeon().getLevel(player.getIndexDungeonLevel()).getWalkingKnownPath(player.getPosition(), player.getDestiny(), 8);
			}
			Collections.sort(steps, Position.MIN_DISTANCE_TO_PLAYER);
			System.out.println("Player is in: "+player.getPosition().toString());
			System.out.println("Player goes to: "+player.getDestiny().toString());
			System.out.println("Player next step: "+steps.get(0).toString());
			this.moveTo(steps.get(0));
		}
	}

	public boolean hasWon() {
		if(this.isPlayer())
			return this.inventory.hasTheSyweroffIdol();		
		return false;
	}

	public void hitNearestCreature() {
		ArrayList<Creature> creaturesOnView = this.getCreaturesOnView();
		Position p = creaturesOnView.get(0).getPosition();
		if(this.getPosition().getNeighbors(8).contains(p))
			this.moveTo(p);
		else{
			ArrayList<Position> path = getDungeon().getLevel(this.getIndexDungeonLevel()).getWalkingPath(this.getPosition(), p, 8);
			//path.remove(this.getPosition());
			Collections.sort(path, Position.MIN_DISTANCE_TO_PLAYER);
			this.moveTo(path.get(0));
		}
	}

	public void setClassAtributes(creatureClasses clas) {
		Factory facto = PlayScreen.getFactory();
		switch(clas){
			case WARRIOR: //WARRIOR
				this.setClas(creatureClasses.WARRIOR);
				this.setMaxHealth(120);
				this.setHp(120);
				this.setIntelligence(5);
				this.setDodge(2);
				this.setWeapon(facto.getWeapon(this, weaponType.SWORD,itemMaterial.RANDOM, 0));
				break;
			case ARCHER: //ARCHER
				this.setClas(creatureClasses.ARCHER);
				this.setMaxHealth(100);
				this.setHp(100);
				this.setIntelligence(7);
				this.setVisionRadius(12);
				this.setHearingRadius(12);
				this.setWeapon(facto.getRangedWeapon(this, 0, 0));
				facto.getAmmo(this, ammoType.ARROW, itemMaterial.RANDOM, -1, 0);
				break;
			case WHITEMAGE:
				this.setClas(creatureClasses.WHITEMAGE);
				this.setMaxHealth(80);
				this.setHp(80);
				this.setIntelligence(15);
				this.setMaxMana(200);
				this.setMana(200);
				this.setManaRegenerationRate(0.5);
				ArrayList<Effect> effects = new ArrayList<Effect>();
				effects.add(new InstaHealEffect(20));
				this.learnSpell(new Spell(messages.getString("healingSpell"),50,5,effects));
				effects = new ArrayList<Effect>();
				effects.add(new InstaDamageEffect(20));
				this.learnSpell(new Spell(messages.getString("magicDart"),15,5,effects));
				this.setWeapon(facto.getWeapon(this, weaponType.STAFF, itemMaterial.RANDOM, 0));
				break;
		default:
			break;
		}		
	}

	public creatureClasses getClas(){return clas;}

	public void setClas(creatureClasses clas){this.clas = clas;}

	public int getDextery(){return dextery;}

	public void setDextery(int dextery){this.dextery = dextery;}

	public int getDodge(){return dodge;}

	public void setDodge(int dodge){this.dodge = dodge;}

	public double getHpRegenerationRate(){return hpRegenerationRate;}

	public void setHPRegenerationRate(double regenerationRate){this.hpRegenerationRate = regenerationRate;}

	public double getLuck(){return this.luck;}
	
	public void setLuck(double luck){this.luck = luck;}
	
	public int getId(){return id;}

	public void setId(int id){this.id = id;}

	public int getScore(){return score;}

	public void setScore(int points){this.score = points;}
	
	public void addScore(int points){this.score+=points;}
	
	/**
	 * Manages the score of the player.
	 */
	public void die(){
	    Creature player = this;
		player.addScore(player.getHunger());
		player.addScore(PlayScreen.getDungeon().getNumTurns());
		player.addScore(player.getInventory().getAmountOfGold() * 5);
		player.addScore(player.getXp()*3);
		System.out.println("Score at time of death: "+player.getScore());
		util.ScoreFileManager.addScore();		
	}

	public double getManaRegenerationRate(){return manaRegenerationRate;}

	public void setManaRegenerationRate(double manaRegenerationRate){this.manaRegenerationRate = manaRegenerationRate;}

	public int getMaxMana(){return maxMana;}

	public void setMaxMana(int maxMana){this.maxMana = maxMana;}

	public int getMana(){return mana;}

	public void setMana(int mana){this.mana = mana;}

	public ArrayList<Spell> getSpellsKnown(){return (ArrayList<Spell>) spellsKnown;}

	public void setSpellsKnown(List<Spell> spellsKnown){this.spellsKnown = spellsKnown;}
	
	public void learnSpell(Spell s){this.spellsKnown.add(s);}

	/**
	 * UNIMPLEMENTED. Adds different scores in function of the player's class and the action.
	 * @param action
	 */
	public void addScore(actions action){
		Creature player = this;
		switch(action){
		case CAST:
			switch(player.getClas()){
			case ARCHER:
				break;
			case WARRIOR:
				break;
			default:
				break;
			
			}
			break;
		case EQUIP:
			switch(player.getClas()){
			case ARCHER:
				break;
			case WARRIOR:
				break;
			default:
				break;
			
			}
			break;
		case MELEEATTACK:
			switch(player.getClas()){
			case ARCHER:
				break;
			case WARRIOR:
				break;
			default:
				break;
			
			}
			break;
		case QUAFF:
			switch(player.getClas()){
			case ARCHER:
				break;
			case WARRIOR:
				break;
			default:
				break;
			
			}
			break;
		case RANGEDATTACK:
			switch(player.getClas()){
			case ARCHER:
				break;
			case WARRIOR:
				break;
			default:
				break;
			
			}
			break;
		case READ:
			switch(player.getClas()){
			case ARCHER:
				break;
			case WARRIOR:
				break;
			default:
				break;
			
			}
			break;
		case THROWATTACK:
			switch(player.getClas()){
			case ARCHER:
				break;
			case WARRIOR:
				break;
			default:
				break;
			
			}
			break;
		case UNEQUIP:
			switch(player.getClas()){
			case ARCHER:
				break;
			case WARRIOR:
				break;
			default:
				break;
			
			}
			break;
		case KILL:
			break;
		case MISSMELEEATTACK:
			break;
		case MISSRANGEDATTACK:
			break;
		case MISSTHROWATTACK:
			break;
		default:
			break;
		
		}
	}

	public int getIntelligence(){return intelligence;}

	public void setIntelligence(int newIntelligence){this.intelligence = newIntelligence;}
	
	/**Used for descriptions, gives the farthest position on sight until there is a door
	 * @return The farthest position until there is a door in all directions
	 */
	public List<Position> getFarthestsPositionsOnSightStoppingAtDoors(){
		Position p = this.getPosition();
		Tile aux = this.getTile();
		Tile aux2;
		Level l = this.getDungeon().getLevel(this.getIndexDungeonLevel());
		int i,j;
		List<Position>toReturn = new ArrayList<Position>();
		//NORTH
		i = 0;
		j = 0;
		aux = this.getTile();	
		while(this.canSee(aux) && !aux.isDoor() && aux.isWalkable() && !aux.isDoor()){
			j--;
			aux = l.getTile(new Position(p.getX()+i,p.getY()+j));
		}
		aux2 = l.getTile(new Position(aux.getPosition().getX(),aux.getPosition().getY()+1));
		if(aux2.isWalkable())
			toReturn.add(aux2.getPosition());
		
		//NORHEAST
		i = 0;
		j = 0;
		aux = this.getTile();	
		while(this.canSee(aux) && !aux.isDoor() && aux.isWalkable() && !aux.isDoor()){
			i++;
			j--;
			aux = l.getTile(new Position(p.getX()+i,p.getY()+j));
		}
		aux2 = l.getTile(new Position(aux.getPosition().getX()-1,aux.getPosition().getY()+1));
		if(aux2.isWalkable())
			toReturn.add(aux2.getPosition());
		
		//EAST
		i = 0;
		j = 0;
		aux = this.getTile();	
		while(this.canSee(aux) && !aux.isDoor() && aux.isWalkable() && !aux.isDoor()){
			i++;
			aux = l.getTile(new Position(p.getX()+i,p.getY()));
		}
		aux2 = l.getTile(new Position(aux.getPosition().getX()-1,aux.getPosition().getY()));
		if(aux2.isWalkable())
			toReturn.add(aux2.getPosition());
		
		//SOUTHEAST
		i = 0;
		j = 0;
		aux = this.getTile();		
		while(this.canSee(aux) && !aux.isDoor() && aux.isWalkable() && !aux.isDoor()){
			i++;
			j++;
			aux = l.getTile(new Position(p.getX()+i,p.getY()+j));
		}
		aux2 = l.getTile(new Position(aux.getPosition().getX()-1,aux.getPosition().getY()-1));
		if(aux2.isWalkable())
			toReturn.add(aux2.getPosition());
		
		//SOUTH
		i = 0;
		j = 0;
		aux = this.getTile();	
		while(this.canSee(aux) && !aux.isDoor() && aux.isWalkable() && !aux.isDoor()){
			j++;
			aux = l.getTile(new Position(p.getX()+i,p.getY()+j));
		}
		
		aux2 = l.getTile(new Position(aux.getPosition().getX(),aux.getPosition().getY()-1));
		if(aux2.isWalkable())
			toReturn.add(aux2.getPosition());
		
		//SOUTHWEST
		i = 0;
		j = 0;
		aux = this.getTile();	
		while(this.canSee(aux) && !aux.isDoor() && aux.isWalkable() && !aux.isDoor()){
			i--;
			j++;
			aux = l.getTile(new Position(p.getX()+i,p.getY()+j));
		}
		aux2 = l.getTile(new Position(aux.getPosition().getX()+1,aux.getPosition().getY()-1));
		if(aux2.isWalkable())
			toReturn.add(aux2.getPosition());
		
		//WEST
		i = 0;
		j = 0;
		aux = this.getTile();		
		while(this.canSee(aux) && !aux.isDoor() && aux.isWalkable() && !aux.isDoor()){
			i--;
			aux = l.getTile(new Position(p.getX()+i,p.getY()+j));
		}
		aux2 = l.getTile(new Position(aux.getPosition().getX()+1,aux.getPosition().getY()));
		if(aux2.isWalkable())
			toReturn.add(aux2.getPosition());
		
		//NORTHWEST
		i = 0;
		j = 0;
		aux = this.getTile();	
		while(this.canSee(aux) && !aux.isDoor() && aux.isWalkable() && !aux.isDoor()){
			i--;
			j--;
			aux = l.getTile(new Position(aux.getPosition().getX()+i,aux.getPosition().getY()+j));
		}
		aux2 = l.getTile(new Position(aux.getPosition().getX()+1,aux.getPosition().getY()+1));
		if(aux2.isWalkable())
			toReturn.add(aux2.getPosition());
		
		Collections.sort(toReturn,Position.MAX_DISTANCE_TO_PLAYER);
		return toReturn;
	}

	public ArrayList<HashMap<Position, Character>> getMemory() {
		return memory;
	}

	public void setMemory(ArrayList<HashMap<Position, Character>> memory) {
		this.memory = memory;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public boolean isAnyoneArround() {
		Position p = this.getPosition();
		for(Position pos : p.getNeighbors(8))
			if(getDungeon().getLevel(this.getIndexDungeonLevel()).getTile(pos).hasCreature())
				return true;
		return false;
	}

	public creatureTypes getType() {
		return type;
	}

	public void setType(creatureTypes type) {
		this.type = type;
	}
}
