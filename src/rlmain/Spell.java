package rlmain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import effects.Effect;

public class Spell implements Serializable{
	private static final long serialVersionUID = RoguelikeMain.getSerialVersionUID(); 
	 
    private String name;
    private List<Effect> effects;
    private int manaCost;
    private int minimumIntelligence;
 
    public Spell(String name, int manaCost, int minInt, List<Effect> effect){
        this.setName(name);
        this.manaCost = manaCost;
        this.effects=effect;
        this.minimumIntelligence = minInt;
    }
    
    public String getName() { return name;}
    
	public void setName(String name){this.name = name;}

    public int getManaCost() { return manaCost; }
    
    public void setManaCost(int cost) { this.manaCost = cost;}
 
    public ArrayList<Effect> getEffects() { return (ArrayList<Effect>) this.effects;}

	public void setEffect(List<Effect> effect){this.effects = effect;}

	public int getMinimumIntelligence() {
		return minimumIntelligence;
	}

	public void setMinimumIntelligence(int minimumIntelligence) {
		this.minimumIntelligence = minimumIntelligence;
	}
}