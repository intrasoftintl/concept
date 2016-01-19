package eu.concept.repository.concept.dao;

import eu.concept.repository.concept.domain.BAComment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public interface BACommentRepository extends JpaRepository<BAComment, Integer> {

}
