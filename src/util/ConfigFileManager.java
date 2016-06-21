package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public abstract class ConfigFileManager {

	
	public static void makeAConfigProperties(){
		if(new File("game.config").exists())
			return;
		newConfigFile();
	}
	
	private static void newConfigFile(){
		Properties prop = new Properties();
		FileOutputStream output = null;	 
		try {	 
			output = new FileOutputStream("game.config");
			// set the properties value
			prop.setProperty("playername", "Heroe");
			prop.setProperty("language", "ES");
			prop.setProperty("country", "ES");
			prop.setProperty("textarea", "yes");
			prop.setProperty("fontsize", "10");
			prop.store(output, null);
	 
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {	
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	 
		}
	}

	public static String getDefaultName() {
		makeAConfigProperties();
		Properties prop = new Properties();
		FileInputStream input = null;	 
		String toReturn ="Player";
		try {	 
			input = new FileInputStream("game.config");
			prop.load(input);
			toReturn = prop.getProperty("playername");
			input.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return toReturn;
	}

	public static boolean getTextArea() {
		makeAConfigProperties();
		Properties prop = new Properties();
		FileInputStream input = null;	 
		boolean toReturn = false;
		try {	 
			input = new FileInputStream("game.config");
			prop.load(input);
			try{
				toReturn = prop.getProperty("textarea").equals("yes");
			}
			catch(Exception e){newConfigFile();}
			input.close();
		} catch (Exception ex) {
			newConfigFile();
			ex.printStackTrace();
		}
		return toReturn;
	}

	public static String getFontSize() {
		makeAConfigProperties();
		Properties prop = new Properties();
		FileInputStream input = null;	 
		String toReturn = "12";
		try {	 
			input = new FileInputStream("game.config");
			prop.load(input);
			try{
				toReturn = prop.getProperty("fontsize");
			}
			catch(Exception e){newConfigFile();}
			input.close();
		} catch (Exception ex) {
			newConfigFile();
			ex.printStackTrace();
		}
		System.out.println("Font Size:"+toReturn);
		return toReturn;
	}

	public static void setDefaultName(String name){
		makeAConfigProperties();
		Properties prop = new Properties();
		FileOutputStream output = null;	 
		try {	
			prop.load(new FileInputStream("game.config"));
			output = new FileOutputStream("game.config");
			prop.setProperty("playername", name);
			prop.store(output, null);
	 
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {	
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	 
		}
	}

	public static String getDefaultLanguage() {
		makeAConfigProperties();
		Properties prop = new Properties();
		FileInputStream input = null;	 
		String toReturn ="EN";
		try {	 
			input = new FileInputStream("game.config");
			prop.load(input);
			toReturn = prop.getProperty("language");
			input.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return toReturn;
	}
}
