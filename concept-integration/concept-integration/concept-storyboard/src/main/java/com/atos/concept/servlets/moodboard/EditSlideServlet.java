package com.atos.concept.servlets.moodboard;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.atos.concept.persistence.Slides;
import com.atos.concept.persistence.services.SlidesServices;
import com.atos.concept.utilities.ConceptConstants;

/**
 * Servlet implementation class SlidesServlet
 */
@WebServlet(name = "EditSlideServlet", urlPatterns = "/moodboard/edit")
public class EditSlideServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(EditSlideServlet.class);
    private static final long serialVersionUID = 1L;

    public EditSlideServlet() {
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
        int projectId = 0;
        int userId = 0;
        try {
            projectId = Integer.valueOf(req.getParameter("pid"));
            userId = Integer.valueOf(req.getParameter("uid"));
        } catch (NumberFormatException ex) {
            logger.error(ex.getStackTrace());
        }
        logger.info("ProjectId: " + projectId + " , UserId: " + userId);

        logger.info("EditSlideServlet --> " + ((HttpServletRequest) req).getServletPath());
        String myId = req.getParameter(ConceptConstants.REQUEST_SLIDE_ID);
        SlidesServices slidesService = new SlidesServices();
        Slides slide = slidesService.findById(new Integer(myId));
        slide.setSlideText(slide.getSlideText().replaceAll("\r\n", "\\\\r\\\\n"));
        req.setAttribute(ConceptConstants.REQUEST_SLIDE, slide);
        req.setAttribute(ConceptConstants.REQUEST_IS_UPDATE, true);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/new_slide.jsp");
        dispatcher.forward(req, resp);
    }
}
