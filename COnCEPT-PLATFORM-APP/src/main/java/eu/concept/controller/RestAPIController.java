package eu.concept.controller;

import eu.concept.authentication.COnCEPTRole;
import eu.concept.repository.concept.domain.MindMap;
import eu.concept.repository.concept.service.FileManagementService;
import eu.concept.repository.concept.service.MindMapService;
import eu.concept.repository.concept.service.UserCoService;
import eu.concept.repository.openproject.domain.MemberOp;
import eu.concept.repository.openproject.domain.MemberRoleOp;
import eu.concept.repository.openproject.service.MemberOpService;
import eu.concept.repository.openproject.service.MemberRoleOpService;
import eu.concept.repository.openproject.service.ProjectServiceOp;
import eu.concept.response.ApplicationResponse;
import eu.concept.response.BasicResponseCode;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Restful API for integration with Openproject
 *
 * @author Christos Paraskeva
 */
@RestController
@RequestMapping("/concept/api")
public class RestAPIController {

    @Autowired
    ProjectServiceOp service;

    @Autowired
    MemberOpService members;

    @Autowired
    FileManagementService fmService;

    @Autowired
    MindMapService mindmapService;

    @Autowired
    UserCoService userCoService;

    @Autowired
    MemberRoleOpService roleService;

    @RequestMapping(value = "/memberships/{project_id}", method = RequestMethod.GET)
    public List<MemberOp> fetchProjectByID(@PathVariable int project_id) {
        return members.fetchMemberhipsByProjectId(project_id);
    }

    //MindMap API
    /**
     * Fetch a MindMap from database based on a specific id
     *
     * @param id The MindMap object id
     * @param uid
     * @return ApplicationResponse object
     */
    @RequestMapping(value = "/mindmap/{id}", method = RequestMethod.GET)
    public ApplicationResponse fetchMindMap(@PathVariable("id") int mid, @RequestParam(value = "uid", defaultValue = "0", required = false) int uid) {
        MindMap mindmap = mindmapService.fetchMindMapById(mid);
        //MindMap not found exception
        if (null == mindmap) {
            return new ApplicationResponse(BasicResponseCode.EXCEPTION, "MindMap with id: " + mid + " could not be found in COnCEPT database", null);
        }

        MemberRoleOp memberRole = roleService.findByUserId(uid);

        //Non valid member user role
        if (null == memberRole || memberRole.getRoleId() == COnCEPTRole.NON_MEMBER.getID()) {
            return new ApplicationResponse(BasicResponseCode.INVALIDATE, "User with id: " + mid + " is not member of the COnCEPT platform", null);
        }

        //Inefficient privileges
        if (mindmap.getIsPublic() == 0 && memberRole.getRoleId() == COnCEPTRole.CLIENT.getID()) {
            return new ApplicationResponse(BasicResponseCode.PERMISSION, "Role name: CLIENT is not allowed to perform CRUD actions...", null);
        }

        //Remove user object
        return new ApplicationResponse(BasicResponseCode.SUCCESS, "Successfully retrieved mindmap with id: " + mid, mindmap);
    }


    /**
     * Creates/update a MindMap to COnCEPT db
     *
     * @param mindmap
     * @return
     */
    @RequestMapping(value = "/mindmap", method = RequestMethod.POST, consumes = "application/json")
    public ApplicationResponse createMindMap(@RequestBody MindMap mindmap) {
        if (null == mindmap || null == mindmap.getUserCo()) {
            return new ApplicationResponse(BasicResponseCode.EXCEPTION, "Not a valid MinMap object... ", null);
        }
        
       mindmap = mindmapService.store(mindmap);
        if (null != mindmap && mindmap.getId()>0) {
            System.out.println("Mindmap ID: "+mindmap.getId());
            return new ApplicationResponse(BasicResponseCode.SUCCESS, "Successfully stored mindmap to COnCEPT db...: ", mindmap);
        }

        return new ApplicationResponse(BasicResponseCode.UNKNOWN, "Could not store mindmap to COnCEPT db...: ", mindmap);

    }

//    @RequestMapping(value = "/mindmap", method = RequestMethod.GET)
//    public ResponseEntity<String> deleteMindMap() {
//        System.out.println("IN GET");
//        if (null == mindmap) {
//            return new ApplicationResponse(BasicResponseCode.EXCEPTION, "Not a valid MinMap object... ", null);
//        }
//
//        String URI = "http://localhost:8080/concept/api/mindmap";
//        RestTemplate restTemplate = new RestTemplate();
//        System.out.println("MindMap title: " + mindmapService.fetchMindMapById(1).getTitle());
//        restTemplate.postForObject(URI, mindmapService.fetchMindMapById(1), ApplicationResponse.class);
//        System.out.println("AFTER  POST");
//        System.out.println("Rest: " + restTemplate.toString());
//
//        HttpHeaders httpHeaders = new HttpHeaders();
//        return new ResponseEntity(null, httpHeaders, HttpStatus.CREATED);
//
//    }

}
