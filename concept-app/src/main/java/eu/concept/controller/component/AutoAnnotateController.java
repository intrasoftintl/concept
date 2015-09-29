package eu.concept.controller.component;

import com.google.common.base.Joiner;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sun.jersey.core.util.Base64;
import eu.concept.repository.concept.domain.BriefAnalysis;
import eu.concept.repository.concept.domain.FileManagement;
import eu.concept.repository.concept.domain.Metadata;
import eu.concept.repository.concept.service.BriefAnalysisService;
import eu.concept.repository.concept.service.FileManagementService;
import eu.concept.repository.concept.service.MetadataService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by nikos on 29/9/2015.
 */
@Controller
public class AutoAnnotateController {


    @Autowired
    MetadataService metadataService;

    @Autowired
    BriefAnalysisService briefAnalysisService;

    @Autowired
    FileManagementService fileManagementService;


    public String getTagsForText(String content) {
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.post("http://context.erve.vtt.fi/semantic-enhancer/tags/text").body(content).asJson();
            List<String> tags = new ArrayList<>();
            for (int i = 0; i < jsonResponse.getBody().getArray().length(); i++) {
                JSONObject obj = jsonResponse.getBody().getArray().getJSONObject(i);
                double relevancy = (double)obj.get("relevancy");
                Logger.getLogger(AutoAnnotateController.class.getName()).info("obj "+obj.toString());
                if(relevancy > 0.5){
                    tags.add((String) obj.get("name"));
                }
            }
            return Joiner.on(",").join(tags);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public String getTagsForImage(String uri) {
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.get("http://context.erve.vtt.fi/semantic-enhancer/tags?url=http://concept.euprojects.net/"+uri).asJson();
            List<String> tags = new ArrayList<>();
            for (int i = 0; i < jsonResponse.getBody().getArray().length(); i++) {
                JSONObject obj = jsonResponse.getBody().getArray().getJSONObject(i);
                double relevancy = (double)obj.get("relevancy");
                Logger.getLogger(AutoAnnotateController.class.getName()).info("obj "+obj.toString());
                if(relevancy > 0.1){
                    tags.add((String) obj.get("name"));
                }
            }
            return Joiner.on(",").join(tags);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /*
     *  POST Methods
     */
    @RequestMapping(value = "/autoannotate", method = RequestMethod.POST)
    public String autoAnnotate(Model model, @ModelAttribute Metadata metadata, @RequestParam(value = "project_id", defaultValue = "0", required = false) int project_id, final RedirectAttributes redirectAttributes) {

        int cid = metadata.getCid();
        Logger.getLogger(AutoAnnotateController.class.getName()).info("AUTO +++ MetadataID is: " + metadata.getId() + " CID: " + metadata.getCid() + " Componenet: " + metadata.getComponent().getId() + " Keywprds: " + metadata.getKeywords() + " categories " + metadata.getCategories() + " Project id is: " + project_id);

        String keywords = "";
        //Brief analysis
        if(metadata.getComponent().getId().equals("BA")) {
            BriefAnalysis briefAnalysis = briefAnalysisService.fetchBriefAnalysisById(cid);
            String content = briefAnalysis.getContent();
            keywords = getTagsForText(content);
        }else if(metadata.getComponent().getId().equals("FM")) {
           keywords = getTagsForImage("file/"+cid);
        }else if(metadata.getComponent().getId().equals("SK")) {
            keywords = getTagsForImage("skimage/"+cid);
        }

        metadata.setKeywords(keywords);
        metadataService.storeMetadata(metadata);
        redirectAttributes.addFlashAttribute("projectID", project_id);
        return "redirect:/dashboard";
    }
}
