package com.atos.concept.servlets.storyboard;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.atos.concept.persistence.services.StoriesServices;
import com.atos.concept.servlets.scene.NewSlideServlet;
import com.atos.concept.utilities.ConceptConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(name = "RemoveStoryServlet", urlPatterns = "/storyboard/remove")
public class RemoveStoryServlet extends HttpServlet {

        private static final Logger logger = LogManager.getLogger(RemoveStoryServlet.class);
    private static final long serialVersionUID = 1L;

    public RemoveStoryServlet() {
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

        if (!StringUtils.isEmpty(req.getParameter(ConceptConstants.REQUEST_STORY_ID))) {
            Integer id = new Integer(req.getParameter(ConceptConstants.REQUEST_STORY_ID));
            new StoriesServices().delete(id);
        }
        resp.sendRedirect("view?pid="+projectId+"&uid="+userId);
    }

}
