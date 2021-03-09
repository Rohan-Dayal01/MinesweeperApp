/**
 * Rohan Dayal
 * CSE 160
 * Section 1
 * Final Project
 * May 6th, 2020
 */
package minesweeperproject;

import javafx.scene.control.TextField;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.event.EventHandler;


public class MinesweeperGUI extends Application{
	private int rows;//aka height
	private int cols;//aka width
	private int mines;
	TextField heightTF = new TextField();
	TextField widthTF = new TextField();
	TextField mineTF = new TextField();
	Box[][] grid;
	GridPane pane = new GridPane();
	Stage starting = new Stage();
	Stage primaryStage = new Stage();
	private int totalWB=0;
	private int revealedWB=0;
	private boolean arePlaying=true;
	public void start(Stage primaryStage) {
		//starting = new Stage();
		GridPane menu = new GridPane();
		Label heightLabel = new Label("Height");
		Label widthLabel = new Label("Width");
		Label mineLabel = new Label("Mines");
		Button easyLabel = new Button("Easy");
		Button intermediateLabel = new Button("Intermediate");
		Button advancedLabel = new Button("Advanced");
		Button customLabel = new Button("Custom");
		easyLabel.setOnAction(e->setSettings(9,9,10));
		intermediateLabel.setOnAction(e->setSettings(16,16,40));
		advancedLabel.setOnAction(e->setSettings(16,30,99));
		customLabel.setOnAction(e->setSettings(Integer.parseInt(heightTF.getText()), Integer.parseInt(widthTF.getText()),Integer.parseInt(mineTF.getText())));
		menu.setVgap(10);
		menu.add(heightLabel, 1, 0);
		menu.add(widthLabel, 2, 0);
		menu.add(mineLabel, 3, 0);
		menu.add(easyLabel, 0, 1);
		menu.add(intermediateLabel, 0, 2);
		menu.add(advancedLabel, 0, 3);
		menu.add(customLabel, 0, 4);
		menu.add(new Label("9"), 1, 1);
		menu.add(new Label("9"), 2, 1);
		menu.add(new Label("10"), 3, 1);
		menu.add(new Label("16"), 1, 2);
		menu.add(new Label("16"), 2, 2);
		menu.add(new Label("40"), 3, 2);
		menu.add(new Label("16"), 1, 3);
		menu.add(new Label("30"), 2, 3);
		menu.add(new Label("99"), 3, 3);
		menu.add(heightTF, 1, 4);
		menu.add(widthTF,2,4);
		menu.add(mineTF, 3, 4);
		Scene menuScene = new Scene(menu,600,300);
		starting.setScene(menuScene);
		starting.setAlwaysOnTop(true);
		starting.setTitle("Menu");
		starting.show();
		
		//Box[][] grid  =new Box[16][16];//since these are objects, will not make copies, so can still access from this array
		
		
		Scene scene = new Scene(pane,500,500);
		this.primaryStage.setTitle("ShowMinefield");
		this.primaryStage.setScene(scene); 
		this.primaryStage.show();
	}
	public void setSettings(int height, int width, int mines) {
		rows = height;
		cols = width;
		this.mines = mines;
		//NEED TO CHECK FOR NUMBER OF MINES
		grid = new Box[rows][cols];
		for(int x=0;x<rows;x++) {
			for(int y=0;y<cols;y++) {
				Box beta = new Box(x,y,false);//initialize everything to NOT A MINE
				beta.setMinHeight(25);
				beta.setMinWidth(25);
				beta.setStyle("-fx-background-color: gray");
				beta.setStyle("-fx-border-color: black");
				//beta.setBorder(Color.BLACK);
				/*if((x+y)%2==0)
					alpha.setFill(Color.BLACK);
				else
					alpha.setFill(Color.WHITE);
				pane.add(alpha, x, y);*/
				//beta.setOnAction(e->changeColor(beta));
				int theserows= x;
				int thesecols=y;
				beta.setOnMouseClicked(new EventHandler<MouseEvent>() {	 
		            @Override
		            public void handle(MouseEvent event) {
		                MouseButton button = event.getButton();
		                if(button==MouseButton.PRIMARY){
		                   clickAction(beta,theserows,thesecols);
		                }else if(button==MouseButton.SECONDARY){
		                    markAsMine(beta);
		                }/*else if(button==MouseButton.MIDDLE){
		                    label.setText("MIDDLE button clicked");
		                }*/
		            }
		        });
				grid[x][y]=beta;
				pane.add(beta, y, x);//because add goes col, row instead of row,col
			}
		}
		//Now here, we need to get the minecount correct.
		int mcounter=0;
		boolean [][] taken = new boolean[rows][cols];
		int randRow;
		int randCol;
		while(mcounter<mines) {
			randRow = (int)(Math.random()*rows);
			randCol = (int)(Math.random()*cols);
			if(taken[randRow][randCol]==false) {
				taken[randRow][randCol]=true;
				grid[randRow][randCol].setMine(true);
				mcounter++;
			}
		}
		int counter=0;
		for(int x=0;x<rows;x++) {
			for(int y=0;y<cols;y++) {
				/*
				 * A few cases here:
				 * 1. In the middle, so will have 8 boxes adjacent to you
				 * 2. On a corner, so will have 3 boxes adjacent
				 * 3. On the top row, in the middle
				 * 4. On the bottom row, in the middle
				 * 5. On the left column, in the middle
				 * 6. On the right column, in the middle
				 */
				counter=0;//counts the number of surrounding mines
				if(x==0&&y==0) {//top left
					grid[x][y].setAdjs(3);
					if(grid[0].length>1)
						if(grid[0][1].isMine())
							counter++;
					if(grid.length>1)
						if(grid[1][0].isMine())
							counter++;
					if(grid.length>1&&grid[0].length>1)
						if(grid[1][1].isMine())
							counter++;
					grid[x][y].setAdjMine(counter);
				}
				else if(x==0&&y==cols-1) {//top right
					grid[x][y].setAdjs(3);
					if(grid[0][y-1].isMine())
						counter++;
					if(grid.length>1&&cols>1)
						if(grid[1][y-1].isMine())
							counter++;
					if(grid.length>1)
						if(grid[1][y].isMine())
							counter++;
					grid[x][y].setAdjMine(counter);
				}
				else if(x==rows-1&&y==0) {//bottom left
					grid[x][y].setAdjs(3);
					if(grid.length>1)
						if(grid[x-1][y].isMine())
							counter++;
					if(grid.length>1&&grid[x-1].length>1)
						if(grid[x-1][y+1].isMine())
							counter++;
					if(grid[x].length>1)
						if(grid[x][y+1].isMine())
							counter++;
					grid[x][y].setAdjMine(counter);
				}
				else if(x==rows-1&&y==cols-1) {//bottom right corner
					grid[x][y].setAdjs(3);
					if(cols>1)
						if(grid[x][y-1].isMine())
							counter++;
					if(rows>1&&cols>1)
						if(grid[x-1][y-1].isMine())
							counter++;
					if(rows>1)
						if(grid[x-1][y].isMine())
							counter++;
					grid[x][y].setAdjMine(counter);
				}
				else if(x==rows-1&&y!=cols-1&&y!=0) {//on bottom row
					grid[x][y].setAdjs(5);
					if(grid[x][y-1].isMine())
						counter++;
					if(grid[x][y+1].isMine())
						counter++;
					if(grid[x-1][y].isMine())
						counter++;
					if(grid[x-1][y-1].isMine())
						counter++;
					if(grid[x-1][y+1].isMine())
						counter++;
					grid[x][y].setAdjMine(counter);
				}
				else if(x==0) {//on top row
					grid[x][y].setAdjs(5);
					if(grid[x+1][y].isMine())
						counter++;
					if(grid[x][y-1].isMine())
						counter++;
					if(grid[x][y+1].isMine())
						counter++;
					if(grid[x+1][y-1].isMine())
						counter++;
					if(grid[x+1][y+1].isMine())
						counter++;
					grid[x][y].setAdjMine(counter);
				}
				else if(y==0) {//on leftmost column
					grid[x][y].setAdjs(5);
					if(grid[x-1][y].isMine())
						counter++;
					if(grid[x-1][y+1].isMine())
						counter++;
					if(grid[x][y+1].isMine())
						counter++;
					if(grid[x+1][y+1].isMine())
						counter++;
					if(grid[x+1][y].isMine())
						counter++;
					grid[x][y].setAdjMine(counter);
				}
				else if(y==grid[x].length-1) {//on rightmost column
					grid[x][y].setAdjs(5);
					if(grid[x-1][y].isMine())
						counter++;
					if(grid[x-1][y-1].isMine())
						counter++;
					if(grid[x][y-1].isMine())
						counter++;
					if(grid[x+1][y-1].isMine())
						counter++;
					if(grid[x+1][y].isMine())
						counter++;
					grid[x][y].setAdjMine(counter);
				}
				else {//in middle
					grid[x][y].setAdjs(8);
					if(grid[x][y-1].isMine())
						counter++;
					if(grid[x-1][y-1].isMine())
						counter++;
					if(grid[x-1][y].isMine())
						counter++;
					if(grid[x-1][y+1].isMine())
						counter++;
					if(grid[x][y+1].isMine())
						counter++;
					if(grid[x+1][y+1].isMine())
						counter++;
					if(grid[x+1][y].isMine())
						counter++;
					if(grid[x+1][y-1].isMine())
						counter++;
					grid[x][y].setAdjMine(counter);
				}
				if(counter==0&&grid[x][y].isMine()==false) {
					totalWB++;
				}
			}
		}
		starting.close();
	}
	
	public static void main(String[]args) {
		launch(args);
	}
	public void loseGame() {
		Stage gameOver = new Stage();
		VBox overPane = new VBox();
		Label goMessage = new Label("Mine hit. Game over.");
		overPane.getChildren().add(goMessage);
		Scene loseScene = new Scene(overPane,200,200);
		gameOver.setTitle("Game over.");
		gameOver.setScene(loseScene);
		gameOver.show();
		for(int x=0;x<grid.length;x++) {
			for(int y=0;y<grid[x].length;y++) {
				if(grid[x][y].isMine()) {
					grid[x][y].setStyle("-fx-background-color: red");
				}
				else if(grid[x][y].getAdjMine()==0)
					grid[x][y].setStyle("-fx-background-color: white");
				else
					grid[x][y].setText(grid[x][y].getAdjMine()+"");
			}
		}
		arePlaying=false;
		//System.out.println("Total white boxes: " + totalWB);
		//System.out.println("Revealed white boxes: "+ revealedWB);
	}
	public void winGame() {
		for(int x=0;x<grid.length;x++) {
			for(int y=0;y<grid[x].length;y++) {
				if(grid[x][y].isMine()) {
					grid[x][y].setStyle("-fx-background-color: red");
				}
				else if(grid[x][y].getAdjMine()==0)
					grid[x][y].setStyle("-fx-background-color: white");
				else
					grid[x][y].setText(grid[x][y].getAdjMine()+"");
			}
		}
		Stage gameWon = new Stage();
		VBox winPane = new VBox();
		Label winMessage = new Label("Congratulations! You have won!");
		winPane.getChildren().add(winMessage);
		Scene winScene = new Scene(winPane,100,100);
		gameWon.setTitle("Winner!");
		gameWon.setScene(winScene);
		gameWon.show();
		arePlaying=false;
		//this.primaryStage.close();
	}
	public void clickAction(Box b, int r, int c) {
		if(arePlaying) {
		if(b.isMine()) {
			b.setStyle("-fx-background-color: red");
			loseGame();
		}
		else {
			if(b.getClick()!=true) {//if it hasn't been clicked yet
				b.setStyle("-fx-background-color: white");
				b.clickOn();
				if(b.getAdjMine()==0) {
					revealedWB++;//white boxes are non-mine boxes
					if(revealedWB==totalWB) {
						winGame();
						return;
					}
					else {
						revealer(r,c+1);
						revealer(r,c-1);
						revealer(r+1,c);
						revealer(r+1,c-1);
						revealer(r+1,c+1);
						revealer(r-1,c-1);
						revealer(r-1,c);
						revealer(r-1,c+1);//all 8 surroundings are checked for revealing
					}
				}
				else if(b.getAdjMine()!=0) {
					b.setText(b.getAdjMine()+"");
					b.setTextFill(Color.BLUE);
				}
			}
			//Don't do anything to the box if it has already been clicked on
		}
		}
	}
	public void revealer(int r, int c) {
		if(r<0||r>=rows||c<0||c>=cols) {//base case that out of bounds
			return;
		}
		else if(grid[r][c].getClick()==true) {//base case that already clicked on
			return;
		}
		//will not reveal adjacent boxes unless current box is whitespace
		//need to check if the current box is a mine
		else if(grid[r][c].isMine())//current box is a mine base case
			return;
		else if(grid[r][c].getAdjMine()!=0) {//current is not mine, but not blank base case
			grid[r][c].clickOn();
			grid[r][c].setStyle("-fx-background-color: white");
			grid[r][c].setText(grid[r][c].getAdjMine()+"");
			grid[r][c].setTextFill(Color.BLUE);
		}
		else if(grid[r][c].getAdjMine()==0) {//current box is a blank
			grid[r][c].setStyle("-fx-background-color: white");
			revealedWB++;
			grid[r][c].clickOn();
			if(revealedWB==totalWB) {
				winGame();
				return;
			}
			else {
				revealer(r,c+1);
				revealer(r,c-1);
				revealer(r+1,c);
				revealer(r+1,c-1);
				revealer(r+1,c+1);
				revealer(r-1,c-1);
				revealer(r-1,c);
				revealer(r-1,c+1);//all 8 surroundings are checked for revealing
			}
		}
	}
	public void markAsMine(Box b) {
		if(b.getClick()==true) {
			return;
		}
		if(b.getText().equals("X")) {
			b.setText("");
			//b.setTextFill(Color.BLUE);
			b.setStyle("-fx-background-color: lightgrey");
			b.setStyle("-fx-border-color: black");
		}
		else {
			b.setText("X");
			b.setTextFill(Color.RED);
			b.setStyle("-fx-background-color: white");
		}
		
	}
}
