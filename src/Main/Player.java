package Main;
import java.awt.*;

public class Player {

	// FIELDS
	private int x;
	private int y;
	private int r;

	private int dx;
	private int dy;
	private int speed;

	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;

	private boolean firing;
	private long firingTimer;
	private long firingDelay;

	private boolean recovering;
	private long recoveryTimer;

	private int lives;
	private Color color1;
	private Color color2;
	
	private int score;
	
	private int powerLevel;
	//private int power;
	//private long powerTimer;
	private int powerDelay;
	/*private int[] requiredPower = {
			1, 2, 3, 4, 5
	};*/

	// CONSTRUCTOR
	public Player(){
		
		x = GamePanel.WIDTH / 2;
		y = GamePanel.HEIGHT / 2+100;
		r = 5;
		
		
		dx = 0;
		dy = 0;
		speed = 5;
		
		lives = 3;
		color1 = Color.WHITE;
		color2 = Color.RED;
		
		firing = false;
		firingTimer = System.nanoTime();
		firingDelay = 10;
		
		recovering = false ;
		recoveryTimer = 0;
		
		score = 0;
		
		powerLevel = 1;
		//powerTimer = System.nanoTime();
		//powerDelay = 5000;
	}

	// FUNCTIONS

	public int getx() {		return x;	}
	public int gety() {		return y;	}
	public int getr() {		return r;	}
	
	public int getScore() {		return score; }
	
	public int getLives() {		return lives;	}

	public boolean isRecovering(){ return recovering; }

	public void setLeft(boolean b) {		left = b;	}
	public void setRight(boolean b) {		right = b;	}
	public void setUp(boolean b) {		up = b;	}
	public void setDown(boolean b) {		down = b;	}
	
	public void setPower(int p) {powerLevel = p; }
	public void setPowerDelay(int p) {powerDelay = p; }
	public void addPowerDelay(int p) {powerDelay += p; }
	public int getPowerLevel(){ return powerLevel; }
	public int getPowerDelay(){ return powerDelay; }
	
	
	public void setFiring(boolean b) {		firing = b;	}
	
	public void addScore(int i) {	score += i;	}
	
	public void resetScore(){ score = 0;}
	public void resetPower() { powerLevel = 1; }
	
	public void loseLive() {
		lives--;
		recovering = true;
		recoveryTimer = System.nanoTime();
	}
	
	public void gainLive(int i) {
		for(int j = 0; j <i;j++){
		lives++;	
		}	
	}
	

	public void update() {

		if (left) {
			dx = -speed;
		}
		if (right) {
			dx = speed;
		}
		if (up) {
			dy = -speed;
		}
		if (down) {
			dy = speed;
		}

		x += dx;
		y += dy;
		if (x < r)
			x = r;
		if (y < r)
			y = r;
		if (x > GamePanel.WIDTH - r)
			x = GamePanel.WIDTH - r;
		if (y > GamePanel.HEIGHT - r)
			y = GamePanel.HEIGHT - r;

		dx = 0;
		dy = 0;
	
		if(recovering){
			long elapsed = (System.nanoTime() - recoveryTimer) / 1000000;
			if (elapsed > 2000) {
				recovering = false;
				recoveryTimer = 0;
			}
		}
	}

	public void draw(Graphics2D g) {

		if (recovering) {
			g.setColor(color2);
			g.fillOval(x - r, y - r, 2 * r, 2 * r);

			g.setStroke(new BasicStroke(3));
			g.setColor(color2.darker());
			g.drawOval(x - r, y - r, 2 * r, 2 * r);
			g.setStroke(new BasicStroke(1));
		} else {
			g.setColor(color1);
			g.fillOval(x - r, y - r, 2 * r, 2 * r);

			g.setStroke(new BasicStroke(3));
			g.setColor(color1.darker());
			g.drawOval(x - r, y - r, 2 * r, 2 * r);
			g.setStroke(new BasicStroke(1));
		}
	}

	public void shotOnce(int targetX, int targetY) {
		int angle;
		angle = getAngle(targetX, targetY);
		long elapsed = (System.nanoTime() - firingTimer) / 1000000;
		//long elapsedPower = (System.nanoTime() - powerTimer) / 1000000;
		if (elapsed > firingDelay) {
			if(powerLevel == 2 && powerDelay > 0){
				PlayWaveState.bullets.add(new Bullet(angle -5, x - 5, y));
				PlayWaveState.bullets.add(new Bullet(angle +5, x + 5, y));
				powerDelay--;
			}
			else if(powerLevel == 3 && powerDelay > 0){
				PlayWaveState.bullets.add(new Bullet(angle -10, x, y));
				PlayWaveState.bullets.add(new Bullet(angle, x, y));
				PlayWaveState.bullets.add(new Bullet(angle +10, x, y));
				powerDelay--;
			}
			else if(powerLevel == 1){
				PlayWaveState.bullets.add(new Bullet(angle, x, y));
			}
			firingTimer = System.nanoTime();
			
			//powerTimer = System.nanoTime();
			if(powerDelay <= 0){
				powerDelay = 0;
				powerLevel = 1;
				//elapsedPower = 0;
			}
		}
		
	}
	
	public int getAngle(int xt, int yt){
		int angle;
		
		angle = (int )Math.toDegrees(Math.atan2(yt - y, xt - x));
		if(angle < 0){
	        angle += 360;
	    }
		return angle;
	}
}
