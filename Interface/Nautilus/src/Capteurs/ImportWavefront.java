package Capteurs;

// Etape 1 :
// Importation des packages Java 2
import java.applet.Applet;
import java.awt.*;
import java.io.*;
import java.net.*;

// Etape 2 :
// Importation des packages Java 3D
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.loaders.objectfile.*;
import com.sun.j3d.loaders.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import javax.media.j3d.*;
import javax.vecmath.*;

public class ImportWavefront extends Applet {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
public static boolean applet = true ;

  public ImportWavefront() {
    if (!applet) {
      lanceApplication();
    }
  }

  public void lanceApplication() {
    this.setLayout(new BorderLayout());

    // Etape 3 :
    // Creation du Canvas 3D
    Canvas3D canvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());

    this.add(canvas3D, BorderLayout.CENTER);

    // Etape 4 :
    // Creation d'un objet SimpleUniverse
    SimpleUniverse simpleU = new SimpleUniverse(canvas3D);

    // Etape 5 :
    // Positionnement du point d'observation pour avoir une vue correcte de la
    // scene 3D
    simpleU.getViewingPlatform().setNominalViewingTransform();

    // Etape 6 :
    // Creation de la scene 3D qui contient tous les objets 3D que l'on veut visualiser
    BranchGroup scene = createSceneGraph();

    // Etape 7 :
    // Compilation de la scene 3D
    scene.compile();

    // Etape 8:
    // Attachement de la scene 3D a l'objet SimpleUniverse
    simpleU.addBranchGraph(scene);
  }

  /**
   * Creation de la scene 3D qui contient tous les objets 3D
   * @return scene 3D
   */
  public BranchGroup createSceneGraph() {
    // Creation de l'objet parent qui contiendra tous les autres objets 3D
    BranchGroup parent = new BranchGroup();

    // Creation d'une lumiere ambiante de couleur blanche
    BoundingSphere bounds = new BoundingSphere(new Point3d(), 100);
    Light ambientLight = new AmbientLight(new Color3f(Color.white));
    ambientLight.setInfluencingBounds(bounds);
    parent.addChild(ambientLight);

    // Creation d'une lumiere directionnelle de couleur blanche
    Light directionalLight = new DirectionalLight(
      new Color3f(Color.white),
      new Vector3f(1, -1, -1));
    directionalLight.setInfluencingBounds(bounds);
    parent.addChild(directionalLight);

    // Creation du groupe de transformation sur lequel vont s'appliquer les
    // comportements
    TransformGroup mouseTransform = new TransformGroup();

    // Le groupe de transformation sera modifie par le comportement de la
    // souris
    mouseTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    mouseTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

    // Creation comportement rotation a la souris
    MouseRotate rotate = new MouseRotate(mouseTransform);
    rotate.setSchedulingBounds(new BoundingSphere());
    parent.addChild(rotate);

    // Creation comportement deplacement a la souris
    MouseTranslate translate = new MouseTranslate(mouseTransform);
    translate.setSchedulingBounds(new BoundingSphere());
    parent.addChild(translate);

    // Creation comportement zoom a la souris
    MouseZoom zoom = new MouseZoom(mouseTransform);
    zoom.setSchedulingBounds(new BoundingSphere());
    parent.addChild(zoom);

    // Chargement de l'objet Wavefront et ajout au graphe de la scene
    mouseTransform.addChild(loadWavefrontObject());
    parent.addChild(mouseTransform);

    return parent;
  }

  /**
   * Chargement de l'objet Wavefront (singe.obj) ainsi que les materiaux qui
   * lui sont associes
   * @return BranchGroup branch group contenant l'objet Wavefront
   */
  private BranchGroup loadWavefrontObject() {
    String filename = "singe.obj";
    ObjectFile waveFrontObject = new ObjectFile(ObjectFile.RESIZE);
    Scene scene = null;
    int error = 0;

    try {
      if (!applet) {
        scene = waveFrontObject.load(filename);
      }
      else {
        URL url = this.getCodeBase();
        String path = url.toString() + filename;
        System.out.println(path);
        try {
          URL fileUrl = new URL(path);
          scene = waveFrontObject.load(fileUrl);
        }
        catch (MalformedURLException ex2) {
          System.out.println("Impossible de charger le fichier : " +
                             filename);
          error = -1;
        }
      }  // fin else
    }  // fin try
    catch (ParsingErrorException ex) {
      System.out.println("Fichier defectueux");
      error = -1;
    }
    catch (IncorrectFormatException ex) {
      System.out.println("Mauvais format de fichier");
      error = -1;
    }
    catch (FileNotFoundException ex) {
      System.out.println("Fichier " + filename + " non trouve");
      error = -1;
    }

    if (error != 0)
      System.exit(error);

    return scene.getSceneGroup();
  }

  public void init() {
    if (applet) {
      lanceApplication();
    }
  }
}