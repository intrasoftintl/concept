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

import com.atos.concept.persistence.Slides;
import com.atos.concept.persistence.services.SlidesServices;
import com.atos.concept.servlets.scene.NewSlideServlet;
import com.atos.concept.utilities.ConceptConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servlet implementation class SlidesServlet
 */
@WebServlet(name = "NewStoryBoardServlet", urlPatterns = "/storyboard/new")
public class NewStoryBoardServlet extends HttpServlet {

        private static final Logger logger = LogManager.getLogger(NewStoryBoardServlet.class);
    private static final long serialVersionUID = 1L;

    public NewStoryBoardServlet() {
        super();
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);

    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SlidesServices slidesServices = new SlidesServices();
        Slides slide = new Slides();

        int projectId = 0;
        int userId = 0;
        try {
            projectId = Integer.valueOf(req.getParameter("pid"));
            userId = Integer.valueOf(req.getParameter("uid"));
        } catch (NumberFormatException ex) {
            logger.error(ex.getStackTrace());
        }
        logger.info("ProjectId: " + projectId + " , UserId: " + userId);

        slide.setProjectId(projectId);
        List<Slides> slidesList = slidesServices.findBySlide(slide);
        req.setAttribute(ConceptConstants.REQUEST_SLIDES, slidesList);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/new_story.jsp");
        dispatcher.forward(req, resp);
    }
}
