package Main;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

public class PlayWaveState extends GameState{

	public static Player player;
	public static ArrayList<Bullet> bullets;
	public static ArrayList<Enemy> enemies;
	public static ArrayList<PowerUp> powerups;
	
	private long waveStartTimer;
	private long waveStartTimerDiff;
	private int waveNumber;
	private boolean waveStart;
	private int waveDelay = 2000;
	private boolean playerAlive;
	private int alphaOld;
	private boolean DEBUG = false; // 					DEBUG 
	
	//private double rnd;
	private int rndInt1, rndInt2;
	private int rndInt3;
	
	public PlayWaveState(GameStateManager gsm){
		this.gsm = gsm;
		
		player = new Player();
		bullets = new ArrayList <Bullet>();
		enemies = new ArrayList <Enemy>();
		powerups = new ArrayList <PowerUp>();
		
		waveStartTimer = 0;
		waveStartTimerDiff = 0;
		waveStart = true;
		waveNumber = 0;
		playerAlive = true;
	}
	
	public void update(){
		
		Random rnd = new Random();
		rndInt1 = rnd.nextInt(3)+ 1;
		rndInt2 = rnd.nextInt(3)+ 1;
		rndInt3 = rnd.nextInt(3)+ 1;
		//rndInt = (int) (rnd);
		// new wave
		if(waveStartTimer == 0 && enemies.size() == 0 ){
			waveNumber++;
			waveStart = false;
			waveStartTimer = System.nanoTime();			
		}else{
			waveStartTimerDiff = (System.nanoTime() - waveStartTimer) / 1000000;
			if (waveStartTimerDiff > waveDelay){
				waveStart = true;
				waveStartTimer = 0;
				waveStartTimerDiff = 0;
			}
		}
		
		// create enemies
		if (waveStart && enemies.size() == 0){
			createNewEnemies();
		}
		
		//player update
		player.update();
		
		//bullet update
		for(int i = 0; i < bullets.size(); i++){
			boolean remove = bullets.get(i).update();
			if(remove){
				bullets.remove(i);
				i--;
			}
		}
		
		// enemy update
		for(int i = 0; i<enemies.size(); i++){
			enemies.get(i).update();
		}
		
		// powerup update
		for(int i = 0; i < powerups.size(); i++){
			boolean remove = powerups.get(i).update();
			if(remove){
				powerups.remove(i);
				i--;
			}
		}
		
		// bullet-enemy collision
		for(int i = 0; i < bullets.size(); i++){
			
			Bullet b = bullets.get(i);
			double bx = b.getx();
			double by = b.gety();
			double br = b.getr();
			
			for(int j = 0; j < enemies.size(); j++){
				
				Enemy e = enemies.get(j);
				double ex = e.getx();
				double ey = e.gety();
				double er = e.getr();
				
				double dx = bx - ex;
				double dy = by - ey;
				double dist = Math.sqrt(dx * dx + dy * dy);
				
				if(dist < br + er){
					e.hit();
					bullets.remove(i);
					i--;
					break;
				}
			}
			
		}
		
		//**check dead enemies
		for(int i = 0; i < enemies.size(); i++){
			if(enemies.get(i).isDead()){
				
				Enemy e = enemies.get(i);
				//int rank = e.getRank();
				//int type = e.getType();
				
				// chance for power up
				double rand = Math.random();
				if(rand < 0.010) powerups.add(new PowerUp(1, e.getx(), e.gety()));
				else if(rand < 0.020) powerups.add(new PowerUp(3, e.getx(), e.gety()));
				else if(rand < 0.120) powerups.add(new PowerUp(2,e.getx(), e.gety()));
				
				
				player.addScore(e.getType() + e.getRank());
				enemies.remove(i);
				i--;
				e.explode(e.getType());
				
			}
		}
		
		// player - enemy collision
		if(!player.isRecovering()){
			int px = player.getx();
			int py = player.gety();
			int pr = player.getr();
			for(int i = 0; i < enemies.size(); i++){
				Enemy e = enemies.get(i);
				double ex = e.getx();
				double ey = e.gety();
				double er = e.getr();
				
				double dx = px - ex;
				double dy = py - ey;
				double dist = Math.sqrt(dx * dx + dy *dy);
				
				if(dist < pr +er){
					if(!DEBUG){
					player.loseLive();
					}
				}
			}
		}
		
		// player-powerups collision
		int px = player.getx();
		int py = player.gety();
		int pr = player.getr();
		for(int i = 0; i < powerups.size(); i++){
			PowerUp p = powerups.get(i);
			double x = p.getx();
			double y = p.gety();
			double r = p.getr();
			double dx = px - x;
			double dy = py - y;
			double dist = Math.sqrt(dx * dx + dy * dy);
		
			
			//** collected power up
			if(dist < pr + r){
				player.addScore(1);
				int type = p.getType();
				
				if(type == 1){
					player.gainLive(1);
					player.addScore(10);
				} 
				if(type == 2){
					if(player.getPowerLevel() < type){
					player.setPower(2);
					player.setPowerDelay(20);
					}else if(player.getPowerLevel() == type){
						player.addPowerDelay(20);
					}else if(player.getPowerLevel() > type){
						player.addPowerDelay(5);
					}
				}
				if(type == 3){
					player.setPower(3);
					player.addPowerDelay(10);
				}
				
				powerups.remove(i);
				i--;
				
			}/**/
			
		}/**/
		
		
		// player dead check
		if(player.getLives() <= 0){
			playerAlive = false;
			enemies.clear();
			bullets.clear();
			powerups.clear();
			waveNumber = 0;	
			waveStartTimer = 0;
			player.gainLive(3);
			player.resetScore();
			player.resetPower();
			
			
		}
	}
	
	public void draw(Graphics2D g){
		
		// draw background
		//g.setColor(new Color(0, 100, 255));
		g.setColor(Color.GRAY.darker());
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		
		//draw power ups text
		if(player.getPowerLevel() > 1){
			g.setColor(Color.WHITE);
			g.setFont(new Font("Century Gothic", Font.PLAIN, 17));
			String s = "- POWER SHOTS LEFT: " + player.getPowerDelay() + "-";
			int lenght = (int) g.getFontMetrics().getStringBounds(s,  g).getWidth();
			g.drawString(s, GamePanel.WIDTH / 2 - lenght / 2, 30);			
		}
		
		//** DEBUG//////////////////////
		g.setColor(Color.BLACK);
		g.setFont(new Font ("Century Gothic", Font.PLAIN, 10));
		
		g.drawString("RNDINT: " + rndInt1 +"  "+ rndInt2 + "  " + rndInt3+ " | Bullets: " + bullets.size() + " | FPS: " + (int)GamePanel.averageFPS + " | EnemyCount: " + enemies.size(), 10, GamePanel.HEIGHT -30);
		g.drawString("powerLevel: " + player.getPowerLevel() + "  powerDelay: " + player.getPowerDelay() + " | WaveNumber: " + waveNumber, 10, GamePanel.HEIGHT -15);
		
		
		// draw player
		player.draw(g);
		
		// draw bullets
		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).draw(g);
		}
		
		// draw enemies
		for(int i = 0; i<enemies.size(); i++){
			enemies.get(i).draw(g);
		}
		
		// draw powerups
		for(int i = 0; i < powerups.size(); i++){
			powerups.get(i).draw(g);
		}
		
		//draw wave number
		if(waveStartTimer != 0){
			g.setFont(new Font ("Century Gothic", Font.PLAIN, 18));
			String s = "- W A V E  " + waveNumber + "  -";
			int lenght = (int) g.getFontMetrics().getStringBounds(s,  g).getWidth();
			int alpha = (int) (255 * Math.sin(3.14 * waveStartTimerDiff / waveDelay));
			if(alpha > 255) alpha = 255;
			g.setColor(new Color(255, 255, 255, alpha));
			g.drawString(s, GamePanel.WIDTH / 2 - lenght / 2, GamePanel.HEIGHT / 2);
		}
		
		// draw player lives
		for(int i = 0; i < player.getLives(); i++){
			g.setColor(Color.WHITE);
			g.fillOval(20 + (20*i), 20, player.getr() * 2, player.getr() * 2);
			g.setStroke(new BasicStroke(3));
			g.setColor(Color.WHITE.darker());
			g.drawOval(20 + (20*i), 20, player.getr() * 2, player.getr() * 2);
			g.setStroke(new BasicStroke(1));
		}
		
		//draw player score
		g.setColor(Color.WHITE);
		g.setFont(new Font("Century Gothic", Font.PLAIN, 14));
		g.drawString("Score: " + player.getScore(), GamePanel.WIDTH - 100, 30);
		
		//draw game over
		if(!playerAlive){
			g.setFont(new Font("Century Gothic", Font.PLAIN, 20));
			
			int alpha = (int) (255 * Math.sin(3.14 * waveStartTimerDiff / waveDelay));
			String s = "- G A M E   O V E R -";
			int lenght = (int) g.getFontMetrics().getStringBounds(s,  g).getWidth();
			int height = (int) g.getFontMetrics().getStringBounds(s,  g).getHeight();
			g.setColor(new Color(255, 255, 255));
			g.drawString(s, GamePanel.WIDTH / 2 - lenght / 2, GamePanel.HEIGHT / 2 - height - 5);
			if(alpha == 0 && alphaOld != 0){
				playerAlive = true;
			}
			 alphaOld = alpha;
		}
		
		if(player.isRecovering()){
			
			g.setColor(new Color(255,255,255,60));
			g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
			
		}
	}
	
private void createNewEnemies(){
		
		enemies.clear();
		//Enemy e;
		//rnd = (System.nanoTime() + Math.random()) / 1000000000; //Math.random() * 3;
		
		
		//rndInt = rndInt;
		
//		if(waveNumber == 1){
//			for(int i = 0; i < 4; i++){
//				enemies.add(new Enemy(1, 1));
//			}
//		}
//		else if(waveNumber == 2){
//			for(int i = 0; i < 4; i++){
//				enemies.add(new Enemy(1, 1));
//			}
//			for(int i = 0; i < 4; i++){
//				enemies.add(new Enemy(2, 1));
//			}
//		}else{
			for(int i = 0; i < waveNumber * 2; i++){
				if(rndInt1 >= 1 && rndInt1 <=3){
				enemies.add(new Enemy(rndInt1, rndInt2));
				enemies.add(new Enemy(rndInt2, rndInt3));
				enemies.add(new Enemy(rndInt3, rndInt1));
				}else{
					enemies.add(new Enemy(1, 1));
				//}
			}
		}
	}

	private void killEnemies() {
		for(int i = 0; i < enemies.size(); i++){
			enemies.get(i).kill();
		}
	}

	public void keyPressed(int keyCode){
		if(keyCode == KeyEvent.VK_ESCAPE){
			gsm.setCurrentState(GameStateManager.MENUSTATE);
		}if(keyCode == KeyEvent.VK_LEFT  || keyCode == KeyEvent.VK_A){
			player.setLeft(true);
		}
		if(keyCode == KeyEvent.VK_RIGHT  || keyCode == KeyEvent.VK_D){
			player.setRight(true);
		}
		if(keyCode == KeyEvent.VK_UP  || keyCode == KeyEvent.VK_W){
			player.setUp(true);
		}
		if(keyCode == KeyEvent.VK_DOWN  || keyCode == KeyEvent.VK_S){
			player.setDown(true);
		}
	
		if(keyCode == KeyEvent.VK_Q){
			killEnemies();
		}
	
	}
	


	public void keyReleased(int keyCode){
		if(keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A){
			player.setLeft(false);
		}
		if(keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D){
			player.setRight(false);
		}
		if(keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W){
			player.setUp(false);
		}
		if(keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S){
			player.setDown(false);
		}
		if(keyCode == KeyEvent.VK_SPACE ){// shoot up SPACEBAR
			player.shotOnce(player.getx(), player.gety() - 10);
		}
	
	}

	
	public void mouseClicked(int mouseButton, int x, int y) {
		if(mouseButton == MouseEvent.BUTTON1){
			player.shotOnce(x,y);
		}
		
	}
}
