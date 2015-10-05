package com.atos.concept.persistence.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.Example;

import com.atos.concept.persistence.StoriesSlides;

public class StoriesSlidesDao extends DaoFather implements StoriesDaoInterface<StoriesSlides, Integer> {
	private static final Logger logger = LogManager.getLogger(StoriesSlidesDao.class);
	
	@Override
	public void persist(StoriesSlides t) {
		getCurrentSession().save(t);
	}

	@Override
	public void update(StoriesSlides t) {
		getCurrentSession().saveOrUpdate(t);
	}

	@Override
	public StoriesSlides findById(Integer id) {
		StoriesSlides stories = (StoriesSlides) getCurrentSession().get(StoriesSlides.class, id);
		logger.debug("stories -->" + stories);
		return stories;
	}

	@Override
	public void delete(StoriesSlides t) {
		getCurrentSession().delete(t);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<StoriesSlides> findByEntity(StoriesSlides t) {
		return getCurrentSession().createCriteria(StoriesSlides.class).add(Example.create(t)).list();
	}

}
