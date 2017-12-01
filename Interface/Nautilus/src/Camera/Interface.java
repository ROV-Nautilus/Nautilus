package Camera;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.jcraft.jsch.JSchException;

import Carto.Cartographie;
import SSH.Exec;
import SSH.Shell;

public class Interface extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** Camera FPV */
    public String jpgURL1="http://169.254.14.03:8081/";
    public String mjpgURL1="http://169.254.14.03:8081/";
    
    /** Camera Carto */
    public String jpgURL="http://169.254.14.03:8000/";
    public String mjpgURL="http://169.254.14.03:8000/stream.mjpg";
    
    public Container win;
    
    GridBagConstraints c = new GridBagConstraints();
    Panel axPanel = new Panel();
    Panel axPanel1 = new Panel();
    Cartographie carto = null;
    public String nomPhoto = "carto";
    public int posX = 0;
    public int posY = 0;
    public int vitX = 0;
    public int vitY = 0;


		//Constructeur
		public Interface(String titre, int x, int y, int longueur, int largeur, boolean b){
			super(titre);
			super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			super.setLocation(x,y);
			super.setSize(longueur,largeur);
			
			//JFrame jframe = new JFrame();
			
			/* Construction du Conteneur */
			win = getContentPane();
			win.setLayout (new GridBagLayout());
			
			
	    	/** Barre de menu */
	    	JMenuBar menu= new JMenuBar();
			JMenu fichier=new JMenu("Fichier");
			JMenuItem quitter= new JMenuItem("Quitter");
			quitter.addActionListener(this);
			fichier.add(quitter);
			menu.add(fichier);
			
			JMenu ssh =new JMenu("SSH");
			JMenuItem shell= new JMenuItem("Shell");
			JMenuItem commande= new JMenuItem("Commande");
			shell.addActionListener(this);
			commande.addActionListener(this);
			ssh.add(shell);
			ssh.add(commande);
			
			menu.add(ssh);
			setJMenuBar(menu);
			
			/** Bouton */
			JPanel pan1=new JPanel();
			pan1.setLayout(new GridLayout(2,3));
			
			JButton demarrer = new JButton("Demarrer Camera 1");
			demarrer.addActionListener(this);
			demarrer.setBackground(Color.white);
			pan1.add(demarrer);
			
			JButton demarrer2 = new JButton("Demarrer Camera 2");
			demarrer2.addActionListener(this);
			demarrer2.setBackground(Color.white);
			pan1.add(demarrer2);
			
			JButton photo = new JButton("Prendre Photo");
			photo.addActionListener(this);
			photo.setBackground(Color.white);
			pan1.add(photo);
			
			JButton arret = new JButton("Arreter Camera 1");
			arret.addActionListener(this);
			arret.setBackground(Color.white);
			pan1.add(arret);
			
			JButton arret1 = new JButton("Arreter Camera 2");
			arret1.addActionListener(this);
			arret1.setBackground(Color.white);
			pan1.add(arret1);
			
			JButton carte = new JButton("Carte");
			carte.addActionListener(this);
			carte.setBackground(Color.white);
			pan1.add(carte);
			
			/* Placement */
			pan1.setMinimumSize(new Dimension(1366,50));
			pan1.setMaximumSize(new Dimension(1366,50));
			pan1.setPreferredSize(new Dimension(1366,50));
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.LAST_LINE_START; //bottom of space
			c.gridx = 0;       //Colonne
			c.gridy = 3;       //Ligne
			c.weighty = 0.0;
			c.gridwidth = 3;   //Prend 3 Colonne
			c.gridheight = 1;   //Prend 1 ligne
			win.add(pan1,c);
			/* Capteurs */
			
			JPanel pan2=new JPanel();
			pan2.setBackground(Color.GRAY);
			pan2.setLayout(new GridLayout(4,1));
			
			JLabel positionX = new JLabel(" Position X = "+posX);
			JLabel positionY = new JLabel(" Position Y = "+posY);
			JLabel vitesseX = new JLabel(" Vitesse X = "+vitY);
			JLabel vitesseY = new JLabel(" Vitesse Y = "+vitY);
			pan2.add(positionX);
			pan2.add(positionY);
			pan2.add(vitesseX);
			pan2.add(vitesseY);
			
			pan2.setMinimumSize(new Dimension(86,80));
			pan2.setMaximumSize(new Dimension(86,80));
			pan2.setPreferredSize(new Dimension(86,80));
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.FIRST_LINE_START;
			c.gridx = 0; // Colonne
			c.gridy = 0; // Ligne
			c.weighty = 1.0;
			c.weightx = 0.0;
			c.gridwidth = 1;   //Prend 1 Colonne
			c.gridheight = 1;   //Prend 1 ligne
			
			win.add(pan2,c);
			
			this.setResizable(false);
	    	super.setVisible(b);
		}
	
		
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("Quitter")){
			System.exit(0);
		}
		if(cmd.equals("Demarrer Camera 1")){
			Exec ex4 = new Exec("python3 rpi_camera_demarrer.py");
			try {
				Thread.sleep(600);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			axPanel = new Camera(win,jpgURL,mjpgURL,"FPV");
	    	new Thread(axPanel).start();
	    	
	    	axPanel.setMinimumSize(new Dimension(640,480));
			axPanel.setMaximumSize(new Dimension(640,480));
			axPanel.setPreferredSize(new Dimension(640,480));
			c.fill = GridBagConstraints.VERTICAL;
			c.anchor = GridBagConstraints.PAGE_START;
			c.gridx = 1; // Colonne
			c.gridy = 0; // Ligne
			c.weighty = 0.0;
			c.weightx = 0.0;
			c.gridwidth = 1;   //Prend 1 Colonne
			c.gridheight = 2;   //Prend 1 ligne
			win.add(axPanel,c);
	    	
		}
		if(cmd.equals("Demarrer Camera 2")){
			Exec ex1 = new Exec("sudo motion");
			Exec ex2 = new Exec("sudo systemctl start motion.service");
			try {
				Thread.sleep(600);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			axPanel1 = new Camera(win,jpgURL1,mjpgURL1,"Cartographie");
	    	new Thread(axPanel1).start();
	    	
	    	axPanel1.setMinimumSize(new Dimension(640,480));
			axPanel1.setMaximumSize(new Dimension(640,480));
			axPanel1.setPreferredSize(new Dimension(640,480));
			c.fill = GridBagConstraints.VERTICAL;
			c.anchor = GridBagConstraints.PAGE_START;
			c.gridx = 2; // Colonne
			c.gridy = 0; // Ligne
			c.weighty = 0.0;
			c.weightx = 0.0;
			c.gridwidth = 1;   //Prend 1 Colonne
			c.gridheight = 2;   //Prend 2 ligne
			win.add(axPanel1,c);
		}
		if(cmd.equals("Arreter Camera 1")){
			
		}
		if(cmd.equals("Arreter Camera 2")){
			Exec ex = new Exec("sudo /etc/init.d/motion stop");
		}
		if(cmd.equals("Shell")){
			Shell s = new Shell();
		}
		if(cmd.equals("Commande")){
			Exec ex = new Exec();
		}
		
		if(cmd.equals("Prendre Photo")){
			
			BufferedImage img = new BufferedImage(axPanel.getWidth(), axPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
			axPanel.print(img.getGraphics());
		    try {
		        ImageIO.write(img, "jpg", new File(nomPhoto+".jpg"));
		    }
		    catch (IOException e5) {
		        e5.printStackTrace();
		    }
		    nomPhoto=nomPhoto+"1";
		}
		
		if(cmd.equals("Carte")){
			carto = new Cartographie("Cartographie",0,0,true);
		}
	}
	
}
