package eu.concept.repository.concept.dao;

import eu.concept.repository.concept.domain.Component;
import eu.concept.repository.concept.domain.Metadata;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public interface MetadataRepository extends JpaRepository<Metadata, Long> {

    public Metadata findByCidAndComponent(int cid, Component component);

    @Query("select m from Metadata m")
    Stream<Metadata> findAllByCustomQueryAndStream();
}
