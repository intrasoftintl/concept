package eu.concept.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import eu.concept.repository.concept.domain.BriefAnalysis;
import eu.concept.repository.concept.domain.Metadata;
import eu.concept.repository.concept.domain.MindMap;
import eu.concept.repository.concept.domain.Storyboard;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public final class ElasticSearchController {

    private static Optional<ElasticSearchController> instance = Optional.empty();
    private static final Logger logger = Logger.getLogger(ElasticSearchController.class.getName());

    private final String INSERT_URL = "http://192.168.3.5:9999/insert_item";
    private final String DELETE_URL = "http://192.168.3.5:9999/delete_item";
    private final String COnCEPT_BASE_URL = "http://concept.euprojects.net/";
    private final Map<Integer, String> categoriesMap = new HashMap<>();

    private ElasticSearchController() {
        String[] categoriesList = new String[]{"ProductCategory,1", "Kitchenware,11", "Exhibition,12", "Lighting,13", "Furniture,14", "ProductDomain,2", "Medical,21", "Cosumer,22", "Sport,23", "MarketAnalysis,24", "Technology,25", "Usability,26", "ProductLanguage,3", "Style,31", "PeriodStyle,311", "Clasic,3112", "Chic,3113", "Modern,3113", "Artdeco,3115", "PartialStyle,312", "National,3121", "Corporate,3122", "TargetStyle,3123", "Material,32", "Steel,321", "Stone,322", "AssociationsandFeelings,33", "Cold,331", "Warm,332", "Aggressive,333"};
        for (String category : categoriesList) {
            categoriesMap.put(Integer.parseInt(category.split(",")[1]), category.split(",")[0]);
        }
    }

    public static ElasticSearchController getInstance() {
        if (!instance.isPresent()) {
            instance = Optional.of(new ElasticSearchController());
        }
        return instance.get();
    }

    /**
     * Delete a document by id
     *
     * @param id
     * @return
     */
    public boolean deleteById(String id) {
        try {
            HttpResponse<String> response = Unirest.post(DELETE_URL).field("id", id).asString();
            if (200 == response.getStatus()) {
                logger.info("Successfully deleted document with id: " + id);
                return true;
            }
            logger.warning("Could not delete docuemnt with id: " + id + " reason: " + response.getStatusText());
        } catch (UnirestException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Insert a component to Elastic search engine
     *
     * @param document
     * @param metadata
     * @return
     */
    public boolean insert(Optional<Object> document, Optional<Metadata> metadata) {

        //Check if document is not empty
        if (false == document.isPresent()) {
            logger.severe("Could not insert document to Elastic search , reason: document is empty");
            return false;
        }

        int id, pid;
        String title, elastic_id;
        String content = new String();
        String component = getComponentName(document.get().getClass().getSimpleName());
        String keywords = new String();
        String categories = new String();
        String user_id = new String();
        String url = new String();

        //Get all the necessary of the object
        try {

            //Id of the Component
            id = (int) document.get().getClass().getDeclaredMethod("getId", null).invoke(document.get(), null);
            //Project id 
            pid = (int) document.get().getClass().getDeclaredMethod("getPid", null).invoke(document.get(), null);
            //Title
            title = (String) document.get().getClass().getDeclaredMethod("getTitle", null).invoke(document.get(), null);

            //Get content only  object is intance of BriefAnalysis
            if (document.get() instanceof BriefAnalysis) {
                content = (String) document.get().getClass().getDeclaredMethod("getContent", null).invoke(document.get(), null);
            }

            //If document is a MindMap object get the userid of the creator
            if (document.get() instanceof MindMap) {
                user_id = String.valueOf(((MindMap) document.get()).getUserCo().getId());
            }

            //If document is a Storyboard object get the userid of the creator
            if (document.get() instanceof Storyboard) {
                user_id = String.valueOf(((Storyboard) document.get()).getUid().getId());
            }

            //Construct URL where the document is accessible
            url = getURL(component, String.valueOf(id), String.valueOf(pid), user_id);

            //Check if document has metadata to store
            if (metadata.isPresent()) {
                keywords = metadata.get().getKeywords();
                categories = getCategoriesNames(metadata.get().getCategories());
            }

        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(ElasticSearchController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        //Create the unique id for the elastic document
        elastic_id = component + String.valueOf(id);
        //Make the call to insert document to elastic
        try {
            HttpResponse<String> response = Unirest.post(INSERT_URL).field("id", elastic_id).field("project_id", pid).field("title", title).field("content", content).field("url", url).field("component", component).field("keywords", keywords).field("categories", categories).asString();

            if (201 == response.getStatus()) {
                logger.info("Successfully inserted document with id: " + elastic_id);
                return true;
            }
        } catch (UnirestException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public String getCategoriesNames(String categories) {
        int end = categories.length() - 1;
        int start = categories.indexOf("selected_node") + "selected_node".length() + 2;
        String categoriesIds[] = categories.substring(start, end).replace("[", "").replace("]", "").split(",");
        String categoriesNames = new String();

        if (false == (1 == categoriesIds.length && categoriesIds[0].isEmpty())) {
            for (String categoryId : categoriesIds) {
                categoriesNames += categoriesMap.get(Integer.parseInt(categoryId)) + ",";
            }
        }
        return categoriesNames.isEmpty() ? categoriesNames : categoriesNames.substring(0, categoriesNames.length() - 1);
    }

    private String getComponentName(String className) {

        System.out.println("Class name is: " + className);

        switch (className) {
            case "BriefAnalysis":
                return "BA";
            case "FileManagement":
                return "FM";
            //Deprecated
            case "Sketch":
                return "SK";

            case "MindMap":
                return "MM";

            case "Moodboard":
                return "MB";

            case "Storyboard":
                return "SB";
            //Unknown name of component    
            default:
                return "N/A";
        }

    }

    private String getURL(String componentName, String componentId, String projectId, String userId) {
        switch (componentName) {
            case "BA":
                return COnCEPT_BASE_URL.concat("ba_all/").concat(projectId).concat("/").concat(componentId);
            case "FM":
                return COnCEPT_BASE_URL.concat("fm_all/").concat(projectId).concat("/").concat(componentId);
            //Deprecated
            case "SK":
                return COnCEPT_BASE_URL.concat("sk_app/").concat(componentId);
            case "MM":
                return COnCEPT_BASE_URL.concat("mm_all/").concat(projectId).concat("/").concat(componentId);
//                return "http://concept-mm.euprojects.net/wisemapping/c/maps/" + componentId + "/" + userId + "/edit";
            case "MB":
                return COnCEPT_BASE_URL.concat("mb_all/").concat(projectId).concat("/").concat(componentId);
//                return "N/A";
            case "SB":
                return COnCEPT_BASE_URL.concat("sb_all/").concat(projectId).concat("/").concat(componentId);
            //return "http://concept-sb.euprojects.net/storyboard/storyboard/edit?pid= " + projectId + "&uid=" + userId + "4&idStory=" + componentId;
            //Unknown name of component    
            default:
                return "N/A";
        }
    }
}
