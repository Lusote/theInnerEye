package screens;

import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

import rlmain.RoguelikeMain;
import squidpony.squidgrid.gui.SGPane;

public interface Screen {

	public ResourceBundle messages = RoguelikeMain.messages;
	public String blank = messages.getString("blank"); 
	public String space = messages.getString("space");
	public String x = messages.getString("x");
	public String dash = messages.getString("dashSpaces");
	public String equippedText = messages.getString("spaceDashEQUIPPEDdash");
	public String spaceX = messages.getString("spaceX");
	public void display(SGPane console);
	
	public Screen reactToInput(KeyEvent key);

}
