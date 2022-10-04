package model.view;

import static org.junit.Assert.assertTrue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Taskbar;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.management.loading.PrivateClassLoader;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import engine.Game;
import engine.PriorityQueue;
import exceptions.AbilityUseException;
import exceptions.ChampionDisarmedException;
import exceptions.InvalidTargetException;
import exceptions.LeaderAbilityAlreadyUsedException;
import exceptions.LeaderNotCurrentException;
import exceptions.NotEnoughResourcesException;
import exceptions.UnallowedMovementException;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.effects.Disarm;
import model.effects.Effect;
import model.world.AntiHero;
import model.world.Champion;
import model.world.Cover;
import model.world.Direction;
import model.world.Hero;

public class BoardView extends JFrame implements ActionListener, MouseListener{
	private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
	private JPanel board;
	private JPanel info;
	private JPanel buttons;
	private JPanel ccInfo; 
	private JButton[][] boardButtons;
	private Game myGame;
	private JButton attack;
	private JButton ability1;
	private JButton ability2;
	private JButton ability3;
	private JButton leaderAbility;
	private JButton endTurn;
	private ImageIcon logo;
	private JLabel name1;
	private JLabel name2;
	
	private JTextArea infoPlayer1;
	private JTextArea infoPlayer2;
	private JProgressBar hpBar;
	private Font font;
	private JTextArea txtArea1;
	private JTextArea txtArea2;
	private JTextArea txtArea3;
	private JTextArea txtArea4;
	private Boolean singleTargetClicked;
	private Ability singleTargetAbility;
	private JButton punch;
	private Boolean hasPunch;
	private JPanel turnOrderPanel;
	private ImageIcon[] covers;
	private ImageIcon[] images;
	private ImageIcon[] faces;
	private Font gasaltFont;
	private static Clip mainClip;
	private JLabel upNext;
	private JLabel firstAbilityUsed;
	private JLabel secondAbilityUsed;
	private ImageIcon redTick;
	private ImageIcon redX;
	private JLabel leader1;
	private JLabel leader2;
	
	public BoardView(Game myGame, ImageIcon[] images, ImageIcon[] faces) throws FontFormatException, IOException {
		this.setBounds(200,100,1000,800);
		this.setTitle("Marvel- Ultimate War");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		logo = new ImageIcon("logo.png");
		setIconImage(logo.getImage());
		this.myGame =myGame;
		board = new JPanel();
		info= new JPanel();
		buttons = new JPanel();
		ccInfo = new JPanel();
		boardButtons = new JButton[5][5];
		attack = new JButton("Attack");
		ability1 = new JButton();
		ability2 = new JButton();
		ability3 = new JButton();
		leaderAbility = new JButton();
		hpBar = new JProgressBar();
		endTurn = new JButton();
		txtArea1 = new JTextArea();
		txtArea2 = new JTextArea();
		txtArea3 = new JTextArea();
		txtArea4 = new JTextArea();
		
		
		File gasaltFile = new File("Gasalt-Black.ttf");
		Font tempFont = Font.createFont(Font.TRUETYPE_FONT, gasaltFile);
		gasaltFont = tempFont.deriveFont(13f);
		
		redX = new ImageIcon("red x.png");
		redTick = new ImageIcon("red tick.png");
		
		firstAbilityUsed = new JLabel();
		firstAbilityUsed.setIcon(redX);
		firstAbilityUsed.setEnabled(false);
		firstAbilityUsed.setBounds(130,5,100,100);
		firstAbilityUsed.setOpaque(false);
		firstAbilityUsed.setDisabledIcon(redX);
		firstAbilityUsed.setText("Leader Ability");
		firstAbilityUsed.setFont(gasaltFont);
		firstAbilityUsed.setIconTextGap(-60);
		firstAbilityUsed.setVerticalTextPosition(JLabel.TOP);
		firstAbilityUsed.setForeground(Color.black);
		
		secondAbilityUsed = new JLabel();
		secondAbilityUsed.setIcon(redX);
		secondAbilityUsed.setEnabled(false);
		secondAbilityUsed.setOpaque(false);
		secondAbilityUsed.setDisabledIcon(redX);
		secondAbilityUsed.setBounds(330,5,100,100);
		secondAbilityUsed.setText("Leader Ability");
		secondAbilityUsed.setFont(gasaltFont);
		secondAbilityUsed.setIconTextGap(-60);
		secondAbilityUsed.setVerticalTextPosition(JLabel.TOP);
		secondAbilityUsed.setForeground(Color.black);
		info.add(firstAbilityUsed);
		info.add(secondAbilityUsed);
		
		leader1 = new JLabel("Leader");
		leader1.setBounds(5,10,50,20);
		leader1.setOpaque(false);
		leader1.setFont(gasaltFont);
		
		leader2 = new JLabel("Leader");
		leader2.setBounds(5,10,50,20);
		leader2.setOpaque(false);
		leader2.setFont(gasaltFont);
		
		gasaltFont = tempFont.deriveFont(11f);
		txtArea4.setEditable(false);
		covers = new ImageIcon[5];
		this.images = images;
		this.faces = faces;
		turnOrderPanel = new JPanel();
		turnOrderPanel.setBounds(0,520,400,70);
		turnOrderPanel.setLayout(new FlowLayout());
		turnOrderPanel.setOpaque(false);
		info.add(turnOrderPanel);
		File fontFile = new File("AVENGEANCE HEROIC AVENGER AT.ttf");
		font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
		Font sizedFont = font.deriveFont(20f);
		upNext = new JLabel("Coming Up Next:");
		upNext.setBounds(5,460,400,100);
		upNext.setFont(sizedFont);
		upNext.setOpaque(false);
		upNext.setForeground(new Color(0x9e1a1a));
		info.add(upNext);
		sizedFont = font.deriveFont(15f);
		name1 = new JLabel(myGame.getFirstPlayer().getName() + "'s Team");
		name1.setFont(sizedFont);
		name1.setOpaque(false);
		name1.setForeground(new Color(0x9e1a1a));
		name1.setBounds(5,5,400,20);
		
		name2 = new JLabel(myGame.getSecondPlayer().getName() + "'s Team");
		name2.setFont(sizedFont);
		name2.setOpaque(false);
		name2.setForeground(new Color(0x9e1a1a));
		name2.setBounds(205,5,400,20);
		info.add(name1);
		info.add(name2);
		
		sizedFont = font.deriveFont(20f);
		Point point = new Point(0,0);
		Toolkit tkit=Toolkit.getDefaultToolkit();
		Image img1 = tkit.getImage("cursor .png");
		Cursor cursor = tkit.createCustomCursor(img1,point,"cursor");
		setCursor(cursor);
		
		final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
		final URL imageResource = getClass().getResource("/resources/logo.png");
		final Image image = defaultToolkit.getImage(imageResource);
		final Taskbar taskbar = Taskbar.getTaskbar();
		try {
			taskbar.setIconImage(image);
		} catch (final UnsupportedOperationException e) {
			System.out.println("The os does not support: 'taskbar.setIconImage'");
		} catch (final SecurityException e) {
			System.out.println("There was a security exception for: 'taskbar.setIconImage'");
		}
		
		covers[0] = new ImageIcon("purple cover.png");
		covers[1] = new ImageIcon("red cover.png");
		covers[2] = new ImageIcon("green cover.png");
		covers[3] = new ImageIcon("yellow cover.png");
		covers[4] = new ImageIcon("orange cover.png");
		
		
		
		board.getInputMap(IFW).put(KeyStroke.getKeyStroke("UP"), "UP");
		board.getInputMap(IFW).put(KeyStroke.getKeyStroke("DOWN"), "DOWN");
		board.getInputMap(IFW).put(KeyStroke.getKeyStroke("RIGHT"), "RIGHT");
		board.getInputMap(IFW).put(KeyStroke.getKeyStroke("LEFT"), "LEFT");
		
		
		class MoveAction extends AbstractAction{
			Direction d;
			public MoveAction(Direction d) {
				this.d= d; 
			}
			public void actionPerformed(ActionEvent e) {
				try {
					myGame.move(d);
					board.removeAll();
					updateInfo();
					redoBoard();	
					updateccInfo();
				} catch (NotEnoughResourcesException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Not Enough Resources!", JOptionPane.PLAIN_MESSAGE);
					e1.printStackTrace();
				} catch (UnallowedMovementException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Unallowed Movement!", JOptionPane.PLAIN_MESSAGE);
					e1.printStackTrace();
				}
			}
			
		}
		
		board.getActionMap().put("UP", new MoveAction(Direction.UP));
		board.getActionMap().put("DOWN", new MoveAction(Direction.DOWN));
		board.getActionMap().put("LEFT",  new MoveAction(Direction.LEFT));
		board.getActionMap().put("RIGHT",  new MoveAction(Direction.RIGHT));

		
		setResizable(false);
		
		

		
		
		
		ImageIcon redButtonIcon = new ImageIcon("redbutton.png");
		
		attack.setFont(sizedFont);
		attack.setIcon(new ImageIcon(redButtonIcon.getImage().getScaledInstance(100,50, Image.SCALE_SMOOTH)));
		attack.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
					String[] direction = {"Up", "Down", "Left", "Right"};
					int option = JOptionPane.showOptionDialog(null,"Choose a direction!","Attack", 
							JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE, null,direction,direction[0]);
					switch(option) {
					case 0:
						try {
							myGame.attack(Direction.UP);
							playSound("attack.wav");
						} catch (NotEnoughResourcesException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage(), "Not Enough Resources!", JOptionPane.PLAIN_MESSAGE);
							e1.printStackTrace();
						} catch (ChampionDisarmedException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage(), "You're disarmed!", JOptionPane.PLAIN_MESSAGE);
							e1.printStackTrace();
						} catch (InvalidTargetException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage(), "Invalid Target!", JOptionPane.PLAIN_MESSAGE);
							e1.printStackTrace();
						}
						break;
					case 1:
						try {
							myGame.attack(Direction.DOWN);
							playSound("attack.wav");
						} catch (NotEnoughResourcesException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage(), "Not Enough Resources!", JOptionPane.PLAIN_MESSAGE);
							e1.printStackTrace();
						} catch (ChampionDisarmedException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage(), "You're disarmed!", JOptionPane.PLAIN_MESSAGE);
							e1.printStackTrace();
						} catch (InvalidTargetException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage(), "Invalid Target!", JOptionPane.PLAIN_MESSAGE);
							e1.printStackTrace();
						}
						break;
					case 2:
						try {
							myGame.attack(Direction.LEFT);
							playSound("attack.wav");
						} catch (NotEnoughResourcesException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage(), "Not Enough Resources!", JOptionPane.PLAIN_MESSAGE);
							e1.printStackTrace();
						} catch (ChampionDisarmedException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage(), "You're disarmed!", JOptionPane.PLAIN_MESSAGE);
							e1.printStackTrace();
						} catch (InvalidTargetException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage(), "Invalid Target!", JOptionPane.PLAIN_MESSAGE);
							e1.printStackTrace();
						}
						break;
					default:
						try {
							myGame.attack(Direction.RIGHT);
							playSound("attack.wav");
						} catch (NotEnoughResourcesException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage(), "Not Enough Resources!", JOptionPane.PLAIN_MESSAGE);
							e1.printStackTrace();
						} catch (ChampionDisarmedException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage(), "You're disarmed!", JOptionPane.PLAIN_MESSAGE);
							e1.printStackTrace();
						} catch (InvalidTargetException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage(), "Invalid Target!", JOptionPane.PLAIN_MESSAGE);
							e1.printStackTrace();
						}
						break;
					}
					updateInfo();
					board.removeAll();
					redoBoard();
					updateccInfo();
					try {
						checkGameOver();
					} catch (FontFormatException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			
			
		});
		attack.setHorizontalTextPosition(SwingConstants.CENTER);
		attack.setBorderPainted(false);
		attack.setContentAreaFilled(false);
		attack.setForeground(new Color(0xf5edd6));
		attack.setOpaque(false);
		attack.setFocusPainted(false);
		attack.setFocusable(true);
		buttons.add(attack);
		
		sizedFont = font.deriveFont(13f);
		
		ability1.setText(myGame.getCurrentChampion().getAbilities().get(0).getName());
		ability1.setFont(sizedFont);
		ability1.setIcon(new ImageIcon(redButtonIcon.getImage().getScaledInstance(100,50, Image.SCALE_SMOOTH)));
		ability1.addActionListener(this);
		ability1.setHorizontalTextPosition(SwingConstants.CENTER);
		ability1.setBorderPainted(false);
		ability1.setContentAreaFilled(false);
		ability1.setForeground(new Color(0xf5edd6));
		ability1.setOpaque(false);
		ability1.setFocusPainted(false);
		buttons.add(ability1);
		
		
		ability2.setText(myGame.getCurrentChampion().getAbilities().get(1).getName());
		ability2.setFont(sizedFont);
		ability2.setIcon(new ImageIcon(redButtonIcon.getImage().getScaledInstance(100,50, Image.SCALE_SMOOTH)));
		ability2.addActionListener(this);
		ability2.setHorizontalTextPosition(SwingConstants.CENTER);
		ability2.setBorderPainted(false);
		ability2.setContentAreaFilled(false);
		ability2.setForeground(new Color(0xf5edd6));
		ability2.setOpaque(false);
		ability2.setFocusPainted(false);
		buttons.add(ability2);
		
		
		ability3.setText(myGame.getCurrentChampion().getAbilities().get(2).getName());
		ability3.setFont(sizedFont);
		ability3.setIcon(new ImageIcon(redButtonIcon.getImage().getScaledInstance(100,50, Image.SCALE_SMOOTH)));
		ability3.addActionListener(this);
		ability3.setHorizontalTextPosition(SwingConstants.CENTER);
		ability3.setBorderPainted(false);
		ability3.setContentAreaFilled(false);
		ability3.setForeground(new Color(0xf5edd6));
		ability3.setOpaque(false);
		ability3.setFocusPainted(false);
		buttons.add(ability3);
		
		leaderAbility.setText("Leader Ability");
		leaderAbility.setFont(sizedFont);
		leaderAbility.setIcon(new ImageIcon(redButtonIcon.getImage().getScaledInstance(100,50, Image.SCALE_SMOOTH)));
		leaderAbility.addActionListener(this);
		leaderAbility.setHorizontalTextPosition(SwingConstants.CENTER);
		leaderAbility.setBorderPainted(false);
		leaderAbility.setContentAreaFilled(false);
		leaderAbility.setForeground(new Color(0xf5edd6));
		leaderAbility.setOpaque(false);
		leaderAbility.setFocusPainted(false);
		buttons.add(leaderAbility);
		
		sizedFont = font.deriveFont(20f);
		
		endTurn.setText("End Turn");
		endTurn.setFont(sizedFont);
		endTurn.setIcon(new ImageIcon(redButtonIcon.getImage().getScaledInstance(100,50, Image.SCALE_SMOOTH)));
		endTurn.addActionListener(this);
		endTurn.setHorizontalTextPosition(SwingConstants.CENTER);
		endTurn.setBorderPainted(false);
		endTurn.setContentAreaFilled(false);
		endTurn.setForeground(new Color(0xf5edd6));
		endTurn.setOpaque(false);
		endTurn.setFocusPainted(false);
		buttons.add(endTurn);
		
		punch = new JButton("Punch");
		punch.setFont(sizedFont);
		punch.setIcon(new ImageIcon(redButtonIcon.getImage().getScaledInstance(100,50, Image.SCALE_SMOOTH)));
		punch.addActionListener(this);
		punch.setHorizontalTextPosition(SwingConstants.CENTER);
		punch.setBorderPainted(false);
		punch.setContentAreaFilled(false);
		punch.setForeground(new Color(0xf5edd6));
		punch.setOpaque(false);
		punch.setFocusPainted(false);
		punch.setFocusable(true);
		punch.setBounds(400,700,200,50);
		
		board.setLayout(new GridLayout(5,5));
		board.setBounds(0,0,600,600);
		board.setBorder(new LineBorder(Color.black));
		board.setBackground(new Color(0xf5edd6));
		board.setOpaque(false);
		add(board);
		
		info.setLayout(null);
		info.setBounds(600,0,400,600);
		info.setBorder(new LineBorder(Color.black));
		info.setBackground(new Color(0xf5edd6));
		
		infoPlayer1 = new JTextArea();
		infoPlayer2 = new JTextArea();
		infoPlayer1.setBounds(EXIT_ON_CLOSE, ABORT, WIDTH, HEIGHT);
		infoPlayer2.setBounds(EXIT_ON_CLOSE, ABORT, WIDTH, HEIGHT);
		
		add(info);
		
		buttons.setLayout(new FlowLayout());
		buttons.setBounds(600,600,400,200);
		buttons.setFocusable(true);
		buttons.setBorder(new LineBorder(Color.black));
		buttons.setBackground(new Color(0xf5edd6));
		add(buttons);
		
		ccInfo.setLayout(null);
		ccInfo.setBounds(0,600,600,200);
		ccInfo.setBorder(new LineBorder(Color.black));
		ccInfo.setBackground(new Color(0xf5edd6));
		add(ccInfo);	
		txtArea1.setEditable(false);
		txtArea2.setEditable(false);
		txtArea3.setEditable(false);
		
		
		redoBoard();
		updateInfo();
		updateccInfo();
		
		this.setVisible(true);
		this.revalidate();
		this.repaint();
	}
	
	public void redoBoard() {
		int covercount =0;
		for(int i=4; i>=0; i--) {
			for(int j=0; j<5; j++) {
				boardButtons[i][j]= new JButton(); 
				boardButtons[i][j].setIcon(new ImageIcon("beige bg.png"));
				boardButtons[i][j].setBorder(new LineBorder(Color.white));
				boardButtons[i][j].addMouseListener(this);
				if(myGame.getBoard()[i][j] instanceof Cover) {
					boardButtons[i][j].setBorder(new LineBorder(Color.white));
					boardButtons[i][j].setIcon(new ImageIcon(covers[covercount].getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
					covercount++;
					JProgressBar bar = new JProgressBar();
					bar.setMaximum( ((Cover)(myGame.getBoard()[i][j])).getMaxHP());
					bar.setValue(((Cover)(myGame.getBoard()[i][j])).getCurrentHP());
					bar.setStringPainted(true);
					bar.setString(((Cover)(myGame.getBoard()[i][j])).getCurrentHP() + "/" + ((Cover)(myGame.getBoard()[i][j])).getMaxHP());
					bar.setFont(new Font("Verdana", Font.PLAIN, 10));
					bar.setForeground(Color.red);
					bar.setBackground(Color.black);
//					bar.setPreferredSize(new Dimension(50,5));
					bar.setBounds(0,3,80,10);
					boardButtons[i][j].add(bar); 
					boardButtons[i][j].setLayout(null);
					
					}else if(myGame.getFirstPlayer().getTeam().contains((Champion)(myGame.getBoard()[i][j]))){
					if(((Champion)(myGame.getBoard()[i][j])) == myGame.getFirstPlayer().getLeader()) {
						boardButtons[i][j].add(leader1);
					}
					boardButtons[i][j].setBorder(new LineBorder(Color.red));
					boardButtons[i][j].setIcon(new ImageIcon(images[getIndexOfChampion(((Champion)(myGame.getBoard()[i][j])).getName())].getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
					JProgressBar bar = new JProgressBar();
					bar.setMaximum(((Champion)(myGame.getBoard()[i][j])).getMaxHP());
					bar.setValue(((Champion)(myGame.getBoard()[i][j])).getCurrentHP());
					bar.setStringPainted(true);
					bar.setString(((Champion)(myGame.getBoard()[i][j])).getCurrentHP() + "/" + ((Champion)(myGame.getBoard()[i][j])).getMaxHP());
					bar.setFont(new Font("Verdana", Font.PLAIN, 10));
					bar.setForeground(Color.green);
					bar.setBackground(Color.black);
					bar.setBounds(0,3,80,10);
					boardButtons[i][j].setLayout(null);
					boardButtons[i][j].add(bar); 
				}else if(myGame.getSecondPlayer().getTeam().contains((Champion)(myGame.getBoard()[i][j]))){
					if(((Champion)(myGame.getBoard()[i][j])) == myGame.getSecondPlayer().getLeader()) {
						boardButtons[i][j].add(leader2);
					}
					boardButtons[i][j].setBorder(new LineBorder(Color.blue));
					boardButtons[i][j].setIcon(new ImageIcon(images[getIndexOfChampion(((Champion)(myGame.getBoard()[i][j])).getName())].getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
					JProgressBar bar = new JProgressBar();
					bar.setMaximum(((Champion)(myGame.getBoard()[i][j])).getMaxHP());
					bar.setValue(((Champion)(myGame.getBoard()[i][j])).getCurrentHP());
					bar.setStringPainted(true);
					bar.setString(((Champion)(myGame.getBoard()[i][j])).getCurrentHP() + "/" + ((Champion)(myGame.getBoard()[i][j])).getMaxHP());
					bar.setFont(new Font("Verdana", Font.PLAIN, 10));
					bar.setForeground(Color.green);
					bar.setBackground(Color.black);
					bar.setBounds(0,3,80,10);
					boardButtons[i][j].add(bar); 
					boardButtons[i][j].setLayout(null);
				}
				boardButtons[i][j].addActionListener(this);
				boardButtons[i][j].setFocusable(false);
				
				board.add(boardButtons[i][j]);	
				if(myGame.getCurrentChampion() == myGame.getBoard()[i][j]) {
					boardButtons[i][j].setBorder(new LineBorder(Color.black));
				}
			}
			revalidate();
			repaint();
		}
	}

	
	public void moveInDir(Direction d) {
		System.out.println(d+" PRESSED");
		try {
			myGame.move(d);
			board.removeAll();
			updateInfo();
			redoBoard();
			updateccInfo();
		} catch (NotEnoughResourcesException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Not Enough Resources!", JOptionPane.PLAIN_MESSAGE);
			e1.printStackTrace();
		} catch (UnallowedMovementException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage(), "Unallowed Movement!", JOptionPane.PLAIN_MESSAGE);
			e1.printStackTrace();
		}
	}
	
	
	
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == ability1) {
			singleTargetClicked = false;
			Ability a =myGame.getCurrentChampion().getAbilities().get(0);
			helperAbility(a);
			try {
				checkGameOver();
			} catch (FontFormatException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(e.getSource() == ability2) {
			singleTargetClicked = false;
			Ability a =myGame.getCurrentChampion().getAbilities().get(1);
			helperAbility(a);
			try {
				checkGameOver();
			} catch (FontFormatException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(e.getSource() == ability3) {
			singleTargetClicked = false;
			Ability a =myGame.getCurrentChampion().getAbilities().get(2);
			helperAbility(a);
			try {
				checkGameOver();
			} catch (FontFormatException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(e.getSource() == leaderAbility) {
			singleTargetClicked = false;
			try {
				myGame.useLeaderAbility();
				playSound("ability.wav");
				if(myGame.getFirstPlayer().getTeam().contains(myGame.getCurrentChampion())) {
					firstAbilityUsed.setIcon(redTick);
					firstAbilityUsed.setDisabledIcon(redTick);
				}else {
					secondAbilityUsed.setIcon(redTick);
					secondAbilityUsed.setDisabledIcon(redTick);
				}
			} catch (LeaderNotCurrentException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "Current champion is not a leader!", JOptionPane.PLAIN_MESSAGE);
				e1.printStackTrace();
			} catch (LeaderAbilityAlreadyUsedException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "Leader Ability already Used!", JOptionPane.PLAIN_MESSAGE);
				e1.printStackTrace();
			}
			updateInfo();
			board.removeAll();
			redoBoard();
			updateccInfo();
			try {
				checkGameOver();
			} catch (FontFormatException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(e.getSource() == endTurn) {
			singleTargetClicked = false;
			ccInfo.remove(punch);
			myGame.endTurn();
			hasPunch = false;
			updateInfo();
			board.removeAll();
			redoBoard();
			updateccInfo();
			try {
				updateButtons();
			} catch (FontFormatException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				checkGameOver();
			} catch (FontFormatException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(e.getSource() == punch) {
			singleTargetClicked = true;
			JOptionPane.showMessageDialog(null, "Select a target on the board", "Punch!", JOptionPane.PLAIN_MESSAGE);
		}
		
		
		for(int i=4; i>=0; i--) {
			for(int j=0; j<5; j++) {
				if(e.getSource() == boardButtons[i][j]) {
					int x =i;
					int y= j;
					if(singleTargetClicked) {
						try {
							myGame.castAbility(singleTargetAbility,x,y);
							playSound("ability.wav");
						} catch (NotEnoughResourcesException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage(), "Not enough resources!", JOptionPane.PLAIN_MESSAGE);
							e1.printStackTrace();
						} catch (AbilityUseException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage(), "Ability Use Exception!", JOptionPane.PLAIN_MESSAGE);
							e1.printStackTrace();
						} catch (InvalidTargetException e1) {
							JOptionPane.showMessageDialog(null, e1.getMessage(), "Invalid target!", JOptionPane.PLAIN_MESSAGE);
							e1.printStackTrace();
						} catch (CloneNotSupportedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							checkGameOver();
						} catch (FontFormatException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						updateInfo();
						board.removeAll();
						redoBoard();
						updateccInfo();
						singleTargetClicked =false;
					}
				}
			}
		}
		
	}
	
	
	public void helperAbility(Ability a) {
		if(a.getCastArea() == AreaOfEffect.DIRECTIONAL) {
			String[] direction = {"Up", "Down", "Left", "Right"};
			int option = JOptionPane.showOptionDialog(null,"Choose a direction!","Cast Ability", 
					JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE, null,direction,0);
			switch(option) {
			case 0:
				try {
					myGame.castAbility(a, Direction.UP);
					playSound("ability.wav");
				} catch (NotEnoughResourcesException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Not Enough Resources!", JOptionPane.PLAIN_MESSAGE);
					e1.printStackTrace();
				} catch (AbilityUseException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Ability Use Exception!", JOptionPane.PLAIN_MESSAGE);
					e1.printStackTrace();
				} catch (CloneNotSupportedException e1) {
					e1.printStackTrace();
				}
				break;
			case 1:
				try {
					myGame.castAbility(a, Direction.DOWN);
					playSound("ability.wav");
				} catch (NotEnoughResourcesException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Not Enough Resources!", JOptionPane.PLAIN_MESSAGE);
					e1.printStackTrace();
				} catch (AbilityUseException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Ability Use Exception!", JOptionPane.PLAIN_MESSAGE);
					e1.printStackTrace();
				} catch (CloneNotSupportedException e1) {
					e1.printStackTrace();
				}
				break;
			case 2:
				try {
					myGame.castAbility(a, Direction.LEFT);
					playSound("ability.wav");
				} catch (NotEnoughResourcesException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Not Enough Resources!", JOptionPane.PLAIN_MESSAGE);
					e1.printStackTrace();
				} catch (AbilityUseException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Ability Use Exception!", JOptionPane.PLAIN_MESSAGE);
					e1.printStackTrace();
				} catch (CloneNotSupportedException e1) {
					e1.printStackTrace();
				}
				break;
			default:
				try {
					myGame.castAbility(a, Direction.RIGHT);
					playSound("ability.wav");
				} catch (NotEnoughResourcesException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Not Enough Resources!", JOptionPane.PLAIN_MESSAGE);
					e1.printStackTrace();
				} catch (AbilityUseException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), "Ability Use Exception!", JOptionPane.PLAIN_MESSAGE);
					e1.printStackTrace();
				} catch (CloneNotSupportedException e1) {
					e1.printStackTrace();
				}
				break;
			}
		}else if(a.getCastArea() == AreaOfEffect.SINGLETARGET) {
			singleTargetAbility = a;
			singleTargetClicked = true;
			JOptionPane.showMessageDialog(null, "Select a target on the board", "Cast Ability!", JOptionPane.PLAIN_MESSAGE);
		
		}else {
			try {
				myGame.castAbility(a);
				playSound("ability.wav");
			} catch (NotEnoughResourcesException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "Not Enough Resources Exception!", JOptionPane.PLAIN_MESSAGE);
				e1.printStackTrace();
			} catch (AbilityUseException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "Ability Use Exception!", JOptionPane.PLAIN_MESSAGE);
				e1.printStackTrace();
			} catch (CloneNotSupportedException e1) {
				e1.printStackTrace();
			}
		}
		updateInfo();
		board.removeAll();
		redoBoard();
		updateccInfo();
		try {
			checkGameOver();
		} catch (FontFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateButtons() throws FontFormatException, IOException {
		buttons.removeAll();
		
		File fontFile = new File("AVENGEANCE HEROIC AVENGER AT.ttf");
		font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
		Font sizedFont = font.deriveFont(20f);
		
		ImageIcon blueButtonIcon = new ImageIcon("redbutton.png");
		
		buttons.add(attack);
		
		ability1.setText(myGame.getCurrentChampion().getAbilities().get(0).getName());
		ability1.setFont(sizedFont);
		ability1.setIcon(new ImageIcon(blueButtonIcon.getImage().getScaledInstance(100,50, Image.SCALE_SMOOTH)));
		ability1.setHorizontalTextPosition(SwingConstants.CENTER);
		ability1.setBorderPainted(false);
		ability1.setContentAreaFilled(false);
		ability1.setForeground(new Color(0xf5edd6));
		ability1.setOpaque(false);
		ability1.setFocusPainted(false);
		buttons.add(ability1);
		
		
		ability2.setText(myGame.getCurrentChampion().getAbilities().get(1).getName());
		ability2.setFont(sizedFont);
		ability2.setIcon(new ImageIcon(blueButtonIcon.getImage().getScaledInstance(100,50, Image.SCALE_SMOOTH)));
		ability2.setHorizontalTextPosition(SwingConstants.CENTER);
		ability2.setBorderPainted(false);
		ability2.setContentAreaFilled(false);
		ability2.setForeground(new Color(0xf5edd6));
		ability2.setOpaque(false);
		ability2.setFocusPainted(false);
		buttons.add(ability2);
		
		
		ability3.setText(myGame.getCurrentChampion().getAbilities().get(2).getName());
		ability3.setFont(sizedFont);
		ability3.setIcon(new ImageIcon(blueButtonIcon.getImage().getScaledInstance(100,50, Image.SCALE_SMOOTH)));
		ability3.setHorizontalTextPosition(SwingConstants.CENTER);
		ability3.setBorderPainted(false);
		ability3.setContentAreaFilled(false);
		ability3.setForeground(new Color(0xf5edd6));
		ability3.setOpaque(false);
		ability3.setFocusPainted(false);
		buttons.add(ability3);
		
		leaderAbility.setText("Leader Ability");
		leaderAbility.setFont(sizedFont);
		leaderAbility.setIcon(new ImageIcon(blueButtonIcon.getImage().getScaledInstance(100,50, Image.SCALE_SMOOTH)));
		leaderAbility.setHorizontalTextPosition(SwingConstants.CENTER);
		leaderAbility.setBorderPainted(false);
		leaderAbility.setContentAreaFilled(false);
		leaderAbility.setForeground(new Color(0xf5edd6));
		leaderAbility.setOpaque(false);
		leaderAbility.setFocusPainted(false);
		buttons.add(leaderAbility);
		
		buttons.add(endTurn);
	
		
		revalidate();
		repaint();
		
	}
	
	public void updateInfo() {
		String output = "\n";
		for(Champion c : myGame.getFirstPlayer().getTeam()) {
			output += "Name: " + c.getName() + '\n';
			output += "Type: " + getType(c) + '\n';
			output += "Current HP: " + c.getCurrentHP() + '\n'; 
			output += "Speed: " + c.getSpeed() + '\n';
			output += "Mana: " +c.getMana() + '\n';
			output += "Max Action Points: " + c.getMaxActionPointsPerTurn() +"\n";
			output +=  "Attack Range: " + c.getAttackRange() + '\n';
			output += "Attack Damage: " + c.getAttackDamage() + '\n';
			output += "Ability 1: " + c.getAbilities().get(0).getName() + "\n";
			output += "Ability 2: " +c.getAbilities().get(1).getName() + "\n";
			output += "Ability 3: " + c.getAbilities().get(2).getName() +"\n";
			output += "-------------" + "\n";
			
			infoPlayer1.setText(output);
			infoPlayer1.setEditable(false);
			infoPlayer1.setWrapStyleWord(true);
			infoPlayer1.setLineWrap(true);
			infoPlayer1.setForeground(new Color(0x000000));
			infoPlayer1.setBounds(10,10, 190, 480);
			infoPlayer1.setFont(gasaltFont);
			infoPlayer1.setOpaque(false);
			infoPlayer1.setVisible(true);
			infoPlayer1.add(hpBar);
			
		}
		infoPlayer1.revalidate();
		infoPlayer1.repaint();
		info.add(infoPlayer1);
		output = "\n";
		for(Champion c : myGame.getSecondPlayer().getTeam()) {
			output += "Name: " + c.getName() + '\n';
			output += "Type: " + getType(c) + '\n';
			output += "Current HP: " + c.getCurrentHP() + '\n'; 
			output += "Speed: " + c.getSpeed() + '\n';
			output += "Mana: " +c.getMana() + '\n';
			output += "Action Points: " +c.getCurrentActionPoints() +'\n';
			output += "Attack Range: " + c.getAttackRange() + '\n';
			output += "Attack Damage: " + c.getAttackDamage() + '\n';
			output += "Ability 1: " + c.getAbilities().get(0).getName() + "\n";
			output += "Ability 2: " +c.getAbilities().get(1).getName() + "\n";
			output += "Ability 3: " + c.getAbilities().get(2).getName() +"\n";
			output += "-------------" + "\n";
			
			infoPlayer2.setText(output);
			infoPlayer2.setEditable(false);
			infoPlayer2.setWrapStyleWord(true);
			infoPlayer2.setLineWrap(true);
			infoPlayer2.setForeground(new Color(0x000000));
			infoPlayer2.setBounds(210,10, 190, 480);
			infoPlayer2.setFont(gasaltFont);
			infoPlayer2.setOpaque(false);
			infoPlayer2.setVisible(true);
			infoPlayer2.add(hpBar);
				
		}
		info.setFocusable(true);
		info.add(infoPlayer2);
		turnOrderPanel.removeAll();
		PriorityQueue temp= new PriorityQueue(10);
		while(!myGame.getTurnOrder().isEmpty()) {
			Champion c = (Champion) myGame.getTurnOrder().remove();
			temp.insert(c);
			JLabel label = new JLabel();
			ImageIcon icon = faces[getIndexOfChampion(c.getName())];
			label.setIcon(new ImageIcon(icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
			turnOrderPanel.add(label);
		}
		
		while(!temp.isEmpty()) {
			myGame.getTurnOrder().insert(temp.remove());
		}
		
		
		infoPlayer2.revalidate();
		infoPlayer2.repaint();
		
	}
	
	public void checkGameOver() throws FontFormatException, IOException {
		if(myGame.checkGameOver()!=null) {
			this.dispose();
			new GameOver(myGame.checkGameOver());
		}
	}
	
	
	public void updateccInfo() {
		
		String output = "";
		Champion c = myGame.getCurrentChampion();
		output += "Name: " + c.getName() + '\n';
		output += "Type: " + getType(c) + '\n';
		output += "Current HP: " + c.getCurrentHP() + '\n'; 
		output += "Speed: " + c.getSpeed() + '\n';
		output += "Mana: " +c.getMana() + '\n';
		output += "Current Action Points: " +c.getCurrentActionPoints() +'\n';
		output += "Max Action Points: " + c.getMaxActionPointsPerTurn() +"\n";
		output +=  "Attack Range: " + c.getAttackRange() + '\n';
		output += "Attack Damage: " + c.getAttackDamage() + '\n';
		output += "Ability 1: " + c.getAbilities().get(0).getName() + "\n";
		output += "Ability 2: " +c.getAbilities().get(1).getName() + "\n";
		output += "Ability 3: " + c.getAbilities().get(2).getName() +"\n";
		
		txtArea1.setText(output);
		output ="";
		for(int i=0; i<2 ;i++) {
			Ability a = c.getAbilities().get(i);
			output += a.getName() + getAbilityType(a) + "\n";
			output += "Cast Area: " + a.getCastArea().toString() + "\n";
			output += "Cast Range: " + a.getCastRange() ;
			output += ", Action Points: " + a.getRequiredActionPoints() + "\n";
			output += "Mana Cost: " + a.getManaCost() + "\n";
			output += "Cooldown: " + a.getBaseCooldown() +" (base) "+ a.getCurrentCooldown() +" (current)"+ "\n";
			output += getExtraAttribute(a) +"\n" +"\n";
		}
		txtArea2.setText(output);
		output ="";
		for(int i=2; i<4 && i < c.getAbilities().size() ;i++) {
			Ability a = c.getAbilities().get(i);
			output += a.getName() + getAbilityType(a) + "\n";
			output += "Cast Area: " + a.getCastArea().toString() + "\n";
			output += "Cast Range: " + a.getCastRange() ;
			output += ", Action Points: " + a.getRequiredActionPoints() + "\n";
			output += "Mana Cost: " + a.getManaCost() + "\n";
			output += "Cooldown: " + a.getBaseCooldown() +" (base) "+ a.getCurrentCooldown() +" (current)"+ "\n";
			output += getExtraAttribute(a) +"\n" + "\n";
		}
		txtArea3.setText(output);
		output ="";
		for(Effect e : c.getAppliedEffects()) {
			output += "Effect: " + e.getName() + "\n";
			output += "Duration: " + e.getDuration() + "\n";
		}
		txtArea4.setText(output);
		
		txtArea1.revalidate();
		txtArea1.repaint();
		txtArea2.revalidate();
		txtArea2.repaint();
		txtArea3.revalidate();
		txtArea3.repaint();
		txtArea4.revalidate();
		txtArea4.repaint();
		ccInfo.setFocusable(true);
		txtArea1.setBounds(5,600,150,200);
		txtArea2.setBounds(150,600,150,200);
		txtArea3.setBounds(300,600,150,100);
		txtArea4.setBounds(450,600,150,100);
		txtArea1.setOpaque(false);
		txtArea2.setOpaque(false);
		txtArea3.setOpaque(false);
		txtArea4.setOpaque(false);
		txtArea1.setFont(gasaltFont);
		txtArea2.setFont(gasaltFont);
		txtArea3.setFont(gasaltFont);
		txtArea4.setFont(gasaltFont);
		ccInfo.add(txtArea1);
		ccInfo.add(txtArea2);
		ccInfo.add(txtArea3);
		ccInfo.add(txtArea4);
		boolean hasDisarm = false;
		
		for(Effect e: myGame.getCurrentChampion().getAppliedEffects()) {
			if(e instanceof Disarm) {
				hasDisarm = true;
			}
		}
		
		if(hasDisarm && !hasPunch) {
			ccInfo.add(punch);
			hasPunch = true;
		}
		
	}
	
	public int getIndexOfChampion(String name) {
		if(name.equals("Captain America")) {
			return 0;
		}
		if(name.equals("Deadpool")) {
			return 1;
		}
		if(name.equals("Dr Strange")) {
			return 2;
		}
		if(name.equals("Electro")) {
			return 3;
		}
		if(name.equals("Ghost Rider")) {
			return 4;
		}
		if(name.equals("Hela")) {
			return 5;
		}
		if(name.equals("Hulk")) {
			return 6;
		}
		if(name.equals("Iceman")) {
			return 7;
		}
		if(name.equals("Ironman")) {
			return 8;
		}
		if(name.equals("Loki")) {
			return 9;
		}
		if(name.equals("Quicksilver")) {
			return 10;
		}
		if(name.equals("Spiderman")) {
			return 11;
		}
		if(name.equals("Thor")) {
			return 12;
		}
		if(name.equals("Venom")) {
			return 13;
		}
		else {
			return 14;
		}
		
	}
	
	public void playSound(String filename) {
		try
	    {	
	    	File file = new File(filename);
	        mainClip = AudioSystem.getClip();
	        mainClip.open(AudioSystem.getAudioInputStream(file));
	        mainClip.start();
	    }
	    catch (Exception exc)
	    {
	        exc.printStackTrace(System.out);
	    }
	}
	
	public String getType(Champion c) {
		if(c instanceof Hero) {
			return "Hero";
		}else if(c instanceof AntiHero) {
			return "AntiHero";
		}else {
			return "Villain";
		}
	}
	
	public String getAbilityType(Ability a) {
		if(a instanceof HealingAbility) {
			return " (Healing)";
		}else if(a instanceof DamagingAbility) {
			return " (Damaging)";
		}else {
			return " (Crowd Control)";
		}
	}
	
	public String getExtraAttribute(Ability a) {
		if(a instanceof HealingAbility) {
			return "Healing Amount: "+ ((HealingAbility)a).getHealAmount();
		}else if(a instanceof DamagingAbility) {
			return "Damaging Amount: " + ((DamagingAbility)a).getDamageAmount();
		}else {
			return "Effect: " +((CrowdControlAbility)a).getEffect().getName();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
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

	@Override
	public void mouseEntered(MouseEvent e) {
		for(int i=4; i>=0; i--) {
			for(int j=0; j<5; j++) {
				if(e.getSource() == boardButtons[i][j] && myGame.getBoard()[i][j] instanceof Champion) {
					
					String output = "<html>";
					Champion c = (Champion) myGame.getBoard()[i][j];
					for(int x =0; x<c.getAppliedEffects().size(); x++) {
						output += "Effect: " + c.getAppliedEffects().get(x).getName() + "<br>";
						output += "Duration: " + c.getAppliedEffects().get(x).getDuration() + "<br>";
					}
					output += "</html>";
					boardButtons[i][j].setToolTipText(output);
					revalidate();
					repaint();
				}
			}
		}
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
