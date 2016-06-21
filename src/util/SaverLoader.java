package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

import rlmain.Creature;
import rlmain.RoguelikeMain;
import screens.PlayScreen;
import dungeongen.Dungeon;
import factories.Factory;

public class SaverLoader implements Serializable{
	
	private static final long serialVersionUID = RoguelikeMain.getSerialVersionUID();
	private Dungeon dun;
	private Creature player;
	private List<Creature> creatures;
	private Random theRNG;	
	private Factory fact;
	
	public SaverLoader(){
		setDun();
		setPlayer();
		setCreatures();
		setTheRNG();
		setFactory();
	}

	public void saveGame(){
		System.out.println("Saving game....");
	    try{
	    	String filename = PlayScreen.getPlayer().getName()+".save";
	        OutputStream file = new FileOutputStream(filename);
	        OutputStream buffer = new BufferedOutputStream(file);
	        ObjectOutput output = new ObjectOutputStream(buffer);
	        try{
	          output.writeObject(this);
	        }
	        finally{
	          output.close();
	        }
	      }  
	      catch(IOException ex){
	      }		
	}
	
	public static SaverLoader loadGame(String name){
		System.out.println("Loading game "+ name+"....");
		try(InputStream file = new FileInputStream(name);
			InputStream buffer = new BufferedInputStream(file);
			ObjectInput input = new ObjectInputStream (buffer);){
		      SaverLoader recoveredGame = (SaverLoader)input.readObject();
		      return recoveredGame;		      
		    }
	    catch(ClassNotFoundException ex){
	      System.out.println("Cannot perform input. Class not found.");
	      System.out.println(ex);
	      return null;
	    }
	    catch(IOException ex){
	      System.out.println("Cannot perform input.");
	      System.out.println(ex);
	      return null;
	    }
	}
	
	public static File[] getSaveGames(){
		File folder = new File(".");
		FilenameFilter saveGamesFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
				if (lowercaseName.endsWith(".save")) {
					return true;
				} else {
					return false;
				}
			}
		};
		return folder.listFiles(saveGamesFilter);
	}


	public Dungeon getDun(){return dun;}

	public void setDun(){this.dun = PlayScreen.getDungeon();}

	public Creature getPlayer(){return player;}

	public void setPlayer(){this.player = PlayScreen.getPlayer();}

	public List<Creature> getCreatures(){return creatures;}

	public void setCreatures(){this.creatures = getDun().getCreaturesButPlayer();}

	public Random getTheRNG(){return this.theRNG;}

	public void setTheRNG(){this.theRNG = PlayScreen.getTheRNG();}

	public Factory getFactory(){return fact;}

	public void setFactory(){this.fact = PlayScreen.getFactory();}

	public static void deleteFile(String name) {
		try{
			Path path = FileSystems.getDefault().getPath(name);
	    	Files.delete(path);
	    	System.out.println("File game.save deleted.");
		}catch(NoSuchFileException x){
			System.out.println("No such file or directory game.save");
	    }catch(DirectoryNotEmptyException x){
	    	System.out.println("Not empty game.save");
	    }catch(IOException x){
	    	System.out.println(x);
	    }
	}
}
