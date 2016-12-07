
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class ChoixPersoGameState extends BasicGameState{

	public static final int ID=3;
	private Input input;
	
	///Tableau de boutons (nombre de bouton en fonction de la taille de l'array list
	///Placer les boutons sur une liste, aller à la ligne quand on arrive au bout
	///Clic sur un perso, selectedIndex1=indiceBouton
	///variable choixPerso détermine qui choisis son perso
	///Pas de modification de choix possible, quand perso choisis, afficher tête en bas a gauche/droite
	
	///Attributs:
	///Tableau de boutons pour choisir le perso
	///entier choixPerso qui détermine qui choisis son perso
	///2 images pour afficher les têtes des persos choisis
	///un texte pour dire qui choisi?
	
	private Bouton listePerso[]=null;
	private Bouton btnJouer=null;
	private final static int LARGEUR_BOUTON=100, HAUTEUR_BOUTON=100, ESPACEMENT=50;
	private int choixPerso=1;
	private Image tetePerso1=null, tetePerso2=null;
	private Image background= null;
	
	public void initialisation(ArrayList<Personnage> liste){
		listePerso = new Bouton[liste.size()];
		for(int i=1, j=0; i<=liste.size(); i++){
			listePerso[i-1] = new Bouton(50 + (ESPACEMENT+LARGEUR_BOUTON)*(i-j), 50+(ESPACEMENT+HAUTEUR_BOUTON)*j, LARGEUR_BOUTON, HAUTEUR_BOUTON, StateGame.listePerso.get(i-1).getImageTete());
			if((i-j)/5 >= 1){
				j++;
			}
		}
		btnJouer = new Bouton(StateGame.LARGEUR_FENETRE/2-LARGEUR_BOUTON/2, 500, LARGEUR_BOUTON, HAUTEUR_BOUTON, "Jouer");
		btnJouer.setEnabled(false);
		choixPerso=1;
		tetePerso1=null;
		tetePerso2=null;
	}
	
	@Override
	public void init(GameContainer c, StateBasedGame game) throws SlickException {
		// TODO Auto-generated method stub
		input = c.getInput();
		try{
			background =new Image("src/fond/fond1.png");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void render(GameContainer c, StateBasedGame game, Graphics g) throws SlickException {
		// TODO Auto-generated method stub
		background.draw(0, 0, StateGame.LARGEUR_FENETRE, StateGame.HAUTEUR_FENETRE);
		for(Bouton b : listePerso){
			b.render(g);
		}
		btnJouer.render(g);
		if(tetePerso1!=null)
			tetePerso1.draw(0, StateGame.HAUTEUR_FENETRE-150, 150, StateGame.HAUTEUR_FENETRE, 0, 0, tetePerso1.getWidth(), tetePerso1.getHeight());
		if(tetePerso2!=null)
			tetePerso2.draw(StateGame.LARGEUR_FENETRE-150, StateGame.HAUTEUR_FENETRE-150, StateGame.LARGEUR_FENETRE, StateGame.HAUTEUR_FENETRE, 0, 0, tetePerso2.getWidth(), tetePerso2.getHeight());
	}

	@Override
	public void update(GameContainer c, StateBasedGame game, int delta) throws SlickException {
		// TODO Auto-generated method stub
		boolean found=false;
		int i=0;
		ChoixMapGameState choixMap = (ChoixMapGameState)game.getState(ChoixMapGameState.ID);
		if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
			while(i<StateGame.listePerso.size() && !found){
				found = (listePerso[i].testClic(input.getMouseX(), input.getMouseY()));
				i++;
			}
			i--;
			if(found && listePerso[i].isEnabled()){	
				if(tetePerso1==null){
					StateGame.selectedPerso1=i;
					tetePerso1= StateGame.listePerso.get(i).getImageTete();
					choixPerso=2;
					listePerso[i].setEnabled(false);
				}else if(tetePerso2==null){
					StateGame.selectedPerso2=i;
					tetePerso2= StateGame.listePerso.get(i).getImageTete();
					listePerso[i].setEnabled(false);
					btnJouer.setEnabled(true);
				}
			}else if(btnJouer.testClic(input.getMouseX(), input.getMouseY()) && btnJouer.isEnabled()){
				choixMap.initialisation();
				game.enterState(ChoixMapGameState.ID);
			}
		}else if(input.isKeyPressed(Input.KEY_ESCAPE)){
			game.enterState(MainScreenGameState.ID);
		}
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return ID;
	}

}
