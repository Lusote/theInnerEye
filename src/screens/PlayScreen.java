package screens;

import items.Item;
import items.RangedWeapon;
import items.Item.itemMaterial;
import items.Weapon.weaponType;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.text.BadLocationException;

import rlmain.Creature;
import rlmain.Creature.creatureClasses;
import rlmain.Drawable;
import rlmain.RoguelikeMain;
import squidpony.squidcolor.SColor;
import squidpony.squidgrid.gui.SGPane;
import util.SaverLoader;
import util.Util;
import dungeongen.Dungeon;
import dungeongen.Level;
import dungeongen.Position;
import dungeongen.Tile;
import factories.Factory;
import factories.FoodFactory.foodType;
import factories.PotionFactory.potionTypes;
import factories.ScrollFactory.ScrollTypes;

public class PlayScreen implements Screen {
	
	private static int worldWidth=80;
	private static int worldHeight=24;
	private static int numberOfLevels = 6;
	private static creatureClasses playerClass;
	private static int previousPlayerLevel = 1;
	private static String previousHunger = blank;
	private static Dungeon uniqueDun;
	private static Creature uniquePlayer;
	private static Factory uniqueFactory;
	private static Screen subscreen;
	private static Random theRNG;
	private static String playerName; 
	//private static Robot rob;
	
	public PlayScreen(Long seed, creatureClasses PlayerClass, String name){
		if(PlayerClass == null){
			loadGame(name);
		}
		else{
			PlayScreen.newGame(seed);
			playerName = name;
			playerClass = PlayerClass;
			if(uniqueDun.getLevel(0).getNumRooms()>0){
				uniqueFactory = getFactory();
				createEveryThing(uniqueFactory);
				uniquePlayer = getPlayer();
				getPlayer().setEnemiesSawLastTurn(getPlayer().getCreaturesOnView());
			}
			System.out.println("Created dungeon with "+getDungeon().getNumOfLevels()+" levels.");
			for(int i=0;i<getDungeon().getNumOfLevels();i++)
				System.out.println("Level "+i+" has "+getDungeon().getLevel(i).getRooms().size()+" rooms.");
		}
	}

	public static void newGame(Long seed) {
		resetGame();
		if(seed != 0)
			theRNG = new Random(seed);
		else 
			theRNG = new Random();
		uniqueDun 		= null;
		uniqueDun = getDungeon();
		uniquePlayer	= null;
		uniqueFactory	= getFactory();
		subscreen		= null;
	}

	public static void resetGame(){
		theRNG 			= null;
		uniqueDun 		= null;
		Dungeon.resetDungeon();
		uniquePlayer	= null;
		uniqueFactory	= null;
		Factory.resetFactory();
		subscreen		= null;
	}
	
	public static void saveGame(){
		SaverLoader game = new SaverLoader();
		game.saveGame();
		subscreen = new StartScreen();
	}
	
	public static void loadGame(String name){
		SaverLoader game = SaverLoader.loadGame(name);
		theRNG = game.getTheRNG();
		uniqueDun = game.getDun();
		uniqueDun.setCreatures((ArrayList<Creature>)game.getCreatures());
		uniquePlayer = game.getPlayer();
		uniqueDun.getCreatures().add(uniquePlayer);
		uniqueFactory = game.getFactory();
		SaverLoader.deleteFile(name);
	}
	
	public static Dungeon getDungeon(){
		if(uniqueDun != null) return uniqueDun;
		else uniqueDun = Dungeon.getDungeon(numberOfLevels,2, worldWidth, worldHeight); //Normal Dungeon. For playing.
		//else uniqueDun = Dungeon.getDungeon(theRNG); //Square dungeon. For testing.
		return uniqueDun;		
	}
	
	public static Factory getFactory(){
		if(uniqueFactory != null) return uniqueFactory;
		else uniqueFactory = Factory.getFactory();
		return uniqueFactory;
	}

	public static Creature getPlayer(){
		if(uniquePlayer != null) return uniquePlayer;
		else uniquePlayer = getFactory().newPlayer(playerClass, playerName);
		return uniquePlayer;
	}
	
	public static Random getTheRNG(){
		return theRNG;
	}
	
	public static int getNumberOfLevels(){
		return numberOfLevels;
	}

	private void createEveryThing(Factory factory){		
		factory.makeALLTheCreatures();
		System.out.println("Creaturesmade: "+Creature.creaturesMade);
		factory.makeALLTheFood();
		factory.makeALLTheItems();	
		
		
		
		factory.getFood(getPlayer(), foodType.RANDOM, 0);
		factory.getFood(getPlayer(), foodType.RANDOM, 0);
		factory.getFood(getPlayer(), foodType.RANDOM, 0);
		factory.getFood(getPlayer(), foodType.RANDOM, 0);
		factory.getFood(getPlayer(), foodType.RANDOM, 0);
		factory.getFood(getPlayer(), foodType.RANDOM, 0);
		factory.getFood(getPlayer(), foodType.RANDOM, 0);
		factory.getWeapon(null, weaponType.SWORD, itemMaterial.IRON, 0);
		factory.getWeapon(null, weaponType.SWORD, itemMaterial.IRON, 0);
		factory.getWeapon(null, weaponType.SWORD, itemMaterial.IRON, 0);
		factory.getWeapon(null, weaponType.SWORD, itemMaterial.IRON, 0);
		factory.getWeapon(null, weaponType.SWORD, itemMaterial.IRON, 0);
		factory.getWeapon(null, weaponType.SWORD, itemMaterial.IRON, 0);
		factory.getWeapon(null, weaponType.SWORD, itemMaterial.IRON, 0);
		factory.getWeapon(null, weaponType.SWORD, itemMaterial.IRON, 0);
		factory.getWeapon(null, weaponType.SWORD, itemMaterial.IRON, 0);
		factory.getWeapon(null, weaponType.SWORD, itemMaterial.IRON, 0);
		factory.getWeapon(null, weaponType.SWORD, itemMaterial.IRON, 0);
		factory.getScroll(getPlayer(), ScrollTypes.REMOVECURSEALL, 0);
		factory.getScroll(getPlayer(), ScrollTypes.REMOVECURSEALL, 0);
		factory.getScroll(getPlayer(), ScrollTypes.REMOVECURSEALL, 0);
		factory.getPotion(getPlayer(), potionTypes.RANDOM, 0);
		factory.getPotion(getPlayer(), potionTypes.RANDOM, 0);
		factory.getPotion(getPlayer(), potionTypes.RANDOM, 0);
		factory.getPotion(getPlayer(), potionTypes.RANDOM, 0);
		factory.getPotion(getPlayer(), potionTypes.RANDOM, 0);
		factory.getThrowableWeapon(getPlayer(), 0, 30, 0);
	}

	@Override
	public void display(SGPane console) {
		Util.clearAll();
		printDungeon(console);
		printNewEnemies();
		printNeighbors();
		printMessages(console);
		printStats(console);
		console.refresh();
		if(RoguelikeMain.isBlindMode())
			printTextArea();
		if(RoguelikeMain.isBlindMode())try {RoguelikeMain.getTextArea().setCaretPosition(RoguelikeMain.getTextArea().getLineStartOffset(22));} catch (BadLocationException e){e.printStackTrace();}
		if (subscreen != null && !(subscreen instanceof PlayScreen))
			subscreen.display(console);
	}

	//TODO change breaks!
	@Override
	public Screen reactToInput(KeyEvent key) {
		Position newPos;
		Creature player = getPlayer();
		Dungeon dun = getDungeon();	
		boolean invalidKeyToTravel=false;
		/*CONTROLS IN CASE OF TRAVEL*/
		while(player.isTraveling()){
			subscreen=null;		
			switch(key.getKeyCode()){
				case KeyEvent.VK_V:
					util.Descriptor.describe(false); //Describe while travel, without move or pass a turn
					return this;
				case KeyEvent.VK_B:
					util.Descriptor.describe(true); //Describe while travel, without move or pass a turn
					return this;
					//case KeyEvent.VK_ESCAPE: //Stops traveling
				//case KeyEvent.VK_LEFT:
					//case KeyEvent.VK_4:
				//case KeyEvent.VK_NUMPAD4:
				case KeyEvent.VK_Q:
				case KeyEvent.VK_W:
				case KeyEvent.VK_E:
				case KeyEvent.VK_A:
				case KeyEvent.VK_S:
				case KeyEvent.VK_D:
				case KeyEvent.VK_Z:
				case KeyEvent.VK_C:
					player.setTraveling(false);
					dun.addMessage(messages.getString("plyScrYouStopped"));
					return this;
				case KeyEvent.VK_X: //Take a step
					break;
				default: //Invalids all other keys
					invalidKeyToTravel = true;
			}
			if(invalidKeyToTravel)
				break;
			if(player.getCreaturesOnView().isEmpty()){
				if(player.getPosition().equals(player.getDestiny())){
					player.setTraveling(false);
					dun.addMessage(messages.getString("plyScrYouStopped"));
				}
				else
					player.travelOneStep();
			}else {
				player.setTraveling(false);
				dun.addMessage(messages.getString("plyScrYouFoundAMonster"));
				break;
			}			
			return this;
		}	
		if(invalidKeyToTravel)
			return this;
		/*END OF CONTROLS IN CASE OF TRAVEL	*/
		
		if(player.getLevel()>getPreviousPlayerLevel()){
			subscreen = new LevelScreen();
			setPreviousPlayerLevel(getPreviousPlayerLevel()+1);
		}
		
		if (subscreen != null && !(subscreen instanceof PlayScreen)){ 
			subscreen = subscreen.reactToInput(key);
		} else {
			switch (key.getKeyCode()){
			case KeyEvent.VK_A: 
				newPos = new Position(player.getPosition().getX()-1, player.getPosition().getY());
				if(player.canWalkOn(newPos))
					player.moveTo(newPos);
				else									
					dun.addMessage(messages.getString("plyScrYouHitAWall"));
				break;
			case KeyEvent.VK_D: 
				newPos = new Position(player.getPosition().getX()+1, player.getPosition().getY());
				if(player.canWalkOn(newPos))
					player.moveTo(newPos);
				else
					dun.addMessage(messages.getString("plyScrYouHitAWall"));
				break;
			case KeyEvent.VK_W:
				newPos = new Position(player.getPosition().getX(), player.getPosition().getY()-1);
				if(player.canWalkOn(newPos))
					player.moveTo(newPos);
				else
					dun.addMessage(messages.getString("plyScrYouHitAWall"));
				break;
			case KeyEvent.VK_S:
				newPos = new Position(player.getPosition().getX(), player.getPosition().getY()+1);
				if(player.canWalkOn(newPos))
					player.moveTo(newPos);
				else
					dun.addMessage(messages.getString("plyScrYouHitAWall"));
				break;
			case KeyEvent.VK_Q:
				newPos = new Position(player.getPosition().getX()-1, player.getPosition().getY()-1);
				if(player.canWalkOn(newPos))
					player.moveTo(newPos);
				else
					dun.addMessage(messages.getString("plyScrYouHitAWall"));
				break;			
			case KeyEvent.VK_E:
				newPos = new Position(player.getPosition().getX()+1, player.getPosition().getY()-1);
				if(player.canWalkOn(newPos))
					player.moveTo(newPos);
				else
					dun.addMessage(messages.getString("plyScrYouHitAWall"));
				break;
			case KeyEvent.VK_Z:
				newPos = new Position(player.getPosition().getX()-1, player.getPosition().getY()+1);
				if(player.canWalkOn(newPos))
					player.moveTo(newPos);
				else
					dun.addMessage(messages.getString("plyScrYouHitAWall"));
				break;
			case KeyEvent.VK_C:
				newPos = new Position(player.getPosition().getX()+1, player.getPosition().getY()+1);
				if(player.canWalkOn(newPos))
					player.moveTo(newPos);
				else
					dun.addMessage(messages.getString("plyScrYouHitAWall"));
				break;
			case KeyEvent.VK_I: 
				if(!player.getInventory().getItems().isEmpty())
					subscreen = new ExamineInventoryScreen(); 
				else
					dun.addMessage(messages.getString("plyScrInventoryEmpty"));
				break;
			case KeyEvent.VK_T: 
				if(!player.getInventory().getItems().isEmpty())
					subscreen = new DropScreen(); 
				else
					dun.addMessage(messages.getString("plyScrInventoryEmpty"));//
				break;
			case KeyEvent.VK_Y: 
				if(player.getInventory().getUnequippedItems().isEmpty())
					dun.addMessage(messages.getString("plyScrNothingEquip"));//"You have nothing else to equip!!");
				else
					subscreen = new EquipScreen();
				break;
			case KeyEvent.VK_R: 
				if(player.getInventory().getEquippedItems().isEmpty())
					dun.addMessage(messages.getString("plyScrNothingRemove"));//"You have nothing to remove!!");
				else
					subscreen = new UnequipScreen(); 
				break;				
			case KeyEvent.VK_H:
				if(!player.getInventory().getEdibleItems().isEmpty())
					subscreen = new EatScreen(); 
				else
					dun.addMessage(messages.getString("plyScrNothingEat"));//"You have nothing to eat!!");
				break;
			case KeyEvent.VK_K:
				if(!player.getInventory().getQuaffeableItems().isEmpty())
					subscreen = new QuaffScreen(); 
				else
					dun.addMessage(messages.getString("plyScrNothingQuaff"));//"You have nothing to drink!!");
				break;
			case KeyEvent.VK_L:
				if(player.getClas()==creatureClasses.WARRIOR){
					dun.addMessage(messages.getString("plyScrCantRead"));//"You never learned to read.");
					break;
				}
				if(player.isBlind()){
					dun.addMessage(messages.getString("plyScrCantSee"));//"You can't see anything!!!");
					break;
				}		
				if(!player.getInventory().getReadableItems().isEmpty())
					subscreen = new ReadScreen(); 
				else
					dun.addMessage(messages.getString("plyScrNothingRead"));//"You have nothing to read!!");
				break;
			case KeyEvent.VK_G: 
				if(player.getCreaturesOnView().isEmpty()){
					dun.addMessage(messages.getString("plyScrNoTarget"));//"There's nothing interesting to throw things at.");
					break;
				}
				else{
					if(player.getQuiveredWeapon() == null)
						dun.addMessage(messages.getString("plyScrNoMissiles"));//"You have no missiles quivered.");
					else
						subscreen = new ThrowScreen();					
					break;
				}
			case KeyEvent.VK_F: 
				if(player.getWeapon()==null){
					dun.addMessage(messages.getString("plyScrNoWeapon"));//"You have no weapon equipped.");
					break;
				}
				if (player.getWeapon().getType()!=Item.itemType.RANGEDWEAPON){
					dun.addMessage(messages.getString("plyScrNoRangedWeapon"));//"You have no ranged weapon equipped.");
					break;
				}
				RangedWeapon r = (RangedWeapon) player.getWeapon();
				if(player.getCreaturesOnRange(r.getRange()).isEmpty()){
					dun.addMessage(messages.getString("plyScrNoTargetInRange"));//"There's nothing interesting to throw things at range.");
					break;
				}
				if(player.getInventory().getAMissile(r.getAmmo())==null){
					dun.addMessage(messages.getString("plyScrYouDontHaveSpace")
							+r.getAmmoName(r.getAmmo())
							+messages.getString("plyScrPluralToFire"));//"You don't have "+r.getAmmo()+"s to fire.");
					break;
				}
				else{
					subscreen = new FireWeaponScreen();
					break;
				}
			case KeyEvent.VK_M:
				subscreen = new MessagesScreen();
				break;
			case KeyEvent.VK_X:
				if(!player.getCreaturesOnView().isEmpty())
					dun.addMessage(messages.getString("plyScrCantTravelMonsters"));//"You can't travel. There are monsters nearby.");
				else
					subscreen = new TravelScreen();
				break;
			case KeyEvent.VK_U:
				if(player.getCreaturesOnView().isEmpty())
					dun.addMessage(messages.getString("plyScrDontSeeMonsters"));//"You don't see any monsters");
				else player.hitNearestCreature();
				break;
			case KeyEvent.VK_P:
				saveGame();
				subscreen = new SavedScreen();
				break;
			//case KeyEvent.VK_P: PRAY?
			//case KeyEvent.VK_G: Pick. See below.
			case KeyEvent.VK_J:
				if(player.getSpellsKnown().isEmpty())
					dun.addMessage(messages.getString("plyScrNoSpellsKnown"));
				else subscreen = new CastScreen();
				break;
			}
			
			switch (key.getKeyChar()){
			case ';':
				if(player.getEverythingOnView().isEmpty())dun.addMessage(messages.getString("plyScrNothingInterestInView"));//("There's nothing interesting on view.");
				else subscreen = new ExamineMapScreen(); 
				break;
			case ',': player.pick(); break;
			case '.': player.waitATurn(); break;
			case '<': 				
				if(player.getIndexDungeonLevel() > 0) 
					player.changeDungeonLevel(1);
				else
					if(player.getTile().getSymbol()=='<' || player.getTile().getSymbol()=='X' )
						subscreen = playerGoingOut();	
				break;
			case '>': player.changeDungeonLevel(0); break;
			case '?': subscreen = new HelpScreen(); break;
			case '¡': subscreen = new StatusScreen(); break;
			
			case 'v': util.Descriptor.describe(false);break;
			case 'b': util.Descriptor.describe(true);break;			
			}
		}		
		if (player.getHp() < 1){
			player.die();
			return new LoseScreen(messages.getString("dthNegativeHP"));
		}
		return this;
        
        
		}

	private Screen playerGoingOut(){
		Creature player = PlayScreen.getPlayer();
		player.die();
		if(getPlayer().hasWon())
			return new WinScreen();		
		return new LoseScreen(messages.getString("dthNoIdol"));
	}

	public static void printDungeon(SGPane console){
		Creature player = getPlayer();
		Dungeon dun = getDungeon();
		int lev = player.getIndexDungeonLevel();
		//System.out.println("Printing level: "+lev+", player level: "+player.getIndexDungeonLevel()+", tile level: "+player.getTile().getLevel());
		Level levelToPrint = dun.getLevel(lev);
		int dunHeight = levelToPrint.getGridHeight();
		int dunWidth  = levelToPrint.getGridWidth();
		char charToPrint;
		Tile tileToPrint;
		Position p; 
		for(int i=0;i< dunWidth;i++){
		 	for(int j=0;j< dunHeight;j++){
		 		p = new Position(i,j);
	 			tileToPrint = levelToPrint.getTile(p);
	 			if(!player.canSee(tileToPrint)){ 
		 			tileToPrint.setOnView(false);
		 			if(tileToPrint.isExplored())
		 				if(player.getMemory().get(lev).containsKey(p))
		 					console.placeCharacter(p.getX(), p.getY(), player.getMemory().get(lev).get(p), SColor.DARK_GRAY);
		 				else
		 					console.placeCharacter(p.getX(), p.getY(), tileToPrint.getSymbol(), SColor.DARK_GRAY);
		 		}else{  //player CAN see it
		 			tileToPrint.setOnView(true);
		 			if(!tileToPrint.isExplored() && !tileToPrint.getThingsOnTile().isEmpty())
		 				dun.addMessage(messages.getString("plyScrNewObjects"));//"You see some new objects.");
		 			tileToPrint.setExplored(true);
		 			if(tileToPrint.getSymbol() == '#'){
		 				charToPrint = tileToPrint.getSymbol();
		 				console.placeCharacter(tileToPrint.getPosition().getX(), tileToPrint.getPosition().getY(), charToPrint, SColor.WHITE);
		 			}
		 			if(tileToPrint.getThingsOnTile().isEmpty()){
		 				charToPrint = tileToPrint.getSymbol();
		 				console.placeCharacter(p.getX(), p.getY(), charToPrint, SColor.WHITE);
		 			}else{ //This tile is not empty
		 				Drawable thing = (Drawable)tileToPrint.getThingsOnTile().get(tileToPrint.getThingsOnTile().size()-1);
		 				charToPrint = thing.getSymbol();
		 				console.placeCharacter(p.getX(),p.getY(),charToPrint,thing.getColor());
		 			}
		 			if(tileToPrint.hasCreature()){
		 				console.placeCharacter(p.getX(),p.getY(),tileToPrint.getCreature().getSymbol(), tileToPrint.getCreature().getColor());
		 			}
		 		}
	 			/*THINGS THAT ARE ALWAYS VISIBLE*/
	 			
	 			/**/
	 		}
 	 	}
	}
	
	private void printNewEnemies(){
		Creature p = getPlayer();
		ArrayList<Creature> enemiesNow = p.getCreaturesOnView();
		List<Creature> enemiesBefore = p.getEnemiesSawLastTurn();
		for(Creature c : enemiesNow)
			if(!enemiesBefore.contains(c)){
				PlayScreen.getDungeon().addMessage(messages.getString("A")+c.getName()+messages.getString("spaceAppeared"));
				//newEnemies.add(c);
				if(c.getWeapon()!= null)
					PlayScreen.getDungeon().addMessage(messages.getString("itWieldsA")+c.getWeapon().getName()+messages.getString("dot"));					
			}

		p.setEnemiesSawLastTurn(p.getCreaturesOnView());
	}
	
	private void printNeighbors(){
		Position p = getPlayer().getPosition();
		ArrayList<Tile> tilesArround = getPlayer().getTile().getNeighbors(8);
		ArrayList<String> walls = new ArrayList<String>();
		for(Tile t : tilesArround){
			//System.out.println("tilesArround.size = "+tilesArround.size());
			if(!t.isWalkable()) walls.add(p.getRelativePosition(t.getPosition()));
		}
		
		//OK, this is useless on a corridor. Too much spam.
/*		if(!walls.isEmpty() && getPlayer().getPosition().getRoom()!=null){
			System.out.println("walls.size = "+walls.size());
			String wall = "There's wall to your "+walls.get(0);
			for(int i = 1; i<walls.size();i++){
				System.out.println("adding to wall: "+walls.get(i));
				wall = wall.concat(", ");
				wall = wall.concat(walls.get(i));
				System.out.println(wall);
			}
			System.out.println(wall);
			d.addMessage(wall);
		}*/
	}
	
	public static void printMessages(SGPane console){
		ArrayList<String> mes = getDungeon().getMessages();
		console.placeHorizontalString(0, 26, mes.get(mes.size()-15));
		console.placeHorizontalString(0, 27, mes.get(mes.size()-14));
		console.placeHorizontalString(0, 28, mes.get(mes.size()-13));
		console.placeHorizontalString(0, 29, mes.get(mes.size()-12));
		console.placeHorizontalString(0, 30, mes.get(mes.size()-11));
		console.placeHorizontalString(0, 31, mes.get(mes.size()-10));
		console.placeHorizontalString(0, 32, mes.get(mes.size()-9));
		console.placeHorizontalString(0, 33, mes.get(mes.size()-8));
		console.placeHorizontalString(0, 34, mes.get(mes.size()-7));
		console.placeHorizontalString(0, 35, mes.get(mes.size()-6));
		console.placeHorizontalString(0, 36, mes.get(mes.size()-5));
		console.placeHorizontalString(0, 37, mes.get(mes.size()-4));
		console.placeHorizontalString(0, 38, mes.get(mes.size()-3));
		console.placeHorizontalString(0, 39, mes.get(mes.size()-2));
		console.placeHorizontalString(0, 40, mes.get(mes.size()-1));
	}
	
	public static void printStats(SGPane console){
		Creature player = getPlayer();
		Dungeon dun = getDungeon();
		String hp = (Integer.toString(player.getHp()) +messages.getString("slash")+Integer.toString(player.getMaxHealth())+messages.getString("spacehp"));
		String mana = (Integer.toString(player.getMana()) +messages.getString("slash")+Integer.toString(player.getMaxMana())+messages.getString("spacemp")); 
		SColor backColor = Util.getBackColorLife();
		String hungerText = util.Util.getHungerDesc();
		if(!hungerText.equals(previousHunger)){
			dun.addMessage(messages.getString("plyScrYouAreNow")+hungerText.toLowerCase());
			previousHunger = hungerText;
		}
		console.placeHorizontalString(0, 42, hp, SColor.BLACK, backColor);
		console.placeHorizontalString(14, 42, mana,SColor.BLACK, SColor.BLUE_VIOLET);
		console.placeHorizontalString(0, 43, messages.getString("time")+dun.getNumTurns(),SColor.WHITE,SColor.BLACK);
		console.placeHorizontalString(20, 43, hungerText, SColor.WHITE,SColor.BLACK);
		console.placeHorizontalString(34, 43, util.Util.getEffectsText(), SColor.WHITE,SColor.BLACK);
		console.placeHorizontalString(0, 44, (messages.getString("level")+player.getLevel()+dash+messages.getString("xpspace")+Util.getXPtext()), SColor.WHITE,SColor.BLACK);
	}

	public static void printTextArea(){
		Creature player = getPlayer();
		/* /Then the dungeon
		RoguelikeMain.getTextPane().setFont( new Font("monospaced", Font.PLAIN, RoguelikeMain.getFontSize()));
		Dungeon dun = getDungeon();
		int lev = player.getIndexDungeonLevel();
		Level levelToPrint = dun.getLevel(lev);
		char charToPrint;
		StringBuilder rowToPrint = new StringBuilder();
		Tile tileToPrint;
		Position playerPos = player.getPosition();
		Position p;
		int rowStart = playerPos.getY()-5 < 0 ? 0 : playerPos.getY()-5;
		int rowEnd = playerPos.getY()+5 > levelToPrint.getGridHeight() ? levelToPrint.getGridHeight() : playerPos.getY()+5;
		int columnStart = playerPos.getX()-9 < 0 ? 0 : playerPos.getX()-9;
		int columnEnd = playerPos.getX()+9 > levelToPrint.getGridWidth() ? levelToPrint.getGridWidth() : playerPos.getX()+9;
		//System.out.println("RowStart = "+rowStart);
		//System.out.println("RowEnd = "+rowEnd);
		for(int i = rowStart; i< rowEnd;i++){
		 	for(int j=columnStart;j< columnEnd;j++){
		 		p = new Position(j,i);
	 			tileToPrint = levelToPrint.getTile(p);
	 			if(player.canSee(tileToPrint)){
		 		  //player CAN see it

	 				if(tileToPrint.getSymbol() == '#'){
	 					rowToPrint.append(tileToPrint.getSymbol());
			 			continue;
	 				}
		 			if(tileToPrint.hasCreature()){
	 					rowToPrint.append(tileToPrint.getCreature().getSymbol());
		 				continue;
		 			}
		 			
		 			if(!tileToPrint.getThingsOnTile().isEmpty()){ //This tile is not empty
		 				Drawable thing = (Drawable)tileToPrint.getThingsOnTile().get(tileToPrint.getThingsOnTile().size()-1);
		 				charToPrint = thing.getSymbol();
	 					rowToPrint.append(charToPrint);
		 				continue;
		 			}
		 			else{
		 				rowToPrint.append(tileToPrint.getSymbol());
		 			}
		 			
	 			}
	 			else rowToPrint.append(" ");
		 	}
		 	rowToPrint.append("\n");
 	 	}
		Util.insertDungeonTA(0,rowToPrint);
 		//System.out.print(rowToPrint);
		RoguelikeMain.getTextPane().setFont( new Font("Arial Unicode MS", Font.PLAIN, RoguelikeMain.getFontSize()));
		*/
		
		
		//MESSAGES after, because at the beginning there are less than 15
		ArrayList<String> mes = getDungeon().getMessages();
		/*for(int i = 0;i<15; i++){
			Util.insertInTextAreaWithBlanks(mes.get(mes.size()-15-i));
		}*/
		/*Util.insertInTextAreaWithBlanks(mes.get(mes.size()-29));
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-28));
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-27));
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-26));
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-25));
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-24));
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-23));*/	
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-22));
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-21));
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-20));
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-19));
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-18));
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-17));
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-16));
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-15));
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-14));
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-13));
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-12));
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-11));
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-10));
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-9));
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-8));
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-7));
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-6));
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-5));
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-4));
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-3));
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-2));
		Util.insertInTextAreaWithBlanks(mes.get(mes.size()-1));
		
		String hp = (Integer.toString(player.getHp()) +messages.getString("slash")+Integer.toString(player.getMaxHealth())+messages.getString("spacehp"));
		String mana = (Integer.toString(player.getMana()) +messages.getString("slash")+Integer.toString(player.getMaxMana())+messages.getString("spacemp")); 
		String hungerText = util.Util.getHungerDesc();
		Util.insertInTextArea(messages.getString("level")+player.getLevel()+dash+messages.getString("xpspace")+util.Util.getXPtext()+blank+hungerText+ blank+util.Util.getEffectsText());
		Util.insertInTextArea(hp+" "+mana);
		Util.insertInTextArea(messages.getString("time")+getDungeon().getNumTurns());
	}
	
	public static int getPreviousPlayerLevel(){return previousPlayerLevel;}

	public static void setPreviousPlayerLevel(int previousPlayerLevel){PlayScreen.previousPlayerLevel = previousPlayerLevel;}
}
