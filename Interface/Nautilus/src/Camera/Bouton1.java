package Camera;

import java.awt.Color;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;


	public class Bouton1 extends JButton {
	
		private static final long serialVersionUID = 1L;

		/** Bouton personnalisé*/
	public Bouton1(String txt, URL icon, URL iconHover, URL iconPressed) {
		super(txt);
		setForeground(Color.WHITE);
		setOpaque(false);
		setContentAreaFilled(false); // On met à false pour empêcher le composant de peindre l'intérieur du JButton.
		setBorderPainted(false); // De même, on ne veut pas afficher les bordures.
		setFocusPainted(false); // On n'affiche pas l'effet de focus.
		setHorizontalAlignment(SwingConstants.CENTER);
		setHorizontalTextPosition(SwingConstants.CENTER);
		setIcon(new ImageIcon(icon));
		setPressedIcon(new ImageIcon(iconPressed));
		setRolloverIcon(new ImageIcon(iconHover));
		}
	}