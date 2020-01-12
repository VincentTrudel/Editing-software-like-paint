import java.awt.Color;
import java.awt.Graphics2D;

public class Ovale extends Forme {

	public Ovale(Point point1, Point point2, Color couleur, int tailleTrait) {
		super(point1, point2, couleur, tailleTrait);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw(Graphics2D graphics) {
		Point point1 = this.getPoint1();
		Point point2 = this.getPoint2();

		int xPoint1 = point1.getX();
		int yPoint1 = point1.getY();
		int xPoint2 = point2.getX();
		int yPoint2 = point2.getY();
		int xMin = Math.min(xPoint1, xPoint2);
		int yMin = Math.min(yPoint1, yPoint2);
		int distanceX1X2 = Math.max(xPoint1, xPoint2) - xMin;
		int distanceY1Y2 = Math.max(yPoint1, yPoint2) - yMin;
		graphics.drawOval(xMin, yMin, distanceX1X2, distanceY1Y2);
		
	}

}
