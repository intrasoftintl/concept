package com.atos.concept.servlets.storyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.atos.concept.persistence.Slides;
import com.atos.concept.persistence.services.SlidesServices;
import com.atos.concept.utilities.ConceptConstants;

/** Servlet implementation class SlidesServlet. */
@WebServlet(name = "PreviewStoryboardUnsavedServlet", urlPatterns = "/storyboard/previewunsaved")
public class PreviewStoryboardUnsavedServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(PreviewStoryboardUnsavedServlet.class);
	
	public PreviewStoryboardUnsavedServlet() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processRequest(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processRequest(req, resp);
	}

	protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		logger.debug("PreviewStoryboardUnsavedServlet. processRequest");
		SlidesServices slidesServices = new SlidesServices();
		String[] idSlides = req.getParameterValues(ConceptConstants.REQUEST_STORY_SLIDES);
		List<Slides> slides = new ArrayList<Slides>();
		for (String myId : idSlides) {
			if (StringUtils.isNotEmpty(myId)) {
				slides.add(slidesServices.findById(new Integer(myId)));
			}
		}
		req.setAttribute(ConceptConstants.REQUEST_SLIDES, slides);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/preview_storyboard.jsp");
		dispatcher.forward(req, resp);
	}

}
