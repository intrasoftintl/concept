package com.atos.concept.servlets.scene;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.atos.concept.persistence.Sketches;
import com.atos.concept.persistence.services.SketchesServices;


/**
 * Servlet implementation class SlidesServlet
 */
@WebServlet(name = "NewSlideServlet", urlPatterns = "/scene/new")
public class NewSlideServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(NewSlideServlet.class);
    private static final long serialVersionUID = 1L;

    public NewSlideServlet() {
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
        /* storing sketches on the request */
		Sketches sketch = new Sketches();		
		sketch.setIdProject(projectId);
		req.setAttribute("sketches", sketchesService.findBySketches(sketch));
		
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/new_slide.jsp");
        System.out.println("I am in servlet new slid, pid is: " + req.getParameter("pid"));
        dispatcher.forward(req, response);
    }
}
