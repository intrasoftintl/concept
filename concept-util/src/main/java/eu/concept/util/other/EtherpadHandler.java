package eu.concept.util.other;

import java.util.logging.Logger;
import net.gjerull.etherpad.client.EPLiteClient;
import net.gjerull.etherpad.client.EPLiteException;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public enum EtherpadHandler {

    INSTANCE;

    private final String ETHERPAD_API_KEY = "85905bf430573c66478afd532826a912411c8b2e4569434a7b4c17f5bc40fb67";
    private final String ETHERPAD_ENDPOINT = "http://concept-ba.euprojects.net/";

    private final EPLiteClient client;

    EtherpadHandler() {
        //Initialize Etherpad-lite handler
        client = new EPLiteClient(ETHERPAD_ENDPOINT, ETHERPAD_API_KEY);
        Logger.getLogger(EtherpadHandler.class.getName()).info("Initialize EPLiteClient ...");
    }

    public EPLiteClient getClient(){
        return client;
    }
    
    
    public static void main(String[] args){
        
        String[] padIds = "16,17,18,19".split(",");
        
        for (String padId : padIds){
            
            try{
            
            EtherpadHandler.INSTANCE.getClient().deletePad(padId);
            }catch (EPLiteException ex){
                System.out.println(ex.getLocalizedMessage());
            }
        }
        
        EtherpadHandler.INSTANCE.getClient().listAllPads().values().forEach(System.out :: println);
        
         System.out.println( EtherpadHandler.INSTANCE.getClient().getText("21").get("text"));
         
    }
    
}
