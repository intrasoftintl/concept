package eu.concept.repository.concept.service;

import eu.concept.repository.concept.dao.MetadataRepository;
import eu.concept.repository.concept.domain.Metadata;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Service
public class MetadataService {

    @Autowired
    private MetadataRepository metadata;

    public boolean deleteMetadata(long metadataID) {
        try {
            metadata.delete(metadataID);
        } catch (Exception ex) {
            Logger.getLogger(MetadataService.class.getName()).severe(ex.getMessage());
            return false;
        }
        return true;
    }

    public Metadata fetchMetadataById(long id) {
        return metadata.findOne(id);
    }

}
