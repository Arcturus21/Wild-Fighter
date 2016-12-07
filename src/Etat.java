

public enum Etat{
	REPOS(0, -1),
	MARCHE(1, -1),
	SAUTE(2, -1),
	COUP_FAIBLE1(3, 400),
	COUP_FAIBLE2(4, 400),
	COUP_FAIBLE3(5, 600),
	COUP_FORT1(6, 500),
	COUP_FORT2(7, 500),
	COUP_FORT3(8, 700),
	DASH(9, 100),
	RECOIT_COUP(10, 500),
	BLOQUE(11, 500);
	
	private int etat;
	private int tempsActivation;
	
	private int DUREE_ETAT;
	
	private final static int NB_ETAT=12;
	
	Etat(){
		
	}
	
	Etat(int etat, int duree){
		this.etat = etat;
		this.DUREE_ETAT = duree;
		this.tempsActivation=0;
	}
	
	public boolean update(int delta){
		//System.out.println(etat);
		tempsActivation+=delta;
		if(tempsActivation >= DUREE_ETAT || DUREE_ETAT == -1){
			return true;
		}
		return false;
	}
	
	public void copie(Etat e){
		this.etat = e.etat;
		this.DUREE_ETAT = e.DUREE_ETAT;
		this.tempsActivation=0;
	}
	
	public void reset(){
		this.tempsActivation=0;
	}
	
	public int getEtat(){
		return etat;
	}
	
	public int getTempsActif(){
		return tempsActivation;
	}
	
	public int getDureeEtat(){
		return DUREE_ETAT;
	}
	
	public static int getNbEtat(){
		return NB_ETAT;
	}
}
