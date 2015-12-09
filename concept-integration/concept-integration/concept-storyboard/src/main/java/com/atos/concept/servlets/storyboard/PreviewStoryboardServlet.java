package com.atos.concept.servlets.storyboard;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.atos.concept.persistence.Slides;
import com.atos.concept.persistence.services.StoriesServices;
import com.atos.concept.utilities.ConceptConstants;

/** Servlet implementation class SlidesServlet. */
@WebServlet(name = "PreviewStoryboardServlet", urlPatterns = "/storyboard/preview")
public class PreviewStoryboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(PreviewStoryboardServlet.class);
	
	public PreviewStoryboardServlet() {
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
		logger.debug("PreviewStoryboardServlet. processRequest");
		// RequestDispatcher dispatcher =
		// getServletContext().getRequestDispatcher("/jsps/preview_slide.jsp");
		StoriesServices storiesService = new StoriesServices();
		Integer myId = new Integer(req.getParameter(ConceptConstants.REQUEST_STORY_ID));
		List<Slides> slides = storiesService.findSlides(myId);
		req.setAttribute(ConceptConstants.REQUEST_SLIDES, slides);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/preview_storyboard.jsp");
		dispatcher.forward(req, resp);
	}

}
