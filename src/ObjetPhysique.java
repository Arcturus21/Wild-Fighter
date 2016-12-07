
import java.io.Serializable;
import java.util.ArrayList;

public class ObjetPhysique implements Serializable{
	protected float x=0,y=0;
	protected int w=100, h=195;
	protected float vx, vy;
	
	protected float poids=Personnage.POIDS;
	
	protected boolean sens=false, enLair=true, isMoving=false, peutDash=true, peutFrapper=true;
	protected boolean peutBouger=true;
	
	public static final double RAPPORT_DELTA = 1.5;
	
	public ObjetPhysique(){
		
	}
	
	public ObjetPhysique(float x, float y, int w, int h, float poids, boolean moveable){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h= h;
		this.vx=0;
		this.vy=0;
		
		this.poids = poids;
		
		this.peutBouger = moveable;
	}
	
	public void update(int delta){
		
	}
	
	boolean testCollision(){
		if(this.x < 0 || this.y < 0 || this.x+w > StateGame.LARGEUR_FENETRE || this.y+h > MapGameState.POS_SOL)
			return false;
		return true;
	}
	
	public void move(int delta){ ///passer liste d'objet en paramètre et tester collision avec tous
		this.x += vx/1000*delta;
		this.y += vy/1000*delta;
				
		if(testCollision()){
			
		}
		else{
			affineX();
			affineY();
		}
	}
	
	public void setSens(boolean s){
		this.sens=s;
	}
	
	public Hitbox getRelHitbox(){
		return new Hitbox((int)this.x, (int)this.y, this.w, this.h);
	}
	
	public void deplacer(int delta){ ///applique simplement les vecteurs
		this.x += vx*delta/10;;
		this.y += vy/1000*(delta*1000);		
	}
	
	void affineX(){
		if(x<0){
			x=0;
			vx=0;
		}	
		else if(x+w>StateGame.LARGEUR_FENETRE){
			x=StateGame.LARGEUR_FENETRE-w;
			vx=0;
		}
	}
	
	void affineY(){
		if(y+h>=MapGameState.POS_SOL-1){
			vy=0;
			y=MapGameState.POS_SOL-h;
			enLair=false;
		}
		else
			enLair=true;
	}
		
	public void addImpulse(float vx, float vy){
		if(peutBouger){
			this.vx+=vx;
			this.vy+=vy;
		}
	}
	
	public float getVx() {
		return vx;
	}

	public void setVx(float vx) {
		this.vx = vx;
		if(vx < 0)
			sens=false;
		else if(vx >0)
			sens=true;
	}

	public float getVy() {
		return vy;
	}

	public void setVy(float vy) {
		this.vy = vy;
	}
}
