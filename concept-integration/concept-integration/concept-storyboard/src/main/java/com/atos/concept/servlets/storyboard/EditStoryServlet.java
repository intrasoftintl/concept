package com.atos.concept.servlets.storyboard;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.atos.concept.persistence.Slides;
import com.atos.concept.persistence.Stories;
import com.atos.concept.persistence.services.SlidesServices;
import com.atos.concept.persistence.services.StoriesServices;
import com.atos.concept.utilities.ConceptConstants;

/**
 * Servlet implementation class StoryServlet
 */
@WebServlet(name = "EditStoryServlet", urlPatterns = "/storyboard/edit")
public class EditStoryServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(EditStoryServlet.class);
    private static final long serialVersionUID = 1L;

    public EditStoryServlet() {
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
        logger.info("EditStoryServlet --> " + ((HttpServletRequest) req).getServletPath());

        int projectId = 0;
        int userId = 0;
        try {
            projectId = Integer.valueOf(req.getParameter("pid"));
            userId = Integer.valueOf(req.getParameter("uid"));
        } catch (NumberFormatException ex) {
            logger.error(ex.getStackTrace());
        }
        logger.info("ProjectId: " + projectId + " , UserId: " + userId);
        
        String myId = req.getParameter(ConceptConstants.REQUEST_STORY_ID);
        StoriesServices storiesService = new StoriesServices();
        Stories story = storiesService.findById(new Integer(myId));
        req.setAttribute(ConceptConstants.REQUEST_STORY, story);
        req.setAttribute(ConceptConstants.REQUEST_IS_UPDATE, true);

        List<Slides> slides = storiesService.findSlides(story.getId());
        req.setAttribute(ConceptConstants.REQUEST_SLIDES_ORDERED, slides);

        /* select the list of slides. */
        SlidesServices slidesServices = new SlidesServices();
        Slides slide = new Slides();

        slide.setProjectId(projectId);
        List<Slides> slidesList = slidesServices.findBySlide(slide);
        req.setAttribute(ConceptConstants.REQUEST_SLIDES, slidesList);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/new_story.jsp");
        dispatcher.forward(req, resp);
    }
}
