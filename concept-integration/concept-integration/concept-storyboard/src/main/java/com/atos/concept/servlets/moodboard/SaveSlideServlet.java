package com.atos.concept.servlets.moodboard;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

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

@WebServlet(name = "SaveSlideServlet", urlPatterns = "/moodboard/save")
public class SaveSlideServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(SaveSlideServlet.class);
    private static final long serialVersionUID = 1L;

    public SaveSlideServlet() {
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
        Slides slide = new Slides();
        SlidesServices slidesService = new SlidesServices();
        String slideName = req.getParameter(ConceptConstants.REQUEST_SLIDE_NAME);
        String content = req.getParameter(ConceptConstants.REQUEST_SLIDE_TEXT) == null ? "" : req.getParameter(ConceptConstants.REQUEST_SLIDE_TEXT);
        String slideId = req.getParameter(ConceptConstants.REQUEST_SLIDE_ID);

        int projectId = 0;
        int userId = 0;
        try {
            projectId = Integer.valueOf(req.getParameter("pid"));
            userId = Integer.valueOf(req.getParameter("uid"));
        } catch (NumberFormatException ex) {
            logger.error(ex.getStackTrace());
        }
        logger.info("ProjectId: " + projectId + " , UserId: " + userId);

        slide.setSlideName(slideName);
        slide.setSlideText(content);
        slide.setProjectId(projectId);

        logger.info("SaveSlideServlet");
        /* insert or update? */
        if (StringUtils.isNotEmpty(slideId)) {
            logger.info("Update Slide with id: " + slideId);
            Integer mySlideId = new Integer(slideId);
            Slides slideAux = new Slides();
            slideAux.setSlideName(slideName);
            slideAux.setProjectId(Integer.valueOf(req.getParameter("pid")));
            List<Slides> findBySlide = slidesService.findBySlide(slideAux);
            boolean nameTaken = false;
            for (Slides slides : findBySlide) {
                if (!slides.getId().equals(mySlideId)) {
                    nameTaken = true;
                }
            }
            if (!nameTaken) {
                slide.setId(mySlideId);
                slidesService.update(slide);
                resp.sendRedirect("view?pid=" + projectId + "&uid=" + userId);
            } else {
                slide.setSlideText(slide.getSlideText().replaceAll("\r\n", "\\\\r\\\\n"));
                ResourceBundle bundle = ResourceBundle.getBundle("ext");
                req.setAttribute(ConceptConstants.REQUEST_IS_UPDATE, true);
                req.setAttribute(ConceptConstants.REQUEST_NEW_SLIDE, slide);
                req.setAttribute(ConceptConstants.REQUEST_NEW_SLIDE_ERROR, bundle.getString("new_slide.error.name"));
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/jsp/new_slide.jsp");
                dispatcher.forward(req, resp);
            }
        } else {
            if (projectId > 0) {
                logger.info("Create a new slide for project with id: " + projectId);
                Slides slideAux = new Slides();
                slideAux.setSlideName(slideName);
                slideAux.setProjectId(projectId);
                slide.setCreation(new Date());
                slidesService.persist(slide,projectId,userId);
            }

            resp.sendRedirect("view?pid=" + projectId + "&uid=" + userId);
        }
    }
}
