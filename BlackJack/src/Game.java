import java.util.*;
import javax.swing.*;
import java.awt.*;

public class Game {
    private  Player winner,
    				loser,
    				inTurn;
   
    public Game (){
    	this.winner = new Player();
    	this.loser = new Player();
    	this.inTurn = new Player();
    }
    
    public void setImages(JLabel cardImageLabel, String path){
    	try{
		Image card = new ImageIcon(this.getClass().getResource(path)).getImage();
		cardImageLabel.setIcon(new ImageIcon(card));
    	}
    	catch(Exception e){
    		System.out.println("the invalid path: " + path);
    	}
    }
    
    public void clearImage(JLabel imageLabel){
    	imageLabel.setIcon(null);
    }   
     
	public  Card[] generateCards(){
		int[] values = {2,3,4,5,6,7,8,9,10};
		int Ace = 1, 
	            King = 11,      
	            Queen = 12,       
	            Jack = 13;
		int[] royalValues = {Ace, King, Queen,Jack};
		String [] royalNames = {"Ace","King","Queen","Jack"};
		Card [] cards = new Card[52];
		String[] suits = {"Club", "Heart","Diamond","Spade"};
		int counter = 0;
		for(int i = 0; i < values.length;i++){
			for(int j = 0; j < suits.length;j++){
				cards[counter] = new Card(suits[j],values[i],"/" + suits[j] + "_" + values[i] + ".png");
				counter++;
			}
		}
		System.out.println("\n\n");
		for(int i =0; i < royalValues.length; i++){
			for(int j = 0; j < suits.length;j++){
				cards[counter] = new Card(suits[j],royalValues[i],"/" + royalNames[i] + "_" + suits[j] + ".png");
				counter++;
			}
		}	
		return cards;
	}
	
	public void setPlayerInTurn(Player p){
		this.inTurn = p;
	}
	
	public void setLoser(Player p){
		loser = p;
	}
	
	public void setWinner(Player p){
		winner = p;
	}
	
	public Player getLoser(){
		return loser;
	}
	
	public Player getWinner(){
		return winner;
	}
	
	public Player getPlayerInTurn(){
		return inTurn;
	}
	
	public  Deck shuffle(Card[] cards){

		Deck gameDeck = new Deck(52);
		 Random rnd = new Random();
		    for (int i = cards.length - 1; i > 0; i--)
		    {
		      int index = rnd.nextInt(i + 1);
		      Card tempCard = cards[index];
		      cards[index] = cards[i];
		      cards[i] = tempCard;
		    }
			for(int i =cards.length-1; i >=0; i--){
				gameDeck.push(cards[i]);
			}
			return gameDeck;
		}
}


