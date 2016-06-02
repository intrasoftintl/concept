/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.concept.controller;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public class TestCompletableFuture {
    
    public static void amain(String... args) {
        
        
        long startTime =  System.currentTimeMillis();
        
        List<String> heroes = Arrays.asList(new String[]{"superman", "batman", "wolvering", "spiderman", "songoku"});
        
        List<CompletableFuture<String>> stringsList = heroes.stream().map(heroe -> CompletableFuture.supplyAsync(() -> new HeroeReputation().getScore(heroe))).collect(Collectors.toList());
        
        stringsList.stream().map(CompletableFuture::join).map(heroeRep -> "Total reputation is: " + heroeRep).forEach(System.out::println);
        
        
        System.out.println("\n\nTotal time: "+   (System.currentTimeMillis() - startTime)/1000+" sec");
    }
}

class HeroeReputation {
    
    public String getScore(String heroe) {
        
        System.out.println("I am waiting heroe: " + heroe + " for " + heroe.length() + " seconds!");
        try {
            Thread.sleep(heroe.length() * 1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(HeroeReputation.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Heroe: " + heroe + " just finished!");
        
        return String.valueOf(heroe.length() * 1000);
    }
    
}
