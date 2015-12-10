package eu.concept.util.other;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public final class ElasticSearchHandler {
    
    private static  Optional<ElasticSearchHandler> instance = Optional.empty();
    
    
    private final String INSERT_URL = "http://medialab4.atosresearch.eu:9999/insert_item";
    
    
    public static ElasticSearchHandler getInstance(){
        if (!instance.isPresent()){
            instance= Optional.of(new ElasticSearchHandler());
        }
        return instance.get();
    }
    
    
    
    public static void main(String[] args){

        try {
            HttpResponse<String>  response =  Unirest.post("http://medialab4.atosresearch.eu:9999/insert_item").field("id", "101").field("title", "hola!").asString();
            System.out.println("Response: "+response.getBody());
        } catch (UnirestException ex) {
            Logger.getLogger(ElasticSearchHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
