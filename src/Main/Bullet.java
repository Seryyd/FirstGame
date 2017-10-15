package Main;
import java.awt.*;
public class Bullet {
	
	//FIELDS
	private double x;
	private double y;
	private int r;
	
	private double dx;
	private double dy;
	private double rad;
	private double speed;
	
	private Color color1;
	
	//CONSTRUCTOR
	public Bullet (double angle, int x, int y ){
		this.x = x;
		this.y = y;
		r = 2;
		
		
		rad = Math.toRadians(angle);
		speed = 10;
		dx = Math.cos(rad) * speed;
		dy = Math.sin(rad) * speed;
		
		color1 = Color.YELLOW;
	}
	
	//FUNCTIONS
	
	public double getx(){ return x;}
	public double gety(){ return y;}
	public double getr(){ return r;}
	
	public boolean update(){
		x += dx;
		y += dy;
		
		if(x < -r || x > GamePanel.WIDTH + r || 
				y < -r || y > GamePanel.HEIGHT +r){
			return true;
		}
		
		return false;
	}
	
	public void draw (Graphics2D g){
		int lineOffset = 4;
		g.setColor(color1.darker());
		g.drawLine((int) (x - lineOffset), (int) (y - lineOffset), (int) (x + lineOffset), (int) (y + lineOffset));
		g.drawLine((int) (x + lineOffset), (int) (y - lineOffset), (int) (x - lineOffset), (int) (y + lineOffset) );
		g.setColor(color1.brighter());
		g.fillOval((int) (x - r), (int) (y - r - 3), 2 * r, 2 * r + 6);
		g.fillOval((int) (x - r -  3), (int) (y - r), 2 * r +  6, 2 * r);
		g.setColor(Color.white);
		g.fillOval((int)(x - r), (int) (y - r), 2 * r, 2 * r);
	}
}
