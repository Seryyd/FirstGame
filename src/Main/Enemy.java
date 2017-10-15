package Main;
import java.awt.*;

public class Enemy {
	
	//FIELDS
	private double x;
	private double y;
	private int r;
	
	private double dx;
	private double dy;
	private double rad;
	private double speed;
	
	private int health;
	private int type;
	private int rank;
	
	private Color color1;
	
	private boolean ready;
	private boolean dead;
	
	//CONSTRUCTOR
	public Enemy(int type, int rank){
		
		this.type = type;
		this.rank  = rank;
		
		//default enemy
		if (type == 1){
			color1 =new Color(0, 0, 255, 170);
			if (rank == 1){
				speed = 2;
				r = 5;
				health = 1;
			}	
			if (rank == 2){
				speed = 2;
				r = 10;
				health = 2;
			}	
			if (rank == 3){
				speed = 1.5;
				r = 20;
				health = 3;
			}	
		}
		// fast default
		if (type == 2){
			color1 =new Color(255, 0, 0, 170);
			if (rank == 1){
				speed = 3;
				r = 4;
				health = 1;
			}	
			if (rank == 2){
				speed = 3.5;
				r = 8;
				health = 1;
			}	
			if (rank == 3){
				speed = 4;
				r = 16;
				health = 2;
			}	
		}
		// tank default
		if (type == 3){
			color1 =new Color(100, 100, 100, 220);
			if (rank == 1){
				speed = 1;
				r = 7;
				health = 2;
			}	
			if (rank == 2){
				speed = 0.8;
				r = 17;
				health = 3;
			}	
			if (rank == 3){
				speed = 0.5;
				r = 25;
				health = 5;
			}	
			
		}		
		x = Math.random() * GamePanel.WIDTH / 2 + GamePanel.WIDTH / 4;
		y = -r;
		
		double angle = Math.random() * 140 + 20;
		rad = Math.toRadians(angle);
		
		dx = Math.cos(rad) * speed;
		dy = Math.sin(rad) * speed;
		
		ready = false;
		dead = false;
	}
	
	//FUNCTIONS
	public double getx(){ return x;}
	public double gety(){ return y;}
	public double getr(){ return r;}
	
	public int getType(){ return type;}
	public int getRank(){ return rank;}
	
	public boolean isDead() { return dead;}
	
	public void hit (){
		health--;
		if(health <= 0){
			dead = true;
		}
	
	}
	public void explode(int type){
		
		if (rank > 1) {
			int amount = 0;
			if (type  == 1) {
				amount = 3;
			}
			else if (type == 2) {
				amount = 2;
			}
			else if (type == 3) {
				amount = 1;
			}else amount = 4;
			for (int i = 0; i < amount; i++) {
				Enemy e = new Enemy(getType(), getRank() - 1);
				e.x = this.x;
				e.y = this.y;
				double angle = 0;
				if(!ready){
					angle = Math.random() * 140 +20;
				}else{
					angle = Math.random() * 360;
				}
				e.rad = Math.toRadians(angle);
				PlayWaveState.enemies.add(e);
			}
		}
		
	}
	
	public void update(){
		
		x += dx;
		y += dy;
		
		if(!ready){
			if(x > r && x < GamePanel.WIDTH - r && 
					y > r && y < GamePanel.HEIGHT -r){
				ready = true;
			}
		}
		
		if(x < r && dx < 0) dx = -dx;
		if(y < r && dy < 0) dy = -dy;
		if(x > GamePanel.WIDTH - r && dx > 0) dx = -dx;
		if(y > GamePanel.HEIGHT - r && dy >0) dy = -dy;
	}
	public void draw (Graphics2D g){
		g.setColor(color1);
		g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
		
		g.setStroke(new BasicStroke(3));
		g.setColor(color1.darker());
		g.drawOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
		g.setStroke(new BasicStroke(1));
	}

	public void kill() {
		dead=true;
		
	}

}
