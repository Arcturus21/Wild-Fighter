

public enum CodeAction {
	
	TC_DROITE(0),
	TC_GAUCHE(1),
	TC_SAUT(2),
	TC_COUP_FAIBLE(3),
	TC_COUP_FORT(4),
	TC_DASH(5),
	TC_BLOQUE(6);
	
	private int codeTouche;
	
	CodeAction(int codeTouche)
	{
		this.codeTouche=codeTouche;
	}

	public int getCodeTouche() {
		return codeTouche;
	}

	public void setCodeTouche(int codeTouche) {
		this.codeTouche = codeTouche;
	}
	
}