
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.Color;

public class Bouton {
	private int x, y, w, h;
	private Image image;
	private String texte;
	private boolean enabled;
	
	public Bouton(int x, int y, int w, int h, String texte){
		this.x=x;
		this.y=y;
		this.w=w;
		this.h=h;
		this.texte=texte;
		image=null;
		enabled=true;
	}
	
	public Bouton(int x, int y, int w, int h, Image i){
		this.x=x;
		this.y=y;
		this.w=w;
		this.h=h;
		this.image=i;
		texte=null;
		enabled=true;
	}
	
	public void render(Graphics g){
		if(image != null){
			g.drawImage(this.image, x, y, x+w, y+h, 0, 0, this.image.getWidth(), this.image.getHeight());
		}else{
			g.setColor(Color.orange);
			g.drawString(texte, x+w/6, y+h/2-5);
		}
		g.draw(new Rectangle(x, y, w, h));
		if(!this.enabled){
			g.setColor(new Color(128, 128, 128, 128));
			g.fill(new Rectangle(x, y, w, h));
		}
	}
	
	
	public boolean testClic(int mouseX, int mouseY){
		return ((x < mouseX && x+w > mouseX && y < mouseY && y+h > mouseY) && this.enabled);
	}
	
	//Getters and setters

	public int getX() {
		return x;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getTexte() {
		return texte;
	}

	public void setTexte(String texte) {
		this.texte = texte;
	}
}
