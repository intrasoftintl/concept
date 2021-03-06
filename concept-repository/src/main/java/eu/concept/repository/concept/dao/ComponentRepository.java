package eu.concept.repository.concept.dao;

import eu.concept.repository.concept.domain.Component;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public interface ComponentRepository extends JpaRepository<Component, Integer> {
    
}
