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

import Camera.Interface;

import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Box;

import javax.media.j3d.*;
import javax.vecmath.*;

public class ImportWavefront extends Applet {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
public static boolean applet = true ;

public TransformGroup rotationGroup = null;

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
    OrbitBehavior orbit = new OrbitBehavior(canvas3D, OrbitBehavior.REVERSE_ROTATE);
    orbit.setRotXFactor(0);//or any other value
    orbit.setRotYFactor(0);
    orbit.setTransXFactor(0);
    orbit.setTransYFactor(0);
    
    orbit.setSchedulingBounds(new BoundingSphere());
    simpleU.getViewingPlatform().setViewPlatformBehavior(orbit);
    
    
    ViewingPlatform vp = simpleU.getViewingPlatform();
    TransformGroup steerTG = vp.getViewPlatformTransform();
    Transform3D t3d = new Transform3D();
    steerTG.getTransform(t3d);
    // args are: viewer posn, where looking, up direction
    t3d.lookAt(new Point3d(-1.2,1.2,1.2), new Point3d(0,0,0), new Vector3d(0,1,0));
    t3d.invert();
    steerTG.setTransform(t3d);
    
    //simpleU.getViewingPlatform().setNominalViewingTransform();

    // Etape 6 :
    // Creation de la scene 3D qui contient tous les objets 3D que l'on veut visualiser
    BranchGroup scene = createSceneGraph(simpleU);

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
  public BranchGroup createSceneGraph(SimpleUniverse simpleU) {
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
    
    // Construction de l'eau
    
    Box eau1 = new Box(0.5f, 0.001f, 1.5f, null);
    Box eau2 = new Box(0.5f, 0.001f, 1.5f, null);
    Box eau3 = new Box(1.5f, 0.001f, 0.5f, null);
    Box eau4 = new Box(1.5f, 0.001f, 0.5f, null);
    
    Material emissiveColor = new Material();
    emissiveColor.setEmissiveColor(new Color3f(Color.blue));
    Appearance appearance = new Appearance();
    appearance.setMaterial(emissiveColor);
    eau1.setAppearance(appearance);
    eau2.setAppearance(appearance);
    eau3.setAppearance(appearance);
    eau4.setAppearance(appearance);
    
    // Creation de la transformation (translation)
    Transform3D translationEau1 = new Transform3D();
    Transform3D translationEau2 = new Transform3D();
    Transform3D translationEau3 = new Transform3D();
    Transform3D translationEau4 = new Transform3D();
    translationEau1.setTranslation(new Vector3f(1f, 0f, 0f));
    translationEau2.setTranslation(new Vector3f(-1f, 0f, 0f));
    translationEau3.setTranslation(new Vector3f(0f, 0f, 1.1f));
    translationEau4.setTranslation(new Vector3f(0f, 0f, -1.1f));
    TransformGroup eauTransform1 = new TransformGroup(translationEau1);
    TransformGroup eauTransform2 = new TransformGroup(translationEau2);
    TransformGroup eauTransform3 = new TransformGroup(translationEau3);
    TransformGroup eauTransform4 = new TransformGroup(translationEau4);
    
    eauTransform1.addChild(eau1);
    eauTransform2.addChild(eau2);
    eauTransform3.addChild(eau3);
    eauTransform4.addChild(eau4);
    parent.addChild(eauTransform1);
    parent.addChild(eauTransform2);
    parent.addChild(eauTransform3);
    parent.addChild(eauTransform4);

    // Creation du groupe de transformation sur lequel vont s'appliquer les
    // comportements
    TransformGroup mouseTransform = new TransformGroup();

    // Le groupe de transformation sera modifie par le comportement de la
    // souris
    mouseTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    mouseTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

    // Creation comportement rotation a la souris
    //MouseRotate rotate = new MouseRotate(mouseTransform);
    //rotate.setSchedulingBounds(new BoundingSphere());
    //parent.addChild(rotate);

    // Creation comportement deplacement a la souris
    //MouseTranslate translate = new MouseTranslate(mouseTransform);
    //translate.setSchedulingBounds(new BoundingSphere());
    //parent.addChild(translate);

    // Creation comportement zoom a la souris
    //MouseZoom zoom = new MouseZoom(mouseTransform);
    //zoom.setSchedulingBounds(new BoundingSphere());
    //parent.addChild(zoom);

    // Chargement de l'objet Wavefront
    mouseTransform.addChild(loadWavefrontObject());
    
    // Creation de l'homethetie (homothetie)
    Transform3D homothetie = new Transform3D();
    homothetie.setScale(Interface.scale); 
    
    // Creation de la transformation (translation)
    Transform3D translation = new Transform3D();
    translation.setTranslation(new Vector3f(0f, 0f, 0f));
    translation.mul(homothetie);
    
    // Creation de la rotation X(rotation)
    Transform3D rotationX = new Transform3D();
    rotationX.rotX( Interface.rotX * Math.PI/180f);
    rotationX.mul(translation);
    
    // Creation de la rotation Y(rotation)
    Transform3D rotationY = new Transform3D();
    rotationY.rotY( Interface.rotY * Math.PI/180f);
    rotationY.mul(rotationX);
    
    // Creation de la rotation Z(rotation)
    Transform3D rotationZ = new Transform3D();
    rotationZ.rotZ( Interface.rotZ * Math.PI/180f);
    rotationZ.mul(rotationY);
    
    this.rotationGroup = new TransformGroup(rotationZ);
    
    rotationGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    rotationGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    
    // Chargement de l'objet Wavefront et ajout au graphe de la scene
    rotationGroup.addChild(mouseTransform);
    parent.addChild(rotationGroup);

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
  
  public void mouvement(double rotX, double rotY, double rotZ, Vector3d trans, double scale) {
	  Matrix4d m = new Matrix4d();
	  double A = Math.cos(rotX* Math.PI/180f);
	  double B = Math.sin(rotX* Math.PI/180f);
	  double C = Math.cos(rotY* Math.PI/180f);
	  double D = Math.sin(rotY* Math.PI/180f);
	  double E = Math.cos(rotZ* Math.PI/180f);
	  double F = Math.sin(rotZ* Math.PI/180f);
	  
	  double AD = A*D;
	  double BD = B*D;
	  
	  m.m00 = (C*E)*scale;
	  m.m01 = (-C*F)*scale;
	  m.m02 = D*scale;
	  m.m03 = trans.x;
	  
	  m.m10 = (BD*E + A*F)*scale;
	  m.m11 = (-BD*F + A*E)*scale;
	  m.m12 = (-B*C)*scale;
	  m.m13 = trans.y;
	  
	  m.m20 = (-AD*E + B*F)*scale;
	  m.m21 = (AD*F + B*E)*scale;
	  m.m22 = (A*C)*scale;
	  m.m23 = trans.z;
	  
	  m.m30 = 0f;
	  m.m31 = 0f;
	  m.m32 = 0f;
	  m.m33 = 1f;
	  
	  Transform3D rotationZ = new Transform3D();
	  this.rotationGroup.getTransform(rotationZ);
	  rotationZ.set(m);
	  this.rotationGroup.setTransform(rotationZ);
  }

  public void init() {
    if (applet) {
      lanceApplication();
    }
  }
}
