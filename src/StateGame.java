
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class StateGame extends StateBasedGame{
	
	final static int LARGEUR_FENETRE = 1280;
	final static int HAUTEUR_FENETRE = 720;
	
	public static ArrayList<Personnage> listePerso = new ArrayList<Personnage>();
	public static int selectedPerso1 = 0;
	public static int selectedPerso2 = 1;
	public static ArrayList<Map> listeMap = new ArrayList<Map>();
	public static int selectedMap = 0;
	
	public StateGame() {
		super("Wild Fighter");
		
		listePerso.add(new Personnage("src/sprite/charset_tortue.png", "src/sprite/tete_tortue.png", 1));
		listePerso.add(new Personnage("src/sprite/mouton.png", "src/sprite/mouton_tete.png", 1));
		listePerso.add(new Personnage("src/sprite/gorille.png", "src/sprite/gorille_tete.png", 1));
		listePerso.add(new Personnage("src/sprite/Charset_grand.png", "src/sprite/tete1.png", 1));
		/*listeMap.add(new Map("src/prototype2/fond/fond1.png", "src/prototype2/son/musique1.wav"));
		listeMap.add(new Map("src/prototype2/fond/fond2.png", "src/prototype2/son/musique2.wav"));
		listeMap.add(new Map("src/prototype2/fond/fond3.png", "src/prototype2/son/musique3.wav"));
		listeMap.add(new Map("src/prototype2/fond/fond4.png", "src/prototype2/son/musique4.wav"));*/
		listeMap.add(new Map("src/fond/fond1.png", "src/son/musique1.ogg"));
		listeMap.add(new Map("src/fond/fond2.png", "src/son/musique2.ogg"));
		listeMap.add(new Map("src/fond/fond3.png", "src/son/musique3.ogg"));
		listeMap.add(new Map("src/fond/fond4.png", "src/son/musique4.ogg"));
	}

	@Override
	public void initStatesList(GameContainer c) throws SlickException {
		// TODO Auto-generated method stub
		addState(new MainScreenGameState());
		addState(new MapGameState());
		addState(new ChoixPersoGameState());
		addState(new ChoixMapGameState());
	}
	
	public static void main(String[] args){
		try{
			AppGameContainer c = new AppGameContainer(new StateGame(), LARGEUR_FENETRE, HAUTEUR_FENETRE, false);
			c.start();
		}catch(SlickException e){
			e.printStackTrace();
		}
	}
		
}
