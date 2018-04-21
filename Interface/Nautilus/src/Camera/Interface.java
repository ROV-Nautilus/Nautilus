package Camera;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import com.jcraft.jsch.JSchException;
import com.sun.j3d.utils.applet.MainFrame;
import javax.vecmath.*;

import Capteurs.ImportWavefront;
import Capteurs.Matrice;
import Capteurs.Moteurs;
import Capteurs.Pression;
import Carto.Cartographie;
import SSH.Exec;
import SSH.Shell;

public class Interface extends JFrame implements ActionListener, KeyListener{

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
    
    /** Container principal avec sa contrainte*/
    public Container win;
    GridBagConstraints c = new GridBagConstraints();
    
    /** Panel lié à l'affichage video */
    Panel axPanel = new Panel(); //Camera carto
    Panel axPanel1 = new Panel(); //Camera FPV
    
    /** Initialisation pour la cartographie*/
    Cartographie carto = null;
    public String nomPhoto = "carto";
    
    /**  Valeur partagé*/
    public static int posX = 0;
    public static int posY = 0;
    public static int vitX = 0;
    public static int vitY = 0;
    public static int m1 = 150;
    public static int m2 = 120;
    public static int m3 = 120;
    public static Exec exPression = new Exec();
    public static Exec exMatrice1 = new Exec();
    public static Exec exMatrice2 = new Exec();
    public static Exec exCam = new Exec();
    public static Exec exMoteur = new Exec();
    public static String pre = "0";
    public static String tempC = "0";
    public static String tempF = "0";
    JPanel pan2=new JPanel();
    JPanel panM1=new JPanel();
    JPanel panM2=new JPanel();
    JPanel panM3=new JPanel();
    
    public static double rotX = 0.0f;
    public static double rotY = 0.0f;
    public static double rotZ = 0.0f;
    public static double transX = 0.0f;
    public static double transY = 0.0f;
    public static double transZ = 0.0f;
    public static double scale = 0.6f;
    
    
    /** Affichage */
    JLabel positionX = new JLabel(" Position X = "+posX,SwingConstants.CENTER);
	JLabel positionY = new JLabel(" Position Y = "+posY,SwingConstants.CENTER);
	JLabel vitesseX = new JLabel(" Vitesse X = "+vitY,SwingConstants.CENTER);
	JLabel vitesseY = new JLabel(" Vitesse Y = "+vitY,SwingConstants.CENTER);
	public static JLabel moteur1 = new JLabel(""+m1+"    ",SwingConstants.RIGHT);
	public static JLabel moteur2 = new JLabel(""+m2+"    ",SwingConstants.RIGHT);
	public static JLabel moteur3 = new JLabel(""+m3+"    ",SwingConstants.RIGHT);
	public static JLabel pression = new JLabel(" Pression = "+pre,SwingConstants.CENTER);
	public static JLabel temperatureC = new JLabel(" TemperatureC = "+tempC,SwingConstants.CENTER);
	public static JLabel temperatureF = new JLabel(" TemperatureF = "+tempF,SwingConstants.CENTER);
	
	/** Pour l'affichage 3d*/
	public static ImportWavefront singe = null;

	/** Pour le clavier */
    boolean isPressed;

		//Constructeur
		public Interface(String titre, int x, int y, int longueur, int largeur, boolean b){
			super(titre);
			super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			super.setLocation(0,0);
			
			super.setExtendedState(JFrame.MAXIMIZED_BOTH);
			this.setIconImage(new ImageIcon(getClass().getResource("/Nautilus.jpg")).getImage());
			
			/** Initialisation des moteurs*/
			Moteurs ex1 = new Moteurs(m1,m2,m3);
			Thread a1 = new Thread(ex1);
			a1.start();
			
			/** Initialisation matrice */
			Interface.exMatrice1.setCommande("minimu9-ahrs --output euler &");
			Thread a8 = new Thread(Interface.exMatrice1);
			a8.start();
			
			/** Construction du Conteneur */
			win = getContentPane();
			win.setLayout (new GridBagLayout());
			
			/** Premier Bouton FPV */
			axPanel1.setMinimumSize(new Dimension(400,210));
			axPanel1.setMaximumSize(new Dimension(400,210));
			axPanel1.setPreferredSize(new Dimension(400,210));
			c.fill = GridBagConstraints.BOTH;
			c.anchor = GridBagConstraints.CENTER;
			c.gridx = 0; // Colonne
			c.gridy = 0; // Ligne
			c.weighty = 0.0;
			c.weightx = 0.0;
			c.gridwidth = 1;   //Prend 1 Colonne
			c.gridheight = 1;   //Prend 1 ligne
			c.insets = new Insets(0, 0, 0, 200); // Insets(margeSupérieure, margeGauche, margeInférieur, margeDroite)
			
			/* Affichage bouton*/
			Bouton1 FPV = new Bouton1("FPV OFF",getClass().getResource("/BoutonFPV.jpg"), getClass().getResource("/BoutonFPVRoll.jpg"),getClass().getResource("/BoutonFPVPressed.jpg"));
			Font font = new Font("Arial",Font.BOLD,20);
			FPV.setFont(font);
			FPV.addActionListener(this);
			FPV.addKeyListener(this);
			axPanel1.add(FPV);
				
			win.add(axPanel1,c);
				
			/** Deuxieme Bouton Carto */
			axPanel.setMinimumSize(new Dimension(400,210));
			axPanel.setMaximumSize(new Dimension(400,210));
			axPanel.setPreferredSize(new Dimension(400,210));
			c.fill = GridBagConstraints.BOTH;
			c.anchor = GridBagConstraints.CENTER;
			c.gridx = 1; // Colonne
			c.gridy = 0; // Ligne
			c.weighty = 0.0;
			c.weightx = 0.0;
			c.gridwidth = 1;   //Prend 1 Colonne
			c.gridheight = 1;   //Prend 1 ligne
			c.insets = new Insets(0, 200, 0, 0); // Insets(margeSupérieure, margeGauche, margeInférieur, margeDroite)
				
			Bouton1 CARTO = new Bouton1("CARTO OFF",getClass().getResource("/BoutonFPV.jpg"), getClass().getResource("/BoutonFPVRoll.jpg"),getClass().getResource("/BoutonFPVPressed.jpg"));
			Font font1 = new Font("Arial",Font.BOLD,20);
			CARTO.setFont(font1);
			CARTO.addActionListener(this);
			CARTO.addKeyListener(this);
			axPanel.add(CARTO);
				
			win.add(axPanel,c);
			
			this.setFocusable(true);
			
			/** Ecouteur du Clavier */
			this.addKeyListener(this);
			win.addKeyListener(this);
			pan2.addKeyListener(this);
			panM1.addKeyListener(this);
			panM2.addKeyListener(this);
			panM3.addKeyListener(this);
			axPanel.addKeyListener(this);
			axPanel1.addKeyListener(this);
			
			/** Affichage de la fenetre*/
	    	super.setVisible(b);
		}
	
		
		/** Action de la souris sur les panel */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		/** Bouton FPV */
		if(cmd.equals("FPV OFF")) {
			
			/* Video FPV*/
			this.exCam.setCommande("sudo motion");
			Thread a2 = new Thread(exCam);
			a2.start();
			/* Attente du lancement du flux video*/
			while( a2.isAlive()) {
			}
			/* Double commande pour un bon lancement*/
			this.exCam.setCommande("sudo systemctl start motion.service");
			Thread a1 = new Thread(exCam);
			a1.start();
			/* Attente du lancement du flux video*/
			while(a1.isAlive()) {
			}
			
			/* Affichage mis a nu*/
			axPanel.removeAll();
			axPanel.repaint();
			axPanel1.removeAll();
			axPanel1.repaint();
			win.removeAll();
			win.setLayout (new GridBagLayout());
			win.repaint();
			
			/* Recuperation des valeurs du capteur Pression/Temperature/Matrice */
	    	Pression ex4 = new Pression();
			Thread a4 = new Thread(ex4);
			a4.start();
			
			/* Affichage de la video */
			axPanel1 = new Camera(win,jpgURL1,mjpgURL1,"Cartographie");
	    	new Thread(axPanel1).start();
	    	
	    	axPanel1.setMinimumSize(new Dimension(1280,720));
			axPanel1.setMaximumSize(new Dimension(1280,720));
			axPanel1.setPreferredSize(new Dimension(1280,720));
			c.fill = GridBagConstraints.BOTH;
			c.anchor = GridBagConstraints.CENTER;
			c.gridx = 0; // Colonne
			c.gridy = 0; // Ligne
			c.weighty = 0;
			c.weightx = 0;
			c.gridwidth = 4;   //Prend 2 Colonne
			c.gridheight = 3;   //Prend 2 ligne
			c.insets = new Insets(0, 0, 0, 0); // Insets(margeSupérieure, margeGauche, margeInférieur, margeDroite)
			win.add(axPanel1,c);
			axPanel1.repaint();
			
			
			/* Affichage Bouton Carto */
			
			axPanel.setMinimumSize(new Dimension(400,210));
			axPanel.setMaximumSize(new Dimension(400,210));
			axPanel.setPreferredSize(new Dimension(400,210));
			c.fill = GridBagConstraints.BOTH;
			c.anchor = GridBagConstraints.CENTER;
			c.gridx = 4; // Colonne
			c.gridy = 0; // Ligne
			c.weighty = 0.0;
			c.weightx = 0.0;
			c.gridwidth = 1;   //Prend 1 Colonne
			c.gridheight = 1;   //Prend 1 ligne
			c.insets = new Insets(0, 25, 0, 0); // Insets(margeSupérieure, margeGauche, margeInférieur, margeDroite)
			
			Bouton1 CARTO = new Bouton1("CARTO OFF",getClass().getResource("/BoutonFPV.jpg"), getClass().getResource("/BoutonFPVRoll.jpg"),getClass().getResource("/BoutonFPVPressed.jpg"));
			Font font1 = new Font("Arial",Font.BOLD,20);
			CARTO.setFont(font1);
			CARTO.addActionListener(this);
			CARTO.addKeyListener(this);
			axPanel.add(CARTO);
			win.add(axPanel,c);
			axPanel.repaint();
			
			/* Cadre Position et Vitesse */
			
			pan2.setBackground(Color.GRAY);
			pan2.setLayout(new GridLayout(3,1));
			Font font2 = new Font("Arial",Font.BOLD,20);
			//positionX.setFont(font2);
			//positionY.setFont(font2);
			//vitesseX.setFont(font2);
			//vitesseY.setFont(font2);
			pression.setFont(font2);
			temperatureC.setFont(font2);
			temperatureF.setFont(font2);
			//pan2.add(positionX);
			//pan2.add(positionY);
			//pan2.add(vitesseX);
			//pan2.add(vitesseY);
			pan2.add(pression);
			pan2.add(temperatureC);
			pan2.add(temperatureF);
			
			pan2.setSize(new Dimension(400,200));
			pan2.setMinimumSize(new Dimension(400,200));
			pan2.setMaximumSize(new Dimension(400,200));
			pan2.setPreferredSize(new Dimension(400,200));
			c.fill = GridBagConstraints.BOTH;
			c.anchor = GridBagConstraints.CENTER;
			c.gridx = 4; // Colonne
			c.gridy = 1; // Ligne
			c.weighty = 0.0;
			c.weightx = 0.0;
			c.gridwidth = 1;   //Prend 1 Colonne
			c.gridheight = 1;   //Prend 1 ligne
			c.insets = new Insets(25, 25, 0, 0); // Insets(margeSupérieure, margeGauche, margeInférieur, margeDroite)
			
			win.add(pan2,c);
			
			/* Cadre M1 M2 M3 */
			
			panM1=new JPanel(){
	            protected void paintComponent(Graphics g){
	                super.paintComponent(g);
	                ImageIcon m = new ImageIcon("moteur1.jpg");
	                Image monImage = m.getImage();
	                g.drawImage(monImage, 0, 0,this);
	            }
	        };
			
			panM1.setBackground(Color.GRAY);
			panM1.setLayout(new GridLayout(1,1));
			
			Font font3 = new Font("Arial",Font.BOLD,20);
			moteur1.setFont(font3);
			panM1.add(moteur1);
			
			panM1.setSize(new Dimension(200,100));
			panM1.setMinimumSize(new Dimension(200,100));
			panM1.setMaximumSize(new Dimension(200,100));
			panM1.setPreferredSize(new Dimension(200,100));
			c.fill = GridBagConstraints.BOTH;
			c.anchor = GridBagConstraints.CENTER;
			c.gridx = 0; // Colonne
			c.gridy = 3; // Ligne
			c.weighty = 0.0;
			c.weightx = 0.0;
			c.gridwidth = 1;   //Prend 1 Colonne
			c.gridheight = 1;   //Prend 1 ligne
			c.insets = new Insets(77, 0, 77, 0); // Insets(margeSupérieure, margeGauche, margeInférieur, margeDroite)
			
			win.add(panM1,c);
			
			
			panM2=new JPanel(){
	            protected void paintComponent(Graphics g){
	                super.paintComponent(g);
	                ImageIcon m = new ImageIcon("moteur2.jpg");
	                Image monImage = m.getImage();
	                g.drawImage(monImage, 0, 0,this);
	            }
	        };
			
			panM2.setBackground(Color.GRAY);
			panM2.setLayout(new GridLayout(1,1));
			
			Font font4 = new Font("Arial",Font.BOLD,20);
			moteur2.setFont(font4);
			panM2.add(moteur2);
			
			panM2.setSize(new Dimension(200,100));
			panM2.setMinimumSize(new Dimension(200,100));
			panM2.setMaximumSize(new Dimension(200,100));
			panM2.setPreferredSize(new Dimension(200,100));
			c.fill = GridBagConstraints.BOTH;
			c.anchor = GridBagConstraints.CENTER;
			c.gridx = 1; // Colonne
			c.gridy = 3; // Ligne
			c.weighty = 0.0;
			c.weightx = 0.0;
			c.gridwidth = 1;   //Prend 1 Colonne
			c.gridheight = 1;   //Prend 1 ligne
			c.insets = new Insets(77, 50, 77, 50); // Insets(margeSupérieure, margeGauche, margeInférieur, margeDroite)
			
			win.add(panM2,c);
			
			panM3=new JPanel(){
	            protected void paintComponent(Graphics g){
	                super.paintComponent(g);
	                ImageIcon m = new ImageIcon("moteur3.jpg");
	                Image monImage = m.getImage();
	                g.drawImage(monImage, 0, 0,this);
	            }
	        };
			
			panM3.setLayout(new GridLayout(1,1));
			
			Font font5 = new Font("Arial",Font.BOLD,20);
			moteur3.setFont(font5);
			panM3.add(moteur3);
			
			panM3.setSize(new Dimension(200,100));
			panM3.setMinimumSize(new Dimension(200,100));
			panM3.setMaximumSize(new Dimension(200,100));
			panM3.setPreferredSize(new Dimension(200,100));
			c.fill = GridBagConstraints.BOTH;
			c.anchor = GridBagConstraints.CENTER;
			c.gridx = 2; // Colonne
			c.gridy = 3; // Ligne
			c.weighty = 0.0;
			c.weightx = 0.0;
			c.gridwidth = 1;   //Prend 1 Colonne
			c.gridheight = 1;   //Prend 1 ligne
			c.insets = new Insets(77, 0, 77, 0); // Insets(margeSupérieure, margeGauche, margeInférieur, margeDroite)
			
			win.add(panM3,c);
			
			
			/* Rendu mini rov*/
			
			ImportWavefront.applet = false;
			singe = new ImportWavefront();
			
			singe.setSize(new Dimension(256, 256));
			singe.setMinimumSize(new Dimension(256, 256));
			singe.setMaximumSize(new Dimension(256, 256));
			singe.setPreferredSize(new Dimension(256, 256));
			c.fill = GridBagConstraints.BOTH;
			c.anchor = GridBagConstraints.CENTER;
			c.gridx = 4; // Colonne
			c.gridy = 3; // Ligne
			c.weighty = 0.0;
			c.weightx = 0.0;
			c.gridwidth = 1;   //Prend 1 Colonne
			c.gridheight = 1;   //Prend 1 ligne
			c.insets = new Insets(0, 25, 0, 0); // Insets(margeSupérieure, margeGauche, margeInférieur, margeDroite)
			win.add(singe,c);
			
		}
		
		/** Bouton carto */
		if(cmd.equals("CARTO OFF")) {
			/* Reste à developper */
		}
		/** Test et autres*/
		
		/** Demarre l'autre caméra*/
		if(cmd.equals("Demarrer Camera 1")){
			Exec ex4 = new Exec("python3 rpi_camera_demarrer.py");
			try {
				Thread.sleep(600);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			axPanel = new Camera(win,jpgURL,mjpgURL,"FPV");
	    	new Thread(axPanel).start();
		}
		
		/** Commande pour arreter la camera FPV*/
		if(cmd.equals("Arreter Camera 2")){
			Exec ex = new Exec("sudo /etc/init.d/motion stop");
		}
		/** Ceci permet d'afficher le shell de la raspberry dans la console eclipse */
		if(cmd.equals("Shell")){
			Shell s = new Shell();
		}
		
		/** Enregistre l'image venant de axPanel en jpg*/
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
		/** Lance la fenetre avec la carte et les photos prises (non developé)*/
		if(cmd.equals("Carte")){
			carto = new Cartographie("Cartographie",0,0,true);
		}
	}


    
    /** Action touche du clavier */
    public void keyPressed(KeyEvent e) {
    	int key = e.getKeyCode();
    	if(!isPressed) {
    		if (key == KeyEvent.VK_UP) {
    			Moteurs ex1 = new Moteurs(m1,m2+10,m3+10);
    			Thread a1 = new Thread(ex1);
    			a1.start();
    			System.out.println("Avant appuyé");
    		}
    		if (key == KeyEvent.VK_DOWN) {
    			Moteurs ex1 = new Moteurs(m1,m2-10,m3-10);
    			Thread a1 = new Thread(ex1);
    			a1.start();
    			System.out.println("Arriere appuyé");
    		}
    		if (key == KeyEvent.VK_SPACE) {
    			Moteurs ex1 = new Moteurs(150,120,120);
    			Thread a1 = new Thread(ex1);
    			a1.start();
    			System.out.println("Stop");
    		}
    		if (key == KeyEvent.VK_LEFT) {
    			Moteurs ex1 = new Moteurs(m1,m2-10,m3+10);
    			Thread a1 = new Thread(ex1);
    			a1.start();
    			System.out.println("Gauche appuyé");
    		}
    		if (key == KeyEvent.VK_RIGHT) {
    			Moteurs ex1 = new Moteurs(m1,m2+10,m3-10);
    			Thread a1 = new Thread(ex1);
    			a1.start();
    			System.out.println("Droite appuyé");
    		}
    		if (key == KeyEvent.VK_Z) {
    			Moteurs ex1 = new Moteurs(m1+1,m2,m3);
    			Thread a1 = new Thread(ex1);
    			a1.start();
    			System.out.println("Haut appuyé");
    		}
    		if (key == KeyEvent.VK_S) {
    			Moteurs ex1 = new Moteurs(m1-1,m2,m3);
    			Thread a1 = new Thread(ex1);
    			a1.start();
    			System.out.println("Bas appuyé");
    		}
    		//isPressed = true;
    	}
    }
    
    public void keyReleased(KeyEvent e) {
    	int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP) {
        	System.out.println("Avant relaché");
        }
        if (key == KeyEvent.VK_DOWN) {
			System.out.println("Arriere relaché");
		}
		if (key == KeyEvent.VK_LEFT) {
			System.out.println("Gauche relaché");
		}
		if (key == KeyEvent.VK_RIGHT) {
			System.out.println("Droite relaché");
		}
        isPressed = false;
    }

    /* Non utilisé */
	public void keyTyped(KeyEvent e) {
	}
	
}
