package eu.concept.repository.concept.service;

import eu.concept.repository.concept.dao.TagsRepository;
import eu.concept.repository.concept.domain.Tag;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Service
public class TagService {

    @Autowired
    private TagsRepository tagRepo;

    public boolean store(Tag tag) {
        try {
            tagRepo.save(tag);
        } catch (Exception ex) {
            Logger.getLogger(TagService.class.getName()).severe(ex.getMessage());
            return false;
        }
        return tag.getId() > 0;
    }

    public boolean deleteTag(int tag_id) {
        try {
            tagRepo.delete(tag_id);
        } catch (Exception ex) {
            Logger.getLogger(TagService.class.getName()).severe(ex.getMessage());
            return false;
        }
        return true;
    }

    public Tag fetchTagByPid(int project_id) {
        return tagRepo.findByPid(project_id);
    }

}
