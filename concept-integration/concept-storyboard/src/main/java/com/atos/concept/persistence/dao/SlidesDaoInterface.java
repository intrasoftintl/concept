package com.atos.concept.persistence.dao;

import java.io.Serializable;
import java.util.List;

import com.atos.concept.persistence.Slides;

public interface SlidesDaoInterface<T, Id extends Serializable> {

	public void persist(T entity);
		
    public T findById(Integer id);
    
    public List<T> findByEntity(T entity);

	void delete(Slides slide);
	
	void update(Slides slide);


}
