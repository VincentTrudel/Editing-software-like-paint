import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Vector;

public class Trait {

	private Vector<Point> points;
	private Color couleur;
	private int tailleTrait;
	
	
	public Trait(Vector<Point> points, Color couleur, int tailleTrait)
	{
		this.setPoints(points);
		this.setCouleur(couleur);
		this.setTailleTrait(tailleTrait);
	}
	
	public Vector<Point> getPoints() {
		return points;
	}
	public void setPoints(Vector<Point> lesPoints) {
		this.points = lesPoints;
	}
	public Color getCouleur() {
		return couleur;
	}
	public void setCouleur(Color couleur) {
		this.couleur = couleur;
	}
	
	public void draw(Graphics2D g2d) {
		int size = points.size();
		Point point1;
		Point point2;
		int x1, x2, y1, y2;
		if (size > 1) {
			for(int i=0; i<size-2; i++)
			{
				point1 = points.get(i);
				point2 = points.get(i+1);
				x1 = point1.getX();
				y1 = point1.getY();
				x2 = point2.getX();
				y2 = point2.getY();
				g2d.drawLine(x1, y1, x2, y2);	
			}
		}
		else if (size ==1) {
			point1 = points.get(0);
			x1 = point1.getX();
			y1 = point1.getY();
			g2d.drawLine(x1, y1, x1, y1);	
		}
	}

	public int getTailleTrait() {
		return tailleTrait;
	}

	public void setTailleTrait(int tailleTrait) {
		this.tailleTrait = tailleTrait;
	}
	
}
