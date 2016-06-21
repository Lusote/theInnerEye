package util;
import java.util.ArrayList;
import java.util.HashSet;

import dungeongen.Position;


public class Node{

	private Position p;
	private double g;
	private double h;
	private double f;
	private Node parent;
	private Node goal;
	private int numNeighbors;

	public Node(Position pos, Node go, Node par, int numN){
		p = pos;
		goal = go;
		parent = par;
		h = 0;
		g = 0;
		f = 0;
		numNeighbors = numN;

	}

	@Override
	public boolean equals(Object other){
	    if (other == null) return false;
    	if (other == this) return true;
	    if (!(other instanceof Node))
	    	return false;
	    Node another = (Node)other;
	    if ((another.getNodePosition().getX() == this.getNodePosition().getX()) &&
	    	(another.getNodePosition().getY() == this.getNodePosition().getY())){
        	return true;
    	}
	    else return false;
	}

	@Override
	public int hashCode(){
		final int prime = 41;
		int result =1;
		result = result + this.getNodePosition().getX() * prime;
		result = result + this.getNodePosition().getY() * prime;
		return result;
	}

	public void calculateH(){
		double dx = Math.abs(this.getNodePosition().getX() - this.getGoal().getNodePosition().getX());
		double dy = Math.abs(this.getNodePosition().getY() - this.getGoal().getNodePosition().getY());
		if(this.getNumNeighbors() == 4){
			this.h = Math.sqrt(dx*dx + dy*dy);
		}
		else if(this.getNumNeighbors() == 8){
			this.h = Math.max(dx, dy);
		}
	}

	public ArrayList<Node> getNodeNeighbors(HashSet<Position> walls){
		ArrayList<Node> toReturn = new ArrayList<Node>();
		Node aux = new Node(null, this.getGoal(), this, this.getNumNeighbors());
		ArrayList<Position> neigh = this.getNodePosition().getNeighbors(this.getNumNeighbors());
		for(Position pos : neigh){
			if(!walls.contains(pos)){ 
				aux = new Node(pos, this.getGoal(), this, this.getNumNeighbors());
				aux.calculateH();
				aux.calculateF();
				toReturn.add(aux);
			}
		}
		return toReturn;
	}

	public double calculateDist(Node n){
		int neigh = this.getNumNeighbors();
		double dx = Math.abs(this.getNodePosition().getX() - n.getNodePosition().getX());
		double dy = Math.abs(this.getNodePosition().getY() - n.getNodePosition().getY());
		if(neigh == 4){
			return Math.sqrt(dx*dx + dy*dy);
		}
		else
			return Math.sqrt(dx*dx + dy*dy);
	}

	public void setGoal(Node g){
		this.goal = g;
	}

	public void setParent(Node p){
		this.parent = p;
	}

	public void calculateF(){
		this.f = this.g + this.h;
	}

	public void setG(double value){
		this.g = value;
	}
	public void setF(double value){
		this.f = value;
	}

	public void setNodePosition(Position pos){
		this.p = pos;
	}

	public Position getNodePosition(){
		return this.p;
	}

	public double getF(){
		return this.f;
	}

	public double getG(){
		return this.g;
	}

	public double getH(){
		return this.h;
	}

	public Node getGoal(){
		return this.goal;
	}

	public Node getParent(){
		return this.parent;
	}

	public int getNumNeighbors(){
		return this.numNeighbors;
	}
}