package Camera;

import java.net.*;
import com.sun.image.codec.jpeg.*;

import java.io.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.*;
import javax.swing.*;

public class Camera  extends Panel implements Runnable{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
    /** Attribut */
	public boolean useMJPGStream = true; //On utilise une video streaming en MJPG
	
	public String jpgURL=null;
	public String mjpgURL=null;
	public String nomCamera=null;

    DataInputStream dis;
    private BufferedImage image=null;
    int counter=0;
    public Dimension imageSize = null;
    public boolean connected = false;
    private boolean initCompleted = false;
    HttpURLConnection huc=null;
    Component parent;
 
    /** Constructeur qui crée notre Camera */
    public Camera(Component parent_,String jpgURL_,String mjpgURL_,String nomCamera_) {
    	parent = parent_;
    	jpgURL = jpgURL_;
    	mjpgURL = mjpgURL_;
    	nomCamera = nomCamera_;
    }
 
    /** Méthodes */
    
    /** Redimensionne notre image à afficher */
    public static Image getScaledInstanceAWT(BufferedImage source, double factor) {
    	int w = (int) (source.getWidth() * factor);
    	int h = (int) (source.getHeight() * factor);
    	return source.getScaledInstance(w, h, Image.SCALE_SMOOTH);
    }
    /** Redimensionne notre image à afficher 2*/
    public BufferedImage scale(BufferedImage bImage, double factor) {
        int destWidth=(int) (bImage.getWidth() * factor);
        int destHeight=(int) (bImage.getHeight() * factor);
        
        /* créer l'image de destination*/
        GraphicsConfiguration configuration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        BufferedImage bImageNew = configuration.createCompatibleImage(destWidth, destHeight);
        Graphics2D graphics = bImageNew.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        /* dessiner l'image de destination*/
        graphics.drawImage(bImage, 0, 0, destWidth, destHeight, 0, 0, bImage.getWidth(), bImage.getHeight(), null);
        graphics.dispose();
 
        return bImageNew;
    }
 
    /** */
    public static BufferedImage toBufferedImage(Image image) {
    	new ImageIcon(image); //Charge l'image
    	int w = image.getWidth(null);
    	int h = image.getHeight(null);
    	BufferedImage bimage = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_INDEXED);
    	Graphics2D g = bimage.createGraphics();
    	g.drawImage(image, 0, 0, null);
    	g.dispose();
    	return bimage;
    }
 
    /** Requete http pour se connecter */
    public void connect()
    {
        try
        {
            URL u = new URL(useMJPGStream?mjpgURL:jpgURL);
            huc = (HttpURLConnection) u.openConnection();
            InputStream is = huc.getInputStream();
            connected = true;
            BufferedInputStream bis = new BufferedInputStream(is);
            dis= new DataInputStream(bis);
            if (!initCompleted) initDisplay();
        }
        catch(IOException e)
        { //Relance la connection si pas de connection en attendant un petit temps
            try
            {
                huc.disconnect();
                Thread.sleep(60);
            }
            catch(InterruptedException ie)
            {
                //huc.disconnect();connect();
            }
            //connect();
        }
        catch(Exception e){;}
    }
    
    /** Requete http pour se deconnecter */
    public void disconnect(){
    	try{
    		if(connected){
    			dis.close();
    			connected = false;
    		}
    	}catch(Exception e){;}
    }
 
    /** Mise en page de l'emplacement de la video */
    public void initDisplay()
    { //setup the display
        if (useMJPGStream)readMJPGStream();
        else
        {
            readJPG();
            disconnect();
        }
        imageSize = new Dimension((image.getWidth(this)*2), image.getHeight(this)*2);
        setPreferredSize(imageSize);
        parent.validate();
        initCompleted = true;
    }
    
    /** Méthode de tracer */
    public void paint(Graphics g) {
    	if (image != null)
    		//image = toBufferedImage(getScaledInstanceAWT(image, 2));
    		image=scale(image, 2);
    		g.drawImage(image, 0, 0, this);
    		
    		Graphics2D g2 = (Graphics2D) g;
    		
    		double alpha = Interface.rotZ* Math.PI/180f;
    		int a = image.getHeight();
    		int b = image.getWidth();
    		
    		double S =(b/2)*(Math.sin(alpha))*(Math.cos((Math.PI/2)-alpha));
    		double T =(b/2)*(Math.sin(alpha))*(Math.sin((Math.PI/2)-alpha));
    		
    		g2.draw(new Line2D.Double(S,(a/2)-T,b-S,(a/2)+T));
    }
 
    /** Lecture du flux streamer pour reaffichage*/
    public void readStream(){
    	try{
    		if (useMJPGStream){
    			while(true){
    				readMJPGStream();
    				parent.repaint();
    			}
    		}
    		else{
    			while(true){
    				connect();
    				readJPG();
    				parent.repaint();
    				disconnect();
    			}
    		}
    	}catch(Exception e){;}
    }
 
 
    /** Lecture du flux MJPG pour recuperer l'image en JPG
    *	Il enleve l'encapsulation MJPG
    */
    public void readMJPGStream(){
    	readLine(4,dis); //enleve les 3 premieres lignes
    	readJPG();
    	readLine(1,dis); //enleve les 2 dernieres lignes
    }
 
    /** Decode l'image JPG */
    public void readJPG(){
    	try{
    		JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(dis);
    		image = decoder.decodeAsBufferedImage();
    	}catch(Exception e){
    		//System.out.println("Probleme 2");
    		disconnect();
    	}
    }
    /** Methode pour supprimer des lignes dans le flux dis */
    public void readLine(int n, DataInputStream dis){
    	for (int i=0; i<n;i++){
    		readLine(dis);
    	}
    }
 
    /** decode une ligne */
    public void readLine(DataInputStream dis){
    	try{
    		boolean end = false;
    		String lineEnd = "\n"; //designe la fin de la ligne
    		byte[] lineEndBytes = lineEnd.getBytes();
    		byte[] byteBuf = new byte[lineEndBytes.length];
    		
    		while(!end){
    			dis.read(byteBuf,0,lineEndBytes.length);
    			String t = new String(byteBuf);
    			//System.out.print(t); //Pour regarder la ligne en directe
    			if(t.equals(lineEnd)) end=true;
    		}
    	}catch(Exception e){
    		//System.out.println("Probleme 1");
    	}
    }
    
    /** Methode Start() en java */
    public void run() {
        System.out.println("Lecture de la Camera : "+nomCamera);
        connect();
        readStream();
    }
}
