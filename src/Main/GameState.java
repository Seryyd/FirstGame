package Main;

public abstract class GameState {
	
	protected GameStateManager gsm;
	
	public abstract void update();
	public abstract void draw(java.awt.Graphics2D g);
	public abstract void keyPressed(int keyCode);
	public abstract void keyReleased(int keyCode);
	public abstract void mouseClicked(int mouseButton, int x, int y);
	
}
