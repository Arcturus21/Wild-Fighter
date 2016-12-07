
import org.newdawn.slick.Input;

public class Controller {

	private int typeController;
	private int configTouche[];
	private float vx;
	private int nbTouche=7;
	private Input input;
	private Personnage perso;
	
	public Controller(Personnage p,int typeControle, Input input)
	{
		this.typeController=typeControle;
		this.perso=p;
		this.input=input;
		vx=0;
		configTouche= new int[nbTouche];
		if(typeController==-1){
			configTouche[CodeAction.TC_DROITE.getCodeTouche()]=Input.KEY_RIGHT;
			configTouche[CodeAction.TC_GAUCHE.getCodeTouche()]=Input.KEY_LEFT;
			configTouche[CodeAction.TC_SAUT.getCodeTouche()]=Input.KEY_UP;
			configTouche[CodeAction.TC_COUP_FAIBLE.getCodeTouche()]=Input.KEY_A;
			configTouche[CodeAction.TC_COUP_FORT.getCodeTouche()]=Input.KEY_Z;
			configTouche[CodeAction.TC_DASH.getCodeTouche()]=Input.KEY_SPACE;
			configTouche[CodeAction.TC_BLOQUE.getCodeTouche()]=Input.KEY_E;	
		}
		else{
			configTouche[CodeAction.TC_SAUT.getCodeTouche()]=5;
			configTouche[CodeAction.TC_COUP_FAIBLE.getCodeTouche()]=4;
			configTouche[CodeAction.TC_COUP_FORT.getCodeTouche()]=6;
			configTouche[CodeAction.TC_DASH.getCodeTouche()]=9;
			configTouche[CodeAction.TC_BLOQUE.getCodeTouche()]=8;
		}
		
		
	}
	
	public void update(int delta)
	{
		if(typeController==-1)
		{
			if(input.isKeyPressed(configTouche[CodeAction.TC_SAUT.getCodeTouche()]))
			{
				perso.saut();
			}
			else if(input.isKeyPressed(configTouche[CodeAction.TC_DASH.getCodeTouche()]))
			{
				perso.dash(); 
			}
			else if (input.isKeyPressed(configTouche[CodeAction.TC_BLOQUE.getCodeTouche()]))
			{
				perso.bloque(); 
			}
			else if (input.isKeyPressed(configTouche[CodeAction.TC_COUP_FAIBLE.getCodeTouche()]))
			{
				perso.coupFaible();
			}
			else if (input.isKeyPressed(configTouche[CodeAction.TC_COUP_FORT.getCodeTouche()]))
			{
				perso.coupFort();
			}
			
			if(input.isKeyDown(configTouche[CodeAction.TC_DROITE.getCodeTouche()]))
			{
				vx=1;
			}
			else if (input.isKeyDown(configTouche[CodeAction.TC_GAUCHE.getCodeTouche()]))
			{
				vx=-1; 
			}
			else if(!input.isKeyDown(configTouche[CodeAction.TC_DROITE.getCodeTouche()]) && !input.isKeyDown(configTouche[CodeAction.TC_GAUCHE.getCodeTouche()]))
			{
				vx=0;

			}
			
		}
		else{
			if(input.isControlPressed(configTouche[CodeAction.TC_SAUT.getCodeTouche()], typeController)){
				perso.saut();
			}
			else if(input.isControlPressed(configTouche[CodeAction.TC_DASH.getCodeTouche()], typeController)){
				perso.dash();
			}
			else if(input.isControlPressed(configTouche[CodeAction.TC_BLOQUE.getCodeTouche()], typeController)){
				perso.bloque();
			}
			else if(input.isControlPressed(configTouche[CodeAction.TC_COUP_FAIBLE.getCodeTouche()], typeController)){
				perso.coupFaible();
			}
			else if(input.isControlPressed(configTouche[CodeAction.TC_COUP_FORT.getCodeTouche()], typeController)){
				perso.coupFort();
			}
			
			if(input.isControllerLeft(typeController) || getAxisValue(typeController, 1) < -0.5){
				vx=-1;
			}
			else if(input.isControllerRight(typeController)|| getAxisValue(typeController, 1) > 0.5){
				vx=1;
			}
			else if(!input.isControllerLeft(typeController) && !input.isControllerRight(typeController)){
				vx=0;
			}
		}
		
		
		perso.controleDeplacement(vx, delta);
	}
	
	private float getAxisValue(int controller, int axis){
		if(input.getControllerCount() > controller)
		{
			if(input.getAxisCount(controller) > axis){
				return input.getAxisValue(controller, axis);
			}
		}
		return 0;
	}
	
	public float getVx(){
		return vx;
	}
	
	public void clear(){
		input.clearControlPressedRecord();
		input.clearKeyPressedRecord();
		input.clearMousePressedRecord();
	}

}
