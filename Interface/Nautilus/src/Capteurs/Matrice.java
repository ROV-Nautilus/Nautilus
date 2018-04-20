package Capteurs;

import javax.vecmath.Vector3d;

import Camera.Interface;
import SSH.Exec;

public class Matrice  implements Runnable{

	public String Yaw = "0";
	public String Pitch = "0";
	public String Roll = "0";
	
	public Matrice() {
		
	}
	
	public void run() {
		while(true) {
			Interface.exMatrice2.Commander("python matrice.py");
			String[] a = Interface.exMatrice2.retour.split("/");
			this.Yaw=a[1];
			this.Pitch=a[0];
			this.Roll=a[2];
			System.out.println("yaw"+a[0]);
			Interface.rotX=Double.parseDouble(Yaw);
			Interface.rotY=Double.parseDouble(Pitch);
			Interface.rotZ=Double.parseDouble(Roll);
			Interface.singe.mouvement(Interface.rotX, Interface.rotY, Interface.rotZ, new Vector3d(Interface.transX, Interface.transY, Interface.transZ), Interface.scale);
		}
	}

}
