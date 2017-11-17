package Video;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import com.jcraft.jsch.JSchException;

public class Interface extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** Camera carto */
    public String jpgURL1="http://169.254.14.03:8081/";
    public String mjpgURL1="http://169.254.14.03:8081/";
    
    /** Camera FPV */
    public String jpgURL="http://169.254.14.03:8000/";
    public String mjpgURL="http://169.254.14.03:8000/stream.mjpg";
    
    public Container win;


		//Constructeur
		public Interface(String titre, int x, int y, int longueur, int largeur, boolean b){
			super(titre);
			super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			super.setLocation(x,y);
			super.setSize(longueur,largeur);
			
			//JFrame jframe = new JFrame();
			
			/* Construction du Conteneur */
			win = getContentPane();
			
	    	/** Barre de menu */
	    	JMenuBar menu= new JMenuBar();
			JMenu fichier=new JMenu("Fichier");
			JMenuItem quitter= new JMenuItem("Quitter");
			quitter.addActionListener(this);
			fichier.add(quitter);
			menu.add(fichier);
			setJMenuBar(menu);
			
			/** Bouton */
			JPanel pan1=new JPanel();
			pan1.setLayout(new GridLayout(3,2));
			
			JButton demarrer = new JButton("Demarrer Camera 1");
			demarrer.addActionListener(this);
			demarrer.setBackground(Color.white);
			pan1.add(demarrer);
			
			JButton demarrer2 = new JButton("Demarrer Camera 2");
			demarrer2.addActionListener(this);
			demarrer2.setBackground(Color.white);
			pan1.add(demarrer2);
			
			JButton demarrer1 = new JButton("Afficher Camera 1");
			demarrer1.addActionListener(this);
			demarrer1.setBackground(Color.white);
			pan1.add(demarrer1);
			
			JButton demarrer3 = new JButton("Afficher Camera 2");
			demarrer3.addActionListener(this);
			demarrer3.setBackground(Color.white);
			pan1.add(demarrer3);
			
			JButton arret = new JButton("Arreter Camera 1");
			arret.addActionListener(this);
			arret.setBackground(Color.white);
			pan1.add(arret);
			
			JButton arret1 = new JButton("Arreter Camera 2");
			arret1.addActionListener(this);
			arret1.setBackground(Color.white);
			pan1.add(arret1);
			
			win.add(pan1,"South");
			
	    	super.setVisible(b);
		}
	
		
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("Quitter")){
			System.exit(0);
		}
		if(cmd.equals("Demarrer Camera 1")){
			ssh cam1 = new ssh("python3 rpi_camera_surveillance_system.py");
		}
		if(cmd.equals("Demarrer Camera 2")){
			ssh cam1 = new ssh("sudo motion");
			ssh cam2 = new ssh("sudo systemctl start motion.service");
		}
		if(cmd.equals("Afficher Camera 1")){
			Camera axPanel = new Camera(win,jpgURL,mjpgURL,"FPV");
	    	new Thread(axPanel).start();
	    	win.add(axPanel,"West");
		}
		if(cmd.equals("Afficher Camera 2")){
			Camera axPanel1 = new Camera(win,jpgURL1,mjpgURL1,"Cartographie");
	    	new Thread(axPanel1).start();
	    	win.add(axPanel1,"East");
		}
		if(cmd.equals("Arreter Camera 1")){
			ssh cam1 = new ssh("");
		}
		if(cmd.equals("Arreter Camera 2")){
			ssh cam1 = new ssh("sudo /etc/init.d/motion stop");
		}
	}
	
}