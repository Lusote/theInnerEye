package util;

import java.util.ArrayList;
import java.util.ResourceBundle;

import rlmain.Creature;
import rlmain.Drawable;
import rlmain.RoguelikeMain;
import screens.PlayScreen;
import dungeongen.Dungeon;
import dungeongen.Position;
import dungeongen.Room;
import dungeongen.Tile;

public abstract class Descriptor {

	public static ResourceBundle messages = RoguelikeMain.messages;
	public static void describe(boolean detail){
		Creature p = PlayScreen.getPlayer();
		Dungeon d = PlayScreen.getDungeon();
		if(p.isBlind()){ 
			d.addMessage(messages.getString("blindEstart"));
			return;		
		}
		ArrayList<Drawable> things = p.getEverythingOnView();
		Position pos;
		describePosition(detail);
		for(Drawable dr : things){
			if(detail)d.addMessage(messages.getString("youSeeASpace")+dr.getName()+messages.getString("spaceToTheSpace")+p.getPosition().getRelativePosition(dr.getPosition()));
			else{
				pos = new Position(dr.getPosition(), p.getPosition());
				d.addMessage(messages.getString("youSeeASpace")+dr.getName()+messages.getString("onSpaced")+pos.toString());
			}
		}
		if(things.size()>15)d.addMessage(messages.getString("thereAreMoreThan15ThingsOnView"));
	}
	
	private static void describePosition(boolean detail){
		Creature p = PlayScreen.getPlayer();
		
		if(p.getTile().isDoor()){
			describePositionInDoor(detail);
			return;
		}
		if(p.getPosition().getRoom(PlayScreen.getPlayer().getIndexDungeonLevel())!=null){ 
			describePositionInRoom(detail);
			return;
		}
		describePositionInCorridor(detail);
	}

	private static void describePositionInDoor(boolean detail) {
		Creature p = PlayScreen.getPlayer();
		Dungeon d = PlayScreen.getDungeon();
		
		Room r;
		try{
			r = p.getPosition().getRoom(PlayScreen.getPlayer().getIndexDungeonLevel());
		}catch(NullPointerException e){	//This shouldn't happen ever. I'm not sure why is here...
			System.out.println("NULL ROOM");
			return;
		}	
		d.addMessage(messages.getString("youAreInARoomsDoor"));
		ArrayList<Position> doors = r.getDoors();
		Position otherDoor = doors.get(0).equals(p.getPosition()) ? doors.get(1) : doors.get(0) ;
		String direction = (!detail)? p.getPosition().getRelativeDetailedPosition(otherDoor).toString() : otherDoor.distance(p.getPosition()).intValue()+messages.getString("spaceMetersToTheSpace")+ otherDoor.getRelativePosition(p.getPosition());
		d.addMessage(messages.getString("theOtherDoorIsOnSpace")+direction);
	}

	private static void describePositionInCorridor(boolean detail){
		Creature p = PlayScreen.getPlayer();
		Dungeon d = PlayScreen.getDungeon();
		Tile playerTile = d.getLevel(p.getIndexDungeonLevel()).getTile(p.getPosition());
		int distance;
		
		d.addMessage(messages.getString("youAreInACorridor"));		
		ArrayList<Position> posOnView = (ArrayList<Position>) p.getFarthestsPositionsOnSightStoppingAtDoors();
		String direction = p.getPosition().getRelativePosition(posOnView.get(0));
		distance = p.getPosition().distance(posOnView.get(0)).intValue();
		Tile farthestTile = d.getLevel(p.getIndexDungeonLevel()).getTile(posOnView.get(0));
		d.addMessage(messages.getString("itGoesForSpace")+distance+messages.getString("spaceMetersToTheSpace")+direction);
		for(Tile t : farthestTile.getNeighbors(8))
			if(t.isDoor())
				d.addMessage(messages.getString("andThenThereIsADoor"));
		
		
		//If the closest tile is NEXT to the player
		for(Tile t : playerTile.getNeighbors(8)){
			if(t.isDoor()){
				d.addMessage(messages.getString("thereIsADoorNextToYouToTheSpace")+p.getPosition().getRelativePosition(t.getPosition()));
				return;
			}
		}
		distance = p.getPosition().distance(posOnView.get(1)).intValue();
		direction = p.getPosition().getRelativePosition(posOnView.get(1));
		d.addMessage(messages.getString("itGoesForSpace")+distance+messages.getString("spaceMetersToTheSpace")+direction);
		for(Tile t : d.getLevel(p.getIndexDungeonLevel()).getTile(posOnView.get(1)).getNeighbors(8))
			if(t.isDoor())
				d.addMessage(messages.getString("andThenThereIsADoor"));
		
	}

	private static void describePositionInRoom(boolean detail) {
		Creature p = PlayScreen.getPlayer();
		Dungeon d = PlayScreen.getDungeon();
		
		Room r;
		try{
			r = p.getPosition().getRoom(PlayScreen.getPlayer().getIndexDungeonLevel());
		}catch(NullPointerException e){	//This shouldn't happen ever. I'm not sure why is here...
			System.out.println("NULL ROOM");
			return;
		}		
		d.addMessage(messages.getString("youAreInARoom"));
		d.addMessage(messages.getString("theRoomIsSpace")+r.getWide()+"x"+r.getHeight()+messages.getString("spaceMetersHighDot"));
		Position door1 = r.getDoors().get(0);
		Position door2 = r.getDoors().get(1);
		String direction = (!detail)? 
										p.getPosition().getRelativeDetailedPosition(door1).toString() : 
										door1.distance(p.getPosition()).intValue()+messages.getString("spaceMetersToTheSpace")+ door1.getRelativePosition(p.getPosition());
		d.addMessage(messages.getString("oneDoorIsOnSpace")+direction+messages.getString("dot"));
		direction = (!detail)? 
										p.getPosition().getRelativeDetailedPosition(door2).toString() : 
										p.getPosition().distance(door2).intValue()+messages.getString("spaceMetersToTheSpace")+ p.getPosition().getRelativePosition(door2);
		d.addMessage(messages.getString("theOtherDoorIsOnSpace")+direction+messages.getString("dot"));
	}
	
}
