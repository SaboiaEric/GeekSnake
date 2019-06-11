package mygame;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
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
import java.util.Random;
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
    
    public static boolean running = true;
    public static boolean primeiroFrame = true;
    public static float segundo = 0.0f;
    public static float tempo = 0.0f;
    public static float speed = 2.0f;
    public static int acaoUsuario = 0;
    public static int maxX=9;
    public static int maxY=9;
    public static int playerX=4;
    public static int playerY=4;
    public static Geometry[][] matrix;
    public Random gerador;
    
    private int fase=0;
    private boolean terminou=false;
    private boolean pontuou=false;
    public static float score = 0.0f;
    

    @Override
    public void simpleInitApp() {
        
        matrix = new Geometry[maxX][maxY];
        
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
        flyCam.setEnabled(false);
        //flyCam.setDragToRotate(true);
        inputManager.setCursorVisible(true);
        
        initKeys();
        
        int k = 0;
        
        for (;k<maxX*maxY;k++){
            Geometry  g= Fase();
            g.setLocalTranslation(k/maxX*1.25f-8f, k%maxX*1.25f-5f, layerFundo+1);
            g.setName("x"+(k/maxX) +"y"+(k%maxX));
            matrix[k/maxX][k%maxX] = g;
                
            rootNode.attachChild(g);
        }
        
        gerador = new Random();
        
        Material matCentro = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        matCentro.setColor("Color", ColorRGBA.Green);
        
        Spatial centro = rootNode.getChild("x"+playerX+"y"+playerY);
        
        centro.setMaterial(matCentro);
        acaoUsuario = KeyInput.KEY_UP;
        
        
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        
        
        Util.TrocarTextoGUI(nifty, "tempo", "Tempo: 0[s]");
        Util.TrocarTextoGUI(nifty, "score", "Score: 0[letras]");

    }
    public Geometry Fase(){
        Box b = new Box(0.5f, .5f, 0.1f);
        Geometry geom = new Geometry("Box", b);
        
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Red);
        geom.setMaterial(mat);
        
        return geom;
    }

    @Override
    public void simpleUpdate(float tpf) {
        
        if (primeiroFrame){
            
            Util.TrocarTextoGUI(nifty, "text", String.valueOf(dicionario.getPalavras().get(0)));
            String[] palavra = dicionario.getPalavras().get(0).split("(?!^)");
            
            for(String p : palavra){
                int x = gerador.nextInt(maxX);
                int y = gerador.nextInt(maxY);
                
                Material mat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
                mat.setTexture("ColorMap",assetManager.loadTexture("Textures/"+p.toUpperCase()+".png"));

                matrix[x][y].setMaterial(mat);
                
                terminou = false;
                
            }
            
            primeiroFrame=false;
        }else{
            if(running){
                segundo += tpf;

                if (segundo>1-speed*0.1){
                    tempo += 1;
                    segundo = 0.0f;
                    Util.TrocarTextoGUI(nifty, "tempo", "Tempo: "+String.valueOf(tempo)+"[s]");
                    Util.TrocarTextoGUI(nifty, "score", "Score: "+String.valueOf(score)+"[letras]");

                    if (acaoUsuario == KeyInput.KEY_UP) {
                        saiuTale();
                        playerY += 1;
                        entrouTale();

                    }else if (acaoUsuario == KeyInput.KEY_DOWN) {
                        saiuTale();
                        playerY -= 1;
                        entrouTale();

                    }else if (acaoUsuario == KeyInput.KEY_RIGHT) {
                        saiuTale();
                        playerX += 1;
                        entrouTale();

                    }else if (acaoUsuario == KeyInput.KEY_LEFT) {
                        saiuTale();
                        playerX -= 1;
                        entrouTale();                
                    }
                }
            }
        }
        
        if (pontuou){
            score+=1;
            Util.TrocarTextoGUI(nifty, "score", String.valueOf(tempo));
        }
        
        if (terminou){
            fase += 1;
            Util.TrocarTextoGUI(nifty, "text", String.valueOf(dicionario.getPalavras().get(fase)));
        }
    }

    public void saiuTale(){
        Spatial centro = rootNode.getChild("x"+playerX+"y"+playerY);
        Material saiu = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        saiu.setColor("Color", ColorRGBA.Red);
        centro.setMaterial(saiu);
    }
    public void entrouTale(){
        if(
            playerX<0 || playerX>maxX-1 ||
            playerY<0 || playerY>maxY-1){
            running = false;
            Util.TrocarTextoGUI(nifty, "text", String.valueOf("!!!!FIM DO JOGO!!!!"));

        }
        if (running){
            Spatial centro = rootNode.getChild("x"+playerX+"y"+playerY);
            Material entrou = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            entrou.setColor("Color", ColorRGBA.Green);
            centro.setMaterial(entrou);            
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
