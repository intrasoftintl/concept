package com.atos.concept.persistence.services;

import java.util.List;

import com.atos.concept.persistence.Slides;
import com.atos.concept.persistence.dao.SlidesDao;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SlidesServices {

    private static final Logger logger = LogManager.getLogger(SlidesServices.class);
    private static SlidesDao slidesDao;

    public SlidesServices() {
        slidesDao = new SlidesDao();
    }

    public void persist(Slides entity, int projectId, int userId) {
        slidesDao.openCurrentSessionwithTransaction();
        if (entity.getSlideText() == null) {
            entity.setSlideText("");
        }
        slidesDao.persist(entity);
        slidesDao.closeCurrentSessionwithTransaction();

        //Strore slide to Moodboard @COnCEPT database
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.post("http://concept.euprojects.net/conceptRest/api/moodboard/replicate/")
                    .queryString("id", entity.getId())
                    .field("uid", userId)
                    .field("pid", projectId)
                    .field("title", entity.getSlideName())
                    .field("content", entity.getSlideText())
                    .field("content_thumbnail", entity.getSlideText())
                    .asJson();
            logger.info("RESPONSE---> " + jsonResponse.getBody().toString());
        } catch (UnirestException ex) {
            ex.printStackTrace();
        }

    }

    public Slides findById(Integer id) {
        slidesDao.openCurrentSession();
        Slides slides = slidesDao.findById(id);
        slidesDao.closeCurrentSession();
        return slides;
    }

    public List<Slides> findBySlide(Slides slide) {
        slidesDao.openCurrentSession();
        List<Slides> slides = slidesDao.findByEntity(slide);
        slidesDao.closeCurrentSession();
        return slides;
    }

    public void delete(Integer id) {
        slidesDao.openCurrentSessionwithTransaction();
        Slides slide = slidesDao.findById(id);
        slidesDao.delete(slide);
        slidesDao.closeCurrentSessionwithTransaction();
    }

    public void update(Slides entity) {
        slidesDao.openCurrentSessionwithTransaction();
        if (entity.getSlideText() == null) {
            entity.setSlideText("");
        }
        slidesDao.update(entity);
        slidesDao.closeCurrentSessionwithTransaction();
    }

}
