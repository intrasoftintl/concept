package eu.concept.controller;

import eu.concept.authentication.COnCEPTRole;
import eu.concept.configuration.COnCEPTProperties;
import eu.concept.repository.concept.domain.BriefAnalysis;
import eu.concept.repository.concept.domain.MindMap;
import eu.concept.repository.concept.service.BriefAnalysisService;
import eu.concept.repository.concept.service.FileManagementService;
import eu.concept.repository.concept.service.MindMapService;
import eu.concept.repository.concept.service.NotificationService;
import eu.concept.repository.concept.service.SketchService;
import eu.concept.repository.concept.service.UserCoService;
import eu.concept.repository.openproject.domain.MemberOp;
import eu.concept.repository.openproject.domain.MemberRoleOp;
import eu.concept.repository.openproject.service.MemberOpService;
import eu.concept.repository.openproject.service.MemberRoleOpService;
import eu.concept.repository.openproject.service.ProjectServiceOp;
import eu.concept.response.ApplicationResponse;
import eu.concept.response.BasicResponseCode;
import eu.concept.util.other.NotificationTool;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Restful API for integration with Openproject
 *
 * @author Christos Paraskeva
 */
@RestController
@RequestMapping("/conceptRest/api")
public class RestAPIController {

    private final Logger restLogger = Logger.getLogger(RestAPIController.class.getName());

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

    @Autowired
    NotificationService notificationService;

    @Autowired
    SketchService sketchService;

    @Autowired
    BriefAnalysisService briefAnalysisService;

    @Autowired
    COnCEPTProperties conceptProperties;

    @RequestMapping(value = "/memberships/{project_id}", method = RequestMethod.GET)
    public List<MemberOp> fetchProjectByID(@PathVariable int project_id) {
        return members.fetchMemberhipsByProjectId(project_id);
    }

    @RequestMapping(value = "/notifications_count/{project_id}", method = RequestMethod.GET)
    public long createBriefAnalysis(@PathVariable int project_id) {
        return notificationService.countNotificationsById(project_id);
    }

    //MindMap API
    /**
     * Fetch a MindMap from database based on a specific id
     *
     * @param mid
     * @param uid
     * @return ApplicationResponse object
     */
    @RequestMapping(value = "/mindmap/{id}", method = RequestMethod.GET)
    public ApplicationResponse fetchMindMap(@PathVariable("id") int mid, @RequestParam(value = "uid", defaultValue = "0", required = false) int uid) {
        restLogger.info("Trying to fetch MindMap with mid: " + mid + " and uid: " + uid);
        String responseMessage = "Successfully retrieved mindmap with id: " + mid;
        BasicResponseCode responseCode = BasicResponseCode.SUCCESS;
        MindMap mindmap = mindmapService.fetchMindMapById(mid);
        MemberRoleOp memberRole;
        //MindMap not found exception
        if (null == mindmap) {
            responseMessage = "MindMap with id: " + mid + " could not be found in COnCEPT database";
            responseCode = BasicResponseCode.EXCEPTION;
        } //Non valid member user role
        else if (null == (memberRole = roleService.findByUserId(uid)) || memberRole.getRoleId() == COnCEPTRole.NON_MEMBER.getID()) {
            responseMessage = "User with id: " + uid + " is not member of the COnCEPT platform";
            responseCode = BasicResponseCode.INVALIDATE;

        } //Inefficient privileges
        else if (mindmap.getIsPublic() == 0 && memberRole.getRoleId() == COnCEPTRole.CLIENT.getID()) {
            responseMessage = "Role name: CLIENT is not allowed to perform CRUD actions...";
            responseCode = BasicResponseCode.PERMISSION;
        }
        //Print response message
        restLogger.log(Level.INFO, "Response code: {0} Response message: {1}", new Object[]{responseCode.name(), responseMessage});
        //Return Response
        return new ApplicationResponse(responseCode, responseMessage, mindmap);
    }

    /**
     * Creates/update a MindMap to COnCEPT db
     *
     * @param mindmap
     * @return
     */
    @RequestMapping(value = "/mindmap", method = RequestMethod.POST, consumes = "application/json")
    public ApplicationResponse createMindMap(@RequestBody MindMap mindmap) {

        System.out.println("Thumbnail content: " + (mindmap.getContentThumbnail() == null ? "nothing at al..." : mindmap.getContentThumbnail().toString()));

        System.out.println("ProjectID: " + mindmap.getPid());
        restLogger.info("Trying to create/update a mindmap...");
        String responseMessage = "Could not store mindmap to COnCEPT db... ";
        BasicResponseCode responseCode = BasicResponseCode.UNKNOWN;

        if (null != mindmap && mindmap.getContent().isEmpty()) {
            mindmap.setContent("<map name=\"3\" version=\"tango\"><topic central=\"true\" text=\"COnCEPT Mindmap\" id=\"1\"/></map>");
        }

        if (null == mindmap || null == mindmap.getUserCo()) {
            responseMessage = "Not a valid MinMap object... ";
            responseCode = BasicResponseCode.EXCEPTION;
        } else if (null != (mindmap = mindmapService.store(mindmap)) && mindmap.getId() > 0) {
            responseMessage = "Successfully stored mindmap to COnCEPT db...";
            responseCode = BasicResponseCode.SUCCESS;
        }
        //Print response message
        restLogger.log(Level.INFO, "Response code: {0} Response message: {1}", new Object[]{responseCode.name(), responseMessage});
        return new ApplicationResponse(responseCode, responseMessage, mindmap);

    }

    //Change isPublic Status
    @RequestMapping(value = "/share/{component_id}", method = RequestMethod.POST)
    public int changeIsPublicStatus(@PathVariable int component_id, @RequestParam(value = "isPublic", defaultValue = "0", required = false) short isPublic, @RequestParam(value = "componentCode", defaultValue = "", required = false) String componentCode) {

        switch (componentCode) {

            case "BA":
                int statusCode = briefAnalysisService.changePublicStatus(component_id, isPublic);

                if (isPublic == 0 && statusCode == 1) {
                    BriefAnalysis bf = briefAnalysisService.fetchBriefAnalysisById(component_id);
                    notificationService.storeNotification(bf.getPid(), NotificationTool.BA, NotificationTool.NOTIFICATION_OPERATION.SHARED, "a BriefAnalysis (" + bf.getTitle() + ")", "/resources/img/fm_generic_mm.png", WebController.getCurrentUserCo());
                }
                return statusCode;

            case "FM":
                return fmService.changePublicStatus(component_id, isPublic);

            case "MM":
                return mindmapService.changePublicStatus(component_id, isPublic);

            case "SK":
                return sketchService.changePublicStatus(component_id, isPublic);

            default:
                return 0;
        }

    }

    @RequestMapping(value = "/mm_app/{project_id}", method = RequestMethod.GET)
    public ApplicationResponse createMindMap(@PathVariable("project_id") int project_id) {//@RequestParam(value = "projectID", defaultValue = "0", required = false) int projectID) {
        String responseMessage = "Could create a new MindMap... ";
        BasicResponseCode responseCode = BasicResponseCode.UNKNOWN;
        String currentUserID = String.valueOf(WebController.getCurrentUserCo().getId());
        String createdMMURL = conceptProperties.getmindmapediturl();
        System.out.println(createdMMURL);

        restLogger.log(Level.INFO, "Create URL : {0}", conceptProperties.getmindmapcreateurl());
        String URI = conceptProperties.getmindmapcreateurl().concat(currentUserID).concat("/").concat(String.valueOf(project_id));
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<MindMapCreateResponse> mindmapCreateResponse = restTemplate.getForEntity(URI, MindMapCreateResponse.class);

        if (null != mindmapCreateResponse.getBody() && "OK".equals(mindmapCreateResponse.getBody().getResult())) {
            restLogger.info("Success created MindMap with ID: " + mindmapCreateResponse.getBody().getMindMapId());
            createdMMURL = createdMMURL.concat(String.valueOf(mindmapCreateResponse.getBody().getMindMapId()) + "/" + currentUserID + "/edit");
            restLogger.info("Created Edit MindMap URL: " + createdMMURL);
            responseCode = BasicResponseCode.SUCCESS;
            responseMessage = "Success created MindMap with ID: " + mindmapCreateResponse.getBody().getMindMapId();
        }

        return new ApplicationResponse(responseCode, responseMessage, createdMMURL);
    }

    @XmlRootElement(name = "conceptCreate")
    private static class MindMapCreateResponse {

        private Integer mindMapId;
        private String result;

        public Integer getMindMapId() {
            return mindMapId;
        }

        public void setMindMapId(Integer mindMapId) {
            this.mindMapId = mindMapId;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

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
