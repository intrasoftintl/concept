package com.atos.concept.servlets.moodboard;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.atos.concept.persistence.Slides;
import com.atos.concept.persistence.services.SlidesServices;
import com.atos.concept.utilities.ConceptConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servlet implementation class SlidesServlet.
 */
@WebServlet(name = "PreviewSlideServlet", urlPatterns = "/moodboard/preview")
public class PreviewSlideServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(PreviewSlideServlet.class);
    private static final long serialVersionUID = 1L;

    public PreviewSlideServlet() {
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

    protected void processRequest(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {

        int projectId = 0;
        int userId = 0;
        try {
            projectId = Integer.valueOf(req.getParameter("pid"));
            userId = Integer.valueOf(req.getParameter("uid"));
        } catch (NumberFormatException ex) {
            logger.error(ex.getStackTrace());
        }
        logger.info("ProjectId: " + projectId + " , UserId: " + userId);

        String myStr = "";
        if (!StringUtils.isEmpty(req.getParameter(ConceptConstants.REQUEST_SLIDE_TEXT))) {
            myStr = req.getParameter(ConceptConstants.REQUEST_SLIDE_TEXT);
        } else if (!StringUtils.isEmpty(req.getParameter(ConceptConstants.REQUEST_SLIDE_ID))) {
            Integer id = new Integer(req.getParameter(ConceptConstants.REQUEST_SLIDE_ID));
            SlidesServices slidesServices = new SlidesServices();
            Slides slide = slidesServices.findById(id);
            if (slide != null) {
                myStr = slide.getSlideText();
            }
        } else {
            myStr = req.getParameter("content");
            if (StringUtils.isEmpty(myStr)) {
                myStr = "<h1> Testing StoryBoard</h1><br><h2>Slide n1</h2>";
            }
        }
        req.setAttribute("SLIDE_CONTENT", myStr);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/preview_slide.jsp");
        dispatcher.forward(req, response);
    }

}
