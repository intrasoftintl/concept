package com.atos.concept.persistence.services;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.atos.concept.persistence.Stories;
import com.atos.concept.persistence.StoriesSlides;
import com.atos.concept.persistence.dao.StoriesSlidesDao;

public class StoriesSlidesServices {

	private static StoriesSlidesDao storiesSlidesDao;
	private static final Logger logger = LogManager.getLogger(StoriesSlidesDao.class);
	
	public StoriesSlidesServices() {
		storiesSlidesDao = new StoriesSlidesDao();
	}

	public StoriesSlides findById(Integer id) {
		storiesSlidesDao.openCurrentSession();
		StoriesSlides stories = storiesSlidesDao.findById(id);
		storiesSlidesDao.closeCurrentSession();
		return stories;
	}

	public List<StoriesSlides> findBySlide(StoriesSlides storiesSlides) {
		storiesSlidesDao.openCurrentSession();
		List<StoriesSlides> list = storiesSlidesDao.findByEntity(storiesSlides);
		logger.debug(list);
		storiesSlidesDao.closeCurrentSession();
		return list;
	}

	public void delete(Integer id) {
		storiesSlidesDao.openCurrentSessionwithTransaction();
		StoriesSlides storySlide = storiesSlidesDao.findById(id);
		storiesSlidesDao.delete(storySlide);
		storiesSlidesDao.closeCurrentSessionwithTransaction();
	}
	
	public void deleteByEntity(StoriesSlides storySlide) {
		storiesSlidesDao.openCurrentSessionwithTransaction();
		storiesSlidesDao.delete(storySlide);
		storiesSlidesDao.closeCurrentSessionwithTransaction();
	}

	public void deleteByStory(Stories story) {
		storiesSlidesDao.openCurrentSessionwithTransaction();
		
		//this.findBySlide(storiesSlides)
		/*
		Stories stories = new Stories();
		stories.setId(id);
		StoriesSlides storiesSlides = new StoriesSlides();
		storiesSlides.setStories(stories);
		//this.findBySlide(storiesSlides )
		StoriesSlides storySlide = storiesSlidesDao.findById(id);
		storiesSlidesDao.delete(storySlide);
		*/
		storiesSlidesDao.closeCurrentSessionwithTransaction();
	}	
	
	
}
