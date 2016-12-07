
import java.io.Serializable;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Hitbox implements Serializable{
	private int x, y;
	private int w, h;
	
	public Hitbox(){
		
	}
	
	public Hitbox(int x, int y, int w, int h){
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public Hitbox(Hitbox b){
		this.x=b.x;
		this.y=b.y;
		this.w=b.w;
		this.h=b.h;
	}
	
	public void draw(Graphics g, int xPerso, int yPerso, boolean frappe){
		if(!frappe)
			g.setColor(Color.blue);
		else
			g.setColor(Color.red);
		g.drawRect(x+xPerso, y+yPerso, w, h);
	}
	
	public boolean pointDansBox(int x, int y){
		return !(x < this.x && y < this.y 
				&& x > this.x+this.w && y > this.y + this.h);
	}
	
	public boolean boxToucheBox(Hitbox b){
		return !(this.x > b.x + b.w || this.x + this.w < b.x
				|| this.y > b.y + b.h || this.y + this.h < b.y);
	}
	
	public int getX() {
		return x;
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
}
