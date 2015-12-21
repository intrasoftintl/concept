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

package com.wisemapping.webmvc;

import com.wisemapping.exceptions.MapCouldNotFoundException;
import com.wisemapping.exceptions.WiseMappingException;
import com.wisemapping.model.CollaborationRole;
import com.wisemapping.model.Mindmap;
import com.wisemapping.model.Projects;
import com.wisemapping.model.User;
import com.wisemapping.security.Utils;
import com.wisemapping.service.LockManager;
import com.wisemapping.service.MindmapService;
import com.wisemapping.view.MindMapBean;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@Controller
public class MindmapController {

	public static final String LOCK_SESSION_ATTRIBUTE = "lockSession";
	@Qualifier("mindmapService")
	@Autowired
	private MindmapService mindmapService;

	@RequestMapping(value = "maps/import")
	public String showImportPage() {
		return "mindmapImport";
	}

	@RequestMapping(value = "maps/{id}/details")
	public String showDetails(@PathVariable int id, @NotNull Model model, @NotNull HttpServletRequest request) throws MapCouldNotFoundException {
		final MindMapBean mindmap = findMindmapBean(id);
		model.addAttribute("mindmap", mindmap);
		model.addAttribute("baseUrl", request.getAttribute("site.baseurl"));
		return "mindmapDetail";
	}

	@RequestMapping(value = "maps/{id}/print")
	public String showPrintPage(@PathVariable int id, @NotNull Model model) throws MapCouldNotFoundException {
		final MindMapBean mindmap = findMindmapBean(id);
		model.addAttribute("principal", Utils.getUser());
		model.addAttribute("mindmap", mindmap);
		final Locale locale = LocaleContextHolder.getLocale();
		model.addAttribute("locale", locale.toString().toLowerCase());
		return "mindmapPrint";
	}

	@RequestMapping(value = "maps/{id}/export")
	public String showExportPage(@PathVariable int id, @NotNull Model model) throws IOException, MapCouldNotFoundException {
		final Mindmap mindmap = findMindmap(id);
		model.addAttribute("mindmap", mindmap);
		return "mindmapExport";
	}

	@RequestMapping(value = "maps/{id}/exportf")
	public String showExportPageFull(@PathVariable int id, @NotNull Model model) throws IOException, MapCouldNotFoundException {
		showExportPage(id, model);
		return "mindmapExportFull";
	}

	@RequestMapping(value = "maps/{id}/share")
	public String showSharePage(@PathVariable int id, @NotNull Model model) throws MapCouldNotFoundException {
		final Mindmap mindmap = findMindmap(id);
		model.addAttribute("mindmap", mindmap);
		return "mindmapShare";
	}

	@RequestMapping(value = "maps/{id}/sharef")
	public String showSharePageFull(@PathVariable int id, @NotNull Model model) throws MapCouldNotFoundException {
		showSharePage(id, model);
		return "mindmapShareFull";
	}

	@RequestMapping(value = "maps/{id}/publish")
	public String showPublishPage(@PathVariable int id, @NotNull Model model, @NotNull HttpServletRequest request) throws MapCouldNotFoundException {
		final Mindmap mindmap = findMindmap(id);
		model.addAttribute("mindmap", mindmap);
		model.addAttribute("baseUrl", request.getAttribute("site.baseurl"));
		return "mindmapPublish";
	}

	@RequestMapping(value = "maps/{id}/publishf")
	public String showPublishPageFull(@PathVariable int id, @NotNull Model model, @NotNull HttpServletRequest request)
			throws MapCouldNotFoundException {
		showPublishPage(id, model, request);
		return "mindmapPublishFull";
	}

	@RequestMapping(value = "maps/{id}/history", method = RequestMethod.GET)
	public String showHistoryPage(@PathVariable int id, @NotNull Model model) {
		model.addAttribute("mindmapId", id);
		return "mindmapHistory";
	}

	@RequestMapping(value = "maps/{id}/historyf", method = RequestMethod.GET)
	public String showHistoryPageFull(@PathVariable int id, @NotNull Model model) {
		showHistoryPage(id, model);
		return "mindmapHistoryFull";
	}

	@RequestMapping(value = "maps/")
	public String showListPage(@NotNull Model model) {
		final Locale locale = LocaleContextHolder.getLocale();
		// @Todo: This should be more flexible ...
		String localeStr = locale.toString().toLowerCase();
		if ("es".equals(locale.getLanguage()) || "pt".equals(locale.getLanguage())) {
			localeStr = locale.getLanguage();
		}
		model.addAttribute("locale", localeStr);
		List<Projects> allProjects = mindmapService.getAllProjects();
		model.addAttribute("projects", allProjects);
		return "mindmapList";
	}

	@RequestMapping(value = "maps/{id}/edit", method = RequestMethod.GET)
	public String showMindmapEditorPage(@PathVariable int id, @NotNull Model model) throws WiseMappingException {
		return showEditorPage(id, model, true, null);
	}
	
	@RequestMapping(value = "maps/{id}/{idUser}/edit", method = RequestMethod.GET)
	public String showMindmapEditorPageConcept(@PathVariable int id, @PathVariable int idUser, @NotNull Model model) throws WiseMappingException {
		return showEditorPage(id, model, true, (long)idUser);
	}

	private String showEditorPage(int id, @NotNull final Model model, boolean requiresLock, Long userId) throws WiseMappingException {
		final MindMapBean mindmapBean = findMindmapBean(id);
		final Mindmap mindmap = mindmapBean.getDelegated();
		final User collaborator = Utils.getUser();
		final Locale locale = LocaleContextHolder.getLocale();

		// Is the mindmap locked ?.
		boolean isLocked = false;
		boolean readOnlyMode = !requiresLock || !mindmap.hasPermissions(collaborator, CollaborationRole.EDITOR);
		if (!readOnlyMode) {
			final LockManager lockManager = this.mindmapService.getLockManager();
			if (lockManager.isLocked(mindmap) && !lockManager.isLockedBy(mindmap, collaborator)) {
				readOnlyMode = true;
				isLocked = true;
			} else {
				model.addAttribute("lockTimestamp", mindmap.getLastModificationTime().getTimeInMillis());
				model.addAttribute(LOCK_SESSION_ATTRIBUTE, lockManager.generateSession());
			}
			model.addAttribute("lockInfo", lockManager.getLockInfo(mindmap));
		}
		// Set render attributes ...
		model.addAttribute("mindmap", mindmapBean);

		// Configure default locale for the editor ...
		model.addAttribute("userId", userId!=null?userId:collaborator.getId());
		model.addAttribute("locale", locale.toString().toLowerCase());
		model.addAttribute("principal", collaborator);
		model.addAttribute("readOnlyMode", readOnlyMode);
		model.addAttribute("memoryPersistence", false);
		model.addAttribute("mindmapLocked", isLocked);

		return "mindmapEditor";
	}

	@RequestMapping(value = "maps/{id}/view", method = RequestMethod.GET)
	public String showMindmapViewerPage(@PathVariable int id, @NotNull Model model) throws WiseMappingException {
		return showEditorPage(id, model, false, null);
	}

	@RequestMapping(value = "maps/{id}/try", method = RequestMethod.GET)
	public String showMindmapTryPage(@PathVariable int id, @NotNull Model model) throws WiseMappingException {
		final String result = showEditorPage(id, model, false, null);
		model.addAttribute("memoryPersistence", true);
		model.addAttribute("readOnlyMode", false);
		return result;
	}

	@RequestMapping(value = "maps/{id}/{hid}/view", method = RequestMethod.GET)
	public String showMindmapViewerRevPage(@PathVariable int id, @PathVariable int hid, @NotNull Model model) throws WiseMappingException {

		final String result = showMindmapEditorPage(id, model);
		model.addAttribute("readOnlyMode", true);
		model.addAttribute("hid", String.valueOf(hid));
		return result;
	}

	@RequestMapping(value = "maps/{id}/embed")
	public ModelAndView showEmbeddedPage(@PathVariable int id, @RequestParam(required = false) Float zoom) throws MapCouldNotFoundException {
		ModelAndView view;
		final MindMapBean mindmap = findMindmapBean(id);
		view = new ModelAndView("mindmapEmbedded", "mindmap", mindmap);
		view.addObject("zoom", zoom == null ? 1 : zoom);
		final Locale locale = LocaleContextHolder.getLocale();
		view.addObject("locale", locale.toString().toLowerCase());
		return view;
	}

	@RequestMapping(value = "maps/{id}/public", method = RequestMethod.GET)
	public String showPublicViewPage(@PathVariable int id, @NotNull Model model) throws WiseMappingException {
		return this.showPrintPage(id, model);
	}

	@Deprecated
	@RequestMapping(value = "publicView", method = RequestMethod.GET)
	public String showPublicViewPageLegacy(@RequestParam(required = true) int mapId) {
		return "redirect:maps/" + mapId + "/public";
	}

	@Deprecated
	@RequestMapping(value = "embeddedView", method = RequestMethod.GET)
	public String showPublicViewLegacyPage(@RequestParam(required = true) int mapId, @RequestParam(required = false) int zoom) {
		return "redirect:maps/" + mapId + "/embed?zoom=" + zoom;
	}

	@NotNull
	private Mindmap findMindmap(long mapId) throws MapCouldNotFoundException {
		final Mindmap result = mindmapService.findMindmapById((int) mapId);
		if (result == null) {
			throw new MapCouldNotFoundException("Map could not be found " + mapId);
		}
		return result;

	}

	@NotNull
	private MindMapBean findMindmapBean(long mapId) throws MapCouldNotFoundException {
		final Mindmap mindmap = findMindmap(mapId);
		return new MindMapBean(mindmap, Utils.getUser());
	}
}
