
import java.io.Serializable;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class Personnage extends ObjetPhysique implements Serializable{
	
	private Etat etat, nextEtat;
	private Sprite sp=null;
	private String cheminSprite = MapGameState.CHEMIN_SPRITE;
	private String cheminTete = null;
	private Image imageTete=null;
	
	transient private Sound sonCoup[]=null, sonRecoitCoup=null, sonDash=null;
	
	transient Personnage adversaire=null;
	
	
	private double vie=1.0, energie=1.0;
	private boolean peutPrendreDegat = true;
	private int direction=0;
	private float degat=0;
	private int type=1;
	
	private Hitbox box[][][]=null;
	
	private final static float SAUT = (float)-2.2;
	private final static float MAX_VY = (float)0.9;
	public final static float POIDS = (float)0.07; //changer de 0.01
	
	private final static float VWALK = (float)5;//0.8;
	private final static float INERTIE_SAUT = (float)0.75;
	private final static float VDASH = (float)35;
	private final static float CONSO_DASH = (float)0.35;
	
	private final static float REGEN_ENERGIE = (float)0.0001;
			
	public Personnage(){
		super();
	}
	
	public String toString(){
		String str;
		
		str = cheminSprite + "\n";
		str += vie + " " + energie + "\n";
		if(box == null)
			str+="null\n"; 
		
		return str;
	}
	
	///méthode d'initialisation après la désérialization
	public void initialisation(){
		etat = Etat.REPOS;
		nextEtat=Etat.REPOS;
		try{
			if(cheminSprite != null)
				sp = new Sprite(cheminSprite, this.type);
			imageTete = new Image(cheminTete);
		}catch(SlickException se){
			se.printStackTrace();
		}
		adversaire = null;
		sonCoup=null;
		sonRecoitCoup=null;
		sonDash=null;
	}
	
	public void setSon(Sound sonCoup[], Sound sonRecoitCoup, Sound sonDash){
		this.sonCoup=sonCoup;
		this.sonRecoitCoup=sonRecoitCoup;
		this.sonDash=sonDash;
	}
	
	public Personnage(String charset, String cheminTete, int type){
		super();
		cheminSprite=charset;
		etat=Etat.REPOS;
		nextEtat=Etat.REPOS;
		box = loadDefaultHitbox(w, h);
		this.cheminTete=cheminTete;
		this.type=type;
	}

	
	public Personnage(float x, float y, int w, int h, String charset, Sound sonCoup[], Sound sonRecoitCoup, Sound sonDash){
		
		super(x, y, w, h, POIDS, true);
		
		box = loadDefaultHitbox(w, h);
		try {
			//spriteTemp = new Image(charset);
			sp = new Sprite(charset, this.type);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
		etat = Etat.REPOS;
		nextEtat = Etat.REPOS;

		this.peutPrendreDegat = true;
		this.sonCoup=sonCoup;
		this.sonRecoitCoup=sonRecoitCoup;
		this.sonDash=sonDash;
	}
	
	public Personnage(Personnage p){
		this.x = p.x;
		this.y = p.y;
		this.w = p.w;
		this.vx = p.vy;
		this.poids = p.poids;
		this.sens = p.sens;
		this.enLair = p.enLair;
		this.peutPrendreDegat = p.peutPrendreDegat;
		this.peutBouger = p.peutBouger;
		this.etat = p.etat;
		this.nextEtat = p.nextEtat;
		this.direction = direction;
	}
	
	public void reset(int x, int y){
		this.vie=1.0;
		this.energie=1.0;
		this.etat = Etat.REPOS;
		this.nextEtat=Etat.REPOS;
		this.x=x;
		this.y=y;
		this.peutPrendreDegat=true;
		this.enLair=true;
	}
	
	public Hitbox[][][] loadDefaultHitbox(int w, int h){
		int widthFrappe = (int)(w/1.5), heightFrappe = (int)(h/1.5);
		Hitbox defaultCarac = new Hitbox(0,0,w,h);
		Hitbox defaultHitRight = new Hitbox(w*3/4, h*1/8, widthFrappe, heightFrappe);
		Hitbox defaultHitLeft = new Hitbox(-w*3/7, h*1/8, widthFrappe, heightFrappe);
		
		Hitbox[][][] b = new Hitbox[Etat.getNbEtat()][2][2];
		for(int i=0; i<Etat.getNbEtat(); i++){
			b[i][0][0] = defaultCarac;
			b[i][0][1] = defaultCarac;
		}
		for(int i=Etat.COUP_FAIBLE1.getEtat(); i<=Etat.DASH.getEtat(); i++){
			b[i][1][0] = defaultHitRight;
			b[i][1][1] = defaultHitLeft;
		}
		
		return b;
	}
	
	public void render(Graphics g) throws SlickException{
		int sensBox;
		if(sens)
			sensBox=0;
		else
			sensBox=1;
		sp.render(g, etat.getEtat(), sensBox, (int)x, (int)y);
		/*box[etat.getEtat()][0][sensBox].draw(g, (int)x, (int)y, false);
		if(box[etat.getEtat()][1][sensBox]!=null){
			box[etat.getEtat()][1][sensBox].draw(g, (int)x, (int)y, true);
		}*/
	}
	
	///change l'état de façon sécurisée
	public boolean setEtat(Etat e){
		/*si l'état est passable (saut, repos, marche) ou si l'état est finit, 
		 *on change l'état directement 
		 */
		boolean retour;
		
		
		if(etat.getDureeEtat() == -1 || etat.getDureeEtat() <= etat.getTempsActif() || e.getEtat() == Etat.RECOIT_COUP.getEtat()){

			etat = e;
			nextEtat=Etat.REPOS;
			etat.reset();
			retour= true;
		}
		
		/*sinon on le met en attente jusqu'à ce que l'autre état soit fini*/
		else{
			nextEtat = e;
			retour= false;
		}
		
		
		return retour;
	}
	
	public void controleDeplacement(float vx, int delta){
		///Détermine le sens de déplacement du personnage et s'il doit bouger
		///Vérifie que le perso peut changer de sens en fonction de l'ETAT
		if(this.checkEtatDCB()){
			if(vx < 0)
				sens=false;
			else if(vx>0)
				sens=true;
			if(vx==0){
				isMoving=false;
			}
			else{
				isMoving=true;			
			}	
		}
		
	}
	
	public void actualiserEtat(int delta){
		///met l'état à jour (l'échange avec nextEtat)
		///met à jour certains booléens
		if(etat.update(delta)){
			
			if(etat.getEtat() == Etat.BLOQUE.getEtat() || etat.getEtat() == Etat.RECOIT_COUP.getEtat()){
				peutPrendreDegat=true;
			}
			
			etat=nextEtat;
			nextEtat=Etat.REPOS;
			etat.reset();
			if(enLair){
				setEtat(Etat.SAUTE);
			}
			else if(isMoving){
				setEtat(Etat.MARCHE);
			}
			else{
				setEtat(Etat.REPOS);
			}
			etat.reset();

			if(etat.getEtat()==Etat.COUP_FAIBLE1.getEtat() || etat.getEtat()==Etat.COUP_FORT1.getEtat()){
				sonCoup[0].play();
			}
			else if(etat.getEtat()==Etat.COUP_FAIBLE2.getEtat() || etat.getEtat()==Etat.COUP_FORT2.getEtat()){
				sonCoup[1].play();
			}
			else if(etat.getEtat()==Etat.COUP_FAIBLE3.getEtat() || etat.getEtat()==Etat.COUP_FORT3.getEtat()){
				sonCoup[2].play();
			}
				
			if(!enLair && this.checkEtatDCB() && energie >= CONSO_DASH){
				peutDash=true;
			}
		}
		if(etat.getEtat()==Etat.DASH.getEtat())
		{
			this.hitOponent();
		}
		
	}
	
	public void preparerVecteur(int delta){		
		///modifie les vecteurs de déplacement en fonction des booléens et ETAT
		///s'occupe de vx/vy, de la vitesse, de la gravité
		int sensDeplac = ((sens==true) ? 1 : -1);
		
		if(!isMoving){
			vx=0;
		}
		else{
			if(this.checkEtatDC()){
				if(!enLair){
					vx=VWALK*sensDeplac;
				}
				else{
					vx = VWALK*INERTIE_SAUT*sensDeplac;
				}	
			}else if(etat.getEtat() == Etat.DASH.getEtat()){
				vx = VDASH*sensDeplac;
			}
			
		}
		
		if(enLair && (this.checkEtatDCB() || etat.getEtat() == Etat.RECOIT_COUP.getEtat())){
			//factoriser la condition dans une fonction boolean peutTomber();
			vy+=POIDS*delta/10;
			if(vy>MAX_VY){
				vy=MAX_VY;
			}
		}
		else{
			vy=0;
		}
		
	}
	
	
	public boolean testPosition(ObjetPhysique o, float vx, float vy){
		///test la prochaine position supposée du personnage
		///en fonction de ses vecteurs et d'un objet (plateforme) de référence
		Personnage testObj = new Personnage(this);
		testObj.x += vx;
		testObj.y += vy;
		
		return !this.getRelHitbox().boxToucheBox(o.getRelHitbox());
	}
	
	
	void affine(ObjetPhysique o, float vx, float vy){
		///si les déplacements du perso sont impossible
		///on cherche à affiner les vecteurs de déplacements
		///pour que le déplacement soit possible par rapport à l'objet de référence
		
		if(!testPosition(o, vx, 0)){
			if(x+w > o.x && vx > 0){
				x=o.x-w;
				vx=0;
			}
			else if(x < o.x + o.w && vx < 0){
				x= o.x + o.w;
				vy=0;
			}
		}
		if(!testPosition(o, vx, vy)){
			if(y+h > o.y && vy > 0){
				y=o.y-h;
				vy=0;
				enLair=false;
			}
			else if(y < o.y + o.h && vy < 0){
				y= o.y + o.h;
				vy=0;
			}
		}
	}
	
	
	
	void testCollisionTemp(int delta){
		///TEMPORAIRE : test si le perso ne sort pas de l'écran ni ne dépasse la barre noire de ref
		if(!super.testCollision()){
			super.affineX();
			super.affineY();
		}
	}
	
	public void update(int delta, Personnage p2){
		energie+=REGEN_ENERGIE*delta;
		if(energie>1)
			energie=1;
		
		
		actualiserEtat(delta);		
		preparerVecteur(delta);
		testCollisionTemp(delta);
		deplacer(delta);
	
	}
	
	void saut(){
		if(!enLair){
			this.addImpulse(0, SAUT);
			enLair=true;
			setEtat(Etat.SAUTE);
		}
	}
	
	void dash(){
		if(checkEtatDCB() && peutDash){
			int sensDeplac = ((sens==true) ? 1 : -1);
			this.addImpulse(VDASH*sensDeplac, 0);
			setEtat(Etat.DASH);
			isMoving=true;
			peutDash=false;
			energie-=CONSO_DASH;
			degat=(float)0.10;
			this.sonDash.play();
		}
		
	}
	
	void bloque(){
			peutPrendreDegat=false;
			isMoving=false;
			peutDash=false;
			setEtat(Etat.BLOQUE);		
	}
	
	void recoitCoup(float degat){
		vie-=degat;
		if(vie<0)
			vie=0;
		peutPrendreDegat=false;
		isMoving=false;
		peutDash=false;
		this.sonRecoitCoup.play();
		setEtat(Etat.RECOIT_COUP);
	}
	
	void coupFaible(){
		if(etat.getEtat() == Etat.COUP_FAIBLE1.getEtat() || etat.getEtat() == Etat.COUP_FORT1.getEtat()){
			this.setEtat(Etat.COUP_FAIBLE2);
			degat=(float)0.04;
		}
		else if(etat.getEtat() == Etat.COUP_FAIBLE2.getEtat() || etat.getEtat() == Etat.COUP_FORT2.getEtat()){
			this.setEtat(Etat.COUP_FAIBLE3);
			degat=(float)0.06;
		}
		else if(checkEtatDB()){
			if(this.setEtat(Etat.COUP_FAIBLE1))
				sonCoup[0].play();
			degat=(float)0.04;
		}
		isMoving=false;
		this.hitOponent();
	}
	
	void coupFort(){
		if(etat.getEtat() == Etat.COUP_FAIBLE1.getEtat() || etat.getEtat() == Etat.COUP_FORT1.getEtat()){
			this.setEtat(Etat.COUP_FORT2);
			degat=(float)0.05;
		}
		else if(etat.getEtat() == Etat.COUP_FAIBLE2.getEtat() || etat.getEtat() == Etat.COUP_FORT2.getEtat()){
			this.setEtat(Etat.COUP_FORT3);
			degat=(float)0.07;
		}
		else{
			if(this.setEtat(Etat.COUP_FORT1))
				sonCoup[0].play();
			degat=(float)0.05;
		}
		isMoving=false;
		this.hitOponent();
	}
	
	public void hitOponent(){	
		if(this.etat.getEtat() >= Etat.COUP_FAIBLE1.getEtat() && this.etat.getEtat() <= Etat.DASH.getEtat()){
			Hitbox h1=this.getActualHitbox(1);
			Hitbox h2=adversaire.getActualHitbox(0);
			if(adversaire.peutPrendreDegat && h1.boxToucheBox(h2)){
				adversaire.recoitCoup(this.getDegat());
			}
		}	
	}
	
	boolean checkEtatDB(){
		return !(etat.getEtat() == Etat.DASH.getEtat()
				|| etat.getEtat() == Etat.BLOQUE.getEtat());
	}
	
	boolean checkEtatDCB(){
		return !(etat.getEtat() >= Etat.COUP_FAIBLE1.getEtat());
	}
	
	boolean checkEtatDC(){
		return (etat.getEtat() < Etat.COUP_FAIBLE1.getEtat() || etat.getEtat() > Etat.DASH.getEtat());
	}
		
	public Hitbox getRelHitbox(){		
		return new Hitbox((int)this.x, (int)this.y, this.w, this.h);
	}
	
	public Hitbox getActualHitbox(int frappe){
		int sensDeplac;
		if(sens)
			sensDeplac=0;
		else
			sensDeplac=1;
		Hitbox b = new Hitbox(box[etat.getEtat()][frappe][sensDeplac]);
		if(b!=null){
			b.setX(b.getX()+(int)this.x);
			b.setY(b.getY()+(int)this.y);
		}
		return b;
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
	}

	public Etat getEtat() {
		return etat;
	}

	public Etat getNextEtat() {
		return nextEtat;
	}

	public void setNextEtat(Etat nextEtat) {
		this.nextEtat = nextEtat;
	}

	public double getVie() {
		return vie;
	}

	public void setVie(double vie) {
		this.vie = vie;
	}

	public double getEnergie() {
		return energie;
	}

	public void setEnergie(double energie) {
		this.energie = energie;
	}
	
	public float getDegat(){
		return degat;
	}
	
	public void setAdversaire(Personnage p){
		this.adversaire=p;
	}
	
	public Personnage getAdversaire(){
		return this.adversaire;
	}
	
	public Image getImageTete(){
		return imageTete;
	}
}
