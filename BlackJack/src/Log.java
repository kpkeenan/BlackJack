import java.io.IOException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.GridLayout;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;

import javax.swing.*;
public class Log implements MouseListener, MouseMotionListener,Runnable {
	private Logger logger;
	private FileHandler logFile;
	
	public Log(){
		initLogger();
	}
	
	
	public void initLogger(){
		
		try {
			logger =  Logger.getLogger("MyLog");
			logFile = new FileHandler("E:/Kevin/Advanced Object Oriented Programming/MyLog.log",true);
			logger.addHandler(logFile);
			logger.setLevel(Level.ALL);
			SimpleFormatter format = new SimpleFormatter();
			logFile.setFormatter(format);
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	 public void mouseMoved(MouseEvent event) {
		Game blackjack = new Game();
		Player player = blackjack.getPlayerInTurn();
		logger.log(Level.INFO,"During " + player.getName() + "'s turn, the mouse button was hovering over at: " + event.getComponent().getName() + " with balance of: $" + player.getBalance() );
	}

	@Override
	public void run() {
		initLogger();		
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}