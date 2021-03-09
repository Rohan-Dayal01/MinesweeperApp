/**
 * Rohan Dayal
 * CSE 160
 * Section 1
 * Final Project
 * May 6th, 2020
 */
package minesweeperproject;
import javafx.scene.control.Button;


public class Box extends Button{
	private int row;
	private int column;
	private boolean mine;
	private int numAdj;
	private int numAdjMine;
	private boolean beenClicked=false;
	public Box(int xcoord, int ycoord, boolean mine) {
		super();
		this.row = xcoord;
		this.column= ycoord;
		this.mine =mine;
	}
	public void clickOn() {
		beenClicked=true;
	}
	public boolean getClick() {
		return beenClicked;
	}
	public int getRow() {
		return row;
	}
	public int getCol() {
		return column;
	}
	public int getAdjs() {
		return numAdj;
	}
	public int getAdjMine() {
		return numAdjMine;
	}
	public boolean isMine() {
		return mine;
	}
	public void setAdjs(int adj) {
		this.numAdj = adj;
	}
	public void setAdjMine(int adjMine) {
		this.numAdjMine = adjMine;
	}
	public void setMine(boolean isIt) {
		mine = isIt;
	}
}
