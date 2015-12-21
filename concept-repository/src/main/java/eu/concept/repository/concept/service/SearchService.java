package eu.concept.repository.concept.service;

import eu.concept.repository.concept.dao.MetadataRepository;
import eu.concept.repository.concept.dao.SearchRepository;
import eu.concept.repository.concept.domain.Component;
import eu.concept.repository.concept.domain.Metadata;
import eu.concept.repository.concept.domain.Search;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Service
public class SearchService {

    @Autowired
    private SearchRepository searchRepo;

    public boolean storeSearch(Search search) {
        try {

            searchRepo.save(search);
        } catch (Exception ex) {
            Logger.getLogger(SearchService.class.getName()).severe(ex.getMessage());
            return false;
        }
        return search.getId() > 0;
    }

    public boolean deleteSearch(long searchID) {
        try {
            searchRepo.delete(searchID);
        } catch (Exception ex) {
            Logger.getLogger(SearchService.class.getName()).severe(ex.getMessage());
            return false;
        }
        return true;
    }

    public Search fetchSearchById(long id) {
        return searchRepo.findOne(id);
    }

    public List<Search> fetchMetadataByCidAndComponent(int pid) {
        return searchRepo.findByPid(pid);
    }

}
