
import java.util.ArrayList;
import java.awt.Font;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MapGameState extends BasicGameState{
	
	public static final int ID=2;
	
	private StateBasedGame game;
	private GameContainer container;
	private Personnage player, player2;
	private PersoHUD hud1, hud2;
	private Controller controle=null, controle2=null;
	private Map map;

	private Sound sonCoup[], sonRecoitCoup, sonDash;
	
	final static double DUREE_COMBAT = 61;
	private double compteur=4;
	
	private Font awtFont = new Font("Times New Roman", Font.BOLD, 60);
	private TrueTypeFont fCompteur = new TrueTypeFont(awtFont, true);
	
	private boolean play=false;
	private boolean started=false;
	
	int numRound=1;
	int roundGagne1=0, roundGagne2=0;
	final static int NB_ROUND_GAGNANT=2;
	
	final static int PHASE_ANNONCE_ROUND=0;
	final static int PHASE_COMPTE_REBOURS=1;
	final static int PHASE_COMBAT=2;
	final static int PHASE_FIN_ROUND=3;
	final static int PHASE_FIN_JEU=4;
	private int phaseJeu=PHASE_ANNONCE_ROUND;
	
	private Font awtFont2 = new Font("Times New Roman", Font.BOLD, 180);
	private TrueTypeFont fCompteRebours = new TrueTypeFont(awtFont, true);
	

	final static int POS_SOL = StateGame.HAUTEUR_FENETRE*3/4;
	final static String CHEMIN_SPRITE = "sprite/Charset.png";
	final static String CHEMIN_MUSIQUE1 = "son/musique1.ogg";
	final static String CHEMIN_MUSIQUE2 = "son/musique2.ogg";
	final static String CHEMIN_MUSIQUE3 = "son/musique3.ogg";
	final static String CHEMIN_SON = "src/son/";
	final static float GRAVITE = (float)0.07;
	
	private Circle circle = new Circle(60, 150, 25);
	public MapGameState() {
		// TODO Auto-generated constructor stub
	}	
	
	@Override
	public void init(GameContainer c, StateBasedGame game) throws SlickException {
		// TODO Auto-generated method stub
		this.game=game;
		
		sonCoup = new Sound[3];
		sonCoup[0] = new Sound(CHEMIN_SON+"coup1.ogg");
		sonCoup[1] = new Sound(CHEMIN_SON+"coup2.ogg");
		sonCoup[2] = new Sound(CHEMIN_SON+"coup3.ogg");
		sonRecoitCoup = new Sound(CHEMIN_SON+"recoitCoup.ogg");
		sonDash = new Sound(CHEMIN_SON+"dash.ogg");
		
		map=null;
		
		container = c;
	}
	
	public void initialisation(Personnage player1, Personnage player2, Map m){
		this.map=StateGame.listeMap.get(StateGame.selectedMap);
		
		this.player=player1;
		this.player2=player2;
		this.player.setAdversaire(this.player2);
		this.player2.setAdversaire(this.player);
		this.player.setSon(sonCoup, sonRecoitCoup, sonDash);
		this.player2.setSon(sonCoup, sonRecoitCoup, sonDash);
		this.player.reset(100, POS_SOL-350);
		this.player2.reset(StateGame.LARGEUR_FENETRE-200, POS_SOL-350);
		this.player.setSens(true);
		
		this.hud1=new PersoHUD(this.player, null, null, 1);
		this.hud2=new PersoHUD(this.player2, null, null, 2);
		
		controle = new Controller(this.player, -1, container.getInput());
		
		int i=0;
		boolean found=false;
		while(i<container.getInput().getControllerCount() && !found){
			found = (container.getInput().getAxisCount(i) > 0);
			i++;
		}
		i--;
		if(found)
			controle2 = new Controller(this.player2, i, container.getInput());
		
		this.phaseJeu = this.PHASE_ANNONCE_ROUND;
		this.compteur=4;
		this.roundGagne1=this.roundGagne2=0;
		this.numRound=1;
		
		this.started=true;
		
	}
	
	@Override
	public void render(GameContainer c, StateBasedGame game, Graphics g) throws SlickException {
		g.setBackground(Color.white);
		map.render(g);
		player.render(g);
		player2.render(g);
		
		hud1.render(g);
		hud2.render(g);

		g.setColor(Color.green);
		
		if(this.roundGagne1 > 0){
			g.fill(new Circle(60, 100, 20));
		}
		if(this.roundGagne1 > 1){
			g.fill(new Circle(120, 100, 20));
		}
		if(this.roundGagne2 > 0){
			g.fill(new Circle(StateGame.LARGEUR_FENETRE-60, 100, 20));
		}
		if(this.roundGagne2 > 1){
			g.fill(new Circle(StateGame.LARGEUR_FENETRE-120, 100, 20));
		}
		
		if(this.phaseJeu==this.PHASE_COMPTE_REBOURS || this.phaseJeu==this.PHASE_COMBAT){
			String txtCompteur=Integer.toString((int)compteur);
			fCompteur.drawString(StateGame.LARGEUR_FENETRE/2-fCompteur.getWidth(txtCompteur)/2, 10, txtCompteur, Color.white);
			
		}else if(this.phaseJeu==this.PHASE_FIN_JEU){
			String txt;
			if(this.roundGagne1 == 2){
				txt="PLAYER 1";
			}else{
				txt="PLAYER 2";
			}
			txt += " WINS !";
			fCompteRebours.drawString(StateGame.LARGEUR_FENETRE/2-fCompteRebours.getWidth(txt)/2, 
					StateGame.HAUTEUR_FENETRE/2-fCompteRebours.getHeight(txt)/2, txt, Color.yellow);			
		}else if(this.phaseJeu == this.PHASE_ANNONCE_ROUND){
			String txtRound = "ROUND " + numRound;
			fCompteRebours.drawString(StateGame.LARGEUR_FENETRE/2-fCompteRebours.getWidth(txtRound)/2, 
					StateGame.HAUTEUR_FENETRE/2-fCompteRebours.getHeight(txtRound)/2, txtRound, Color.yellow);
		}else if(this.phaseJeu==this.PHASE_FIN_ROUND){
			int gagnant=1;
			if(player.getVie()<=0 || player.getVie() < player2.getVie())
				gagnant=2;
			else if(player2.getVie()<=0 || player2.getVie() < player.getVie())
				gagnant=1;
			
			String txtCompteur="GAME OVER\n";
			
			fCompteRebours.drawString(StateGame.LARGEUR_FENETRE/2-fCompteRebours.getWidth(txtCompteur)/2, 
									StateGame.HAUTEUR_FENETRE/2-fCompteRebours.getHeight(txtCompteur)-30, txtCompteur, Color.yellow);
			if(compteur <= 1){
				txtCompteur="TIMES OUT\n";
				fCompteRebours.drawString(StateGame.LARGEUR_FENETRE/2-fCompteRebours.getWidth(txtCompteur)/2, 
				StateGame.HAUTEUR_FENETRE/2-fCompteRebours.getHeight(txtCompteur)/2, txtCompteur, Color.yellow);
			}
			txtCompteur="PLAYER " + gagnant + " WINS !";
			fCompteRebours.drawString(StateGame.LARGEUR_FENETRE/2-fCompteRebours.getWidth(txtCompteur)/2, 
				StateGame.HAUTEUR_FENETRE/2+30, txtCompteur, Color.yellow);
		}	
	}



	@Override
	public void update(GameContainer c, StateBasedGame game, int delta) throws SlickException {
		// TODO Auto-generated method stub
		if(this.phaseJeu == this.PHASE_ANNONCE_ROUND){
			compteur-=(double)delta/1000;
			if(compteur <=2){
				phaseJeu=PHASE_COMPTE_REBOURS;
				compteur=4;
				play=true;
			}
		}else if(this.phaseJeu==this.PHASE_COMPTE_REBOURS){
			compteur-=(double)delta/1000;
			if(compteur<=1){
				phaseJeu=PHASE_COMBAT;
				compteur=this.DUREE_COMBAT;
			}
		}else if(this.phaseJeu==this.PHASE_COMBAT){
			if(play){
				compteur -= (double)delta/1000;
				controle.update(delta);
				if(controle2!=null)
					controle2.update(delta);
				player.update(delta, player2);
				player2.update(delta, player);
				
				if(compteur<=1 || player.getVie() <= 0 || player2.getVie() <= 0){
					play=false;
					phaseJeu=PHASE_FIN_ROUND;
					compteur=4;
					if(player.getVie() < player2.getVie()){
						this.roundGagne2+=1;
					}else{
						this.roundGagne1+=1;
					}
				}
			}	
		}else if(this.phaseJeu==this.PHASE_FIN_ROUND){
			compteur -= (double)delta/1000;
			if(this.roundGagne1 ==2 || this.roundGagne2==2 && compteur < 1){
				this.phaseJeu=this.PHASE_FIN_JEU;
				compteur =0;
			}else if(compteur <1){
				this.numRound+=1;
				this.phaseJeu=this.PHASE_ANNONCE_ROUND;
				compteur=4;
				player.reset(100, POS_SOL-350);
				player2.reset(StateGame.LARGEUR_FENETRE-200, POS_SOL-350);
			}
		}else if(this.phaseJeu==this.PHASE_FIN_JEU){
			compteur += delta;
			if(compteur > 2000){
				this.stop();
				map.pauseMusic();
				game.enterState(MainScreenGameState.ID);
			}
		}
	}
	
	public void keyReleased(int key, char c){
		if(Input.KEY_ESCAPE == key){
			play=false;
			map.pauseMusic();
			game.enterState(MainScreenGameState.ID);
		}else if(Input.KEY_ENTER == key){
			play = !play;
			if(play){
				if(map.getStarted())
					map.getMusic().resume();
				else{
					map.startMusic();
					
				}
				controle.clear();
			}else{
				map.getMusic().pause();
			}
		}
	}
	
	public void keyPressed(int key, char c){
		
	}

	public void resumeGame(){
		if(this.phaseJeu==this.PHASE_COMBAT)
			this.play=true;
		this.map.playMusic();
	}
	
	public boolean getStarted(){
		return started;
	}
	
	public void setStarted(boolean s){
		this.started=s;
	}
	
	public void stop(){
		started=false;
	}
	
	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return ID;
	}
}
