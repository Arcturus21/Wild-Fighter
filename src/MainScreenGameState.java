
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MainScreenGameState extends BasicGameState implements MouseListener{

	public static final int ID=1;
	private Image background;
	private StateBasedGame game;
	
	final static int LARGEUR_BOUTON = 75, HAUTEUR_BOUTON = 50;
	private Bouton btnJouer=new Bouton(StateGame.LARGEUR_FENETRE/2-LARGEUR_BOUTON/2, StateGame.HAUTEUR_FENETRE/2-2*HAUTEUR_BOUTON, LARGEUR_BOUTON, HAUTEUR_BOUTON, "Jouer");
	private Bouton btnQuitter=new Bouton(StateGame.LARGEUR_FENETRE/2-LARGEUR_BOUTON/2, StateGame.HAUTEUR_FENETRE/2+2*HAUTEUR_BOUTON, LARGEUR_BOUTON, HAUTEUR_BOUTON, "Quitter");
	private Bouton btnReset = new Bouton(StateGame.LARGEUR_FENETRE/2-LARGEUR_BOUTON/2, StateGame.HAUTEUR_FENETRE/2, LARGEUR_BOUTON, HAUTEUR_BOUTON, "Reset");
	private Input input;
	
	@Override
	public void init(GameContainer c, StateBasedGame g) throws SlickException {
		// TODO Auto-generated method stub
		this.game = g;
		this.background = new Image("fond/fond1.png");
		input = c.getInput();
		
		
		for(int i=0; i<StateGame.listePerso.size(); i++){
			StateGame.listePerso.get(i).initialisation();
		}
		for(int i=0; i<StateGame.listeMap.size(); i++){
			StateGame.listeMap.get(i).initialisation();
		}
	}

	@Override
	public void render(GameContainer c, StateBasedGame game, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		background.draw(0, 0, StateGame.LARGEUR_FENETRE, StateGame.HAUTEUR_FENETRE);
		btnJouer.render(g);
		btnReset.render(g);
		btnQuitter.render(g);
	}

	@Override
	public void update(GameContainer c, StateBasedGame game, int delta) throws SlickException {
		// TODO Auto-generated method stub
		int state;
		MapGameState jeu = (MapGameState)game.getState(MapGameState.ID);
		if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
			if(btnJouer.testClic(input.getMouseX(), input.getMouseY())){
				
				if(!jeu.getStarted()){
					ChoixPersoGameState choix = (ChoixPersoGameState)game.getState(ChoixPersoGameState.ID);
					choix.initialisation(StateGame.listePerso);
					state=ChoixPersoGameState.ID;
				}else{
					state=MapGameState.ID;
					jeu.resumeGame();
				}
				game.enterState(state);
			}else if(btnQuitter.testClic(input.getMouseX(), input.getMouseY())){
				c.exit();
			}else if(btnReset.testClic(input.getMouseX(), input.getMouseY())){
				jeu.setStarted(false);
			}
		}
	}
	
	public void keyPressed(int key, char c){
		
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return ID;
	}


}
