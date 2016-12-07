
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class PersoHUD {
	private Personnage cible;
	private int num;
	//private Image barreVie, barreEnergie;
	
	private static final int P_BAR_X = StateGame.LARGEUR_FENETRE/50;
	private static final int P_BAR_Y = StateGame.HAUTEUR_FENETRE/50;
	private static final int LARGEUR_BARRE = StateGame.LARGEUR_FENETRE/3;
	private static final int HAUTEUR_BARRE = StateGame.HAUTEUR_FENETRE/40;
	
	public PersoHUD(Personnage p, String cheminBarreVie, String cheminBarreEnergie, int numPlayer){
		this.cible = p;
		this.num = numPlayer;
		/*try{
			this.barreEnergie= new Image(cheminBarreEnergie);
			this.barreVie= new Image(cheminBarreVie);	
		}catch(Exception e){
			e.printStackTrace();
		}*/
		
	}
	
	public void render(Graphics g){
		int posX;
		int sens;
		if(num==1){
			posX=P_BAR_X;
			sens=1;
		}
		else{
			posX=StateGame.LARGEUR_FENETRE-P_BAR_X;
			sens=-1;
		}
		g.resetTransform();
		/*g.drawImage(barreVie, P_BAR_X, P_BAR_Y);
		g.drawImage(barreEnergie, P_BAR_X, P_BAR_Y+15);*/
		g.setColor(Color.red);
		g.fillRect(posX, P_BAR_Y, (float) (cible.getVie()*LARGEUR_BARRE*sens), HAUTEUR_BARRE);
		g.setColor(Color.blue);
		g.fillRect(posX, P_BAR_Y+HAUTEUR_BARRE+5, (float) (cible.getEnergie()*LARGEUR_BARRE*sens), HAUTEUR_BARRE);
	}
}
