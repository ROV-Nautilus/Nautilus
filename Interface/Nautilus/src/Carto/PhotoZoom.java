package Carto;

import java.awt.Container;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class PhotoZoom extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Container win;
	private String nomPhoto;

	/** Lorsque l'on clique sur l'image, cela lance une autre fenetre, c'est cella ci*/
	public PhotoZoom(String titre, int x, int y, boolean b, String nomPhoto){
		super(titre);
		super.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		super.setLocation(x,y);
		this.setNomPhoto(nomPhoto);
		this.setIconImage(new ImageIcon(getClass().getResource("/Nautilus.jpg")).getImage());
		
		/* Construction du Conteneur */
		win = getContentPane();
		
		File fichierPhoto = new File(this.nomPhoto+".jpg");
		Photo photo = new Photo(fichierPhoto,0,0,2);
		win.add(photo);
		super.setSize(640*2, 480*2);
		super.setVisible(b);
	}

	public String getNomPhoto() {
		return nomPhoto;
	}

	public void setNomPhoto(String nomPhoto) {
		this.nomPhoto = nomPhoto;
	}
	
}
