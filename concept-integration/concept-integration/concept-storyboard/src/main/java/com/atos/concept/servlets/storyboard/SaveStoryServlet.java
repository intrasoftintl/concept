package com.atos.concept.servlets.storyboard;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.atos.concept.persistence.Slides;
import com.atos.concept.persistence.Stories;
import com.atos.concept.persistence.StoriesSlides;
import com.atos.concept.persistence.services.SlidesServices;
import com.atos.concept.persistence.services.StoriesServices;
import com.atos.concept.utilities.ConceptConstants;

@WebServlet(name = "SaveStoryServlet", urlPatterns = "/storyboard/save")
public class SaveStoryServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(SaveStoryServlet.class);
    private static final long serialVersionUID = 1L;

    public SaveStoryServlet() {
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
        logger.info("SaveStoryServlet");

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
        String storyName = req.getParameter(ConceptConstants.REQUEST_STORY_NAME);

        String[] idSlides = req.getParameter(ConceptConstants.REQUEST_STORY_SLIDES).split("\\|");

        String storyId = req.getParameter(ConceptConstants.REQUEST_STORY_ID);
        String uuidOld;
        if (StringUtils.isNotEmpty(storyId)) {
            Integer myId = new Integer(storyId);
            Stories oldStory = storiesServices.findById(myId);
            uuidOld = oldStory.getUuid();
            storiesServices.delete(myId);
        } else {
            uuidOld = UUID.randomUUID().toString();
        }
        Stories story = new Stories();
        Set<StoriesSlides> storiesSlides = generateSlidesList(story, idSlides);
        story.setStoryName(storyName);
        story.setProjectId(projectId);
        story.setUuid(uuidOld);
        story.setStoriesSlideses(storiesSlides);
        story.setCreation(new Date());
        story.setUserId(String.valueOf(userId));
        storiesServices.persist(story);
        resp.sendRedirect("view?pid=" + projectId + "&uid=" + userId);
    }

    private Set<StoriesSlides> generateSlidesList(Stories story, String[] slides) {
        SlidesServices slidesServices = new SlidesServices();
        Set<StoriesSlides> storiesSlideses = new HashSet<StoriesSlides>();
        int count = 0;
        for (String idSlide : slides) {
            if (StringUtils.isNotEmpty(idSlide)) {
                Slides slide = slidesServices.findById(new Integer(idSlide));
                StoriesSlides storiesSlides = new StoriesSlides(slide, story, count++);
                storiesSlideses.add(storiesSlides);
            }
        }
        return storiesSlideses;
    }

}
