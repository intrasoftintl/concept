package com.atos.concept.persistence.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.Example;

import com.atos.concept.persistence.Stories;

public class StoriesDao extends DaoFather implements StoriesDaoInterface<Stories, Integer> {
	private static final Logger logger = LogManager.getLogger(StoriesDao.class);
	
	@Override
	public void persist(Stories story) {
		getCurrentSession().persist(story);
	}

	@Override
	public void update(Stories story) {
		getCurrentSession().saveOrUpdate(story);
	}

	@Override
	public Stories findById(Integer id) {
		Stories stories = (Stories) getCurrentSession().get(Stories.class, id);
		logger.debug("stories -->" + stories);
		return stories;
	}

	@Override
	public void delete(Stories story) {
		getCurrentSession().delete(story);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Stories> findByEntity(Stories story) {
		return getCurrentSession().createCriteria(Stories.class).add(Example.create(story)).list();
	}

}
