package util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dungeongen.Position;

public class Line implements Iterable<Position>{

	private List<Position> points;
	
	public Line(Position start, Position end){
		int x0 = start.getX();
		int y0 = start.getY();
		int x1 = end.getX();
		int y1 = end.getY();

        points = new ArrayList<Position>();
     
        int dx = Math.abs(x1-x0);
        int dy = Math.abs(y1-y0);
     
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx-dy;
     
        while (true){
            points.add(new Position(x0, y0));
         
            if (x0==x1 && y0==y1)
                break;
         
            int e2 = err * 2;
            if (e2 > -dx) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx){
                err += dx;
                y0 += sy;
            }
        }
	}

	public List<Position> getPoints(){
		return this.points;
	}

	@Override
	public Iterator<Position> iterator(){
		return this.points.iterator();
	}
}
