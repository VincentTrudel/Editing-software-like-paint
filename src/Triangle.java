import java.awt.Color;
import java.awt.Graphics2D;

public class Triangle extends Forme  {

	public Triangle(Point point1, Point point2, Color couleur, int tailleTrait) {
		super(point1, point2, couleur, tailleTrait);

	}

	@Override
	public void draw(Graphics2D g2d) {
			Point point1 = this.getPoint1();
			Point point2 = this.getPoint2();
		
			int x1 = point1.getX();
			int y1 = point1.getY();
			int x2 = point2.getX();
			int y2 = point1.getY();
			int x3 = (Math.max(x1, x2) - Math.min(x1, x2))/2 + Math.min(x1, x2);
			int y3 = point2.getY();

			g2d.drawLine(x1, y1, x2, y2);
			g2d.drawLine(x2, y2, x3, y3);
			g2d.drawLine(x1, y1, x3, y3);
	}

}
