package model.view;

import java.awt.BorderLayout;
import java.awt.Button;
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
import java.io.File;
import java.io.IOException;
import java.net.URL;

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
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.junit.runners.model.FrameworkField;

import Controller.Controller;
import engine.Game;
import engine.Player;
import engine.PlayerListener;

public class DraftScreen extends JFrame implements ActionListener, PlayerListener{
	
	///use text field not buttons
	private JTextField name1;
	private JTextField name2;
	private JButton done;
	private JLabel label;
	private static Clip mainClip;
	private JLabel title;
	
	public DraftScreen() throws FontFormatException, IOException {
		this.setBounds(200,100,1100,800);
		this.setTitle("Marvel- Ultimate War");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		
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
		
		
		label = new JLabel();
		ImageIcon background = new ImageIcon("startScreen.png");
		background.getImage().getScaledInstance(1100, 800, Image.SCALE_SMOOTH);
		//setContentPane(new JLabel(background));
		label.setIcon(new ImageIcon(background.getImage().getScaledInstance(1100, 800, Image.SCALE_SMOOTH)));
		setContentPane(label);
		label.setForeground(new Color(0x000000));
		label.setBounds(100,50,1100,800);
			
		File fontFile = new File("AVENGEANCE HEROIC AVENGER AT.ttf");
		Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
		Font sizedFont = font.deriveFont(100f);
		
		title = new JLabel("Enter your names");
		title.setFont(sizedFont);
		title.setForeground(new Color(0x9e1a1a));
		title.setOpaque(false);
		title.setBounds(200,100,1000,200);
		label.add(title);
		
		sizedFont = font.deriveFont(40f);

		
		name1 = new JTextField();
		name2 = new JTextField();
		name1.setText("Player 1");
		name2.setText("Player 2");
		name1.setBounds(250,300,250,100);
		name2.setBounds(550,300,250,100);
		name1.setFont(sizedFont);
		name2.setFont(sizedFont);
		name1.setForeground( Color.darkGray);
		name2.setForeground( Color.darkGray);
		
		ImageIcon redButtonIcon = new ImageIcon("redbutton.png");
			
		done = new JButton("Done");
		done.setHorizontalTextPosition(SwingConstants.CENTER);
		done.setBorderPainted(false);
		done.setContentAreaFilled(false);
		done.setForeground(new Color(0xf5edd6));
		done.setFont(sizedFont);
		done.setIcon(new ImageIcon(redButtonIcon.getImage().getScaledInstance(200,100, Image.SCALE_SMOOTH)));
		done.setBorder(BorderFactory.createRaisedBevelBorder());
		done.setBounds(420, 450, 200, 100);
		
		done.addActionListener(this);
		
		add(name1);
		add(name2);
		add(done);
	//	this.add(panel, BorderLayout.CENTER);
		
//		name1.setPreferredSize(new Dimension(200,100));
//		name2.setPreferredSize(new Dimension(200,100));
//		name1.addActionListener(e -> {
//			name1.setText("Player 1");
//			String userInput = JOptionPane.showInputDialog(null,"Enter your name", "Player 1", JOptionPane.QUESTION_MESSAGE);
//			name1.setText(userInput);
//	     });
//		name2.addActionListener(e -> {
//			name2.setText("Player 1");
//			String userInput = JOptionPane.showInputDialog(null,"Enter your name", "Player 2", JOptionPane.QUESTION_MESSAGE);
//			name2.setText(userInput);
//	     });
		
		//championGrid.setLayout(new GridLayout(3,5));
		this.revalidate();
		this.repaint();
		
		
		}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == done) {
			playSound("select.wav");
			this.dispose();
			try {
				new Controller(name1.getText(), name2.getText());
			//	new ChooseChampions();
			} catch (FontFormatException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
	}

	@Override
	public void onSetName() {
		
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
