package com.atos.concept.servlets.scene;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atos.concept.persistence.Slides;
import com.atos.concept.persistence.services.SlidesServices;
import com.atos.concept.utilities.ConceptConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Servlet implementation class SlidesServlet.
 */
@WebServlet(name = "SlidesServlet", urlPatterns = "/scene/view")
public class SlidesServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(SlidesServlet.class);
    private static final long serialVersionUID = 1L;

    public SlidesServlet() {
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
        SlidesServices slidesServices = new SlidesServices();
        int projectId = 0;
        try {
            projectId = Integer.valueOf(req.getParameter("pid"));
        } catch (NumberFormatException ex) {
            logger.error(ex.getStackTrace());
        }
       logger.info("SlidesServlet --> Project Id: " + projectId);
        Slides mySlide = new Slides();
        mySlide.setProjectId(projectId);
        List<Slides> slidesList = slidesServices.findBySlide(mySlide);
        req.setAttribute(ConceptConstants.REQUEST_SLIDES, slidesList);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/slides.jsp");
        dispatcher.forward(req, resp);
    }

}
