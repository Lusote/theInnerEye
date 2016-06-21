package util;
import java.util.*;

import dungeongen.Position;


public class Astar{

    private static HashSet<Node> open;
    private static HashSet<Node> closed;

    public Astar(){}

    // walls being the obstacles
    public static ArrayList<Position> getPath(Position start, Position goal, int numNeighbor, HashSet<Position> walls){

        //int iters = 0;     
        double minimumDistance = 99;

        Node current;
        Node goalNode = new Node(goal, null, null, numNeighbor);
        Node startNode = new Node(start, goalNode, null, numNeighbor);

        startNode.setG(0);
        startNode.calculateH();
        startNode.calculateF();
        //System.out.println("Initial dist: "+startNode.getH());
        ArrayList<Node> neighborNodes = new ArrayList<Node>();
        double tentativeGScore;
        ArrayList<Position> toReturn = new ArrayList<Position>();

        closed = new HashSet<Node>();
        open = new HashSet<Node>();
        open.add(startNode);

        while(!open.isEmpty()){
        	//iters++;
            current = getLowestF(open);

            if(current.getH()<minimumDistance){
                minimumDistance=current.getH();
            }
            if(current.getNodePosition().getX() == goal.getX() &&
                current.getNodePosition().getY() == goal.getY()){
                //System.out.println("iters: "+iters );
                //System.out.println("minDist: "+minimumDistance );
                //System.out.println("open.size(): "+open.size());
                //System.out.println("closed.size(): "+closed.size());
                //System.out.println("walls.size(): "+walls.size());
                //System.out.println();
                
                
                return reconstructPath(current, toReturn);
                
                
            }
            open.remove(current);
            closed.add(current);

            neighborNodes = current.getNodeNeighbors(walls);
            for(Node n :neighborNodes){

                tentativeGScore = current.getG() + current.calculateDist(n);

                if(closed.contains(n) && tentativeGScore >= n.getG()){
                    continue;
                }

                if(!open.contains(n) || tentativeGScore < n.getG()){
                    n.setG(tentativeGScore);
                    n.calculateH();
                    n.calculateF();

                    if(!open.contains(n)){;
                        open.add(n);
                    }
                }
            }
        }
            System.out.println("PATHFAILING.");
            //System.out.println("start: "+start.getX()+", "+start.getY());
            //System.out.println("end: "+goal.getX()+", "+goal.getY());
            //System.out.println("iters: "+iters );
            //System.out.println("minDist: "+minimumDistance );
            //System.out.println("open.size(): "+open.size());
            //System.out.println("closed.size(): "+closed.size());
            //System.out.println("walls.size(): "+walls.size());
            //System.out.println();
        return null;
    }

    private static ArrayList<Position> reconstructPath(Node current, ArrayList<Position> pos){
        if(current.getParent()==null){
            pos.add(current.getNodePosition());
            return pos;
        }
        else{
            pos.add(current.getNodePosition());
            return reconstructPath(current.getParent(), pos);
        }
    }

    private static Node getLowestF(HashSet<Node> nodes){
        Iterator<Node> iter = nodes.iterator();
        Node toReturn = null;
        Node aux;
        while(iter.hasNext()){
            if(toReturn == null){
                toReturn = (Node) iter.next();
            }
            else{
                aux = (Node) iter.next();
                if(aux.getF() < toReturn.getF()){
                    toReturn = aux;
                }
            }
        }
        return toReturn;
    }

}
