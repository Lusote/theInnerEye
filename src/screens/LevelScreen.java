package screens;

import items.Ammo.ammoType;
import items.Item.itemMaterial;
import items.Weapon.weaponType;

import java.awt.event.KeyEvent;

import javax.swing.text.BadLocationException;

import dungeongen.Dungeon;
import effects.RemoveAllCurseEffect;
import rlmain.Creature;
import rlmain.RoguelikeMain;
import squidpony.squidgrid.gui.SGPane;
import util.Util;

public class LevelScreen implements Screen {
	
	@Override
	public void display(SGPane console) {
		Util.clearTextArea();
		Util.printText(1, 1, messages.getString("lvlScrLevelUp"));
		Util.printText(1, 2, messages.getString("lvlScrChooseReward"));
		
		switch(PlayScreen.getPlayer().getClas()){
			case WARRIOR:
				Util.printText(1, 3, messages.getString("1.-")+messages.getString("lvlScrMoreHP"));
				Util.printText(1, 4, messages.getString("2.-")+messages.getString("lvlScrMoreSTR"));
				Util.printText(1, 5, messages.getString("3.-")+messages.getString("lvlScrRandomWeapon"));
				Util.printText(1, 6, messages.getString("4.-")+messages.getString("lvlScrMoreHPREGEN"));
				Util.printText(1, 7, messages.getString("5.-")+messages.getString("lvlScrUncurseItems"));
				break;
			case ARCHER:
				Util.printText(1, 3, messages.getString("1.-")+messages.getString("lvlScrMoreHP"));
				Util.printText(1, 4, messages.getString("2.-")+messages.getString("lvlScrMoreDODGE"));
				Util.printText(1, 5, messages.getString("3.-")+messages.getString("lvlScrRandomAmmo"));
				Util.printText(1, 6, messages.getString("4.-")+messages.getString("lvlScrMoreHPREGEN"));
				Util.printText(1, 7, messages.getString("5.-")+messages.getString("lvlScrUncurseItems"));
				break;
			case WHITEMAGE:
				Util.printText(1, 3, messages.getString("1.-")+messages.getString("lvlScrMoreHP"));
				Util.printText(1, 4, messages.getString("2.-")+messages.getString("lvlScrMoreDODGE"));
				Util.printText(1, 5, messages.getString("3.-")+messages.getString("lvlScrRandomAmmo"));
				Util.printText(1, 6, messages.getString("4.-")+messages.getString("lvlScrMoreHPREGEN"));
				Util.printText(1, 7, messages.getString("5.-")+messages.getString("lvlScrUncurseItems"));
				break;
		default:
			break;
		}		
		if(RoguelikeMain.isBlindMode())try {RoguelikeMain.getTextArea().setCaretPosition(RoguelikeMain.getTextArea().getLineStartOffset(0));} catch (BadLocationException e){e.printStackTrace();}
		console.refresh();
	}

	@Override
	public Screen reactToInput(KeyEvent key) {
		Creature player = PlayScreen.getPlayer();
		Dungeon dun = PlayScreen.getDungeon();
		switch(key.getKeyCode()){		
			case KeyEvent.VK_1:
				switch(player.getClas()){
					case WARRIOR:
						player.setMaxHealth(player.getMaxHealth()+20);
						dun.addMessage(messages.getString("lvlScrMoreHPChoosed"));
						return null;
					case ARCHER:
						player.setMaxHealth(player.getMaxHealth()+20);
						dun.addMessage(messages.getString("lvlScrMoreHPChoosed"));
						return null;
				case WHITEMAGE:
					player.setMaxHealth(player.getMaxHealth()+20);
					dun.addMessage(messages.getString("lvlScrMoreHPChoosed"));
					return null;
				default:
					break;
				}
			case KeyEvent.VK_2:
				switch(player.getClas()){
				case WARRIOR:
					player.setStrength(player.getStrength()+2);
					dun.addMessage(messages.getString("lvlScrMoreSTRChoosed"));
					return null;
				case ARCHER:
					player.setDodge(player.getDodge()+2);
					dun.addMessage(messages.getString("lvlScrMoreDODGEChoosed"));
					return null;
				case WHITEMAGE:
					player.setAttack(player.getAttack()+2);
					dun.addMessage(messages.getString("lvlScrRandomAmmoChoosed"));
					return null;
				default:
					break;
			}
			case KeyEvent.VK_3:
				switch(player.getClas()){
				case WARRIOR:
					PlayScreen.getFactory().getWeapon(player, weaponType.RANDOM,itemMaterial.RANDOM, 0);
					dun.addMessage(messages.getString("lvlScrRandomWeaponChoosed"));
					return null;
				case ARCHER:
					PlayScreen.getFactory().getAmmo(player,ammoType.RANDOM, itemMaterial.RANDOM, -1, 0);
					dun.addMessage(messages.getString("lvlScrRandomAmmoChoosed"));
					return null;
				case WHITEMAGE:
					PlayScreen.getFactory().getWeapon(player, weaponType.RANDOM,itemMaterial.RANDOM, 0);
					dun.addMessage(messages.getString("lvlScrRandomWeaponChoosed"));
					return null;
				default:
					break;
			}
			case KeyEvent.VK_4:
				switch(player.getClas()){
					case WARRIOR:
						player.setHPRegenerationRate(player.getHpRegenerationRate()+0.05);
						dun.addMessage(messages.getString("lvlScrMoreHPREGENChoosed"));
						return null;
					case ARCHER:
						player.setHPRegenerationRate(player.getHpRegenerationRate()+0.05);
						dun.addMessage(messages.getString("lvlScrMoreHPREGENChoosed"));
						return null;
					case WHITEMAGE:
						player.setHPRegenerationRate(player.getHpRegenerationRate()+0.05);
						dun.addMessage(messages.getString("lvlScrMoreHPREGENChoosed"));
						return null;
				default:
					break;
				}
			case KeyEvent.VK_5:
				switch(player.getClas()){
					case WARRIOR:
						player.addEffect(new RemoveAllCurseEffect());
						return null;
					case ARCHER:
						player.addEffect(new RemoveAllCurseEffect());
						return null;
					case WHITEMAGE:
						player.addEffect(new RemoveAllCurseEffect());
						return null;
				default:
					break;
				}
				
		}
		return this;
	}

}
