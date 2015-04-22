import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.JSpinner;
import javax.swing.JEditorPane;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import java.awt.Color;
import java.util.logging.*;
import java.awt.CardLayout;

import javax.swing.JPanel;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
public class BJGui implements Runnable {

	
	private static  JFrame frame;
	private static JPanel playerEnter,
						dealerEnter,
						playerHand,
						resultPanel,
						ultimateWinner;
	private static JTextArea playerName,
							dealerName;
	private static JLabel playerHandLabel, dealerLabel, playerLabel,
						  playerScore, statusLabel, winnerName,
						  loserName, reasonLabel, winnerBalance, loserBalance,winnerFinal,loserFinal,
						  initCardOne,initCardTwo,newCardImage;
	private static JButton dealerNext, playerNext, playerStay, playerHit,
							resetGame;
	private static Game blackjack;
    private static Card[] cards;
    private static Deck gameDeck;
    private static Player playerOne, playerTwo;

	
	public BJGui() {
		initialize();
	}

	public static void initCards(){
		cards = blackjack.generateCards();
		gameDeck = blackjack.shuffle(cards);
	}

	public static void displayResults(Player winner, Player loser, String reason){
		String how = "";
		switch(reason){
		case "overScore":
			how = " wins!! Other player broke 21.";
			break;
		case "blackJack":
				how = " got blackjack!!";
		break;
		case "higherScore":
			how = " wins by higher score.";
			break;
		}
		reasonLabel.setText(winner.getName()+ how);
		winnerName.setText(winner.getName());
		winnerBalance.setText(winner.getBalance()+"");
		loserName.setText(loser.getName());
		loserBalance.setText(loser.getBalance()+"");
	}
	
	
	public static void checkScore(int score){
		if(score>21){
			determineWinner("overScore");
		}
		if(score==21){
			determineWinner("blackJack");
		}
	}

	
	public static void hit(){
		Card hitCard= gameDeck.pop();
		blackjack.setImages(newCardImage, hitCard.getPath());
		checkCardValue(hitCard);
		blackjack.getPlayerInTurn().updateScore(hitCard.getValue());
		playerScore.setText(blackjack.getPlayerInTurn().getScore()+"");
		checkScore(blackjack.getPlayerInTurn().getScore());
	}

	
	public static void determineWinner(String status){
		Player winner = new Player(),loser = new Player();
		
		if(!checkBalance()){
			return;
		}		
		playerHand.setVisible(false);
		resultPanel.setVisible(true);
		
		switch(status){
		case "higherScore": 
			winner = playerOne.getScore() > playerTwo.getScore() ? playerOne:playerTwo;
			loser = playerOne.getScore() < playerTwo.getScore() ? playerOne:playerTwo;
			blackjack.setLoser(loser);
			blackjack.setWinner(winner);
			blackjack.getWinner().deposit();
			blackjack.getLoser().deduct();
		break;
		case "overScore" :
		winner = playerOne.getScore() > 21 ? playerTwo: playerOne;
		loser  = playerOne.getScore() <=21 ? playerTwo:playerOne;
		blackjack.setLoser(loser);
		blackjack.setWinner(winner);
		blackjack.getWinner().deposit();
		blackjack.getLoser().deduct();
		break;
		case "blackJack":
			if(playerOne.hasBlackJack()){
				winner = playerOne;
				loser = playerTwo;
				blackjack.setLoser(playerTwo);
				blackjack.setWinner(playerOne);
				playerOne.deposit();
				playerTwo.deduct();
			}
			if(playerTwo.hasBlackJack()){
				winner = playerTwo;
				loser = playerOne;
				blackjack.setLoser(playerOne);
				blackjack.setWinner(playerTwo);
				playerOne.deduct();
				playerTwo.deposit();
			}
			break;
		default:System.out.println("bad winner case. return.");return;
		}
		displayResults(winner, loser, status);
	}

	public static void stay(){	
			blackjack.clearImage(newCardImage);
			if(blackjack.getPlayerInTurn().isDealer()){
				determineWinner("higherScore");
			}
			else{
				if(playerOne.isDealer()){
					blackjack.setPlayerInTurn(playerOne);
					initHand(playerOne);
				}
				if(playerTwo.isDealer()){
					blackjack.setPlayerInTurn(playerTwo);
					initHand(playerTwo);
				}
			}
 }
	
	public static void swapRoles(){
		boolean rolePlayerOne = playerOne.isDealer() ? false:true;
		playerOne.setRole(rolePlayerOne);
		playerTwo.setRole(!rolePlayerOne);
		if(playerOne.isDealer()){
			blackjack.setPlayerInTurn(playerTwo);
		}
		if(playerTwo.isDealer()){
			blackjack.setPlayerInTurn(playerOne);
		}
	}
	
	public static boolean checkBalance(){
		Player winner = playerOne.getBalance()<=0 ? playerTwo:playerOne,
			   loser = playerTwo.getBalance() <=0?  playerTwo:playerOne;
		
		if(playerOne.getBalance()<=0 ||playerTwo.getBalance() <=0){
			winnerFinal.setText(winner.getName() + " Final balance: " + winner.getBalance());
			loserFinal.setText(loser.getName() + " Final balance: " + loser.getBalance());
			return false;
		}
		else{
			return true;
		}
	}
	
	public static void shuffle(){
		if(gameDeck.size()<=5){
			gameDeck = blackjack.shuffle(cards);
			}
	}
	
	public static void showFinalResults(){
		resultPanel.setVisible(false);
		ultimateWinner.setVisible(true);
		winnerFinal.setText(blackjack.getWinner().getName() + "  Final Balance: " + blackjack.getWinner().getBalance());
		loserFinal.setText(blackjack.getLoser().getName() + "  Final Balance: " + blackjack.getLoser().getBalance());
	}
	
	public static void resetGame(){
		if(!checkBalance()) {
			showFinalResults();
			return;
		}
		clear();
		if(gameDeck.size()<=5){
			shuffle();
			}
		resultPanel.setVisible(false);
		playerHand.setVisible(true);
		playerOne.resetScore();
		playerTwo.resetScore();
		swapRoles();
		blackjack.clearImage(initCardOne);
		blackjack.clearImage(initCardTwo);
		blackjack.clearImage(newCardImage);
		initHand(blackjack.getPlayerInTurn());
	}
	
	public static void checkCardValue(Card c1){
		if(c1.getValue()>10){
			c1.setValue(10);
			return;
		}
		if(c1.getValue()==1||c1.getValue()==11){
			String input = JOptionPane.showInputDialog("You have drawn an ace. Would you like to use this as a a.1 \n b.11");
			switch(input.charAt(0)){
			case 'a':
				c1.setValue(1);
				break;
			case 'b':
				c1.setValue(11);
				break;
			default:
				//JOptionPane.showMessageDialog(frame, "Invalid response. This card will count for nothing.");
				c1.setValue(0);
				break;
			}
		}
		else{
			c1.setValue(c1.getValue());
		}
	}
	
	public static void clear(){
		playerScore.setText("");
		statusLabel.setText("");
		winnerName.setText("");
		loserName.setText("");
		reasonLabel.setText("");
		winnerBalance.setText("");
		loserBalance.setText("");
	}
	
	public static void initHand(Player p){
		String role = p.isDealer() ?  "Dealer":"Player";
		statusLabel.setText(p.getName() + ", the  " + role);
		Card c1 = gameDeck.pop(),
			 c2 = gameDeck.pop();
		checkCardValue(c1);
		checkCardValue(c2);
		String initialHand = c1.getSuit() + "_ " + c1.getValue() + ", " + c2.getSuit() + "__" + c2.getValue();
		blackjack.setImages(initCardOne,c1.getPath());
		blackjack.setImages(initCardTwo, c2.getPath());
		p.updateScore(c1,c2);
		playerScore.setText(p.getScore() + "");
		checkScore(blackjack.getPlayerInTurn().getScore());
	}
	
 	synchronized public void initialize() {
 		Log log = new Log();
		frame = new JFrame("BlackJack");
		frame.setBounds(100, 100, 756, 496);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));

		
		playerEnter = new JPanel();
		frame.getContentPane().add(playerEnter, "name_421012245654947");
		playerEnter.setLayout(null);

		
		playerLabel = new JLabel("Player please enter your name: ");
		playerLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		playerLabel.setBounds(193, 98, 254, 89);
		playerEnter.add(playerLabel);
	
		
		playerName = new JTextArea();
		playerName.setFont(new Font("Monospaced", Font.ITALIC, 20));
		playerName.setBounds(238, 198, 149, 31);
		playerEnter.add(playerName);

		
		JButton quitButton = new JButton("QUIT");
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		quitButton.setFont(new Font("Tahoma", Font.BOLD, 15));
		quitButton.setBounds(525, 348, 131, 42);
		playerEnter.add(quitButton);


		
	    playerNext = new JButton("NEXT");
		playerNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				playerEnter.setVisible(false);
				dealerEnter.setVisible(true);
				playerOne = new Player(playerName.getText(), false);
				blackjack.setPlayerInTurn(playerOne);
			}
		});
		playerNext.setFont(new Font("Tahoma", Font.BOLD, 15));
		playerNext.setBounds(238, 338, 179, 52);
		playerEnter.add(playerNext);
		
		
		 dealerEnter = new JPanel();
		frame.getContentPane().add(dealerEnter, "name_421014919210519");
		dealerEnter.setLayout(null);
		
		dealerLabel = new JLabel("Dealer please enter your name: ");
		dealerLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		dealerLabel.setBounds(193, 98, 254, 89);
		dealerEnter.add(dealerLabel);
	;
		
		
		dealerName = new JTextArea();
		dealerName.setFont(new Font("Monospaced", Font.ITALIC, 20));
		dealerName.setBounds(235, 182, 149, 31);
		dealerEnter.add(dealerName);

		
		dealerNext = new JButton("NEXT");
		dealerNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dealerEnter.setVisible(false);
				playerHand.setVisible(true);
				playerTwo = new Player(dealerName.getText(),true);
				blackjack.setPlayerInTurn(playerOne);
				initHand(playerOne);
			}
		});
		dealerNext.setFont(new Font("Tahoma", Font.BOLD, 15));
		dealerNext.setBounds(228, 338, 179, 52);
		dealerEnter.add(dealerNext);
		
		JButton dealerQuit = new JButton("QUIT");
		dealerQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		dealerQuit.setFont(new Font("Tahoma", Font.BOLD, 15));
		dealerQuit.setBounds(525, 348, 131, 42);
		dealerEnter.add(dealerQuit);
		
		playerHand = new JPanel();
		frame.getContentPane().add(playerHand, "name_421017807619820");
		playerHand.setLayout(null);
		
	    playerHandLabel = new JLabel("Initial Hand:");
		playerHandLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		playerHandLabel.setBounds(19, 11, 105, 61);
		playerHand.add(playerHandLabel);
		
		 playerStay = new JButton("STAY");
		 playerStay.setName("The STAY Button");
		 playerStay.addMouseListener(log);
		 playerStay.addMouseMotionListener(log);
		playerStay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stay();
			}
		});
		playerStay.setBounds(178, 347, 138, 48);
		playerHand.add(playerStay);
		
		playerHit = new JButton("HIT");
		playerHit.setName("HIT BUTTON");
		playerHit.addMouseListener(log);
		playerHit.addMouseMotionListener(log);
		playerHit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hit();
			}
		});
		playerHit.setBounds(377, 347, 138, 48);
		playerHand.add(playerHit);
		
		JLabel SCORE = new JLabel("Score: ");
		SCORE.addMouseListener(log);
		SCORE.addMouseMotionListener(log);
		SCORE.setFont(new Font("Tahoma", Font.BOLD, 20));
		SCORE.setHorizontalAlignment(SwingConstants.CENTER);
		SCORE.setBounds(484, 145, 115, 39);
		playerHand.add(SCORE);
		
		playerScore = new JLabel("");
		playerScore.setName("Score Label");
		playerScore.addMouseListener(log);
		playerScore.addMouseMotionListener(log);
		playerScore.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 25));
		playerScore.setHorizontalAlignment(SwingConstants.CENTER);
		playerScore.setBounds(609, 133, 121, 61);
		playerHand.add(playerScore);
		
		statusLabel = new JLabel("");
		statusLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		statusLabel.setBounds(473, -2, 267, 53);
		playerHand.add(statusLabel);
		
		JLabel lblNewCard = new JLabel("New Card:");
		lblNewCard.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewCard.setBounds(31, 194, 93, 76);
		playerHand.add(lblNewCard);
		
		JButton gameQuit = new JButton("QUIT");
		gameQuit.addMouseListener(log);
		gameQuit.addMouseMotionListener(log);
		gameQuit.setName("QUIT BUTTON");
		gameQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		gameQuit.setFont(new Font("Tahoma", Font.BOLD, 15));
		gameQuit.setBounds(599, 392, 131, 42);
		playerHand.add(gameQuit);
		
		initCardOne = new JLabel("");
		initCardOne.setName("Card One");
		initCardOne.setHorizontalAlignment(SwingConstants.CENTER);
		initCardOne.setBounds(134, 11, 155, 173);
		playerHand.add(initCardOne);
		initCardOne.addMouseListener(log);
		initCardOne.addMouseMotionListener(log);
		
		initCardTwo = new JLabel("");
		initCardTwo.setName("Card Two");
		initCardTwo.setBounds(288, 10, 155, 173);
		playerHand.add(initCardTwo);
		initCardTwo.addMouseListener(log);
		initCardTwo.addMouseMotionListener(log);
		
		newCardImage = new JLabel("");
		newCardImage.setName("Hit Card");
		newCardImage.setName("New Card");
		newCardImage.setHorizontalAlignment(SwingConstants.CENTER);
		newCardImage.setFont(new Font("Tahoma", Font.BOLD, 15));
		newCardImage.setBounds(122, 145, 150, 170);
		playerHand.add(newCardImage);
		newCardImage.addMouseListener(log);
		newCardImage.addMouseMotionListener(log);
		
		 resultPanel = new JPanel();
		frame.getContentPane().add(resultPanel, "name_426569251221695");
		resultPanel.setLayout(null);
		
		JLabel winnerLabel = new JLabel("WINNER:");
		winnerLabel.setForeground(Color.GREEN);
		winnerLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		winnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		winnerLabel.setBounds(10, 97, 126, 39);
		resultPanel.add(winnerLabel);
		
		JLabel loserLabel = new JLabel("LOSER:");
		loserLabel.setHorizontalAlignment(SwingConstants.CENTER);
		loserLabel.setForeground(Color.RED);
		loserLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		loserLabel.setBounds(10, 182, 126, 39);
		resultPanel.add(loserLabel);
		
		 winnerName = new JLabel("");
		winnerName.setHorizontalAlignment(SwingConstants.CENTER);
		winnerName.setForeground(Color.DARK_GRAY);
		winnerName.setFont(new Font("Tahoma", Font.BOLD, 20));
		winnerName.setBounds(163, 97, 131, 39);
		resultPanel.add(winnerName);
		
		 loserName = new JLabel("");
		loserName.setHorizontalAlignment(SwingConstants.CENTER);
		loserName.setForeground(Color.DARK_GRAY);
		loserName.setFont(new Font("Tahoma", Font.BOLD, 20));
		loserName.setBounds(163, 182, 131, 39);
		resultPanel.add(loserName);
		
		 winnerBalance = new JLabel("");
		winnerBalance.setHorizontalAlignment(SwingConstants.CENTER);
		winnerBalance.setForeground(Color.BLACK);
		winnerBalance.setFont(new Font("Tahoma", Font.BOLD, 20));
		winnerBalance.setBounds(500, 97, 126, 39);
		resultPanel.add(winnerBalance);
		
		 loserBalance = new JLabel((String) null);
		loserBalance.setHorizontalAlignment(SwingConstants.CENTER);
		loserBalance.setForeground(Color.DARK_GRAY);
		loserBalance.setFont(new Font("Tahoma", Font.BOLD, 20));
		loserBalance.setBounds(500, 182, 126, 39);
		resultPanel.add(loserBalance);
		
		 reasonLabel = new JLabel("");
		reasonLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 17));
		reasonLabel.setForeground(Color.BLACK);
		reasonLabel.setHorizontalAlignment(SwingConstants.CENTER);
		reasonLabel.setBounds(117, 11, 476, 54);
		resultPanel.add(reasonLabel);
		
		JButton summaryQuit = new JButton("QUIT");
		summaryQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		summaryQuit.setFont(new Font("Tahoma", Font.BOLD, 15));
		summaryQuit.setBounds(514, 348, 131, 42);
		resultPanel.add(summaryQuit);
		
		JLabel lblNewLabel = new JLabel("Summary: ");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 17));
		lblNewLabel.setBounds(10, 11, 126, 39);
		resultPanel.add(lblNewLabel);
		
		 resetGame = new JButton("Reset Game");
		resetGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetGame();
			}
		});
		resetGame.setFont(new Font("Tahoma", Font.BOLD, 15));
		resetGame.setBounds(10, 348, 131, 42);
		resultPanel.add(resetGame);
		
		JLabel lblBalance = new JLabel("Balance");
		lblBalance.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 17));
		lblBalance.setBounds(363, 97, 126, 39);
		resultPanel.add(lblBalance);
		
		JLabel label = new JLabel("Balance");
		label.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 17));
		label.setBounds(363, 183, 126, 39);
		resultPanel.add(label);
		
		ultimateWinner = new JPanel();
		frame.getContentPane().add(ultimateWinner, "name_432011019687822");
		ultimateWinner.setLayout(null);
		
		JLabel gameOver = new JLabel("GAME OVER");
		gameOver.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
		gameOver.setHorizontalAlignment(SwingConstants.CENTER);
		gameOver.setBounds(202, 26, 233, 72);
		ultimateWinner.add(gameOver);
		
		JLabel finalBalance = new JLabel("Final Balances:");
		finalBalance.setHorizontalAlignment(SwingConstants.CENTER);
		finalBalance.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
		finalBalance.setBounds(202, 113, 233, 72);
		ultimateWinner.add(finalBalance);
		
		winnerFinal = new JLabel("");
		winnerFinal.setForeground(Color.GREEN);
		winnerFinal.setHorizontalAlignment(SwingConstants.CENTER);
		winnerFinal.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
		winnerFinal.setBounds(30, 192, 289, 72);
		ultimateWinner.add(winnerFinal);
		
		 loserFinal = new JLabel("");
		 loserFinal.setForeground(Color.RED);
		loserFinal.setHorizontalAlignment(SwingConstants.CENTER);
		loserFinal.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 18));
		loserFinal.setBounds(10, 297, 320, 72);
		ultimateWinner.add(loserFinal);
	}

	public void run() {
		BJGui gui = new BJGui();
		blackjack = new Game();
		gui.frame.setVisible(true);
		initCards();
	}
}

