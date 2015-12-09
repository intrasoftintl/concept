package com.atos.concept.servlets.storyboard;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.atos.concept.persistence.Stories;
import com.atos.concept.persistence.services.StoriesServices;
import com.atos.concept.utilities.ConceptConstants;

/**
 * Servlet implementation class SlidesServlet
 */
@WebServlet(name = "StoriesServlet", urlPatterns = "/storyboard/view")
public class StoriesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LogManager.getLogger(StoriesServlet.class);

    public StoriesServlet() {
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
        logger.debug("StoriesServlet ");

        int projectId = 0;
        int userId = 0;
        try {
            projectId = Integer.valueOf(req.getParameter("pid"));
            userId = Integer.valueOf(req.getParameter("uid"));
        } catch (NumberFormatException ex) {
            logger.error(ex.getStackTrace());
        }
        logger.info("ProjectId: " + projectId + " , UserId: " + userId);

        StoriesServices storiesServices = new StoriesServices();
        Stories story = new Stories();
        story.setProjectId(projectId);
//        story.setUserId(userId);
        List<Stories> findBySlide = storiesServices.findBySlide(story);
        logger.debug(findBySlide.size());
        req.setAttribute(ConceptConstants.REQUEST_STORIES, findBySlide);
        req.getRequestDispatcher("/WEB-INF/jsp/stories.jsp").forward(req, resp);
    }

}
