package screens;

import java.awt.event.KeyEvent;

import javax.swing.text.BadLocationException;

import rlmain.RoguelikeMain;
import rlmain.Creature.creatureClasses;
import squidpony.squidgrid.gui.SGPane;
import util.Util;

public class SeedScreen implements Screen {
	
	private Long seed;
	private creatureClasses playerClass = null;
	private String name;
//	private boolean firstTime = true;
	
	public SeedScreen(creatureClasses playerClass, String name){
		this.seed = new Long(0);
		this.playerClass = playerClass;
		this.name = name;
	}

	@Override
	public void display(SGPane console) {
		Util.clearAll();
		Util.printText(10, 10, messages.getString("sedScrEnterSeed"));//"Enter seed:");
		//if(firstTime)rlmain.RoguelikeMain.getSpeaker().speak("Enter seed");
		//firstTime = false;
		if(getSeed() != 0)
			Util.printText(10, 12, getSeedString());
		Util.printText(10, 13, messages.getString("nmeScrUnderline"));//"________");

		if(RoguelikeMain.isBlindMode())try {RoguelikeMain.getTextArea().setCaretPosition(RoguelikeMain.getTextArea().getLineStartOffset(0));} catch (BadLocationException e){e.printStackTrace();}
		console.refresh();

	}

	@Override
	public Screen reactToInput(KeyEvent key) {
		switch(key.getKeyCode()){
			case KeyEvent.VK_0: addToSeed(0); 
			//				rlmain.RoguelikeMain.getSpeaker().speak("zero");
				return this;
			case KeyEvent.VK_1: addToSeed(1); 
			//rlmain.RoguelikeMain.getSpeaker().speak("one");
				return this;
			case KeyEvent.VK_2: addToSeed(2); 
			//rlmain.RoguelikeMain.getSpeaker().speak("two");
				return this;
			case KeyEvent.VK_3: addToSeed(3); 
			//rlmain.RoguelikeMain.getSpeaker().speak("three");
				return this;
			case KeyEvent.VK_4: addToSeed(4); 
			//rlmain.RoguelikeMain.getSpeaker().speak("four");
				return this;
			case KeyEvent.VK_5: addToSeed(5); 
			//rlmain.RoguelikeMain.getSpeaker().speak("five");
				return this;
			case KeyEvent.VK_6: addToSeed(6); 
			//rlmain.RoguelikeMain.getSpeaker().speak("six");
				return this;
			case KeyEvent.VK_7: addToSeed(7); 
			//rlmain.RoguelikeMain.getSpeaker().speak("seven");
			 	return this;
			case KeyEvent.VK_8: addToSeed(8); 
			//rlmain.RoguelikeMain.getSpeaker().speak("eight");
				 return this;
			case KeyEvent.VK_9: addToSeed(9); 
			//rlmain.RoguelikeMain.getSpeaker().speak("nine");
				return this;
			case KeyEvent.VK_BACK_SPACE: removeLast(); 
			//rlmain.RoguelikeMain.getSpeaker().speak("delete");
			 	return this;
			case KeyEvent.VK_ENTER: /*firstTime=true;*/ return new PlayScreen(this.getSeed(), this.playerClass, name);
		}
		return this;
	}
	
	private void setSeed(long s){this.seed = s;}
	
	private Long getSeed(){return this.seed;}
	
	private String getSeedString(){return this.getSeed().toString();}
	
	private void addToSeed(int i){
		if(getSeedString().length()<8){
			Integer newI = new Integer(i);
			String s = getSeedString();	
			s = s+newI.toString();
			setSeed(Long.parseLong(s));
		}
	}
	
	private void removeLast(){
		if(getSeedString().length()<=0)
			return;
		String s = getSeedString();	
		s = s.substring(0, s.length()-1);
		setSeed(Long.parseLong(s));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
