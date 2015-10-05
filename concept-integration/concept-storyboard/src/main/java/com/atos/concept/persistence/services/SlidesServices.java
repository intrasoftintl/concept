package com.atos.concept.persistence.services;

import java.util.List;

import com.atos.concept.persistence.Slides;
import com.atos.concept.persistence.dao.SlidesDao;

public class SlidesServices {

	private static SlidesDao slidesDao;

	public SlidesServices() {
		slidesDao = new SlidesDao();
	}

	public void persist(Slides entity) {
		slidesDao.openCurrentSessionwithTransaction();
		if (entity.getSlideText()==null) {
			entity.setSlideText("");
		}
		slidesDao.persist(entity);
		slidesDao.closeCurrentSessionwithTransaction();
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
		if (entity.getSlideText()==null) {
			entity.setSlideText("");
		}
		slidesDao.update(entity);
		slidesDao.closeCurrentSessionwithTransaction();
	}
	
	
}
