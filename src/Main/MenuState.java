package Main;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class MenuState extends GameState{
	
	private ArrayList<String> menuList;
	private Font titleFont, defaultFont;
	FontMetrics metrics;
	private int selectedMenuItem = 0;
	
	public MenuState(GameStateManager gsm){
		this.gsm = gsm;
		
		titleFont = new Font("Century Gothic", Font.BOLD, 36);
		defaultFont = new Font("Century Gothic", Font.PLAIN, 24);
		
		menuList = new ArrayList<String>();
		
		menuList.add("Play wave mode");
		//menuList.add("Play Survival mode");
		menuList.add("Help");
		menuList.add("About");
		menuList.add("Exit");
	}
	
	public void update(){
		
	}
	
	public void draw(Graphics2D g){				//Main draw func	
		g.setColor(Color.GRAY);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		drawBackGrid(g);
		drawMenu(g);
		
	}
	
	private void drawBackGrid(Graphics2D g){	//drawing background grid	
		int spacing = 50;
		g.setColor(Color.GRAY.darker());
		//	draw Vertical lines
		for(int currentVertical = 0; currentVertical < GamePanel.WIDTH; currentVertical+=spacing){
			g.drawLine(currentVertical, 0, currentVertical, GamePanel.HEIGHT);
		}	
		//	draw Horizontal lines
		for(int currentHorizontal = 0; currentHorizontal < GamePanel.HEIGHT; currentHorizontal+=spacing){
			g.drawLine(0, currentHorizontal, GamePanel.WIDTH, currentHorizontal);
		}	
	}
	
	private void drawMenu(Graphics2D g){		//drawing menu
		
		
		//	Background rectangle to delete grid beneath
		g.setColor(Color.GRAY.brighter());
		g.fillRect(GamePanel.WIDTH/4, GamePanel.HEIGHT/6, GamePanel.WIDTH/2+1, GamePanel.HEIGHT/3*2+1);
		
		//Title
		g.setColor(Color.WHITE);
		g.setFont(titleFont);
		g.drawString("NAME OF THE GAME", 
				GamePanel.WIDTH/2-(g.getFontMetrics().stringWidth("NAME OF THE GAME"))/2, 
				GamePanel.HEIGHT/6+36);
		

		metrics = g.getFontMetrics(defaultFont);
		int fontHeight = metrics.getHeight();
	
		for(int i = 0; i < menuList.size(); i++){
			boolean isSelected = false;
			if(i==selectedMenuItem){
				isSelected = true;
			}
			
			drawMenuString(g, menuList.get(i),defaultFont, 150+300/(menuList.size()*2)+fontHeight/2+(300/(menuList.size())*i),isSelected);
			
		}
		
	}
	private void drawMenuString(Graphics2D g, String string, Font font, int y, boolean selected){
		metrics = g.getFontMetrics(font);
		int fontHeight = metrics.getHeight();
		g.setFont(font);
		g.drawString(string, GamePanel.WIDTH/2-(g.getFontMetrics().stringWidth(string))/2, y);
		if(selected){
			g.drawLine(GamePanel.WIDTH/2-(g.getFontMetrics().stringWidth(string))/2-30, y-fontHeight/3, 
					GamePanel.WIDTH/2-(g.getFontMetrics().stringWidth(string))/2-10, y-fontHeight/3);
			g.drawLine(GamePanel.WIDTH/2+(g.getFontMetrics().stringWidth(string))/2+30, y-fontHeight/3, 
					GamePanel.WIDTH/2+(g.getFontMetrics().stringWidth(string))/2+10, y-fontHeight/3);
		}
		
	}
	
	public void keyPressed(int keyCode){
		if(keyCode == KeyEvent.VK_ENTER){
			switch (selectedMenuItem){
			
			case 0:	gsm.setCurrentState(GameStateManager.PLAYWAVESTATE);
					break;
			//case 1://	gsm.setCurrentState(GameStateManager.PLAYSURVIVALSTATE);
			//		break;
			case 1:	//gsm.setCurrentState(GameStateManager.HELPSTATE);
					break;
			case 2:	//gsm.setCurrentState(GameStateManager.ABOUTSTATE);
					break;
			case 3:	System.exit(0);
					break;
			
			}
			
		//	gsm.setCurrentState(GameStateManager.PLAYSTATE);
		}else if(keyCode == KeyEvent.VK_DOWN){
			if(selectedMenuItem < menuList.size()-1){
				selectedMenuItem++;
			}
		}else if(keyCode == KeyEvent.VK_UP){
			if(selectedMenuItem > 0){
				selectedMenuItem--;
			}
		}
	}
	
	public void keyReleased(int keyCode){
		
	}

	@Override
	public void mouseClicked(int mouseButton, int x, int y) {
		// TODO Auto-generated method stub
		
	}
}
