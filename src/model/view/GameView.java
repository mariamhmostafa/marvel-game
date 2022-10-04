package model.view;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.Point;
import java.awt.Taskbar;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.naming.NameAlreadyBoundException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.Border;

import org.junit.runners.model.FrameworkField;


public class GameView extends JFrame implements ActionListener{
	private JButton startButton;
	private JPanel panel;
	private JButton exButton;
	private JLabel label;
	private JTextArea name;
	private ActionListener listener;
	private static Clip mainClip;
	
	
	public GameView() throws FontFormatException, IOException{
	
		//new window
		this.setBounds(200,100,1100,800);
		this.setTitle("Marvel- Ultimate War");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		
		//applying logo 
		ImageIcon logo = new ImageIcon("logo.png");
		setIconImage(logo.getImage());
		
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
		
		//background music? 
		playCont("Avengers background.wav");
		
		//new panel
		panel = new JPanel();
		panel.setLayout(null);
		
		
		//font
		File fontFile = new File("AVENGEANCE HEROIC AVENGER AT.ttf");
		Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
		Font sizedFont = font.deriveFont(130f);
		
		//new name
//		name = new JTextArea("Marvel");
//		name.setFont(sizedFont);
//		name.setBounds(300,100,800,200);
//		name.setOpaque(false);
//		name.setEditable(false);
//		name.setForeground(new Color(0x9e1a1a));
//		
//		JTextArea name2 = new JTextArea("Ultimate War");
//		name2.setFont(sizedFont);
//		name2.setBounds(160,300,800,200);
//		name2.setOpaque(false);
//		name2.setEditable(false);
//		name2.setForeground(new Color(0x9e1a1a));
//		
		//making the label with the picture and title
		label = new JLabel();
		ImageIcon background = new ImageIcon("background.png");
		label.setIcon(background);
		setContentPane(label);

		label.setBackground(Color.red);
		label.setBounds(0,0,0,0);


		
		File fontFile2 = new File("Butter Layer.ttf");
		Font font2 = Font.createFont(Font.TRUETYPE_FONT, fontFile2);

		//start and exit buttons
		startButton = new JButton();
		startButton.setBorderPainted(false);
		startButton.setContentAreaFilled(false);
		double factor = 1;
		ImageIcon playUnhover = new ImageIcon("play_unhover.png");
		Image playButtonImageUnhover = playUnhover.getImage().getScaledInstance
				((int) (playUnhover.getIconWidth() * factor),
						(int) (playUnhover.getIconHeight() * factor), Image.SCALE_SMOOTH);
		ImageIcon playButtonIconUnhover = new ImageIcon(playButtonImageUnhover);
		ImageIcon playHover = new ImageIcon("play_hover.png");
		Image playButtonImageHover = playHover.getImage().getScaledInstance
				((int) (playHover.getIconWidth() * factor),
						(int) (playHover.getIconHeight() * factor), Image.SCALE_SMOOTH);
		ImageIcon playButtonIconHover = new ImageIcon(playButtonImageHover);
		startButton.setIcon(playButtonIconUnhover);
		startButton.setOpaque(false);
		startButton.setFocusPainted(false);
		startButton.addActionListener(this);
		startButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				startButton.setIcon(playButtonIconHover);
			}
		
			public void mouseExited(MouseEvent e) {
				startButton.setIcon(playButtonIconUnhover);
			}
		});
		
		exButton = new JButton();
		exButton.setBorderPainted(false);
		exButton.setContentAreaFilled(false);
		ImageIcon exitUnhover = new ImageIcon("exit_unhover.png");
		Image exitButtonImageUnhover = exitUnhover.getImage().getScaledInstance
				((int) (exitUnhover.getIconWidth() * factor),
						(int) (exitUnhover.getIconHeight() * factor), Image.SCALE_SMOOTH);
		ImageIcon exitButtonIconUnhover = new ImageIcon(exitButtonImageUnhover);
		ImageIcon exitHover = new ImageIcon("exit_hover.png");
		Image exitButtonImageHover = exitHover.getImage().getScaledInstance
				((int) (exitHover.getIconWidth() * factor),
						(int) (exitHover.getIconHeight() * factor), Image.SCALE_SMOOTH);
		ImageIcon exitButtonIconHover = new ImageIcon(exitButtonImageHover);
		exButton.setIcon(exitButtonIconUnhover);
		exButton.setOpaque(false);
		exButton.setFocusPainted(false);
		exButton.addActionListener(this);
		exButton.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				exButton.setIcon(exitButtonIconHover);
			}
		
			public void mouseExited(MouseEvent e) {
				exButton.setIcon(exitButtonIconUnhover);
			}
		
		});

	    //setting bounds to start and exit buttons
		startButton.setBounds(300, 400, playButtonIconUnhover.getIconWidth(), playButtonIconUnhover.getIconHeight());
		exButton.setBounds(300,500,exitButtonIconUnhover.getIconWidth(), exitButtonIconUnhover.getIconHeight());
		

		add(startButton);
		add(exButton);
	
		this.setVisible(true);
		this.revalidate();
		this.repaint();
		
	}
	

	public static void main(String[] args) throws FontFormatException, IOException{
		new GameView();
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == startButton) {
			playSound("select.wav");
			this.dispose();
			try {
				new DraftScreen();
			} catch (FontFormatException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		if(e.getSource() == exButton) {
			playSound("select.wav");
			this.dispose();
		}
		
	}
	
	public void playCont(String filename)
	{
	    try
	    {	
	    	File file = new File(filename);
	        mainClip = AudioSystem.getClip();
	        mainClip.open(AudioSystem.getAudioInputStream(file));
	        mainClip.start();
	        mainClip.loop(mainClip.LOOP_CONTINUOUSLY);
	    }
	    catch (Exception exc)
	    {
	        exc.printStackTrace(System.out);
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
}
