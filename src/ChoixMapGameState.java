
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class ChoixMapGameState extends BasicGameState{

	public static final int ID=4;
	//2 boutons précédent et suivant
	//1 bouton "jouer"
	//1 image et 1 musique pour représenter la map
	
	private Input input;
	
	private Bouton btnPrecedent=null, btnSuivant=null;
	private Bouton btnJouer = null;
	private Image fond = null;
	private Music music = null;
	
	private final static int LARGEUR_BOUTON=150, HAUTEUR_BOUTON=50, ESPACEMENT=50;
	private int selectedMap=0;
	
	@Override
	public void init(GameContainer c, StateBasedGame game) throws SlickException {
		// TODO Auto-generated method stub
		input = c.getInput();
		btnPrecedent = new Bouton(0, StateGame.HAUTEUR_FENETRE/2-HAUTEUR_BOUTON/2, LARGEUR_BOUTON, HAUTEUR_BOUTON, "<< Precedent");
		btnSuivant = new Bouton(StateGame.LARGEUR_FENETRE-LARGEUR_BOUTON, StateGame.HAUTEUR_FENETRE/2-HAUTEUR_BOUTON/2, LARGEUR_BOUTON, HAUTEUR_BOUTON, "Suivant >>");
		btnJouer = new Bouton(StateGame.LARGEUR_FENETRE/2-LARGEUR_BOUTON/2, StateGame.HAUTEUR_FENETRE-HAUTEUR_BOUTON, LARGEUR_BOUTON, HAUTEUR_BOUTON, "Jouer");
	}
	
	public void initialisation(){
		fond = StateGame.listeMap.get(selectedMap).getFond();
		music = StateGame.listeMap.get(selectedMap).getMusic();
		music.play();
	}

	@Override
	public void render(GameContainer c, StateBasedGame game, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		fond.draw(0, 0, StateGame.LARGEUR_FENETRE, StateGame.HAUTEUR_FENETRE, 0, 0, fond.getWidth(), fond.getHeight());
		btnPrecedent.render(g);
		btnSuivant.render(g);
		btnJouer.render(g);
	}

	@Override
	public void update(GameContainer c, StateBasedGame game, int delta) throws SlickException {
		// TODO Auto-generated method stub
		MapGameState jeu = (MapGameState)game.getState(MapGameState.ID);
		if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
			if(btnSuivant.testClic(input.getMouseX(), input.getMouseY())){
				StateGame.selectedMap++;
				StateGame.selectedMap%=StateGame.listeMap.size();
				fond=StateGame.listeMap.get(StateGame.selectedMap).getFond();
				music=StateGame.listeMap.get(StateGame.selectedMap).getMusic();
				music.play();
			}
			else if(btnPrecedent.testClic(input.getMouseX(), input.getMouseY())){
				StateGame.selectedMap--;
				if(StateGame.selectedMap<0){
					StateGame.selectedMap=StateGame.listeMap.size()-1;
				}
				fond=StateGame.listeMap.get(StateGame.selectedMap).getFond();
				music=StateGame.listeMap.get(StateGame.selectedMap).getMusic();
				music.play();
			}
			else if(btnJouer.testClic(input.getMouseX(), input.getMouseY()) && btnJouer.isEnabled()){
				music.stop();
				jeu.initialisation(StateGame.listePerso.get(StateGame.selectedPerso1), StateGame.listePerso.get(StateGame.selectedPerso2), null);
				jeu.resumeGame();
				game.enterState(MapGameState.ID);
			}
		}else if(input.isKeyPressed(Input.KEY_ESCAPE)){
			music.pause();
		game.enterState(MainScreenGameState.ID);
		}
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return ID;
	}

}
