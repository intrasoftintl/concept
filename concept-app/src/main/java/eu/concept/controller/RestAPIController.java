package eu.concept.controller;

import com.google.common.base.Joiner;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import eu.concept.authentication.COnCEPTRole;
import eu.concept.configuration.COnCEPTProperties;
import eu.concept.repository.concept.dao.ChatMessageRepository;
import eu.concept.repository.concept.domain.*;
import eu.concept.repository.concept.service.*;
import eu.concept.repository.openproject.domain.MemberOp;
import eu.concept.repository.openproject.domain.MemberRoleOp;
import eu.concept.repository.openproject.service.MemberOpService;
import eu.concept.repository.openproject.service.MemberRoleOpService;
import eu.concept.repository.openproject.service.ProjectServiceOp;
import eu.concept.response.ApplicationResponse;
import eu.concept.response.BasicResponseCode;
import eu.concept.util.other.NotificationTool;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.w3c.dom.Document;
import org.apache.commons.codec.binary.Base64;

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
    SketchService skService;

    @Autowired
    BriefAnalysisService briefAnalysisService;

    @Autowired
    LikesService likesService;

    @Autowired
    COnCEPTProperties conceptProperties;

    @Autowired
    ChatMessageRepository chatMessageRepository;

    @Autowired
    StoryboardService sbService;

    @Autowired
    MoodboardService mbService;

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
                return skService.changePublicStatus(component_id, isPublic);

            case "SB":
                return sbService.changePublicStatus(component_id, isPublic);

            case "MB":
                return mbService.changePublicStatus(component_id, isPublic);

            default:
                return 0;
        }

    }

    //Change Like  status
    @RequestMapping(value = "/like/{component_id}", method = RequestMethod.POST)
    public int changeLikeStatus(@PathVariable int component_id, @RequestParam(value = "componentCode", defaultValue = "", required = false) String componentCode) {
        UserCo currentUser = WebController.getCurrentUserCo();
        Likes likes;
        System.out.println("Current user is : " + currentUser.getId());

        switch (componentCode) {

            case "BA":
                BriefAnalysis ba = new BriefAnalysis(component_id);
                likes = likesService.findBriefAnalysisLike(currentUser, ba);
                if (null == likes) {
                    likes = new Likes(null, currentUser);
                    likes.setBaId(ba);
                } else {
                    likes.setBaId(null);
                }
//                    notificationService.storeNotification(bf.getPid(), NotificationTool.BA, NotificationTool.NOTIFICATION_OPERATION.SHARED, "a BriefAnalysis (" + bf.getTitle() + ")", "/resources/img/fm_generic_mm.png", WebController.getCurrentUserCo());
                return (likesService.storeLikes(likes) ? 1 : 0);

            case "FM":
                FileManagement fm = new FileManagement(component_id);
                likes = likesService.findFileManagementLike(currentUser, fm);
                if (null == likes) {
                    likes = new Likes(null, currentUser);
                    likes.setFmId(fm);
                } else {
                    likes.setFmId(null);
                }
//                    notificationService.storeNotification(bf.getPid(), NotificationTool.BA, NotificationTool.NOTIFICATION_OPERATION.SHARED, "a BriefAnalysis (" + bf.getTitle() + ")", "/resources/img/fm_generic_mm.png", WebController.getCurrentUserCo());
                return (likesService.storeLikes(likes) ? 1 : 0);

            case "MM":
                MindMap mm = new MindMap(component_id);
                likes = likesService.findMindMapLike(currentUser, mm);
                if (null == likes) {
                    likes = new Likes(null, currentUser);
                    likes.setMmId(mm);
                } else {
                    likes.setMmId(null);
                }
//                    notificationService.storeNotification(bf.getPid(), NotificationTool.BA, NotificationTool.NOTIFICATION_OPERATION.SHARED, "a BriefAnalysis (" + bf.getTitle() + ")", "/resources/img/fm_generic_mm.png", WebController.getCurrentUserCo());
                return (likesService.storeLikes(likes) ? 1 : 0);

            case "SK":
                Sketch sk = new Sketch(component_id);
                likes = likesService.findSketchLike(currentUser, sk);
                if (null == likes) {
                    likes = new Likes(null, currentUser);
                    likes.setSkId(sk);
                } else {
                    likes.setSkId(null);
                }

                return (likesService.storeLikes(likes) ? 1 : 0);

            case "SB":
                Storyboard sb = new Storyboard(component_id);
                likes = likesService.findStoryBoardLike(currentUser, sb);
                if (null == likes) {
                    likes = new Likes(null, currentUser);
                    likes.setSbId(sb);
                } else {
                    likes.setSbId(null);
                }
//                    notificationService.storeNotification(bf.getPid(), NotificationTool.BA, NotificationTool.NOTIFICATION_OPERATION.SHARED, "a BriefAnalysis (" + bf.getTitle() + ")", "/resources/img/fm_generic_mm.png", WebController.getCurrentUserCo());
                return (likesService.storeLikes(likes) ? 1 : 0);

            case "MB":
                Moodboard mb = new Moodboard(component_id);
                likes = likesService.findMoodBoardLike(currentUser, mb);
                if (null == likes) {
                    likes = new Likes(null, currentUser);
                    likes.setMbId(mb);
                } else {
                    likes.setMbId(null);
                }
//                    notificationService.storeNotification(bf.getPid(), NotificationTool.BA, NotificationTool.NOTIFICATION_OPERATION.SHARED, "a BriefAnalysis (" + bf.getTitle() + ")", "/resources/img/fm_generic_mm.png", WebController.getCurrentUserCo());
                return (likesService.storeLikes(likes) ? 1 : 0);

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

    //Fetch 100 of the most recent chat messages for project_id
    @RequestMapping(value = "/chatmessages/{project_id}", method = RequestMethod.GET)
    public List<ChatMessage> chatMessagesForProject(@PathVariable int project_id) {
        List<ChatMessage> chatMessages = chatMessageRepository.findTop100ByPidOrderByCreatedDateDesc(project_id);
        Collections.reverse(chatMessages);
        return chatMessages;
    }

    //Store storyboard data
    @RequestMapping(value = "/storyboard/replicate", method = RequestMethod.POST)
    public ApplicationResponse replicateStoryboard(
            @RequestParam(value = "id") Integer sb_id,
            @RequestParam(value = "pid") int projetct_id,
            @RequestParam(value = "uid") int uid,
            @RequestParam(value = "title") String title,
            /*@RequestParam(value = "date") Date date,*/
            @RequestParam(value = "content") String content,
            @RequestParam(value = "content_thumbnail") String content_thumbnail
    ) {
        String fileContent = "";
        try {
            InputStream inputStream = new ByteArrayInputStream(content_thumbnail.getBytes());
            OutputStream outputStream = new ByteArrayOutputStream();
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
            Document doc = f.createDocument("http://www.w3.org/2000/svg", inputStream);
            TranscoderInput input = new TranscoderInput(doc);
            TranscoderOutput output = new TranscoderOutput(outputStream);
            Transcoder transcoder = new PNGTranscoder();
            transcoder.transcode(input, output);
            outputStream.flush();
            String base64 = new String(Base64.encodeBase64(((ByteArrayOutputStream) outputStream).toByteArray()));
            fileContent = "data:".concat("image/png".concat(";base64,").concat(base64));
            //System.out.println(fileContent);

        } catch (IOException | TranscoderException ex) {
            Logger.getLogger(RestAPIController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Storyboard sb = new Storyboard();
        sb.setId(sb_id);
        sb.setPid(projetct_id);
        sb.setUid(userCoService.findById(uid));
        sb.setTitle(title);
        //sb.setCreatedDate(date);
        sb.setContent(content);
        sb.setContentThumbnail(fileContent);

        Storyboard newsb = sbService.store(sb);
        BasicResponseCode responseCode;
        String responseMessage;
        if (newsb != null) {
            responseCode = BasicResponseCode.SUCCESS;
            responseMessage = "Storyboard replicated !";
        } else {
            responseCode = BasicResponseCode.EXCEPTION;
            responseMessage = "Storyboard replication failed !";
        }

        return new ApplicationResponse(responseCode, responseMessage, newsb);
    }
    
        //Store storyboard data
    @RequestMapping(value = "/moodboard/replicate", method = RequestMethod.POST)
    public ApplicationResponse replicateMoodboard(
            @RequestParam(value = "id") Integer sb_id,
            @RequestParam(value = "pid") int projetct_id,
            @RequestParam(value = "uid") int uid,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "content") String content,
            @RequestParam(value = "content_thumbnail") String content_thumbnail
    ) {
        String fileContent = "";
        try {
            InputStream inputStream = new ByteArrayInputStream(content_thumbnail.getBytes());
            OutputStream outputStream = new ByteArrayOutputStream();
            String parser = XMLResourceDescriptor.getXMLParserClassName();
            SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
            Document doc = f.createDocument("http://www.w3.org/2000/svg", inputStream);
            TranscoderInput input = new TranscoderInput(doc);
            TranscoderOutput output = new TranscoderOutput(outputStream);
            Transcoder transcoder = new PNGTranscoder();
            transcoder.transcode(input, output);
            outputStream.flush();
            String base64 = new String(Base64.encodeBase64(((ByteArrayOutputStream) outputStream).toByteArray()));
            fileContent = "data:".concat("image/png".concat(";base64,").concat(base64));
            //System.out.println(fileContent);

        } catch (IOException | TranscoderException ex) {
            Logger.getLogger(RestAPIController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Moodboard mb = new Moodboard();
        mb.setId(sb_id);
        mb.setPid(projetct_id);
        mb.setUid(userCoService.findById(uid));
        mb.setTitle(title);
        mb.setCreatedDate(Calendar.getInstance().getTime());
        mb.setContent(content);
        mb.setContentThumbnail(fileContent);

        Moodboard newsb = mbService.store(mb);
        BasicResponseCode responseCode;
        String responseMessage;
        if (newsb != null) {
            responseCode = BasicResponseCode.SUCCESS;
            responseMessage = "Moodboard replicated !";
        } else {
            responseCode = BasicResponseCode.EXCEPTION;
            responseMessage = "Moodboard replication failed !";
        }

        return new ApplicationResponse(responseCode, responseMessage, newsb);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    

    public String getTagsForText(String content) {
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.post("http://localhost:8081/semantic-enhancer/tags/text").body(content).asJson();
            List<String> tags = new ArrayList<>();
            for (int i = 0; i < jsonResponse.getBody().getArray().length(); i++) {
                JSONObject obj = jsonResponse.getBody().getArray().getJSONObject(i);
                double relevancy = (double) obj.get("relevancy");
                Logger.getLogger(RestAPIController.class.getName()).info("obj " + obj.toString());
                if (relevancy > 0.5) {
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
            HttpResponse<JsonNode> jsonResponse = Unirest.get("http://localhost:8081/semantic-enhancer/tags?url=http://concept.euprojects.net/" + uri).asJson();
            List<String> tags = new ArrayList<>();
            for (int i = 0; i < jsonResponse.getBody().getArray().length(); i++) {
                JSONObject obj = jsonResponse.getBody().getArray().getJSONObject(i);
                double relevancy = (double) obj.get("relevancy");
                if (relevancy > 0.3) {
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
        String keywords = "";
        if (metadata.getComponent().getId().equals("BA")) {
            BriefAnalysis briefAnalysis = briefAnalysisService.fetchBriefAnalysisById(cid);
            String content = briefAnalysis.getContent();
            keywords = getTagsForText(content);
        } else if (metadata.getComponent().getId().equals("FM")) {
            keywords = getTagsForImage("file/" + cid);
        } else if (metadata.getComponent().getId().equals("SK")) {
            keywords = getTagsForImage("skimage/" + cid);
        } else if (metadata.getComponent().getId().equals("SB")) {
            keywords = getTagsForImage("sbimage/" + cid);
        } else if (metadata.getComponent().getId().equals("MB")) {
            keywords = getTagsForImage("mbimage/" + cid);
        }
        return keywords;
    }
}
