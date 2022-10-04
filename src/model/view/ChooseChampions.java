package model.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Taskbar;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import org.junit.validator.PublicClassValidator;

import Controller.Controller;
import engine.Game;
import engine.Player;
import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.CrowdControlAbility;
import model.abilities.DamagingAbility;
import model.abilities.HealingAbility;
import model.effects.Disarm;
import model.effects.Dodge;
import model.effects.Effect;
import model.effects.Embrace;
import model.effects.PowerUp;
import model.effects.Root;
import model.effects.Shield;
import model.effects.Shock;
import model.effects.Silence;
import model.effects.SpeedUp;
import model.effects.Stun;
import model.world.AntiHero;
import model.world.Champion;
import model.world.Hero;
import model.world.Villain;

public class ChooseChampions extends JFrame implements ActionListener, MouseListener {
	
	private JButton[] champions;
	private JPanel panel;
	private JPanel championsPanel;
	private JPanel player1Panel;
	private JPanel player2Panel;
	private JPanel attributePanel;
	//private ArrayList<Champion> availableChampions;
	private static int counter =0;
	private JTextArea txtArea;
	private Game myGame;
	private ImageIcon[] images;
	private ImageIcon[] fullbodyImages;
	private JLabel tempLabel;
	private JLabel player1name;
	private JLabel player2name;
	private Player player1;
	private Player player2;
	private static ArrayList<Champion> availableChampions;
	private static ArrayList<Ability> availableAbilities;	
	private ImagePanel imagePanel;
	private static Clip mainClip;
	
	public ChooseChampions(Player player1, Player player2) throws IOException, FontFormatException {
		availableAbilities = new ArrayList<>();
		availableChampions = new ArrayList<>();
		loadAbilities("Abilities.csv");
		loadChampions("Champions.csv");
		txtArea = new JTextArea();
		this.player1 = player1;
		this.player2 = player2;
		//BufferedImage background = ImageIO.read(getClass().getResource("/resources/choose.jpeg"));
	//	setContentPane(new ImagePanel(background));
		
		imagePanel = new ImagePanel(ImageIO.read(getClass().getResource("/resources/beige bg.png")));
		
		tempLabel = new JLabel();
		fullbodyImages = new ImageIcon[15];
		
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
		
		
		this.setBounds(200,100,1000,800);
		this.setTitle("Marvel- Ultimate War");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//setBackground(new Color(0x8cd3ff));
	
		images = new ImageIcon[15];
		images[0] = new ImageIcon("captain america rounded.png");
		images[1] = new ImageIcon("deadpool rounded.png");
		images[2] = new ImageIcon("doctor strange rounded.png");
		images[3] = new ImageIcon("electro rounded.png");
		images[4] = new ImageIcon("ghost rider rounded.png");
		images[5] = new ImageIcon("hela rounded.png");
		images[6] = new ImageIcon("hulk rounded.png");
		images[7] = new ImageIcon("iceman rounded.png");
		images[8] = new ImageIcon("ironman rounded.png");
		images[9] = new ImageIcon("loki rounded.png");
		images[10] = new ImageIcon("quicksilver rounded.png");
		images[11] = new ImageIcon("spiderman rounded.png");
		images[12] = new ImageIcon("thor rounded.png");
		images[13] = new ImageIcon("venom rounded.png");
		images[14] = new ImageIcon("yellow jacket rounded.png");
		
		fullbodyImages[0] = new ImageIcon("captain_america-removebg-preview.png");
		fullbodyImages[1] = new ImageIcon("deadpool-removebg-preview.png");
		fullbodyImages[2] = new ImageIcon("dr_strange-removebg-preview.png");
		fullbodyImages[3] = new ImageIcon("electro-removebg-preview.png");
		fullbodyImages[4] = new ImageIcon("ghost_rider-removebg-preview.png");
		fullbodyImages[5] = new ImageIcon("hela-removebg-preview.png");
		fullbodyImages[6] = new ImageIcon("hulk-removebg-preview.png");
		fullbodyImages[7] = new ImageIcon("iceman-removebg-preview.png");
		fullbodyImages[8] = new ImageIcon("ironman-removebg-preview.png");
		fullbodyImages[9] = new ImageIcon("loki-removebg-preview.png");
		fullbodyImages[10] = new ImageIcon("quicksilver-removebg-preview.png");
		fullbodyImages[11] = new ImageIcon("spiderman-removebg-preview.png");
		fullbodyImages[12] = new ImageIcon("thor-removebg-preview.png");
		fullbodyImages[13] = new ImageIcon("venom-removebg-preview.png");
		fullbodyImages[14] = new ImageIcon("yellow_jacket-removebg-preview.png");

		
		ImageIcon logo = new ImageIcon("logo.png");
		setIconImage(logo.getImage());
		
		champions = new JButton[15];
		
	//	panel = new JPanel();
		imagePanel.setLayout(new BorderLayout());
	//	setContentPane(new JLabel(new ImageIcon("choose.jpeg")));
		
				
		championsPanel = new JPanel();
		championsPanel.setPreferredSize(new Dimension(1000, 270) );
		//championsPanel.setBorder(new LineBorder(Color.black));
		championsPanel.setOpaque(false);
		
		for(int i=0; i<15; i++) {
			champions[i] = new JButton();
			champions[i].setIcon(new ImageIcon(images[i].getImage().getScaledInstance(100, 115, Image.SCALE_SMOOTH)));
			champions[i].setDisabledIcon(new ImageIcon(images[i].getImage().getScaledInstance(100, 115, Image.SCALE_SMOOTH)));
			champions[i].addActionListener(this);
			champions[i].addMouseListener(this);
			champions[i].setOpaque(false);
			champions[i].setContentAreaFilled(false);
			champions[i].setBorderPainted(false);
			champions[i].setFocusPainted(false);
		}
		
		for(int i=0; i<15; i++) {
			championsPanel.add(champions[i]);
		}
		imagePanel.add(championsPanel, BorderLayout.SOUTH);
		add(imagePanel); 
		
		attributePanel = new JPanel();
		attributePanel.setPreferredSize(new Dimension(333, 600) );
		attributePanel.setLayout(new FlowLayout());
	//	attributePanel.setBorder(new LineBorder(Color.black));
		attributePanel.add(txtArea , BorderLayout.CENTER);
		attributePanel.setOpaque(false);
		imagePanel.add(attributePanel, BorderLayout.CENTER);
		
		File fontFile = new File("AVENGEANCE HEROIC AVENGER AT.ttf");
		Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
		Font sizedFont = font.deriveFont(50f);
		
		player1name = new JLabel(player1.getName() + "'s Team");
		player1name.setFont(sizedFont);
		player1name.setOpaque(false);
		player1name.setForeground(new Color(0x9e1a1a));
	//	player1name.setEditable(false);
		player1name.setBounds(35,15,300,80);
		//player1name.setSize(900, 900);
	//	player1name.setText(player1.getName() + "'s Team");
	//	player1name.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
		
		player1Panel = new JPanel();
		player1Panel.setPreferredSize(new Dimension(333, 600) );
		player1Panel.setLayout(null);
//		player1Panel.setBorder(new LineBorder(Color.black));
		player1Panel.add(player1name);
		player1Panel.setOpaque(false);
		imagePanel.add(player1Panel, BorderLayout.WEST);
		
		
		player2name = new JLabel(player2.getName() + "'s Team");
		player2name.setFont(sizedFont);
		player2name.setOpaque(false);
		player2name.setForeground(new Color(0x9e1a1a));
		//player2name.setEditable(false);
		player2name.setBounds(35,15,300,80);
		//player2name.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));


		
		player2Panel = new JPanel();
		player2Panel.setPreferredSize(new Dimension(333, 600) );
		player2Panel.setLayout(null);
	//	player2Panel.setBorder(new LineBorder(Color.black));
		player2Panel.add(player2name);
		player2Panel.setOpaque(false);
		imagePanel.add(player2Panel, BorderLayout.EAST);
		
		
//		player1Panel.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
//		player2Panel.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
//		championsPanel.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
//		attributePanel.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));

		
		this.setVisible(true);
		this.revalidate();
		this.repaint();
		JOptionPane.showMessageDialog(null, "Choose your leader!", player1.getName() + "'s Team", JOptionPane.PLAIN_MESSAGE);

		
	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		for(int i=0 ;i<15; i++) {
			if(e.getSource() == champions[i]) {
				playSound("select.wav");
				champions[i].remove(championsPanel);
				if(counter ==0) {
					player1.setLeader(availableChampions.get(i));
					champions[i].setBounds(110,85,100,115);
					player1Panel.add(champions[i]);
					JOptionPane.showMessageDialog(null, "Now choose your other champions!",  player1.getName() + "'s Team", JOptionPane.PLAIN_MESSAGE);
				}else if(counter ==1) {
					champions[i].setBounds(110,205,100,115);
					player1Panel.add(champions[i], BorderLayout.CENTER);
				}else if(counter ==2) {
					champions[i].setBounds(110,325,100,115);
					player1Panel.add(champions[i], BorderLayout.SOUTH);
					JOptionPane.showMessageDialog(null, "Choose your leader!",  player2.getName() + "'s Team", JOptionPane.PLAIN_MESSAGE);
				}else if(counter ==3) {
					player2.setLeader(availableChampions.get(i));
					champions[i].setBounds(110,85,100,115);
					player2Panel.add(champions[i], BorderLayout.NORTH);
					JOptionPane.showMessageDialog(null, "Now choose your other champions!", player2.getName() + "'s Team", JOptionPane.PLAIN_MESSAGE);
				}else if(counter ==4) {
					champions[i].setBounds(110,205,100,115);
					player2Panel.add(champions[i], BorderLayout.CENTER);
				}else if(counter ==5) {
					champions[i].setBounds(110,325,100,115);
					player2Panel.add(champions[i], BorderLayout.SOUTH);
				}
				if(counter ==0 || counter ==1 || counter ==2) {
					player1.getTeam().add(availableChampions.get(i));
				}else if(counter == 3|| counter ==4 || counter ==5 ) {
					player2.getTeam().add(availableChampions.get(i));
				}
				champions[i].setEnabled(false);
				counter++;
				if(counter>5) {
					this.dispose();
					myGame = new Game(player1, player2);
					try {
						new BoardView(myGame, fullbodyImages, images);
					} catch (FontFormatException | IOException e1) {
						e1.printStackTrace();
					}
				}
				revalidate();
				repaint();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		this.revalidate();
		this.repaint();		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		this.revalidate();
		this.repaint();	
		tempLabel.setVisible(false);

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		for(int i=0; i<15;i++) {
			if(e.getSource() == champions[i]) {
				
				tempLabel.setIcon(new ImageIcon(fullbodyImages[i].getImage().getScaledInstance(300,300, Image.SCALE_SMOOTH)));
				tempLabel.setVisible(true);
				tempLabel.setBounds(400,10,300,300);
		//		tempLabel.setBackground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
				tempLabel.setOpaque(false);
				
				
				String output = "Name: " + availableChampions.get(i).getName() + '\n';
				output += "Max HP: " + availableChampions.get(i).getMaxHP() + '\n'; 
				output += "Speed: " + availableChampions.get(i).getSpeed() + '\n';
				output += "Mana: " +availableChampions.get(i).getMana() + '\n';
				output += "Action Points: " + availableChampions.get(i).getMaxActionPointsPerTurn() +'\n';
				output += "Attack Range: " + availableChampions.get(i).getAttackRange() + '\n';
				output += "Attack Damage: " + availableChampions.get(i).getAttackDamage() + '\n';
				output += "Ability 1: " + availableChampions.get(i).getAbilities().get(0).getName() + "\n";
				output += "Ability 2: " + availableChampions.get(i).getAbilities().get(1).getName() + "\n";
				output += "Ability 3: " + availableChampions.get(i).getAbilities().get(2).getName() +"\n" +"\n";
				
				for(int j=0; j<=2 ;j++) {
					Ability a = availableChampions.get(i).getAbilities().get(j);
					output += a.getName() + getAbilityType(a) + " ";
					output += "Cast Area: " + a.getCastArea().toString() + " ";
					output += "Cast Range: " + a.getCastRange() ;
					output += ", Action Points: " + a.getRequiredActionPoints() + "\n";
					output += "Mana Cost: " + a.getManaCost() + " ";
					output += "Cooldown: " + a.getBaseCooldown() +" (base) "+ a.getCurrentCooldown() +" (current)"+ " ";
					output += getExtraAttribute(a) +"\n" +"\n";
				}
				
				txtArea.setText(output);
				txtArea.setEditable(false);
				txtArea.setWrapStyleWord(true);
				txtArea.setLineWrap(true);
				txtArea.setForeground(new Color(0x000000));
				txtArea.setPreferredSize(new Dimension(300,200));
				txtArea.setFont(new Font("Verdana", Font.PLAIN, 7));
				txtArea.setOpaque(false);
				txtArea.setVisible(true);
				
				
				attributePanel.add(txtArea, BorderLayout.CENTER);
				attributePanel.add(tempLabel, BorderLayout.SOUTH);
				this.revalidate();
				this.repaint();
			}
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
	public static void loadAbilities(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			Ability a = null;
			AreaOfEffect ar = null;
			switch (content[5]) {
			case "SINGLETARGET":
				ar = AreaOfEffect.SINGLETARGET;
				break;
			case "TEAMTARGET":
				ar = AreaOfEffect.TEAMTARGET;
				break;
			case "SURROUND":
				ar = AreaOfEffect.SURROUND;
				break;
			case "DIRECTIONAL":
				ar = AreaOfEffect.DIRECTIONAL;
				break;
			case "SELFTARGET":
				ar = AreaOfEffect.SELFTARGET;
				break;

			}
			Effect e = null;
			if (content[0].equals("CC")) {
				switch (content[7]) {
				case "Disarm":
					e = new Disarm(Integer.parseInt(content[8]));
					break;
				case "Dodge":
					e = new Dodge(Integer.parseInt(content[8]));
					break;
				case "Embrace":
					e = new Embrace(Integer.parseInt(content[8]));
					break;
				case "PowerUp":
					e = new PowerUp(Integer.parseInt(content[8]));
					break;
				case "Root":
					e = new Root(Integer.parseInt(content[8]));
					break;
				case "Shield":
					e = new Shield(Integer.parseInt(content[8]));
					break;
				case "Shock":
					e = new Shock(Integer.parseInt(content[8]));
					break;
				case "Silence":
					e = new Silence(Integer.parseInt(content[8]));
					break;
				case "SpeedUp":
					e = new SpeedUp(Integer.parseInt(content[8]));
					break;
				case "Stun":
					e = new Stun(Integer.parseInt(content[8]));
					break;
				}
			}
			switch (content[0]) {
			case "CC":
				a = new CrowdControlAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), e);
				break;
			case "DMG":
				a = new DamagingAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), Integer.parseInt(content[7]));
				break;
			case "HEL":
				a = new HealingAbility(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[4]),
						Integer.parseInt(content[3]), ar, Integer.parseInt(content[6]), Integer.parseInt(content[7]));
				break;
			}
			availableAbilities.add(a);
			line = br.readLine();
		}
		br.close();
	}
	
	public static void loadChampions(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line = br.readLine();
		while (line != null) {
			String[] content = line.split(",");
			Champion c = null;
			switch (content[0]) {
			case "A":
				c = new AntiHero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;

			case "H":
				c = new Hero(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;
			case "V":
				c = new Villain(content[1], Integer.parseInt(content[2]), Integer.parseInt(content[3]),
						Integer.parseInt(content[4]), Integer.parseInt(content[5]), Integer.parseInt(content[6]),
						Integer.parseInt(content[7]));
				break;
			}

			c.getAbilities().add(findAbilityByName(content[8]));
			c.getAbilities().add(findAbilityByName(content[9]));
			c.getAbilities().add(findAbilityByName(content[10]));
			availableChampions.add(c);
			line = br.readLine();
		}
		br.close();
	}
	
	private static Ability findAbilityByName(String name) {
		for (Ability a : availableAbilities) {
			if (a.getName().equals(name))
				return a;
		}
		return null;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		txtArea.setVisible(false);
		tempLabel.setVisible(false);
		revalidate();
		repaint();
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
}
