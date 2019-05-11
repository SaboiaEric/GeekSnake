/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Palavra;
import mygame.Main;

/**
 *
 * @author Edson
 */
public class Util {
    
    public static Palavra CarregarJSON(String path){
        
        Palavra dicionario = new Palavra();
        
        JsonParser jsonParser = new JsonParser();
        
        try{
            FileReader reader = new FileReader(path+"\\assets\\Dicionario\\palavras.json");
            Object obj = jsonParser.parse(reader);
            JsonArray palavrasList = (JsonArray) obj;
            
            for (JsonElement jsonElement : palavrasList) {
                JsonObject jsonObj = jsonElement.getAsJsonObject();
                dicionario.addPalavra(jsonObj.get("palavra").getAsString());
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return dicionario;
    }
    
    public static void TrocarTextoGUI(Nifty nifty,String elementoID, String stringAlteracao){
        Element elementToFill = nifty.getCurrentScreen().findElementById(elementoID);
        elementToFill.getRenderer(TextRenderer.class).setText( stringAlteracao );
    }
    
}
