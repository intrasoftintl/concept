package eu.concept.controller.component;

import eu.concept.repository.concept.service.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Controller
public class MetadataController {
    
    @Autowired
    MetadataService metadataService;

    /*
     *  GET Methods 
     */
    @RequestMapping(value = "/metadata/{project_id}", method = RequestMethod.GET)
    public String fetchBAByProjectID(Model model, @PathVariable long metadata_id) {
        model.addAttribute("metadataContent", metadataService.fetchMetadataById(metadata_id));
        System.out.println("Metadata description: " +metadataService.fetchMetadataById(metadata_id).getDescription());
        return "sidebar :: sidebar-metadata";
    }
    
//    @RequestMapping(value = "/ba_app", method = RequestMethod.GET)
//    public String ba_app(Model model) {
//        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
//        model.addAttribute("projects", projects);
//        model.addAttribute("currentUser", getCurrentUser());
//        if (!model.containsAttribute("briefanalysis")) {
//            BriefAnalysis ba = new BriefAnalysis();
//            
//            model.addAttribute("briefanalysis", new BriefAnalysis());
//        } else {
//            
//        }
//        return "ba_app";
//    }
//    
//    @RequestMapping(value = "/sidebar", method = RequestMethod.GET)
//    public String baPage(Model model) {
//        return "sidebar";
//    }

    /*
     *  POST Methods 
     */
//    @RequestMapping(value = "/ba_app", method = RequestMethod.POST)
//    public String createBriefAnalysis(@RequestParam(value = "projectID", defaultValue = "0", required = false) int projectID, Model model) {
//        model.addAttribute("projectID", projectID);
//        return ba_app(model);
//    }
    
}
