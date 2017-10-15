package Main;

import javax.swing.JPanel;

import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;


public class GamePanel extends JPanel implements Runnable, MouseListener, KeyListener {
	
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	private Thread thread;
	
	private BufferedImage image;
	private Graphics2D g;
	private boolean running;
	static double averageFPS;
	
	private GameStateManager gsm;
	private static boolean DEBUG = true;							// DEBUG
	
	public static void LOG(String DEBUG_SENTENCE){
		if(DEBUG) System.out.println(DEBUG_SENTENCE);
	}
	
	
	public GamePanel() {
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
	}
	
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			addKeyListener(this);
			addMouseListener(this);
			thread = new Thread(this);
			thread.start();
		}
	}
	
	// initializes variables
	private void init() {
		
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		running = true;
		
		gsm = new GameStateManager();
	}
	
	// the "main" function
	public void run() {
		
		init();
		
		int FPS = 30;
		int targetTime = 1000 / FPS;
		
		long start;
		long elapsed;
		long wait;
		
		long totalTime = 0;
		
		int frameCount = 0;
		int maxFrameCount = 30;
		
		// simple game loop
		while(running) {
			
			start = System.nanoTime();
			
			update();
			draw();
			drawToScreen();
			
			elapsed = (System.nanoTime() - start) / 1000000;
			
			wait = targetTime - elapsed;
			if(wait < 0) wait = 5;
			
			try {
				Thread.sleep(wait);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
			totalTime += System.nanoTime() - start;
			frameCount++;
			if(frameCount == maxFrameCount){
				averageFPS = 1000.0 / ((totalTime / frameCount) / 1000000);
				frameCount = 0;
				totalTime = 0;
			}
		}
		
	}
	
	// updates the game
	private void update() {
		gsm.update();
	}
	
	// draws the game onto an off-screen buffered image
	private void draw() {
		gsm.draw(g);
	}
	
	// draws the off-screen buffered image to the screen
	private void drawToScreen() {
		Graphics g2 = getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
	}
	
	public void keyTyped(KeyEvent k) {
		
	}
	public void keyPressed(KeyEvent k) {
		gsm.keyPressed(k.getKeyCode());
		LOG("Key Pressed, KeyCode: " + k.getKeyCode() + " Key: " + k.getKeyText(k.getKeyCode()));
	}
	public void keyReleased(KeyEvent k) {
		gsm.keyReleased(k.getKeyCode());
		LOG("Key Released, KeyCode: " + k.getKeyCode() + " Key: " + k.getKeyText(k.getKeyCode()));
	}

	@Override
	public void mouseClicked(MouseEvent m) {
		
	}

	public void mouseEntered(MouseEvent m) {
	
	}

	public void mouseExited(MouseEvent m) {
	
	}
	
	public void mousePressed(MouseEvent m) {
		gsm.mouseClicked(m.getButton(), m.getX(), m.getY());
		LOG("Mouse Clicked" + m.getButton() + " At: (x):" + m.getX() + " (y):" + m.getY());
	}

	public void mouseReleased(MouseEvent m) {
		
	}	
}