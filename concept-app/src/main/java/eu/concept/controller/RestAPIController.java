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
import eu.concept.util.other.Util;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
import org.json.JSONArray;

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

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProjectCategoryService projectCategoryService;

    @Autowired
    MetadataService metadataService;

    @Autowired
    ElasticSearchController elasticSearchController;

    @Autowired
    TimelineService timelineService;

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
        System.out.println("Thumbnail content: " + (mindmap.getContentThumbnail() == null ? "nothing at all..." : mindmap.getContentThumbnail().toString()));
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
            //Insert mindmap to elastic search engine            
            elasticSearchController.insert(Optional.ofNullable(mindmap), Optional.ofNullable(metadataService.fetchMetadataByCidAndComponent(mindmap.getId(), Util.getComponentName(MindMap.class.getSimpleName()))));
            restLogger.info("Inserted into Elastic search.....");
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
                    notificationService.storeNotification(bf.getPid(), NotificationTool.BA, NotificationTool.NOTIFICATION_OPERATION.SHARED, "a BriefAnalysis (" + bf.getTitle() + ")", "/images/fm_generic_mm.png", WebController.getCurrentUserCo());
                }
                return statusCode;

            case "FM":
                return fmService.changePublicStatus(component_id, isPublic);

            case "MM":
                return mindmapService.changePublicStatus(component_id, isPublic);

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
//                    notificationService.storeNotification(bf.getPid(), NotificationTool.BA, NotificationTool.NOTIFICATION_OPERATION.SHARED, "a BriefAnalysis (" + bf.getTitle() + ")", "/images/fm_generic_mm.png", WebController.getCurrentUserCo());
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
//                    notificationService.storeNotification(bf.getPid(), NotificationTool.BA, NotificationTool.NOTIFICATION_OPERATION.SHARED, "a BriefAnalysis (" + bf.getTitle() + ")", "/images/fm_generic_mm.png", WebController.getCurrentUserCo());
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
//                    notificationService.storeNotification(bf.getPid(), NotificationTool.BA, NotificationTool.NOTIFICATION_OPERATION.SHARED, "a BriefAnalysis (" + bf.getTitle() + ")", "/images/fm_generic_mm.png", WebController.getCurrentUserCo());
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
//                    notificationService.storeNotification(bf.getPid(), NotificationTool.BA, NotificationTool.NOTIFICATION_OPERATION.SHARED, "a BriefAnalysis (" + bf.getTitle() + ")", "/images/fm_generic_mm.png", WebController.getCurrentUserCo());
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
//                    notificationService.storeNotification(bf.getPid(), NotificationTool.BA, NotificationTool.NOTIFICATION_OPERATION.SHARED, "a BriefAnalysis (" + bf.getTitle() + ")", "/images/fm_generic_mm.png", WebController.getCurrentUserCo());
                return (likesService.storeLikes(likes) ? 1 : 0);

            default:
                return 0;
        }

    }

    //Change Pin  status
    @RequestMapping(value = "/pin/{component_id}", method = RequestMethod.POST)
    public int changePinStatus(@PathVariable int component_id, @RequestParam(value = "componentCode", required = true) String componentCode, @RequestParam(value = "cid", required = true) int cid, @RequestParam(value = "pid", required = true) int pid, @RequestParam(value = "isPinned", required = true) int isPinned) {
        UserCo currentUser = WebController.getCurrentUserCo();
//        System.out.println("isPinned : " + isPinned);

        //Create a new Component object
        Timeline timeline = new Timeline();
        timeline.setCid(cid);
        timeline.setPid(pid);
        timeline.setComponent(new Component(componentCode));
        timeline.setUid(currentUser);

        switch (componentCode) {

            case "BA":
                //Create Pin
                if (isPinned == 0) {
                    BriefAnalysis ba = briefAnalysisService.fetchBriefAnalysisById(cid);
                    if (null == ba) {
                        return -1;
                    }
                    String baImage = "data:image/png;base64,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";
                    timeline.setOperation("PINNED");
                    timeline.setMessage("a BriefAnalysis (" + ba.getTitle() + ")");
                    timeline.setThumbnail(baImage);
                    timelineService.store(timeline);

                } else //Delete Pin
                {
                    Timeline timelineToDelete = timelineService.findByOther(pid, cid, timeline.getComponent());
                    timelineService.delete(timelineToDelete.getId());
                }

                return 0;

            case "FM":
                //Create Pin
                if (isPinned == 0) {
                    FileManagement fm = fmService.fetchImageById(cid);
                    if (null == fm) {
                        return -1;
                    }
                    timeline.setOperation("PINNED");
                    timeline.setMessage("a File (" + fm.getFilename() + ")");
                    timeline.setThumbnail(fm.getContent());
                    timelineService.store(timeline);
                } else //Delete Pin
                {
                    Timeline timelineToDelete = timelineService.findByOther(pid, cid, timeline.getComponent());
                    timelineService.delete(timelineToDelete.getId());
                }

                return 0;

            case "MM":

                //Create Pin
                if (isPinned == 0) {
                    MindMap mm = mindmapService.fetchMindMapById(cid);
                    if (null == mm) {
                        return -1;
                    }
                    String mmImage = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFAAAAAtCAYAAAA5reyyAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAoxJREFUeNrsms1xwjAQhQ2TO04FcTowZw6BCmIqCFQAVABUQDogVACpAHLgjDsIJbiDZJc8TTQaGTtMEMLsm9EICeHYn1feHycIRCKRSCQSia5SNV9PbLvdxtQNqEWYWrRarTcBWA5eQt3S8tUbQez7dK51Tw1wroBRu6fWpZZR6xHcnk8neueh9bWpC6mlmrWtaD4E2CeAFQss0N4YZ+hD2cLHlaJP4EgCWN8Y8x/iRIq38Yy6IYYbajEsj62ySVs7E4DFECea1SmQfYK3FwssD/FLG3YI3sa3c/TZiVyF6h5bX1sAnsc7C8CSCgvGt+1EENslWrFAV4PTNgMaW+BUC6hTH8KZ2oXgMZzZP1rVAS4BXVUeIODNz3R4hjipLEBs20/L1sxynoGxkRtHJf6M03jRdTUmMeD184qkCGPWakzrHnNCnLaRsQyQtVQSoG5BaUGFOTcONCxsQ0ADDWJyK2HMXzxoUf57sRTPNcCHE3/nVQHhkgCjEy1QUjl6TkWmF4VXzlNDAP6C4nBkZwDkh/3aBpHmOE4c6g4Fc3nHHltizUpZoMo62OtyONJB/BcjZdMvfoI53uKvaOqN3MRi1Wt47L1WcJi7gugK4OEC+S0bV5QRhozw3bOxVo27tG7ELfh5rcl6MdYOcGNWHCdSa3Jsqd20SjmRrKRnjc1YT/sc2dZqNyNAbMmWGMJCKwGQYcXGthrngEzNbEP7fBS6kQY68fSuMpEpCgj8bFLbLsIFTo217wCypLUqU1HgF5a1h1SO1mYAPMaxnZS7nBUTLCWsw3OQLjLN8cKmE7D+Xwyt3VksMENRIa0MwBNDH5XXro7BoLVDzfmwFU59e/0pEolEIpFI5Jm+BRgA3DTreSj6LvUAAAAASUVORK5CYII=";
                    timeline.setOperation("PINNED");
                    timeline.setMessage("a MindMap (" + mm.getTitle() + ")");
                    timeline.setThumbnail(mmImage);
                    timelineService.store(timeline);

                } else //Delete Pin
                {
                    Timeline timelineToDelete = timelineService.findByOther(pid, cid, timeline.getComponent());
                    timelineService.delete(timelineToDelete.getId());
                }

                return 0;

            case "SB":

                //Create Pin
                if (isPinned == 0) {
                    Storyboard sb = sbService.fetchStoryboardById(cid);
                    if (null == sb) {
                        return -1;
                    }
                    timeline.setOperation("PINNED");
                    timeline.setMessage("a Storyboard (" + sb.getTitle() + ")");
                    timeline.setThumbnail(sb.getContentThumbnail());
                    timelineService.store(timeline);
                } else //Delete Pin
                {
                    Timeline timelineToDelete = timelineService.findByOther(pid, cid, timeline.getComponent());
                    timelineService.delete(timelineToDelete.getId());
                }

                return 0;

            case "MB":

                //Create Pin
                if (isPinned == 0) {
                    Moodboard mb = mbService.fetchMoodboardById(cid);
                    if (null == mb) {
                        return -1;
                    }
                    timeline.setOperation("PINNED");
                    timeline.setMessage("a Moodboard (" + mb.getTitle() + ")");
                    timeline.setThumbnail(mb.getContentThumbnail());
                    timelineService.store(timeline);
                } else //Delete Pin
                {
                    Timeline timelineToDelete = timelineService.findByOther(pid, cid, timeline.getComponent());
                    timelineService.delete(timelineToDelete.getId());
                }

                return 0;

            default:
                return 0;
        }

    }

    @RequestMapping(value = "/mm_app/{project_id}", method = RequestMethod.GET)
    public ApplicationResponse createMindMap(@PathVariable("project_id") int project_id) {
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
        } catch (IOException | TranscoderException ex) {
            Logger.getLogger(RestAPIController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Storyboard sb = new Storyboard();
        sb.setId(sb_id);
        sb.setPid(projetct_id);
        sb.setUid(userCoService.findById(uid));
        sb.setTitle(title);
        sb.setContent(content);
        sb.setContentThumbnail(fileContent);
        //Save new stroyboard
        Storyboard newsb = sbService.store(sb);

        //Insert document to elastic search engine    
        elasticSearchController.insert(Optional.ofNullable(newsb), Optional.ofNullable(metadataService.fetchMetadataByCidAndComponent(sb_id, Util.getComponentName(Storyboard.class.getSimpleName()))));

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
            @RequestParam(value = "id") Integer mb_id,
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
        } catch (IOException | TranscoderException ex) {
            Logger.getLogger(RestAPIController.class.getName()).log(Level.SEVERE, null, ex);
        }

        Moodboard mb = new Moodboard();
        mb.setId(mb_id);
        mb.setPid(projetct_id);
        mb.setUid(userCoService.findById(uid));
        mb.setTitle(title);
        mb.setCreatedDate(Calendar.getInstance().getTime());
        mb.setContent(content);
        mb.setContentThumbnail(fileContent);
        //Save new moodboard
        Moodboard newsb = mbService.store(mb);

        //Insert document to elastic search engine    
        elasticSearchController.insert(Optional.ofNullable(newsb), Optional.ofNullable(metadataService.fetchMetadataByCidAndComponent(mb_id, Util.getComponentName(Moodboard.class.getSimpleName()))));

        //Create response body
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

    @RequestMapping(value = "/category/search", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public String autoCompleteCategories(@RequestParam("keyword") String keyword, @RequestParam("projectID") String projectID, @RequestParam("callback") String callback) {

        List<Category> categories = categoryService.getCategoriesByKeyword(keyword, projectCategoryService.findByPid(Integer.valueOf(projectID)));
        JSONObject jsonResponse = new JSONObject();
        JSONArray jsonValues = new JSONArray();

        if (null != categories && !categories.isEmpty()) {
            categories.stream().forEach(category -> {
                JSONObject jsonCategory = new JSONObject();
                jsonCategory.put("id", category.getId());
                jsonCategory.put("value", category.getName());
                jsonValues.put(jsonCategory);
            });

            jsonResponse.put("total", categories.size());
            jsonResponse.put("values", jsonValues);

        } else {
            jsonResponse.put("total", 0);
            jsonResponse.put("values", jsonValues);
        }

        return callback + "(" + jsonResponse.toString() + ")";
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

    @RequestMapping(value = "/keywords", method = RequestMethod.GET)
    public List<String> getKeywords() {
        return metadataService.findAllMetadata();
    }

}
