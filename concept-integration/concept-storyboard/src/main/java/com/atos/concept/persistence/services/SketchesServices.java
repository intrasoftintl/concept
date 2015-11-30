package com.atos.concept.persistence.services;

import java.util.List;

import com.atos.concept.persistence.Sketches;
import com.atos.concept.persistence.dao.SketchesDao;

public class SketchesServices {

	private static SketchesDao sketchesDao;

	public void persist(Sketches entity) {
		sketchesDao.openCurrentSessionwithTransaction();
		sketchesDao.persist(entity);
		sketchesDao.closeCurrentSessionwithTransaction();
	}

	public SketchesServices() {
		sketchesDao = new SketchesDao();
	}

	public List<Sketches> findBySketches(Sketches sketche) {
		sketchesDao.openCurrentSession();
		List<Sketches> sketches = sketchesDao.findByEntity(sketche);
		sketchesDao.closeCurrentSession();
		return sketches;
	}
}
