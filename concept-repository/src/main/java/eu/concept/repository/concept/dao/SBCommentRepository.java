package eu.concept.repository.concept.dao;

import eu.concept.repository.concept.domain.SBComment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public interface SBCommentRepository extends JpaRepository<SBComment, Integer> {

}
