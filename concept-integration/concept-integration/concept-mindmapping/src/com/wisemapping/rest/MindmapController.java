/*
 *    Copyright [2015] [wisemapping]
 *
 *   Licensed under WiseMapping Public License, Version 1.0 (the "License").
 *   It is basically the Apache License, Version 2.0 (the "License") plus the
 *   "powered by wisemapping" text requirement on every single page;
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the license at
 *
 *       http://www.wisemapping.org/license
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.wisemapping.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.concept.json.fetch.MindMapVO;
import com.concept.json.fetch.ResponseVO;
import com.concept.json.fetch.UserVO;
import com.mangofactory.swagger.annotations.ApiIgnore;
import com.wisemapping.exceptions.ImportUnexpectedException;
import com.wisemapping.exceptions.LabelCouldNotFoundException;
import com.wisemapping.exceptions.MapCouldNotFoundException;
import com.wisemapping.exceptions.MultipleSessionsOpenException;
import com.wisemapping.exceptions.SessionExpiredException;
import com.wisemapping.exceptions.WiseMappingException;
import com.wisemapping.exporter.ExportException;
import com.wisemapping.exporter.ExportFormat;
import com.wisemapping.exporter.ExportProperties;
import com.wisemapping.exporter.ExporterFactory;
import com.wisemapping.importer.ImportFormat;
import com.wisemapping.importer.Importer;
import com.wisemapping.importer.ImporterException;
import com.wisemapping.importer.ImporterFactory;
import com.wisemapping.model.Collaboration;
import com.wisemapping.model.CollaborationProperties;
import com.wisemapping.model.CollaborationRole;
import com.wisemapping.model.Constants;
import com.wisemapping.model.Label;
import com.wisemapping.model.MindMapHistory;
import com.wisemapping.model.Mindmap;
import com.wisemapping.model.Projects;
import com.wisemapping.model.User;
import com.wisemapping.rest.model.RestCollaboration;
import com.wisemapping.rest.model.RestCollaborationList;
import com.wisemapping.rest.model.RestConcept;
import com.wisemapping.rest.model.RestLabel;
import com.wisemapping.rest.model.RestMindmap;
import com.wisemapping.rest.model.RestMindmapHistory;
import com.wisemapping.rest.model.RestMindmapHistoryList;
import com.wisemapping.rest.model.RestMindmapInfo;
import com.wisemapping.rest.model.RestMindmapList;
import com.wisemapping.security.Utils;
import com.wisemapping.service.CollaborationException;
import com.wisemapping.service.LabelService;
import com.wisemapping.service.LockInfo;
import com.wisemapping.service.LockManager;
import com.wisemapping.service.MindmapService;
import com.wisemapping.service.UserService;
import com.wisemapping.validator.MapInfoValidator;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiParam;

@Api(value = "mindmap", description = "User Mindmap Objects.")
@Controller
public class MindmapController extends BaseController {

	public static final String LATEST_HISTORY_REVISION = "latest";
	@Qualifier("mindmapService")
	@Autowired
	private MindmapService mindmapService;

	@Qualifier("labelService")
	@Autowired
	private LabelService labelService;

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	final Logger logger = Logger.getLogger("com.wisemapping");

	private Integer idUserUpdateConcept;
	private Integer idProjectUpdateConcept;

	@RequestMapping(method = RequestMethod.GET, value = "/maps/{id}", produces = { "application/json", "application/xml", "text/html" })
	@ResponseBody
	public RestMindmap retrieve(@PathVariable int id) throws WiseMappingException {
		final User user = Utils.getUser();
		final Mindmap mindMap = findMindmapById(id);
		return new RestMindmap(mindMap, user);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/maps/{id}", produces = { "application/wisemapping+xml" }, params = { "download=wxml" })
	@ResponseBody
	public ModelAndView retrieveAsWise(@PathVariable int id) throws WiseMappingException {
		final Mindmap mindMap = findMindmapById(id);
		final Map<String, Object> values = new HashMap<String, Object>();

		final User user = Utils.getUser();
		values.put("mindmap", new RestMindmap(mindMap, user));
		values.put("filename", mindMap.getTitle());
		return new ModelAndView("transformViewWise", values);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/maps/{id}", produces = { "application/freemind" }, params = { "download=mm", "version" })
	@ResponseBody
	public ModelAndView retrieveDocumentAsFreemind(@PathVariable int id, @RequestParam(value = "version") String version) throws IOException,
			MapCouldNotFoundException {
		final Mindmap mindMap = findMindmapById(id);
		final Map<String, Object> values = new HashMap<String, Object>();
		values.put("content", mindMap.getXmlStr());
		values.put("filename", mindMap.getTitle());
		values.put("version", version);
		return new ModelAndView("transformViewFreemind", values);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/maps/{id}", produces = { "text/plain" }, params = { "download=txt" })
	@ResponseBody
	public ModelAndView retrieveDocumentAsText(@PathVariable int id) throws IOException, MapCouldNotFoundException {
		final Mindmap mindMap = findMindmapById(id);
		final Map<String, Object> values = new HashMap<String, Object>();
		values.put("content", mindMap.getXmlStr());
		values.put("filename", mindMap.getTitle());
		return new ModelAndView("transformViewTxt", values);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/maps/{id}", produces = { "application/vnd.mindjet.mindmanager" }, params = { "download=mmap" })
	@ResponseBody
	public ModelAndView retrieveDocumentAsMindJet(@PathVariable int id) throws IOException, MapCouldNotFoundException {
		final Mindmap mindMap = findMindmapById(id);
		final Map<String, Object> values = new HashMap<String, Object>();
		values.put("content", mindMap.getXmlStr());
		values.put("filename", mindMap.getTitle());
		return new ModelAndView("transformViewMMap", values);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/maps/{id}", produces = { "application/vnd.ms-excel" }, params = { "download=xls" })
	@ResponseBody
	public ModelAndView retrieveDocumentAsExcel(@PathVariable int id) throws IOException, MapCouldNotFoundException {
		final Mindmap mindMap = findMindmapById(id);
		final Map<String, Object> values = new HashMap<String, Object>();
		values.put("content", mindMap.getXmlStr());
		values.put("filename", mindMap.getTitle());
		return new ModelAndView("transformViewXls", values);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/maps/{id}", produces = { "application/vnd.oasis.opendocument.text" }, params = { "download=odt" })
	@ResponseBody
	public ModelAndView retrieveDocumentAsOdt(@PathVariable int id) throws IOException, MapCouldNotFoundException {
		final Mindmap mindMap = findMindmapById(id);
		final Map<String, Object> values = new HashMap<String, Object>();
		values.put("content", mindMap.getXmlStr());
		values.put("filename", mindMap.getTitle());
		return new ModelAndView("transformViewOdt", values);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/maps/", produces = { "application/json", "application/xml" })
	public RestMindmapList retrieveList(@RequestParam(required = false) String q) throws IOException {
		final User user = Utils.getUser();

		final MindmapFilter filter = MindmapFilter.parse(q);
		final List<Collaboration> collaborations = mindmapService.findCollaborations(user);

		final List<Mindmap> mindmaps = new ArrayList<Mindmap>();
		for (Collaboration collaboration : collaborations) {
			final Mindmap mindmap = collaboration.getMindMap();
			if (filter.accept(mindmap, user)) {
				try {
					/*
					 * Projects project =
					 * mindmapService.findProjectById(Integer.
					 * parseInt(mindmap.getIdProject()));
					 * mindmap.setProjectTitle(project.getTitle());
					 */
					List<Projects> allProjects = mindmapService.getAllProjects();
					for (Projects project : allProjects) {
						if (project.getId() == Integer.parseInt(mindmap.getIdProject())) {
							mindmap.setProjectTitle(project.getTitle());
						}
					}
				} catch (Exception ex) {
					mindmap.setProjectTitle(mindmap.getIdProject());
				}
				mindmaps.add(mindmap);
			}
		}
		return new RestMindmapList(mindmaps, user);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/maps/{id}/history", produces = { "application/json", "application/xml" })
	public RestMindmapHistoryList retrieveHistory(@PathVariable int id) throws IOException {
		final List<MindMapHistory> histories = mindmapService.findMindmapHistory(id);
		final RestMindmapHistoryList result = new RestMindmapHistoryList();
		for (MindMapHistory history : histories) {
			result.addHistory(new RestMindmapHistory(history));
		}
		return result;
	}

	@RequestMapping(value = "/maps/{id}/history/{hid}", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void updateRevertMindmap(@PathVariable int id, @PathVariable String hid) throws WiseMappingException, IOException {
		final Mindmap mindmap = findMindmapById(id);
		final User user = Utils.getUser();

		if (LATEST_HISTORY_REVISION.equals(hid)) {
			// Revert to the latest stored version ...
			List<MindMapHistory> mindmapHistory = mindmapService.findMindmapHistory(id);
			if (mindmapHistory.size() > 0) {
				final MindMapHistory mindMapHistory = mindmapHistory.get(0);
				mindmap.setZippedXml(mindMapHistory.getZippedXml());
				saveMindmapDocument(true, mindmap, user);
			}
		} else {
			mindmapService.revertChange(mindmap, Integer.parseInt(hid));
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/maps/{id}/document", consumes = { "application/xml", "application/json" }, produces = {
			"application/json", "application/xml" })
	@ResponseBody
	public Long updateDocument(@RequestBody RestMindmap restMindmap, @PathVariable int id, @RequestParam(required = false) boolean minor,
			@RequestParam(required = false) boolean saveConcept, @RequestParam(required = false) int idUserConcept, @RequestParam(required = false) Long timestamp,
			@RequestParam(required = false) Long session, HttpServletRequest request) throws WiseMappingException, IOException {
		// logger.debug("saveConcept " + saveConcept);
		final Mindmap mindmap = findMindmapById(id);
		final User user = Utils.getUser();
		// Validate arguments ...
		final String properties = restMindmap.getProperties();
		if (properties == null) {
			throw new IllegalArgumentException("Map properties can not be null");
		}
		// Could the map be updated ?
		if (session != null) {
			verifyLock(mindmap, user, session, timestamp);
		}
		// Update collaboration properties ...
		final CollaborationProperties collaborationProperties = mindmap.findCollaborationProperties(user);
		collaborationProperties.setMindmapProperties(properties);
		// Validate content ...
		String xml = restMindmap.getXml();
		if (xml == null) {
			throw new IllegalArgumentException("Map xml can not be null");
		}
		mindmap.setXmlStr(xml);
		mindmap.setThumbnail(binary2hexadecimal(request, restMindmap.getMySvg()));
		mindmap.setTitle(restMindmap.getTitle());
		// Update map ...
		if (saveConcept) {
			mindmap.setIdUserConcept(idUserConcept);
			if (this.idProjectUpdateConcept != null) {
				mindmap.setIdProjectConcept(idProjectUpdateConcept);
			}
			saveMindMap(request, mindmap);
		} else {
			saveMindmapDocument(minor, mindmap, user);
		}
		// Update edition timeout ...
		final LockManager lockManager = mindmapService.getLockManager();
		long result = -1;
		if (session != null) {
			final LockInfo lockInfo = lockManager.updateExpirationTimeout(mindmap, user);
			result = lockInfo.getTimestamp();
		}
		return result;
	}

	private void saveMindMap(HttpServletRequest request, final Mindmap mindmap) throws UnsupportedEncodingException, IOException {
		RestTemplate restTemplate = new RestTemplate();
		MindMapVO mindMapVO = generateMindMapVO(request, mindmap);
		UserVO userVO = new UserVO();
		userVO.setId(mindmap.getIdUserConcept());
		mindMapVO.setUserCo(userVO);
		Map<String, String> vars = new HashMap<String, String>();
		logger.debug("requestVO ->" + mindMapVO);
		restTemplate.postForObject(mindmapService.getUrlSave(), mindMapVO, ResponseVO.class, vars);
		// logger.debug("postForObject ->" + postForObject);
	}

	private MindMapVO generateMindMapVO(HttpServletRequest request, final Mindmap mindmap) throws UnsupportedEncodingException, IOException {
		MindMapVO mindMapVO = new MindMapVO();
		mindMapVO.setId(mindmap.getId());
		mindMapVO.setPid(mindmap.getIdProjectConcept());
		mindMapVO.setTitle(mindmap.getTitle());
		mindMapVO.setContent(mindmap.getXmlStr());
		mindMapVO.setContentThumbnail(new String (mindmap.getThumbnail()));
		mindMapVO.setCreatedDate(mindmap.getCreationTime().getTimeInMillis());
		mindMapVO.setIsPublic(mindmap.isPublic() ? 1 : 0);
		return mindMapVO;
	}

	private byte[] binary2hexadecimal(HttpServletRequest request, final String mySvg) throws IOException, UnsupportedEncodingException {
		final ServletContext servletContext = request.getSession().getServletContext();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			final ExporterFactory factory = new ExporterFactory(servletContext);
			ExportFormat exportFormat = ExportFormat.fromContentType("image/png");
			final ExportProperties exportProperties = ExportProperties.create(exportFormat);
			final ExportProperties.ImageProperties imageProperties = (ExportProperties.ImageProperties) exportProperties;
			imageProperties.setSize(ExportProperties.ImageProperties.Size.MEDIUM);
			factory.export(exportProperties, null, outputStream, mySvg);
			//FileUtils.writeByteArrayToFile(new File("c:/Users/A549232/Desktop/img.jpg"), outputStream.toByteArray());
		} catch (ParserConfigurationException e) {
			new WiseMappingException(e.getMessage());
		} catch (ExportException e) {
			new WiseMappingException(e.getMessage());
		} catch (TranscoderException e) {
			new WiseMappingException(e.getMessage());
		}
		return Hex.encodeHexString(outputStream.toByteArray()).getBytes();
	}

	@ApiIgnore
	@RequestMapping(method = RequestMethod.GET, value = { "/maps/{id}/document/xml", "/maps/{id}/document/xml-pub" }, consumes = { "text/plain" }, produces = { "application/xml" })
	@ResponseBody
	public byte[] retrieveDocument(@PathVariable int id, @NotNull HttpServletResponse response, HttpServletRequest request)
			throws WiseMappingException, IOException {
		// I should not return byte, but there is some encoding issue here.
		// Further research needed.
		response.setCharacterEncoding("UTF-8");
		final Mindmap mindmap = findMindmapById(id);
		MindMapVO mindMapVO = fecthMindMap(id, (long)mindmap.getIdUserConcept());
		String xmlStr = mindMapVO.getContent();
		return xmlStr.getBytes("UTF-8");
	}
	
	@ApiIgnore
	@RequestMapping(method = RequestMethod.GET, value = { "/maps/{id}/{idUser}/document/concept/xml", "/maps/{id}/{idUser}/document/concept/xml-pub" }, consumes = { "text/plain" }, produces = { "application/xml" })
	@ResponseBody
	public byte[] retrieveDocumentConcept(@PathVariable int id, @PathVariable long idUser, @NotNull HttpServletResponse response, HttpServletRequest request)
			throws WiseMappingException, IOException {
		// I should not return byte, but there is some encoding issue here.
		// Further research needed.
		response.setCharacterEncoding("UTF-8");
		MindMapVO mindMapVO = fecthMindMap(id, idUser);
		String xmlStr = mindMapVO.getContent();
		return xmlStr.getBytes("UTF-8");
	}

	private MindMapVO fecthMindMap(int idMindmap, final Long concetpUserId) {
		RestTemplate restTemplate = new RestTemplate();
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("id", Integer.toString(idMindmap));
		vars.put("uid", Long.toString(concetpUserId));
		// 69?uid=3
		// String myUrl = mindmapService.getUrlFetch() + userId;
		String myUrl = mindmapService.getUrlFetch();
		ResponseVO responseVO = restTemplate.getForObject(myUrl, ResponseVO.class, vars);
		logger.debug(responseVO);
		logger.debug(responseVO.getReturnobject().getContent());
		// String xmlStr = mindmap.getXmlStr();
		MindMapVO mindMapVO = responseVO.getReturnobject();
		return mindMapVO;
	}

	@ApiIgnore
	@RequestMapping(method = RequestMethod.PUT, value = { "/maps/{id}/document/xml" }, consumes = { "text/plain" })
	@ResponseBody
	public void updateDocument(@PathVariable int id, @RequestBody String xmlDoc) throws WiseMappingException, IOException {
		final Mindmap mindmap = findMindmapById(id);
		final User user = Utils.getUser();
		if (xmlDoc != null && !xmlDoc.isEmpty()) {
			mindmap.setXmlStr(xmlDoc);
		}
		mindmap.setXmlStr(xmlDoc);
		saveMindmapDocument(false, mindmap, user);
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/maps/{id}/{hid}/document/xml" }, consumes = { "text/plain" }, produces = { "application/xml" })
	@ResponseBody
	public byte[] retrieveDocument(@PathVariable int id, @PathVariable int hid, @NotNull HttpServletResponse response) throws WiseMappingException,
			IOException {
		// I should not return byte, but there is some encoding issue here.
		// Further research needed.
		response.setCharacterEncoding("UTF-8");
		final MindMapHistory mindmapHistory = mindmapService.findMindmapHistory(id, hid);
		return mindmapHistory.getUnzipXml();
	}

	private void verifyLock(@NotNull Mindmap mindmap, @NotNull User user, long session, long timestamp) throws WiseMappingException {
		// The lock was lost, reclaim as the ownership of it.
		final LockManager lockManager = mindmapService.getLockManager();
		final boolean lockLost = lockManager.isLocked(mindmap);
		if (!lockLost) {
			lockManager.lock(mindmap, user, session);
		}
		final LockInfo lockInfo = lockManager.getLockInfo(mindmap);
		if (lockInfo.getUser().identityEquality(user)) {
			final boolean outdated = mindmap.getLastModificationTime().getTimeInMillis() > timestamp;
			if (lockInfo.getSession() == session) {
				// Timestamp might not be returned to the client. This try to
				// cover this case, ignoring the client timestamp check.
				final User lastEditor = mindmap.getLastEditor();
				boolean editedBySameUser = lastEditor == null || user.identityEquality(lastEditor);
				if (outdated && !editedBySameUser) {
					throw new SessionExpiredException("Map has been updated by " + (lastEditor.getEmail()) + ",Timestamp:" + timestamp + ","
							+ mindmap.getLastModificationTime().getTimeInMillis() + ", User:" + lastEditor.getId() + ":" + user.getId() + ",Mail:'"
							+ lastEditor.getEmail() + "':'" + user.getEmail(), lastEditor);
				}
			} else if (outdated) {
				throw new MultipleSessionsOpenException("Sessions:" + session + ":" + lockInfo.getSession() + ",Timestamp: " + timestamp + ": "
						+ lockInfo.getTimestamp() + ",User:");
			}
		} else {
			throw new SessionExpiredException("Different Users.", lockInfo.getUser());
		}
	}

	/**
	 * The intention of this method is the update of several properties at once
	 * ...
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/maps/{id}", consumes = { "application/xml", "application/json" }, produces = {
			"application/json", "application/xml" })
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void updateProperties(@RequestBody RestMindmap restMindmap, @PathVariable int id, @RequestParam(required = false) boolean minor)
			throws IOException, WiseMappingException {

		final Mindmap mindmap = findMindmapById(id);
		final User user = Utils.getUser();

		final String xml = restMindmap.getXml();
		if (xml != null && !xml.isEmpty()) {
			mindmap.setXmlStr(xml);
		}

		// Update title ...
		final String title = restMindmap.getTitle();
		if (title != null && !title.equals(mindmap.getTitle())) {
			if (mindmapService.getMindmapByTitle(title, user) != null) {
				throw buildValidationException("title", "You already have a map with this title");
			}
			mindmap.setTitle(title);
		}

		// Update description ...
		final String description = restMindmap.getDescription();
		if (description != null) {
			mindmap.setDescription(description);
		}

		final String tags = restMindmap.getTags();
		if (tags != null) {
			mindmap.setTags(tags);
		}

		// Update document properties ...
		final String properties = restMindmap.getProperties();
		if (properties != null) {
			final CollaborationProperties collaborationProperties = mindmap.findCollaborationProperties(user);
			collaborationProperties.setMindmapProperties(properties);
		}

		// Update map ...
		saveMindmapDocument(minor, mindmap, user);
	}

	@NotNull
	private Mindmap findMindmapById(int id) throws MapCouldNotFoundException {
		Mindmap result = mindmapService.findMindmapById(id);
		if (result == null) {
			throw new MapCouldNotFoundException("Map could not be found. Id:" + id);
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/maps/{id}/title", consumes = { "text/plain" }, produces = { "application/json",
			"application/xml" })
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void updateTitle(@RequestBody String title, @PathVariable int id) throws WiseMappingException {

		final Mindmap mindMap = findMindmapById(id);
		final User user = Utils.getUser();

		// Is there a map with the same name ?
		if (mindmapService.getMindmapByTitle(title, user) != null) {

			throw buildValidationException("title", "You already have a mindmap with this title");
		}

		// Update map ...
		final Mindmap mindmap = findMindmapById(id);
		mindmap.setTitle(title);
		mindmapService.updateMindmap(mindMap, !true);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/maps/{id}/collabs", consumes = { "application/json", "application/xml" }, produces = {
			"application/json", "application/xml" })
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void updateCollabs(@PathVariable int id, @NotNull @RequestBody RestCollaborationList restCollabs) throws CollaborationException,
			MapCouldNotFoundException {
		final Mindmap mindMap = findMindmapById(id);

		// Only owner can change collaborators...
		final User user = Utils.getUser();
		if (!mindMap.hasPermissions(user, CollaborationRole.OWNER)) {
			throw new IllegalArgumentException("No enough permissions");
		}

		// Compare one by one if some of the elements has been changed ....
		final Set<Collaboration> collabsToRemove = new HashSet<Collaboration>(mindMap.getCollaborations());
		for (RestCollaboration restCollab : restCollabs.getCollaborations()) {
			final Collaboration collaboration = mindMap.findCollaboration(restCollab.getEmail());
			// Validate role format ...
			String roleStr = restCollab.getRole();
			if (roleStr == null) {
				throw new IllegalArgumentException(roleStr + " is not a valid role");
			}

			// Remove from the list of pendings to remove ...
			if (collaboration != null) {
				collabsToRemove.remove(collaboration);
			}

			// Is owner ?
			final CollaborationRole role = CollaborationRole.valueOf(roleStr.toUpperCase());
			if (role != CollaborationRole.OWNER) {
				mindmapService.addCollaboration(mindMap, restCollab.getEmail(), role, restCollabs.getMessage());
			}

		}

		// Remove all collaborations that no applies anymore ..
		for (final Collaboration collaboration : collabsToRemove) {
			mindmapService.removeCollaboration(mindMap, collaboration);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/maps/{id}/collabs", produces = { "application/json", "application/xml" })
	public RestCollaborationList retrieveList(@PathVariable int id) throws MapCouldNotFoundException {
		final Mindmap mindMap = findMindmapById(id);

		final Set<Collaboration> collaborations = mindMap.getCollaborations();
		final List<RestCollaboration> collabs = new ArrayList<RestCollaboration>();
		for (Collaboration collaboration : collaborations) {
			collabs.add(new RestCollaboration(collaboration));
		}

		final RestCollaborationList result = new RestCollaborationList();
		result.setCollaborations(collabs);

		return result;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/maps/{id}/description", consumes = { "text/plain" }, produces = { "application/json",
			"application/xml" })
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void updateDescription(@RequestBody String description, @PathVariable int id) throws WiseMappingException {

		final Mindmap mindMap = findMindmapById(id);
		// final User user = Utils.getUser();

		// Update map ...
		final Mindmap mindmap = findMindmapById(id);
		mindmap.setDescription(description);
		mindmapService.updateMindmap(mindMap, !true);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/maps/{id}/publish", consumes = { "text/plain" }, produces = { "application/json",
			"application/xml" })
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void updatePublishState(@RequestBody String value, @PathVariable int id) throws WiseMappingException {

		final Mindmap mindMap = findMindmapById(id);

		final User user = Utils.getUser();
		if (!mindMap.hasPermissions(user, CollaborationRole.OWNER)) {
			throw new IllegalArgumentException("No enough to execute this operation");
		}

		// Update map status ...
		mindMap.setPublic(Boolean.parseBoolean(value));
		mindmapService.updateMindmap(mindMap, !true);

	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/maps/{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteMapById(@PathVariable int id) throws IOException, WiseMappingException {
		final User user = Utils.getUser();
		final Mindmap mindmap = findMindmapById(id);
		mindmapService.removeMindmap(mindmap, user);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/maps/{id}/starred", consumes = { "text/plain" }, produces = { "application/json",
			"application/xml" })
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void updateStarredState(@RequestBody @ApiParam(defaultValue = "false", allowableValues = "true,false") String value, @PathVariable int id)
			throws WiseMappingException {

		final Mindmap mindmap = findMindmapById(id);
		final User user = Utils.getUser();

		// Update map status ...
		final boolean starred = Boolean.parseBoolean(value);
		final Collaboration collaboration = mindmap.findCollaboration(user);
		if (collaboration == null) {
			throw new WiseMappingException("No enough permissions.");
		}
		collaboration.getCollaborationProperties().setStarred(starred);
		mindmapService.updateCollaboration(user, collaboration);
	}

	@ApiIgnore
	@RequestMapping(method = RequestMethod.PUT, value = "/maps/{id}/lock", consumes = { "text/plain" }, produces = { "application/json",
			"application/xml" })
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void updateMapLock(@RequestBody String value, @PathVariable int id) throws IOException, WiseMappingException {
		final User user = Utils.getUser();
		final LockManager lockManager = mindmapService.getLockManager();
		final Mindmap mindmap = findMindmapById(id);

		final boolean lock = Boolean.parseBoolean(value);
		if (!lock) {
			lockManager.unlock(mindmap, user);
		} else {
			throw new UnsupportedOperationException("REST lock must be implemented.");
		}
	}

	@ApiIgnore
	@RequestMapping(method = RequestMethod.DELETE, value = "/maps/batch")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void batchDelete(@RequestParam(required = true) String ids) throws IOException, WiseMappingException {
		final User user = Utils.getUser();
		final String[] mapsIds = ids.split(",");
		for (final String mapId : mapsIds) {
			final Mindmap mindmap = findMindmapById(Integer.parseInt(mapId));
			mindmapService.removeMindmap(mindmap, user);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/maps", consumes = { "application/xml", "application/json", "application/wisemapping+xml" })
	@ResponseStatus(value = HttpStatus.CREATED)
	public void createMap(@RequestBody RestMindmap restMindmap, @NotNull HttpServletResponse response, @RequestParam(required = false) String title,
			@RequestParam(required = false) String description, HttpServletRequest request) throws IOException, WiseMappingException {

		// Overwrite title and description if they where specified by parameter.
		if (title != null && !title.isEmpty()) {
			restMindmap.setTitle(title);
		}
		if (description != null && !description.isEmpty()) {
			restMindmap.setDescription(description);
		}

		// Validate ...
		final BindingResult result = new BeanPropertyBindingResult(restMindmap, "");
		new MapInfoValidator(mindmapService).validate(restMindmap.getDelegated(), result);
		if (result.hasErrors()) {
			throw new ValidationException(result);
		}

		// If the user has not specified the xml content, add one ...
		final Mindmap delegated = restMindmap.getDelegated();
		String xml = restMindmap.getXml();
		if (xml == null || xml.isEmpty()) {
			xml = Mindmap.getDefaultMindmapXml(restMindmap.getTitle());
		}
		delegated.setXmlStr(xml);
		delegated.setThumbnail(this.binary2hexadecimal(request, ""));

		// Add new mindmap ...
		final User user = Utils.getUser();
		mindmapService.addMindmap(delegated, user);
		saveMindMap(request, delegated);

		// Return the new created map ...
		response.setHeader("Location", "/service/maps/" + delegated.getId());
		response.setHeader("ResourceId", Integer.toString(delegated.getId()));
	}

	@RequestMapping(method = RequestMethod.POST, value = "/maps", consumes = { "application/freemind" })
	@ResponseStatus(value = HttpStatus.CREATED)
	public void createMapFromFreemind(@RequestBody byte[] freemindXml, @RequestParam(required = true) String title,
			@RequestParam(required = false) String description, @NotNull HttpServletResponse response, HttpServletRequest request)
			throws IOException, WiseMappingException {

		// Convert map ...
		final Mindmap mindMap;
		try {
			final Importer importer = ImporterFactory.getInstance().getImporter(ImportFormat.FREEMIND);
			final ByteArrayInputStream stream = new ByteArrayInputStream(freemindXml);
			mindMap = importer.importMap(title, "", stream);
		} catch (ImporterException e) {
			// @Todo: This should be an illegal argument exception. Review the
			// all the other cases.
			throw buildValidationException("xml",
					"The selected file does not seems to be a valid Freemind or WiseMapping file. Contact support in case the problem persists.");
		} catch (Throwable e) {
			throw new ImportUnexpectedException(e, freemindXml);
		}

		// Save new map ...
		createMap(new RestMindmap(mindMap, null), response, title, description, request);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/maps/{id}", consumes = { "application/xml", "application/json" }, produces = {
			"application/xml", "application/json", "text/plain" })
	@ResponseStatus(value = HttpStatus.CREATED)
	public void createDuplicate(@RequestBody RestMindmapInfo restMindmap, @PathVariable int id, @NotNull HttpServletResponse response)
			throws IOException, WiseMappingException {
		// Validate ...
		final BindingResult result = new BeanPropertyBindingResult(restMindmap, "");
		new MapInfoValidator(mindmapService).validate(restMindmap.getDelegated(), result);
		if (result.hasErrors()) {
			throw new ValidationException(result);
		}

		// Some basic validations ...
		final User user = Utils.getUser();

		// Create a shallowCopy of the map ...
		final Mindmap mindMap = findMindmapById(id);
		final Mindmap clonedMap = mindMap.shallowClone();
		clonedMap.setTitle(restMindmap.getTitle());
		clonedMap.setDescription(restMindmap.getDescription());

		// Add new mindmap ...
		mindmapService.addMindmap(clonedMap, user);

		// Return the new created map ...
		response.setHeader("Location", "/service/maps/" + clonedMap.getId());
		response.setHeader("ResourceId", Integer.toString(clonedMap.getId()));
	}

	private void saveMindmapDocument(boolean minor, @NotNull final Mindmap mindMap, @NotNull final User user) throws WiseMappingException {
		final Calendar now = Calendar.getInstance();
		mindMap.setLastModificationTime(now);
		mindMap.setLastEditor(user);
		mindmapService.updateMindmap(mindMap, !minor);
	}

	private ValidationException buildValidationException(@NotNull String fieldName, @NotNull String message) throws WiseMappingException {
		final BindingResult result = new BeanPropertyBindingResult(new RestMindmap(), "");
		result.rejectValue(fieldName, "error.not-specified", null, message);
		return new ValidationException(result);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/labels/maps/{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void removeLabel(@RequestBody RestLabel restLabel, @PathVariable int id) throws WiseMappingException {
		final Mindmap mindmap = findMindmapById(id);
		final User currentUser = Utils.getUser();
		final Label delegated = restLabel.getDelegated();
		assert currentUser != null;
		delegated.setCreator(currentUser);
		mindmapService.removeLabel(mindmap, delegated);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/labels/maps", consumes = { "application/xml", "application/json" })
	@ResponseStatus(value = HttpStatus.OK)
	public void addLabel(@RequestBody RestLabel restLabel, @RequestParam(required = true) String ids) throws WiseMappingException {
		int labelId = restLabel.getId();
		final User user = Utils.getUser();
		final Label delegated = restLabel.getDelegated();
		delegated.setCreator(user);
		final Label found = labelService.getLabelById(labelId, user);
		if (found == null) {
			throw new LabelCouldNotFoundException("Label could not be found. Id: " + labelId);
		}
		for (String id : ids.split(",")) {
			final int mindmapId = Integer.parseInt(id);
			final Mindmap mindmap = findMindmapById(mindmapId);
			final Label label = mindmap.findLabel(labelId);
			if (label == null) {
				mindmapService.linkLabel(mindmap, delegated);
			}
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/maps/concept/create/{idUser}/{idProject}", produces = { "application/json", "application/xml" })
	@ResponseBody
	public RestConcept conceptCreate(@PathVariable String idUser, @PathVariable String idProject, HttpServletRequest request)
			throws WiseMappingException {
		RestConcept restConceptCreate = new RestConcept();
		Mindmap mindmap = new Mindmap();
		mindmap.setTitle("Mindmap automaticaly generated " + new Date());
		mindmap.setDescription("Mindmap automaticaly generated " + new Date());
		mindmap.setIdProject(String.valueOf(Constants.ID_PROJECT_CONCEPT));
		mindmap.setXmlStr("");
		mindmap.setThumbnail("".getBytes());
		User user = Utils.getUser();
		user.setId(Constants.ID_USER_CONCEPT);
		try {
			/* save in wisemapping database. */
			// saveMindMap(request, mindmap, user, "");
			mindmap.setIdUserConcept(Integer.valueOf(idUser));
			mindmap.setIdProjectConcept(Integer.valueOf(idProject));
			mindmapService.addMindmap(mindmap, user);
			restConceptCreate.setMindMapId(mindmap.getId());
			restConceptCreate.setResult(Constants.CONCEPT_RESULT_OK);
			saveMindMap(request, mindmap);
		} catch (Exception e) {
			restConceptCreate.setResult(Constants.CONCEPT_RESULT_ERROR);
			restConceptCreate.setErrorDesc(e.getMessage());
			logger.debug(e);
			e.printStackTrace();
		}
		return restConceptCreate;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/maps/concept/update/{idUser}/{idProject}/{idMindmap}", produces = { "application/json", "application/xml" })
	public @ResponseBody RestConcept conceptUpdate(@PathVariable String idUser, @PathVariable String idProject, @PathVariable String idMindmap,
			HttpServletRequest request) throws WiseMappingException {
		RestConcept restConcept = new RestConcept();
		this.idUserUpdateConcept = Integer.valueOf(idUser);
		this.idProjectUpdateConcept = Integer.valueOf(idProject);
		try {
			Mindmap mindmap = this.findMindmapById(Integer.parseInt(idMindmap));
			MindMapVO mindMapVO = this.generateMindMapVO(request, mindmap);
			restConcept.setMindMapVO(mindMapVO);
			restConcept.setResult(Constants.CONCEPT_RESULT_OK);
		} catch (Exception e) {
			restConcept.setResult(Constants.CONCEPT_RESULT_ERROR);
			restConcept.setErrorDesc(e.getMessage());
		}
		return restConcept;
	}
}
