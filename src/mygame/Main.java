package mygame;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.List;
import model.Palavra;
/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication implements ScreenController {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    
    final float layerFundo = -10f;
    private Nifty nifty;

    @Override
    public void simpleInitApp() {
        
        Palavra dicionario = new Palavra();
        JsonParser jsonParser = new JsonParser();
        try{
            FileReader reader = new FileReader("C:\\Users\\Edson\\Documents\\GIT\\ProjetoComputacaoGraficaIISnake\\assets\\Dicionario\\palavras.json");
            
            Object obj = jsonParser.parse(reader);
            
            JsonArray palavrasList = (JsonArray) obj;
            
            for (JsonElement jsonElement : palavrasList) {
                JsonObject jsonObj = jsonElement.getAsJsonObject();
                dicionario.addPalavra(jsonObj.get("palavra").getAsString());
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(dicionario.To_String());

        Box b = new Box(10f, 10f, 0.1f);
        Geometry geom = new Geometry("Box", b);
        geom.setLocalTranslation(0f, 0f, layerFundo);
        

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue);
        geom.setMaterial(mat);

        rootNode.attachChild(geom);
        
        NiftyJmeDisplay niftyDisplay = NiftyJmeDisplay.newNiftyJmeDisplay(
                assetManager,
                inputManager,
                audioRenderer,
                guiViewPort);
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/Teste.xml", "start", this);

        // attach the nifty display to the gui view port as a processor
        guiViewPort.addProcessor(niftyDisplay);

        // disable the fly cam
        //flyCam.setEnabled(false);
        //flyCam.setDragToRotate(true);
        inputManager.setCursorVisible(true);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    @Override
    public void bind(Nifty nifty, Screen screen) {
        System.out.println("bind( " + screen.getScreenId() + ")");
    }

    @Override
    public void onStartScreen() {
        System.out.println("onStartScreen");
    }

    @Override
    public void onEndScreen() {
        System.out.println("onEndScreen");
    }

    public void quit(){
        nifty.gotoScreen("end");
    }
    
}
