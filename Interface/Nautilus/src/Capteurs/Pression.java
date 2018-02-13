package Capteurs;

import Camera.Interface;
import SSH.Exec;

public class Pression  implements Runnable{

	public String Pression = "0";
	public String TemperatureC = "0";
	public String TemperatureF = "0";
	
	public Pression() {
		
	}
	
	public void run() {
		while(true) {
			Interface.exPression.Commander("python MS5803_14BA.py");
			String[] a = Interface.exPression.retour.split("/");
			this.Pression=a[0];
			this.TemperatureC=a[1];
			this.TemperatureF=a[2];
			Interface.pre=Pression;
			Interface.tempC=TemperatureC;
			Interface.tempF=TemperatureF;
			Interface.pression.setText(" Pression = "+Interface.pre);
			Interface.pression.repaint();
			Interface.temperatureC.setText(" TemperatureC = "+Interface.tempC);
			Interface.temperatureC.repaint();
			Interface.temperatureF.setText(" TemperatureF = "+Interface.tempF);
			Interface.temperatureF.repaint();
		}
	}

}
