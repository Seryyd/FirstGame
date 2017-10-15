package Main;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class GameStateManager {
	
	private ArrayList<GameState> gameStates;
	private int currentState;
	
	public static final int MENUSTATE = 0;
	public static final int PLAYWAVESTATE = 1;
	
	public GameStateManager() {
			
		gameStates = new ArrayList<GameState>();
		
		gameStates.add(new MenuState(this));
		gameStates.add(new PlayWaveState(this));
		
		currentState = MENUSTATE;
		
	}
	
	public void setCurrentState (int i){
		currentState = i;
	}

	public void update() {
		gameStates.get(currentState).update();
		
	}

	public void draw(java.awt.Graphics2D g) {
		gameStates.get(currentState).draw(g);
		
	}

	public void keyPressed(int keyCode) {
		gameStates.get(currentState).keyPressed(keyCode);
		
	}

	public void keyReleased(int keyCode) {
		gameStates.get(currentState).keyReleased(keyCode);
		
	}
	
	public void mouseClicked(int mouseButton, int x, int y){
		gameStates.get(currentState).mouseClicked(mouseButton, x, y);
	}
}
