package mygame;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.input.controls.AnalogListener;
import com.jme3.math.FastMath;
import com.jme3.scene.Spatial;
import java.util.List;
import model.Palavra;
import service.Util;

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
    
    private Palavra dicionario;
    
    public static float segundo = 0.0f;
    public static float score = 0.0f;
    public static float speed = 1.0f;
    public static int acaoUsuario = 0;

    @Override
    public void simpleInitApp() {
        
        String path = System.getProperty("user.dir");

        dicionario = Util.CarregarJSON(path);
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
        
        initKeys();
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
        segundo += tpf;
        
        if (segundo>1-speed*0.1){
            score += 1;
            segundo = 0.0f;
            Util.TrocarTextoGUI(nifty, "tempo", String.valueOf(score));
            
            if (acaoUsuario == KeyInput.KEY_UP) {
                System.out.println("UP");
                
            }else if (acaoUsuario == KeyInput.KEY_DOWN) {
                System.out.println("DOWN");
                
            }else if (acaoUsuario == KeyInput.KEY_RIGHT) {
                System.out.println("RIGHT");
                
            }else if (acaoUsuario == KeyInput.KEY_LEFT) {
                System.out.println("LEFT");
                
            }
        }
        
        //troca a palavra
        if (score%10 == 0){
            Util.TrocarTextoGUI(nifty, "text", String.valueOf(dicionario.getPalavras().get((int) (score%dicionario.getPalavras().size()))));
        }
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
    
    private final AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {
                
            if (name.equals("UP")) {
                acaoUsuario = KeyInput.KEY_UP;
                
            } else if(name.equals("DOWN")){
                acaoUsuario = KeyInput.KEY_DOWN;
                
            }else if(name.equals("RIGHT")){
                acaoUsuario = KeyInput.KEY_RIGHT;
                
            } else if(name.equals("LEFT")){
                acaoUsuario = KeyInput.KEY_LEFT;
                
            } else if(name.equals("DOWN")){
                acaoUsuario = KeyInput.KEY_DOWN;
                
            }
             else {
                System.out.println("Press P to unpause.");
            }
        }
    };
    private void initKeys() {
        // You can map one or several inputs to one named action
        inputManager.addMapping("UP",  new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("DOWN",  new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping("RIGHT",  new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("LEFT",  new KeyTrigger(KeyInput.KEY_LEFT));
        
        // Add the names to the action listener.
        inputManager.addListener(analogListener, "UP", "DOWN", "RIGHT","LEFT");
    }
}
