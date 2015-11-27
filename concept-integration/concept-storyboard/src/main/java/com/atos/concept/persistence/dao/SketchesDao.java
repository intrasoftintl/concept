package com.atos.concept.persistence.dao;

import java.util.List;

import org.hibernate.criterion.Example;

import com.atos.concept.persistence.Sketches;

public class SketchesDao extends DaoFather implements SketchesDaoInterface<Sketches, Integer> {
	
	@Override
	public void persist(Sketches sketch) {
		getCurrentSession().persist(sketch);
	}

	@Override
	public void update(Sketches sketch) {
		getCurrentSession().saveOrUpdate(sketch);
	}

	@Override
	public Sketches findById(Integer id) {
		Sketches sketches = (Sketches) getCurrentSession().get(Sketches.class, id);
		return sketches;
	}

	@Override
	public void delete(Sketches sketch) {
		getCurrentSession().delete(sketch);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Sketches> findByEntity(Sketches sketch) {
		return getCurrentSession().createCriteria(Sketches.class).add(Example.create(sketch)).list();
	}
}
