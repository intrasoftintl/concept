package eu.concept.repository.openproject.service;


import eu.concept.repository.openproject.dao.ProjectOpRepository;
import eu.concept.repository.openproject.domain.ProjectOp;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Christos Paraskeva
 */
@Service
@Transactional
public class ProjectService {

    @Autowired
    ProjectOpRepository projectRepo;

    /**
     *
     * @return
     */
    public List<ProjectOp> findAllProjects() {
        return projectRepo.findAll();
    }

    /**
     *
     * @param projectID
     * @return
     */
    public ProjectOp findProjectByID(int projectID) {
        return projectRepo.findOne(projectID);
    }

}
