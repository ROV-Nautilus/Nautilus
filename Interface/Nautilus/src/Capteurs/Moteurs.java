package Capteurs;

import Camera.Interface;
import SSH.Exec;

public class Moteurs implements Runnable{

	public int m1 = 120;
	public int m2 = 120;
	public int m3 = 120;
	
	public Moteurs(int m1, int m2, int m3) {
		this.m1=m1;
		this.m2=m2;
		this.m3=m3;
	}
	 
	/** Envoie la commandes pour le 3 moteurs à la Raspberry */
	public void run() {
		Interface.exMoteur.setCommande("echo 0="+m1+" > /dev/servoblaster & echo 1="+m2+" > /dev/servoblaster & echo 2="+m3+" > /dev/servoblaster");
		Thread a1 = new Thread(Interface.exMoteur);
		a1.start();
		while( a1.isAlive()) {
		}
		Interface.m1=this.m1;
		Interface.m2=this.m2;
		Interface.m3=this.m3;
		Interface.moteur1.setText(""+Interface.m1+"    ");
		Interface.moteur1.repaint();
		Interface.moteur2.setText(""+Interface.m2+"    ");
		Interface.moteur2.repaint();
		Interface.moteur3.setText(""+Interface.m3+"    ");
		Interface.moteur3.repaint();
	}

}
