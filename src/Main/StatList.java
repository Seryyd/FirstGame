package Main;
import java.awt.*;

public class StatList {
	
	//FIELDS
	private int score;
	private int topScore;
	private int waveNumber;
	private int enemiesOnScreen;
	private int enemiesKillCount;
	private int powerUpLevel;
	private double averageFPS;
	private int lives;
	
	public StatList(){
	//CONSTRUCTOR
	enemiesKillCount = 0;
	enemiesOnScreen = 0;
	}
	
	public int getScore() { return score; }
	
	public void addScore(int i) { score += i; }
	public void enemyKillCount(){
		enemiesKillCount++;
	}
	public void enemyCount(int i) { enemiesOnScreen = i; }
}
