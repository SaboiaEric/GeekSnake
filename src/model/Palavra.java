package model;

import java.util.ArrayList;
import java.util.List;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Edson
 */
public class Palavra {
    private static List<String> palavras;

    public Palavra(){
        palavras = new ArrayList<String> ();
    }
    
    public List<String> getPalavras() {
        return palavras;
    }
    
    public void setPalavras(List<String> palavras) {
        this.palavras = palavras;
        return;
    }
    
    public void addPalavra(String palavra) {
        this.palavras.add(palavra);
        return;
    }
    
    public String To_String(){
        String a="";
        for(String s : this.palavras){
            a+=s+" ";
        }
        return a;
    }
}
