package model.view;

import java.awt.Color;
import java.awt.Cursor;
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
import java.net.URL;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import engine.Player;

public class GameOver extends JFrame implements ActionListener{
	
	private ImageIcon logo;
	private JLabel label;
	private JButton exButton;
	private JTextArea title;
	private static Clip mainClip;
	
	public GameOver(Player winner) throws FontFormatException, IOException {
		this.setBounds(200,100,1100,800);
		this.setTitle("Marvel- Ultimate War");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		logo = new ImageIcon("logo.png");
		setIconImage(logo.getImage());
		playSound("win.wav");
		
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
		
		
		File fontFile = new File("AVENGEANCE HEROIC AVENGER AT.ttf");
		Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
		Font sizedFont = font.deriveFont(130f);
		
		label = new JLabel();
		title = new JTextArea(winner.getName() + "Won!");
		title.setFont(sizedFont);
		title.setForeground(new Color(0x9e1a1a));
		title.setBounds(240,200,1000,200);
		title.setOpaque(false);
		title.setEditable(false);
		label.add(title);
		ImageIcon background = new ImageIcon("startScreen.png");
		label.setIcon(background);
		setContentPane(label);
		
		
		double factor =1;
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
		exButton.setBounds(300,400,exitButtonIconUnhover.getIconWidth(), exitButtonIconUnhover.getIconHeight());

		add(exButton);
		//label.setBackground(Color.red);
		label.setBounds(0,0,1000,800);
		
		this.setVisible(true);
		this.revalidate();
		this.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == exButton) {
			this.dispose();
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
