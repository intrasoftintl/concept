package eu.concept.repository.concept.service;

import eu.concept.repository.concept.dao.BriefAnalysisRepository;
import eu.concept.repository.concept.domain.BriefAnalysis;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Service
public class BriefAnalysisService {

    @Autowired
    private BriefAnalysisRepository briefAnalysis;

    public boolean storeFile(BriefAnalysis ba) {
        try {

            System.out.println(ba.getTitle());
            System.out.println(ba.getContent());
            briefAnalysis.save(ba);
        } catch (Exception ex) {
            Logger.getLogger(BriefAnalysis.class.getName()).severe(ex.getMessage());
            return false;
        }
        return ba.getId() > 0;
    }

    public boolean deleteFile(int briefAnalysisID) {
        try {
            briefAnalysis.delete(briefAnalysisID);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

}
