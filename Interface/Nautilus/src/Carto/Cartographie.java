package Carto;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class Cartographie extends JFrame implements ActionListener, MouseMotionListener, MouseListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Attributs */
	public Container win;
	public String nomPhoto = "carto";
	public int nombrePhoto = 0;
	GridBagConstraints c = new GridBagConstraints();
	Photo[] carto = null;
	private int xSouris;
	private int ySouris;
	
	public Cartographie(String titre, int x, int y, boolean b){
		super(titre);
		super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		super.setLocation(x,y);
		this.setIconImage(new ImageIcon(getClass().getResource("/Nautilus.jpg")).getImage());
		
		/* Construction du Conteneur */
		win = getContentPane();
		
		
		JPanel panel = new JPanel();
		panel.setLayout (new GridBagLayout());
		JScrollPane scrollPane = new JScrollPane(panel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setPreferredSize(new Dimension(1366,800));
		
		/** Recuperation de chaque photos*/
		File fichierPhoto = new File(nomPhoto+".jpg");
		while(fichierPhoto.exists()) {
			nomPhoto=nomPhoto+"1";
			fichierPhoto = new File(nomPhoto+".jpg");
			nombrePhoto++;
		}
		carto = new Photo[nombrePhoto];
		nomPhoto="carto";
		int h = 0;
		/** afficage de chaque photo*/
		for(int k = 0 ; k < nombrePhoto ; k++) {
			fichierPhoto = new File(nomPhoto+".jpg");
			carto[k] = new Photo(fichierPhoto,0,0,0.5);
			carto[k].setName(nomPhoto);
			nomPhoto=nomPhoto+"1";
			
			carto[k].addMouseListener(this);
			carto[k].addMouseMotionListener(this);
			
			
			carto[k].setMinimumSize(new Dimension(320,240));
			carto[k].setMaximumSize(new Dimension(320,240));
			carto[k].setPreferredSize(new Dimension(320,240));
			c.fill = GridBagConstraints.HORIZONTAL;
			c.anchor = GridBagConstraints.FIRST_LINE_START;
			c.gridx = k%4;//Colonne
			c.gridy = h;       //Ligne
			c.weightx = 0.0;
			c.weighty = 0.0;
			c.gridwidth = 1;   //Prend 1 Colonne
			c.gridheight = 1;   //Prend 1 ligne
			panel.add(carto[k],c);
			
			if((k+1)%4==0){
				h++;
			}
		}
		
		
		win.add(scrollPane, "West");
		
		super.setSize((640/2)*4, (480/2)*2);
		
		/** Barre de menu */
    	JMenuBar menu= new JMenuBar();
		JMenu fichier=new JMenu("Fichier");
		JMenuItem quitter= new JMenuItem("Quitter");
		quitter.addActionListener(this);
		fichier.add(quitter);
		menu.add(fichier);
		
		setJMenuBar(menu);
		
		
		super.setVisible(b);
		//this.setResizable(false);
	}
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("Quitter")){
			System.exit(0);
		}
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		String nomPhoto = arg0.getComponent().getName();
		PhotoZoom zoom = new PhotoZoom("Cartographie",0,0,true,nomPhoto);
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
