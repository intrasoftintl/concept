package com.atos.concept.persistence.dao;

import java.util.List;

import org.hibernate.criterion.Example;

import com.atos.concept.persistence.Slides;

public class SlidesDao extends DaoFather implements SlidesDaoInterface<Slides, Integer> {

	public SlidesDao() {
	}

	@Override
	public void persist(Slides entity) {
		getCurrentSession().save(entity);
	}

	@Override
	public Slides findById(Integer id) {
		Slides slides = (Slides) getCurrentSession().get(Slides.class, id);
		return slides;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Slides> findByEntity(Slides slide) {
		return getCurrentSession().createCriteria(Slides.class).add(Example.create(slide)).list();
	}

	@Override
	public void delete(Slides slide) {
		getCurrentSession().delete(slide);
	}

	@Override
	public void update(Slides slide) {
		getCurrentSession().saveOrUpdate(slide);

	}

}
