package eu.concept.controller.component;

import eu.concept.repository.concept.domain.Component;
import eu.concept.repository.concept.domain.Metadata;
import eu.concept.repository.concept.service.MetadataService;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @RequestMapping(value = "/metadata/{metadata_id}", method = RequestMethod.GET)
    public String fetchMetadataByID(Model model, @PathVariable long metadata_id) {
        model.addAttribute("metadataContent", metadataService.fetchMetadataById(metadata_id));
        System.out.println("Metadata description: " + metadataService.fetchMetadataById(metadata_id).getDescription());
        return "metadata :: sidebar-metadata";
    }

    @RequestMapping(value = "/metadata", method = RequestMethod.GET)
    public String metadataPage(Model model, @RequestParam(value = "cid", defaultValue = "0", required = false) int cid, @RequestParam(value = "component", defaultValue = "", required = false) String component, @RequestParam(value = "project_id", defaultValue = "0", required = false) int project_id) {

        Metadata metadata = metadataService.fetchMetadataByCidAndComponent(cid, component);
        if (null == metadata) {
            metadata = new Metadata(null, cid, "", "", "", null);
            metadata.setComponent(new Component(component));
            metadata.setCategories("{\"open_nodes\":[1,2,3],\"selected_node\":[]}");
            metadataService.storeMetadata(metadata);
        }
        model.addAttribute("metadata", metadata);
        model.addAttribute("project_id", project_id);
        return "metadata :: sidebar-metadata";
    }

    /*
     *  POST Methods 
     */
    @RequestMapping(value = "/metadata", method = RequestMethod.POST)
    public String addMetadata(Model model, @ModelAttribute Metadata metadata, @RequestParam(value = "project_id", defaultValue = "0", required = false) int project_id, final RedirectAttributes redirectAttributes) {
        if (null == metadata) {
            Logger.getLogger(MetadataController.class.getName()).severe("Metadata object is null... aborting saving metadata object..");
        } else {
            Logger.getLogger(MetadataController.class.getName()).info("MetadataID is: " + metadata.getId() + " CID: " + metadata.getCid() + " Componenet: " + metadata.getComponent().getId() + " Keywprds: " + metadata.getKeywords() + " categories " + metadata.getCategories() + " Project id is: " + project_id);
        }
        //Save Metadata object
        metadataService.storeMetadata(metadata);
        redirectAttributes.addFlashAttribute("projectID", project_id);
        return "redirect:/dashboard";
    }

}
