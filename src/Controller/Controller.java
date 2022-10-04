package Controller;

import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import engine.Game;
import engine.Player;
import model.view.ChooseChampions;
import model.view.DraftScreen;
import model.world.Champion;

public class Controller implements ActionListener{
	//private Game myGame;
	private DraftScreen draftScreen;
	
	public Controller(String name1, String name2) throws FontFormatException, IOException {
		Player player1 = new Player(name1);
		Player player2 = new Player(name2);
		
	//	myGame = new Game(player1, player2);
//		myGame.loadAbilities("Abilities.csv");
//		myGame.loadChampions("Champions.csv");
		new ChooseChampions(player1, player2);
		
	}
	
	public void putChamp (int i) {
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
