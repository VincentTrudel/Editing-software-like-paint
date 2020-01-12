import java.awt.Color;
import java.awt.Graphics2D;

public abstract class Forme {
	
	private Point point1;
	private Point point2;
	private Color couleur;
	private int tailleTrait;
	
		public Forme(Point point1, Point point2, Color couleur, int tailleTrait) {
			this.setPoint1(point1);
			this.setPoint2(point2);
			this.setCouleur(couleur);
			this.setTailleTrait(tailleTrait);
		};
	
	public abstract void draw(Graphics2D g2d);


	public Point getPoint1() {
		return point1;
	}

	public void setPoint1(Point point1) {
		this.point1 = point1;
	}
	
	public Point getPoint2() {
		return point2;
	}

	public void setPoint2(Point point2) {
		this.point2 = point2;
	}


	public Color getCouleur() {
		return couleur;
	}


	public void setCouleur(Color couleur) {
		this.couleur = couleur;
	}


	public int getTailleTrait() {
		return tailleTrait;
	}


	public void setTailleTrait(int tailleTrait) {
		this.tailleTrait = tailleTrait;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
