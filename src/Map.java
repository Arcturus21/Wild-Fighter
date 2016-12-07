
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

public class Map {
	private Image fond=null;
	private final int POS_SOL;
	private Music music=null;
	private String cheminFond, cheminMusic;
	private boolean started=false;
	
	public Map(String cheminFond, String cheminMusic){
		this.cheminFond=cheminFond;
		this.cheminMusic=cheminMusic;
		this.POS_SOL=MapGameState.POS_SOL;
	}
	
	public Map(String cheminFond, String cheminMusic, int posSol) throws SlickException{
		POS_SOL=posSol;
		fond = new Image(cheminFond);
		music = new Music(cheminMusic);
	}
	
	public void initialisation(){
		try{
			fond = new Image(this.cheminFond);
			System.out.println(this.cheminMusic);
			music = new Music(this.cheminMusic);	
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(this.cheminFond);
		}
		
	}
	
	public void render(Graphics g){
		fond.draw(0, 0, StateGame.LARGEUR_FENETRE, StateGame.HAUTEUR_FENETRE);
		
		g.setColor(Color.black);
		g.drawLine(0, POS_SOL, StateGame.LARGEUR_FENETRE, POS_SOL);
	}
	
	public void startMusic(){
		music.loop();
		started=true;
	}
	
	public void stopMusic(){
		music.stop();
		started=false;
	}
	
	public void pauseMusic(){
		music.pause();
	}
	
	public void playMusic(){
		music.resume();
	}
	
	public Music getMusic(){
		return music;
	}
	
	public boolean getStarted(){
		return started;
	}
	
	public Image getFond(){
		return fond;
	}
	
	public String getCheminFond(){
		return cheminFond;
	}
}
