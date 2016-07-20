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
@WebServlet(name = "PreviewNewStoryboardServlet", urlPatterns = "/storyboard/previewNew")
public class PreviewNewStoryboardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LogManager.getLogger(PreviewNewStoryboardServlet.class);
	
	public PreviewNewStoryboardServlet() {
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
		logger.debug("PreviewNewStoryboardServlet.processRequest");
		List<Slides> slides = new ArrayList<Slides>();
		String[] idSlides = req.getParameter(ConceptConstants.REQUEST_STORY_SLIDES).split("\\|");
		SlidesServices slidesServices = new SlidesServices();
		for (String idSlide : idSlides) {
			if (StringUtils.isNotEmpty(idSlide)) {
				Slides slide = slidesServices.findById(new Integer(idSlide));
				slides.add(slide);
			}
		}
		req.setAttribute(ConceptConstants.REQUEST_SLIDES, slides);
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/preview_storyboard.jsp");
		dispatcher.forward(req, resp);
	}
}
