
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

public class Sprite{
	
	private Animation anim[][] = new Animation[2][Etat.getNbEtat()];
	private int nbFrameEtat[];
	private final int LARGEUR_FRAME=100, HAUTEUR_FRAME=200;
	private int largeur = LARGEUR_FRAME, hauteur = HAUTEUR_FRAME;
	
		
	public Sprite(String charset, int type) throws SlickException{
		init(charset);
		if(type==2){
			largeur = HAUTEUR_FRAME;
			hauteur = LARGEUR_FRAME;
		}
	}
	
	public void init(String charset) throws SlickException{
		SpriteSheet spriteSheet = new SpriteSheet(charset, LARGEUR_FRAME, HAUTEUR_FRAME);
		
		this.anim[0][0] = loadAnimation(spriteSheet, 0, 1, 0, 100);
		this.anim[0][1] = loadAnimation(spriteSheet, 0, 3, 1, 100);
		this.anim[0][2] = loadAnimation(spriteSheet, 1, 2, 0, 100);
		this.anim[0][3] = loadAnimation(spriteSheet, 0, 1, 2, 100);
		this.anim[0][4] = loadAnimation(spriteSheet, 1, 2, 2, 100);
		this.anim[0][5] = loadAnimation(spriteSheet, 2, 3, 2, 100);
		this.anim[0][6] = loadAnimation(spriteSheet, 0, 1, 2, 100);
		this.anim[0][7] = loadAnimation(spriteSheet, 1, 2, 2, 100);
		this.anim[0][8] = loadAnimation(spriteSheet, 2, 3, 2, 100);
		this.anim[0][9] = loadAnimation(spriteSheet, 2, 3, 0, 100);
		this.anim[0][10] = loadAnimation(spriteSheet, 1, 2, 3, 100);
		this.anim[0][11] = loadAnimation(spriteSheet, 0, 1, 3, 100);
		
		this.anim[1][0] = loadAnimation(spriteSheet, 5, 6, 0, 100);
		this.anim[1][1] = loadAnimation(spriteSheet, 3, 6, 1, 100);
		this.anim[1][2] = loadAnimation(spriteSheet, 4, 5, 0, 100);
		this.anim[1][3] = loadAnimation(spriteSheet, 5, 6, 2, 100);
		this.anim[1][4] = loadAnimation(spriteSheet, 4, 5, 2, 100);
		this.anim[1][5] = loadAnimation(spriteSheet, 3, 4, 2, 100);
		this.anim[1][6] = loadAnimation(spriteSheet, 5, 6, 2, 100);
		this.anim[1][7] = loadAnimation(spriteSheet, 4, 5, 2, 100);
		this.anim[1][8] = loadAnimation(spriteSheet, 3, 4, 2, 100);
		this.anim[1][9] = loadAnimation(spriteSheet, 3, 4, 0, 100);
		this.anim[1][10] = loadAnimation(spriteSheet, 4, 5, 3, 100);
		this.anim[1][11] = loadAnimation(spriteSheet, 5, 6, 3, 100);
	}
	
	private Animation loadAnimation(SpriteSheet spriteSheet, int startX, int endX, int y, int duree){
		Animation animation = new Animation();
		for(int x = startX; x< endX; x++){
			animation.addFrame(spriteSheet.getSprite(x, y), duree);
		}
		
		return animation;
	}
	
	public void update(int delta){
		
	}
	
	
	public void render(Graphics g, int etat, int sens, int x, int y) throws SlickException{
		//g.drawAnimation(anim[sens][etat], x, y);
		anim[sens][etat].draw(x, y, largeur, hauteur);
	}
}
